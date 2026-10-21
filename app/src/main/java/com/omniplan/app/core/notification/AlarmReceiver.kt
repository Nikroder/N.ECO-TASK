package com.omniplan.app.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(AlarmScheduler.EXTRA_TASK_ID, -1L)
        val title = intent.getStringExtra(AlarmScheduler.EXTRA_TASK_TITLE) ?: "Напоминание о задаче"
        val desc = intent.getStringExtra(AlarmScheduler.EXTRA_TASK_DESC) ?: ""

        if (taskId != -1L) {
            NotificationHelper.showTaskReminderNotification(context, taskId, title, desc)
        }
    }
}
