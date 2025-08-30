# Hands-on Lab: User Manager App

## Overview
Build a complete User Manager App that demonstrates all the coroutine concepts learned. This app will include:
- User management with Room database
- API integration with Retrofit
- Reactive UI with StateFlow
- Background sync with WorkManager
- Error handling and loading states

## Project Setup

### 1. Dependencies
First, ensure your `app/build.gradle.kts` has all necessary dependencies:

```kotlin
dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // ViewModel & LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

## Lab Exercises

### Exercise 1: Basic Coroutine Setup

**Objective**: Create a simple coroutine that updates UI

**Steps**:
1. Create a button in your layout
2. Launch a coroutine when button is clicked
3. Simulate background work with delay
4. Update UI with result

**Code**:
```kotlin
// In MainActivity.kt
findViewById<Button>(R.id.startButton).setOnClickListener {
    lifecycleScope.launch {
        // Show loading
        findViewById<TextView>(R.id.resultText).text = "Loading..."
        
        // Simulate work
        val result = withContext(Dispatchers.IO) {
            delay(2000L)
            "Task completed at ${System.currentTimeMillis()}"
        }
        
        // Update UI
        findViewById<TextView>(R.id.resultText).text = result
    }
}
```

**Expected Result**: Button click shows "Loading..." then displays completion message after 2 seconds.

### Exercise 2: Multiple Concurrent Operations

**Objective**: Use async/await to perform multiple operations concurrently

**Steps**:
1. Create two suspend functions that simulate different tasks
2. Use async to launch both concurrently
3. Wait for both results with await()
4. Display combined result

**Code**:
```kotlin
suspend fun fetchUserData(): String {
    delay(1000L)
    return "User data loaded"
}

suspend fun fetchUserPosts(): String {
    delay(1500L)
    return "User posts loaded"
}

// In button click
lifecycleScope.launch {
    val userDeferred = async { fetchUserData() }
    val postsDeferred = async { fetchUserPosts() }
    
    val userData = userDeferred.await()
    val postsData = postsDeferred.await()
    
    findViewById<TextView>(R.id.resultText).text = "$userData\n$postsData"
}
```

**Expected Result**: Both operations complete in ~1.5 seconds (not 2.5 seconds) due to concurrency.

### Exercise 3: StateFlow Implementation

**Objective**: Create a reactive UI with StateFlow

**Steps**:
1. Create a ViewModel with StateFlow
2. Implement loading, success, and error states
3. Observe StateFlow in Activity
4. Update UI based on state changes

**Code**:
```kotlin
// MainViewModel.kt
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: String) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val data = withContext(Dispatchers.IO) {
                    delay(2000L)
                    "Data loaded successfully"
                }
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

// MainActivity.kt
private val viewModel: MainViewModel by viewModels()

lifecycleScope.launch {
    viewModel.uiState.collect { state ->
        when (state) {
            is UiState.Loading -> {
                findViewById<TextView>(R.id.resultText).text = "Loading..."
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            }
            is UiState.Success -> {
                findViewById<TextView>(R.id.resultText).text = state.data
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
            is UiState.Error -> {
                findViewById<TextView>(R.id.resultText).text = "Error: ${state.message}"
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }
        }
    }
}
```

### Exercise 4: Room Database Integration

**Objective**: Integrate Room database with coroutines and Flow

**Steps**:
1. Create User entity and DAO
2. Set up Room database
3. Create repository with Flow
4. Observe database changes in UI

**Code**:
```kotlin
// User.kt
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String
)

// UserDao.kt
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
    
    @Insert
    suspend fun insertUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
}

// AppDatabase.kt
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// UserRepository.kt
class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
}

// MainViewModel.kt
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    val allUsers: StateFlow<List<User>>
    
    init {
        val dao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(dao)
        allUsers = repository.allUsers.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }
    
    fun addUser(name: String, email: String) {
        viewModelScope.launch {
            val user = User(name = name, email = email)
            repository.insertUser(user)
        }
    }
}
```

### Exercise 5: Retrofit Integration

**Objective**: Fetch data from API using Retrofit with coroutines

**Steps**:
1. Create API service interface
2. Set up Retrofit client
3. Create repository with API calls
4. Handle network errors

**Code**:
```kotlin
// Post.kt
data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)

// ApiService.kt
interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
    
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): Post
}

// RetrofitClient.kt
object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

// PostRepository.kt
class PostRepository {
    private val apiService = RetrofitClient.apiService
    
    suspend fun getPosts(): List<Post> {
        return try {
            apiService.getPosts()
        } catch (e: Exception) {
            throw NetworkException("Failed to fetch posts: ${e.message}")
        }
    }
}

// MainViewModel.kt
class MainViewModel : ViewModel() {
    private val postRepository = PostRepository()
    
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()
    
    fun loadPosts() {
        viewModelScope.launch {
            try {
                val postsList = postRepository.getPosts()
                _posts.value = postsList
            } catch (e: Exception) {
                // Handle error
                println("Error loading posts: ${e.message}")
            }
        }
    }
}
```

### Exercise 6: WorkManager Background Task

**Objective**: Implement background data sync using WorkManager

**Steps**:
1. Create a Worker class
2. Schedule periodic work
3. Handle work constraints
4. Update UI when work completes

**Code**:
```kotlin
// SyncWorker.kt
class SyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    
    override suspend fun doWork(): Result {
        return try {
            // Simulate sync work
            delay(5000L)
            
            // Update database or perform sync
            println("Background sync completed")
            
            Result.success()
        } catch (e: Exception) {
            println("Background sync failed: ${e.message}")
            Result.retry()
        }
    }
}

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Schedule periodic sync
        schedulePeriodicSync()
    }
    
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
}
```

### Exercise 7: Error Handling and Retry Logic

**Objective**: Implement robust error handling with retry mechanisms

**Steps**:
1. Create custom exception classes
2. Implement retry logic with exponential backoff
3. Handle different types of errors
4. Show appropriate error messages

**Code**:
```kotlin
// Exceptions.kt
sealed class AppException(message: String) : Exception(message)
class NetworkException(message: String) : AppException(message)
class DatabaseException(message: String) : AppException(message)

// RetryUtils.kt
suspend fun <T> retryIO(
    times: Int = 3,
    initialDelay: Long = 1000,
    factor: Double = 2.0,
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

// MainViewModel.kt
class MainViewModel : ViewModel() {
    fun loadDataWithRetry() {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                val data = retryIO(times = 3) {
                    apiService.fetchData()
                }
                
                _uiState.value = UiState.Success(data)
                
            } catch (e: NetworkException) {
                _uiState.value = UiState.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Unknown error: ${e.message}")
            }
        }
    }
}
```

## Final Project: Complete User Manager App

Combine all exercises to create a complete app with:

### Features:
1. **User Management**: Add, view, delete users
2. **API Integration**: Fetch posts from JSONPlaceholder API
3. **Database**: Store users locally with Room
4. **Background Sync**: Periodic data sync with WorkManager
5. **Reactive UI**: Real-time updates with StateFlow
6. **Error Handling**: Comprehensive error handling and retry logic

### UI Components:
- User list with RecyclerView
- Add user form
- Posts list from API
- Loading indicators
- Error messages
- Sync status

### Architecture:
- MVVM pattern
- Repository pattern
- Dependency injection (manual)
- Clean separation of concerns

## Testing Your Implementation

### Unit Tests:
```kotlin
@Test
fun testViewModelLoadData() = runTest {
    val viewModel = MainViewModel()
    
    viewModel.loadData()
    
    val states = mutableListOf<UiState>()
    viewModel.uiState.toList(states)
    
    assertTrue(states.first() is UiState.Loading)
    assertTrue(states.last() is UiState.Success)
}

@Test
fun testRetryLogic() = runTest {
    var attempts = 0
    val result = retryIO(times = 3) {
        attempts++
        if (attempts < 3) throw Exception("Network error")
        "Success"
    }
    
    assertEquals("Success", result)
    assertEquals(3, attempts)
}
```

## Success Criteria

Your app should demonstrate:
- ✅ Proper coroutine usage with appropriate dispatchers
- ✅ Structured concurrency with async/await
- ✅ Reactive UI updates with StateFlow
- ✅ Room database integration with Flow
- ✅ Retrofit API calls with suspend functions
- ✅ Background work with WorkManager
- ✅ Comprehensive error handling
- ✅ Clean architecture and separation of concerns
- ✅ Unit tests for coroutines and flows

## Next Steps

After completing this lab:
1. Add more features (search, filtering, pagination)
2. Implement offline-first architecture
3. Add more sophisticated error handling
4. Implement caching strategies
5. Add UI animations and transitions
6. Write comprehensive tests

This hands-on lab provides practical experience with all the coroutine concepts covered in the documentation.
