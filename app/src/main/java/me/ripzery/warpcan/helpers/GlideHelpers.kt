package me.ripzery.warpcan.helpers

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import me.ripzery.warpcan.R


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 3/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object GlideHelpers {
    private fun noCache(): RequestOptions {
        return RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
    }

    fun defaultRequestOptions(): RequestOptions {
        return RequestOptions()
                .encodeQuality(100)
                .downsample(DownsampleStrategy.NONE)
                .error(R.drawable.error)
    }
}