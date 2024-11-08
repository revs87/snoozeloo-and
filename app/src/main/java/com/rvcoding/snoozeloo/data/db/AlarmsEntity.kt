package com.rvcoding.snoozeloo.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
class AlarmsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val enabled: Boolean,
    val name: String,
    val utcTime: Long
)