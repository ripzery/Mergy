package me.ripzery.mergy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.nisrulz.sensey.Sensey

class DragActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
        Sensey.getInstance().init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Sensey.getInstance().stop()
    }
}
