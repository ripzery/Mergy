package me.ripzery.mergy

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.merge.BackgroundImageGroup
import me.ripzery.mergy.merge.GalleryFragment
import me.ripzery.mergy.models.BackgroundData

class DragActivity : AppCompatActivity(), BackgroundImageGroup.OnImageSelectedListener {
    private lateinit var mGalleryFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
        initInstance()
    }

    private fun initInstance() {
        val listBackgroundData = arrayListOf<BackgroundData>(
                BackgroundData(R.drawable.bg, "Background 1"),
                BackgroundData(R.drawable.bg2, "Background 2"),
                BackgroundData(R.drawable.bg3, "Background 3"),
                BackgroundData(R.drawable.bg4, "Background 4"),
                BackgroundData(R.drawable.bg5, "Background 5"),
                BackgroundData(R.drawable.bg6, "Background 6")
        )
        mGalleryFragment = GalleryFragment.newInstance(listBackgroundData)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.galleryContainer, mGalleryFragment)
                .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /* Gallery Listener */
    override fun onBackgroundSelected(bg: Bitmap) {
        logd("Test")
    }
}
