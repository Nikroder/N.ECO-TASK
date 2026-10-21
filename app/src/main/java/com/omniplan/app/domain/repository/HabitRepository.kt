package com.omniplan.app.domain.repository

import com.omniplan.app.core.model.Habit
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun toggleHabitCheckIn(habitId: Long, date: LocalDate = LocalDate.now())
}
