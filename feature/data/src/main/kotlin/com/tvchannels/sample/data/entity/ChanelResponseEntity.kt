package com.tvchannels.sample.data.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ChannelsResponseEntity(@SerialName("channels") val items: List<ChannelResponseEntity>?) {
    @Serializable
   class ChannelResponseEntity(
        @SerialName("name_ru") val name: String?,
        @SerialName("image") val image: String?,
        @SerialName("url") val url: String?,
    )
}