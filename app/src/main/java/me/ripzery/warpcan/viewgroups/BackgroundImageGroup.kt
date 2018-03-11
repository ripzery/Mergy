package me.ripzery.warpcan.viewgroups

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.layout_background_image.view.*
import me.ripzery.warpcan.R
import me.ripzery.warpcan.extensions.logd
import me.ripzery.warpcan.helpers.GlideHelpers
import me.ripzery.warpcan.network.Response
import me.ripzery.warpcan.ui.merge.gallery.GalleryViewModel


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class BackgroundImageGroup constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var mRootLayout: FrameLayout
    private var mBackground: Bitmap? = null
    private var mPhoto: Response.Photo? = null
    private var mFirst: Boolean = false
    private var mGalleryViewModel: GalleryViewModel? = null

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        mRootLayout = View.inflate(context, R.layout.layout_background_image, this) as FrameLayout
        mRootLayout.backgroundLayout.setOnClickListener {
            if (mPhoto != null && mBackground != null) {
                mGalleryViewModel?.select(mBackground!!, mPhoto!!)
            } else {
                logd("Background is null")
            }
        }
    }

    fun setCaption(caption: String) {
        mRootLayout.tvCaption.text = caption
    }

    fun setFirstImage(first: Boolean) {
        mFirst = first
    }

    fun setImageBackground(photo: Response.Photo) {
        if (photo.imageUrl == "empty") {
            mRootLayout.ivBackground.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.image_placeholder))
            return
        }
        mPhoto = photo
        val width = 1920
        val height = 1080
        Glide.with(context)
                .applyDefaultRequestOptions(GlideHelpers.defaultRequestOptions())
                .asBitmap()
                .load(photo.imageUrl)
                .into(object : SimpleTarget<Bitmap>(width, height) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        progressBar.visibility = View.GONE
                        mBackground = resource
                        mRootLayout.ivBackground.setImageBitmap(resource)
                        if (mFirst && mGalleryViewModel?.subscribeBitmap()?.value == null) {
                            mGalleryViewModel?.select(resource, photo)
                        }
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        mRootLayout.ivBackground.setImageDrawable(errorDrawable)
                        progressBar.visibility = View.GONE
                        mBackground = (errorDrawable!! as BitmapDrawable).bitmap
                    }
                })
    }


    fun setGalleryViewModel(listener: GalleryViewModel) {
        mGalleryViewModel = listener
    }
}