package com.tvchannels.sample.data.delegate

import com.tvchannels.sample.domain.delegate.FavoriteAction
import com.tvchannels.sample.domain.delegate.IFavoriteActionDelegate
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton



/**
 * for sending events about adding or removing
 * use this only for sending events. For read only use @IFavoriteActionDelegate
 */

@Singleton
class FavoriteActionDelegate @Inject constructor(): IFavoriteActionDelegate {

    private val _action = MutableSharedFlow<FavoriteAction>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST)
   override val  action = _action.asSharedFlow()

    suspend fun sendAction(action: FavoriteAction){
        _action.emit(action)
    }
}





