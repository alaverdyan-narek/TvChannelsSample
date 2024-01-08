package com.tvchannels.sample.domain.extensions

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import java.lang.Integer.max

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()


fun Int.toColor(context: Context) = ContextCompat.getColor(context, this)

fun Int.isZero() = this == 0

fun Int?.orZero() = this ?: 0
fun Int?.moreOrZero() = max(0, this.orZero())
