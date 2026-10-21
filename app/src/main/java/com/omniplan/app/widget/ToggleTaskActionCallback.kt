package com.omniplan.app.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.omniplan.app.data.local.OmniPlanDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ToggleTaskActionCallback : ActionCallback {

    companion object {
        val TASK_ID_KEY = ActionParameters.Key<Long>("task_id")
        val CURRENT_STATUS_KEY = ActionParameters.Key<Boolean>("current_status")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val taskId = parameters[TASK_ID_KEY] ?: return
        val currentStatus = parameters[CURRENT_STATUS_KEY] ?: false
        val newStatus = !currentStatus

        val database = OmniPlanDatabase.getInstance(context)
        val nowStr = if (newStatus) LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) else null

        database.taskDao().setTaskCompleted(taskId, newStatus, nowStr)

        // Refresh all widgets
        TodayTasksWidget().updateAll(context)
    }
}
