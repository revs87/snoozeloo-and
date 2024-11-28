package com.rvcoding.snoozeloo.ui.screen.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.domain.model.Time
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.navigation.Navigator
import com.rvcoding.snoozeloo.ui.util.fromLocalHoursAndMinutes24Format
import com.rvcoding.snoozeloo.ui.util.truncateToMinute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class AlarmSettingsViewModel(
    private val dispatchersProvider: DispatchersProvider,
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler,
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getAlarmBy(id: Int) = flow {
        if (id == -1) { emit(Alarm.NewAlarm) }
        else emit(alarmRepository.getAlarm(id) ?: Alarm.NewAlarm)
    }
        .flatMapLatest { flow { emit(it) } }
        .onEach { saveData(AlarmSettingsState(it)) }
        .flowOn(dispatchersProvider.io)
        .launchIn(viewModelScope)

    private fun saveData(value: AlarmSettingsState) { savedStateHandle[SAVE_STATE_KEY] = Json.encodeToString(AlarmSettingsState.serializer(), value) }
    @OptIn(FlowPreview::class)
    val state: StateFlow<AlarmSettingsState> = savedStateHandle.getStateFlow(
        key = SAVE_STATE_KEY,
        initialValue = Json.encodeToString(AlarmSettingsState.serializer(), AlarmSettingsState.Initial)
    )
        .map { Json.decodeFromString(AlarmSettingsState.serializer(), it) }
        .debounce(500L)
        .onEach {
            println("STATE: $it")
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000L),
            initialValue = AlarmSettingsState.Initial
        )

    fun onAction(action: Actions.AlarmSettings) {
        viewModelScope.launch(dispatchersProvider.io) {
            when (action) {
                Actions.AlarmSettings.Close -> navigator.navigateUp()
                is Actions.AlarmSettings.Save -> {
                    println("Saving: id=${action.alarm.id} hour=${action.alarm.time.localHours} minute=${action.alarm.time.localMinutes} meridiem=${action.alarm.time.localMeridiem}")
                    action.alarm.let {
                        if (it.id == -1) {
                            alarmRepository.addAlarm(it.copy(
                                enabled = true,
                                time = it.time.copy(utcTime = it.time.utcTime.truncateToMinute())
                            ))
                        }
                        else {
                            alarmRepository.getAlarm(action.alarm.id)?.let {
                                alarmScheduler.cancel(it.id)
                            }
                            alarmRepository.updateAlarm(it.copy(
                                enabled = true,
                                time = it.time.copy(utcTime = it.time.utcTime.truncateToMinute())
                            ))
                        }
                    }
                    alarmScheduler.schedule(action.alarm)
                    navigator.navigateUp()
                }
                is Actions.AlarmSettings.Load -> {
                    getAlarmBy(action.alarmId)
                }
                is Actions.AlarmSettings.OnTimeChange -> {
                    val alarm = action.current.copy(
                        time = Time(
                            utcTime = Triple(
                                action.hour.toString(),
                                action.minute.toString(),
                                action.isAfternoon
                            ).fromLocalHoursAndMinutes24Format()
                        )
                    )
                    saveData(AlarmSettingsState(alarm))
                }
                is Actions.AlarmSettings.OnNameChange -> {
                    val alarm = state.value.alarm.copy(name = action.name)
                    saveData(AlarmSettingsState(alarm))
                }
            }
        }
    }

    companion object {
        const val SAVE_STATE_KEY = "alarmSettings"
    }
}