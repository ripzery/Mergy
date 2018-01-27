package me.ripzery.shooter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class BitmapOptimizer(private val photoPath: String) {
    private val mBitmapScaler by lazy { BitmapScaler() }
    private val mBitmap by lazy { BitmapFactory.decodeFile(photoPath) }
    fun optimize(height: Int): Bitmap {
        val adjustAngleBitmap = adjustOrientation(photoPath, mBitmap)
        val resizedBitmap = mBitmapScaler.scaleToFitWidth(adjustAngleBitmap, height)
        return resizedBitmap
    }

    private fun adjustOrientation(photoPath: String, bitmap: Bitmap): Bitmap {
        val ei = ExifInterface(photoPath)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }
}