package com.codemave.mobilecomputing.data.room

import androidx.room.*
import com.codemave.mobilecomputing.data.entity.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ReminderDao {

    @Query("SELECT * FROM reminders LIMIT 15")
    abstract fun reminders(): Flow<List<Reminder>>

    @Query("""SELECT * FROM reminders WHERE id = :reminderId""")
    abstract fun reminder(reminderId: Long): Reminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Reminder): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: Reminder)

    @Delete
    abstract suspend fun delete(entity: Reminder): Int

    @Query("DELETE FROM reminders WHERE id = :reminderId")
    abstract suspend fun deleteReminderWithId(reminderId: Long)

    @Query("UPDATE reminders SET reminder_seen = :seen WHERE id = :reminderId")
    abstract fun updateReminderSeen(reminderId: Long, seen: Boolean)
}