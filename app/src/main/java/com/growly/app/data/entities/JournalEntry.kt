package com.growly.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: JournalCategory,
    val title: String = "",
    val content: String,
    val wordCount: Int = 0,
    val createdAt: Date,
    val updatedAt: Date,
    val mood: Int? = null, // 1-5 scale, optional
    val tags: List<String> = emptyList()
)

enum class JournalCategory {
    DAILY_REFLECTIONS,
    GRATITUDE_JOURNALING,
    GOAL_TRACKING,
    FREE_WRITING,
    CUSTOM_PROMPTS
}

// Data class for journal statistics
data class JournalStats(
    val totalEntries: Int,
    val currentStreak: Int,
    val totalWords: Int,
    val averageWordsPerEntry: Int,
    val entriesThisWeek: Int,
    val entriesThisMonth: Int
)
