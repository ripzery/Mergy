package me.ripzery.warpcan.ui.merge

import android.graphics.Bitmap
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class MergePresenter(private val mView: MergeContract.View) : MergeContract.Presenter {
    override fun handleSaveClicked(bg: Bitmap, sticker: Bitmap) {
        with(mView) {
            async(UI) {
                setSaveEnabled(false)
                setScalableViewVisibility(true)
                setLoadingVisibility(true)
                setPhotoAlpha(0.7f)
                val bgTask = bg {
                    val newBitmap = merge(bg, sticker)
                    setMergedImageUri(saveToDevice(newBitmap))
                    newBitmap
                }
                setBackground(bgTask.await())
                setScalableViewVisibility(false)
                setLoadingVisibility(false)
                setSaveEnabled(true)
                setPhotoAlpha(1.0f)
                showSaveImageSuccess()
            }
        }
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