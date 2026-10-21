package com.omniplan.app

import android.app.Application
import com.omniplan.app.core.notification.NotificationHelper
import com.omniplan.app.data.local.OmniPlanDatabase
import com.omniplan.app.data.repository.CalendarRepositoryImpl
import com.omniplan.app.data.repository.HabitRepositoryImpl
import com.omniplan.app.data.repository.TaskRepositoryImpl
import com.omniplan.app.domain.repository.CalendarRepository
import com.omniplan.app.domain.repository.HabitRepository
import com.omniplan.app.domain.repository.TaskRepository

class OmniPlanApp : Application() {

    lateinit var taskRepository: TaskRepository
        private set

    lateinit var habitRepository: HabitRepository
        private set

    lateinit var calendarRepository: CalendarRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        // 1. Initialize notification channels
        NotificationHelper.createNotificationChannels(this)

        // 2. Initialize Room Database & Repositories
        val database = OmniPlanDatabase.getInstance(this)
        taskRepository = TaskRepositoryImpl(database.taskDao(), database.categoryDao())
        habitRepository = HabitRepositoryImpl(database.habitDao())
        calendarRepository = CalendarRepositoryImpl(database.timeBlockDao(), database.pomodoroDao())
    }

    companion object {
        lateinit var instance: OmniPlanApp
            private set
    }
}
