package me.ripzery.mergy

import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_merge.*
import kotlinx.android.synthetic.main.scalable_layout.view.*
import me.ripzery.bitmapmerger.BitmapMerger


class MergeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)

        /* background image */
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.bg)

        /* draggable image */
        val bitmap2 = MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
//        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.minion)
        ivPhoto.setImageBitmap(bitmap1)

        /* Initialize the merger */
        val bitmapMerger = BitmapMerger(bitmap1, bitmap2)
        val vto = ivPhoto.viewTreeObserver

        btnCancel.setOnClickListener {
            scalableLayout.visibility = View.VISIBLE
            ivPhoto.setImageBitmap(bitmap1)
        }


        btnSave.setOnClickListener {
            val containerWidth = container.width
            val containerHeight = container.height
            val finalHeight = ivPhoto.drawable.intrinsicHeight
            val finalWidth = ivPhoto.drawable.intrinsicWidth
            val ratioWidthDrawableToScreen = finalWidth / containerWidth.toFloat()
            val ratioHeightDrawableToScreen = finalHeight / containerHeight.toFloat()
            val adjustWidthAmount = ((scalableLayout.scaleX - 1) / 2) * scalableLayout.width
            val adjustTopAmount = ((scalableLayout.scaleY - 1) / 2) * scalableLayout.height
            val left = (scalableLayout.getViewLeft(scalableLayout.imageView, container) - adjustWidthAmount) * ratioWidthDrawableToScreen
            val top = (scalableLayout.getViewTop(scalableLayout.imageView, container) - adjustTopAmount) * ratioHeightDrawableToScreen
            val right = (left + scalableLayout.getImageWidth() * scalableLayout.scaleX * ratioWidthDrawableToScreen)
            val bottom = (top + scalableLayout.getImageHeight() * scalableLayout.scaleY * ratioHeightDrawableToScreen)
            Log.d("Position", "$left, $top, $right, $bottom")
            val mergedBitmap = bitmapMerger.merge(this@MergeActivity, finalWidth, finalHeight, left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            ivPhoto.setImageBitmap(mergedBitmap)
            scalableLayout.visibility = View.GONE
        }
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                ivPhoto.viewTreeObserver.removeOnPreDrawListener(this)
                scalableLayout.setImage(bitmap2)
                return true
            }
        })
    }

    fun dipToPixels(dipValue: Float): Float {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}
