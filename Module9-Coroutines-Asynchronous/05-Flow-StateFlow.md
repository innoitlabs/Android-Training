# 5. Flow & StateFlow

## What are Flows?

Flows are cold streams that emit values over time. They are designed for reactive programming and can handle asynchronous data streams.

### Key Characteristics
- **Cold**: Only start emitting when collected
- **Sequential**: Emit values one at a time
- **Cancellable**: Can be cancelled at any time
- **Backpressure**: Handle slow consumers automatically

## Basic Flow Concepts

### 1. Creating Flows

#### Simple Flow
```kotlin
val simpleFlow = flow {
    for (i in 1..5) {
        emit(i)
        delay(1000L)
    }
}

// Collection
lifecycleScope.launch {
    simpleFlow.collect { value ->
        println("Received: $value")
    }
}
```

#### Flow from Collections
```kotlin
val listFlow = listOf(1, 2, 3, 4, 5).asFlow()

val rangeFlow = (1..5).asFlow()

val arrayFlow = arrayOf("a", "b", "c").asFlow()
```

#### Flow from Functions
```kotlin
fun fetchDataFlow(): Flow<String> = flow {
    emit("Loading...")
    delay(1000L)
    emit("Data loaded")
}

// Usage
lifecycleScope.launch {
    fetchDataFlow().collect { data ->
        println(data)
    }
}
```

### 2. Flow Operators

#### Transform Operators
```kotlin
val numbersFlow = (1..10).asFlow()

// Map
numbersFlow.map { it * 2 }
    .collect { println("Doubled: $it") }

// Filter
numbersFlow.filter { it % 2 == 0 }
    .collect { println("Even: $it") }

// Transform
numbersFlow.transform { value ->
    emit("Number: $value")
    emit("Squared: ${value * value}")
}.collect { println(it) }
```

#### Combine Operators
```kotlin
val flow1 = flow { emit("A") }
val flow2 = flow { emit("B") }

// Combine
combine(flow1, flow2) { a, b ->
    "$a$b"
}.collect { println(it) } // Prints: "AB"

// Zip
flow1.zip(flow2) { a, b ->
    "$a$b"
}.collect { println(it) } // Prints: "AB"
```

#### Terminal Operators
```kotlin
val numbersFlow = (1..10).asFlow()

// toList
val list = numbersFlow.toList()

// first
val first = numbersFlow.first()

// count
val count = numbersFlow.count()

// reduce
val sum = numbersFlow.reduce { acc, value -> acc + value }
```

## StateFlow

StateFlow is a hot stream that holds the latest value and emits it to new collectors.

### 1. Basic StateFlow

```kotlin
class MainViewModel : ViewModel() {
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()
    
    fun increment() {
        _counter.value += 1
    }
    
    fun decrement() {
        _counter.value -= 1
    }
}
```

### 2. StateFlow in Activity

```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Collect StateFlow
        lifecycleScope.launch {
            viewModel.counter.collect { count ->
                findViewById<TextView>(R.id.counterText).text = "Count: $count"
            }
        }
        
        findViewById<Button>(R.id.incrementButton).setOnClickListener {
            viewModel.increment()
        }
    }
}
```

### 3. StateFlow with UI State

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<User>) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadUsers() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val users = repository.getUsers()
                _uiState.value = UiState.Success(users)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

## Practical Examples

### Example 1: Network Data Flow

```kotlin
class UserRepository {
    fun getUsersFlow(): Flow<List<User>> = flow {
        emit(emptyList()) // Initial empty state
        
        try {
            val users = apiService.getUsers()
            emit(users)
        } catch (e: Exception) {
            throw e
        }
    }.flowOn(Dispatchers.IO)
}

class MainViewModel : ViewModel() {
    private val repository = UserRepository()
    
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    fun loadUsers() {
        viewModelScope.launch {
            repository.getUsersFlow()
                .catch { e ->
                    // Handle error
                    println("Error: ${e.message}")
                }
                .collect { userList ->
                    _users.value = userList
                }
        }
    }
}
```

### Example 2: Search Flow

```kotlin
class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    init {
        // Debounced search
        viewModelScope.launch {
            searchQuery
                .debounce(300L) // Wait 300ms after last emission
                .filter { it.length >= 2 } // Only search if 2+ characters
                .distinctUntilChanged() // Only emit if value changed
                .flatMapLatest { query ->
                    searchRepository.search(query)
                }
                .collect { results ->
                    _searchResults.value = results
                }
        }
    }
}
```

### Example 3: Database Flow

```kotlin
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
    
    @Query("SELECT * FROM users WHERE name LIKE '%' || :query || '%'")
    fun searchUsers(query: String): Flow<List<User>>
}

class UserRepository(private val userDao: UserDao) {
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    
    fun searchUsers(query: String): Flow<List<User>> = userDao.searchUsers(query)
}

class MainViewModel(
    private val repository: UserRepository
) : ViewModel() {
    
    val users: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}
```

### Example 4: Combined Flows

```kotlin
class DashboardViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val postRepository = PostRepository()
    
    val dashboardData: StateFlow<DashboardData> = combine(
        userRepository.getCurrentUser(),
        postRepository.getRecentPosts(),
        postRepository.getPostCount()
    ) { user, posts, count ->
        DashboardData(user, posts, count)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DashboardData(null, emptyList(), 0)
    )
}

data class DashboardData(
    val user: User?,
    val recentPosts: List<Post>,
    val totalPosts: Int
)
```

## Flow Testing

### Testing Flows

```kotlin
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
fun testStateFlow() = runTest {
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
```

## Best Practices

### 1. Use StateFlow for UI State

```kotlin
// ✅ GOOD: Use StateFlow for UI state
class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}

// ❌ BAD: Don't use LiveData with Flow
class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> = _data
}
```

### 2. Handle Flow Errors

```kotlin
// ✅ GOOD: Handle errors properly
viewModelScope.launch {
    dataFlow
        .catch { e ->
            // Handle error
            _error.value = e.message
        }
        .collect { data ->
            _data.value = data
        }
}
```

### 3. Use Appropriate Operators

```kotlin
// ✅ GOOD: Use debounce for search
searchQuery
    .debounce(300L)
    .filter { it.length >= 2 }
    .distinctUntilChanged()
    .flatMapLatest { query ->
        searchRepository.search(query)
    }
    .collect { results ->
        _searchResults.value = results
    }
```

### 4. Optimize Flow Collection

```kotlin
// ✅ GOOD: Use stateIn for expensive flows
val expensiveFlow: StateFlow<ExpensiveData> = repository.getExpensiveData()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ExpensiveData()
    )
```

## Common Patterns

### Pattern 1: Loading State with Flow

```kotlin
sealed class Resource<T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}

fun <T> Flow<T>.asResource(): Flow<Resource<T>> = flow {
    emit(Resource.Loading)
    try {
        collect { data ->
            emit(Resource.Success(data))
        }
    } catch (e: Exception) {
        emit(Resource.Error(e.message ?: "Unknown error"))
    }
}
```

### Pattern 2: Refreshable Flow

```kotlin
class RefreshableFlow<T>(
    private val fetchData: suspend () -> T
) {
    private val _refreshTrigger = MutableStateFlow(0)
    
    val data: StateFlow<T?> = _refreshTrigger
        .flatMapLatest { fetchData() }
        .stateIn(
            scope = CoroutineScope(Dispatchers.Main),
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
    
    fun refresh() {
        _refreshTrigger.value += 1
    }
}
```

## Key Takeaways

1. **Flows are cold**: Only emit when collected
2. **StateFlow is hot**: Emits latest value to new collectors
3. **Use operators**: Map, filter, combine for data transformation
4. **Handle errors**: Always use catch operator
5. **Optimize collection**: Use stateIn for expensive flows
6. **Test flows**: Use runTest and toList for testing

## Next Steps
- Learn about Room integration with Flow
- Understand WorkManager with coroutines
- Explore advanced Flow operators and patterns
