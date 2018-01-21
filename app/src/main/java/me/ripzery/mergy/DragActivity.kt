package me.ripzery.mergy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.nisrulz.sensey.Sensey

class DragActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
