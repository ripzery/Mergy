package me.ripzery.warpcan.ui.merge.gallery

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import me.ripzery.warpcan.network.Response


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class GalleryViewModel : ViewModel() {
    private val mGalleryLiveData: GalleryLiveData by lazy { GalleryLiveData() }
    private val mBitmapLiveData: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }
    private val mPhotoLiveData: MutableLiveData<Response.Photo> by lazy { MutableLiveData<Response.Photo>() }

    fun retrieveGallery() = mGalleryLiveData

    fun subscribeBitmap() = mBitmapLiveData

    fun subscribePhoto() = mPhotoLiveData

    fun select(bitmap: Bitmap, photo: Response.Photo) {
        mBitmapLiveData.value = bitmap
        mPhotoLiveData.value = photo
    }
}