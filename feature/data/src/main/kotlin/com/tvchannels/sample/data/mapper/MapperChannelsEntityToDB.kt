package com.tvchannels.sample.data.mapper

import com.tvchannels.sample.data.db.entity.ChannelDbEntity
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.utils.Mapper

class MapperChannelsEntityToDB : Mapper<ChannelsEntity.ChannelEntity, ChannelDbEntity> {
    override fun map(from: ChannelsEntity.ChannelEntity) = ChannelDbEntity(
        id = from.id,
        name = from.name,
        imageUrl = from.image,
        videoUrl = from.url,
    )
}