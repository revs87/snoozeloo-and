package com.rvcoding.snoozeloo.domain.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {
    @Serializable
    data object YourAlarms: Destination

    @Serializable
    data class AlarmSettings(val id: Int): Destination

    @Serializable
    data class AlarmTrigger(val id: Int, val isTurnOff: Boolean = false): Destination
}