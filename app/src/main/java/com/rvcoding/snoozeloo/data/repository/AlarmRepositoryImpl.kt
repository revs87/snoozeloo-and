package com.rvcoding.snoozeloo.data.repository

import com.rvcoding.snoozeloo.data.db.AlarmsDao
import com.rvcoding.snoozeloo.data.db.AlarmsEntity
import com.rvcoding.snoozeloo.domain.model.Alarm
import com.rvcoding.snoozeloo.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AlarmRepositoryImpl(private val dao: AlarmsDao) : AlarmRepository {
    override fun getAlarms(): Flow<List<Alarm>> = dao.getAlarms().map { list -> list.map { it.toDomain() } }
    override suspend fun getAlarm(id: Int): Alarm? = dao.getAlarmById(id)?.toDomain()
    override suspend fun addAlarm(alarm: Alarm) { dao.insertAlarm(alarm.toEntity()) }
    override suspend fun updateAlarmEnabled(id: Int, enabled: Boolean) {
        getAlarm(id)?.let { alarm ->
            dao.updateAlarm(alarm.copy(enabled = enabled).toEntity())
        }
    }
    override suspend fun deleteAlarm(id: Int) { dao.deleteAlarm(id) }
}

private fun AlarmsEntity.toDomain(): Alarm = Alarm(
    id = this.id,
    enabled = this.enabled,
    name = this.name,
    time = this.time
)

private fun Alarm.toEntity(): AlarmsEntity = AlarmsEntity(
    id = this.id,
    enabled = this.enabled,
    name = this.name,
    time = this.time
)
