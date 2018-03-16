package me.ripzery.warpcan.ui.main

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.ripzery.shooter.BitmapOptimizer
import me.ripzery.shooter.ImageDataCreator
import me.ripzery.warpcan.R
import me.ripzery.warpcan.RemoveGreenScreenActivity
import me.ripzery.warpcan.StartActivity
import me.ripzery.warpcan.helpers.ExifExtractor


class MainActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mCurrentPhotoPath: String
    private var isFrontCamera: Boolean = true
    private var mImageUri: Uri? = null

    companion object {
        const val SAVED_STATE_PHOTO_PATH = "PHOTO_PATH"
        const val TAB_S3_APERTURE_FRONT = "2.2"
        const val TAB_S3_APERTURE_BACK = "1.9"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            if (it.getString(SAVED_STATE_PHOTO_PATH) != null) {
                mImageUri = Uri.parse(it.getString(SAVED_STATE_PHOTO_PATH))
                showImageResult()
            }
        }

        btnTakePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
        btnMerge.setOnClickListener {
            startActivity(Intent(this, RemoveGreenScreenActivity::class.java).apply {
                putExtra(RemoveGreenScreenActivity.INTENT_PHOTO_PATH, mCurrentPhotoPath)
                putExtra(RemoveGreenScreenActivity.INTENT_CAMERA_FACING, isFrontCamera)
            })
        }
        btnHome.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        layoutEmptyImage.setOnClickListener { dispatchTakePictureIntent() }
        tvTakePhoto.setOnClickListener { dispatchTakePictureIntent() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    showImageResult()
                }
            }
        }
    }

    private fun showImageResult() {
        mCurrentPhotoPath = getRealPathFromURI(mImageUri!!)
        val aperture = ExifExtractor.readByTag(mCurrentPhotoPath)
        isFrontCamera = aperture ?: TAB_S3_APERTURE_FRONT == TAB_S3_APERTURE_FRONT
        val optimizer = BitmapOptimizer(mCurrentPhotoPath)
        ivPhoto.setImageBitmap(optimizer.optimize(ivPhoto.maxHeight))
        showMergeBtnIfNeeded()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (mImageUri != null) {
            outState?.putString(SAVED_STATE_PHOTO_PATH, mImageUri.toString())
        }
        super.onSaveInstanceState(outState)
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
        if (mCurrentPhotoPath.isNotEmpty()) btnMerge.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    }
}
