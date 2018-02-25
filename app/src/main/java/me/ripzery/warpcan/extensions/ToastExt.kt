package me.ripzery.warpcan.extensions

import android.content.Context
import android.widget.Toast


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Toaster.toast?.cancel()
    Toaster.toast = Toast.makeText(this, msg, length)
    Toaster.toast?.show()
}

private object Toaster {
    var toast: Toast? = null
}