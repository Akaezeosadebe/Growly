package com.growly.app.data.dao

import androidx.room.*
import com.growly.app.data.entities.Task
import com.growly.app.data.entities.TaskPriority
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TaskDao {
    
    @Query("SELECT * FROM tasks ORDER BY priority DESC, createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY priority DESC, createdAt DESC")
    fun getPendingTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedTasks(): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Long): Task?
    
    @Query("SELECT * FROM tasks WHERE category = :category ORDER BY priority DESC, createdAt DESC")
    fun getTasksByCategory(category: String): Flow<List<Task>>
    
    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY createdAt DESC")
    fun getTasksByPriority(priority: TaskPriority): Flow<List<Task>>
    
    @Query("""
        SELECT * FROM tasks 
        WHERE isCompleted = 0 
        AND (dueDate IS NULL OR DATE(dueDate/1000, 'unixepoch') = DATE('now'))
        ORDER BY priority DESC, createdAt DESC
    """)
    fun getTodaysTasks(): Flow<List<Task>>
    
    @Query("""
        SELECT COUNT(*) FROM tasks 
        WHERE isCompleted = 1 
        AND DATE(completedAt/1000, 'unixepoch') = DATE('now')
    """)
    suspend fun getTasksCompletedToday(): Int
    
    @Query("""
        SELECT COUNT(*) FROM tasks 
        WHERE isCompleted = 1 
        AND DATE(completedAt/1000, 'unixepoch') >= DATE('now', '-7 days')
    """)
    suspend fun getTasksCompletedThisWeek(): Int
    
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTotalTaskCount(): Int
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1")
    suspend fun getCompletedTaskCount(): Int
    
    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    suspend fun getPendingTaskCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long
    
    @Update
    suspend fun updateTask(task: Task)
    
    @Delete
    suspend fun deleteTask(task: Task)
    
    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Long)
    
    @Query("UPDATE tasks SET isCompleted = 1, completedAt = :completedAt WHERE id = :id")
    suspend fun markTaskCompleted(id: Long, completedAt: Date)
    
    @Query("UPDATE tasks SET isCompleted = 0, completedAt = NULL WHERE id = :id")
    suspend fun markTaskIncomplete(id: Long)
    
    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    fun searchTasks(searchQuery: String): Flow<List<Task>>

    @Query("SELECT DISTINCT category FROM tasks WHERE category != '' ORDER BY category")
    suspend fun getAllCategories(): List<String>
}
