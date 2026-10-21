package com.omniplan.app.ui.screens.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omniplan.app.core.model.PomodoroSession
import com.omniplan.app.core.model.SessionType
import com.omniplan.app.domain.repository.CalendarRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AmbientSound(val title: String) {
    NONE("Без звука"),
    RAIN("Шум дождя"),
    FIRE("Уютный костёр"),
    WHITE_NOISE("Белый шум")
}

class FocusViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    val currentSessionType = MutableStateFlow(SessionType.WORK)
    val isRunning = MutableStateFlow(false)
    val timeLeftSeconds = MutableStateFlow(25 * 60)
    val totalTimeSeconds = MutableStateFlow(25 * 60)
    val selectedAmbientSound = MutableStateFlow(AmbientSound.NONE)

    val todayFocusMinutes: StateFlow<Int> = calendarRepository.getTodayFocusMinutes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todaySessions: StateFlow<List<PomodoroSession>> = calendarRepository.getTodayPomodoroSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var timerJob: Job? = null

    fun setSessionType(type: SessionType) {
        if (isRunning.value) pauseTimer()
        currentSessionType.value = type
        val minutes = when (type) {
            SessionType.WORK -> 25
            SessionType.SHORT_BREAK -> 5
            SessionType.LONG_BREAK -> 15
        }
        totalTimeSeconds.value = minutes * 60
        timeLeftSeconds.value = minutes * 60
    }

    fun startTimer() {
        if (isRunning.value) return
        isRunning.value = true
        timerJob = viewModelScope.launch {
            while (isRunning.value && timeLeftSeconds.value > 0) {
                delay(1000L)
                timeLeftSeconds.value -= 1
            }
            if (timeLeftSeconds.value <= 0) {
                onSessionCompleted()
            }
        }
    }

    fun pauseTimer() {
        isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        timeLeftSeconds.value = totalTimeSeconds.value
    }

    fun setAmbientSound(sound: AmbientSound) {
        selectedAmbientSound.value = sound
    }

    private fun onSessionCompleted() {
        pauseTimer()
        viewModelScope.launch {
            val minutes = totalTimeSeconds.value / 60
            val session = PomodoroSession(
                durationMinutes = minutes,
                type = currentSessionType.value
            )
            calendarRepository.insertPomodoroSession(session)
            // Auto switch to break if it was work
            if (currentSessionType.value == SessionType.WORK) {
                setSessionType(SessionType.SHORT_BREAK)
            } else {
                setSessionType(SessionType.WORK)
            }
        }
    }
}
