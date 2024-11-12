package com.rvcoding.snoozeloo.navigation

import com.rvcoding.snoozeloo.domain.model.Alarm

sealed interface Actions {
    data class OnAddAlarmButtonClicked(val alarm: Alarm) : Actions
    data class OnAlarmCheckedChange(val id : Int, val checked: Boolean) : Actions
    data class OnAlarmClicked(val id : Int) : Actions
    data class OnAlarmDelete(val id : Int) : Actions
}