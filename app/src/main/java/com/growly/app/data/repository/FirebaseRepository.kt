package com.growly.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.growly.app.data.entities.JournalEntry
import com.growly.app.data.entities.Task
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor() {
    
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    
    // Authentication
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    
    suspend fun signInWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun signOut() {
        auth.signOut()
    }
    
    // Firestore - Journal Entries
    suspend fun saveJournalEntry(entry: JournalEntry): Result<Unit> {
        return try {
            val userId = getCurrentUser()?.uid ?: return Result.failure(Exception("User not authenticated"))
            
            firestore.collection("users")
                .document(userId)
                .collection("journal_entries")
                .document(entry.id.toString())
                .set(entry)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getJournalEntries(): Result<List<JournalEntry>> {
        return try {
            val userId = getCurrentUser()?.uid ?: return Result.failure(Exception("User not authenticated"))
            
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("journal_entries")
                .get()
                .await()
            
            val entries = snapshot.documents.mapNotNull { doc ->
                doc.toObject(JournalEntry::class.java)
            }
            
            Result.success(entries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Firestore - Tasks
    suspend fun saveTask(task: Task): Result<Unit> {
        return try {
            val userId = getCurrentUser()?.uid ?: return Result.failure(Exception("User not authenticated"))
            
            firestore.collection("users")
                .document(userId)
                .collection("tasks")
                .document(task.id.toString())
                .set(task)
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTasks(): Result<List<Task>> {
        return try {
            val userId = getCurrentUser()?.uid ?: return Result.failure(Exception("User not authenticated"))
            
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("tasks")
                .get()
                .await()
            
            val tasks = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Task::class.java)
            }
            
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteTask(taskId: Long): Result<Unit> {
        return try {
            val userId = getCurrentUser()?.uid ?: return Result.failure(Exception("User not authenticated"))
            
            firestore.collection("users")
                .document(userId)
                .collection("tasks")
                .document(taskId.toString())
                .delete()
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
