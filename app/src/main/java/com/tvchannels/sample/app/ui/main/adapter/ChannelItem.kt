package com.tvchannels.sample.app.ui.main.adapter

data class ChannelItem(
    val id: Long,
    val name: String,
    val streamUrl: String,
    val imageUrl: String,
    val isFavorite:Boolean,
)