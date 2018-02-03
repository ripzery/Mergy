package me.ripzery.mergy

import android.app.Application
import com.bumptech.glide.Glide
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


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
    }
}