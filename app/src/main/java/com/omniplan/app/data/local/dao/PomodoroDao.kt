package com.omniplan.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omniplan.app.data.local.entity.PomodoroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {

    @Query("SELECT * FROM pomodoro_sessions WHERE completedAt >= :startOfDay ORDER BY completedAt DESC")
    fun getTodaySessions(startOfDay: String): Flow<List<PomodoroEntity>>

    @Query("SELECT * FROM pomodoro_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): Flow<List<PomodoroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroEntity): Long

    @Query("SELECT SUM(durationMinutes) FROM pomodoro_sessions WHERE completedAt >= :startOfDay")
    fun getTodayFocusMinutes(startOfDay: String): Flow<Int?>
}
