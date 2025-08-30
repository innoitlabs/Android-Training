package com.example.architecturepatterns.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architecturepatterns.domain.model.Result
import com.example.architecturepatterns.domain.model.User
import com.example.architecturepatterns.domain.usecase.GetUserUseCase
import com.example.architecturepatterns.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing user data in the presentation layer.
 * 
 * This ViewModel handles the business logic for user-related operations
 * and exposes UI state through StateFlow.
 * 
 * @param getUserUseCase Use case for retrieving a single user
 * @param getUsersUseCase Use case for retrieving all users
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()
    
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()
    
    init {
        loadUsers()
    }
    
    /**
     * Loads all users from the data source.
     */
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            getUsersUseCase()
                .onSuccess { userList ->
                    _users.value = userList
                    _uiState.value = UserUiState.Success
                }
                .onError { exception ->
                    _uiState.value = UserUiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
    
    /**
     * Loads a specific user by ID.
     * 
     * @param id The unique identifier of the user to load
     */
    fun loadUser(id: Int) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            getUserUseCase(id)
                .onSuccess { user ->
                    _selectedUser.value = user
                    _uiState.value = UserUiState.Success
                }
                .onError { exception ->
                    _uiState.value = UserUiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
    
    /**
     * Refreshes the user data.
     */
    fun refresh() {
        loadUsers()
    }
    
    /**
     * Clears the selected user.
     */
    fun clearSelectedUser() {
        _selectedUser.value = null
    }
}

/**
 * UI state for user-related operations.
 */
sealed class UserUiState {
    object Loading : UserUiState()
    object Success : UserUiState()
    data class Error(val message: String) : UserUiState()
}
