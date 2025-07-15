package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.growly.app.data.dao.HabitDao
import com.growly.app.data.entities.Habit
import com.growly.app.data.entities.HabitCompletion
import com.growly.app.data.entities.HabitFrequency
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class HabitViewModel(private val habitDao: HabitDao) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()
    
    val activeHabits = habitDao.getActiveHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val allHabits = habitDao.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun addHabit(
        name: String,
        description: String = "",
        color: String = "#87CEEB",
        icon: String = "default",
        targetFrequency: HabitFrequency = HabitFrequency.DAILY,
        targetCount: Int = 1,
        category: String = "General"
    ) {
        viewModelScope.launch {
            try {
                val habit = Habit(
                    name = name,
                    description = description,
                    color = color,
                    icon = icon,
                    targetFrequency = targetFrequency,
                    targetCount = targetCount,
                    createdAt = Date(),
                    category = category
                )
                
                habitDao.insertHabit(habit)
                _uiState.value = _uiState.value.copy(
                    message = "Habit added successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to add habit: ${e.message}"
                )
            }
        }
    }
    
    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                habitDao.updateHabit(habit)
                _uiState.value = _uiState.value.copy(
                    message = "Habit updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update habit: ${e.message}"
                )
            }
        }
    }
    
    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            try {
                habitDao.deleteHabit(habit)
                _uiState.value = _uiState.value.copy(
                    message = "Habit deleted successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete habit: ${e.message}"
                )
            }
        }
    }
    
    fun completeHabit(habitId: Long, count: Int = 1, notes: String = "") {
        viewModelScope.launch {
            try {
                // Check if already completed today
                val existingCompletion = habitDao.getTodayCompletion(habitId)
                
                if (existingCompletion != null) {
                    // Update existing completion
                    val updatedCompletion = existingCompletion.copy(
                        count = existingCompletion.count + count,
                        notes = if (notes.isNotEmpty()) notes else existingCompletion.notes
                    )
                    habitDao.updateCompletion(updatedCompletion)
                } else {
                    // Create new completion
                    val completion = HabitCompletion(
                        habitId = habitId,
                        completedAt = Date(),
                        count = count,
                        notes = notes
                    )
                    habitDao.insertCompletion(completion)
                }
                
                _uiState.value = _uiState.value.copy(
                    message = "Habit completed! 🎉"
                )
                
                loadTodayCompletions()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to complete habit: ${e.message}"
                )
            }
        }
    }
    
    fun uncompleteHabit(habitId: Long) {
        viewModelScope.launch {
            try {
                val todayCompletion = habitDao.getTodayCompletion(habitId)
                todayCompletion?.let {
                    if (it.count > 1) {
                        // Decrease count
                        val updatedCompletion = it.copy(count = it.count - 1)
                        habitDao.updateCompletion(updatedCompletion)
                    } else {
                        // Remove completion
                        habitDao.deleteCompletion(it)
                    }
                }
                
                _uiState.value = _uiState.value.copy(
                    message = "Habit completion removed"
                )
                
                loadTodayCompletions()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to uncomplete habit: ${e.message}"
                )
            }
        }
    }
    
    fun getCompletionsForHabit(habitId: Long): StateFlow<List<HabitCompletion>> {
        return habitDao.getCompletionsForHabit(habitId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    fun loadTodayCompletions() {
        viewModelScope.launch {
            try {
                val habits = activeHabits.value
                val completions = mutableMapOf<Long, HabitCompletion>()
                
                habits.forEach { habit ->
                    val completion = habitDao.getTodayCompletion(habit.id)
                    if (completion != null) {
                        completions[habit.id] = completion
                    }
                }
                
                _uiState.value = _uiState.value.copy(todayCompletions = completions)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load today's completions: ${e.message}"
                )
            }
        }
    }
    
    fun getHabitStats(habitId: Long) {
        viewModelScope.launch {
            try {
                val completionsThisWeek = habitDao.getCompletionsThisWeek(habitId)
                val completionsThisMonth = habitDao.getCompletionsThisMonth(habitId)
                
                // TODO: Calculate streak and other statistics
                val stats = HabitStatsData(
                    habitId = habitId,
                    currentStreak = calculateCurrentStreak(habitId),
                    longestStreak = calculateLongestStreak(habitId),
                    completionRate = calculateCompletionRate(habitId),
                    completionsThisWeek = completionsThisWeek,
                    completionsThisMonth = completionsThisMonth
                )
                
                val currentStats = _uiState.value.habitStats.toMutableMap()
                currentStats[habitId] = stats
                _uiState.value = _uiState.value.copy(habitStats = currentStats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load habit statistics: ${e.message}"
                )
            }
        }
    }
    
    private suspend fun calculateCurrentStreak(habitId: Long): Int {
        // TODO: Implement proper streak calculation
        return 5 // Placeholder
    }
    
    private suspend fun calculateLongestStreak(habitId: Long): Int {
        // TODO: Implement proper streak calculation
        return 12 // Placeholder
    }
    
    private suspend fun calculateCompletionRate(habitId: Long): Float {
        // TODO: Implement proper completion rate calculation
        return 0.85f // Placeholder
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class HabitUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val todayCompletions: Map<Long, HabitCompletion> = emptyMap(),
    val habitStats: Map<Long, HabitStatsData> = emptyMap()
)

data class HabitStatsData(
    val habitId: Long,
    val currentStreak: Int,
    val longestStreak: Int,
    val completionRate: Float,
    val completionsThisWeek: Int,
    val completionsThisMonth: Int
)

class HabitViewModelFactory(private val habitDao: HabitDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitViewModel(habitDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
