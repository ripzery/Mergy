package me.ripzery.bgcutter

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 13/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class BgCutter(private val originalBitmap: Bitmap, val diff: Pair<Double, Double> = 25.0 to 40.0) {
    private val bgCollection = mutableListOf<Deferred<Bitmap>>()

    fun removeGreen(callback: (Bitmap) -> Unit, onCompleted: (Bitmap) -> Unit) {
        val bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmap.setHasAlpha(true)
        Log.d("Total pixels", "${bitmap.width}, ${bitmap.height}")
        val a = async(UI) {
            (0 until bitmap.width).mapTo(bgCollection) {
                bg {
                    for (j in 0 until bitmap.height) {
                        val pixel1 = bitmap.getPixel(it, j)
                        if (Color.green(pixel1) - Color.red(pixel1) > diff.first && Color.green(pixel1) - Color.blue(pixel1) > diff.second) {
                            val a = Color.alpha(Color.TRANSPARENT)
                            bitmap.setPixel(it, j, a)
                        } else {
//                            Log.d("Non-Green", "${Color.red(pixel1)} ${Color.green(pixel1)} ${Color.blue(pixel1)}")
                        }
                    }
                    bitmap
                }
            }
            bgCollection.forEach {
                callback(it.await())
            }
            onCompleted(bitmap)
        }
    }
}