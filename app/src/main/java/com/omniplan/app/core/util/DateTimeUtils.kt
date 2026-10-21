package com.omniplan.app.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateTimeUtils {
    private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
    private val FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    private val DAY_MONTH_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMM, HH:mm", Locale("ru"))

    fun formatTime(dateTime: LocalDateTime): String = dateTime.format(TIME_FORMATTER)

    fun formatDate(date: LocalDate): String = date.format(DATE_FORMATTER)

    fun formatFullDate(date: LocalDate): String = date.format(FULL_DATE_FORMATTER)

    fun formatRelativeDateTime(dateTime: LocalDateTime): String {
        val today = LocalDate.now()
        val targetDate = dateTime.toLocalDate()
        val timeStr = formatTime(dateTime)

        return when (ChronoUnit.DAYS.between(today, targetDate)) {
            0L -> "Сегодня, $timeStr"
            1L -> "Завтра, $timeStr"
            -1L -> "Вчера, $timeStr"
            in 2L..6L -> {
                val dayOfWeek = targetDate.dayOfWeek.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    Locale("ru")
                ).replaceFirstChar { it.uppercase() }
                "$dayOfWeek, $timeStr"
            }
            else -> dateTime.format(DAY_MONTH_TIME_FORMATTER)
        }
    }

    fun formatRelativeDate(date: LocalDate): String {
        val today = LocalDate.now()
        return when (ChronoUnit.DAYS.between(today, date)) {
            0L -> "Сегодня"
            1L -> "Завтра"
            -1L -> "Вчера"
            else -> date.format(DATE_FORMATTER)
        }
    }
}
