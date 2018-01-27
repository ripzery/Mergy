package me.ripzery.mergy.merge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_merge.*
import me.ripzery.bitmapkeeper.BitmapKeeper
import me.ripzery.bitmapmerger.BitmapMerger
import me.ripzery.bitmapmerger.Position
import me.ripzery.mergy.R
import me.ripzery.mergy.ScalableLayout
import me.ripzery.mergy.extensions.toast


class MergeActivity : AppCompatActivity(), PositionManagerInterface.View {
    private val mPositionManager by lazy { PositionManager(this) }
    private val mPosition: Position = mPositionManager.getPosition()
    private val mSticker by lazy {
        MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
    }
    private lateinit var mBitmapBG: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)
        initInstance()
    }

    private fun initInstance() {
        /* Initialize background and sticker images */
        mBitmapBG = BitmapFactory.decodeResource(resources, R.drawable.bg)
        changeBackground(mBitmapBG)
        scalableLayout.setImage(mSticker)

        btnCancel.setOnClickListener {
            scalableLayout.visibility = View.VISIBLE
            changeBackground(mBitmapBG)
        }

        btnSave.setOnClickListener {
            scalableLayout.visibility = View.GONE
            val newBitmap = merge(mBitmapBG, mSticker)
            changeBackground(newBitmap)
            saveToDevice(newBitmap)
            toast("Saved image successfully.")
        }
    }

    private fun changeBackground(bitmap: Bitmap) {
        ivPhoto.setImageBitmap(bitmap)
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
}
