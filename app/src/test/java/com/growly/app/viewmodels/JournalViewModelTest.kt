package com.growly.app.viewmodels

import com.growly.app.data.dao.JournalDao
import com.growly.app.data.entities.JournalCategory
import com.growly.app.data.entities.JournalEntry
import com.growly.app.ui.viewmodels.JournalViewModel
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
class JournalViewModelTest {

    @Mock
    private lateinit var journalDao: JournalDao

    private lateinit var viewModel: JournalViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        // Setup mock responses
        whenever(journalDao.getAllEntries()).thenReturn(flowOf(emptyList()))
        whenever(journalDao.getEntriesByCategory(JournalCategory.DAILY_REFLECTIONS))
            .thenReturn(flowOf(emptyList()))
        
        viewModel = JournalViewModel(journalDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveEntry should insert entry with correct word count`() = runTest {
        // Given
        val category = JournalCategory.DAILY_REFLECTIONS
        val title = "Test Entry"
        val content = "This is a test entry with multiple words"
        val expectedWordCount = 9

        // When
        viewModel.saveEntry(category, title, content)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(journalDao).insertEntry(
            org.mockito.kotlin.argThat { entry ->
                entry.category == category &&
                entry.title == title &&
                entry.content == content &&
                entry.wordCount == expectedWordCount
            }
        )
    }

    @Test
    fun `saveEntry with empty content should have zero word count`() = runTest {
        // Given
        val category = JournalCategory.FREE_WRITING
        val title = "Empty Entry"
        val content = ""

        // When
        viewModel.saveEntry(category, title, content)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(journalDao).insertEntry(
            org.mockito.kotlin.argThat { entry ->
                entry.wordCount == 0
            }
        )
    }

    @Test
    fun `updateEntry should update entry with new timestamp`() = runTest {
        // Given
        val originalEntry = JournalEntry(
            id = 1,
            category = JournalCategory.GRATITUDE_JOURNALING,
            title = "Original Title",
            content = "Original content",
            wordCount = 2,
            createdAt = Date(System.currentTimeMillis() - 10000),
            updatedAt = Date(System.currentTimeMillis() - 10000)
        )

        // When
        viewModel.updateEntry(originalEntry)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(journalDao).updateEntry(
            org.mockito.kotlin.argThat { entry ->
                entry.id == originalEntry.id &&
                entry.updatedAt.time > originalEntry.updatedAt.time
            }
        )
    }

    @Test
    fun `deleteEntry should call dao deleteEntry`() = runTest {
        // Given
        val entry = JournalEntry(
            id = 1,
            category = JournalCategory.GOAL_TRACKING,
            title = "Test",
            content = "Test content",
            wordCount = 2,
            createdAt = Date(),
            updatedAt = Date()
        )

        // When
        viewModel.deleteEntry(entry)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        verify(journalDao).deleteEntry(entry)
    }

    @Test
    fun `getJournalStats should calculate correct statistics`() = runTest {
        // Given
        whenever(journalDao.getTotalEntryCount()).thenReturn(10)
        whenever(journalDao.getTotalWordCount()).thenReturn(1000)
        whenever(journalDao.getEntriesThisWeek()).thenReturn(3)
        whenever(journalDao.getEntriesThisMonth()).thenReturn(8)

        // When
        viewModel.getJournalStats()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val stats = viewModel.uiState.value.stats
        assert(stats != null)
        assert(stats!!.totalEntries == 10)
        assert(stats.totalWords == 1000)
        assert(stats.averageWordsPerEntry == 100)
        assert(stats.entriesThisWeek == 3)
        assert(stats.entriesThisMonth == 8)
    }
}
