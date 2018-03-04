package me.ripzery.warpcan.ui.custom

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/3/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class RetryViewModel : ViewModel() {
    private val mRequestingLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun subscribeRequesting() = mRequestingLiveData
    fun dispatchRequesting() {
        mRequestingLiveData.value = (mRequestingLiveData.value ?: 1) + 1
    }
}