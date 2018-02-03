package me.ripzery.mergy.helpers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.ByteArrayOutputStream


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object Base64Helper {
    private val QUALITY = 90

    fun encrypt(context: Context, uri: Uri, callback: (String) -> Unit) {
        async(UI) {
            val encodedTask = bg {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
                return@bg "data:image/jpeg;base64,$encoded"
            }
            callback(encodedTask.await())
        }
    }
}