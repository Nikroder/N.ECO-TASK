package com.omniplan.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.omniplan.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, priority DESC, orderIndex ASC, dueDateTime ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted ORDER BY priority DESC, dueDateTime ASC")
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDateTime BETWEEN :startOfDay AND :endOfDay ORDER BY isCompleted ASC, priority DESC, dueDateTime ASC")
    fun getTodayTasks(startOfDay: String, endOfDay: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE (dueDateTime BETWEEN :startOfDay AND :endOfDay) OR (isCompleted = 0 AND dueDateTime < :startOfDay) ORDER BY isCompleted ASC, priority DESC, dueDateTime ASC")
    fun getTodayAndOverdueTasks(startOfDay: String, endOfDay: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE (dueDateTime BETWEEN :startOfDay AND :endOfDay) OR (isCompleted = 0 AND dueDateTime < :startOfDay) ORDER BY isCompleted ASC, priority DESC, dueDateTime ASC")
    fun getTodayAndOverdueTasksSync(startOfDay: String, endOfDay: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskByIdSync(id: Long): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)

    @Query("UPDATE tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun setTaskCompleted(id: Long, isCompleted: Boolean, completedAt: String?)

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<TaskEntity>>

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    fun getCompletedTasksCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    fun getActiveTasksCount(): Flow<Int>
}
