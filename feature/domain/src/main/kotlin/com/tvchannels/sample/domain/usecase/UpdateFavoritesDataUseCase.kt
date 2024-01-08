package com.tvchannels.sample.domain.usecase

import com.tvchannels.sample.domain.delegate.FavoriteAction
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.repository.IChannelsRepository
import javax.inject.Inject

class UpdateFavoritesDataUseCase @Inject constructor(private val repository: IChannelsRepository) {
    suspend operator fun invoke(data: List<ChannelsEntity.ChannelEntity>, action: FavoriteAction) =
        when (action) {
            is FavoriteAction.Clear -> emptyList()
            is FavoriteAction.Add -> buildList {
                repository.getCachedChannelById(action.id)?.let {
                    add(it.copy(isFavorite = true))
                }
                addAll(data)
            }
            is FavoriteAction.Remove -> data.filter { it.id != action.id }
        }
}