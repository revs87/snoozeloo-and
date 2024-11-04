package com.rvcoding.snoozeloo.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object YourAlarms: Route

    @Serializable
    data object AlarmSettings: Route

    @Serializable
    data object AlarmTrigger: Route
}