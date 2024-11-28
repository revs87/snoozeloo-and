package com.rvcoding.snoozeloo.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.rvcoding.snoozeloo.DEEP_LINK_DOMAIN
import com.rvcoding.snoozeloo.MainActivity
import com.rvcoding.snoozeloo.R
import com.rvcoding.snoozeloo.domain.AlarmScheduler
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.ALARM_ID_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.IS_ALARM_TRIGGERED_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.AlarmScheduler.Companion.IS_ALARM_TURN_OFF_EXTRA_KEY
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import com.rvcoding.snoozeloo.ui.util.timeWithMeridiemAsString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject


class AlarmReceiver : BroadcastReceiver() {
    private val alarmRepository: AlarmRepository by inject(AlarmRepository::class.java)
    private val alarmScheduler: AlarmScheduler by inject(AlarmScheduler::class.java)
    private val coScope: CoroutineScope by inject(CoroutineScope::class.java)

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmId = intent?.getIntExtra(ALARM_ID_EXTRA_KEY, -1) ?: return
        if (alarmId == -1) return

        coScope.launch {
            alarmRepository.updateAlarmEnabled(alarmId, false)
            alarmRepository.getAlarm(alarmId)?.let { alarm ->
                println("[AlarmReceiver] Alarm: $alarm")
                context?.let {
                    val alarmTriggerIntent = Intent(context, MainActivity::class.java).apply {
                        data = android.net.Uri.parse("https://$DEEP_LINK_DOMAIN/$alarmId")
                        flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(IS_ALARM_TRIGGERED_EXTRA_KEY, true)
                    }
                    val alarmTurnOffIntent = Intent(context, MainActivity::class.java).apply {
                        data = android.net.Uri.parse("https://$DEEP_LINK_DOMAIN/$alarmId")
                        flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(IS_ALARM_TURN_OFF_EXTRA_KEY, true)
                        putExtra(ALARM_ID_EXTRA_KEY, alarmId)
                    }

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        context.startActivity(alarmTriggerIntent)
                    } else {
                        showNotification(
                            context,
                            alarmId = alarmId,
                            alarmTime = alarm.time.utcTime,
                            alarmTriggerIntent = alarmTriggerIntent,
                            alarmTurnOffIntent = alarmTurnOffIntent
                        )
                    }

                    alarmScheduler.playRingtone()
                }
            }
        }
    }

    private fun showNotification(
        context: Context,
        title: String = "Alarm Triggered",
        alarmId: Int = -1,
        alarmTime: Long = 0L,
        alarmTriggerIntent: Intent,
        alarmTurnOffIntent: Intent
    ) {
        val alarmTriggerPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(alarmTriggerIntent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }
        val alarmTurnOffPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(alarmTurnOffIntent)
            getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
        }

        context.startActivity(alarmTriggerIntent)

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setBypassDnd(true)
            enableLights(true)
            enableVibration(true)
            setShowBadge(true)
            lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }

        val notificationManager = context.getSystemService<NotificationManager>()!!
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.alarm_blue)
            .setContentTitle(title)
            .setContentText(timeWithMeridiemAsString(alarmTime))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setFullScreenIntent(alarmTriggerPendingIntent, true)
            .addAction(R.drawable.alarm_blue, context.getString(R.string.turn_off), alarmTurnOffPendingIntent)
            .build()
        notificationManager.notify(alarmId, notification)
    }

    companion object {
        const val CHANNEL_ID = "alarm_channel"
        const val CHANNEL_NAME = "Alarms"
    }
}