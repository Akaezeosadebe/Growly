package com.growly.app.data.converters

import androidx.room.TypeConverter
import com.growly.app.data.entities.*
import java.util.Date

class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }

    @TypeConverter
    fun fromJournalCategory(category: JournalCategory): String {
        return category.name
    }

    @TypeConverter
    fun toJournalCategory(category: String): JournalCategory {
        return JournalCategory.valueOf(category)
    }

    @TypeConverter
    fun fromTaskPriority(priority: TaskPriority): String {
        return priority.name
    }

    @TypeConverter
    fun toTaskPriority(priority: String): TaskPriority {
        return TaskPriority.valueOf(priority)
    }

    @TypeConverter
    fun fromHabitFrequency(frequency: HabitFrequency): String {
        return frequency.name
    }

    @TypeConverter
    fun toHabitFrequency(frequency: String): HabitFrequency {
        return HabitFrequency.valueOf(frequency)
    }

    @TypeConverter
    fun fromMoodLevel(mood: MoodLevel): String {
        return mood.name
    }

    @TypeConverter
    fun toMoodLevel(mood: String): MoodLevel {
        return MoodLevel.valueOf(mood)
    }
}
