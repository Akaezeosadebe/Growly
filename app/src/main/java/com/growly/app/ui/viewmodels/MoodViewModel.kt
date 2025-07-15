package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.growly.app.data.dao.MoodDao
import com.growly.app.data.entities.MoodEntry
import com.growly.app.data.entities.MoodLevel
import com.growly.app.data.entities.MoodTrend
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class MoodViewModel(private val moodDao: MoodDao) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MoodUiState())
    val uiState: StateFlow<MoodUiState> = _uiState.asStateFlow()
    
    val allMoodEntries = moodDao.getAllMoodEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    init {
        loadTodayMood()
        loadMoodStats()
    }
    
    fun addMoodEntry(
        mood: MoodLevel,
        energy: Int,
        stress: Int,
        notes: String = "",
        activities: List<String> = emptyList()
    ) {
        viewModelScope.launch {
            try {
                // Check if there's already an entry for today
                val existingEntry = moodDao.getTodayMoodEntry()
                
                if (existingEntry != null) {
                    // Update existing entry
                    val updatedEntry = existingEntry.copy(
                        mood = mood,
                        energy = energy,
                        stress = stress,
                        notes = notes,
                        activities = activities
                    )
                    moodDao.updateMoodEntry(updatedEntry)
                    _uiState.value = _uiState.value.copy(
                        message = "Mood updated successfully"
                    )
                } else {
                    // Create new entry
                    val moodEntry = MoodEntry(
                        mood = mood,
                        energy = energy,
                        stress = stress,
                        notes = notes,
                        activities = activities,
                        createdAt = Date()
                    )
                    moodDao.insertMoodEntry(moodEntry)
                    _uiState.value = _uiState.value.copy(
                        message = "Mood recorded successfully"
                    )
                }
                
                loadTodayMood()
                loadMoodStats()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to record mood: ${e.message}"
                )
            }
        }
    }
    
    fun updateMoodEntry(moodEntry: MoodEntry) {
        viewModelScope.launch {
            try {
                moodDao.updateMoodEntry(moodEntry)
                _uiState.value = _uiState.value.copy(
                    message = "Mood entry updated successfully"
                )
                loadTodayMood()
                loadMoodStats()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update mood entry: ${e.message}"
                )
            }
        }
    }
    
    fun deleteMoodEntry(moodEntry: MoodEntry) {
        viewModelScope.launch {
            try {
                moodDao.deleteMoodEntry(moodEntry)
                _uiState.value = _uiState.value.copy(
                    message = "Mood entry deleted successfully"
                )
                loadTodayMood()
                loadMoodStats()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete mood entry: ${e.message}"
                )
            }
        }
    }
    
    fun getMoodEntriesThisWeek(): StateFlow<List<MoodEntry>> {
        return flow {
            emit(moodDao.getMoodEntriesThisWeek())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }
    
    fun getMoodEntriesThisMonth(): StateFlow<List<MoodEntry>> {
        return flow {
            emit(moodDao.getMoodEntriesThisMonth())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }
    
    private fun loadTodayMood() {
        viewModelScope.launch {
            try {
                val todayMood = moodDao.getTodayMoodEntry()
                _uiState.value = _uiState.value.copy(todayMood = todayMood)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load today's mood: ${e.message}"
                )
            }
        }
    }
    
    private fun loadMoodStats() {
        viewModelScope.launch {
            try {
                val averageMood = moodDao.getAverageMoodLast30Days() ?: 3.0f
                val averageEnergy = moodDao.getAverageEnergyLast30Days() ?: 3.0f
                val averageStress = moodDao.getAverageStressLast30Days() ?: 3.0f
                val entriesThisWeek = moodDao.getMoodEntriesCountThisWeek()
                val entriesThisMonth = moodDao.getMoodEntriesCountThisMonth()
                
                // Calculate mood trend
                val moodTrend = calculateMoodTrend()
                val mostCommonMood = calculateMostCommonMood()
                
                val stats = MoodStatsData(
                    averageMood = averageMood,
                    averageEnergy = averageEnergy,
                    averageStress = averageStress,
                    moodTrend = moodTrend,
                    entriesThisWeek = entriesThisWeek,
                    entriesThisMonth = entriesThisMonth,
                    mostCommonMood = mostCommonMood
                )
                
                _uiState.value = _uiState.value.copy(stats = stats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load mood statistics: ${e.message}"
                )
            }
        }
    }
    
    private suspend fun calculateMoodTrend(): MoodTrend {
        // TODO: Implement proper trend calculation based on recent entries
        return MoodTrend.IMPROVING // Placeholder
    }
    
    private suspend fun calculateMostCommonMood(): MoodLevel {
        // TODO: Implement calculation of most common mood
        return MoodLevel.HAPPY // Placeholder
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class MoodUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val todayMood: MoodEntry? = null,
    val stats: MoodStatsData? = null
)

data class MoodStatsData(
    val averageMood: Float,
    val averageEnergy: Float,
    val averageStress: Float,
    val moodTrend: MoodTrend,
    val entriesThisWeek: Int,
    val entriesThisMonth: Int,
    val mostCommonMood: MoodLevel
)

class MoodViewModelFactory(private val moodDao: MoodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(moodDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
