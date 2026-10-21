package com.omniplan.app.domain.repository

import com.omniplan.app.core.model.PomodoroSession
import com.omniplan.app.core.model.TimeBlock
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CalendarRepository {
    fun getTimeBlocksForDate(date: LocalDate): Flow<List<TimeBlock>>
    suspend fun insertTimeBlock(block: TimeBlock): Long
    suspend fun updateTimeBlock(block: TimeBlock)
    suspend fun deleteTimeBlock(block: TimeBlock)
    suspend fun deleteTimeBlockById(id: Long)

    fun getTodayPomodoroSessions(): Flow<List<PomodoroSession>>
    fun getAllPomodoroSessions(): Flow<List<PomodoroSession>>
    suspend fun insertPomodoroSession(session: PomodoroSession): Long
    fun getTodayFocusMinutes(): Flow<Int>
}
