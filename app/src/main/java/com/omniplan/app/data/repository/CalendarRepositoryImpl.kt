package com.omniplan.app.data.repository

import com.omniplan.app.core.model.PomodoroSession
import com.omniplan.app.core.model.TimeBlock
import com.omniplan.app.data.local.dao.PomodoroDao
import com.omniplan.app.data.local.dao.TimeBlockDao
import com.omniplan.app.data.local.entity.PomodoroEntity
import com.omniplan.app.data.local.entity.TimeBlockEntity
import com.omniplan.app.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarRepositoryImpl(
    private val timeBlockDao: TimeBlockDao,
    private val pomodoroDao: PomodoroDao
) : CalendarRepository {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun getTimeBlocksForDate(date: LocalDate): Flow<List<TimeBlock>> {
        val startOfDay = date.atStartOfDay().format(formatter)
        val endOfDay = date.atTime(23, 59, 59).format(formatter)
        return timeBlockDao.getTimeBlocksForRange(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTimeBlock(block: TimeBlock): Long =
        timeBlockDao.insertTimeBlock(TimeBlockEntity.fromDomain(block))

    override suspend fun updateTimeBlock(block: TimeBlock) =
        timeBlockDao.updateTimeBlock(TimeBlockEntity.fromDomain(block))

    override suspend fun deleteTimeBlock(block: TimeBlock) =
        timeBlockDao.deleteTimeBlock(TimeBlockEntity.fromDomain(block))

    override suspend fun deleteTimeBlockById(id: Long) =
        timeBlockDao.deleteTimeBlockById(id)

    override fun getTodayPomodoroSessions(): Flow<List<PomodoroSession>> {
        val startOfDay = LocalDate.now().atStartOfDay().format(formatter)
        return pomodoroDao.getTodaySessions(startOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllPomodoroSessions(): Flow<List<PomodoroSession>> =
        pomodoroDao.getAllSessions().map { entities -> entities.map { it.toDomain() } }

    override suspend fun insertPomodoroSession(session: PomodoroSession): Long =
        pomodoroDao.insertSession(PomodoroEntity.fromDomain(session))

    override fun getTodayFocusMinutes(): Flow<Int> {
        val startOfDay = LocalDate.now().atStartOfDay().format(formatter)
        return pomodoroDao.getTodayFocusMinutes(startOfDay).map { it ?: 0 }
    }
}
