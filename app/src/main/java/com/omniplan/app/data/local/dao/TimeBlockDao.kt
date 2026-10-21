package com.omniplan.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.omniplan.app.data.local.entity.TimeBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeBlockDao {

    @Query("SELECT * FROM time_blocks WHERE startTime >= :startTime AND endTime <= :endTime ORDER BY startTime ASC")
    fun getTimeBlocksForRange(startTime: String, endTime: String): Flow<List<TimeBlockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeBlock(block: TimeBlockEntity): Long

    @Update
    suspend fun updateTimeBlock(block: TimeBlockEntity)

    @Delete
    suspend fun deleteTimeBlock(block: TimeBlockEntity)

    @Query("DELETE FROM time_blocks WHERE id = :id")
    suspend fun deleteTimeBlockById(id: Long)
}
