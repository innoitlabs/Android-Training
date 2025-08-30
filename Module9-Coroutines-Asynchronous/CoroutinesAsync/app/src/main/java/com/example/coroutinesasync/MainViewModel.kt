package com.example.coroutinesasync

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinesasync.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: String) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    private val postRepository = PostRepository()
    
    // UI State
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    // Users from database
    val allUsers: StateFlow<List<User>>
    
    // Posts from API
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    
    // Counter for demo
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()
    
    init {
        val dao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(dao)
        
        allUsers = userRepository.allUsers.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }
    
    fun incrementCounter() {
        _counter.value += 1
    }
    
    fun decrementCounter() {
        _counter.value -= 1
    }
    
    fun addUser(name: String, email: String) {
        if (name.isBlank() || email.isBlank()) {
            _uiState.value = UiState.Error("Name and email cannot be empty")
            return
        }
        
        viewModelScope.launch {
            try {
                val user = User(name = name.trim(), email = email.trim())
                userRepository.insertUser(user)
                _uiState.value = UiState.Success("User added successfully")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to add user: ${e.message}")
            }
        }
    }
    
    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(user)
                _uiState.value = UiState.Success("User deleted successfully")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to delete user: ${e.message}")
            }
        }
    }
    
    fun loadPosts() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val postsList = postRepository.getPosts()
                _posts.value = postsList.take(10) // Limit to first 10 posts
                _uiState.value = UiState.Success("Posts loaded successfully")
            } catch (e: NetworkException) {
                _uiState.value = UiState.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Unknown error: ${e.message}")
            }
        }
    }
    
    fun performBackgroundTask() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                // Simulate multiple concurrent operations
                val userData = async { fetchUserData() }
                val postsData = async { fetchPostsData() }
                
                val userResult = userData.await()
                val postsResult = postsData.await()
                
                _uiState.value = UiState.Success("$userResult\n$postsResult")
                
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Background task failed: ${e.message}")
            }
        }
    }
    
    private suspend fun fetchUserData(): String {
        delay(1000L)
        return "User data loaded at ${System.currentTimeMillis()}"
    }
    
    private suspend fun fetchPostsData(): String {
        delay(1500L)
        return "Posts data loaded at ${System.currentTimeMillis()}"
    }
    
    fun clearUiState() {
        _uiState.value = UiState.Loading
    }
}
