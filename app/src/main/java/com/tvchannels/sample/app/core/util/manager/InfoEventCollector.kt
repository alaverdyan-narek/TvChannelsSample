package com.tvchannels.sample.app.core.util.manager

import androidx.fragment.app.Fragment
import com.tvchannels.sample.coreui.extension.collectWhenStarted
import com.tvchannels.sample.coreui.extension.errorDialog
import com.tvchannels.sample.domain.event.InfoEvent
import kotlinx.coroutines.flow.Flow

interface InfoEventCollector {

    fun collectInfoEvents(host: Fragment, events: Flow<InfoEvent>) {
        host.collectWhenStarted(events) { event ->
            onEventReceived(host, event)
        }
    }

    fun onEventReceived(host: Fragment, event: InfoEvent)
}

class InfoEventCollectorImpl : InfoEventCollector {

    override fun onEventReceived(host: Fragment, event: InfoEvent) = when (event) {
        is InfoEvent.ErrorEvent -> host.errorDialog(
            title = event.title.asString(host.requireContext()),
            message = event.message.asString(host.requireContext()),
            buttonText = event.btnText.asString(host.requireContext())
        ) { }.show()
    }
}