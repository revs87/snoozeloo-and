package com.rvcoding.snoozeloo.ui.navigation

import androidx.navigation.NavOptionsBuilder
import com.rvcoding.snoozeloo.domain.navigation.Destination


sealed interface NavigationAction {

    data class Navigate(
        val destination: Destination,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationAction

    data object NavigateUp : NavigationAction
}