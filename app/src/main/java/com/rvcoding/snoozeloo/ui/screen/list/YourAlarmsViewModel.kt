package com.rvcoding.snoozeloo.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.model.toAlarmInfo
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.domain.navigation.Destination.AlarmSettings
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.navigation.Navigator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class YourAlarmsViewModel(
    private val dispatchersProvider: DispatchersProvider,
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val navigator: Navigator
) : ViewModel() {

    val alarms: StateFlow<YourAlarmsState> = alarmRepository
        .getAlarms()
        .map { YourAlarmsState(it.map { alarm -> alarm.toAlarmInfo() }) }
        .onEach { println("Alarms: $it") }
        .stateIn(
            scope = viewModelScope + dispatchersProvider.io,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = YourAlarmsState(emptyList())
        )

    fun onAction(action: Actions.YourAlarms) {
        viewModelScope.launch(dispatchersProvider.io) {
            when (action) {
                is Actions.YourAlarms.OnAddAlarmButtonClicked ->  navigator.navigate(destination = AlarmSettings(-1))
                is Actions.YourAlarms.OnAlarmCheckedChange -> {
                    alarmRepository.updateAlarmEnabled(id = action.id, enabled = action.checked)
                    alarmRepository.getAlarm(id = action.id)?.let { alarm ->
                        if (alarm.enabled) {
                            alarmScheduler.schedule(alarm)
                        } else {
                            alarmScheduler.cancel(alarm.id)
                        }
                    }
                }
                is Actions.YourAlarms.OnAlarmClicked -> navigator.navigate(destination = AlarmSettings(action.id))
                is Actions.YourAlarms.OnAlarmDelete -> alarmRepository.deleteAlarm(id = action.id)
            }
        }
    }
}
