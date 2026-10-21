package com.omniplan.app.core.model

import java.time.LocalDate

data class Habit(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val targetDaysPerWeek: Int = 7,
    val streakCount: Int = 0,
    val bestStreak: Int = 0,
    val colorHex: String = "#6750A4",
    val iconName: String = "fitness_center",
    val isCompletedToday: Boolean = false,
    val completedDates: List<LocalDate> = emptyList(),
    val createdAt: LocalDate = LocalDate.now()
)
