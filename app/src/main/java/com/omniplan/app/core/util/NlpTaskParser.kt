package com.omniplan.app.core.util

import com.omniplan.app.core.model.Priority
import com.omniplan.app.core.model.RecurrenceType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.regex.Pattern

data class ParsedTaskResult(
    val cleanTitle: String,
    val dueDateTime: LocalDateTime?,
    val priority: Priority,
    val tags: List<String>,
    val recurrence: RecurrenceType
)

object NlpTaskParser {

    fun parse(input: String, now: LocalDateTime = LocalDateTime.now()): ParsedTaskResult {
        var text = input.trim()
        var priority = Priority.NONE
        val tags = mutableListOf<String>()
        var recurrence = RecurrenceType.NONE
        var targetDate: LocalDate? = null
        var targetTime: LocalTime? = null

        // 1. Extract Tags (#tag)
        val tagPattern = Pattern.compile("#([a-zA-Zа-яА-Я0-9_]+)")
        val tagMatcher = tagPattern.matcher(text)
        val extractedTags = mutableListOf<String>()
        while (tagMatcher.find()) {
            tagMatcher.group(1)?.let { extractedTags.add(it) }
        }
        tags.addAll(extractedTags)
        text = tagMatcher.replaceAll("").trim()

        // 2. Extract Priority (!urgent, !высокий, !p1, etc.)
        val priorityMap = mapOf(
            "(?i)!(срочно|urgent|p1|критично)" to Priority.URGENT,
            "(?i)!(высокий|важно|high|p2)" to Priority.HIGH,
            "(?i)!(средний|medium|p3)" to Priority.MEDIUM,
            "(?i)!(низкий|low|неважно|p4)" to Priority.LOW
        )
        for ((patternStr, prio) in priorityMap) {
            val pattern = Pattern.compile(patternStr)
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                priority = prio
                text = matcher.replaceAll("").trim()
                break
            }
        }

        // 3. Extract Recurrence
        val recurrenceDailyPattern = Pattern.compile("(?i)(каждый\\s+день|ежедневно|every\\s+day)")
        if (recurrenceDailyPattern.matcher(text).find()) {
            recurrence = RecurrenceType.DAILY
            text = recurrenceDailyPattern.matcher(text).replaceAll("").trim()
        }

        val recurrenceWeekdaysPattern = Pattern.compile("(?i)(по\\s+будням|каждый\\s+будний\\s+день|every\\s+weekday)")
        if (recurrenceWeekdaysPattern.matcher(text).find()) {
            recurrence = RecurrenceType.WEEKDAYS
            text = recurrenceWeekdaysPattern.matcher(text).replaceAll("").trim()
        }

        val recurrenceWeeklyPattern = Pattern.compile("(?i)(каждую\\s+неделю|еженедельно|every\\s+week)")
        if (recurrenceWeeklyPattern.matcher(text).find()) {
            recurrence = RecurrenceType.WEEKLY
            text = recurrenceWeeklyPattern.matcher(text).replaceAll("").trim()
        }

        // 4. Extract Relative "Через N часов / минут / дней"
        val inHoursPattern = Pattern.compile("(?i)через\\s+(\\d+)\\s*(час(?:а|ов)?|ч)")
        val inHoursMatcher = inHoursPattern.matcher(text)
        if (inHoursMatcher.find()) {
            val hours = inHoursMatcher.group(1)?.toLongOrNull() ?: 0L
            val target = now.plusHours(hours)
            targetDate = target.toLocalDate()
            targetTime = target.toLocalTime().withSecond(0).withNano(0)
            text = inHoursMatcher.replaceAll("").trim()
        }

        val inMinutesPattern = Pattern.compile("(?i)через\\s+(\\d+)\\s*(минут(?:ы)?|мин)")
        val inMinutesMatcher = inMinutesPattern.matcher(text)
        if (inMinutesMatcher.find()) {
            val minutes = inMinutesMatcher.group(1)?.toLongOrNull() ?: 0L
            val target = now.plusMinutes(minutes)
            targetDate = target.toLocalDate()
            targetTime = target.toLocalTime().withSecond(0).withNano(0)
            text = inMinutesMatcher.replaceAll("").trim()
        }

        val inDaysPattern = Pattern.compile("(?i)через\\s+(\\d+)\\s*(д(?:ень|ня|ней)?)")
        val inDaysMatcher = inDaysPattern.matcher(text)
        if (inDaysMatcher.find()) {
            val days = inDaysMatcher.group(1)?.toLongOrNull() ?: 0L
            targetDate = now.toLocalDate().plusDays(days)
            text = inDaysMatcher.replaceAll("").trim()
        }

        // 5. Extract Days (сегодня, завтра, послезавтра, today, tomorrow)
        if (targetDate == null) {
            val todayPattern = Pattern.compile("(?i)\\b(сегодня|today)\\b")
            if (todayPattern.matcher(text).find()) {
                targetDate = now.toLocalDate()
                text = todayPattern.matcher(text).replaceAll("").trim()
            }

            val tomorrowPattern = Pattern.compile("(?i)\\b(завтра|tomorrow)\\b")
            if (tomorrowPattern.matcher(text).find()) {
                targetDate = now.toLocalDate().plusDays(1)
                text = tomorrowPattern.matcher(text).replaceAll("").trim()
            }

            val afterTomorrowPattern = Pattern.compile("(?i)\\b(послезавтра)\\b")
            if (afterTomorrowPattern.matcher(text).find()) {
                targetDate = now.toLocalDate().plusDays(2)
                text = afterTomorrowPattern.matcher(text).replaceAll("").trim()
            }
        }

        // 6. Extract Days of week ("в понедельник", "во вторник", "в пятницу", etc.)
        val daysOfWeekMap = mapOf(
            "(?i)\\b(в|во)?\\s*(понедельник|monday)\\b" to DayOfWeek.MONDAY,
            "(?i)\\b(в|во)?\\s*(вторник|tuesday)\\b" to DayOfWeek.TUESDAY,
            "(?i)\\b(в|во)?\\s*(среду|среда|wednesday)\\b" to DayOfWeek.WEDNESDAY,
            "(?i)\\b(в|во)?\\s*(четверг|thursday)\\b" to DayOfWeek.THURSDAY,
            "(?i)\\b(в|во)?\\s*(пятницу|пятница|friday)\\b" to DayOfWeek.FRIDAY,
            "(?i)\\b(в|во)?\\s*(субботу|суббота|saturday)\\b" to DayOfWeek.SATURDAY,
            "(?i)\\b(в|во)?\\s*(воскресенье|sunday)\\b" to DayOfWeek.SUNDAY
        )

        if (targetDate == null) {
            for ((dayRegex, dayOfWeek) in daysOfWeekMap) {
                val pattern = Pattern.compile(dayRegex)
                val matcher = pattern.matcher(text)
                if (matcher.find()) {
                    val currentDayOfWeek = now.dayOfWeek
                    val daysToAdd = (dayOfWeek.value - currentDayOfWeek.value + 7) % 7
                    val calculatedDate = if (daysToAdd == 0) now.toLocalDate().plusWeeks(1) else now.toLocalDate().plusDays(daysToAdd.toLong())
                    targetDate = calculatedDate
                    text = matcher.replaceAll("").trim()
                    break
                }
            }
        }

        // 7. Extract Exact Time ("в 15:30", "at 14:00", "в 18ч", "в 9")
        val timePattern = Pattern.compile("(?i)(?:в|at)?\\s*(\\d{1,2}):(\\d{2})")
        val timeMatcher = timePattern.matcher(text)
        if (timeMatcher.find()) {
            val hour = timeMatcher.group(1)?.toIntOrNull() ?: 0
            val minute = timeMatcher.group(2)?.toIntOrNull() ?: 0
            if (hour in 0..23 && minute in 0..59) {
                targetTime = LocalTime.of(hour, minute)
                text = timeMatcher.replaceAll("").trim()
            }
        } else {
            val simpleHourPattern = Pattern.compile("(?i)\\bв\\s*(\\d{1,2})(?:\\s*(?:часов|часа|ч))?\\b")
            val simpleHourMatcher = simpleHourPattern.matcher(text)
            if (simpleHourMatcher.find()) {
                val hour = simpleHourMatcher.group(1)?.toIntOrNull() ?: 0
                if (hour in 0..23) {
                    targetTime = LocalTime.of(hour, 0)
                    text = simpleHourMatcher.replaceAll("").trim()
                }
            }
        }

        // Clean extra spaces or remaining punctuation
        val cleanTitle = text.replace(Regex("\\s+"), " ").trim(' ', ',', '.')

        // Combine date & time
        val finalDateTime = when {
            targetDate != null && targetTime != null -> LocalDateTime.of(targetDate, targetTime)
            targetDate != null -> LocalDateTime.of(targetDate, LocalTime.of(9, 0)) // default 9:00 AM
            targetTime != null -> {
                // If only time is given, if it's already passed today, set to tomorrow
                val todayWithTime = LocalDateTime.of(now.toLocalDate(), targetTime)
                if (todayWithTime.isBefore(now)) todayWithTime.plusDays(1) else todayWithTime
            }
            else -> null
        }

        return ParsedTaskResult(
            cleanTitle = if (cleanTitle.isEmpty()) input.trim() else cleanTitle,
            dueDateTime = finalDateTime,
            priority = priority,
            tags = tags,
            recurrence = recurrence
        )
    }
}
