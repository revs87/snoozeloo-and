@file:OptIn(ExperimentalMaterial3Api::class)

package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.domain.model.Time
import com.rvcoding.snoozeloo.domain.model.TimeFormatPreference
import com.rvcoding.snoozeloo.domain.model.toAlarmInfo
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsState
import com.rvcoding.snoozeloo.ui.theme.Background
import com.rvcoding.snoozeloo.ui.theme.BackgroundCard
import com.rvcoding.snoozeloo.ui.theme.BackgroundCardDark
import com.rvcoding.snoozeloo.ui.theme.BackgroundDark
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.TextDisabled
import com.rvcoding.snoozeloo.ui.theme.TextDisabledDark
import com.rvcoding.snoozeloo.ui.theme.TextSecondary
import com.rvcoding.snoozeloo.ui.theme.TextSecondaryDark
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import java.util.Calendar
import java.util.TimeZone


@Composable
fun TimeCard(
    state: AlarmSettingsState,
    onAction: (Actions.AlarmSettings) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard
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
                clockDialColor = if (isDarkTheme()) BackgroundDark else Background,
                selectorColor = if (isDarkTheme()) PrimaryDark else Primary,
                containerColor = if (isDarkTheme()) BackgroundDark else Background,
                clockDialSelectedContentColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard,
                clockDialUnselectedContentColor = if (isDarkTheme()) TextSecondaryDark else TextSecondary,
                periodSelectorBorderColor = if (isDarkTheme()) BackgroundDark else Background,
                periodSelectorSelectedContainerColor = if (isDarkTheme()) PrimaryDark else Primary,
                periodSelectorSelectedContentColor = if (isDarkTheme()) TextDisabledDark else TextDisabled,
                periodSelectorUnselectedContainerColor = Color.Transparent,
                periodSelectorUnselectedContentColor = if (isDarkTheme()) TextSecondaryDark else TextSecondary,
                timeSelectorSelectedContainerColor = if (isDarkTheme()) BackgroundDark else Background,
                timeSelectorSelectedContentColor = if (state.alarm.time.isInitial) if (isDarkTheme()) TextSecondaryDark else TextSecondary else if (isDarkTheme()) PrimaryDark else Primary,
                timeSelectorUnselectedContainerColor = if (isDarkTheme()) BackgroundDark else Background,
                timeSelectorUnselectedContentColor = if (state.alarm.time.isInitial) if (isDarkTheme()) TextSecondaryDark else TextSecondary else if (isDarkTheme()) PrimaryDark else Primary
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