package com.rvcoding.snoozeloo.domain.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object YourAlarms: Destination

    @Serializable
    data class AlarmSettings(val id: Int): Destination

    @Serializable
    data object AlarmTrigger: Destination
}