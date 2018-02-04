package me.ripzery.mergy.ui.merge

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_merge.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import me.ripzery.bitmapkeeper.BitmapKeeper
import me.ripzery.bitmapmerger.BitmapMerger
import me.ripzery.mergy.R
import me.ripzery.mergy.viewgroups.ScalableLayout
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.extensions.toast
import me.ripzery.mergy.helpers.PositionManager
import me.ripzery.mergy.helpers.PositionManagerInterface
import me.ripzery.mergy.network.DataProvider
import me.ripzery.mergy.network.Response
import me.ripzery.mergy.ui.merge.gallery.GalleryFragment
import me.ripzery.mergy.ui.share.ShareActivity
import me.ripzery.mergy.viewgroups.BackgroundImageGroup
import org.jetbrains.anko.coroutines.experimental.bg


class MergeActivity : AppCompatActivity(), PositionManagerInterface.View, BackgroundImageGroup.OnImageSelectedListener {
    private val mPosition
        get() = PositionManager(this).getPosition()
    private val mSticker by lazy {
        MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
    }
    private var mCurrentMergedImage: Uri? = null
    private var mMenuShare: MenuItem? = null
    private var mMenuSave: MenuItem? = null
    private var mMenuCancel: MenuItem? = null
    private var mCurrentPhoto: Response.Photo? = null
    private lateinit var mGalleryFragment: GalleryFragment
    private lateinit var mBitmapBG: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)
        initInstance()
    }

    override fun onStop() {
        DataProvider.unsubscribe()
        super.onStop()
    }


    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Merge Image"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /* Initialize background and sticker images */
        mBitmapBG = BitmapFactory.decodeResource(resources, R.drawable.bg)
        changeBackground(mBitmapBG)
        scalableLayout.setImage(mSticker)

        /* Initialize gallery layout */
        DataProvider.retrievesPhotos {
            mGalleryFragment = GalleryFragment.newInstance(it)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.galleryContainer, mGalleryFragment)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_merge, menu)
        mMenuSave = menu?.findItem(R.id.menu_save)
        mMenuCancel = menu?.findItem(R.id.menu_cancel)
        mMenuShare = menu?.findItem(R.id.menu_share)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
            R.id.menu_save -> {
                showCancel()
                showSaveLoading()
                mMenuCancel?.isEnabled = false
                setPhotoAlpha(0.7f)
                async(UI) {
                    val bgTask = bg {
                        val newBitmap = merge(mBitmapBG, mSticker)
                        mCurrentMergedImage = saveToDevice(newBitmap)
                        newBitmap
                    }
                    changeBackground(bgTask.await())
                    mMenuCancel?.isEnabled = true
                    mMenuShare?.isVisible = true
                    showCancel()
                    hideSaveLoading()
                    setPhotoAlpha(1.0f)
                    toast("Saved image successfully.")
                }
            }
            R.id.menu_cancel -> {
                showSave()
                scalableLayout.visibility = View.VISIBLE
                changeBackground(mBitmapBG)
                mMenuShare?.isVisible = false
            }
            R.id.menu_share -> {
                if (mCurrentMergedImage != null) {
                    val intent = Intent(this, ShareActivity::class.java)
                    intent.putExtra("result", mCurrentMergedImage!!.toString())
                    intent.putExtra("photo", mCurrentPhoto)
                    startActivity(intent)
                } else {
                    logd("The user hasn't merge an image yet.")
                }
            }
        }
        return true
    }

    private fun showSaveLoading() {
        scalableLayout.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideSaveLoading() {
        progressBar?.visibility = View.GONE
    }

    private fun showCancel() {
        mMenuSave?.isVisible = false
        mMenuCancel?.isVisible = true
    }

    private fun showSave() {
        mMenuSave?.isVisible = true
        mMenuCancel?.isVisible = false
    }

    private fun changeBackground(bitmap: Bitmap) {
        Glide.with(this).load(bitmap).into(ivPhoto)
    }

    private fun setPhotoAlpha(alpha: Float) {
        ivPhoto.alpha = alpha
    }

    private fun merge(background: Bitmap, sticker: Bitmap): Bitmap {
        val bitmapMerger = BitmapMerger(background, sticker)
        val newBitmap = bitmapMerger.merge(this@MergeActivity, mPosition)
        return newBitmap
    }

    private fun saveToDevice(bitmap: Bitmap): Uri {
        val bitmapKeeper = BitmapKeeper(bitmap)
        val savedImageUri = bitmapKeeper.save(this)
        return savedImageUri
    }

    /* Override interface */
    override fun getContainer(): View = container

    override fun getImageDrawable(): Drawable = ivPhoto.drawable
    override fun getStickerLayout(): ScalableLayout = scalableLayout

    override fun onBackgroundSelected(bg: Bitmap, photo: Response.Photo) {
        mBitmapBG = bg
        mCurrentPhoto = photo
        changeBackground(bg)
        scalableLayout.visibility = View.VISIBLE
        mMenuSave?.isVisible = true
        mMenuCancel?.isVisible = false
    }
}
