package com.tvchannels.sample.domain.usecase

import com.tvchannels.sample.domain.repository.IChannelsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RemoveChannelFavUseCase @Inject constructor(private val repository: IChannelsRepository) {
    operator fun invoke(id: Long) = repository.removeChannelFromFav(id).flowOn(Dispatchers.IO)
}