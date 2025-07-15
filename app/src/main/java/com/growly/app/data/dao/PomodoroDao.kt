package com.growly.app.data.dao

import androidx.room.*
import com.growly.app.data.entities.PomodoroSession
import com.growly.app.data.entities.PomodoroSettings
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PomodoroDao {
    
    // Pomodoro Sessions
    @Query("SELECT * FROM pomodoro_sessions ORDER BY startedAt DESC")
    fun getAllSessions(): Flow<List<PomodoroSession>>
    
    @Query("SELECT * FROM pomodoro_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): PomodoroSession?
    
    @Query("""
        SELECT * FROM pomodoro_sessions 
        WHERE DATE(startedAt/1000, 'unixepoch') = DATE('now')
        ORDER BY startedAt DESC
    """)
    suspend fun getTodaySessions(): List<PomodoroSession>
    
    @Query("""
        SELECT * FROM pomodoro_sessions 
        WHERE DATE(startedAt/1000, 'unixepoch') >= DATE('now', '-7 days')
        ORDER BY startedAt DESC
    """)
    suspend fun getSessionsThisWeek(): List<PomodoroSession>
    
    @Query("""
        SELECT COUNT(*) FROM pomodoro_sessions 
        WHERE isCompleted = 1 
        AND DATE(startedAt/1000, 'unixepoch') = DATE('now')
    """)
    suspend fun getCompletedSessionsToday(): Int
    
    @Query("""
        SELECT COUNT(*) FROM pomodoro_sessions 
        WHERE isCompleted = 1 
        AND DATE(startedAt/1000, 'unixepoch') >= DATE('now', '-7 days')
    """)
    suspend fun getCompletedSessionsThisWeek(): Int
    
    @Query("""
        SELECT SUM(totalWorkTime) FROM pomodoro_sessions 
        WHERE isCompleted = 1 
        AND DATE(startedAt/1000, 'unixepoch') = DATE('now')
    """)
    suspend fun getTotalWorkTimeToday(): Int?
    
    @Query("""
        SELECT SUM(totalWorkTime) FROM pomodoro_sessions 
        WHERE isCompleted = 1 
        AND DATE(startedAt/1000, 'unixepoch') >= DATE('now', '-7 days')
    """)
    suspend fun getTotalWorkTimeThisWeek(): Int?
    
    @Query("SELECT COUNT(*) FROM pomodoro_sessions WHERE isCompleted = 1")
    suspend fun getTotalCompletedSessions(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroSession): Long
    
    @Update
    suspend fun updateSession(session: PomodoroSession)
    
    @Delete
    suspend fun deleteSession(session: PomodoroSession)
    
    @Query("DELETE FROM pomodoro_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)
    
    // Pomodoro Settings
    @Query("SELECT * FROM pomodoro_settings WHERE id = 1")
    suspend fun getSettings(): PomodoroSettings?
    
    @Query("SELECT * FROM pomodoro_settings WHERE id = 1")
    fun getSettingsFlow(): Flow<PomodoroSettings?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: PomodoroSettings)
    
    @Update
    suspend fun updateSettings(settings: PomodoroSettings)
}
