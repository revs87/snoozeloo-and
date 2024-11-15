package com.rvcoding.snoozeloo.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcoding.snoozeloo.domain.model.toAlarmInfo
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.TopBar
import com.rvcoding.snoozeloo.ui.screen.list.model.TimeFormat
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import kotlinx.coroutines.flow.update
import org.koin.androidx.compose.koinViewModel


@Composable
fun AlarmSettingsScreenRoot(
    vm: AlarmSettingsViewModel = koinViewModel(),
    alarmId: Int = -1
) {
    val state by vm.state.collectAsStateWithLifecycle()
    vm.currentAlarmId.update { alarmId }

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
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            item = TopBar.Savable(
                isDarkTheme = isDarkTheme(),
                rightButtonEnabled = true,
                onRightButtonClicked = { onAction.invoke(Actions.AlarmSettings.Save) },
                onLeftButtonClicked = { onAction.invoke(Actions.AlarmSettings.Close) }
            )
        )
        Row(modifier = Modifier.fillMaxWidth().height(160.dp)) {
            TextField(
                modifier = Modifier.weight(1f),
                value = when (val timeFormat = state.alarm.toAlarmInfo().timeFormat) {
                    is TimeFormat.Time12 -> timeFormat.hours
                    is TimeFormat.Time24 -> timeFormat.hours
                },
                onValueChange = { },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { }
                )
            )
            TextField(
                modifier = Modifier.weight(1f),
                value = when (val timeFormat = state.alarm.toAlarmInfo().timeFormat) {
                    is TimeFormat.Time12 -> timeFormat.minutes
                    is TimeFormat.Time24 -> timeFormat.minutes
                },
                onValueChange = { },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { }
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenNonEmptyPreview() {
    AlarmSettingsScreen(state = AlarmSettingsState.Stub, onAction = {})
}
