package com.rvcoding.snoozeloo.ui.list

import androidx.compose.runtime.Immutable
import com.rvcoding.snoozeloo.model.AlarmInfo


@Immutable
data class YourAlarmsState(
    val alarms: List<AlarmInfo>
) {
    companion object {
        val Empty: YourAlarmsState = YourAlarmsState(emptyList())
        val NonEmpty: YourAlarmsState = YourAlarmsState(listOf(AlarmInfo.Stub))
    }
}