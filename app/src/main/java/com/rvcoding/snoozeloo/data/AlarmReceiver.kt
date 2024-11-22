package com.rvcoding.snoozeloo.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.ALARM_ID_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject


class AlarmReceiver : BroadcastReceiver() {
    private val alarmRepository: AlarmRepository by inject(AlarmRepository::class.java)
    private val coScope: CoroutineScope by inject(CoroutineScope::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getIntExtra(ALARM_ID_EXTRA_KEY, -1) ?: return
        if (alarmId == -1) return

        coScope.launch {
            println("Alarm triggered id=$alarmId")
            alarmRepository.updateAlarmEnabled(alarmId, false)
            alarmRepository.getAlarm(alarmId)?.let { alarm ->
                println("Alarm: $alarm")
            }
        }
    }
}