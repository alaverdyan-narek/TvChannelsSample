package com.tvchannels.sample.data.mapper

import com.tvchannels.sample.data.entity.ChannelsResponseEntity
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.extensions.toUniqueId
import com.tvchannels.sample.domain.utils.Mapper

class MapperChannelsResponseEntityToEntity : Mapper<ChannelsResponseEntity, ChannelsEntity> {
    override fun map(from: ChannelsResponseEntity)=
        ChannelsEntity(
            MapperChannelResponseEntityToEntity().map(from.items.orEmpty())
        )
    private class MapperChannelResponseEntityToEntity :
        Mapper<ChannelsResponseEntity.ChannelResponseEntity, ChannelsEntity.ChannelEntity> {
        override fun map(from: ChannelsResponseEntity.ChannelResponseEntity) =
            ChannelsEntity.ChannelEntity(
                id = from.url?.toUniqueId()?:-1L,
                name = from.name.orEmpty(),
                image = from.image.orEmpty(),
                url = from.url.orEmpty()
            )
    }

}