package com.growly.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mood: MoodLevel,
    val energy: Int, // 1-5 scale
    val stress: Int, // 1-5 scale
    val notes: String = "",
    val activities: List<String> = emptyList(), // What they did that day
    val createdAt: Date
)

enum class MoodLevel(val value: Int, val emoji: String, val description: String) {
    VERY_SAD(1, "😢", "Very Sad"),
    SAD(2, "😔", "Sad"),
    NEUTRAL(3, "😐", "Neutral"),
    HAPPY(4, "😊", "Happy"),
    VERY_HAPPY(5, "😄", "Very Happy")
}

// Data class for mood statistics
data class MoodStats(
    val averageMood: Float,
    val averageEnergy: Float,
    val averageStress: Float,
    val moodTrend: MoodTrend,
    val entriesThisWeek: Int,
    val entriesThisMonth: Int,
    val mostCommonMood: MoodLevel
)

enum class MoodTrend {
    IMPROVING,
    STABLE,
    DECLINING
}
