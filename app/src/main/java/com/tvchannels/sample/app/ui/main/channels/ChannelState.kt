package com.tvchannels.sample.app.ui.main.channels

import com.tvchannels.sample.app.ui.main.adapter.ChannelItem


sealed class ChannelState {
    object NotLoaded : ChannelState()
    data class DataList(val channels: List<ChannelItem> = emptyList()) : ChannelState()
}
