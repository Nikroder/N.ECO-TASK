package com.omniplan.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.omniplan.app.core.model.Category
import com.omniplan.app.data.local.dao.CategoryDao
import com.omniplan.app.data.local.dao.HabitDao
import com.omniplan.app.data.local.dao.PomodoroDao
import com.omniplan.app.data.local.dao.TaskDao
import com.omniplan.app.data.local.dao.TimeBlockDao
import com.omniplan.app.data.local.entity.CategoryEntity
import com.omniplan.app.data.local.entity.HabitEntity
import com.omniplan.app.data.local.entity.HabitLogEntity
import com.omniplan.app.data.local.entity.PomodoroEntity
import com.omniplan.app.data.local.entity.TaskEntity
import com.omniplan.app.data.local.entity.TimeBlockEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        HabitEntity::class,
        HabitLogEntity::class,
        TimeBlockEntity::class,
        PomodoroEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OmniPlanDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun habitDao(): HabitDao
    abstract fun timeBlockDao(): TimeBlockDao
    abstract fun pomodoroDao(): PomodoroDao

    companion object {
        @Volatile
        private var INSTANCE: OmniPlanDatabase? = null

        fun getInstance(context: Context): OmniPlanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OmniPlanDatabase::class.java,
                    "omniplan_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialCategories(database.categoryDao())
                    }
                }
            }

            private suspend fun populateInitialCategories(categoryDao: CategoryDao) {
                val initial = Category.DEFAULT_CATEGORIES.map { CategoryEntity.fromDomain(it) }
                categoryDao.insertCategories(initial)
            }
        }
    }
}
