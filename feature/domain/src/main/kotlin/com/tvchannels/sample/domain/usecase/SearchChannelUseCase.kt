package com.tvchannels.sample.domain.usecase

import com.tvchannels.sample.domain.entity.ChannelsEntity
import javax.inject.Inject

class SearchChannelUseCase@Inject constructor() {
    operator fun invoke(data:List<ChannelsEntity.ChannelEntity>,query:String) = data.run {
        if (query.isNotBlank()) {
            return filter { item -> item.name.contains(query) }
        }
       this
    }
}