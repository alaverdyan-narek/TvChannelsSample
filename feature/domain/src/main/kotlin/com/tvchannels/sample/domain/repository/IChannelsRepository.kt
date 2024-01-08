package com.tvchannels.sample.domain.repository

import com.tvchannels.sample.domain.entity.ChannelsEntity
import kotlinx.coroutines.flow.Flow

interface IChannelsRepository {
    fun getChannels(): Flow<ChannelsEntity>
    fun saveChannelToFav(id: Long): Flow<Unit>
    fun removeChannelFromFav(id: Long): Flow<Unit>
    fun getFavoriteChannels():Flow<List<ChannelsEntity.ChannelEntity>>
   suspend fun getCachedChannelById(id: Long):ChannelsEntity.ChannelEntity?
}