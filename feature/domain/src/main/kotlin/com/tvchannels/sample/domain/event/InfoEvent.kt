package com.tvchannels.sample.domain.event

import com.tvchannels.sample.R
import com.tvchannels.sample.domain.utils.TextSource

sealed interface InfoEvent {
    data class ErrorEvent(
        val title: TextSource = TextSource.Resource(R.string.error_title),
        val message: TextSource = TextSource.Resource(R.string.something_went_wrong),
        val btnText: TextSource = TextSource.Resource(R.string.ok)
    ) : InfoEvent
}