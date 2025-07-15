package com.growly.app.data.dao

import androidx.room.*
import com.growly.app.data.entities.JournalCategory
import com.growly.app.data.entities.JournalEntry
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface JournalDao {
    
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE category = :category ORDER BY createdAt DESC")
    fun getEntriesByCategory(category: JournalCategory): Flow<List<JournalEntry>>
    
    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): JournalEntry?
    
    @Query("SELECT * FROM journal_entries WHERE category = :category ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestEntryByCategory(category: JournalCategory): JournalEntry?
    
    @Query("SELECT * FROM journal_entries WHERE DATE(createdAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')")
    suspend fun getEntriesForDate(date: Date): List<JournalEntry>
    
    @Query("SELECT COUNT(*) FROM journal_entries")
    suspend fun getTotalEntryCount(): Int
    
    @Query("SELECT SUM(wordCount) FROM journal_entries")
    suspend fun getTotalWordCount(): Int
    
    @Query("SELECT COUNT(*) FROM journal_entries WHERE createdAt >= :startDate")
    suspend fun getEntryCountSince(startDate: Date): Int
    
    @Query("""
        SELECT COUNT(*) FROM journal_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', '-7 days')
    """)
    suspend fun getEntriesThisWeek(): Int
    
    @Query("""
        SELECT COUNT(*) FROM journal_entries 
        WHERE DATE(createdAt/1000, 'unixepoch') >= DATE('now', 'start of month')
    """)
    suspend fun getEntriesThisMonth(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry): Long
    
    @Update
    suspend fun updateEntry(entry: JournalEntry)
    
    @Delete
    suspend fun deleteEntry(entry: JournalEntry)
    
    @Query("DELETE FROM journal_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)
    
    @Query("SELECT * FROM journal_entries WHERE content LIKE '%' || :searchQuery || '%' OR title LIKE '%' || :searchQuery || '%'")
    fun searchEntries(searchQuery: String): Flow<List<JournalEntry>>
}
