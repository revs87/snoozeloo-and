package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.screen.settings.AlarmSettingsState
import com.rvcoding.snoozeloo.ui.theme.BackgroundCard
import com.rvcoding.snoozeloo.ui.theme.BackgroundCardDark
import com.rvcoding.snoozeloo.ui.theme.TextSecondary
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme

@Composable
fun NameCard(
    state: AlarmSettingsState,
    onAction: (Actions.AlarmSettings) -> Unit
) {
    var name by remember { mutableStateOf(state.alarm.name) }

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
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = stringResource(R.string.alarm_name),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            TextField(
                value = name,
                onValueChange = {
                    name = it
                    onAction.invoke(Actions.AlarmSettings.OnNameChange(it))
                },
                maxLines = 1,
                textStyle = LocalTextStyle.current.copy(
                    color = TextSecondary,
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.colors().copy(
                    focusedTextColor = TextSecondary,
                    unfocusedTextColor = TextSecondary,
                    focusedContainerColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard,
                    unfocusedContainerColor = if (isDarkTheme()) BackgroundCardDark else BackgroundCard
                )
            )
        }
    }
}