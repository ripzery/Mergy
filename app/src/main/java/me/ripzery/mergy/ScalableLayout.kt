package me.ripzery.mergy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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
    private lateinit var imageView: ImageView
    private lateinit var viewContainer: View
    private lateinit var ivResize: ImageView
    private var mScaleFactor = 1f
    private var mMinimumWidth = 0.0f
    private var xDelta = 0.0f
    private var yDelta = 0.0f
    private var mCenterLayout = 0.0f to 0.0f
    private var mPos: Pair<Float, Float> = 0.0f to 0.0f
    private var radius = 0.0

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        viewContainer = View.inflate(context, R.layout.scalable_layout, this)
        imageView = viewContainer.findViewById(R.id.imageView)
        ivResize = viewContainer.findViewById(R.id.ivResize)

        ivResize.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (mMinimumWidth == 0.0f) {
                        mMinimumWidth = viewContainer.measuredWidth.toFloat() * 0.3f
                    }
                    mCenterLayout = viewContainer.half()
                    mPos = motionEvent.rawX - ivResize.x + mCenterLayout.first to motionEvent.rawY - ivResize.y + mCenterLayout.second
                    mScaleFactor = viewContainer.scaleX
                    radius = Math.hypot(motionEvent.diffX(mPos.first), motionEvent.diffY(mPos.second))
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val newR = Math.hypot(motionEvent.diffX(mPos.first), motionEvent.diffY(mPos.second))
                    val newScaleFactor = (newR / radius * mScaleFactor).toFloat()
                    if (viewContainer.width * newScaleFactor > mMinimumWidth) {
                        viewContainer.setScale(newScaleFactor)
                    }
                    true
                }
                else -> false
            }
        }

        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.minion))
    }

    fun getViewTop(view: View, target: View): Float {
        Log.d("ViewTop", "${view.javaClass.name} ${view.y + view.paddingTop}")
        return if (view.parent == target) {
            view.y
        } else {
            view.y + getViewTop(view.parent as View, target)
        }
    }

    fun getViewLeft(view: View, target: View): Float {
        Log.d("ViewLeft", "${view.javaClass.name} ${view.left}")
        Log.d("ViewX", "${view.javaClass.name} ${view.x}")
        Log.d("ViewPaddingX", "${view.javaClass.name} ${view.paddingLeft}")
        Log.d("ViewTranslationX", "${view.javaClass.name} ${view.translationX}")
        return if (view.parent == target) {
            view.x
        } else {
            view.x + getViewLeft(view.parent as View, target)
        }
    }

    fun getImageWidth(): Int {
        Log.d("Right", "${this.imageView.right * scaleX}")
        return this.imageView.right
    }

    fun getImageHeight() = this.imageView.height

    fun setImage(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    private fun View.halfWidth() = (this.left + this.right) / 2.0f
    private fun View.halfHeight() = (this.top + this.bottom) / 2.0f
    private fun View.half() = halfWidth() to halfHeight()
    private fun View.setScale(scale: Float) {
        scaleX = scale
        scaleY = scale
    }

    private fun MotionEvent.diffX(x: Float) = (rawX - x).toDouble()
    private fun MotionEvent.diffY(y: Float) = (rawY - y).toDouble()

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
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                animate()
                        .x(event.rawX + xDelta)
                        .y(event.rawY + yDelta)
                        .setDuration(0)
                        .start()
                Log.d("Translate", translationX.toString())
                return true
            }
            else -> return false
        }
        return true
    }
}