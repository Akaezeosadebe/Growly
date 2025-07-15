package com.growly.app.data.dao

import androidx.room.*
import com.growly.app.data.entities.MoodEntry
import com.growly.app.data.entities.MoodLevel
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MoodDao {
    
    @Query("SELECT * FROM mood_entries ORDER BY createdAt DESC")
    fun getAllMoodEntries(): Flow<List<MoodEntry>>
    
    @Query("SELECT * FROM mood_entries WHERE id = :id")
    suspend fun getMoodEntryById(id: Long): MoodEntry?
    
    @Query("""
        SELECT * FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')
        LIMIT 1
    """)
    suspend fun getMoodEntryForDate(date: Date): MoodEntry?
    
    @Query("""
        SELECT * FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') = DATE('now')
        LIMIT 1
    """)
    suspend fun getTodayMoodEntry(): MoodEntry?
    
    @Query("""
        SELECT * FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', '-7 days')
        ORDER BY createdAt DESC
    """)
    suspend fun getMoodEntriesThisWeek(): List<MoodEntry>
    
    @Query("""
        SELECT * FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', 'start of month')
        ORDER BY createdAt DESC
    """)
    suspend fun getMoodEntriesThisMonth(): List<MoodEntry>
    
    @Query("""
        SELECT AVG(CASE mood 
            WHEN 'VERY_SAD' THEN 1 
            WHEN 'SAD' THEN 2 
            WHEN 'NEUTRAL' THEN 3 
            WHEN 'HAPPY' THEN 4 
            WHEN 'VERY_HAPPY' THEN 5 
            ELSE 3 END) 
        FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', '-30 days')
    """)
    suspend fun getAverageMoodLast30Days(): Float?
    
    @Query("""
        SELECT AVG(energy) FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', '-30 days')
    """)
    suspend fun getAverageEnergyLast30Days(): Float?
    
    @Query("""
        SELECT AVG(stress) FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', '-30 days')
    """)
    suspend fun getAverageStressLast30Days(): Float?
    
    @Query("""
        SELECT COUNT(*) FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', '-7 days')
    """)
    suspend fun getMoodEntriesCountThisWeek(): Int
    
    @Query("""
        SELECT COUNT(*) FROM mood_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', 'start of month')
    """)
    suspend fun getMoodEntriesCountThisMonth(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodEntry(moodEntry: MoodEntry): Long
    
    @Update
    suspend fun updateMoodEntry(moodEntry: MoodEntry)
    
    @Delete
    suspend fun deleteMoodEntry(moodEntry: MoodEntry)
    
    @Query("DELETE FROM mood_entries WHERE id = :id")
    suspend fun deleteMoodEntryById(id: Long)
}
