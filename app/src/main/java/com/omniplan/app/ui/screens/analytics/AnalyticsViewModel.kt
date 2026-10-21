package com.omniplan.app.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omniplan.app.domain.repository.CalendarRepository
import com.omniplan.app.domain.repository.HabitRepository
import com.omniplan.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProductivityStats(
    val completedTasks: Int,
    val activeTasks: Int,
    val totalFocusMinutes: Int,
    val totalHabits: Int,
    val completionRate: Int
)

class AnalyticsViewModel(
    taskRepository: TaskRepository,
    habitRepository: HabitRepository,
    calendarRepository: CalendarRepository
) : ViewModel() {

    val stats: StateFlow<ProductivityStats> = combine(
        taskRepository.getCompletedTasksCount(),
        taskRepository.getActiveTasksCount(),
        calendarRepository.getTodayFocusMinutes(),
        habitRepository.getAllHabits()
    ) { completed, active, focusMin, habits ->
        val total = completed + active
        val rate = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100).toInt() else 0

        ProductivityStats(
            completedTasks = completed,
            activeTasks = active,
            totalFocusMinutes = focusMin,
            totalHabits = habits.size,
            completionRate = rate
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProductivityStats(0, 0, 0, 0, 0)
    )
}
