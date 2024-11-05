package com.rvcoding.snoozeloo.domain.model

import com.rvcoding.snoozeloo.ui.util.nextLocalMidnightInUTC

data class Alarm(
    val id: Int = 0,
    val enabled: Boolean,
    val name: String,
    val time: Long
) {
    companion object {
        val NewAlarm = Alarm(
            enabled = false,
            name = "Work",
            time = nextLocalMidnightInUTC()
        )
    }
}
