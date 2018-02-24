package me.ripzery.mergy.ui.main

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.ripzery.mergy.R
import me.ripzery.mergy.RemoveGreenScreenActivity
import me.ripzery.mergy.StartActivity
import me.ripzery.mergy.ui.merge.MergeActivity
import me.ripzery.shooter.BitmapOptimizer
import me.ripzery.shooter.ImageDataCreator


class MainActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mCurrentPhotoPath: String
    private var mMenuNext: MenuItem? = null
    private lateinit var mImageUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
        btnMerge.setOnClickListener {
            startActivity(Intent(this, RemoveGreenScreenActivity::class.java).apply {
                putExtra(RemoveGreenScreenActivity.INTENT_PHOTO_PATH, mCurrentPhotoPath)
            })
        }
        btnHome.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    mCurrentPhotoPath = getRealPathFromURI(mImageUri)
                    val optimizer = BitmapOptimizer(mCurrentPhotoPath)
                    ivPhoto.setImageBitmap(optimizer.optimize(ivPhoto.maxHeight))
                    mCurrentPhotoPath = getRealPathFromURI(mImageUri)
                    showMergeBtnIfNeeded()
                }
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(contentUri, proj, null, null, null)
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val values = ContentValues()
        with(values) {
            put(MediaStore.Images.Media.TITLE, ImageDataCreator.createName())
            put(MediaStore.Images.Media.DESCRIPTION, ImageDataCreator.createName())
        }

        mImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun showMergeBtnIfNeeded() {
        btnMerge.isEnabled = mCurrentPhotoPath.isNotEmpty()
        if(mCurrentPhotoPath.isNotEmpty()) btnMerge.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }
}
