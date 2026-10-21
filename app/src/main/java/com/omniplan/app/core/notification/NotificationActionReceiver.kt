package com.omniplan.app.core.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.omniplan.app.data.local.OmniPlanDatabase
import com.omniplan.app.widget.TodayTasksWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(NotificationHelper.EXTRA_TASK_ID, -1L)
        if (taskId == -1L) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(taskId.toInt())

        val database = OmniPlanDatabase.getInstance(context)

        when (intent.action) {
            NotificationHelper.ACTION_COMPLETE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    database.taskDao().setTaskCompleted(taskId, true, now)
                    // Request widget update
                    TodayTasksWidgetReceiver.updateAllWidgets(context)
                }
            }

            NotificationHelper.ACTION_SNOOZE -> {
                CoroutineScope(Dispatchers.IO).launch {
                    val taskEntity = database.taskDao().getTaskByIdSync(taskId)
                    if (taskEntity != null) {
                        val snoozedTask = taskEntity.toDomain().copy(
                            reminderDateTime = LocalDateTime.now().plusMinutes(15)
                        )
                        AlarmScheduler.scheduleTaskAlarm(context, snoozedTask)
                    }
                }
            }
        }
    }
}
