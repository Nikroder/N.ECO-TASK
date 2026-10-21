package com.omniplan.app.data.local

import androidx.room.TypeConverter
import com.omniplan.app.core.model.ChecklistItem
import com.omniplan.app.core.model.Priority
import com.omniplan.app.core.model.RecurrenceType
import com.omniplan.app.core.model.SessionType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {

    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? =
        dateTime?.format(dateTimeFormatter)

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? =
        date?.format(dateFormatter)

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it, dateFormatter) }

    @TypeConverter
    fun fromPriority(priority: Priority?): Int =
        priority?.level ?: Priority.NONE.level

    @TypeConverter
    fun toPriority(level: Int): Priority =
        Priority.fromLevel(level)

    @TypeConverter
    fun fromRecurrence(recurrence: RecurrenceType?): String =
        recurrence?.name ?: RecurrenceType.NONE.name

    @TypeConverter
    fun toRecurrence(value: String?): RecurrenceType =
        try {
            RecurrenceType.valueOf(value ?: RecurrenceType.NONE.name)
        } catch (e: Exception) {
            RecurrenceType.NONE
        }

    @TypeConverter
    fun fromSessionType(type: SessionType?): String =
        type?.name ?: SessionType.WORK.name

    @TypeConverter
    fun toSessionType(value: String?): SessionType =
        try {
            SessionType.valueOf(value ?: SessionType.WORK.name)
        } catch (e: Exception) {
            SessionType.WORK
        }

    @TypeConverter
    fun fromStringList(list: List<String>?): String =
        list?.joinToString(",") ?: ""

    @TypeConverter
    fun toStringList(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList() else value.split(",").map { it.trim() }

    @TypeConverter
    fun fromChecklist(list: List<ChecklistItem>?): String {
        if (list.isNullOrEmpty()) return ""
        return list.joinToString(";;;") { "${it.id}:::${it.title}:::${it.isDone}" }
    }

    @TypeConverter
    fun toChecklist(value: String?): List<ChecklistItem> {
        if (value.isNullOrBlank()) return emptyList()
        return value.split(";;;").mapNotNull { itemStr ->
            val parts = itemStr.split(":::")
            if (parts.size >= 3) {
                ChecklistItem(id = parts[0], title = parts[1], isDone = parts[2].toBoolean())
            } else null
        }
    }
}
