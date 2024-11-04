package com.rvcoding.snoozeloo.model

data class AlarmInfo(
    val enabled: Boolean,
    val name: String,
    val timeDisplay: TimeDisplay,
    val durationLeft: String
) {
    companion object {
        val Stub = AlarmInfo(
            enabled = false,
            name = "Wake up",
            timeDisplay = TimeDisplay.Time12("10:00", "AM"),
            durationLeft = "Alarm in 30min"
        )
    }
}

sealed interface TimeDisplay {
    data class Time24(val time: String) : TimeDisplay
    data class Time12(val time: String, val amOrPm: String) : TimeDisplay
}