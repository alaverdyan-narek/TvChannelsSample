package com.tvchannels.sample.app.ui.main.player

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.tvchannels.sample.app.core.base.BaseViewModel
import com.tvchannels.sample.app.core.ext.getOrThrow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class PlayerArgs(
    val streamUrl: String,
    val imageUrl:String,
    val name:String
) : Parcelable

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val args: PlayerArgs get() = savedStateHandle.getOrThrow()
    private val _state = MutableStateFlow(PlayerState(
        title = args.name,
        subTitle = "No Information",
        imageUrl = args.imageUrl
    ))
    val state = _state.asStateFlow()

}