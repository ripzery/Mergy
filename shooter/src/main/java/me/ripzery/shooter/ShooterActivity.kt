package me.ripzery.shooter

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_shooter.*
import me.ripzery.bgcutter.BgCutter
import me.ripzery.bitmapkeeper.BitmapKeeper
import java.io.File


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class ShooterActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var mCurrentPhotoPath: String
    lateinit var mImageUri: Uri
    private val mBitmapOptimizer by lazy { BitmapOptimizer(mCurrentPhotoPath) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shooter)
        title = "Processing..."
        dispatchTakePictureIntent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            mCurrentPhotoPath = getRealPathFromURI(mImageUri)
            broadcastImageCreated()
            setCapturedImage(ivGreenPhoto) { result ->
                val resultIntent = Intent().apply { setData(result) }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
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

    private fun setCapturedImage(target: ImageView, onCompleted: (Uri) -> Unit) {
        // Visible the preview image
        ivGreenPhoto.visibility = View.VISIBLE

        // Optimize the image
        val optimizedBitmap = mBitmapOptimizer.optimize(target.maxHeight)

        val bgCutter = BgCutter(optimizedBitmap)

        bgCutter.removeGreen({
            target.setImageBitmap(it)
        }) {
            it.setHasAlpha(true)
            val bitmapKeeper = BitmapKeeper(it)
            val savedImageUri = bitmapKeeper.save(this)
            onCompleted(savedImageUri)
        }
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

    private fun broadcastImageCreated() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }
}