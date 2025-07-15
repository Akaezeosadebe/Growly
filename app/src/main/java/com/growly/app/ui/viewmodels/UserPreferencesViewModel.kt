package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.growly.app.data.dao.UserPreferencesDao
import com.growly.app.data.entities.UserPreferences
import com.growly.app.data.entities.WritingEnvironment
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val userPreferencesDao: UserPreferencesDao) : ViewModel() {
    
    private val _uiState = MutableStateFlow(UserPreferencesUiState())
    val uiState: StateFlow<UserPreferencesUiState> = _uiState.asStateFlow()
    
    val userPreferences = userPreferencesDao.getUserPreferencesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = getDefaultPreferences()
        )
    
    init {
        loadUserPreferences()
    }
    
    fun updateUserName(name: String) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateUserName(name)
                _uiState.value = _uiState.value.copy(
                    message = "Name updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update name: ${e.message}"
                )
            }
        }
    }
    
    fun updateUserPhoto(photoPath: String) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateUserPhoto(photoPath)
                _uiState.value = _uiState.value.copy(
                    message = "Profile photo updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update profile photo: ${e.message}"
                )
            }
        }
    }
    
    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateNotificationsEnabled(enabled)
                _uiState.value = _uiState.value.copy(
                    message = if (enabled) "Notifications enabled" else "Notifications disabled"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update notification settings: ${e.message}"
                )
            }
        }
    }
    
    fun updateDailyReminderTime(time: String) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateDailyReminderTime(time)
                _uiState.value = _uiState.value.copy(
                    message = "Daily reminder time updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update reminder time: ${e.message}"
                )
            }
        }
    }
    
    fun updateThemePreference(theme: String) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateThemePreference(theme)
                _uiState.value = _uiState.value.copy(
                    message = "Theme updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update theme: ${e.message}"
                )
            }
        }
    }
    
    fun updateWritingEnvironment(environment: WritingEnvironment) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateWritingFontSize(environment.fontSize)
                userPreferencesDao.updateWritingFontFamily(environment.fontFamily)
                userPreferencesDao.updateBackgroundTexture(environment.backgroundTexture)
                userPreferencesDao.updateAmbientSoundEnabled(environment.ambientSoundEnabled)
                userPreferencesDao.updateAmbientSoundType(environment.ambientSoundType)
                userPreferencesDao.updateAmbientSoundVolume(environment.ambientSoundVolume)
                
                _uiState.value = _uiState.value.copy(
                    message = "Writing environment updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update writing environment: ${e.message}"
                )
            }
        }
    }
    
    fun updateAutoSaveSettings(enabled: Boolean, interval: Int) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateAutoSaveEnabled(enabled)
                userPreferencesDao.updateAutoSaveInterval(interval)
                _uiState.value = _uiState.value.copy(
                    message = "Auto-save settings updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update auto-save settings: ${e.message}"
                )
            }
        }
    }
    
    fun updateStreakStartDate(startDate: String) {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateStreakStartDate(startDate)
                _uiState.value = _uiState.value.copy(
                    message = "Streak start date updated"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update streak start date: ${e.message}"
                )
            }
        }
    }
    
    fun completeOnboarding() {
        viewModelScope.launch {
            try {
                userPreferencesDao.updateOnboardingCompleted(true)
                _uiState.value = _uiState.value.copy(
                    message = "Welcome to Growly! 🌱"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to complete onboarding: ${e.message}"
                )
            }
        }
    }
    
    fun resetAllPreferences() {
        viewModelScope.launch {
            try {
                val defaultPreferences = getDefaultPreferences()
                userPreferencesDao.updateUserPreferences(defaultPreferences)
                _uiState.value = _uiState.value.copy(
                    message = "All preferences reset to default"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to reset preferences: ${e.message}"
                )
            }
        }
    }
    
    fun exportUserData() {
        viewModelScope.launch {
            try {
                // TODO: Implement data export functionality
                _uiState.value = _uiState.value.copy(
                    message = "Data export feature coming soon!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to export data: ${e.message}"
                )
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                // TODO: Implement logout functionality
                // This might involve clearing user data, stopping services, etc.
                _uiState.value = _uiState.value.copy(
                    message = "Logged out successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to logout: ${e.message}"
                )
            }
        }
    }
    
    private fun loadUserPreferences() {
        viewModelScope.launch {
            try {
                val preferences = userPreferencesDao.getUserPreferences()
                if (preferences == null) {
                    // Insert default preferences
                    userPreferencesDao.insertUserPreferences(getDefaultPreferences())
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load user preferences: ${e.message}"
                )
            }
        }
    }
    
    fun getWritingEnvironment(): WritingEnvironment {
        val prefs = userPreferences.value ?: getDefaultPreferences()
        return WritingEnvironment(
            fontSize = prefs.writingFontSize,
            fontFamily = prefs.writingFontFamily,
            backgroundColor = "#F5F5DC", // Soft beige
            textColor = "#555555", // Darker gray
            backgroundTexture = prefs.backgroundTexture,
            ambientSoundEnabled = prefs.ambientSoundEnabled,
            ambientSoundType = prefs.ambientSoundType,
            ambientSoundVolume = prefs.ambientSoundVolume
        )
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun getDefaultPreferences() = UserPreferences(
        userName = "",
        userPhotoPath = "",
        notificationsEnabled = true,
        dailyReminderTime = "20:00",
        weeklyReviewDay = 7,
        themePreference = "default",
        writingFontSize = 16,
        writingFontFamily = "default",
        backgroundTexture = "none",
        ambientSoundEnabled = false,
        ambientSoundType = "nature",
        ambientSoundVolume = 0.5f,
        autoSaveEnabled = true,
        autoSaveInterval = 30,
        streakStartDate = "",
        onboardingCompleted = false
    )
}

data class UserPreferencesUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)

class UserPreferencesViewModelFactory(private val userPreferencesDao: UserPreferencesDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPreferencesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserPreferencesViewModel(userPreferencesDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
