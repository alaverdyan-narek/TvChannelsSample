package com.tvchannels.sample.app.core.util.manager

import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale

fun ImageRequest.Builder.cachedMode(id: String)= this.apply {
    memoryCachePolicy(CachePolicy.DISABLED)
    memoryCacheKey(id)
    allowHardware(false)
    scale(Scale.FILL)
}

