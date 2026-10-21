package com.omniplan.app.data.repository

import com.omniplan.app.core.model.Habit
import com.omniplan.app.data.local.dao.HabitDao
import com.omniplan.app.data.local.entity.HabitEntity
import com.omniplan.app.data.local.entity.HabitLogEntity
import com.omniplan.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HabitRepositoryImpl(
    private val habitDao: HabitDao
) : HabitRepository {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun getAllHabits(): Flow<List<Habit>> {
        val todayStr = LocalDate.now().format(dateFormatter)
        return habitDao.getAllHabits().combine(
            // To get real-time status, we map habits
            kotlinx.coroutines.flow.flow { emit(todayStr) }
        ) { entities, _ ->
            entities.map { entity ->
                val isCompletedToday = habitDao.isHabitCompletedOnDate(entity.id, todayStr) > 0
                entity.toDomain(isCompletedToday = isCompletedToday)
            }
        }
    }

    override suspend fun insertHabit(habit: Habit): Long =
        habitDao.insertHabit(HabitEntity.fromDomain(habit))

    override suspend fun updateHabit(habit: Habit) =
        habitDao.updateHabit(HabitEntity.fromDomain(habit))

    override suspend fun deleteHabit(habit: Habit) =
        habitDao.deleteHabit(HabitEntity.fromDomain(habit))

    override suspend fun toggleHabitCheckIn(habitId: Long, date: LocalDate) {
        val dateStr = date.format(dateFormatter)
        val isDone = habitDao.isHabitCompletedOnDate(habitId, dateStr) > 0
        if (isDone) {
            habitDao.deleteHabitLog(habitId, dateStr)
        } else {
            habitDao.insertHabitLog(HabitLogEntity(habitId = habitId, date = date, isCompleted = true))
        }
    }
}
