package com.tvchannels.sample.app.core.ext

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow

fun <T> SavedStateHandle.getOrThrow(key: String = "args"): T = get<T>(key) ?: error(
    "not found arguments with key [$key] in entry"
)

fun <T> SavedStateHandle.getOrDefault(key: String = "args", default: T): T = get<T>(key) ?: default

fun <T> SavedStateHandle.asStateFlow(key: String = "args", initialValue: T): StateFlow<T> =
    getStateFlow(key, initialValue)

fun <T> SavedStateHandle.setArg(key: String = "args", value: T) = set(key, value)