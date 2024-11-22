package com.rvcoding.snoozeloo.domain

import com.rvcoding.snoozeloo.domain.model.Alarm

interface AlarmScheduler {
    fun schedule(alarm: Alarm)
    fun cancel(alarm: Alarm)

    companion object {
        const val ALARM_ID_EXTRA_KEY = "ALARM_ID"
    }
}