package me.ripzery.mergy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import me.ripzery.shooter.ShooterActivity
import android.provider.MediaStore
import android.graphics.Bitmap




class MainActivity : AppCompatActivity() {
    val RESULT_SHOOTER_ACTIVITY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_9)
//        val bgcutter: BgCutter = BgCutter(bitmap)
//        ivGreenPhoto.setImageBitmap(bgcutter.removeGreen())

        btnTakeAPhoto.setOnClickListener {
            startActivityForResult(Intent(this, ShooterActivity::class.java), RESULT_SHOOTER_ACTIVITY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_SHOOTER_ACTIVITY -> {
                    Toast.makeText(this, "Image is saved successfully.", Toast.LENGTH_LONG).show()
                    val savedImageUri = data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, savedImageUri)
                    ivPhoto.setImageBitmap(bitmap)
                }
            }
        }
    }
}
