package me.ripzery.mergy

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class ScalableLayout constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mScaleFactor = 1f
    private var mMinimumWidth = 0.0f
    private var xDelta = 0.0f
    private var yDelta = 0.0f
    private var mCenterLayoutX = 0.0f
    private var mCenterLayoutY = 0.0f
    private var mPosX = 0.0f
    private var mPosY = 0.0f
    //    private var mRightBottomX = 0.0f
//    private var mRightBottomY = 0.0f
    private var radius = 0.0

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        val viewContainer = View.inflate(context, R.layout.scalable_layout, this)
        val draggableImageView = viewContainer.findViewById<ImageView>(R.id.draggableImageView)
        val ivResize = viewContainer.findViewById<ImageView>(R.id.ivResize)

        ivResize.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(mMinimumWidth == 0.0f){
                        mMinimumWidth = viewContainer.measuredWidth.toFloat() * 0.5f
                    }
                    mCenterLayoutX = (viewContainer.left + viewContainer.right) / 2.0f
                    mCenterLayoutY = (viewContainer.bottom + viewContainer.top) / 2.0f
                    mPosX = motionEvent.rawX - ivResize.x + mCenterLayoutX
                    mPosY = motionEvent.rawY - ivResize.y + mCenterLayoutY

                    mScaleFactor = viewContainer.scaleX

                    radius = Math.hypot((motionEvent.rawX - mPosX).toDouble(), (motionEvent.rawY - mPosY).toDouble())

                    Log.d("Measure", "${measuredWidth * scaleX}, ${measuredHeight * scaleY}")
                    Log.d("CenterLayout", "$mCenterLayoutX,$mCenterLayoutY")
                    Log.d("Radius", "$radius")
                }
                MotionEvent.ACTION_MOVE -> {
                    val newR = Math.hypot((motionEvent.rawX - mPosX).toDouble(), (motionEvent.rawY - mPosY).toDouble())
                    val newScaleFactor = newR / radius * mScaleFactor
                    Log.d("Scale", newScaleFactor.toString())
                    Log.d("MinimumWidth", mMinimumWidth.toString())
                    if (viewContainer.width * newScaleFactor.toFloat() > mMinimumWidth) {
                        viewContainer.scaleX = newScaleFactor.toFloat()
                        viewContainer.scaleY = newScaleFactor.toFloat()
                    }
                }
            }
//            Log.d("Position", "${motionEvent.x}, ${motionEvent.y}")
            true
        }

        draggableImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.minion))
    }

    private fun isScaleUp(centerX: Float, centerY: Float, radius: Double, posX: Float, posY: Float): Boolean {
        val distanceTouch = Math.sqrt(Math.pow((posX - centerX).toDouble(), 2.0) + Math.pow((posY - centerY).toDouble(), 2.0))
        return distanceTouch > radius
    }

    private fun calculateScaleFactor(centerX: Float, centerY: Float, mTouchX: Float, mTouchY: Float, radius: Float): Double {
        return Math.sqrt(Math.pow((mTouchX - centerX).toDouble(), 2.0) + Math.pow((mTouchY - centerY).toDouble(), 2.0)) / radius
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        /***
         * RawX, RawY are the absolute coordinate to the screen
         * x, y are the coordinate relative to the parent layout
         */
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                xDelta = x - event.rawX
                yDelta = y - event.rawY
                Log.d("X and Y", "$x and $y")
                Log.d("RawX and RawY", "${event.rawX} and ${event.rawY}")
            }
            MotionEvent.ACTION_MOVE ->
                animate()
                        .x(event.rawX + xDelta)
                        .y(event.rawY + yDelta)
                        .setDuration(0)
                        .start()
            else -> return false
        }
        return true
    }
}