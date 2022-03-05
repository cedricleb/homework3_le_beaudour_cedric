package com.codemave.mobilecomputing

import android.content.Context
import androidx.room.Room
import com.codemave.mobilecomputing.data.repository.ReminderRepository
import com.codemave.mobilecomputing.data.room.MobileComputingDatabase

/**
 * A simple singleton dependency graph
 *
 * For a real app, please use something like Koin/Dagger/Hilt instead
 */
object Graph {
    lateinit var database: MobileComputingDatabase

    lateinit var appContext: Context

    val reminderRepository by lazy {
        ReminderRepository(
            reminderDao = database.reminderDao()
        )
    }

    fun provide(context: Context) {
        appContext = context
        database = Room.databaseBuilder(context, MobileComputingDatabase::class.java, "mcData.db")
            .fallbackToDestructiveMigration() // don't use this in production app
            .allowMainThreadQueries()
            .build()
    }
}