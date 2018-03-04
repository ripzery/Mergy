package me.ripzery.warpcan.ui.merge

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_merge.*
import kotlinx.android.synthetic.main.layout_merge_preview.*
import me.ripzery.bitmapkeeper.BitmapKeeper
import me.ripzery.bitmapmerger.BitmapMerger
import me.ripzery.warpcan.R
import me.ripzery.warpcan.extensions.toast
import me.ripzery.warpcan.helpers.Base64Helper
import me.ripzery.warpcan.helpers.PositionManager
import me.ripzery.warpcan.helpers.PositionManagerInterface
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response
import me.ripzery.warpcan.ui.custom.IsetanDialog
import me.ripzery.warpcan.ui.custom.RetryViewModel
import me.ripzery.warpcan.ui.merge.gallery.GalleryFragment
import me.ripzery.warpcan.ui.merge.gallery.GalleryViewModel
import me.ripzery.warpcan.ui.share.ShareActivity
import me.ripzery.warpcan.viewgroups.ScalableLayout


class MergeActivity : AppCompatActivity(), PositionManagerInterface.View, MergeContract.View {
    private val mPosition
        get() = PositionManager(this).getPosition()
    private val mSticker by lazy {
        MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
    }
    private val mGalleryViewModel: GalleryViewModel by lazy { ViewModelProviders.of(this).get(GalleryViewModel::class.java) }
    private val mRetryViewModel: RetryViewModel by lazy { ViewModelProviders.of(this).get(RetryViewModel::class.java) }
    private val mMergePresenter: MergeContract.Presenter by lazy { MergePresenter(this) }
    private var mCurrentMergedImage: Uri? = null
    private var mCurrentPhoto: Response.Photo? = null
    private lateinit var mGalleryFragment: GalleryFragment
    private lateinit var mBitmapBG: Bitmap

    companion object {
        const val CURRENT_PHOTO = "CURRENT_PHOTO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)
        initInstance()
    }

    private fun initInstance() {
        /* Initialize background and sticker images */
        mBitmapBG = BitmapFactory.decodeResource(resources, R.drawable.default_background)
        setBackground(mBitmapBG)
        scalableLayout.setImage(mSticker)

        /* Initialize gallery layout */
        mGalleryFragment = GalleryFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.galleryContainer, mGalleryFragment)
                .commit()

        observeBackgroundChanged()

        btnSave.setOnClickListener {
            mMergePresenter.handleSaveClicked(mBitmapBG, mSticker, mCurrentPhoto!!)
        }

        btnBack.setOnClickListener { finish() }

        mRetryViewModel.subscribeRequesting().observe(this, Observer {
            mMergePresenter.handleSaveClicked(mBitmapBG, mSticker, mCurrentPhoto!!)
        })
    }

    private fun observeBackgroundChanged() {
        mGalleryViewModel.subscribeBitmap().observe(this, Observer {
            mMergePresenter.handleBackgroundChanged(it!!)
        })

        mGalleryViewModel.subscribePhoto().observe(this, Observer {
            mCurrentPhoto = it
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
            R.id.menu_cancel -> mMergePresenter.handleCancelClicked(mBitmapBG)
            R.id.menu_share -> {
                if (mCurrentMergedImage != null && mCurrentPhoto != null) {
                } else {
                    toast("Please merge image first.")
                }
            }
        }
        return true
    }

    private fun startShareActivity(responseUpload: Response.Upload) {
        val intent = Intent(this, ShareActivity::class.java)
        intent.putExtra("result", mCurrentMergedImage!!.toString())
        intent.putExtra("photo", mCurrentPhoto)
        intent.putExtra("uploadResponse", responseUpload)
        startActivity(intent)
    }

    override fun encryptBase64(callback: (String) -> Unit) {
        Base64Helper.encrypt(this, mCurrentMergedImage!!) {
            callback(it)
        }
    }

    override fun setBitmap(bitmap: Bitmap) {
        mBitmapBG = bitmap
    }

    override fun setBackground(bitmap: Bitmap) {
        Glide.with(this).load(bitmap).into(ivPhoto)
    }

    override fun setPhotoAlpha(alpha: Float) {
        ivPhoto.alpha = alpha
    }

    override fun merge(background: Bitmap, sticker: Bitmap): Bitmap {
        return BitmapMerger(background, sticker).merge(this@MergeActivity, mPosition)
    }

    override fun saveToDevice(bitmap: Bitmap): Uri {
        return BitmapKeeper(bitmap).save(this)
    }

    override fun setSaveVisibility(visible: Boolean) {
//        btnSave?.isVisible = visible
    }

    override fun setCancelVisibility(visible: Boolean) {
//        mMenuCancel?.isVisible = visible
    }

    override fun setShareVisibility(visible: Boolean) {
//        mMenuShare?.isVisible = visible
    }

    override fun setLoadingVisibility(visible: Boolean) {
        progressBar?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setScalableViewVisibility(visible: Boolean) {
        scalableLayout?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun setSaveEnabled(enable: Boolean) {
        btnSave?.isEnabled = enable
    }

    override fun setCancelEnabled(enable: Boolean) {
//        mMenuCancel?.isEnabled = enable
    }

    override fun setShareEnabled(enable: Boolean) {
//        mMenuShare?.isEnabled = enable
    }

    override fun setMergedImageUri(uri: Uri) {
        mCurrentMergedImage = uri
    }

    override fun showUploadingMessage() {
        toast("Uploading to server...", Toast.LENGTH_LONG)
    }

    override fun showUploadingSuccess() {
        toast("The image has been uploaded successfully!")
    }

    override fun showSaveImageSuccess(response: Response.Upload) {
        if (mCurrentMergedImage != null && mCurrentPhoto != null) {
            toast("Saved image successfully.")
            mMergePresenter.handleCancelClicked(mBitmapBG)
            startShareActivity(response)
        } else {
            toast("Please select the background image first.")
        }
    }

    override fun showSaveImageFailed(msg: String, request: Request.Retriable.Upload) {
        val dialogRetry = IsetanDialog.newInstance(IsetanDialog.MODE_RETRY, request)
        dialogRetry.show(supportFragmentManager, "retry")
        toast(msg)
    }

    override fun getContainer(): View = container

    override fun getImageDrawable(): Drawable = ivPhoto.drawable

    override fun getStickerLayout(): ScalableLayout = scalableLayout
}
