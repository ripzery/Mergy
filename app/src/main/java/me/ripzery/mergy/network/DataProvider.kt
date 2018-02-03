package me.ripzery.mergy.network

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.ripzery.mergy.extensions.logd


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object DataProvider {

    // Define each error handler here.
    object ErrorHandlerStrategy {
        val mLogErrorHandler: (Throwable) -> Unit = {
            logd(it.localizedMessage)
        }
        val mPrintStackTraceHandler: (Throwable) -> Unit = {
            it.printStackTrace()
        }
    }

    private val mErrorList: MutableList<(Throwable) -> Unit> = mutableListOf(
            ErrorHandlerStrategy.mLogErrorHandler,
            ErrorHandlerStrategy.mPrintStackTraceHandler
    )

    private val mErrorHandler: (Throwable) -> Unit = { throwable ->
        mErrorList.forEach {
            it.invoke(throwable)
        }
    }

    fun retrievesPhotos(callback: (ArrayList<Response.Photo>) -> Unit) {
        ApiService.Isetan.retrievePhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback(it.message)
                }, mErrorHandler)
    }
}