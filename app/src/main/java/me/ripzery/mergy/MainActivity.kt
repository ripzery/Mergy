package me.ripzery.mergy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import me.ripzery.mergy.extensions.toast
import me.ripzery.mergy.merge.MergeActivity
import me.ripzery.shooter.ShooterActivity


class MainActivity : AppCompatActivity() {
    val RESULT_SHOOTER_ACTIVITY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())
        startActivityForResult(Intent(this, ShooterActivity::class.java), RESULT_SHOOTER_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_SHOOTER_ACTIVITY -> {
                    toast("Image is saved successfully.")
                    setBitmapFromUri(data?.data)
                    startMergeActivity(data)
                }
            }
        }
    }

    private fun setBitmapFromUri(uri: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        ivPhoto.setImageBitmap(bitmap)
    }

    private fun startMergeActivity(data: Intent?) {
        val intent = Intent(this, MergeActivity::class.java)
        intent.data = data?.data
        startActivity(intent)
    }
}
