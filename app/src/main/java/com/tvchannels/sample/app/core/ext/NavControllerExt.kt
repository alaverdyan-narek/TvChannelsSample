package com.tvchannels.sample.app.core.ext

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.tvchannels.sample.BuildConfig


@MainThread
fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null) {
        navigate(resId, args, navOptions, navExtras)
    } else {
        if (BuildConfig.DEBUG) throw Exception("Destination not found")
    }
}

@MainThread
fun NavController.navigateSafe(
    directions: NavDirections,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras?=null,
) {
    navigateSafe(directions.actionId, directions.arguments, navOptions, navExtras)
}