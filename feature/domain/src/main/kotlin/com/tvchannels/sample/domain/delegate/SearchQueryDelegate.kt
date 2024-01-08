package com.tvchannels.sample.domain.delegate

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchQueryDelegate @Inject constructor() {
    val query = MutableStateFlow("")
}