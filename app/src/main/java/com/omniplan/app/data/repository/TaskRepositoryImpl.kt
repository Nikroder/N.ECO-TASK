package com.omniplan.app.data.repository

import com.omniplan.app.core.model.Category
import com.omniplan.app.core.model.Task
import com.omniplan.app.data.local.dao.CategoryDao
import com.omniplan.app.data.local.dao.TaskDao
import com.omniplan.app.data.local.entity.CategoryEntity
import com.omniplan.app.data.local.entity.TaskEntity
import com.omniplan.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao
) : TaskRepository {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { entities -> entities.map { it.toDomain() } }

    override fun getTasksByCompletion(isCompleted: Boolean): Flow<List<Task>> =
        taskDao.getTasksByCompletion(isCompleted).map { entities -> entities.map { it.toDomain() } }

    override fun getTodayTasks(date: LocalDate): Flow<List<Task>> {
        val startOfDay = date.atStartOfDay().format(formatter)
        val endOfDay = date.atTime(23, 59, 59).format(formatter)
        return taskDao.getTodayAndOverdueTasks(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTaskById(id: Long): Flow<Task?> =
        taskDao.getTaskById(id).map { it?.toDomain() }

    override suspend fun getTaskByIdSync(id: Long): Task? =
        taskDao.getTaskByIdSync(id)?.toDomain()

    override suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(TaskEntity.fromDomain(task))

    override suspend fun updateTask(task: Task) =
        taskDao.updateTask(TaskEntity.fromDomain(task))

    override suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(TaskEntity.fromDomain(task))

    override suspend fun deleteTaskById(id: Long) =
        taskDao.deleteTaskById(id)

    override suspend fun toggleTaskCompletion(id: Long, isCompleted: Boolean) {
        val completedAt = if (isCompleted) LocalDateTime.now().format(formatter) else null
        taskDao.setTaskCompleted(id, isCompleted, completedAt)
    }

    override fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query).map { entities -> entities.map { it.toDomain() } }

    override fun getCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories().map { entities -> entities.map { it.toDomain() } }

    override suspend fun insertCategory(category: Category): Long =
        categoryDao.insertCategory(CategoryEntity.fromDomain(category))

    override fun getCompletedTasksCount(): Flow<Int> =
        taskDao.getCompletedTasksCount()

    override fun getActiveTasksCount(): Flow<Int> =
        taskDao.getActiveTasksCount()
}
