package me.ripzery.shooter

import java.text.SimpleDateFormat
import java.util.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object ImageDataCreator {
    private val mBaseName = "MERGY"
    fun createName(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return mBaseName + timeStamp + "_"
    }

    fun createDescription(): String {
        return mBaseName
    }
}