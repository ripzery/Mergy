package me.ripzery.mergy.merge

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_background_image.view.*
import me.ripzery.mergy.R


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
    private lateinit var mBackground: Bitmap
    private var mOnBackgroudSelectListener: OnImageSelectedListener? = null

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        mRootLayout = View.inflate(context, R.layout.layout_background_image, this) as FrameLayout
        mRootLayout.backgroundLayout.setOnClickListener {
            mOnBackgroudSelectListener?.onBackgroundSelected(getImageBackground())
        }
    }

    fun setCaption(caption: String) {
        mRootLayout.tvCaption.text = caption
    }

    fun setImageBackground(bg: Bitmap) {
        mBackground = bg
        Glide.with(context)
                .load(bg)
                .into(mRootLayout.ivBackground)
    }

    fun getImageBackground(): Bitmap {
        return mBackground
    }

    fun setOnBackgroundChangeListener(listener: OnImageSelectedListener?) {
        mOnBackgroudSelectListener = listener
    }

    interface OnImageSelectedListener {
        fun onBackgroundSelected(bg: Bitmap)
    }
}