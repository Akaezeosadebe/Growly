package com.growly.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.growly.app.data.converters.Converters
import com.growly.app.data.dao.*
import com.growly.app.data.entities.*

@Database(
    entities = [
        JournalEntry::class,
        Task::class,
        Habit::class,
        HabitCompletion::class,
        MoodEntry::class,
        PomodoroSession::class,
        PomodoroSettings::class,
        UserPreferences::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GrowlyDatabase : RoomDatabase() {
    
    abstract fun journalDao(): JournalDao
    abstract fun taskDao(): TaskDao
    abstract fun habitDao(): HabitDao
    abstract fun moodDao(): MoodDao
    abstract fun pomodoroDao(): PomodoroDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    
    companion object {
        @Volatile
        private var INSTANCE: GrowlyDatabase? = null
        
        fun getDatabase(context: Context): GrowlyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GrowlyDatabase::class.java,
                    "growly_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
