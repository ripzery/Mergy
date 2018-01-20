package me.ripzery.mergy

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.ViewTreeObserver
import com.xiaopo.flying.sticker.DrawableSticker
import kotlinx.android.synthetic.main.activity_merge.*
import me.ripzery.bitmapmerger.BitmapMerger


class MergeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_merge)
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.bg)
        val bitmap2 = MediaStore.Images.Media.getBitmap(this.contentResolver, intent.data)
//        ivPhoto.setImageBitmap(bitmap1)
        val bitmapMerger = BitmapMerger(bitmap1, bitmap2)
        val vto = ivPhoto.viewTreeObserver
        vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                ivPhoto.viewTreeObserver.removeOnPreDrawListener(this)
                val drawableSticker1 = DrawableSticker(BitmapDrawable(resources, bitmap1))
                val drawableSticker2 = DrawableSticker(BitmapDrawable(resources, bitmap2))
//                stickerView.addSticker(drawableSticker1)
//                stickerView.addSticker(drawableSticker2)

//                val finalHeight = ivPhoto.drawable.intrinsicHeight
//                val finalWidth = ivPhoto.drawable.intrinsicWidth
//                val mergedBitmap = bitmapMerger.merge(this@MergeActivity, finalWidth, finalHeight)
//                stickerView.addSticker(DrawableSticker())
//                ivPhoto.setImageBitmap(mergedBitmap)
                return true
            }
        })
    }
}
