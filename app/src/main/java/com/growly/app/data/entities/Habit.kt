package com.growly.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val color: String = "#87CEEB", // Default to sky blue
    val icon: String = "default", // Icon identifier
    val targetFrequency: HabitFrequency,
    val targetCount: Int = 1, // How many times per frequency period
    val isActive: Boolean = true,
    val createdAt: Date,
    val category: String = "General"
)

@Entity(tableName = "habit_completions")
data class HabitCompletion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val completedAt: Date,
    val count: Int = 1, // How many times completed on this date
    val notes: String = ""
)

enum class HabitFrequency {
    DAILY,
    WEEKLY,
    MONTHLY
}

// Data class for habit statistics
data class HabitStats(
    val habitId: Long,
    val currentStreak: Int,
    val longestStreak: Int,
    val completionRate: Float,
    val totalCompletions: Int,
    val completionsThisWeek: Int,
    val completionsThisMonth: Int
)
