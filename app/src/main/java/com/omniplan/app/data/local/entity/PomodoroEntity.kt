package com.omniplan.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.omniplan.app.core.model.PomodoroSession
import com.omniplan.app.core.model.SessionType
import java.time.LocalDateTime

@Entity(
    tableName = "pomodoro_sessions",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("taskId"),
        Index("completedAt")
    ]
)
data class PomodoroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long? = null,
    val taskTitle: String? = null,
    val durationMinutes: Int,
    val type: SessionType = SessionType.WORK,
    val completedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): PomodoroSession = PomodoroSession(
        id = id,
        taskId = taskId,
        taskTitle = taskTitle,
        durationMinutes = durationMinutes,
        type = type,
        completedAt = completedAt
    )

    companion object {
        fun fromDomain(session: PomodoroSession): PomodoroEntity = PomodoroEntity(
            id = session.id,
            taskId = session.taskId,
            taskTitle = session.taskTitle,
            durationMinutes = session.durationMinutes,
            type = session.type,
            completedAt = session.completedAt
        )
    }
}
