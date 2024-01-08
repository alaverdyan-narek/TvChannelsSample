package com.tvchannels.sample.app.ui.main

import com.tvchannels.sample.domain.utils.TextSource

data class MainChannelState(
    val tabNames:List<TextSource>,
    val offsetScreenPageLimit:Int
)