package com.growly.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.growly.app.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class AuthViewModel(
    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        // Check if user is already signed in
        checkAuthState()
    }
    
    private fun checkAuthState() {
        val currentUser = firebaseRepository.getCurrentUser()
        _uiState.value = _uiState.value.copy(
            isSignedIn = currentUser != null
        )
    }
    
    fun signIn(email: String, password: String) {
        if (!isValidInput(email, password)) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            
            firebaseRepository.signInWithEmailAndPassword(email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignedIn = true,
                        successMessage = "Welcome back, ${user.email}!"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
        }
    }
    
    fun signUp(email: String, password: String, confirmPassword: String) {
        if (!isValidSignUpInput(email, password, confirmPassword)) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            
            firebaseRepository.createUserWithEmailAndPassword(email, password)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSignedIn = true,
                        successMessage = "Account created successfully! Welcome, ${user.email}!"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
        }
    }
    
    fun signOut() {
        firebaseRepository.signOut()
        _uiState.value = _uiState.value.copy(
            isSignedIn = false,
            successMessage = "Signed out successfully"
        )
    }
    
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
    
    private fun isValidInput(email: String, password: String): Boolean {
        when {
            email.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Email is required")
                return false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid email")
                return false
            }
            password.isBlank() -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password is required")
                return false
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
                return false
            }
        }
        return true
    }
    
    private fun isValidSignUpInput(email: String, password: String, confirmPassword: String): Boolean {
        if (!isValidInput(email, password)) return false
        
        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords don't match")
            return false
        }
        
        return true
    }
    
    private fun getErrorMessage(exception: Throwable): String {
        return when {
            exception.message?.contains("badly formatted") == true -> "Invalid email format"
            exception.message?.contains("password is invalid") == true -> "Invalid password"
            exception.message?.contains("no user record") == true -> "No account found with this email"
            exception.message?.contains("email address is already") == true -> "An account with this email already exists"
            exception.message?.contains("weak password") == true -> "Password is too weak"
            exception.message?.contains("network error") == true -> "Network error. Please check your connection"
            else -> exception.message ?: "Authentication failed. Please try again"
        }
    }
}
