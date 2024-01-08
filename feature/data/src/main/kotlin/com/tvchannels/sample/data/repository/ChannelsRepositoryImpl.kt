package com.tvchannels.sample.data.repository

import androidx.room.withTransaction
import com.tvchannels.sample.data.db.TvChannelDB
import com.tvchannels.sample.data.delegate.FavoriteActionDelegate
import com.tvchannels.sample.data.mapper.MapperChannelsDBtoEntity
import com.tvchannels.sample.data.mapper.MapperChannelsEntityToDB
import com.tvchannels.sample.data.mapper.MapperChannelsResponseEntityToEntity
import com.tvchannels.sample.data.network.api.ChannelApi
import com.tvchannels.sample.domain.delegate.FavoriteAction
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.repository.IChannelsRepository
import com.tvchannels.sample.domain.utils.RealTimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelsRepositoryImpl @Inject constructor(
    private val api: ChannelApi,
    private val db: TvChannelDB,
    private val favDelegate: FavoriteActionDelegate
) : IChannelsRepository {

    private val realTimeCache = RealTimeCache<String, List<ChannelsEntity.ChannelEntity>>()

    override fun getChannels(): Flow<ChannelsEntity> = flow {
        val channels = api.getChannels()
        emit(db.withTransaction {
            val favChannels = db.channelDao().getAll().associateBy { it.id }
            val data = MapperChannelsResponseEntityToEntity().map(channels)
            data.copy(data.channels.map {
                if (favChannels.containsKey(it.id)) return@map it.copy(isFavorite = true)
                it
            }).also {
                realTimeCache.putSafeValue(CHANNEL_CACHE_KEY, it.channels)
            }
        })
    }

    override fun saveChannelToFav(id: Long): Flow<Unit> = flow {
        realTimeCache.getSafeValue(CHANNEL_CACHE_KEY)?.find { it.id == id }?.let {
            db.channelDao().insert(MapperChannelsEntityToDB().map(it))
            favDelegate.sendAction(FavoriteAction.Add(id))
        } ?: throw Exception("No Channel found !")
        emit(Unit)
    }


    override fun removeChannelFromFav(id: Long): Flow<Unit> = flow {
        realTimeCache.getSafeValue(CHANNEL_CACHE_KEY)?.find { it.id == id }?.let {
            db.channelDao().delete(it.id)
            favDelegate.sendAction(FavoriteAction.Remove(id))
        } ?: throw Exception("No Channel found !")
        emit(Unit)
    }

    override fun getFavoriteChannels(): Flow<List<ChannelsEntity.ChannelEntity>> = flow {
        emit(db.channelDao().getAll().map {
            MapperChannelsDBtoEntity().map(it)
        })
    }

    override suspend fun getCachedChannelById(id: Long) = withContext(Dispatchers.Default) {
        realTimeCache.getSafeValue(CHANNEL_CACHE_KEY)?.find { it.id == id }
    }


    private companion object {
        const val CHANNEL_CACHE_KEY = "Channel Cache Key"
    }
}