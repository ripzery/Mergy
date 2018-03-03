package me.ripzery.bitmapkeeper

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class BitmapKeeper(val bitmap: Bitmap) {
    val DEFAULT_IMAGE_NAME = "Mergy" + System.currentTimeMillis()
    lateinit var mFile: File
    fun save(context: Context): Uri {
        val path = android.os.Environment.getExternalStorageDirectory()
                .toString()
        val file = File(path + "/" + context.getString(R.string.app_name))
        if (!file.exists())
            file.mkdirs()

        mFile = File(file.absolutePath + "/" + DEFAULT_IMAGE_NAME + ".png")
        val out = FileOutputStream(mFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()
        val image = getImageContent(mFile, DEFAULT_IMAGE_NAME, context)
        val result = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, image)
        return result
    }

    private fun getImageContent(parent: File, imageName: String, context: Context): ContentValues {
        val image = ContentValues()
        image.put(MediaStore.Images.Media.TITLE, context.getString(R.string.app_name))
        image.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
        image.put(MediaStore.Images.Media.DESCRIPTION, "Image by Mergy")
        image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        image.put(MediaStore.Images.Media.ORIENTATION, 0)
        image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString()
                .toLowerCase().hashCode())
        image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.name
                .toLowerCase())
        image.put(MediaStore.Images.Media.SIZE, parent.length())
        image.put(MediaStore.Images.Media.DATA, parent.absolutePath)
        return image
    }
}