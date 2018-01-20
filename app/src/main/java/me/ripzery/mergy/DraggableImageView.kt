package me.ripzery.mergy

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class DraggableImageView constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    private var xDelta = 0.0f
    private var yDelta = 0.0f

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        setBackgroundResource(R.drawable.bg_image_border)
    }
}