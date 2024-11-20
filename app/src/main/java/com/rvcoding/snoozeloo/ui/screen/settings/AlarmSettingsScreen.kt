@file:OptIn(ExperimentalMaterial3Api::class)

package com.rvcoding.snoozeloo.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcoding.snoozeloo.domain.model.Time
import com.rvcoding.snoozeloo.domain.model.TimeFormatPreference
import com.rvcoding.snoozeloo.domain.model.toAlarmInfo
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.TimeDisplay
import com.rvcoding.snoozeloo.ui.component.TimePickerDialog
import com.rvcoding.snoozeloo.ui.component.TopBar
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.TextDisabled
import com.rvcoding.snoozeloo.ui.theme.TextSecondary
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import java.util.TimeZone


@Composable
fun AlarmSettingsScreenRoot(
    vm: AlarmSettingsViewModel = koinViewModel(),
    alarmId: Int = -1
) {
    val state by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(alarmId) {
        vm.onAction(Actions.AlarmSettings.Load(alarmId))
    }

    AlarmSettingsScreen(
        state = state,
        onAction = vm::onAction,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmSettingsScreen(
    state: AlarmSettingsState,
    onAction: (Actions.AlarmSettings) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                item = TopBar.Savable(
                    isDarkTheme = isDarkTheme(),
                    rightButtonEnabled = true,
                    onRightButtonClicked = { onAction.invoke(Actions.AlarmSettings.Save(state.alarm)) },
                    onLeftButtonClicked = { onAction.invoke(Actions.AlarmSettings.Close) }
                )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Center
                ) {
                    val currentTime = Calendar.getInstance(TimeZone.getDefault())
                    currentTime.timeInMillis = state.alarm.time.utcTime

                    if (state.alarm.time.isInitial) {
                        val timePickerState = rememberTimePickerState(
                            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                            initialMinute = currentTime.get(Calendar.MINUTE),
                            is24Hour = TimeFormatPreference.is24HourFormat()
                        )
                        TimeContent(timePickerState, state, onAction)
                    } else {
                        val timePickerState = rememberTimePickerState(
                            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                            initialMinute = currentTime.get(Calendar.MINUTE),
                            is24Hour = TimeFormatPreference.is24HourFormat()
                        )
                        TimeContent(timePickerState, state, onAction)
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimeContent(
    timePickerState: TimePickerState,
    state: AlarmSettingsState,
    onAction: (Actions.AlarmSettings) -> Unit
) {
    println("[TimeContent] currentTime: ${state.alarm.time.utcTime}")

    LaunchedEffect(
        key1 = timePickerState.hour,
        key2 = timePickerState.minute,
        key3 = timePickerState.isAfternoon
    ) {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
        cal.set(Calendar.MINUTE, timePickerState.minute)
        cal.set(Calendar.AM_PM, if (timePickerState.isAfternoon) Calendar.PM else Calendar.AM)
        println("[TimeContent] TimeDisplay: hrs=${timePickerState.hour} min=${timePickerState.minute} isAfternoon=${timePickerState.isAfternoon} millis=${cal.timeInMillis}")
        onAction.invoke(
            Actions.AlarmSettings.OnTimeChange(
                state.alarm.copy(
                    time = Time(
                        utcTime = cal.timeInMillis
                    )
                ),
                timePickerState.hour,
                timePickerState.minute,
                timePickerState.isAfternoon
            )
        )
    }

    var dialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 24.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TimeDisplay(
            state = timePickerState,
            colors = TimePickerDefaults.colors().copy(
                clockDialColor = MaterialTheme.colorScheme.background,
                selectorColor = Primary,
                containerColor = MaterialTheme.colorScheme.background,
                clockDialSelectedContentColor = MaterialTheme.colorScheme.surface,
                clockDialUnselectedContentColor = TextSecondary,
                periodSelectorBorderColor = MaterialTheme.colorScheme.background,
                periodSelectorSelectedContainerColor = Primary,
                periodSelectorSelectedContentColor = TextDisabled,
                periodSelectorUnselectedContainerColor = Color.Transparent,
                periodSelectorUnselectedContentColor = TextSecondary,
                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.background,
                timeSelectorSelectedContentColor = if (state.alarm.time.isInitial) TextSecondary else Primary,
                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                timeSelectorUnselectedContentColor = if (state.alarm.time.isInitial) TextSecondary else Primary
            ),
            onClick = { dialogVisible = true }
        )

        if (dialogVisible) {
            TimePickerDialog(
                state = state,
                timePickerState = timePickerState,
                onConfirm = { tps ->
                    onAction.invoke(
                        Actions.AlarmSettings.OnTimeChange(
                            state.alarm,
                            tps.hour,
                            tps.minute,
                            tps.isAfternoon
                        )
                    )
                    dialogVisible = false
                },
                onDismiss = {
                    dialogVisible = false
                },
                onAction = onAction
            )
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = state.alarm.toAlarmInfo().timeLeft.asString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenNonEmptyPreview() {
    AlarmSettingsScreen(state = AlarmSettingsState.Initial, onAction = {})
}
