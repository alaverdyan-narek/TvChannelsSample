package com.tvchannels.sample.app.ui.main

import android.text.Editable
import androidx.lifecycle.viewModelScope
import com.tvchannels.sample.app.core.base.BaseViewModel
import com.tvchannels.sample.app.utils.RString
import com.tvchannels.sample.coreui.extension.toStringOrEmpty
import com.tvchannels.sample.domain.delegate.SearchQueryDelegate
import com.tvchannels.sample.domain.utils.TextSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

typealias OnSearchChange = (Editable?) -> Unit

@HiltViewModel
class MainChannelViewModel @Inject constructor(
    private val mainChannelsDelegate: MainChannelsDelegate,
    private val searchQueryDelegate: SearchQueryDelegate,
) : BaseViewModel() {
    private val _mainState = MutableStateFlow(
        MainChannelState(
            tabNames = buildList {
                add(TextSource.Resource(RString.channel_tab_all))
                add(TextSource.Resource(RString.channel_tab_favs))
            },
            offsetScreenPageLimit = OFFSCREEN_LIMIT
        )
    )

    val mainState = _mainState.asStateFlow()

    val showLoading = mainChannelsDelegate.loading.asStateFlow()

    val searchChange: OnSearchChange = {
        searchQueryDelegate.query.value = it.toStringOrEmpty()
    }
    companion object {
        const val OFFSCREEN_LIMIT = 3
    }

}