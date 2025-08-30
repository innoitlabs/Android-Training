# 1. Coroutine Basics & Lifecycle

## What are Coroutines?

Coroutines are lightweight threads for asynchronous programming in Kotlin. They allow you to write asynchronous code in a sequential manner, making it easier to read and maintain.

### Key Characteristics
- **Lightweight**: Thousands of coroutines can run on a single thread
- **Non-blocking**: Coroutines can suspend execution without blocking threads
- **Sequential**: Code appears sequential but executes asynchronously
- **Cancellable**: Coroutines can be cancelled at any time

## Basic Coroutine Concepts

### 1. Coroutine Builders

#### `launch` - Fire and Forget
```kotlin
// Basic launch example
GlobalScope.launch {
    delay(1000L) // Non-blocking delay
    println("Coroutine executed after 1 second")
}
println("This prints immediately")
```

#### `async` - Returns a Result
```kotlin
// async returns a Deferred<T>
val deferred = GlobalScope.async {
    delay(1000L)
    "Result from coroutine"
}

// await() gets the result
val result = deferred.await()
println(result) // "Result from coroutine"
```

#### `runBlocking` - Blocking Coroutine
```kotlin
// Blocks the current thread until completion
runBlocking {
    delay(1000L)
    println("This blocks the thread")
}
```

### 2. Suspending Functions

Functions that can suspend execution without blocking threads:

```kotlin
suspend fun fetchUserData(): User {
    delay(1000L) // Simulate network delay
    return User("John Doe", "john@example.com")
}

// Usage in coroutine
GlobalScope.launch {
    val user = fetchUserData()
    println("User: ${user.name}")
}
```

### 3. Coroutine Context

Context defines the behavior of a coroutine:

```kotlin
// Context with dispatcher
GlobalScope.launch(Dispatchers.IO) {
    // Runs on IO thread pool
    val data = fetchDataFromNetwork()
    
    withContext(Dispatchers.Main) {
        // Switch to main thread for UI updates
        updateUI(data)
    }
}
```

## Coroutine Lifecycle

### 1. Coroutine States
- **Active**: Running or ready to run
- **Completing**: Finished execution, cleaning up
- **Completed**: Successfully finished
- **Cancelling**: Being cancelled
- **Cancelled**: Cancelled

### 2. Lifecycle Example

```kotlin
val job = GlobalScope.launch {
    try {
        println("Coroutine started")
        delay(5000L)
        println("Coroutine completed")
    } catch (e: CancellationException) {
        println("Coroutine cancelled")
    } finally {
        println("Cleanup code")
    }
}

// Cancel after 2 seconds
delay(2000L)
job.cancel()
```

### 3. Structured Concurrency

Coroutines follow structured concurrency principles:

```kotlin
fun main() = runBlocking {
    // Parent coroutine
    launch {
        // Child coroutine 1
        delay(1000L)
        println("Child 1 completed")
    }
    
    launch {
        // Child coroutine 2
        delay(2000L)
        println("Child 2 completed")
    }
    
    // Parent waits for all children
    println("All children completed")
}
```

## Practical Examples

### Example 1: Simple Background Task
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Launch coroutine in lifecycle scope
        lifecycleScope.launch {
            // Simulate background work
            val result = withContext(Dispatchers.IO) {
                delay(2000L)
                "Background task completed"
            }
            
            // Update UI on main thread
            findViewById<TextView>(R.id.resultText).text = result
        }
    }
}
```

### Example 2: Multiple Concurrent Operations
```kotlin
class DataRepository {
    suspend fun fetchUserData(): User {
        delay(1000L)
        return User("John", "john@example.com")
    }
    
    suspend fun fetchUserPosts(): List<Post> {
        delay(1500L)
        return listOf(Post("Post 1"), Post("Post 2"))
    }
}

// Usage
lifecycleScope.launch {
    val userDeferred = async { repository.fetchUserData() }
    val postsDeferred = async { repository.fetchUserPosts() }
    
    val user = userDeferred.await()
    val posts = postsDeferred.await()
    
    // Both operations completed
    displayUserData(user, posts)
}
```

### Example 3: Coroutine with Timeout
```kotlin
fun fetchDataWithTimeout() = runBlocking {
    try {
        withTimeout(5000L) { // 5 second timeout
            delay(10000L) // This will timeout
            "Data fetched"
        }
    } catch (e: TimeoutCancellationException) {
        "Operation timed out"
    }
}
```

## Key Takeaways

1. **Coroutines are lightweight**: You can create thousands of them
2. **Use appropriate builders**: `launch` for fire-and-forget, `async` for results
3. **Suspending functions**: Mark async functions with `suspend`
4. **Structured concurrency**: Parent coroutines wait for children
5. **Context matters**: Choose the right dispatcher for your task
6. **Handle cancellation**: Always clean up resources in `finally` blocks

## Common Patterns

### Pattern 1: Loading State Management
```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: String) : UiState()
    data class Error(val message: String) : UiState()
}

class ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val data = repository.fetchData()
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

### Pattern 2: Retry Logic
```kotlin
suspend fun <T> retryIO(
    times: Int = 3,
    initialDelay: Long = 100,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    repeat(times - 1) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong()
        }
    }
    return block() // Last attempt
}
```

## Next Steps
- Learn about coroutine scopes and dispatchers
- Understand exception handling patterns
- Explore Flow and StateFlow for reactive programming
