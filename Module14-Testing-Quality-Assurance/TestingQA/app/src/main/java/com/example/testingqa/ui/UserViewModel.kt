package com.example.testingqa.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testingqa.data.User
import com.example.testingqa.data.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _lastAddedUser = MutableLiveData<User?>()
    val lastAddedUser: LiveData<User?> = _lastAddedUser
    
    private val _searchResults = MutableLiveData<List<User>>()
    val searchResults: LiveData<List<User>> = _searchResults
    
    fun loadUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val userList = repository.getUsers()
                _users.value = userList
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val addedUser = repository.addUser(user)
                _lastAddedUser.value = addedUser
                loadUsers() // Refresh the list
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to add user"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updateUser(user)
                loadUsers() // Refresh the list
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update user"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteUser(user)
                loadUsers() // Refresh the list
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete user"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val results = repository.searchUsers(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to search users"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun syncUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.syncUsers()
                loadUsers() // Refresh the list
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to sync users"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
