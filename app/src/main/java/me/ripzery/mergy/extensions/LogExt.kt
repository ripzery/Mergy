package me.ripzery.mergy.extensions

import android.util.Log


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

fun Any.logd(msg: String) {
    Log.d(this.javaClass.simpleName, msg)
}

fun Any.logw(msg: String) {
    Log.w(this.javaClass.simpleName, msg)
}

fun Any.logi(msg: String) {
    Log.i(this.javaClass.simpleName, msg)
}