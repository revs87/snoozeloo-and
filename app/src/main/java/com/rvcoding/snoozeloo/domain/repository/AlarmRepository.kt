package com.rvcoding.snoozeloo.domain.repository

import com.rvcoding.snoozeloo.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun getAlarm(id: Int): Alarm?
    suspend fun addAlarm(alarm: Alarm)
    suspend fun updateAlarm(alarmWithId: Alarm)
    suspend fun updateAlarmEnabled(id: Int, enabled: Boolean)
    suspend fun deleteAlarm(id: Int)
}