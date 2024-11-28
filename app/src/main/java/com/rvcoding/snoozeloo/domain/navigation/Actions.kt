package com.rvcoding.snoozeloo.domain.navigation

import com.rvcoding.snoozeloo.domain.model.Alarm

sealed interface Actions {
    sealed interface YourAlarms : Actions {
        data class OnAddAlarmButtonClicked(val alarm: Alarm) : YourAlarms
        data class OnAlarmCheckedChange(val id: Int, val checked: Boolean) : YourAlarms
        data class OnAlarmClicked(val id: Int) : YourAlarms
        data class OnAlarmDelete(val id: Int) : YourAlarms
    }

    sealed interface AlarmSettings : Actions {
        data object Close : AlarmSettings
        data class Save(val alarm: Alarm) : AlarmSettings
        data class Load(val alarmId: Int) : AlarmSettings
        data class OnTimeChange(
            val current: Alarm,
            val hour: Int,
            val minute: Int,
            val isAfternoon: Boolean
        ) : AlarmSettings
        data class OnNameChange(val name: String) : AlarmSettings
    }

    sealed interface AlarmTrigger : Actions {
        data class Load(val alarmId: Int) : AlarmTrigger
        data class TurnOff(val alarmId: Int) : AlarmTrigger
    }
}