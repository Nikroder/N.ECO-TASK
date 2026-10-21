package com.omniplan.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.omniplan.app.core.model.TimeBlock
import java.time.LocalDateTime

@Entity(
    tableName = "time_blocks",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index("taskId"),
        Index("startTime"),
        Index("endTime")
    ]
)
data class TimeBlockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long? = null,
    val title: String,
    val description: String = "",
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val colorHex: String = "#6750A4",
    val isCompleted: Boolean = false
) {
    fun toDomain(): TimeBlock = TimeBlock(
        id = id,
        taskId = taskId,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        colorHex = colorHex,
        isCompleted = isCompleted
    )

    companion object {
        fun fromDomain(block: TimeBlock): TimeBlockEntity = TimeBlockEntity(
            id = block.id,
            taskId = block.taskId,
            title = block.title,
            description = block.description,
            startTime = block.startTime,
            endTime = block.endTime,
            colorHex = block.colorHex,
            isCompleted = block.isCompleted
        )
    }
}
