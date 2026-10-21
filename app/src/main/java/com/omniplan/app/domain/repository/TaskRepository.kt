package com.omniplan.app.domain.repository

import com.omniplan.app.core.model.Category
import com.omniplan.app.core.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<Task>>
    fun getTodayTasks(date: LocalDate = LocalDate.now()): Flow<List<Task>>
    fun getTaskById(id: Long): Flow<Task?>
    suspend fun getTaskByIdSync(id: Long): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(id: Long)
    suspend fun toggleTaskCompletion(id: Long, isCompleted: Boolean)
    fun searchTasks(query: String): Flow<List<Task>>
    fun getCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category): Long
    fun getCompletedTasksCount(): Flow<Int>
    fun getActiveTasksCount(): Flow<Int>
}
