# Practice Exercises: Android Coroutines

## Exercise 1: Basic Coroutine Setup (Easy)

**Objective**: Create a simple coroutine that fetches and displays the current time.

**Requirements**:
- Create a button that launches a coroutine
- Simulate a network delay (2 seconds)
- Display the current timestamp after the delay
- Show a loading indicator during the operation

**Expected Output**:
```
Button: "Get Current Time"
Loading: "Fetching time..."
Result: "Current time: 2024-01-15 14:30:25"
```

**Solution Template**:
```kotlin
// In your ViewModel
fun getCurrentTime() {
    viewModelScope.launch {
        try {
            _uiState.value = UiState.Loading
            
            val currentTime = withContext(Dispatchers.IO) {
                delay(2000L) // Simulate network delay
                java.time.LocalDateTime.now().toString()
            }
            
            _uiState.value = UiState.Success("Current time: $currentTime")
            
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Failed to get time: ${e.message}")
        }
    }
}
```

---

## Exercise 2: Multiple Concurrent Operations (Intermediate)

**Objective**: Fetch user data and posts concurrently using async/await.

**Requirements**:
- Create two suspend functions: `fetchUserData()` and `fetchUserPosts()`
- Use async/await to run them concurrently
- Display both results when both complete
- Measure and display the total time taken

**Expected Output**:
```
Button: "Load User Data"
Loading: "Loading user data and posts..."
Result: "User: John Doe (john@example.com)
Posts: 5 posts loaded
Total time: 1.5 seconds"
```

**Solution Template**:
```kotlin
fun loadUserDataAndPosts() {
    viewModelScope.launch {
        try {
            _uiState.value = UiState.Loading
            val startTime = System.currentTimeMillis()
            
            val userDeferred = async { fetchUserData() }
            val postsDeferred = async { fetchUserPosts() }
            
            val user = userDeferred.await()
            val posts = postsDeferred.await()
            
            val endTime = System.currentTimeMillis()
            val duration = (endTime - startTime) / 1000.0
            
            val result = "User: ${user.name} (${user.email})\n" +
                        "Posts: ${posts.size} posts loaded\n" +
                        "Total time: $duration seconds"
            
            _uiState.value = UiState.Success(result)
            
        } catch (e: Exception) {
            _uiState.value = UiState.Error("Failed to load data: ${e.message}")
        }
    }
}

private suspend fun fetchUserData(): User {
    delay(1000L)
    return User("John Doe", "john@example.com")
}

private suspend fun fetchUserPosts(): List<Post> {
    delay(1500L)
    return List(5) { Post("Post ${it + 1}", "Content ${it + 1}") }
}
```

---

## Exercise 3: Retry Logic with Exponential Backoff (Intermediate)

**Objective**: Implement retry logic for API calls with exponential backoff.

**Requirements**:
- Create a function that simulates a failing API call
- Implement retry logic with exponential backoff
- Maximum 3 retry attempts
- Display retry attempts and final result

**Expected Output**:
```
Button: "Fetch Data with Retry"
Loading: "Attempt 1 failed, retrying..."
Loading: "Attempt 2 failed, retrying..."
Loading: "Attempt 3 failed, retrying..."
Result: "Data fetched successfully on attempt 4"
```

**Solution Template**:
```kotlin
fun fetchDataWithRetry() {
    viewModelScope.launch {
        try {
            val result = retryIO(
                times = 4,
                initialDelay = 1000L,
                factor = 2.0
            ) {
                fetchDataWithRandomFailure()
            }
            
            _uiState.value = UiState.Success("Data fetched successfully: $result")
            
        } catch (e: Exception) {
            _uiState.value = UiState.Error("All retry attempts failed: ${e.message}")
        }
    }
}

private suspend fun fetchDataWithRandomFailure(): String {
    delay(500L)
    if (Math.random() < 0.7) { // 70% chance of failure
        throw Exception("Network error")
    }
    return "Success data"
}

suspend fun <T> retryIO(
    times: Int,
    initialDelay: Long,
    factor: Double,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            println("Attempt ${attempt + 1} failed: ${e.message}")
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
    }
    return block() // Last attempt
}
```

---

## Exercise 4: Flow and StateFlow Implementation (Intermediate)

**Objective**: Create a search functionality using Flow with debouncing.

**Requirements**:
- Create a search input field
- Implement debounced search (300ms delay)
- Only search if query is 2+ characters
- Display search results in real-time
- Handle loading and error states

**Expected Output**:
```
Input: "android"
Loading: "Searching for 'android'..."
Results: ["Android Development", "Android Studio", "Android Coroutines"]
```

**Solution Template**:
```kotlin
class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults.asStateFlow()
    
    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()
    
    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300L)
                .filter { it.length >= 2 }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    _isSearching.value = true
                    searchRepository.search(query)
                        .onCompletion { _isSearching.value = false }
                }
                .catch { e ->
                    _searchResults.value = emptyList()
                    println("Search error: ${e.message}")
                }
                .collect { results ->
                    _searchResults.value = results
                }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}

// Mock search repository
class SearchRepository {
    suspend fun search(query: String): Flow<List<String>> = flow {
        delay(500L) // Simulate network delay
        val mockResults = listOf(
            "Android Development",
            "Android Studio", 
            "Android Coroutines",
            "Android Room",
            "Android Retrofit"
        ).filter { it.contains(query, ignoreCase = true) }
        emit(mockResults)
    }
}
```

---

## Exercise 5: Room Database with Flow (Advanced)

**Objective**: Create a complete CRUD application with Room and Flow.

**Requirements**:
- Create a Note entity with title, content, and timestamp
- Implement add, update, delete, and list operations
- Use Flow to observe database changes
- Implement search functionality
- Add sorting options (by date, title)

**Expected Output**:
```
Add Note: Title: "Meeting Notes", Content: "Discuss coroutines"
Notes List: 
- Meeting Notes (2024-01-15 14:30)
- Shopping List (2024-01-15 13:45)
Search: "meeting" -> "Meeting Notes"
```

**Solution Template**:
```kotlin
// Note.kt
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

// NoteDao.kt
@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>
    
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<Note>>
    
    @Insert
    suspend fun insertNote(note: Note)
    
    @Update
    suspend fun updateNote(note: Note)
    
    @Delete
    suspend fun deleteNote(note: Note)
}

// NoteViewModel.kt
class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository
    val allNotes: StateFlow<List<Note>>
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val filteredNotes: StateFlow<List<Note>>
    
    init {
        val dao = AppDatabase.getDatabase(application).noteDao()
        noteRepository = NoteRepository(dao)
        
        allNotes = noteRepository.getAllNotes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
        
        filteredNotes = combine(
            allNotes,
            searchQuery
        ) { notes, query ->
            if (query.isBlank()) {
                notes
            } else {
                notes.filter { 
                    it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }
    
    fun addNote(title: String, content: String) {
        if (title.isBlank()) return
        
        viewModelScope.launch {
            val note = Note(title = title.trim(), content = content.trim())
            noteRepository.insertNote(note)
        }
    }
    
    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }
    
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
```

---

## Exercise 6: Combine Multiple Flows (Advanced)

**Objective**: Create a dashboard that combines data from multiple sources.

**Requirements**:
- Combine user data, posts, and notifications
- Handle loading states for each data source
- Implement error handling for individual sources
- Show combined dashboard data

**Expected Output**:
```
Dashboard:
User: John Doe (john@example.com)
Recent Posts: 3 posts
Unread Notifications: 5 notifications
Last Updated: 2024-01-15 14:30
```

**Solution Template**:
```kotlin
data class DashboardData(
    val user: User?,
    val posts: List<Post>,
    val notifications: List<Notification>,
    val lastUpdated: Long
)

class DashboardViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val postRepository = PostRepository()
    private val notificationRepository = NotificationRepository()
    
    val dashboardData: StateFlow<DashboardData> = combine(
        userRepository.getCurrentUser(),
        postRepository.getRecentPosts(),
        notificationRepository.getUnreadNotifications()
    ) { user, posts, notifications ->
        DashboardData(
            user = user,
            posts = posts,
            notifications = notifications,
            lastUpdated = System.currentTimeMillis()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DashboardData(null, emptyList(), emptyList(), 0L)
    )
    
    // Individual loading states
    private val _userLoading = MutableStateFlow(false)
    val userLoading: StateFlow<Boolean> = _userLoading.asStateFlow()
    
    private val _postsLoading = MutableStateFlow(false)
    val postsLoading: StateFlow<Boolean> = _postsLoading.asStateFlow()
    
    private val _notificationsLoading = MutableStateFlow(false)
    val notificationsLoading: StateFlow<Boolean> = _notificationsLoading.asStateFlow()
    
    fun refreshDashboard() {
        viewModelScope.launch {
            try {
                _userLoading.value = true
                _postsLoading.value = true
                _notificationsLoading.value = true
                
                // Refresh all data sources
                val userDeferred = async { userRepository.refreshUser() }
                val postsDeferred = async { postRepository.refreshPosts() }
                val notificationsDeferred = async { notificationRepository.refreshNotifications() }
                
                userDeferred.await()
                postsDeferred.await()
                notificationsDeferred.await()
                
            } catch (e: Exception) {
                println("Dashboard refresh failed: ${e.message}")
            } finally {
                _userLoading.value = false
                _postsLoading.value = false
                _notificationsLoading.value = false
            }
        }
    }
}
```

---

## Exercise 7: WorkManager with Coroutines (Advanced)

**Objective**: Create a background sync system using WorkManager.

**Requirements**:
- Create a sync worker that fetches data from API
- Store data in local database
- Handle network errors and retry logic
- Schedule periodic sync (every 15 minutes)
- Show sync status in UI

**Expected Output**:
```
Sync Status: "Last sync: 2024-01-15 14:15"
Sync Status: "Syncing..." (during sync)
Sync Status: "Sync completed successfully"
Sync Status: "Sync failed: Network error" (on error)
```

**Solution Template**:
```kotlin
// SyncWorker.kt
class SyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        return try {
            Log.d("SyncWorker", "Starting sync...")
            
            // Fetch data from API
            val apiData = withContext(Dispatchers.IO) {
                apiService.fetchData()
            }
            
            // Store in database
            withContext(Dispatchers.IO) {
                database.saveData(apiData)
            }
            
            // Update sync timestamp
            updateSyncTimestamp()
            
            Log.d("SyncWorker", "Sync completed successfully")
            Result.success()
            
        } catch (e: Exception) {
            Log.e("SyncWorker", "Sync failed: ${e.message}")
            Result.retry()
        }
    }
    
    private suspend fun updateSyncTimestamp() {
        val prefs = applicationContext.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_sync", System.currentTimeMillis()).apply()
    }
}

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    private fun schedulePeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()
        
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "sync_work",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
    }
    
    private fun observeSyncStatus() {
        WorkManager.getInstance(applicationContext)
            .getWorkInfosForUniqueWorkLiveData("sync_work")
            .observe(this) { workInfos ->
                workInfos.firstOrNull()?.let { workInfo ->
                    when (workInfo.state) {
                        WorkInfo.State.RUNNING -> {
                            updateSyncStatus("Syncing...")
                        }
                        WorkInfo.State.SUCCEEDED -> {
                            updateSyncStatus("Sync completed successfully")
                        }
                        WorkInfo.State.FAILED -> {
                            updateSyncStatus("Sync failed")
                        }
                        else -> {
                            // Show last sync time
                            val lastSync = getLastSyncTime()
                            updateSyncStatus("Last sync: $lastSync")
                        }
                    }
                }
            }
    }
    
    private fun updateSyncStatus(status: String) {
        findViewById<TextView>(R.id.syncStatusText).text = status
    }
    
    private fun getLastSyncTime(): String {
        val prefs = getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
        val timestamp = prefs.getLong("last_sync", 0L)
        return if (timestamp > 0) {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(Date(timestamp))
        } else {
            "Never"
        }
    }
}
```

---

## Exercise 8: Testing Coroutines and Flows (Advanced)

**Objective**: Write comprehensive tests for coroutines and flows.

**Requirements**:
- Test coroutine execution
- Test Flow emissions
- Test StateFlow updates
- Test error handling
- Test async/await operations

**Solution Template**:
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTest {
    
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    @Test
    fun testCoroutineExecution() = runTest {
        val result = async {
            delay(1000L)
            "Success"
        }
        
        assertEquals("Success", result.await())
    }
    
    @Test
    fun testFlowEmission() = runTest {
        val flow = flow {
            emit(1)
            emit(2)
            emit(3)
        }
        
        val values = mutableListOf<Int>()
        flow.toList(values)
        
        assertEquals(listOf(1, 2, 3), values)
    }
    
    @Test
    fun testStateFlowUpdates() = runTest {
        val stateFlow = MutableStateFlow(0)
        
        val values = mutableListOf<Int>()
        val job = launch {
            stateFlow.toList(values)
        }
        
        stateFlow.value = 1
        stateFlow.value = 2
        stateFlow.value = 3
        
        job.cancel()
        
        assertEquals(listOf(0, 1, 2, 3), values)
    }
    
    @Test
    fun testErrorHandling() = runTest {
        val flow = flow {
            emit(1)
            throw Exception("Test error")
        }
        
        val values = mutableListOf<Int>()
        var error: Throwable? = null
        
        try {
            flow.collect { values.add(it) }
        } catch (e: Exception) {
            error = e
        }
        
        assertEquals(listOf(1), values)
        assertEquals("Test error", error?.message)
    }
    
    @Test
    fun testAsyncAwait() = runTest {
        val startTime = System.currentTimeMillis()
        
        val deferred1 = async { delay(1000L); "Result 1" }
        val deferred2 = async { delay(1000L); "Result 2" }
        
        val result1 = deferred1.await()
        val result2 = deferred2.await()
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        // Should complete in ~1000ms, not 2000ms
        assertTrue(duration < 1500)
        assertEquals("Result 1", result1)
        assertEquals("Result 2", result2)
    }
}
```

---

## Final Challenge: Complete App Integration

**Objective**: Combine all exercises into a complete app.

**Requirements**:
- Implement all exercises in a single app
- Create a navigation system between different features
- Ensure proper error handling throughout
- Add comprehensive logging
- Implement proper state management
- Write unit tests for all components

**Features to Implement**:
1. Time fetcher (Exercise 1)
2. Concurrent data loading (Exercise 2)
3. Retry logic (Exercise 3)
4. Search functionality (Exercise 4)
5. Notes CRUD (Exercise 5)
6. Dashboard (Exercise 6)
7. Background sync (Exercise 7)
8. Comprehensive testing (Exercise 8)

**Architecture Requirements**:
- MVVM pattern
- Repository pattern
- Clean separation of concerns
- Proper dependency injection
- Comprehensive error handling
- Unit tests for all components

This final challenge will test your understanding of all coroutine concepts and their practical application in a real Android app.
