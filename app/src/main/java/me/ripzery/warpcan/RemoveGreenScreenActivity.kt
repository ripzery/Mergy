package me.ripzery.warpcan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_shooter.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.ripzery.bgcutter.BgCutter
import me.ripzery.bitmapkeeper.BitmapKeeper
import me.ripzery.warpcan.ui.merge.MergeActivity
import me.ripzery.shooter.BitmapOptimizer

class RemoveGreenScreenActivity : AppCompatActivity() {
    private lateinit var mCurrentPhotoPath: String
    private lateinit var mBitmapOptimizer: BitmapOptimizer

    companion object {
        val INTENT_PHOTO_PATH = "current_photo_path"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_green_screen)
        setSupportActionBar(toolbar)
        title = "Processing..."
        mCurrentPhotoPath = intent.getStringExtra(INTENT_PHOTO_PATH)
        mBitmapOptimizer = BitmapOptimizer(mCurrentPhotoPath)
        setCapturedImage(ivGreenPhoto) { result ->
            startMergeActivity(result)
            finish()
        }
    }

    private fun startMergeActivity(data: Uri?) {
        val intent = Intent(this, MergeActivity::class.java)
        intent.data = data
        startActivity(intent)
    }

    private fun setCapturedImage(target: ImageView, onCompleted: (Uri) -> Unit) {
        // Visible the preview image
        ivGreenPhoto.visibility = View.VISIBLE

        // Optimize the image
        val optimizedBitmap = mBitmapOptimizer.optimize(target.maxHeight)

        val bgCutter = BgCutter(optimizedBitmap)

        bgCutter.removeGreen({
            target.setImageBitmap(it)
        }) {
            it.setHasAlpha(true)
            val bitmapKeeper = BitmapKeeper(it)
            val savedImageUri = bitmapKeeper.save(this)
            onCompleted(savedImageUri)
        }
    }
}
