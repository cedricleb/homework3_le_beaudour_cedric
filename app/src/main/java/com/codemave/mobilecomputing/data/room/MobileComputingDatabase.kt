package com.codemave.mobilecomputing.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codemave.mobilecomputing.data.entity.Reminder

/**
 * The [RoomDatabase] for this app
 */
@Database(
    entities = [Reminder::class],
    version = 10,
    exportSchema = false
)
abstract class MobileComputingDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
