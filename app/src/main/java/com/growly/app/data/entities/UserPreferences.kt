package com.growly.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey
    val id: Int = 1, // Single row table
    val userName: String = "",
    val userPhotoPath: String = "",
    val notificationsEnabled: Boolean = true,
    val dailyReminderTime: String = "20:00", // Format: HH:mm
    val weeklyReviewDay: Int = 7, // 1-7, Sunday = 7
    val themePreference: String = "default",
    val writingFontSize: Int = 16,
    val writingFontFamily: String = "default",
    val backgroundTexture: String = "none",
    val ambientSoundEnabled: Boolean = false,
    val ambientSoundType: String = "nature",
    val ambientSoundVolume: Float = 0.5f,
    val autoSaveEnabled: Boolean = true,
    val autoSaveInterval: Int = 30, // seconds
    val streakStartDate: String = "", // ISO date string
    val onboardingCompleted: Boolean = false
)

// Data class for writing environment settings
data class WritingEnvironment(
    val fontSize: Int,
    val fontFamily: String,
    val backgroundColor: String,
    val textColor: String,
    val backgroundTexture: String,
    val ambientSoundEnabled: Boolean,
    val ambientSoundType: String,
    val ambientSoundVolume: Float
)
