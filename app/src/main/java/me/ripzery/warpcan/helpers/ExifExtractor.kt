package me.ripzery.warpcan.helpers

import android.support.media.ExifInterface
import me.ripzery.warpcan.extensions.logd


/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 16/3/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
object ExifExtractor {
    fun readAperture(filename: String): String? {
        val exifInterface = ExifInterface(filename)
        val aperture = exifInterface.getAttribute(ExifInterface.TAG_F_NUMBER)
        logd(aperture ?: "0.0")
        return aperture
    }
}