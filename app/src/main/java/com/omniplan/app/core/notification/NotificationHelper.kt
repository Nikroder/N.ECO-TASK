package com.omniplan.app.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.omniplan.app.R
import com.omniplan.app.ui.MainActivity

object NotificationHelper {

    const val CHANNEL_TASKS_ID = "channel_tasks"
    const val CHANNEL_HABITS_ID = "channel_habits"
    const val CHANNEL_FOCUS_ID = "channel_focus"

    const val ACTION_COMPLETE = "com.omniplan.app.ACTION_COMPLETE"
    const val ACTION_SNOOZE = "com.omniplan.app.ACTION_SNOOZE"
    const val EXTRA_TASK_ID = "extra_task_id"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val tasksChannel = NotificationChannel(
                CHANNEL_TASKS_ID,
                "Напоминания о задачах",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о сроках выполнения задач"
                enableVibration(true)
            }

            val habitsChannel = NotificationChannel(
                CHANNEL_HABITS_ID,
                "Привычки и ритуалы",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Напоминания о ежедневных привычках"
            }

            val focusChannel = NotificationChannel(
                CHANNEL_FOCUS_ID,
                "Режим Фокуса (Pomodoro)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Оповещения об окончании интервалов работы и отдыха"
            }

            notificationManager.createNotificationChannels(listOf(tasksChannel, habitsChannel, focusChannel))
        }
    }

    fun showTaskReminderNotification(context: Context, taskId: Long, title: String, description: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Content intent: Open app
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action: Mark Complete
        val completeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_COMPLETE
            putExtra(EXTRA_TASK_ID, taskId)
        }
        val completePendingIntent = PendingIntent.getBroadcast(
            context,
            (taskId * 10 + 1).toInt(),
            completeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action: Snooze 15 min
        val snoozeIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_TASK_ID, taskId)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            (taskId * 10 + 2).toInt(),
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_TASKS_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(if (description.isNotBlank()) description else "Время выполнить запланированную задачу!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .addAction(0, "✓ Выполнено", completePendingIntent)
            .addAction(0, "⏰ Отложить на 15 мин", snoozePendingIntent)
            .build()

        notificationManager.notify(taskId.toInt(), notification)
    }
}
