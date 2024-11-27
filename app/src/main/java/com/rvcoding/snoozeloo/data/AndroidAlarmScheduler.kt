package com.rvcoding.snoozeloo.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.core.content.getSystemService
import com.rvcoding.snoozeloo.common.DispatchersProvider
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.ALARM_ID_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.ui.util.truncateToMinute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AndroidAlarmScheduler(
    private val context: Context,
    private val coScope: CoroutineScope,
    private val dispatchersProvider: DispatchersProvider
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    override fun schedule(
        alarm: Alarm,
        onSchedule: (Context) -> Unit
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALARM_ID_EXTRA_KEY, alarm.id)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.time.utcTime.truncateToMinute(),
            PendingIntent.getBroadcast(
                context,
                alarm.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        coScope.launch(dispatchersProvider.main) {
            onSchedule.invoke(context)
        }
    }

    override fun cancel(alarmId: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmId,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun removeNotification(alarmId: Int) {
        val notificationManager = context.getSystemService<NotificationManager>()!!
        notificationManager.cancel(alarmId)
    }

    private val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    private var ringtone: Ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
    override fun playRingtone() = ringtone.play()
    override fun stopRingtone() = ringtone.stop()
}