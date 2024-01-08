package com.tvchannels.sample.app.ui.main.favorites

import androidx.lifecycle.viewModelScope
import com.tvchannels.sample.app.core.base.BaseViewModel
import com.tvchannels.sample.app.core.navigation.Command
import com.tvchannels.sample.app.ui.main.MainChannelFragmentDirections
import com.tvchannels.sample.app.ui.main.adapter.ChannelItem
import com.tvchannels.sample.app.ui.main.player.PlayerArgs
import com.tvchannels.sample.app.utils.mapper.MapperChannelEntityToItem
import com.tvchannels.sample.domain.delegate.IFavoriteActionDelegate
import com.tvchannels.sample.domain.delegate.SearchQueryDelegate
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.usecase.GetAllFavoriteChannelsUseCase
import com.tvchannels.sample.domain.usecase.RemoveChannelFavUseCase
import com.tvchannels.sample.domain.usecase.SearchChannelUseCase
import com.tvchannels.sample.domain.usecase.UpdateFavoritesDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val removeChannelFavUseCase: RemoveChannelFavUseCase,
    private val getAllFavoriteChannelsUseCase: GetAllFavoriteChannelsUseCase,
    private val searchChannelUseCase: SearchChannelUseCase,
    private val searchQueryDelegate: SearchQueryDelegate,
    private val updateFavoritesDataUseCase: UpdateFavoritesDataUseCase,
    favoriteActionDelegate: IFavoriteActionDelegate
) : BaseViewModel() {

    private val channels = MutableSharedFlow<List<ChannelsEntity.ChannelEntity>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _state = MutableStateFlow(FavoriteState(emptyList()))
    val state = _state.asStateFlow()
    private val _error = MutableSharedFlow<Unit>()
    val error = _error.asSharedFlow()

    init {
        getAllFavoriteChannelsUseCase().onEach {
            channels.emit(it)
        }.catch {
            _error.emit(Unit)
        }.launchIn(viewModelScope)
        favoriteActionDelegate.action.onEach {
            channels.emit(
                updateFavoritesDataUseCase(
                    channels.replayCache.firstOrNull().orEmpty(), it
                )
            )
        }.launchIn(viewModelScope)

        combine(
            channels,
            searchQueryDelegate.query,
        ) { channels, search ->
            _state.value = FavoriteState(
                MapperChannelEntityToItem().map(
                    searchChannelUseCase(
                        data = channels,
                        query = search
                    )
                )
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        ).launchIn(viewModelScope)
    }

    private var favActionJob: Job? = null
    fun favAction(id: Long) {
        favActionJob?.cancel()
        favActionJob = favRemoveAction(id)
    }

    private fun favRemoveAction(id: Long) =
        removeChannelFavUseCase(id).catch {
            _error.emit(Unit)
        }.launchIn(viewModelScope)


    fun navigateToPlayer(item: ChannelItem) {
        val dir = MainChannelFragmentDirections.actionToPlayer(
            args = PlayerArgs(
                streamUrl = item.streamUrl,
                name = item.name,
                imageUrl = item.imageUrl
            )
        )
        sendCommand(Command.NavCommand(dir))
    }
}