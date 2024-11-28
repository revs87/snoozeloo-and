@file:OptIn(ExperimentalMaterial3Api::class)

package com.rvcoding.snoozeloo.ui.screen.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.NameCard
import com.rvcoding.snoozeloo.ui.component.TimeCard
import com.rvcoding.snoozeloo.ui.component.TopBar
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import org.koin.androidx.compose.koinViewModel


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
            TimeCard(state, onAction)
            NameCard(state, onAction)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenNonEmptyPreview() {
    AlarmSettingsScreen(state = AlarmSettingsState.Initial, onAction = {})
}
