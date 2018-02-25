package me.ripzery.warpcan.network

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.ripzery.warpcan.extensions.logd


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

    private val mDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun retrievesPhotos(callback: (ArrayList<Response.Photo>) -> Unit) {
        val d = ApiService.Isetan.retrievePhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback(it.message)
                }, mErrorHandler)
        mDisposable.add(d)
    }

    fun retrieveUsers(request: Request.RetrieveUsers, callback: (Response.RetrieveUsers) -> Unit) {
        val d = ApiService.Isetan.retrieveUserInfos(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback(it)
                }, mErrorHandler)
        mDisposable.add(d)
    }

    fun uploadThenSendEmail(request: Request.Upload, request2: Request.SendEmail, callback: (Response.SendEmail) -> Unit) {
        val d = ApiService.Isetan.upload(request)
                .flatMap {
                    val imageUrl = it.message.imageUrl
                    val newRequestEmail = request2.copy(imageUrl = imageUrl)
                    ApiService.Isetan.sendEmail(newRequestEmail)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback(it)
                }, mErrorHandler)

        mDisposable.add(d)
    }

    fun unsubscribe() {
        mDisposable.clear()
    }
}