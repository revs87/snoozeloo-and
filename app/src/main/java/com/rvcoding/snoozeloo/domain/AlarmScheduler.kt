package com.rvcoding.snoozeloo.domain

import android.content.Context
import android.widget.Toast
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.ui.util.timeWithMeridiemAndDateAsString

interface AlarmScheduler {
    fun schedule(
        alarm: Alarm,
        onSchedule: (Context) -> Unit = {
            Toast.makeText(it, "Alarm scheduled for ${timeWithMeridiemAndDateAsString(alarm.time.utcTime)}", Toast.LENGTH_SHORT).show()
        }
    )
    fun cancel(alarm: Alarm)

    companion object {
        const val ALARM_ID_EXTRA_KEY = "ALARM_ID"
        const val IS_ALARM_TRIGGERED_EXTRA_KEY = "is_alarm_key"
    }
}