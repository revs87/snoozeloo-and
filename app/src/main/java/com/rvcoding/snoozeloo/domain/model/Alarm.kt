package com.rvcoding.snoozeloo.domain.model

import com.rvcoding.snoozeloo.ui.util.meridianAsString
import com.rvcoding.snoozeloo.ui.util.nextLocalMidnightInUtc
import com.rvcoding.snoozeloo.ui.util.toLocalHoursAnMinutes

data class Alarm(
    val id: Int = 0,
    val enabled: Boolean,
    val name: String,
    val time: Time = Time.base(),
) {
    companion object {
        val NewAlarm = Alarm(
            enabled = false,
            name = "Work"
        )
    }
}

data class Time(
    val utcTime: Long,
    val localHours: String = utcTime.toLocalHoursAnMinutes(TimeFormatPreference.is24HourFormat()).first,
    val localMinutes: String = utcTime.toLocalHoursAnMinutes(TimeFormatPreference.is24HourFormat()).second,
    val localMeridiem: String = meridianAsString(utcTime)
) {
    companion object {
        fun base() = Time(utcTime = nextLocalMidnightInUtc())
        fun from(utcTime: Long) = Time(utcTime)
    }
}