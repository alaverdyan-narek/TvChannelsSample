package com.tvchannels.sample.domain.extensions

fun String.toUniqueId(): Long = this.hashCode().toLong() and 0xffffffffL
