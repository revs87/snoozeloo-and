package com.rvcoding.snoozeloo.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.UiText
import com.rvcoding.snoozeloo.ui.component.UiText.DynamicString
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo
import com.rvcoding.snoozeloo.ui.screen.list.model.TimeFormat
import com.rvcoding.snoozeloo.ui.screen.list.model.TimeFormatPreference
import com.rvcoding.snoozeloo.ui.util.meridianAsString
import com.rvcoding.snoozeloo.ui.util.timeAsString
import com.rvcoding.snoozeloo.ui.util.timeLeftAsString
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class YourAlarmsViewModel(
    private val dispatchersProvider: DispatchersProvider,
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    val alarms: StateFlow<YourAlarmsState> = alarmRepository
        .getAlarms()
        .map { YourAlarmsState(it.map { alarm ->
            AlarmInfo(
                id = alarm.id,
                enabled = alarm.enabled,
                name = DynamicString(alarm.name),
                timeFormat = when (TimeFormatPreference.is24HourFormat()){
                    true -> TimeFormat.Time24(time = timeAsString(alarm.time, true))
                    false -> TimeFormat.Time12(time = timeAsString(alarm.time, false), amOrPm = meridianAsString(alarm.time))
                },
                timeLeft = UiText.StringResource(R.string.alarm_time_left, arrayOf(timeLeftAsString(alarm.time)))
            ).also { println("Alarm: $it") }
        }) }
        .onEach { println("Alarms: $it") }
        .stateIn(
            scope = viewModelScope + dispatchersProvider.io,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = YourAlarmsState(emptyList())
        )

    fun onAction(action: Actions) {
        viewModelScope.launch(dispatchersProvider.io) {
            when (action) {
                is Actions.OnAddAlarmButtonClicked -> alarmRepository.addAlarm(action.alarm)
                is Actions.OnAlarmCheckedChange -> alarmRepository.updateAlarmEnabled(id = action.id, enabled = action.checked)
                is Actions.OnAlarmClicked -> { /*TODO*/ }
            }
        }
    }
}