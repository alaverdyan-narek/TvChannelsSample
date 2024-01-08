package com.tvchannels.sample.app.ui.main.channels

import androidx.lifecycle.viewModelScope
import com.tvchannels.sample.app.core.base.BaseViewModel
import com.tvchannels.sample.app.core.navigation.Command
import com.tvchannels.sample.app.ui.main.MainChannelFragmentDirections
import com.tvchannels.sample.app.ui.main.MainChannelsDelegate
import com.tvchannels.sample.app.ui.main.adapter.ChannelItem
import com.tvchannels.sample.app.ui.main.player.PlayerArgs
import com.tvchannels.sample.app.utils.mapper.MapperChannelEntityToItem
import com.tvchannels.sample.domain.delegate.IFavoriteActionDelegate
import com.tvchannels.sample.domain.delegate.SearchQueryDelegate
import com.tvchannels.sample.domain.entity.ChannelsEntity
import com.tvchannels.sample.domain.usecase.AddChannelFavUseCase
import com.tvchannels.sample.domain.usecase.GetChannelsUseCase
import com.tvchannels.sample.domain.usecase.RemoveChannelFavUseCase
import com.tvchannels.sample.domain.usecase.SearchChannelUseCase
import com.tvchannels.sample.domain.usecase.UpdateFavStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val mainDelegate: MainChannelsDelegate,
    private val getChannelsUseCase: GetChannelsUseCase,
    private val searchChannelUseCase: SearchChannelUseCase,
    private val searchQueryDelegate: SearchQueryDelegate,
    private val updateFavStatusUseCase: UpdateFavStatusUseCase,
    private val addChannelFavUseCase: AddChannelFavUseCase,
    private val removeChannelFavUseCase: RemoveChannelFavUseCase,
    favoriteActionDelegate: IFavoriteActionDelegate
) : BaseViewModel() {

    private val channels = MutableSharedFlow<List<ChannelsEntity.ChannelEntity>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val _state = MutableStateFlow<ChannelState>(ChannelState.NotLoaded)
    val state = _state.asStateFlow()
    private val _error = MutableSharedFlow<Unit>()
    val error = _error.asSharedFlow()

    init {
        fetchChannels()
        favoriteActionDelegate.action.onEach {
            channels.emit(
                updateFavStatusUseCase(channels.replayCache.firstOrNull().orEmpty(), it)
            )
        }.launchIn(viewModelScope)

        combine(
            channels,
            searchQueryDelegate.query,
        ) { channels, search ->
            _state.value = ChannelState.DataList(
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

    private var favJob: Job? = null
    fun favAction(id: Long) {
        favJob?.cancel()
        channels.replayCache.firstOrNull().orEmpty().find { it.id == id }?.let {
            favJob = if (it.isFavorite) {
                favRemoveAction(it.id)
            } else {
                favAddAction(it.id)
            }
        }
    }


    private fun favAddAction(id: Long) = addChannelFavUseCase(id)
        .catch {
            _error.emit(Unit)
        }
        .cancellable()
        .launchIn(viewModelScope)

    private fun favRemoveAction(id: Long) = removeChannelFavUseCase(id)
        .catch {
            _error.emit(Unit)
        }.cancellable()
        .launchIn(viewModelScope)


    fun retry() {
        if (blockRetry()) return
        fetchChannels()
    }

    private fun blockRetry() = state.value !is ChannelState.NotLoaded


    private var channelsJob: Job? = null
    private fun fetchChannels() {
        channelsJob?.cancel()
        channelsJob = getChannelsUseCase().onStart {
            mainDelegate.loading.value = true
        }.onEach {
            channels.emit(it.channels)
        }.catch {
            _error.emit(Unit)
        }.onCompletion {
            mainDelegate.loading.value = false
        }.launchIn(viewModelScope)
        searchQueryDelegate.query.onEach {
        }.launchIn(viewModelScope)
    }

    fun navigateToPlayer(item: ChannelItem) {
        val dir = MainChannelFragmentDirections.actionToPlayer(
            args = PlayerArgs(streamUrl = item.streamUrl, name = item.name, imageUrl = item.imageUrl)
        )
        sendCommand(Command.NavCommand(dir))
    }
}