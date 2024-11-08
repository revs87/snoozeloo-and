package com.rvcoding.snoozeloo.ui.screen.list.model

import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.ui.component.UiText
import com.rvcoding.snoozeloo.ui.component.UiText.DynamicString
import com.rvcoding.snoozeloo.ui.component.UiText.StringResource
import com.rvcoding.snoozeloo.ui.util.hourInBounds

data class AlarmInfo(
    val id: Int = 0,
    val enabled: Boolean,
    val name: UiText,
    val timeFormat: TimeFormat,
    val timeLeft: UiText
) {
    companion object {
        const val HOURS = 8
        const val HOURS_DURATION = HOURS * 60 * 60 * 1000 // milliseconds

        val Stub = AlarmInfo(
            enabled = false,
            name = DynamicString("Wake up"),
            timeFormat = TimeFormat.Time12("10", "00", "AM"),
            timeLeft = StringResource(R.string.alarm_time_left, arrayOf("30min"))
        )

        fun showSleepRecommendation(utcTime: Long) =
            System.currentTimeMillis() + HOURS_DURATION < utcTime || utcTime.hourInBounds(of = 4, and = 10)
    }
}

sealed interface TimeFormat {
    data class Time24(val hours: String, val minutes: String) : TimeFormat
    data class Time12(val hours: String, val minutes: String, val meridiem: String) : TimeFormat
}
