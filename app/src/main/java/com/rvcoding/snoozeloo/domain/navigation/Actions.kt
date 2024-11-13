package com.rvcoding.snoozeloo.domain.navigation

import com.rvcoding.snoozeloo.domain.model.Alarm

sealed interface Actions {
    sealed interface YourAlarms : Actions {
        data class OnAddAlarmButtonClicked(val alarm: Alarm) : YourAlarms
        data class OnAlarmCheckedChange(val id: Int, val checked: Boolean) : YourAlarms
        data class OnAlarmClicked(val id: Int) : YourAlarms
        data class OnAlarmDelete(val id: Int) : YourAlarms
    }
}