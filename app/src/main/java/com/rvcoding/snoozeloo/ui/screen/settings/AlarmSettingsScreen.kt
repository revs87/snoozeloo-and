@file:OptIn(ExperimentalMaterial3Api::class)

package com.rvcoding.snoozeloo.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcoding.snoozeloo.domain.model.TimeFormatPreference
import com.rvcoding.snoozeloo.domain.model.toAlarmInfo
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.TopBar
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.TextDisabled
import com.rvcoding.snoozeloo.ui.theme.TextPrimary
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import java.util.Date
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
                val focusRequester = remember { FocusRequester() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .clickable {
                            focusRequester.requestFocus()
                        }
                ) {
                    val currentTime = Calendar.getInstance(TimeZone.getDefault())
                    currentTime.timeInMillis = state.alarm.time.utcTime
                    currentTime.time = Date(state.alarm.time.utcTime)
                    println("currentTime: ${state.alarm.time.utcTime}")

                    val timePickerState = rememberTimePickerState(
                        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                        initialMinute = currentTime.get(Calendar.MINUTE),
                        is24Hour = TimeFormatPreference.is24HourFormat(),
                    )

                    LaunchedEffect(state.alarm.time.utcTime) {
                        timePickerState.hour = state.alarm.time.localHours.toInt()
                        timePickerState.minute = state.alarm.time.localMinutes.toInt()
                        timePickerState.isAfternoon = state.alarm.time.localMeridiem == "PM"
                    }

                    LaunchedEffect(
                        key1 = timePickerState.hour,
                        key2 = timePickerState.minute,
                        key3 = timePickerState.isAfternoon,
                    ) {
                        onAction.invoke(
                            Actions.AlarmSettings.OnTimeChange(
                                state.alarm,
                                timePickerState.hour,
                                timePickerState.minute,
                                timePickerState.isAfternoon
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 24.dp),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TimeInput(
                            modifier = Modifier,
                            state = timePickerState,
                            colors = TimePickerDefaults.colors().copy(
                                clockDialColor = MaterialTheme.colorScheme.background,
                                selectorColor = MaterialTheme.colorScheme.background,
                                containerColor = MaterialTheme.colorScheme.surface,
                                clockDialSelectedContentColor = MaterialTheme.colorScheme.surface,
                                clockDialUnselectedContentColor = MaterialTheme.colorScheme.surface,
                                periodSelectorBorderColor = MaterialTheme.colorScheme.background,
                                periodSelectorSelectedContainerColor = Primary,
                                periodSelectorSelectedContentColor = TextDisabled,
                                periodSelectorUnselectedContainerColor = Color.Transparent,
                                periodSelectorUnselectedContentColor = TextPrimary,
                                timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.background,
                                timeSelectorSelectedContentColor = TextPrimary,
                                timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                                timeSelectorUnselectedContentColor = TextPrimary
                            )
                        )

                        Text(
                            text = state.alarm.toAlarmInfo().timeLeft.asString(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenNonEmptyPreview() {
    AlarmSettingsScreen(state = AlarmSettingsState.Stub, onAction = {})
}
