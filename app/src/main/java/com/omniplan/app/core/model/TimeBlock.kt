package com.omniplan.app.core.model

import java.time.LocalDateTime

data class TimeBlock(
    val id: Long = 0,
    val taskId: Long? = null,
    val title: String,
    val description: String = "",
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val colorHex: String = "#6750A4",
    val isCompleted: Boolean = false
) {
    val durationMinutes: Long
        get() = java.time.Duration.between(startTime, endTime).toMinutes()
}
