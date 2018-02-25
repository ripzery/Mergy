package me.ripzery.warpcan.helpers

import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import kotlinx.android.synthetic.main.scalable_layout.view.*
import me.ripzery.bitmapmerger.Position


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class PositionManager(private val view: PositionManagerInterface.View) : PositionManagerInterface.PositionManager {
    private val mContainer by lazy { view.getContainer() }
    private val mImageDrawable by lazy { view.getImageDrawable() }
    private val mStickerLayout by lazy { view.getStickerLayout() }
    private val mRatioDrawableToScreen by lazy {
        val ratioWidth = mImageDrawable.intrinsicWidth / mContainer.width.toFloat()
        val ratioHeight = mImageDrawable.intrinsicHeight / mContainer.height.toFloat()
        PointF(ratioWidth, ratioHeight)
    }
    private val mAdjustPosition by lazy {
        val adjustWidth = ((mStickerLayout.scaleX - 1) / 2) * mStickerLayout.width
        val adjustHeight = ((mStickerLayout.scaleY - 1) / 2) * mStickerLayout.height
        PointF(adjustWidth, adjustHeight)
    }
    private val mStickerPosition by lazy {
        val left = (mStickerLayout.getViewLeft(mStickerLayout.imageView, mContainer) - mAdjustPosition.x) * mRatioDrawableToScreen.x
        val top = (mStickerLayout.getViewTop(mStickerLayout.imageView, mContainer) - mAdjustPosition.y) * mRatioDrawableToScreen.y
        val right = (left + mStickerLayout.getImageWidth() * mStickerLayout.scaleX * mRatioDrawableToScreen.x)
        val bottom = (top + mStickerLayout.getImageHeight() * mStickerLayout.scaleY * mRatioDrawableToScreen.y)
        Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    }
    override fun getPosition(): Position = Position(mStickerPosition, Point(mImageDrawable.intrinsicWidth, mImageDrawable.intrinsicHeight))
}