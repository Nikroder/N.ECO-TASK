package com.omniplan.app

import com.omniplan.app.core.model.Priority
import com.omniplan.app.core.model.RecurrenceType
import com.omniplan.app.core.util.NlpTaskParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class NlpTaskParserTest {

    private val fixedNow = LocalDateTime.of(2026, 10, 21, 10, 0) // Wednesday 10:00

    @Test
    fun parseTomorrowWithTimeAndPriorityAndTag() {
        val input = "Завтра в 15:30 созвон с командой #работа !срочно"
        val result = NlpTaskParser.parse(input, fixedNow)

        assertEquals("созвон с командой", result.cleanTitle)
        assertEquals(LocalDateTime.of(2026, 10, 22, 15, 30), result.dueDateTime)
        assertEquals(Priority.URGENT, result.priority)
        assertTrue(result.tags.contains("работа"))
        assertEquals(RecurrenceType.NONE, result.recurrence)
    }

    @Test
    fun parseRelativeHoursAndUrgent() {
        val input = "Выпить лекарство через 2 часа !важно #здоровье"
        val result = NlpTaskParser.parse(input, fixedNow)

        assertEquals("Выпить лекарство", result.cleanTitle)
        assertEquals(LocalDateTime.of(2026, 10, 21, 12, 0), result.dueDateTime)
        assertEquals(Priority.HIGH, result.priority)
        assertTrue(result.tags.contains("здоровье"))
    }

    @Test
    fun parseDailyHabitRecurrence() {
        val input = "Каждый день делать зарядку в 8:00 #спорт"
        val result = NlpTaskParser.parse(input, fixedNow)

        assertEquals("делать зарядку", result.cleanTitle)
        assertEquals(RecurrenceType.DAILY, result.recurrence)
        assertEquals(LocalTime.of(8, 0), result.dueDateTime?.toLocalTime())
        assertTrue(result.tags.contains("спорт"))
    }

    @Test
    fun parseFridayMeeting() {
        val input = "В пятницу отчет по проекту"
        val result = NlpTaskParser.parse(input, fixedNow) // Now is Wednesday 21st, Friday is 23rd

        assertEquals("отчет по проекту", result.cleanTitle)
        assertEquals(LocalDate.of(2026, 10, 23), result.dueDateTime?.toLocalDate())
    }
}
