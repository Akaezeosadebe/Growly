package com.growly.app.data.dao

import androidx.room.*
import com.growly.app.data.entities.Habit
import com.growly.app.data.entities.HabitCompletion
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface HabitDao {
    
    @Query("SELECT * FROM habits WHERE isActive = 1 ORDER BY createdAt DESC")
    fun getActiveHabits(): Flow<List<Habit>>
    
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>
    
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): Habit?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long
    
    @Update
    suspend fun updateHabit(habit: Habit)
    
    @Delete
    suspend fun deleteHabit(habit: Habit)
    
    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Long)
    
    // Habit Completions
    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId ORDER BY completedAt DESC")
    fun getCompletionsForHabit(habitId: Long): Flow<List<HabitCompletion>>
    
    @Query("""
        SELECT * FROM habit_completions 
        WHERE habitId = :habitId 
        AND DATE(completedAt/1000, 'unixepoch') = DATE(:date/1000, 'unixepoch')
    """)
    suspend fun getCompletionForDate(habitId: Long, date: Date): HabitCompletion?
    
    @Query("""
        SELECT * FROM habit_completions 
        WHERE habitId = :habitId 
        AND DATE(completedAt/1000, 'unixepoch') = DATE('now')
    """)
    suspend fun getTodayCompletion(habitId: Long): HabitCompletion?
    
    @Query("""
        SELECT COUNT(*) FROM habit_completions 
        WHERE habitId = :habitId 
        AND DATE(completedAt/1000, 'unixepoch') >= DATE('now', '-7 days')
    """)
    suspend fun getCompletionsThisWeek(habitId: Long): Int
    
    @Query("""
        SELECT COUNT(*) FROM habit_completions 
        WHERE habitId = :habitId 
        AND DATE(completedAt/1000, 'unixepoch') >= DATE('now', 'start of month')
    """)
    suspend fun getCompletionsThisMonth(habitId: Long): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletion): Long
    
    @Update
    suspend fun updateCompletion(completion: HabitCompletion)
    
    @Delete
    suspend fun deleteCompletion(completion: HabitCompletion)
    
    @Query("DELETE FROM habit_completions WHERE id = :id")
    suspend fun deleteCompletionById(id: Long)
    
    @Query("DELETE FROM habit_completions WHERE habitId = :habitId")
    suspend fun deleteAllCompletionsForHabit(habitId: Long)
}
