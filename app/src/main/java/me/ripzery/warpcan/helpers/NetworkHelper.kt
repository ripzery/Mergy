package me.ripzery.warpcan.helpers

import android.content.Context
import android.net.ConnectivityManager
import me.ripzery.warpcan.MergyApplication


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/3/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */
object NetworkHelper {
    fun isNetworkAvailable(): Boolean {
        val manager = MergyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            // Network is present and connected
            isAvailable = true
        }
        return isAvailable
    }
}