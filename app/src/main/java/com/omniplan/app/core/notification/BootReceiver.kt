package com.omniplan.app.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.omniplan.app.data.local.OmniPlanDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            val database = OmniPlanDatabase.getInstance(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val activeTasks = database.taskDao().getTasksByCompletion(false).first()
                    val now = LocalDateTime.now()
                    activeTasks.forEach { entity ->
                        val task = entity.toDomain()
                        val reminderTime = task.reminderDateTime ?: task.dueDateTime
                        if (reminderTime != null && reminderTime.isAfter(now)) {
                            AlarmScheduler.scheduleTaskAlarm(context, task)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
