package com.codemave.mobilecomputing.data.repository

import com.codemave.mobilecomputing.data.entity.Reminder
import com.codemave.mobilecomputing.data.room.ReminderDao
import kotlinx.coroutines.flow.Flow


class ReminderRepository(
    private val reminderDao: ReminderDao
) {
    fun reminders(): Flow<List<Reminder>> = reminderDao.reminders()

    fun reminder(reminderId: Long): Reminder? = reminderDao.reminder(reminderId)

    suspend fun addReminder(reminder: Reminder) = reminderDao.insert(reminder)

    suspend fun updateReminder(entity: Reminder) = reminderDao.update(entity)

    suspend fun deleteReminder(entity: Reminder) = reminderDao.delete(entity)

    suspend fun deleteReminderWithId(reminderId : Long) = reminderDao.deleteReminderWithId(reminderId)

    fun updateReminderSeen(reminderId: Long, seen: Boolean) = reminderDao.updateReminderSeen(reminderId, seen)
}