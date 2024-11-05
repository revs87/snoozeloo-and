package com.rvcoding.snoozeloo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AlarmsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AlarmsDatabase : RoomDatabase() {
    abstract val dao: AlarmsDao

    companion object {
        private const val DATABASE_NAME = "alarms_db"

        fun createDb(context: Context): AlarmsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = AlarmsDatabase::class.java,
                name = DATABASE_NAME
            ).build()
        }

        fun createDbForTesting(context: Context): AlarmsDatabase {
            return Room.inMemoryDatabaseBuilder(
                context = context,
                klass = AlarmsDatabase::class.java,
            ).build()
        }
    }
}