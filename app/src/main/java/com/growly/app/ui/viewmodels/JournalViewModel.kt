package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.growly.app.data.dao.JournalDao
import com.growly.app.data.entities.JournalCategory
import com.growly.app.data.entities.JournalEntry
import com.growly.app.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date

class JournalViewModel(
    private val journalDao: JournalDao,
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()
    
    val allEntries = journalDao.getAllEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun getEntriesByCategory(category: JournalCategory): StateFlow<List<JournalEntry>> {
        return journalDao.getEntriesByCategory(category)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    fun saveEntry(
        category: JournalCategory,
        title: String,
        content: String,
        mood: Int? = null
    ) {
        viewModelScope.launch {
            try {
                val wordCount = if (content.isBlank()) 0 else content.trim().split("\\s+".toRegex()).size
                val now = Date()
                
                val entry = JournalEntry(
                    category = category,
                    title = title,
                    content = content,
                    wordCount = wordCount,
                    createdAt = now,
                    updatedAt = now,
                    mood = mood
                )
                
                journalDao.insertEntry(entry)

                // Sync to Firebase
                syncEntryToFirebase(entry)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = "Entry saved successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to save entry: ${e.message}"
                )
            }
        }
    }
    
    fun updateEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                val updatedEntry = entry.copy(
                    updatedAt = Date(),
                    wordCount = if (entry.content.isBlank()) 0 else entry.content.trim().split("\\s+".toRegex()).size
                )
                journalDao.updateEntry(updatedEntry)
                _uiState.value = _uiState.value.copy(
                    message = "Entry updated successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to update entry: ${e.message}"
                )
            }
        }
    }
    
    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch {
            try {
                journalDao.deleteEntry(entry)
                _uiState.value = _uiState.value.copy(
                    message = "Entry deleted successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete entry: ${e.message}"
                )
            }
        }
    }
    
    fun searchEntries(query: String): StateFlow<List<JournalEntry>> {
        return journalDao.searchEntries(query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    fun getJournalStats() {
        viewModelScope.launch {
            try {
                val totalEntries = journalDao.getTotalEntryCount()
                val totalWords = journalDao.getTotalWordCount() ?: 0
                val entriesThisWeek = journalDao.getEntriesThisWeek()
                val entriesThisMonth = journalDao.getEntriesThisMonth()
                
                val stats = JournalStats(
                    totalEntries = totalEntries,
                    totalWords = totalWords,
                    entriesThisWeek = entriesThisWeek,
                    entriesThisMonth = entriesThisMonth,
                    averageWordsPerEntry = if (totalEntries > 0) totalWords / totalEntries else 0,
                    currentStreak = calculateCurrentStreak() // TODO: Implement streak calculation
                )
                
                _uiState.value = _uiState.value.copy(stats = stats)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load statistics: ${e.message}"
                )
            }
        }
    }
    
    private suspend fun calculateCurrentStreak(): Int {
        // TODO: Implement proper streak calculation
        return 12 // Placeholder
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
                    syncMessage = "Sign in to sync with cloud"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isSyncing = true, syncMessage = null)

            firebaseRepository.getJournalEntries()
                .onSuccess { firebaseEntries ->
                    // Merge with local entries
                    mergeFirebaseEntries(firebaseEntries)
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        isOffline = false,
                        syncMessage = "Synced ${firebaseEntries.size} entries from cloud"
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

    private suspend fun mergeFirebaseEntries(firebaseEntries: List<JournalEntry>) {
        try {
            firebaseEntries.forEach { entry ->
                journalDao.insertEntry(entry)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to merge entries: ${e.message}"
            )
        }
    }

    private fun syncEntryToFirebase(entry: JournalEntry) {
        viewModelScope.launch {
            if (firebaseRepository.getCurrentUser() == null) return@launch

            firebaseRepository.saveJournalEntry(entry)
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to sync entry: ${exception.message}"
                    )
                }
        }
    }
}

data class JournalUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,
    val stats: JournalStats? = null,
    val isSyncing: Boolean = false,
    val isOffline: Boolean = false,
    val syncMessage: String? = null
)

data class JournalStats(
    val totalEntries: Int,
    val totalWords: Int,
    val entriesThisWeek: Int,
    val entriesThisMonth: Int,
    val averageWordsPerEntry: Int,
    val currentStreak: Int
)

class JournalViewModelFactory(private val journalDao: JournalDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(journalDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
