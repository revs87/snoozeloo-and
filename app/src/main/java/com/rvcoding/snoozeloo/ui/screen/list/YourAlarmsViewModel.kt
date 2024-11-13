package com.rvcoding.snoozeloo.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.domain.model.TimeFormatPreference
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.UiText
import com.rvcoding.snoozeloo.ui.component.UiText.DynamicString
import com.rvcoding.snoozeloo.ui.component.UiText.StringResource
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo.Companion.HOURS
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo.Companion.HOURS_DURATION
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo.Companion.showSleepRecommendation
import com.rvcoding.snoozeloo.ui.screen.list.model.TimeFormat
import com.rvcoding.snoozeloo.ui.util.nextAlarmTime
import com.rvcoding.snoozeloo.ui.util.timeLeftAsString
import com.rvcoding.snoozeloo.ui.util.timeWithMeridiemAsString
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
        .map { YourAlarmsState(it.map { alarm -> alarm.toAlarmInfo().also { println("Alarm: $it") } }) }
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
                is Actions.OnAlarmDelete -> alarmRepository.deleteAlarm(id = action.id)
            }
        }
    }
}

fun Alarm.toAlarmInfo(): AlarmInfo = AlarmInfo(
    id = this.id,
    enabled = this.enabled,
    name = DynamicString(this.name),
    timeFormat = when (TimeFormatPreference.is24HourFormat()){
        true -> TimeFormat.Time24(hours = this.time.localHours, minutes = this.time.localMinutes)
        false -> TimeFormat.Time12(hours = this.time.localHours, minutes = this.time.localMinutes, meridiem = this.time.localMeridiem)
    },
    timeLeft = if (showSleepRecommendation(this.time.utcTime)) {
        StringResource(R.string.alarm_recommendation, arrayOf(
            timeWithMeridiemAsString(
                utcTime = this.time.utcTime - HOURS_DURATION,
                is24HourFormat = TimeFormatPreference.is24HourFormat()
            ),
            HOURS
        ))
    } else {
        StringResource(R.string.alarm_time_left, arrayOf(timeLeftAsString(this.time.utcTime.nextAlarmTime())))
    }
)