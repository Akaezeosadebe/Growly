package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.growly.app.data.dao.TaskDao
import com.growly.app.data.entities.Task
import com.growly.app.data.entities.TaskPriority
import com.growly.app.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(
    private val taskDao: TaskDao,
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()
    
    val allTasks = taskDao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val pendingTasks = taskDao.getPendingTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val completedTasks = taskDao.getCompletedTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val todaysTasks = taskDao.getTodaysTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun addTask(
        title: String,
        description: String = "",
        priority: TaskPriority = TaskPriority.MEDIUM,
        category: String = "General",
        dueDate: Date? = null,
        estimatedMinutes: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val task = Task(
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    dueDate = dueDate,
                    createdAt = Date(),
                    estimatedMinutes = estimatedMinutes
                )
                
                taskDao.insertTask(task)

                // Sync to Firebase
                syncTaskToFirebase(task)

                _uiState.value = _uiState.value.copy(
                    message = "Task added successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to add task: ${e.message}"
                )
            }
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskDao.updateTask(task)
                _uiState.value = _uiState.value.copy(
                    message = "Task updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update task: ${e.message}"
                )
            }
        }
    }
    
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            try {
                if (task.isCompleted) {
                    taskDao.markTaskIncomplete(task.id)
                } else {
                    taskDao.markTaskCompleted(task.id, Date())
                }
                _uiState.value = _uiState.value.copy(
                    message = if (task.isCompleted) "Task marked as incomplete" else "Task completed! 🎉"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update task: ${e.message}"
                )
            }
        }
    }
    
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                taskDao.deleteTask(task)
                _uiState.value = _uiState.value.copy(
                    message = "Task deleted successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete task: ${e.message}"
                )
            }
        }
    }
    
    fun getTasksByCategory(category: String): StateFlow<List<Task>> {
        return taskDao.getTasksByCategory(category)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    fun getTasksByPriority(priority: TaskPriority): StateFlow<List<Task>> {
        return taskDao.getTasksByPriority(priority)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    fun searchTasks(query: String): StateFlow<List<Task>> {
        return taskDao.searchTasks(query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    fun getTaskStats() {
        viewModelScope.launch {
            try {
                val totalTasks = taskDao.getTotalTaskCount()
                val completedTasks = taskDao.getCompletedTaskCount()
                val pendingTasks = taskDao.getPendingTaskCount()
                val tasksCompletedToday = taskDao.getTasksCompletedToday()
                val tasksCompletedThisWeek = taskDao.getTasksCompletedThisWeek()
                
                val completionRate = if (totalTasks > 0) {
                    (completedTasks.toFloat() / totalTasks.toFloat()) * 100
                } else 0f
                
                val stats = TaskStats(
                    totalTasks = totalTasks,
                    completedTasks = completedTasks,
                    pendingTasks = pendingTasks,
                    completionRate = completionRate,
                    tasksCompletedToday = tasksCompletedToday,
                    tasksCompletedThisWeek = tasksCompletedThisWeek
                )
                
                _uiState.value = _uiState.value.copy(stats = stats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load statistics: ${e.message}"
                )
            }
        }
    }
    
    fun getAllCategories() {
        viewModelScope.launch {
            try {
                val categories = taskDao.getAllCategories()
                _uiState.value = _uiState.value.copy(categories = categories)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load categories: ${e.message}"
                )
            }
        }
    }
    
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // Firebase sync methods
    fun syncWithFirebase() {
        viewModelScope.launch {
            if (firebaseRepository.getCurrentUser() == null) {
                _uiState.value = _uiState.value.copy(
                    isOffline = true,
                    syncMessage = "Sign in to sync tasks with cloud"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isSyncing = true, syncMessage = null)

            firebaseRepository.getTasks()
                .onSuccess { firebaseTasks ->
                    // Merge with local tasks
                    mergeFirebaseTasks(firebaseTasks)
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        isOffline = false,
                        syncMessage = "Synced ${firebaseTasks.size} tasks from cloud"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        isOffline = true,
                        error = "Sync failed: ${exception.message}"
                    )
                }
        }
    }

    private suspend fun mergeFirebaseTasks(firebaseTasks: List<Task>) {
        try {
            firebaseTasks.forEach { task ->
                taskDao.insertTask(task)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to merge tasks: ${e.message}"
            )
        }
    }

    private fun syncTaskToFirebase(task: Task) {
        viewModelScope.launch {
            if (firebaseRepository.getCurrentUser() == null) return@launch

            firebaseRepository.saveTask(task)
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to sync task: ${exception.message}"
                    )
                }
        }
    }
}

data class TaskUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val stats: TaskStats? = null,
    val categories: List<String> = emptyList(),
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val isSyncing: Boolean = false,
    val isOffline: Boolean = false,
    val syncMessage: String? = null
)

data class TaskStats(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val completionRate: Float,
    val tasksCompletedToday: Int,
    val tasksCompletedThisWeek: Int
)

enum class TaskFilter {
    ALL,
    PENDING,
    COMPLETED,
    TODAY,
    HIGH_PRIORITY
}

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
