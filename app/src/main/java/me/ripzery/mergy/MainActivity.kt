package me.ripzery.mergy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import me.ripzery.mergy.extensions.toast
import me.ripzery.mergy.merge.MergeActivity
import me.ripzery.shooter.ShooterActivity


class MainActivity : AppCompatActivity() {
    val RESULT_SHOOTER_ACTIVITY = 1001
    private var mData: Uri? = null
    private var mMenuNext: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInstance()
        btnTakePhoto.setOnClickListener {
            startActivityForResult(Intent(this, ShooterActivity::class.java), RESULT_SHOOTER_ACTIVITY)
        }
        btnMerge.setOnClickListener {
            startMergeActivity(mData)
        }
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mMenuNext = menu?.findItem(R.id.menu_next)
        showMergeBtnIfNeeded()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_next -> startMergeActivity(mData)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_SHOOTER_ACTIVITY -> {
                    toast("Image is saved successfully.")
                    setBitmapFromUri(data?.data)
                    mData = data?.data
                    showMergeBtnIfNeeded()
                }
            }
        }
    }

    private fun showMergeBtnIfNeeded() {
        mMenuNext?.isVisible = mData != null
    }

    private fun setBitmapFromUri(uri: Uri?) {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        ivPhoto.setImageBitmap(bitmap)
    }

    private fun startMergeActivity(data: Uri?) {
        val intent = Intent(this, MergeActivity::class.java)
        intent.data = data
        startActivity(intent)
    }
}
