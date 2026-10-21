package com.omniplan.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.omniplan.app.core.model.ChecklistItem
import com.omniplan.app.core.model.Priority
import com.omniplan.app.core.model.RecurrenceType
import com.omniplan.app.core.model.Task
import java.time.LocalDateTime

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("categoryId"),
        Index("isCompleted"),
        Index("dueDateTime")
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
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
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        description = description,
        dueDateTime = dueDateTime,
        reminderDateTime = reminderDateTime,
        priority = priority,
        categoryId = categoryId,
        isCompleted = isCompleted,
        completedAt = completedAt,
        tags = tags,
        checklist = checklist,
        recurrence = recurrence,
        estimatedMinutes = estimatedMinutes,
        orderIndex = orderIndex,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            dueDateTime = task.dueDateTime,
            reminderDateTime = task.reminderDateTime,
            priority = task.priority,
            categoryId = task.categoryId,
            isCompleted = task.isCompleted,
            completedAt = task.completedAt,
            tags = task.tags,
            checklist = task.checklist,
            recurrence = task.recurrence,
            estimatedMinutes = task.estimatedMinutes,
            orderIndex = task.orderIndex,
            createdAt = task.createdAt
        )
    }
}
