package me.ripzery.mergy

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.ripzery.bgcutter.BgCutter
import me.ripzery.shooter.ShooterActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shooter)
        /* get bitmap from resource */
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_9)
        val bgcutter: BgCutter = BgCutter(bitmap)
        ivGreenPhoto.setImageBitmap(bgcutter.removeGreen())

        startActivity(Intent(this, ShooterActivity::class.java))
    }
}
