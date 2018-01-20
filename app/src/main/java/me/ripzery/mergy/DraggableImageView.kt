package me.ripzery.mergy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.almeros.android.multitouch.MoveGestureDetector


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class DraggableImageView constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {}

    private var mFocusX = 0f
    private var mFocusY = 0f
    private val mScaleFactor = 0.4f
    private val mMatrix by lazy { Matrix() }
    private val mMoveDetector by lazy { MoveGestureDetector(context, MoveListener()) }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mMoveDetector.onTouchEvent(event)

        val mImageHeight = drawable.intrinsicHeight
        val mImageWidth = drawable.intrinsicWidth

        val mScaledImageCenterX = (mImageWidth * mScaleFactor) / 2
        val mScaledImageCenterY = (mImageHeight * mScaleFactor) / 2
        mMatrix.reset()
        invalidate()
        mMatrix.postTranslate(mFocusX - mScaledImageCenterX, mFocusY - mScaledImageCenterY)
        imageMatrix = mMatrix
        return true
    }

    inner class MoveListener : MoveGestureDetector.OnMoveGestureListener {
        override fun onMoveBegin(detector: MoveGestureDetector?): Boolean {
            return true
        }

        override fun onMoveEnd(detector: MoveGestureDetector?) {

        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            val d = detector.focusDelta
            mFocusX += d.x
            mFocusY += d.y
            return true
        }

    }

}