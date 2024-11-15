package com.rvcoding.snoozeloo.ui.screen.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.navigation.Navigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
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
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentAlarmId: MutableStateFlow<Int> = MutableStateFlow(-1)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val alarmJob = currentAlarmId
        .flatMapLatest { value ->
            flow {
                if (value == -1) { emit(Alarm.NewAlarm) }
                else emit(alarmRepository.getAlarm(value) ?: Alarm.NewAlarm)
            }
        }
        .onEach { savedStateHandle[SAVE_STATE_KEY] = Json.encodeToString(AlarmSettingsState.serializer(), AlarmSettingsState(it)) }
        .flowOn(dispatchersProvider.io)
        .launchIn(viewModelScope)

    val state: StateFlow<AlarmSettingsState> = savedStateHandle.getStateFlow(
        key = SAVE_STATE_KEY,
        initialValue = Json.encodeToString(AlarmSettingsState.serializer(), AlarmSettingsState.Stub)
    )
        .map { Json.decodeFromString(AlarmSettingsState.serializer(), it) }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = AlarmSettingsState.Stub
        )

    fun onAction(action: Actions.AlarmSettings) {
        viewModelScope.launch(dispatchersProvider.io) {
            when (action) {
                Actions.AlarmSettings.Close -> navigator.navigateUp()
                Actions.AlarmSettings.Save -> {
                    state.value.alarm.let {
                        if (it.id == -1) alarmRepository.addAlarm(it)
                        else alarmRepository.updateAlarm(it)
                    }
                    navigator.navigateUp()
                }
                Actions.AlarmSettings.OpenTimePicker -> {}
                Actions.AlarmSettings.CloseNameDialog -> {}
                Actions.AlarmSettings.OpenNameDialog -> {}
                Actions.AlarmSettings.SaveNameDialog -> {}
            }
        }
    }

    companion object {
        const val SAVE_STATE_KEY = "alarmSettings"
    }
}