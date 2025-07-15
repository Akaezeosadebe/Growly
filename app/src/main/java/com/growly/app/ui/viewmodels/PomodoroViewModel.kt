package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.growly.app.data.dao.PomodoroDao
import com.growly.app.data.entities.PomodoroSession
import com.growly.app.data.entities.PomodoroSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class PomodoroViewModel(private val pomodoroDao: PomodoroDao) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()
    
    private var timerJob: Job? = null
    private var currentSession: PomodoroSession? = null
    
    val settings = pomodoroDao.getSettingsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = getDefaultSettings()
        )
    
    val todaySessions = flow {
        emit(pomodoroDao.getTodaySessions())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    init {
        loadSettings()
        loadTodayStats()
    }
    
    fun startTimer() {
        val currentSettings = settings.value ?: getDefaultSettings()
        
        if (_uiState.value.timerState == TimerState.PAUSED) {
            // Resume existing session
            resumeTimer()
        } else {
            // Start new session
            val isWorkSession = _uiState.value.currentSessionType == SessionType.WORK
            val duration = if (isWorkSession) {
                currentSettings.workDuration
            } else {
                if (_uiState.value.completedCycles % currentSettings.cyclesBeforeLongBreak == 0) {
                    currentSettings.longBreakDuration
                } else {
                    currentSettings.shortBreakDuration
                }
            }
            
            startNewSession(duration, isWorkSession)
        }
    }
    
    private fun startNewSession(durationMinutes: Int, isWorkSession: Boolean) {
        val now = Date()
        currentSession = PomodoroSession(
            workDuration = if (isWorkSession) durationMinutes else 0,
            breakDuration = if (!isWorkSession) durationMinutes else 0,
            completedCycles = 0,
            totalWorkTime = 0,
            totalBreakTime = 0,
            isCompleted = false,
            startedAt = now
        )
        
        _uiState.value = _uiState.value.copy(
            timerState = TimerState.RUNNING,
            timeRemainingSeconds = durationMinutes * 60,
            totalTimeSeconds = durationMinutes * 60,
            currentSessionType = if (isWorkSession) SessionType.WORK else SessionType.BREAK
        )
        
        startCountdown()
    }
    
    private fun resumeTimer() {
        _uiState.value = _uiState.value.copy(timerState = TimerState.RUNNING)
        startCountdown()
    }
    
    private fun startCountdown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeRemainingSeconds > 0 && _uiState.value.timerState == TimerState.RUNNING) {
                delay(1000)
                val newTime = _uiState.value.timeRemainingSeconds - 1
                _uiState.value = _uiState.value.copy(
                    timeRemainingSeconds = newTime,
                    progress = 1f - (newTime.toFloat() / _uiState.value.totalTimeSeconds.toFloat())
                )
            }
            
            if (_uiState.value.timeRemainingSeconds <= 0) {
                onTimerComplete()
            }
        }
    }
    
    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(timerState = TimerState.PAUSED)
    }
    
    fun resetTimer() {
        timerJob?.cancel()
        currentSession = null
        
        val currentSettings = settings.value ?: getDefaultSettings()
        _uiState.value = _uiState.value.copy(
            timerState = TimerState.IDLE,
            timeRemainingSeconds = currentSettings.workDuration * 60,
            totalTimeSeconds = currentSettings.workDuration * 60,
            progress = 0f,
            currentSessionType = SessionType.WORK,
            completedCycles = 0
        )
    }
    
    private fun onTimerComplete() {
        val currentSettings = settings.value ?: getDefaultSettings()
        
        // Save completed session
        currentSession?.let { session ->
            val completedSession = session.copy(
                isCompleted = true,
                completedAt = Date(),
                completedCycles = if (_uiState.value.currentSessionType == SessionType.WORK) 1 else 0,
                totalWorkTime = if (_uiState.value.currentSessionType == SessionType.WORK) session.workDuration else 0,
                totalBreakTime = if (_uiState.value.currentSessionType == SessionType.BREAK) session.breakDuration else 0
            )
            
            viewModelScope.launch {
                pomodoroDao.insertSession(completedSession)
                loadTodayStats()
            }
        }
        
        // Update UI state
        val newCompletedCycles = if (_uiState.value.currentSessionType == SessionType.WORK) {
            _uiState.value.completedCycles + 1
        } else {
            _uiState.value.completedCycles
        }
        
        val nextSessionType = if (_uiState.value.currentSessionType == SessionType.WORK) {
            SessionType.BREAK
        } else {
            SessionType.WORK
        }
        
        val nextDuration = if (nextSessionType == SessionType.WORK) {
            currentSettings.workDuration
        } else {
            if (newCompletedCycles % currentSettings.cyclesBeforeLongBreak == 0) {
                currentSettings.longBreakDuration
            } else {
                currentSettings.shortBreakDuration
            }
        }
        
        _uiState.value = _uiState.value.copy(
            timerState = TimerState.COMPLETED,
            completedCycles = newCompletedCycles,
            currentSessionType = nextSessionType,
            timeRemainingSeconds = nextDuration * 60,
            totalTimeSeconds = nextDuration * 60,
            progress = 0f,
            message = if (_uiState.value.currentSessionType == SessionType.WORK) {
                "Work session completed! Time for a break."
            } else {
                "Break time over! Ready for another work session?"
            }
        )
        
        // Auto-start next session if enabled
        if ((nextSessionType == SessionType.WORK && currentSettings.autoStartWork) ||
            (nextSessionType == SessionType.BREAK && currentSettings.autoStartBreaks)) {
            startTimer()
        }
    }
    
    fun updateSettings(newSettings: PomodoroSettings) {
        viewModelScope.launch {
            try {
                pomodoroDao.updateSettings(newSettings)
                _uiState.value = _uiState.value.copy(
                    message = "Settings updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update settings: ${e.message}"
                )
            }
        }
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            val existingSettings = pomodoroDao.getSettings()
            if (existingSettings == null) {
                // Insert default settings
                pomodoroDao.insertSettings(getDefaultSettings())
            }
        }
    }
    
    private fun loadTodayStats() {
        viewModelScope.launch {
            try {
                val completedSessionsToday = pomodoroDao.getCompletedSessionsToday()
                val totalWorkTimeToday = pomodoroDao.getTotalWorkTimeToday() ?: 0
                val completedSessionsThisWeek = pomodoroDao.getCompletedSessionsThisWeek()
                val totalWorkTimeThisWeek = pomodoroDao.getTotalWorkTimeThisWeek() ?: 0
                
                val stats = PomodoroStats(
                    sessionsToday = completedSessionsToday,
                    workTimeToday = totalWorkTimeToday,
                    sessionsThisWeek = completedSessionsThisWeek,
                    workTimeThisWeek = totalWorkTimeThisWeek
                )
                
                _uiState.value = _uiState.value.copy(stats = stats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load statistics: ${e.message}"
                )
            }
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun getDefaultSettings() = PomodoroSettings(
        workDuration = 25,
        shortBreakDuration = 5,
        longBreakDuration = 15,
        cyclesBeforeLongBreak = 4,
        autoStartBreaks = false,
        autoStartWork = false,
        soundEnabled = true,
        vibrationEnabled = true
    )
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

data class PomodoroUiState(
    val timerState: TimerState = TimerState.IDLE,
    val currentSessionType: SessionType = SessionType.WORK,
    val timeRemainingSeconds: Int = 25 * 60, // Default 25 minutes
    val totalTimeSeconds: Int = 25 * 60,
    val progress: Float = 0f,
    val completedCycles: Int = 0,
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val stats: PomodoroStats? = null
)

data class PomodoroStats(
    val sessionsToday: Int,
    val workTimeToday: Int, // in minutes
    val sessionsThisWeek: Int,
    val workTimeThisWeek: Int // in minutes
)

enum class TimerState {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED
}

enum class SessionType {
    WORK,
    BREAK
}

class PomodoroViewModelFactory(private val pomodoroDao: PomodoroDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PomodoroViewModel(pomodoroDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
