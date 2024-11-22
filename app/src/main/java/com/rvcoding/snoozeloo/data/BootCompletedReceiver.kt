package com.rvcoding.snoozeloo.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class BootCompletedReceiver : BroadcastReceiver() {
    private val alarmRepository: AlarmRepository by inject(AlarmRepository::class.java)
    private val alarmScheduler: AlarmScheduler by inject(AlarmScheduler::class.java)
    private val coScope: CoroutineScope by inject(CoroutineScope::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            println("Boot completed: Refreshing alarms")

            coScope.launch {
                alarmRepository.getAlarms().collect { list ->
                    list.forEach { alarm ->
                        if (alarm.enabled && alarm.time.utcTime > System.currentTimeMillis()) {
                            alarmScheduler.schedule(alarm)
                        }
                    }
                }
            }
        }
    }
}