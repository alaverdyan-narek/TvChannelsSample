package com.tvchannels.sample.domain.delegate
import kotlinx.coroutines.flow.SharedFlow

sealed interface FavoriteAction {
    class Add(val id: Long) : FavoriteAction
    class Remove(val id: Long) : FavoriteAction
    object Clear : FavoriteAction
}
interface IFavoriteActionDelegate{
    val action : SharedFlow<FavoriteAction>
}