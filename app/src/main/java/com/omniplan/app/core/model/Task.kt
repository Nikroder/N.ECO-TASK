package com.omniplan.app.core.model

import java.time.LocalDateTime

enum class RecurrenceType {
    NONE,
    DAILY,
    WEEKDAYS,
    WEEKLY,
    MONTHLY,
    YEARLY
}

data class ChecklistItem(
    val id: String,
    val title: String,
    val isDone: Boolean = false
)

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dueDateTime: LocalDateTime? = null,
    val reminderDateTime: LocalDateTime? = null,
    val priority: Priority = Priority.NONE,
    val categoryId: Long? = null,
    val isCompleted: Boolean = false,
    val completedAt: LocalDateTime? = null,
    val tags: List<String> = emptyList(),
    val checklist: List<ChecklistItem> = emptyList(),
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val estimatedMinutes: Int = 0,
    val orderIndex: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    val isOverdue: Boolean
        get() = !isCompleted && dueDateTime != null && dueDateTime.isBefore(LocalDateTime.now())

    val isDueToday: Boolean
        get() = dueDateTime?.toLocalDate() == java.time.LocalDate.now()
}
