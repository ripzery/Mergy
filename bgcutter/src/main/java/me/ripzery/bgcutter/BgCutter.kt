package me.ripzery.bgcutter

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class BgCutter(private val originalBitmap: Bitmap) {
    fun removeGreen(): Bitmap {
        val bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmap.setHasAlpha(true)
        Log.d("Total pixels", "${bitmap.width}, ${bitmap.height}")
        for (i in 0 until bitmap.width) {
            for (j in 0 until bitmap.height) {
                val pixel1 = bitmap.getPixel(i, j)
                if (Color.green(pixel1) - Color.red(pixel1) > 20 && Color.green(pixel1) - Color.blue(pixel1) > 20) {
                    val a = Color.alpha(Color.TRANSPARENT)
                    bitmap.setPixel(i, j, a)
                } else {
                    Log.d("Non-Green", "${Color.red(pixel1)} ${Color.green(pixel1)} ${Color.blue(pixel1)}")
                }
            }
        }
        return bitmap
    }
}