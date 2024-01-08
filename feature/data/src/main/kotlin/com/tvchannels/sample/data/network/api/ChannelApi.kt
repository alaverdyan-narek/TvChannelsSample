package com.tvchannels.sample.data.network.api

import com.tvchannels.sample.data.entity.ChannelsResponseEntity
import com.tvchannels.sample.domain.entity.ChannelsEntity
import retrofit2.http.GET


interface ChannelApi {
    @GET("${VERSION_V1}/playlist")
    suspend fun getChannels(): ChannelsResponseEntity

    companion object{
         const val VERSION_V1 ="/api/v1"
    }

}