package me.ripzery.warpcan.ui.merge

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import me.ripzery.warpcan.network.DataProvider
import me.ripzery.warpcan.network.Request
import me.ripzery.warpcan.network.Response
import org.jetbrains.anko.coroutines.experimental.bg


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class MergePresenter(private val mView: MergeContract.View) : MergeContract.Presenter {
    override fun handleSaveClicked(bg: Bitmap, sticker: Bitmap, photo: Response.Photo) {
        var mUri: Uri? = null
        with(mView) {
            async(UI) {
                setBackground(bg)
                setSaveEnabled(false)
                setScalableViewVisibility(true)
                setLoadingVisibility(true)
                setPhotoAlpha(0.7f)
                val bgTask = bg {
                    val newBitmap = merge(bg, sticker)
                    mUri = saveToDevice(newBitmap)
                    setMergedImageUri(mUri!!)
                    newBitmap
                }
                setBackground(bgTask.await())
                setScalableViewVisibility(false)
                setSaveEnabled(true)
                setPhotoAlpha(1.0f)
                showUploadingMessage()
                encryptBase64 {
                    val reqUpload = Request.Retriable.Upload(it, 1, photo.imageType)
                    DataProvider.upload(reqUpload, {
                        handleRequestFail(reqUpload)
                    }) {
                        handleRequestSuccess(it)
                    }
                }
            }
        }
    }

    override fun handleRequestFail(request: Request.Retriable.Upload) {
        mView.setLoadingVisibility(false)
        mView.setSaveEnabled(true)
        mView.showSaveImageFailed("Cannot uploaded an image to the server.", request)
    }

    override fun handleRequestSuccess(response: Response.Upload) {
        mView.setLoadingVisibility(false)
        mView.setSaveEnabled(true)
        mView.showUploadingSuccess()
        mView.showSaveImageSuccess(response)
    }

    override fun handleStartUpload() {
        mView.setLoadingVisibility(true)
        mView.setSaveEnabled(false)
        mView.showUploadingMessage()
    }

    override fun handleCancelClicked(bg: Bitmap) {
        with(mView) {
            setBackground(bg)
            setScalableViewVisibility(true)
            setCancelVisibility(false)
            setCancelEnabled(false)
            setShareVisibility(false)
            setSaveVisibility(true)
            setSaveEnabled(true)
        }
    }

    override fun handleBackgroundChanged(bg: Bitmap) {
        with(mView) {
            setBitmap(bg)
            setBackground(bg)
            setScalableViewVisibility(true)
            setSaveVisibility(true)
            setCancelVisibility(false)
            setShareVisibility(false)
        }
    }
}