package com.omniplan.app.ui.screens.tasks

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omniplan.app.core.model.Priority
import com.omniplan.app.core.model.Task
import com.omniplan.app.core.notification.AlarmScheduler
import com.omniplan.app.domain.repository.TaskRepository
import com.omniplan.app.widget.TodayTasksWidgetReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

enum class TaskFilter {
    ALL,
    TODAY,
    UPCOMING,
    IMPORTANT,
    COMPLETED
}

class TasksViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val searchQuery = MutableStateFlow("")
    val isKanbanView = MutableStateFlow(false)

    val tasks: StateFlow<List<Task>> = combine(
        taskRepository.getAllTasks(),
        selectedFilter,
        searchQuery
    ) { allTasks, filter, query ->
        allTasks.filter { task ->
            val matchesQuery = query.isBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.description.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                TaskFilter.ALL -> !task.isCompleted
                TaskFilter.TODAY -> !task.isCompleted && (task.isDueToday || task.isOverdue)
                TaskFilter.UPCOMING -> !task.isCompleted && task.dueDateTime != null && task.dueDateTime.toLocalDate().isAfter(LocalDate.now())
                TaskFilter.IMPORTANT -> !task.isCompleted && (task.priority == Priority.HIGH || task.priority == Priority.URGENT)
                TaskFilter.COMPLETED -> task.isCompleted
            }

            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: TaskFilter) {
        selectedFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun toggleKanbanView() {
        isKanbanView.value = !isKanbanView.value
    }

    fun toggleTaskCompletion(context: Context, task: Task) {
        viewModelScope.launch {
            val newStatus = !task.isCompleted
            taskRepository.toggleTaskCompletion(task.id, newStatus)
            if (newStatus) {
                AlarmScheduler.cancelTaskAlarm(context, task.id)
            } else if (task.reminderDateTime != null) {
                AlarmScheduler.scheduleTaskAlarm(context, task)
            }
            TodayTasksWidgetReceiver.updateAllWidgets(context)
        }
    }

    fun addTask(context: Context, task: Task) {
        viewModelScope.launch {
            val id = taskRepository.insertTask(task)
            val savedTask = task.copy(id = id)
            if (savedTask.reminderDateTime != null) {
                AlarmScheduler.scheduleTaskAlarm(context, savedTask)
            }
            TodayTasksWidgetReceiver.updateAllWidgets(context)
        }
    }

    fun deleteTask(context: Context, task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            AlarmScheduler.cancelTaskAlarm(context, task.id)
            TodayTasksWidgetReceiver.updateAllWidgets(context)
        }
    }
}
