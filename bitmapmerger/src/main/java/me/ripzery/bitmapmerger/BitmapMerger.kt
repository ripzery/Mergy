package me.ripzery.bitmapmerger

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.Log


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class BitmapMerger(private val bitmap1: Bitmap, private val bitmap2: Bitmap) {
    fun merge(context: Context, position: Position): Bitmap {
        val bitmap = Bitmap.createBitmap(position.canvasSize.x, position.canvasSize.y, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val drawable1 = BitmapDrawable(context.resources, bitmap1)
        val drawable2 = BitmapDrawable(context.resources, bitmap2)
        drawable1.setBounds(0, 0, position.canvasSize.x, position.canvasSize.y)
        drawable2.setBounds(
                position.dicutFrame.left,
                position.dicutFrame.top,
                position.dicutFrame.right,
                position.dicutFrame.bottom)
        drawable1.draw(canvas)
        drawable2.draw(canvas)
        return bitmap
    }
}