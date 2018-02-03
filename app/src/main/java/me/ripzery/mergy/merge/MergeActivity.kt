package me.ripzery.mergy.merge

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
import me.ripzery.mergy.ScalableLayout
import me.ripzery.mergy.extensions.toast
import me.ripzery.mergy.network.DataProvider
import org.jetbrains.anko.coroutines.experimental.bg


class MergeActivity : AppCompatActivity(), PositionManagerInterface.View, BackgroundImageGroup.OnImageSelectedListener {
    private val mPosition
        get() = PositionManager(this).getPosition()
    private val mSticker by lazy {
        MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
    }
    private var mMenuSave: MenuItem? = null
    private var mMenuCancel: MenuItem? = null
    private lateinit var mGalleryFragment: GalleryFragment
    private lateinit var mBitmapBG: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)
        initInstance()
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
            R.id.menu_save -> {
                showSave()
                showSaveLoading()
                setPhotoAlpha(0.7f)
                async(UI) {
                    val bgTask = bg {
                        val newBitmap = merge(mBitmapBG, mSticker)
                        saveToDevice(newBitmap)
                        newBitmap
                    }
                    changeBackground(bgTask.await())
                    setPhotoAlpha(1.0f)
                    toast("Saved image successfully.")
                    showSave()
                }
            }
            R.id.menu_cancel -> {
                showCancel()
                scalableLayout.visibility = View.VISIBLE
                changeBackground(mBitmapBG)
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
        mMenuCancel?.isEnabled = true
    }

    private fun showSave() {
        mMenuSave?.isVisible = false
        mMenuCancel?.isVisible = true
        mMenuCancel?.isEnabled = false
    }

    private fun showCancel() {
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

    override fun onBackgroundSelected(bg: Bitmap) {
        mBitmapBG = bg
        changeBackground(bg)
        scalableLayout.visibility = View.VISIBLE
        mMenuSave?.isVisible = true
        mMenuCancel?.isVisible = false
    }
}
