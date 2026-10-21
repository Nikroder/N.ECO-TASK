package com.omniplan.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omniplan.app.core.model.Habit
import java.time.LocalDate

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val targetDaysPerWeek: Int = 7,
    val streakCount: Int = 0,
    val bestStreak: Int = 0,
    val colorHex: String = "#6750A4",
    val iconName: String = "fitness_center",
    val createdAt: LocalDate = LocalDate.now()
) {
    fun toDomain(isCompletedToday: Boolean = false, completedDates: List<LocalDate> = emptyList()): Habit = Habit(
        id = id,
        title = title,
        description = description,
        targetDaysPerWeek = targetDaysPerWeek,
        streakCount = streakCount,
        bestStreak = bestStreak,
        colorHex = colorHex,
        iconName = iconName,
        isCompletedToday = isCompletedToday,
        completedDates = completedDates,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(habit: Habit): HabitEntity = HabitEntity(
            id = habit.id,
            title = habit.title,
            description = habit.description,
            targetDaysPerWeek = habit.targetDaysPerWeek,
            streakCount = habit.streakCount,
            bestStreak = habit.bestStreak,
            colorHex = habit.colorHex,
            iconName = habit.iconName,
            createdAt = habit.createdAt
        )
    }
}
