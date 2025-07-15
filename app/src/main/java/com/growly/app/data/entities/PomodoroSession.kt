package com.growly.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "pomodoro_sessions")
data class PomodoroSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workDuration: Int, // in minutes
    val breakDuration: Int, // in minutes
    val completedCycles: Int,
    val totalWorkTime: Int, // in minutes
    val totalBreakTime: Int, // in minutes
    val isCompleted: Boolean,
    val startedAt: Date,
    val completedAt: Date? = null,
    val taskId: Long? = null, // Optional link to a task
    val notes: String = ""
)

@Entity(tableName = "pomodoro_settings")
data class PomodoroSettings(
    @PrimaryKey
    val id: Int = 1, // Single row table
    val workDuration: Int = 25, // Default 25 minutes
    val shortBreakDuration: Int = 5, // Default 5 minutes
    val longBreakDuration: Int = 15, // Default 15 minutes
    val cyclesBeforeLongBreak: Int = 4, // Default 4 cycles
    val autoStartBreaks: Boolean = false,
    val autoStartWork: Boolean = false,
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)

// Data class for pomodoro statistics
data class PomodoroStats(
    val totalSessions: Int,
    val totalWorkTime: Int, // in minutes
    val totalBreakTime: Int, // in minutes
    val averageSessionLength: Int, // in minutes
    val sessionsToday: Int,
    val sessionsThisWeek: Int,
    val longestStreak: Int,
    val currentStreak: Int
)
