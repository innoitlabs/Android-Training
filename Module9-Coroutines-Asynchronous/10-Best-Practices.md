# 10. Best Practices & Anti-Patterns

## Coroutine Best Practices

### 1. Use Lifecycle-Aware Scopes

```kotlin
// ✅ GOOD: Use lifecycle-aware scopes
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        lifecycleScope.launch {
            // Automatically cancelled when Activity is destroyed
            val data = repository.fetchData()
            updateUI(data)
        }
    }
}

// ❌ BAD: Avoid GlobalScope
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        GlobalScope.launch {
            // This coroutine outlives the Activity!
            val data = repository.fetchData()
            updateUI(data) // Potential crash!
        }
    }
}
```

### 2. Choose Appropriate Dispatchers

```kotlin
// ✅ GOOD: Use correct dispatchers for tasks
class DataRepository {
    suspend fun fetchData(): Data {
        return withContext(Dispatchers.IO) {
            // Network call on IO dispatcher
            apiService.getData()
        }
    }
    
    suspend fun processData(data: Data): ProcessedData {
        return withContext(Dispatchers.Default) {
            // CPU-intensive work on Default dispatcher
            dataProcessor.process(data)
        }
    }
    
    suspend fun updateUI(data: Data) {
        withContext(Dispatchers.Main) {
            // UI updates on Main dispatcher
            textView.text = data.toString()
        }
    }
}
```

### 3. Handle Exceptions Properly

```kotlin
// ✅ GOOD: Comprehensive error handling
class MainViewModel : ViewModel() {
    fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                val data = repository.fetchData()
                _uiState.value = UiState.Success(data)
                
            } catch (e: NetworkException) {
                _uiState.value = UiState.Error("Network error: ${e.message}")
            } catch (e: DatabaseException) {
                _uiState.value = UiState.Error("Database error: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Unknown error: ${e.message}")
            }
        }
    }
}
```

### 4. Use Structured Concurrency

```kotlin
// ✅ GOOD: Structured concurrency
fun loadUserData(userId: String) {
    viewModelScope.launch {
        val userDeferred = async { repository.getUser(userId) }
        val postsDeferred = async { repository.getUserPosts(userId) }
        
        val user = userDeferred.await()
        val posts = postsDeferred.await()
        
        _userData.value = UserData(user, posts)
    }
}

// ❌ BAD: Unstructured concurrency
fun loadUserDataBad(userId: String) {
    GlobalScope.launch {
        val user = repository.getUser(userId)
        GlobalScope.launch {
            val posts = repository.getUserPosts(userId)
            // No proper coordination between coroutines
        }
    }
}
```

### 5. Cancel Coroutines Properly

```kotlin
// ✅ GOOD: Proper cancellation handling
class MainActivity : AppCompatActivity() {
    private var dataJob: Job? = null
    
    fun loadData() {
        dataJob?.cancel() // Cancel previous job
        
        dataJob = lifecycleScope.launch {
            try {
                val data = repository.fetchData()
                updateUI(data)
            } catch (e: CancellationException) {
                // Handle cancellation gracefully
                Log.d("MainActivity", "Data loading cancelled")
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        dataJob?.cancel() // Ensure cleanup
    }
}
```

## Flow Best Practices

### 1. Use StateFlow for UI State

```kotlin
// ✅ GOOD: StateFlow for UI state
class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val data = repository.fetchData()
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message)
            }
        }
    }
}

// ❌ BAD: Don't use LiveData with Flow
class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data
}
```

### 2. Optimize Flow Collection

```kotlin
// ✅ GOOD: Use stateIn for expensive flows
class MainViewModel : ViewModel() {
    val expensiveData: StateFlow<ExpensiveData> = repository.getExpensiveData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ExpensiveData()
        )
}

// ❌ BAD: Collecting expensive flows multiple times
class MainViewModel : ViewModel() {
    fun observeData() {
        viewModelScope.launch {
            repository.getExpensiveData().collect { data ->
                // This creates a new collection each time
                _data.value = data
            }
        }
    }
}
```

### 3. Handle Flow Errors

```kotlin
// ✅ GOOD: Proper error handling in flows
viewModelScope.launch {
    dataFlow
        .catch { e ->
            // Handle errors in the flow
            _error.value = e.message
        }
        .collect { data ->
            _data.value = data
        }
}
```

## Room Integration Best Practices

### 1. Use Flow for Database Queries

```kotlin
// ✅ GOOD: Flow for reactive database
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User?>
}

// ❌ BAD: LiveData with Room
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>
}
```

### 2. Proper Database Operations

```kotlin
// ✅ GOOD: Suspend functions for database operations
class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }
    
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
}
```

## Retrofit Integration Best Practices

### 1. Use Suspend Functions

```kotlin
// ✅ GOOD: Suspend functions for API calls
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @POST("users")
    suspend fun createUser(@Body user: User): User
}

// ❌ BAD: Callback-based API calls
interface ApiService {
    @GET("users")
    fun getUsers(callback: Callback<List<User>>)
}
```

### 2. Handle Network Errors

```kotlin
// ✅ GOOD: Comprehensive network error handling
class ApiRepository(private val apiService: ApiService) {
    suspend fun getUsers(): List<User> {
        return try {
            apiService.getUsers()
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> throw NotFoundException("Users not found")
                500 -> throw ServerException("Server error")
                else -> throw NetworkException("HTTP error: ${e.code()}")
            }
        } catch (e: IOException) {
            throw NetworkException("Network error: ${e.message}")
        } catch (e: Exception) {
            throw UnknownException("Unknown error: ${e.message}")
        }
    }
}
```

## WorkManager Best Practices

### 1. Use CoroutineWorker

```kotlin
// ✅ GOOD: CoroutineWorker for background tasks
class SyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Perform background work
            val data = apiService.fetchData()
            database.saveData(data)
            
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }
}
```

### 2. Set Proper Constraints

```kotlin
// ✅ GOOD: Appropriate work constraints
val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
    15, TimeUnit.MINUTES
).setConstraints(
    Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(false)
        .build()
).build()
```

## Testing Best Practices

### 1. Test Coroutines with runTest

```kotlin
// ✅ GOOD: Proper coroutine testing
@Test
fun testCoroutineExecution() = runTest {
    val viewModel = MainViewModel()
    
    viewModel.loadData()
    
    val states = mutableListOf<UiState>()
    viewModel.uiState.toList(states)
    
    assertTrue(states.first() is UiState.Loading)
    assertTrue(states.last() is UiState.Success)
}
```

### 2. Test Flows

```kotlin
// ✅ GOOD: Flow testing
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
```

## Performance Optimization

### 1. Minimize Dispatcher Switching

```kotlin
// ✅ GOOD: Group operations by dispatcher
suspend fun processData() {
    // All IO operations together
    val data = withContext(Dispatchers.IO) {
        val networkData = apiService.getData()
        val dbData = database.getData()
        combineData(networkData, dbData)
    }
    
    // All CPU operations together
    val processedData = withContext(Dispatchers.Default) {
        dataProcessor.process(data)
    }
    
    // UI update
    withContext(Dispatchers.Main) {
        updateUI(processedData)
    }
}

// ❌ BAD: Frequent dispatcher switching
suspend fun processDataBad() {
    withContext(Dispatchers.IO) {
        val data = apiService.getData()
        withContext(Dispatchers.Main) {
            updateUI(data)
            withContext(Dispatchers.IO) {
                database.saveData(data)
            }
        }
    }
}
```

### 2. Use Flow Operators Efficiently

```kotlin
// ✅ GOOD: Efficient Flow operators
val optimizedFlow = dataFlow
    .filter { it.isValid() }
    .map { it.transform() }
    .distinctUntilChanged()
    .debounce(300L)
    .flowOn(Dispatchers.Default)
```

### 3. Avoid Memory Leaks

```kotlin
// ✅ GOOD: Proper cleanup
class MainActivity : AppCompatActivity() {
    private var dataJob: Job? = null
    
    override fun onDestroy() {
        super.onDestroy()
        dataJob?.cancel() // Cancel coroutines
    }
}

// ❌ BAD: Potential memory leak
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        GlobalScope.launch {
            // This coroutine never gets cancelled!
            while (true) {
                delay(1000L)
                // Do work
            }
        }
    }
}
```

## Common Anti-Patterns to Avoid

### 1. Blocking in Coroutines

```kotlin
// ❌ BAD: Blocking in coroutine
suspend fun badExample() {
    Thread.sleep(1000L) // Blocks the thread
}

// ✅ GOOD: Non-blocking delay
suspend fun goodExample() {
    delay(1000L) // Suspends the coroutine
}
```

### 2. Ignoring Cancellation

```kotlin
// ❌ BAD: Ignoring cancellation
suspend fun badExample() {
    repeat(1000) {
        heavyComputation() // No cancellation check
    }
}

// ✅ GOOD: Check for cancellation
suspend fun goodExample() {
    repeat(1000) {
        ensureActive() // Check if coroutine is still active
        heavyComputation()
    }
}
```

### 3. Unnecessary Coroutine Launches

```kotlin
// ❌ BAD: Unnecessary launches
fun badExample() {
    viewModelScope.launch {
        val data = repository.getData()
        viewModelScope.launch { // Unnecessary nested launch
            _data.value = data
        }
    }
}

// ✅ GOOD: Direct assignment
fun goodExample() {
    viewModelScope.launch {
        val data = repository.getData()
        _data.value = data // Direct assignment
    }
}
```

### 4. Not Handling Exceptions

```kotlin
// ❌ BAD: No exception handling
fun badExample() {
    viewModelScope.launch {
        val data = repository.getData() // Could throw exception
        _data.value = data
    }
}

// ✅ GOOD: Proper exception handling
fun goodExample() {
    viewModelScope.launch {
        try {
            val data = repository.getData()
            _data.value = data
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}
```

## Key Takeaways

1. **Always use lifecycle-aware scopes** in Android
2. **Choose appropriate dispatchers** for different tasks
3. **Handle exceptions properly** at all levels
4. **Use structured concurrency** for coordinated operations
5. **Cancel coroutines** when they're no longer needed
6. **Optimize Flow collection** with stateIn for expensive flows
7. **Test coroutines and flows** with proper testing utilities
8. **Avoid blocking operations** in coroutines
9. **Check for cancellation** in long-running operations
10. **Follow the single responsibility principle** for coroutines

## Next Steps

- Implement these best practices in your projects
- Use Android Studio Profiler to monitor coroutine performance
- Write comprehensive tests for your coroutine code
- Explore advanced Flow operators and patterns
- Consider using custom dispatchers for specific use cases
