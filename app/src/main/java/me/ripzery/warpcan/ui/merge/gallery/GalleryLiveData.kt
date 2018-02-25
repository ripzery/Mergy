package me.ripzery.warpcan.ui.merge.gallery

import android.arch.lifecycle.LiveData
import me.ripzery.warpcan.extensions.logd
import me.ripzery.warpcan.network.DataProvider
import me.ripzery.warpcan.network.Response


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class GalleryLiveData : LiveData<ArrayList<Response.Photo>>() {
    override fun onActive() {
        logd("Active")
        if (value == null)
            DataProvider.retrievesPhotos { value = it }
    }

    override fun onInactive() {
        logd("Inactive")
        DataProvider.unsubscribe()
    }
}