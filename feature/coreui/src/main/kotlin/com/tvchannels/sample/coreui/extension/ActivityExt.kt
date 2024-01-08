package com.tvchannels.sample.coreui.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


fun <T> AppCompatActivity.collectWhenStarted(flow: Flow<T>, block: suspend (value: T) -> Unit) =
    flow.flowWithLifecycle(lifecycle)
        .onEach(block)
        .launchIn(lifecycleScope)

fun <T> AppCompatActivity.collectLatestWhenStarted(
    flow: Flow<T>,
    block: suspend (value: T) -> Unit
) {
    lifecycleScope.launch {
        flow.flowWithLifecycle(lifecycle).collectLatest { block(it) }
    }
}

fun <T> AppCompatActivity.collectWhenResumed(flow: Flow<T>, block: suspend (value: T) -> Unit) =
    flow.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
        .onEach(block)
        .launchIn(lifecycleScope)

fun <T> AppCompatActivity.collectLatestWhenResumed(
    flow: Flow<T>,
    block: suspend (value: T) -> Unit
) {
    lifecycleScope.launch {
        flow.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).collectLatest { block(it) }
    }
}


