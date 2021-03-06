package me.ripzery.mergy.ui.merge

import android.graphics.Bitmap
import android.net.Uri


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface MergeContract {
    interface View {
        fun setSaveVisibility(visible: Boolean)
        fun setCancelVisibility(visible: Boolean)
        fun setShareVisibility(visible: Boolean)
        fun setLoadingVisibility(visible: Boolean)
        fun setScalableViewVisibility(visible: Boolean)
        fun saveToDevice(bitmap: Bitmap): Uri
        fun setPhotoAlpha(alpha: Float)
        fun setBackground(bitmap: Bitmap)
        fun setBitmap(bitmap: Bitmap)
        fun merge(background: Bitmap, sticker: Bitmap): Bitmap
        fun setSaveEnabled(enable: Boolean)
        fun setCancelEnabled(enable: Boolean)
        fun setShareEnabled(enable: Boolean)
        fun setMergedImageUri(uri: Uri)
        fun showSaveImageSuccess()
    }

    interface Presenter {
        fun handleSaveClicked(bg: Bitmap, sticker: Bitmap)
        fun handleCancelClicked(bg: Bitmap)
        fun handleBackgroundChanged(bg: Bitmap)
    }
}