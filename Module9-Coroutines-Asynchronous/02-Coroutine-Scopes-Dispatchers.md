# 2. Coroutine Scopes & Dispatchers

## Coroutine Scopes

Coroutine scopes define the lifecycle of coroutines and ensure they are cancelled when the scope is destroyed.

### 1. GlobalScope (Avoid in Android)

```kotlin
// ❌ DON'T USE THIS IN ANDROID
GlobalScope.launch {
    // This coroutine lives for the entire application lifetime
    delay(10000L)
    println("This might execute after Activity is destroyed!")
}
```

**Problems with GlobalScope:**
- Coroutines outlive the component that launched them
- Can cause memory leaks
- No automatic cancellation

### 2. Lifecycle-Aware Scopes

#### lifecycleScope (Activities & Fragments)
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ✅ GOOD: Automatically cancelled when Activity is destroyed
        lifecycleScope.launch {
            delay(5000L)
            // This won't execute if Activity is destroyed
            updateUI()
        }
    }
}
```

#### viewModelScope (ViewModels)
```kotlin
class MainViewModel : ViewModel() {
    
    fun loadData() {
        // ✅ GOOD: Automatically cancelled when ViewModel is cleared
        viewModelScope.launch {
            val data = repository.fetchData()
            _data.value = data
        }
    }
    
    fun performBackgroundTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = heavyComputation()
            withContext(Dispatchers.Main) {
                _result.value = result
            }
        }
    }
}
```

#### rememberCoroutineScope (Compose)
```kotlin
@Composable
fun MyScreen() {
    val scope = rememberCoroutineScope()
    
    Button(
        onClick = {
            scope.launch {
                delay(1000L)
                // Update UI state
            }
        }
    ) {
        Text("Click me")
    }
}
```

### 3. Custom Scopes

```kotlin
class MyCustomScope : CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = job + Dispatchers.Main
    
    fun cleanup() {
        job.cancel() // Cancel all coroutines in this scope
    }
}

// Usage
val customScope = MyCustomScope()
customScope.launch {
    // Coroutine work
}
```

## Coroutine Dispatchers

Dispatchers determine which thread(s) a coroutine runs on.

### 1. Main Dispatcher

```kotlin
// UI operations must run on Main thread
lifecycleScope.launch(Dispatchers.Main) {
    // Update UI components
    textView.text = "Updated text"
    progressBar.visibility = View.VISIBLE
}
```

### 2. IO Dispatcher

```kotlin
// Network calls, database operations, file I/O
lifecycleScope.launch(Dispatchers.IO) {
    val data = apiService.fetchData()
    val savedData = database.saveData(data)
    
    withContext(Dispatchers.Main) {
        // Switch back to Main for UI updates
        updateUI(savedData)
    }
}
```

### 3. Default Dispatcher

```kotlin
// CPU-intensive tasks (sorting, parsing, complex calculations)
lifecycleScope.launch(Dispatchers.Default) {
    val sortedList = largeList.sorted()
    val processedData = complexCalculation(sortedList)
    
    withContext(Dispatchers.Main) {
        displayResults(processedData)
    }
}
```

### 4. Unconfined Dispatcher

```kotlin
// ❌ RARELY USED - Can cause issues
GlobalScope.launch(Dispatchers.Unconfined) {
    // Continues on the thread that called it
    // Can lead to unexpected behavior
}
```

## Practical Examples

### Example 1: Proper Threading Pattern

```kotlin
class UserRepository {
    suspend fun getUser(id: String): User {
        // This runs on IO dispatcher
        return withContext(Dispatchers.IO) {
            // Simulate network call
            delay(1000L)
            User(id, "John Doe")
        }
    }
    
    suspend fun saveUser(user: User) {
        // This also runs on IO dispatcher
        withContext(Dispatchers.IO) {
            // Simulate database operation
            delay(500L)
            println("User saved: ${user.name}")
        }
    }
}

class MainViewModel : ViewModel() {
    private val repository = UserRepository()
    
    fun loadUser(id: String) {
        viewModelScope.launch {
            try {
                // Start on Main thread
                _isLoading.value = true
                
                // Switch to IO for network/database work
                val user = withContext(Dispatchers.IO) {
                    repository.getUser(id)
                }
                
                // Back to Main for UI updates
                _user.value = user
                _isLoading.value = false
                
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
}
```

### Example 2: Multiple Dispatchers

```kotlin
class DataProcessor {
    suspend fun processData(data: List<String>): ProcessedResult {
        return withContext(Dispatchers.Default) {
            // CPU-intensive processing
            val processed = data.map { it.uppercase() }
                .filter { it.length > 5 }
                .sorted()
            
            ProcessedResult(processed)
        }
    }
}

class MainViewModel : ViewModel() {
    fun processUserData() {
        viewModelScope.launch {
            try {
                // 1. Fetch data on IO
                val rawData = withContext(Dispatchers.IO) {
                    apiService.fetchData()
                }
                
                // 2. Process data on Default (CPU)
                val processedData = withContext(Dispatchers.Default) {
                    dataProcessor.processData(rawData)
                }
                
                // 3. Save to database on IO
                withContext(Dispatchers.IO) {
                    database.saveData(processedData)
                }
                
                // 4. Update UI on Main
                _result.value = processedData
                
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
```

### Example 3: Lifecycle-Aware Operations

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Launch coroutine that respects lifecycle
        lifecycleScope.launch {
            // This will be cancelled when Activity is destroyed
            repeat(10) {
                delay(1000L)
                binding.counterText.text = "Count: $it"
            }
        }
        
        // Launch coroutine with specific dispatcher
        lifecycleScope.launch(Dispatchers.IO) {
            val data = fetchDataFromNetwork()
            
            // Switch to Main for UI update
            withContext(Dispatchers.Main) {
                binding.dataText.text = data
            }
        }
    }
}
```

## Best Practices

### 1. Choose the Right Dispatcher

```kotlin
// ✅ GOOD: Use IO for network/database
suspend fun fetchData(): Data {
    return withContext(Dispatchers.IO) {
        apiService.getData()
    }
}

// ✅ GOOD: Use Default for CPU-intensive tasks
suspend fun processImage(image: Bitmap): ProcessedImage {
    return withContext(Dispatchers.Default) {
        imageProcessor.process(image)
    }
}

// ✅ GOOD: Use Main for UI updates
suspend fun updateUI(data: Data) {
    withContext(Dispatchers.Main) {
        textView.text = data.toString()
    }
}
```

### 2. Avoid Dispatcher Switching

```kotlin
// ❌ BAD: Unnecessary dispatcher switching
suspend fun badExample() {
    withContext(Dispatchers.IO) {
        val data = fetchData()
        withContext(Dispatchers.Main) {
            updateUI(data)
            withContext(Dispatchers.IO) {
                saveData(data)
            }
        }
    }
}

// ✅ GOOD: Minimize dispatcher switching
suspend fun goodExample() {
    val data = withContext(Dispatchers.IO) {
        fetchData()
    }
    
    withContext(Dispatchers.Main) {
        updateUI(data)
    }
    
    withContext(Dispatchers.IO) {
        saveData(data)
    }
}
```

### 3. Use Structured Concurrency

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
```

## Common Patterns

### Pattern 1: Loading State with Dispatchers

```kotlin
class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                val data = withContext(Dispatchers.IO) {
                    repository.fetchData()
                }
                
                _uiState.value = UiState.Success(data)
                
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val data: Any) : UiState()
    data class Error(val message: String) : UiState()
}
```

### Pattern 2: Background Task with Progress

```kotlin
class MainViewModel : ViewModel() {
    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()
    
    fun performBackgroundTask() {
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 0..100) {
                // Simulate work
                delay(50L)
                
                withContext(Dispatchers.Main) {
                    _progress.value = i / 100f
                }
            }
        }
    }
}
```

## Key Takeaways

1. **Use lifecycle-aware scopes**: `lifecycleScope`, `viewModelScope`
2. **Choose appropriate dispatchers**: Main for UI, IO for network/DB, Default for CPU
3. **Minimize dispatcher switching**: Group operations by dispatcher
4. **Avoid GlobalScope**: Use structured concurrency instead
5. **Handle cancellation**: Coroutines are automatically cancelled with their scope

## Next Steps
- Learn about async/await patterns and structured concurrency
- Understand exception handling in coroutines
- Explore Flow and StateFlow for reactive programming
