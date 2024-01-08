package com.tvchannels.sample.app.utils.mapper

import com.tvchannels.sample.app.ui.main.adapter.ChannelItem
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.utils.Mapper

class MapperChannelEntityToItem : Mapper<ChannelsEntity.ChannelEntity, ChannelItem> {
    override fun map(from: ChannelsEntity.ChannelEntity) = ChannelItem(
        id = from.id,
        name = from.name,
        streamUrl = from.url,
        imageUrl = from.image,
        isFavorite = from.isFavorite
    )
}