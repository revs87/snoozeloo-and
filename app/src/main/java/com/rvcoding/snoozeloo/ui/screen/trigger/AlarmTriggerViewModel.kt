package com.rvcoding.snoozeloo.ui.screen.trigger

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.domain.navigation.Destination
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.navigation.Navigator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class AlarmTriggerViewModel(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val navigator: Navigator,
    private val dispatchersProvider: DispatchersProvider
) : ViewModel() {

    private var alarmId: Int = -1

    val state: StateFlow<AlarmTriggerState?> = snapshotFlow { alarmId }
        .map { id ->
            alarmRepository.getAlarm(id)?.let { alarm ->
                AlarmTriggerState(alarm)
            }
        }
        .stateIn(
            scope = viewModelScope + dispatchersProvider.io,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null
        )

    fun onAction(action: Actions.AlarmTrigger) {
        when (action) {
            is Actions.AlarmTrigger.Load -> {
                alarmId = action.alarmId
                println("AlarmTrigger loaded: id=$alarmId")
            }
            is Actions.AlarmTrigger.TurnOff -> {
                viewModelScope.launch(dispatchersProvider.io) {
                    alarmScheduler.removeNotification(action.alarmId)
                    alarmScheduler.stopRingtone()
                    navigator.navigate(
                        destination = Destination.YourAlarms,
                        navOptions = {
                            popUpTo(Destination.YourAlarms) {
                                inclusive = true
                            }
                        }
                    )
                }
            }
        }
    }
}