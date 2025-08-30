package com.example.mvvmarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<UiState<List<User>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<User>>> = _uiState
    
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery
    
    init {
        loadUsers()
    }
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            repository.getUsers()
                .onSuccess { userList ->
                    _users.value = userList
                    _uiState.value = UiState.Success(userList)
                }
                .onFailure { exception ->
                    val errorMessage = when (exception) {
                        is IOException -> "Network error. Please check your connection."
                        is HttpException -> "Server error. Please try again later."
                        else -> "An unexpected error occurred: ${exception.message}"
                    }
                    _uiState.value = UiState.Error(errorMessage)
                }
        }
    }
    
    fun refreshUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            repository.refreshUsers()
                .onSuccess { userList ->
                    _users.value = userList
                    _uiState.value = UiState.Success(userList)
                }
                .onFailure { exception ->
                    val errorMessage = when (exception) {
                        is IOException -> "Network error. Please check your connection."
                        is HttpException -> "Server error. Please try again later."
                        else -> "An unexpected error occurred: ${exception.message}"
                    }
                    _uiState.value = UiState.Error(errorMessage)
                }
        }
    }
    
    fun searchUsers(query: String) {
        _searchQuery.value = query
        
        if (query.isBlank()) {
            loadUsers()
            return
        }
        
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            repository.searchUsers(query)
                .onSuccess { userList ->
                    _users.value = userList
                    _uiState.value = UiState.Success(userList)
                }
                .onFailure { exception ->
                    _uiState.value = UiState.Error("Search failed: ${exception.message}")
                }
        }
    }
    
    fun getUserById(id: Int): User? {
        return _users.value?.find { it.id == id }
    }
    
    fun retry() {
        loadUsers()
    }
}
