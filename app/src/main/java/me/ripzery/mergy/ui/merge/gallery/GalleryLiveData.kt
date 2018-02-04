package me.ripzery.mergy.ui.merge.gallery

import android.arch.lifecycle.LiveData
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.network.DataProvider
import me.ripzery.mergy.network.Response


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