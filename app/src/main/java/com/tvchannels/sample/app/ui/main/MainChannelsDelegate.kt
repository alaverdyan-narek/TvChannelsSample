package com.tvchannels.sample.app.ui.main

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainChannelsDelegate @Inject constructor() {
    val loading = MutableStateFlow(false)
}


