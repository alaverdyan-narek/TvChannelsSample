package com.tvchannels.sample.domain.entity

data class ChannelsEntity(
    val channels:List<ChannelEntity> = emptyList()
){
    data class  ChannelEntity(
        val id:Long,
        val name:String,
        val image:String,
        val url:String,
        val isFavorite:Boolean = false
    )
}