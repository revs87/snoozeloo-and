package com.rvcoding.snoozeloo.navigation

import com.rvcoding.snoozeloo.domain.model.Alarm

sealed interface Actions {
    data class OnAddAlarmButtonClicked(val alarm: Alarm) : Actions
}