package com.omniplan.app.ui.screens.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omniplan.app.core.model.Habit
import com.omniplan.app.domain.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitsViewModel(
    private val habitRepository: HabitRepository
) : ViewModel() {

    val habits: StateFlow<List<Habit>> = habitRepository.getAllHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleHabitCheckIn(habitId: Long, date: LocalDate = LocalDate.now()) {
        viewModelScope.launch {
            habitRepository.toggleHabitCheckIn(habitId, date)
        }
    }

    fun addHabit(title: String, description: String, targetDays: Int) {
        viewModelScope.launch {
            val habit = Habit(
                title = title.trim(),
                description = description.trim(),
                targetDaysPerWeek = targetDays
            )
            habitRepository.insertHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habit)
        }
    }
}
