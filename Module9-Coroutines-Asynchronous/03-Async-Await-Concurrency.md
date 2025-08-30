# 3. Async/Await & Structured Concurrency

## Structured Concurrency

Structured concurrency ensures that coroutines follow a predictable lifecycle and are properly managed. When a parent coroutine is cancelled, all its children are automatically cancelled.

### Key Principles

1. **Parent-Child Relationship**: Child coroutines are tied to their parent's lifecycle
2. **Automatic Cancellation**: When parent is cancelled, all children are cancelled
3. **Error Propagation**: Exceptions in children can cancel the parent
4. **Resource Management**: Resources are properly cleaned up

## Async/Await Pattern

### Basic Async/Await

```kotlin
class MainViewModel : ViewModel() {
    fun loadUserData(userId: String) {
        viewModelScope.launch {
            try {
                // Launch concurrent operations
                val userDeferred = async { repository.getUser(userId) }
                val postsDeferred = async { repository.getUserPosts(userId) }
                val settingsDeferred = async { repository.getUserSettings(userId) }
                
                // Wait for all results
                val user = userDeferred.await()
                val posts = postsDeferred.await()
                val settings = settingsDeferred.await()
                
                // Combine results
                val userData = UserData(user, posts, settings)
                _userData.value = userData
                
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
```

### Async with Different Dispatchers

```kotlin
fun loadDataWithDifferentDispatchers() {
    viewModelScope.launch {
        val userDeferred = async(Dispatchers.IO) {
            // Network call
            apiService.getUser(userId)
        }
        
        val imageDeferred = async(Dispatchers.Default) {
            // CPU-intensive image processing
            imageProcessor.processImage(imageData)
        }
        
        val user = userDeferred.await()
        val processedImage = imageDeferred.await()
        
        // Update UI on Main thread
        withContext(Dispatchers.Main) {
            updateUI(user, processedImage)
        }
    }
}
```

## Practical Examples

### Example 1: Parallel Data Loading

```kotlin
class DashboardViewModel : ViewModel() {
    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                // Load data in parallel
                val userDeferred = async { userRepository.getCurrentUser() }
                val postsDeferred = async { postRepository.getRecentPosts() }
                val notificationsDeferred = async { notificationRepository.getUnreadCount() }
                val statsDeferred = async { statsRepository.getUserStats() }
                
                // Wait for all results
                val user = userDeferred.await()
                val posts = postsDeferred.await()
                val notifications = notificationsDeferred.await()
                val stats = statsDeferred.await()
                
                // Combine into dashboard data
                val dashboardData = DashboardData(
                    user = user,
                    posts = posts,
                    unreadNotifications = notifications,
                    stats = stats
                )
                
                _uiState.value = UiState.Success(dashboardData)
                
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

### Example 2: Sequential vs Parallel Operations

```kotlin
class DataProcessor {
    // Sequential processing (slower)
    suspend fun processSequentially(): List<ProcessedData> {
        val result = mutableListOf<ProcessedData>()
        
        for (i in 1..10) {
            val processed = processItem(i)
            result.add(processed)
        }
        
        return result
    }
    
    // Parallel processing (faster)
    suspend fun processInParallel(): List<ProcessedData> {
        val deferreds = (1..10).map { i ->
            async { processItem(i) }
        }
        
        return deferreds.awaitAll()
    }
    
    private suspend fun processItem(id: Int): ProcessedData {
        delay(1000L) // Simulate work
        return ProcessedData(id, "Processed $id")
    }
}
```

### Example 3: Error Handling with Async

```kotlin
class RobustDataLoader {
    suspend fun loadDataWithFallback(): Data {
        return try {
            // Try primary source
            val primaryData = async { primarySource.getData() }
            val secondaryData = async { secondarySource.getData() }
            
            // Wait for primary, with timeout
            withTimeout(5000L) {
                primaryData.await()
            }
            
        } catch (e: TimeoutCancellationException) {
            // Fallback to secondary source
            secondaryData.await()
            
        } catch (e: Exception) {
            // Return cached data
            cache.getData()
        }
    }
}
```

## Advanced Patterns

### Pattern 1: SupervisorScope

```kotlin
class ImageLoader {
    suspend fun loadImages(urls: List<String>): List<Image> {
        return supervisorScope {
            val deferreds = urls.map { url ->
                async {
                    try {
                        loadImage(url)
                    } catch (e: Exception) {
                        // One failure doesn't cancel others
                        null
                    }
                }
            }
            
            deferreds.awaitAll().filterNotNull()
        }
    }
}
```

### Pattern 2: CoroutineScope with Custom Context

```kotlin
class CustomScope {
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e("CustomScope", "Error: ${throwable.message}")
        }
    )
    
    fun performTask() {
        scope.launch {
            // This coroutine uses the custom scope
            val result = async { heavyComputation() }
            val processed = result.await()
            // Handle result
        }
    }
    
    fun cleanup() {
        scope.cancel() // Cancel all coroutines in this scope
    }
}
```

### Pattern 3: Structured Concurrency with Resources

```kotlin
class DatabaseManager {
    suspend fun performTransaction(): Result {
        return withContext(Dispatchers.IO) {
            val connection = database.openConnection()
            
            try {
                // Use connection in transaction
                connection.beginTransaction()
                
                val result1 = async { connection.executeQuery("SELECT * FROM table1") }
                val result2 = async { connection.executeQuery("SELECT * FROM table2") }
                
                val data1 = result1.await()
                val data2 = result2.await()
                
                // Process results
                val processedData = processData(data1, data2)
                
                connection.commitTransaction()
                processedData
                
            } catch (e: Exception) {
                connection.rollbackTransaction()
                throw e
            } finally {
                connection.close()
            }
        }
    }
}
```

## Best Practices

### 1. Use Structured Concurrency

```kotlin
// ✅ GOOD: Structured concurrency
fun loadData() {
    viewModelScope.launch {
        val data1 = async { repository.getData1() }
        val data2 = async { repository.getData2() }
        
        val result1 = data1.await()
        val result2 = data2.await()
        
        combineResults(result1, result2)
    }
}

// ❌ BAD: Unstructured concurrency
fun loadDataBad() {
    GlobalScope.launch {
        val data1 = async { repository.getData1() }
        val data2 = async { repository.getData2() }
        // No proper lifecycle management
    }
}
```

### 2. Handle Exceptions Properly

```kotlin
// ✅ GOOD: Proper exception handling
fun loadDataWithErrorHandling() {
    viewModelScope.launch {
        try {
            val data1 = async { repository.getData1() }
            val data2 = async { repository.getData2() }
            
            val result1 = data1.await()
            val result2 = data2.await()
            
            _data.value = combineResults(result1, result2)
            
        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}
```

### 3. Use Appropriate Dispatchers

```kotlin
// ✅ GOOD: Use correct dispatchers
fun loadDataWithDispatchers() {
    viewModelScope.launch {
        val networkData = async(Dispatchers.IO) {
            apiService.getData()
        }
        
        val processedData = async(Dispatchers.Default) {
            dataProcessor.process(rawData)
        }
        
        val networkResult = networkData.await()
        val processedResult = processedData.await()
        
        withContext(Dispatchers.Main) {
            updateUI(networkResult, processedResult)
        }
    }
}
```

## Common Anti-Patterns

### Anti-Pattern 1: Blocking in Coroutines

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

### Anti-Pattern 2: Ignoring Cancellation

```kotlin
// ❌ BAD: Ignoring cancellation
suspend fun badExample() {
    repeat(1000) {
        // Heavy work without checking cancellation
        heavyComputation()
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

## Testing Async Code

### Testing Async Operations

```kotlin
@Test
fun testAsyncOperations() = runTest {
    val viewModel = MainViewModel()
    
    viewModel.loadData()
    
    val states = mutableListOf<UiState>()
    viewModel.uiState.toList(states)
    
    assertTrue(states.first() is UiState.Loading)
    assertTrue(states.last() is UiState.Success)
}

@Test
fun testParallelExecution() = runTest {
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
```

## Key Takeaways

1. **Use structured concurrency**: Always use lifecycle-aware scopes
2. **Leverage async/await**: For concurrent operations that return results
3. **Handle exceptions**: Always wrap async operations in try-catch
4. **Choose appropriate dispatchers**: IO for network/DB, Default for CPU work
5. **Avoid blocking**: Use delay() instead of Thread.sleep()
6. **Check cancellation**: Use ensureActive() in long-running loops
7. **Test properly**: Use runTest for testing coroutines

## Next Steps
- Learn about exception handling patterns
- Understand Flow and StateFlow
- Explore Room and Retrofit integration
