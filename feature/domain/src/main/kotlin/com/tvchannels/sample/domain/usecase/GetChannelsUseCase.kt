package com.tvchannels.sample.domain.usecase

import com.tvchannels.sample.domain.repository.IChannelsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetChannelsUseCase @Inject constructor(
   private val channelRepo:IChannelsRepository
) {
   operator fun invoke() = channelRepo.getChannels().flowOn(Dispatchers.IO)
}