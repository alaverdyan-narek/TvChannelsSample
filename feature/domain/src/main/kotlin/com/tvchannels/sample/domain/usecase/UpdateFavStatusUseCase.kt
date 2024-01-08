package com.tvchannels.sample.domain.usecase

import com.tvchannels.sample.domain.delegate.FavoriteAction
import com.tvchannels.sample.domain.entity.ChannelsEntity
import javax.inject.Inject

class UpdateFavStatusUseCase @Inject constructor() {
    operator fun invoke(
        data:List<ChannelsEntity.ChannelEntity>,action: FavoriteAction) = when (action) {
            is FavoriteAction.Clear -> data.map { it.copy(isFavorite = false) }
             else -> data.map {
            when{
                action is FavoriteAction.Add && it.id == action.id -> it.copy(isFavorite = true)
                action is FavoriteAction.Remove && it.id == action.id -> it.copy(isFavorite = false)
                else -> it
            }
        }
    }


}