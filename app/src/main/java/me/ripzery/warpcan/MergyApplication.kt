package me.ripzery.warpcan

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import okhttp3.Cache
import java.io.File


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class MergyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        val httpCacheDirectory = File(applicationContext.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        cache = Cache(httpCacheDirectory, cacheSize.toLong())
        context = applicationContext
    }

    companion object {
        lateinit var cache: Cache
        lateinit var context: Context
    }
}