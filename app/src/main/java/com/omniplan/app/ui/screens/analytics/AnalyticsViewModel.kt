package com.omniplan.app.ui.screens.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omniplan.app.core.model.Habit
import com.omniplan.app.core.model.Task
import com.omniplan.app.domain.repository.CalendarRepository
import com.omniplan.app.domain.repository.HabitRepository
import com.omniplan.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class LifeSphere(
    val title: String,
    val emoji: String,
    val hexColor: Long,
    val keywords: List<String>
) {
    CAREER("Карьера", "💼", 0xFF6366F1, listOf("работа", "проект", "код", "учеба", "career", "work", "job", "клиент", "отчет", "бизнес")),
    HEALTH("Здоровье", "🏋️", 0xFF10B981, listOf("спорт", "бег", "зал", "вода", "здоровье", "сон", "тренировка", "питание", "health", "шаги")),
    GROWTH("Развитие", "📚", 0xFF8B5CF6, listOf("книги", "языки", "курс", "навык", "чтение", "английский", "лекция", "study", "learn", "урок")),
    FINANCE("Финансы", "💰", 0xFFF59E0B, listOf("деньги", "бюджет", "покупки", "инвестиции", "счет", "оплата", "finance", "банк", "купить")),
    RELATIONS("Окружение", "🤝", 0xFFEC4899, listOf("семья", "друзья", "встреча", "звонок", "родители", "общение", "party", "подарок", "др")),
    REST("Отдых", "🧘", 0xFF06B6D4, listOf("отдых", "медитация", "прогулка", "фильм", "игра", "музыка", "relax", "chill", "выходной"))
}

data class LifeBalanceItem(
    val sphere: LifeSphere,
    val scorePercent: Int,
    val tasksCount: Int
)

data class ChronotypeDistribution(
    val morningPercent: Int,
    val dayPercent: Int,
    val eveningPercent: Int,
    val nightPercent: Int,
    val peakLabel: String,
    val peakEmoji: String
)

data class RewindStats(
    val titleBadge: String,
    val titleDescription: String,
    val totalFocusMinutes: Int,
    val completedTasksCount: Int,
    val bestHabitTitle: String?,
    val bestHabitStreak: Int,
    val dominantSphere: LifeSphere,
    val completionRate: Int
)

data class ProductivityStats(
    val completedTasks: Int,
    val activeTasks: Int,
    val totalFocusMinutes: Int,
    val totalHabits: Int,
    val completionRate: Int,
    val lifeBalance: List<LifeBalanceItem>,
    val chronotype: ChronotypeDistribution,
    val rewindStats: RewindStats
)

class AnalyticsViewModel(
    taskRepository: TaskRepository,
    habitRepository: HabitRepository,
    calendarRepository: CalendarRepository
) : ViewModel() {

    val stats: StateFlow<ProductivityStats> = combine(
        taskRepository.getAllTasks(),
        habitRepository.getAllHabits(),
        calendarRepository.getTodayFocusMinutes()
    ) { tasks, habits, focusMin ->
        computeProductivityStats(tasks, habits, focusMin)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProductivityStats(
            completedTasks = 0,
            activeTasks = 0,
            totalFocusMinutes = 0,
            totalHabits = 0,
            completionRate = 0,
            lifeBalance = LifeSphere.entries.map { LifeBalanceItem(it, 50, 0) },
            chronotype = ChronotypeDistribution(40, 35, 20, 5, "Утренний Снайпер", "🌅"),
            rewindStats = RewindStats(
                titleBadge = "⚡ Неоновый Кибер-Стратег",
                titleDescription = "Твоя продуктивность задает ритм новому дню!",
                totalFocusMinutes = 0,
                completedTasksCount = 0,
                bestHabitTitle = null,
                bestHabitStreak = 0,
                dominantSphere = LifeSphere.CAREER,
                completionRate = 0
            )
        )
    )

    private fun computeProductivityStats(
        tasks: List<Task>,
        habits: List<Habit>,
        focusMinutes: Int
    ): ProductivityStats {
        val completed = tasks.count { it.isCompleted }
        val active = tasks.count { !it.isCompleted }
        val total = completed + active
        val rate = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100).toInt() else 0

        // 1. Life Balance Calculation
        val sphereTaskCounts = mutableMapOf<LifeSphere, Int>()
        LifeSphere.entries.forEach { sphere ->
            val matchTasks = tasks.count { task ->
                val textToMatch = (task.title + " " + task.description + " " + task.tags.joinToString(" ")).lowercase()
                sphere.keywords.any { keyword -> textToMatch.contains(keyword) }
            }
            val matchHabits = habits.count { habit ->
                val textToMatch = (habit.title + " " + habit.description).lowercase()
                sphere.keywords.any { keyword -> textToMatch.contains(keyword) }
            }
            sphereTaskCounts[sphere] = matchTasks + matchHabits
        }

        val maxCount = sphereTaskCounts.values.maxOrNull()?.coerceAtLeast(1) ?: 1
        val lifeBalanceItems = LifeSphere.entries.map { sphere ->
            val count = sphereTaskCounts[sphere] ?: 0
            val baseScore = if (count > 0) {
                ((count.toFloat() / maxCount.toFloat()) * 65f + 35f).toInt().coerceIn(35, 100)
            } else {
                // Adaptive default so the polygon always looks aesthetic
                when (sphere) {
                    LifeSphere.CAREER -> 60 + (rate / 5).coerceAtMost(30)
                    LifeSphere.HEALTH -> 50 + (habits.size * 10).coerceAtMost(40)
                    LifeSphere.GROWTH -> 55 + (focusMinutes / 3).coerceAtMost(35)
                    LifeSphere.FINANCE -> 45
                    LifeSphere.RELATIONS -> 50
                    LifeSphere.REST -> 65
                }
            }
            LifeBalanceItem(sphere = sphere, scorePercent = baseScore, tasksCount = count)
        }

        // 2. Chronotype Distribution
        var morningCount = 0
        var dayCount = 0
        var eveningCount = 0
        var nightCount = 0

        tasks.forEach { task ->
            val hour = task.completedAt?.hour ?: task.dueDateTime?.hour ?: task.createdAt.hour
            when (hour) {
                in 6..11 -> morningCount++
                in 12..17 -> dayCount++
                in 18..22 -> eveningCount++
                else -> nightCount++
            }
        }

        val totalHourCount = (morningCount + dayCount + eveningCount + nightCount).coerceAtLeast(1)
        val mPercent = ((morningCount.toFloat() / totalHourCount) * 100).toInt().coerceAtLeast(10)
        val dPercent = ((dayCount.toFloat() / totalHourCount) * 100).toInt().coerceAtLeast(10)
        val ePercent = ((eveningCount.toFloat() / totalHourCount) * 100).toInt().coerceAtLeast(10)
        val nPercent = (100 - mPercent - dPercent - ePercent).coerceIn(5, 40)

        val (peakLabel, peakEmoji) = when {
            mPercent >= dPercent && mPercent >= ePercent && mPercent >= nPercent ->
                "Утренний Снайпер" to "🌅"
            dPercent >= mPercent && dPercent >= ePercent && dPercent >= nPercent ->
                "Дневной Двигатель" to "☀️"
            ePercent >= mPercent && ePercent >= dPercent && ePercent >= nPercent ->
                "Вечерний Стратег" to "🌆"
            else ->
                "Ночной Архитектор" to "🌙"
        }

        val chronotype = ChronotypeDistribution(
            morningPercent = mPercent,
            dayPercent = dPercent,
            eveningPercent = ePercent,
            nightPercent = nPercent,
            peakLabel = peakLabel,
            peakEmoji = peakEmoji
        )

        // 3. Rewind Stats
        val dominantSphere = lifeBalanceItems.maxByOrNull { it.scorePercent }?.sphere ?: LifeSphere.CAREER
        val bestHabit = habits.maxByOrNull { it.streakCount }

        val (badgeTitle, badgeDesc) = when {
            rate >= 75 ->
                "⚡ Неоновый Кибер-Стратег" to "Твоя продуктивность превысила 75%! Ты уверенно управляешь временем и ресурсами."
            focusMinutes >= 45 ->
                "🧘 Мастер Дзен-Фокуса" to "Более 45 минут чистой концентрации. Никакие уведомления не способны сбить тебя с курса."
            (bestHabit?.streakCount ?: 0) >= 3 ->
                "🔥 Хранитель Огня Дисциплины" to "Серия привычек растет каждый день. Дисциплина формирует твой будущий успех."
            else ->
                "🚀 Первопроходец N.ECO" to "Каждая закрытая задача — это кирпичик в фундаменте твоих больших целей."
        }

        val rewind = RewindStats(
            titleBadge = badgeTitle,
            titleDescription = badgeDesc,
            totalFocusMinutes = focusMinutes,
            completedTasksCount = completed,
            bestHabitTitle = bestHabit?.title,
            bestHabitStreak = bestHabit?.streakCount ?: 0,
            dominantSphere = dominantSphere,
            completionRate = rate
        )

        return ProductivityStats(
            completedTasks = completed,
            activeTasks = active,
            totalFocusMinutes = focusMinutes,
            totalHabits = habits.size,
            completionRate = rate,
            lifeBalance = lifeBalanceItems,
            chronotype = chronotype,
            rewindStats = rewind
        )
    }
}
