package com.tvchannels.sample.app.ui.main.favorites

import com.tvchannels.sample.app.ui.main.adapter.ChannelItem

data class FavoriteState(
    val channels: List<ChannelItem> = emptyList()
)