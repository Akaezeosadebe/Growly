package com.growly.app.data.dao

import androidx.room.*
import com.growly.app.data.entities.UserPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun getUserPreferences(): UserPreferences?
    
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferencesFlow(): Flow<UserPreferences?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(preferences: UserPreferences)
    
    @Update
    suspend fun updateUserPreferences(preferences: UserPreferences)
    
    @Query("UPDATE user_preferences SET userName = :name WHERE id = 1")
    suspend fun updateUserName(name: String)
    
    @Query("UPDATE user_preferences SET userPhotoPath = :photoPath WHERE id = 1")
    suspend fun updateUserPhoto(photoPath: String)
    
    @Query("UPDATE user_preferences SET notificationsEnabled = :enabled WHERE id = 1")
    suspend fun updateNotificationsEnabled(enabled: Boolean)
    
    @Query("UPDATE user_preferences SET dailyReminderTime = :time WHERE id = 1")
    suspend fun updateDailyReminderTime(time: String)
    
    @Query("UPDATE user_preferences SET themePreference = :theme WHERE id = 1")
    suspend fun updateThemePreference(theme: String)
    
    @Query("UPDATE user_preferences SET writingFontSize = :fontSize WHERE id = 1")
    suspend fun updateWritingFontSize(fontSize: Int)
    
    @Query("UPDATE user_preferences SET writingFontFamily = :fontFamily WHERE id = 1")
    suspend fun updateWritingFontFamily(fontFamily: String)
    
    @Query("UPDATE user_preferences SET backgroundTexture = :texture WHERE id = 1")
    suspend fun updateBackgroundTexture(texture: String)
    
    @Query("UPDATE user_preferences SET ambientSoundEnabled = :enabled WHERE id = 1")
    suspend fun updateAmbientSoundEnabled(enabled: Boolean)
    
    @Query("UPDATE user_preferences SET ambientSoundType = :soundType WHERE id = 1")
    suspend fun updateAmbientSoundType(soundType: String)
    
    @Query("UPDATE user_preferences SET ambientSoundVolume = :volume WHERE id = 1")
    suspend fun updateAmbientSoundVolume(volume: Float)
    
    @Query("UPDATE user_preferences SET autoSaveEnabled = :enabled WHERE id = 1")
    suspend fun updateAutoSaveEnabled(enabled: Boolean)
    
    @Query("UPDATE user_preferences SET autoSaveInterval = :interval WHERE id = 1")
    suspend fun updateAutoSaveInterval(interval: Int)
    
    @Query("UPDATE user_preferences SET streakStartDate = :startDate WHERE id = 1")
    suspend fun updateStreakStartDate(startDate: String)
    
    @Query("UPDATE user_preferences SET onboardingCompleted = :completed WHERE id = 1")
    suspend fun updateOnboardingCompleted(completed: Boolean)
}
