package me.ripzery.warpcan.helpers

import android.graphics.drawable.Drawable
import me.ripzery.bitmapmerger.Position
import me.ripzery.warpcan.viewgroups.ScalableLayout


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 27/1/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface PositionManagerInterface {
    interface View {
        fun getContainer(): android.view.View
        fun getImageDrawable(): Drawable
        fun getStickerLayout(): ScalableLayout
    }

    interface PositionManager {
        fun getPosition(): Position
    }
}