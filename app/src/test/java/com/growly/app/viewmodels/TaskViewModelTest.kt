package com.growly.app.viewmodels

import com.growly.app.data.dao.TaskDao
import com.growly.app.data.entities.Task
import com.growly.app.data.entities.TaskPriority
import com.growly.app.ui.viewmodels.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    @Mock
    private lateinit var taskDao: TaskDao

    private lateinit var viewModel: TaskViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Setup mock responses
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(emptyList()))
        whenever(taskDao.getPendingTasks()).thenReturn(flowOf(emptyList()))
        whenever(taskDao.getCompletedTasks()).thenReturn(flowOf(emptyList()))
        whenever(taskDao.getTodaysTasks()).thenReturn(flowOf(emptyList()))
        
        viewModel = TaskViewModel(taskDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addTask should insert task with correct properties`() = runTest {
        // Given
        val title = "Test Task"
        val description = "Test Description"
        val priority = TaskPriority.HIGH
        val category = "Work"

        // When
        viewModel.addTask(title, description, priority, category)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(taskDao).insertTask(
            org.mockito.kotlin.argThat { task ->
                task.title == title &&
                task.description == description &&
                task.priority == priority &&
                task.category == category &&
                !task.isCompleted
            }
        )
    }

    @Test
    fun `toggleTaskCompletion should mark incomplete task as completed`() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "Test Task",
            isCompleted = false,
            priority = TaskPriority.MEDIUM,
            createdAt = Date()
        )

        // When
        viewModel.toggleTaskCompletion(task)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(taskDao).markTaskCompleted(
            org.mockito.kotlin.eq(task.id),
            org.mockito.kotlin.any()
        )
    }

    @Test
    fun `toggleTaskCompletion should mark completed task as incomplete`() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "Test Task",
            isCompleted = true,
            priority = TaskPriority.MEDIUM,
            createdAt = Date(),
            completedAt = Date()
        )

        // When
        viewModel.toggleTaskCompletion(task)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(taskDao).markTaskIncomplete(task.id)
    }

    @Test
    fun `getTaskStats should calculate correct completion rate`() = runTest {
        // Given
        whenever(taskDao.getTotalTaskCount()).thenReturn(10)
        whenever(taskDao.getCompletedTaskCount()).thenReturn(7)
        whenever(taskDao.getPendingTaskCount()).thenReturn(3)
        whenever(taskDao.getTasksCompletedToday()).thenReturn(2)
        whenever(taskDao.getTasksCompletedThisWeek()).thenReturn(5)

        // When
        viewModel.getTaskStats()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val stats = viewModel.uiState.value.stats
        assert(stats != null)
        assert(stats!!.totalTasks == 10)
        assert(stats.completedTasks == 7)
        assert(stats.pendingTasks == 3)
        assert(stats.completionRate == 70f)
        assert(stats.tasksCompletedToday == 2)
        assert(stats.tasksCompletedThisWeek == 5)
    }

    @Test
    fun `deleteTask should call dao deleteTask`() = runTest {
        // Given
        val task = Task(
            id = 1,
            title = "Test Task",
            isCompleted = false,
            priority = TaskPriority.LOW,
            createdAt = Date()
        )

        // When
        viewModel.deleteTask(task)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(taskDao).deleteTask(task)
    }
}
