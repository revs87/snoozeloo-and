package com.rvcoding.snoozeloo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo
import com.rvcoding.snoozeloo.ui.screen.list.model.TimeFormat
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.PrimaryDisabled
import com.rvcoding.snoozeloo.ui.theme.PrimaryDisabledDark
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme

@Composable
fun AlarmCard(
    alarmInfo: AlarmInfo = AlarmInfo.Stub,
    onCheckedChange: (Boolean) -> Unit = {},
    onCardClicked: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCardClicked.invoke() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = alarmInfo.name.asString(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    when (alarmInfo.timeFormat) {
                        is TimeFormat.Time12 -> {
                            Text(
                                text = alarmInfo.timeFormat.hours + ":" + alarmInfo.timeFormat.minutes,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Medium,
                                fontSize = 42.sp
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = alarmInfo.timeFormat.meridiem,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Medium,
                                fontSize = 24.sp
                            )
                        }
                        is TimeFormat.Time24 -> {
                            Text(
                                text = alarmInfo.timeFormat.hours + ":" + alarmInfo.timeFormat.minutes,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Medium,
                                fontSize = 42.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = alarmInfo.timeLeft.asString(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                var checked by remember { mutableStateOf(alarmInfo.enabled) }
                Switch(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    checked = checked,
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = if (isDarkTheme()) PrimaryDark else Primary,
                        checkedThumbColor = MaterialTheme.colorScheme.surface,
                        checkedTrackColor = if (isDarkTheme()) PrimaryDark else Primary,
                        uncheckedBorderColor = if (isDarkTheme()) PrimaryDisabledDark else PrimaryDisabled,
                        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                        uncheckedTrackColor = if (isDarkTheme()) PrimaryDisabledDark else PrimaryDisabled
                    ),
                    onCheckedChange = { checked = it; onCheckedChange.invoke(it) },
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Rounded.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Transparent
                        )
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AlarmCardPreview() {
    AlarmCard(AlarmInfo.Stub)
}