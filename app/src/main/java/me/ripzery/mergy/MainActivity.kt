package me.ripzery.mergy

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import me.ripzery.bgcutter.BgCutter
import me.ripzery.shooter.ShooterActivity


class MainActivity : AppCompatActivity() {
    val RESULT_SHOOTER_ACTIVITY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())

        Glide.with(this).load("https://madu-isetan-space.nyc3.digitaloceanspaces.com/50f199fcc445ac144b1416a39286012e.jpg").into(ivPhoto)


//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_9)
//        val bgcutter: BgCutter = BgCutter(bitmap)
//        ivPhoto.setImageBitmap(bgcutter.removeGreen())

//        startActivity(Intent(this, DragActivity::class.java))

        startActivityForResult(Intent(this, ShooterActivity::class.java), RESULT_SHOOTER_ACTIVITY)
//        val intent = Intent(this, MergeActivity::class.java)
//        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_SHOOTER_ACTIVITY -> {
                    Toast.makeText(this, "Image is saved successfully.", Toast.LENGTH_LONG).show()
                    val savedImageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, savedImageUri)
                    ivPhoto.setImageBitmap(bitmap)

                    val intent = Intent(this, MergeActivity::class.java)
                    intent.data = data?.data
                    startActivity(intent)
                }
            }
        }
    }
}
