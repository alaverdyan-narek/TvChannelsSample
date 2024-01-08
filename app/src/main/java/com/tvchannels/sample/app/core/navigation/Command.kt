package com.tvchannels.sample.app.core.navigation

import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

/**
 * Base Commands of Navigation
 */
sealed interface Command {
    object FinishAppCommand : Command
    object NavigateUpCommand : Command
    class NavCommand(
        val navDirections: NavDirections,
        val navOptions: NavOptions? = null,
        val isNested: Boolean = false
    ) : Command
}