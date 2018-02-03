package me.ripzery.mergy.merge

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.layout_background_image.view.*
import me.ripzery.mergy.R
import me.ripzery.mergy.extensions.logd
import me.ripzery.mergy.helpers.GlideHelpers


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
    private var mOnBackgroudSelectListener: OnImageSelectedListener? = null

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        mRootLayout = View.inflate(context, R.layout.layout_background_image, this) as FrameLayout
        mRootLayout.backgroundLayout.setOnClickListener {
            if (mBackground != null) {
                mOnBackgroudSelectListener?.onBackgroundSelected(mBackground!!)
            } else {
                logd("Background is null")
            }
        }
    }

    fun setCaption(caption: String) {
        mRootLayout.tvCaption.text = caption
    }

    fun setImageBackground(bg: String) {
        val width = resources.getDimension(R.dimen.backgroundWidth)
        val height = resources.getDimension(R.dimen.backgroundHeight)
        Glide.with(context)
                .applyDefaultRequestOptions(GlideHelpers.defaultRequestOptions())
                .asBitmap()
                .load(bg)
                .into(object : SimpleTarget<Bitmap>(width.toInt(), height.toInt()) {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        progressBar.visibility = View.GONE
                        mBackground = resource
                        mRootLayout.ivBackground.setImageBitmap(resource)
                    }
                })
    }

    fun setOnBackgroundChangeListener(listener: OnImageSelectedListener?) {
        mOnBackgroudSelectListener = listener
    }

    interface OnImageSelectedListener {
        fun onBackgroundSelected(bg: Bitmap)
    }
}