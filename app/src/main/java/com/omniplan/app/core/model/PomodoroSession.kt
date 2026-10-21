package com.omniplan.app.core.model

import java.time.LocalDateTime

enum class SessionType {
    WORK,
    SHORT_BREAK,
    LONG_BREAK
}

data class PomodoroSession(
    val id: Long = 0,
    val taskId: Long? = null,
    val taskTitle: String? = null,
    val durationMinutes: Int,
    val type: SessionType = SessionType.WORK,
    val completedAt: LocalDateTime = LocalDateTime.now()
)
