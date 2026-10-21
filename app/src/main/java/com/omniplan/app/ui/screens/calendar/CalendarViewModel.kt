package com.omniplan.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omniplan.app.core.model.Task
import com.omniplan.app.core.model.TimeBlock
import com.omniplan.app.domain.repository.CalendarRepository
import com.omniplan.app.domain.repository.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

enum class CalendarTab {
    DAY_TIMELINE,
    MONTH_VIEW
}

class CalendarViewModel(
    private val calendarRepository: CalendarRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    val selectedDate = MutableStateFlow(LocalDate.now())
    val selectedTab = MutableStateFlow(CalendarTab.DAY_TIMELINE)

    @OptIn(ExperimentalCoroutinesApi::class)
    val timeBlocks: StateFlow<List<TimeBlock>> = selectedDate
        .flatMapLatest { date -> calendarRepository.getTimeBlocksForDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val dayTasks: StateFlow<List<Task>> = selectedDate
        .flatMapLatest { date -> taskRepository.getTodayTasks(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun setSelectedTab(tab: CalendarTab) {
        selectedTab.value = tab
    }

    fun addTimeBlock(title: String, startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        viewModelScope.launch {
            val date = selectedDate.value
            val startTime = LocalDateTime.of(date.year, date.month, date.dayOfMonth, startHour, startMinute)
            val endTime = LocalDateTime.of(date.year, date.month, date.dayOfMonth, endHour, endMinute)

            val block = TimeBlock(
                title = title,
                startTime = startTime,
                endTime = endTime
            )
            calendarRepository.insertTimeBlock(block)
        }
    }

    fun deleteTimeBlock(block: TimeBlock) {
        viewModelScope.launch {
            calendarRepository.deleteTimeBlock(block)
        }
    }
}
