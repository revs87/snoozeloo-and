package com.rvcoding.snoozeloo.ui.screen.settings

import com.rvcoding.snoozeloo.domain.model.Alarm
import kotlinx.serialization.Serializable


@Serializable
data class AlarmSettingsState(
    val alarm: Alarm
) {
    companion object {
        val Stub = AlarmSettingsState(Alarm.NewAlarm)
    }
}