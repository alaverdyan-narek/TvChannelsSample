package com.tvchannels.sample.coreui.extension

import android.text.Editable

fun Editable?.toStringOrEmpty() = this?.toString().orEmpty()