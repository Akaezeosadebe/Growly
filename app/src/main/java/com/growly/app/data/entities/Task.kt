package com.growly.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: String = "General",
    val dueDate: Date? = null,
    val createdAt: Date,
    val completedAt: Date? = null,
    val estimatedMinutes: Int? = null
)

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

// Data class for task statistics
data class TaskStats(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val completionRate: Float,
    val tasksCompletedToday: Int,
    val tasksCompletedThisWeek: Int
)
