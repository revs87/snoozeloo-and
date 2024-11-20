package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.domain.model.Time
import com.rvcoding.snoozeloo.domain.model.toAlarmInfo
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsState
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.TextDisabled
import com.rvcoding.snoozeloo.ui.theme.TextPrimary
import java.util.Calendar
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    state: AlarmSettingsState,
    timePickerState: TimePickerState,
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
    onAction: (Actions.AlarmSettings) -> Unit,
) {
    val initial = remember {
        Actions.AlarmSettings.OnTimeChange(
            state.alarm,
            timePickerState.hour,
            timePickerState.minute,
            timePickerState.isAfternoon
        )
    }

    var wasAfternoon by remember { mutableStateOf(state.alarm.time.localMeridiem == "PM") }
    LaunchedEffect(timePickerState.isAfternoon) {
        if (timePickerState.isAfternoon != wasAfternoon) {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            cal.set(Calendar.MINUTE, timePickerState.minute)
            cal.set(Calendar.AM_PM, if (timePickerState.isAfternoon) Calendar.PM else Calendar.AM)

            onAction(
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
            wasAfternoon = timePickerState.isAfternoon
        }
    }

    TimePickerDialog(
        onDismiss = {
            timePickerState.hour = initial.hour
            timePickerState.minute = initial.minute
            timePickerState.isAfternoon = initial.isAfternoon
            onAction(initial)
            onDismiss()
        },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors().copy(
                    clockDialColor = MaterialTheme.colorScheme.background,
                    selectorColor = Primary,
                    containerColor = MaterialTheme.colorScheme.background,
                    clockDialSelectedContentColor = MaterialTheme.colorScheme.surface,
                    clockDialUnselectedContentColor = TextPrimary,
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

@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = "Dismiss",
                    color = Primary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    style = LocalTextStyle.current.merge(
                        TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both
                            )
                        )
                    ))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = "OK",
                    color = Primary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    style = LocalTextStyle.current.merge(
                        TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.Both
                            )
                        )
                    )
                )
            }
        },
        text = { content() }
    )
}
