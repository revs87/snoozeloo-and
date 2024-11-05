package com.rvcoding.snoozeloo.ui.screen.list.model

import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.ui.component.UiText
import com.rvcoding.snoozeloo.ui.component.UiText.DynamicString
import com.rvcoding.snoozeloo.ui.component.UiText.StringResource

data class AlarmInfo(
    val enabled: Boolean,
    val name: UiText,
    val timeFormat: TimeFormat,
    val timeLeft: UiText
) {
    companion object {
        val Stub = AlarmInfo(
            enabled = false,
            name = DynamicString("Wake up"),
            timeFormat = TimeFormat.Time12("10:00", "AM"),
            timeLeft = StringResource(R.string.alarm_time_left, arrayOf("30min"))
        )
    }
}

sealed interface TimeFormat {
    data class Time24(val time: String) : TimeFormat
    data class Time12(val time: String, val amOrPm: String) : TimeFormat
}

sealed interface TimeFormatPreference {
    data object Time24 : TimeFormatPreference
    data object Time12 : TimeFormatPreference

    companion object {
        /**
         * Naive way to declare the default TimeFormatPreference.
         * */
        private fun default() = Time12 as TimeFormatPreference
        fun is24HourFormat(): Boolean = default() is Time24
    }
}