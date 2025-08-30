package com.example.dependencyhilt.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dependencyhilt.data.model.User
import com.example.dependencyhilt.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing user-related UI state and business logic.
 * 
 * This ViewModel uses Hilt for dependency injection and LiveData for
 * reactive UI updates. It handles the communication between the UI
 * and the repository layer.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    
    /**
     * LiveData for UI state management.
     * The UI observes this to react to state changes.
     */
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState
    
    /**
     * LiveData for the list of users.
     * The UI observes this to display the user list.
     */
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    /**
     * Sealed class representing different UI states.
     */
    sealed class UiState {
        object Loading : UiState()
        data class Success(val users: List<User>) : UiState()
        data class Error(val message: String) : UiState()
    }
    
    /**
     * Load users from the repository.
     * 
     * This method updates the UI state and fetches users from the repository.
     * It uses coroutines to perform the network operation asynchronously.
     */
    fun loadUsers() {
        viewModelScope.launch {
            try {
                // Update UI state to loading
                _uiState.value = UiState.Loading
                
                // Fetch users from repository
                val userList = repository.getUsers()
                
                // Update UI state to success
                _uiState.value = UiState.Success(userList)
                _users.value = userList
                
            } catch (e: Exception) {
                // Update UI state to error
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    /**
     * Get a specific user by ID.
     * 
     * @param id The user ID
     */
    fun getUserById(id: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val user = repository.getUserById(id)
                _uiState.value = UiState.Success(listOf(user))
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to fetch user")
            }
        }
    }
    
    /**
     * Refresh the user list.
     * 
     * This method reloads the users from the repository.
     */
    fun refreshUsers() {
        loadUsers()
    }
}
