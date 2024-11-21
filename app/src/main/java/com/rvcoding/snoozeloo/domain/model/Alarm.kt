package com.rvcoding.snoozeloo.domain.model

import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.ui.component.UiText.DynamicString
import com.rvcoding.snoozeloo.ui.component.UiText.StringResource
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo.Companion.HOURS
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo.Companion.HOURS_DURATION
import com.rvcoding.snoozeloo.ui.screen.list.model.AlarmInfo.Companion.showSleepRecommendation
import com.rvcoding.snoozeloo.ui.screen.list.model.TimeFormat
import com.rvcoding.snoozeloo.ui.util.meridianAsString
import com.rvcoding.snoozeloo.ui.util.nextAlarmTime
import com.rvcoding.snoozeloo.ui.util.nextLocalMidnightInUtc
import com.rvcoding.snoozeloo.ui.util.timeLeftAsString
import com.rvcoding.snoozeloo.ui.util.timeWithMeridiemAsString
import com.rvcoding.snoozeloo.ui.util.toLocalHoursAndMinutes
import kotlinx.serialization.Serializable


@Serializable
data class Alarm(
    val id: Int = -1,
    val enabled: Boolean,
    val name: String,
    val time: Time,
) {
    companion object {
        val NewAlarm = Alarm(
            enabled = false,
            name = "Work",
            time = Time.base()
        )
    }
}

@Serializable
data class Time(
    val utcTime: Long,
    val isInitial: Boolean = false,
    val localHours: String = utcTime.toLocalHoursAndMinutes(TimeFormatPreference.is24HourFormat()).first,
    val localMinutes: String = utcTime.toLocalHoursAndMinutes(TimeFormatPreference.is24HourFormat()).second,
    val localMeridiem: String = meridianAsString(utcTime)
) {
    companion object {
        fun base() = Time(utcTime = nextLocalMidnightInUtc(), isInitial = true)
        fun from(utcTime: Long) = Time(utcTime)
    }
}

fun Alarm.toAlarmInfo(): AlarmInfo = AlarmInfo(
    id = this.id,
    enabled = this.enabled,
    name = DynamicString(this.name),
    timeFormat = when (TimeFormatPreference.is24HourFormat()){
        true -> TimeFormat.Time24(hours = this.time.localHours, minutes = this.time.localMinutes)
        false -> TimeFormat.Time12(hours = this.time.localHours, minutes = this.time.localMinutes, meridiem = this.time.localMeridiem)
    },
//    timeLeft = StringResource(R.string.alarm_time_left, arrayOf(timeLeftAsString(this.time.utcTime.nextAlarmTime())))
    timeLeft = if (showSleepRecommendation(this.time.utcTime)) {
        StringResource(
            R.string.alarm_recommendation, arrayOf(
            timeWithMeridiemAsString(
                utcTime = this.time.utcTime - HOURS_DURATION,
                is24HourFormat = TimeFormatPreference.is24HourFormat()
            ),
            HOURS
        ))
    } else {
        StringResource(R.string.alarm_time_left, arrayOf(timeLeftAsString(this.time.utcTime.nextAlarmTime())))
    }
)