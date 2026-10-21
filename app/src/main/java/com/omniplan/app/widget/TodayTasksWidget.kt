package com.omniplan.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.omniplan.app.core.model.Priority
import com.omniplan.app.core.model.Task
import com.omniplan.app.core.util.DateTimeUtils
import com.omniplan.app.data.local.OmniPlanDatabase
import com.omniplan.app.ui.MainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodayTasksWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val database = OmniPlanDatabase.getInstance(context)
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val endOfDay = today.atTime(23, 59, 59).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val taskEntities = database.taskDao().getTodayAndOverdueTasksSync(startOfDay, endOfDay)
        val tasks = taskEntities.map { it.toDomain() }

        val completedCount = tasks.count { it.isCompleted }
        val totalCount = tasks.size

        provideContent {
            WidgetContent(
                today = today,
                tasks = tasks,
                completedCount = completedCount,
                totalCount = totalCount
            )
        }
    }

    @Composable
    private fun WidgetContent(
        today: LocalDate,
        tasks: List<Task>,
        completedCount: Int,
        totalCount: Int
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFF1E1E24)))
                .cornerRadius(16.dp)
                .padding(12.dp)
        ) {
            // Header Row
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = GlanceModifier.defaultWeight()) {
                    Text(
                        text = "N.ECO TASK • ${DateTimeUtils.formatDate(today)}",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFBB86FC)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = if (totalCount > 0) "Выполнено $completedCount из $totalCount" else "Все задачи выполнены",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // Add Task Button
                Box(
                    modifier = GlanceModifier
                        .size(32.dp)
                        .background(ColorProvider(Color(0xFF6750A4)))
                        .cornerRadius(16.dp)
                        .clickable(actionStartActivity<MainActivity>()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(8.dp))

            // Task List or Empty State
            if (tasks.isEmpty()) {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "На сегодня задач нет 🎉",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFAAAAAA)),
                            fontSize = 13.sp
                        )
                    )
                }
            } else {
                LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                    items(tasks) { task ->
                        TaskWidgetItem(task = task)
                    }
                }
            }
        }
    }

    @Composable
    private fun TaskWidgetItem(task: Task) {
        val priorityColor = when (task.priority) {
            Priority.URGENT -> Color(0xFFD32F2F)
            Priority.HIGH -> Color(0xFFF57C00)
            Priority.MEDIUM -> Color(0xFF1976D2)
            Priority.LOW -> Color(0xFF388E3C)
            Priority.NONE -> Color(0xFF555555)
        }

        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(ColorProvider(Color(0xFF2A2A32)))
                .cornerRadius(8.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Priority stripe
            Box(
                modifier = GlanceModifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(ColorProvider(priorityColor))
                    .cornerRadius(2.dp)
            ) {}

            Spacer(modifier = GlanceModifier.width(8.dp))

            // Checkbox directly toggling task
            CheckBox(
                checked = task.isCompleted,
                onCheckedChange = actionRunCallback<ToggleTaskActionCallback>(
                    parameters = actionParametersOf(
                        ToggleTaskActionCallback.TASK_ID_KEY to task.id,
                        ToggleTaskActionCallback.CURRENT_STATUS_KEY to task.isCompleted
                    )
                )
            )

            Spacer(modifier = GlanceModifier.width(8.dp))

            // Task Title & time
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = task.title,
                    style = TextStyle(
                        color = ColorProvider(if (task.isCompleted) Color(0xFF888888) else Color.White),
                        fontSize = 13.sp,
                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium
                    ),
                    maxLines = 1
                )
                if (task.dueDateTime != null) {
                    Text(
                        text = DateTimeUtils.formatTime(task.dueDateTime),
                        style = TextStyle(
                            color = ColorProvider(Color(0xFFBB86FC)),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}
