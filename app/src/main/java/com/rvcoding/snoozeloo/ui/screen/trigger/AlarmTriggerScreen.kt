package com.rvcoding.snoozeloo.ui.screen.trigger

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.domain.model.TimeFormatPreference
import com.rvcoding.snoozeloo.domain.navigation.Actions
import com.rvcoding.snoozeloo.ui.theme.Background
import com.rvcoding.snoozeloo.ui.theme.BackgroundDark
import com.rvcoding.snoozeloo.ui.theme.GreyDisabled
import com.rvcoding.snoozeloo.ui.theme.GreyDisabledDark
import com.rvcoding.snoozeloo.ui.theme.Primary
import com.rvcoding.snoozeloo.ui.theme.PrimaryDark
import com.rvcoding.snoozeloo.ui.theme.TextPrimary
import com.rvcoding.snoozeloo.ui.theme.isDarkTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun AlarmTriggerScreenRoot(
    vm: AlarmTriggerViewModel = koinViewModel(),
    alarmId: Int = -1
) {
    val state by vm.state.collectAsStateWithLifecycle()
    LaunchedEffect(alarmId) {
        vm.onAction(Actions.AlarmTrigger.Load(alarmId))
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        state?.let {
            AlarmTriggerScreen(
                state = it,
                onAction = vm::onAction,
            )
        } ?: CircularProgressIndicator()
    }
}

@Composable
fun AlarmTriggerScreen(
    state: AlarmTriggerState,
    onAction: (Actions.AlarmTrigger) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        RingingAlarmIconAnimation()
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${state.alarm.time.localHours}:${state.alarm.time.localMinutes}",
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.displayLarge.copy(
                    color = Primary,
                    fontSize = 80.sp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both
                    )
                ),
            )
            if (!TimeFormatPreference.is24HourFormat()) {
                Text(
                    text = state.alarm.time.localMeridiem,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 20.sp,
                        baselineShift = BaselineShift.Subscript,
                        color = Primary,
                    )
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = state.alarm.name.uppercase(),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 20.sp,
                color = TextPrimary,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            )
        )
        Spacer(modifier = Modifier.padding(16.dp))
        TextButton(
            modifier = Modifier.fillMaxWidth(0.7f),
            enabled = true,
            contentPadding = PaddingValues(),
            colors = ButtonColors(
                containerColor = if (isDarkTheme()) PrimaryDark else Primary,
                contentColor = if (isDarkTheme()) PrimaryDark else Primary,
                disabledContainerColor = if (isDarkTheme()) GreyDisabledDark else GreyDisabled,
                disabledContentColor = if (isDarkTheme()) GreyDisabledDark else GreyDisabled
            ),
            onClick = { onAction.invoke(Actions.AlarmTrigger.TurnOff(
                alarmId = state.alarm.id
            )) }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.turn_off),
                color = if (isDarkTheme()) BackgroundDark else Background,
                fontSize = 20.sp,
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
    }
}

@Composable
fun RingingAlarmIconAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val angle by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = keyframes {
                durationMillis = 1700 // 6*200ms + 500ms
                -10f at 0 using LinearEasing // Start position
                10f at 200 using FastOutSlowInEasing // 1st wobble
                -10f at 400 using FastOutSlowInEasing // 2nd wobble
                10f at 600 using FastOutSlowInEasing // 3rd wobble
                -10f at 800 using FastOutSlowInEasing // 4th wobble
                10f at 1000 using FastOutSlowInEasing // 5th wobble
                10f at 1200 using LinearEasing // Hold for 1 second
            }
        ), label = "angle"
    )

    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.alarm_blue),
        tint = Primary,
        contentDescription = "Ringing Bell",
        modifier = Modifier
            .size(60.dp)
            .graphicsLayer {
                rotationZ = angle
            }
    )
}

@Preview(showBackground = true)
@Composable
fun AlarmTriggerScreenPreview() {
    AlarmTriggerScreen(
        state = AlarmTriggerState(Alarm.NewAlarm),
        onAction = {}
    )
}