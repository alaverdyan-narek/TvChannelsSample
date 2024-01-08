package com.tvchannels.sample.data.mapper

import com.tvchannels.sample.data.db.entity.ChannelDbEntity
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.utils.Mapper

class MapperChannelsDBtoEntity:Mapper<ChannelDbEntity,ChannelsEntity.ChannelEntity> {
    override fun map(from: ChannelDbEntity) = ChannelsEntity.ChannelEntity(
        id = from.id,
        name = from.name,
        image = from.imageUrl,
        url = from.videoUrl,
        isFavorite = true
    )
}