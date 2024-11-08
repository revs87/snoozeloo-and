package com.rvcoding.snoozeloo.ui.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.navigation.Actions
import com.rvcoding.snoozeloo.ui.component.AddAlarmButton
import com.rvcoding.snoozeloo.ui.component.AlarmCard
import com.rvcoding.snoozeloo.ui.component.TopBar
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.TextPrimary
import com.rvcoding.snoozeloo.ui.theme.TextPrimaryDark
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun YourAlarmsScreenRoot(
    vm: YourAlarmsViewModel = koinViewModel(),
) {
    val state by vm.alarms.collectAsStateWithLifecycle()

    YourAlarmsScreen(
        state = state,
        onAction = vm::onAction
    )
}

@Composable
private fun YourAlarmsScreen(
    state: YourAlarmsState,
    onAction: (Actions) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (state.alarms.isEmpty()) {
            YourAlarmsEmptyScreen()
        } else {
            YourAlarmsNonEmptyScreen(state.alarms, onAction)
        }

        AddAlarmButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            onAction = { onAction.invoke(Actions.OnAddAlarmButtonClicked(alarm = Alarm.NewAlarm)) }
        )
    }
}

@Composable
private fun YourAlarmsNonEmptyScreen(
    alarms: List<AlarmInfo>,
    onAction: (Actions) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopBar(item = TopBar.Title.alarmList())
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            itemsIndexed(
                items = alarms,
                key = { index, _ -> index }
            ) { _, item ->
                AlarmCard(
                    alarmInfo = item,
                    onCheckedChange = { checked -> onAction.invoke(Actions.OnAlarmCheckedChange(item.id, checked)) },
                    onCardClicked = { onAction.invoke(Actions.OnAlarmClicked(item.id)) }
                )
            }
            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun YourAlarmsEmptyScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(item = TopBar.Title.alarmList())
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.padding(bottom = 16.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.alarm_blue),
                contentDescription = "Alarm blue",
                tint = if (isDarkTheme()) PrimaryDark else Primary
            )
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(R.string.alarm_list_empty_message),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = if (isDarkTheme()) TextPrimaryDark else TextPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenNonEmptyPreview() {
    YourAlarmsScreen(state = YourAlarmsState.NonEmpty, onAction = {})
}

@Preview(showBackground = true)
@Composable
fun AlarmDetailsScreenEmptyPreview() {
    YourAlarmsScreen(state = YourAlarmsState.Empty, onAction = {})
}