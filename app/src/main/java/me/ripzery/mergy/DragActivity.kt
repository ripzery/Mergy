package me.ripzery.mergy

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import me.ripzery.mergy.merge.GalleryFragment

class DragActivity : AppCompatActivity(), GalleryFragment.OnImageSelectedListener {
    private lateinit var mGalleryFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)
        initInstance()
    }

    private fun initInstance() {
        mGalleryFragment = GalleryFragment.newInstance("", "")
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.galleryContainer, mGalleryFragment)
                .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /* Gallery Listener */
    override fun onFragmentInteraction(uri: Uri) {

    }

}
