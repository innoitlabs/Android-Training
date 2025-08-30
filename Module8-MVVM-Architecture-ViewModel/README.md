# Android MVVM Architecture & Jetpack Components - Complete Guide

## Table of Contents
1. [MVVM Architecture Principles](#mvvm-architecture-principles)
2. [ViewModel Lifecycle & Scope](#viewmodel-lifecycle--scope)
3. [LiveData for Reactive UI Updates](#livedata-for-reactive-ui-updates)
4. [Data Binding & Binding Adapters](#data-binding--binding-adapters)
5. [Repository Pattern Implementation](#repository-pattern-implementation)
6. [Dependency Injection Basics](#dependency-injection-basics)
7. [State Management & UI State](#state-management--ui-state)
8. [Error Handling in ViewModels](#error-handling-in-viewmodels)
9. [Testing ViewModels & LiveData](#testing-viewmodels--livedata)
10. [Architecture Components Integration](#architecture-components-integration)
11. [Best Practices](#best-practices)
12. [Hands-on Lab](#hands-on-lab)
13. [Exercises](#exercises)
14. [Summary](#summary)

## MVVM Architecture Principles

### What is MVVM?
MVVM (Model-View-ViewModel) is an architectural pattern that separates the UI logic from the business logic and data access logic.

### Components:
- **Model**: Data and business logic (Repository, API, Database)
- **View**: UI layer (Activity/Fragment) - observes ViewModel
- **ViewModel**: Connects View and Model, survives configuration changes

### Benefits:
✅ **Separation of Concerns**: Clear boundaries between UI, business logic, and data
✅ **Testability**: Easy to unit test ViewModels independently
✅ **Reactive UI Updates**: LiveData/Flow provide lifecycle-aware data streams
✅ **Configuration Change Survival**: ViewModels survive screen rotations
✅ **Maintainability**: Clean, organized code structure

### Architecture Flow:
```
View (Activity/Fragment) ←→ ViewModel ←→ Repository ←→ Data Sources
     ↑                           ↑           ↑
  Observes                  Business      Single Source
  LiveData                   Logic        of Truth
```

## ViewModel Lifecycle & Scope

### Key Concepts:
- ViewModels survive Activity/Fragment recreation
- Scoped to UI lifecycle (not Application lifecycle)
- Automatically cleared when the associated lifecycle owner is permanently destroyed

### Basic ViewModel Implementation:
```kotlin
class MyViewModel : ViewModel() {
    private val _counter = MutableLiveData(0)
    val counter: LiveData<Int> = _counter

    fun increment() {
        _counter.value = (_counter.value ?: 0) + 1
    }

    override fun onCleared() {
        super.onCleared()
        // Clean up resources
    }
}
```

### ViewModel Factory (for dependencies):
```kotlin
class MyViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```

## LiveData for Reactive UI Updates

### What is LiveData?
LiveData is an observable data holder class that is lifecycle-aware, meaning it respects the lifecycle of other app components.

### Key Features:
- **Lifecycle-aware**: Only updates observers in active lifecycle states
- **Memory leak safe**: Automatically manages subscriptions
- **Configuration change safe**: Survives screen rotations
- **Always up-to-date data**: Ensures observers receive the latest data

### Basic Usage:
```kotlin
class UserViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun updateUserName(name: String) {
        _userName.value = name
    }
}
```

### In Activity/Fragment:
```kotlin
viewModel.userName.observe(this) { name ->
    textView.text = name
}
```

### LiveData Transformations:
```kotlin
val userDisplayName = Transformations.map(userName) { name ->
    "Welcome, $name!"
}
```

## Data Binding & Binding Adapters

### Enable Data Binding:
```kotlin
// build.gradle.kts
android {
    buildFeatures {
        dataBinding = true
    }
}
```

### XML Layout with Data Binding:
```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    
    <data>
        <variable
            name="viewModel"
            type="com.example.mvvmapp.UserViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{viewModel.userName}" />

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{() -> viewModel.updateUserName('John')}"
            android:text="Update Name" />

    </LinearLayout>
</layout>
```

### Binding Adapters:
```kotlin
@BindingAdapter("app:visibleIf")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:loadImage")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context)
            .load(it)
            .into(imageView)
    }
}
```

### In Activity:
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}
```

## Repository Pattern Implementation

### What is Repository Pattern?
Repository pattern provides a clean API for data access, abstracting the data source from the rest of the app.

### Benefits:
- Single source of truth for data
- Abstracts data sources (API, Database, Cache)
- Easier to test and maintain
- Centralized data access logic

### Repository Implementation:
```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    suspend fun getUser(id: Int): Result<User> {
        return try {
            // Try to get from cache first
            val cachedUser = userDao.getUser(id)
            if (cachedUser != null) {
                return Result.success(cachedUser)
            }
            
            // Fetch from API
            val user = apiService.getUser(id)
            userDao.insertUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = apiService.getUsers()
            userDao.insertUsers(users)
            Result.success(users)
        } catch (e: Exception) {
            // Return cached data if available
            val cachedUsers = userDao.getAllUsers()
            if (cachedUsers.isNotEmpty()) {
                Result.success(cachedUsers)
            } else {
                Result.failure(e)
            }
        }
    }
}
```

## Dependency Injection Basics

### Using Hilt (Recommended):

#### Setup:
```kotlin
// build.gradle.kts (app level)
plugins {
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

dependencies {
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
}
```

#### Application Class:
```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

#### Module Definition:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }
}
```

#### ViewModel Injection:
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    // ViewModel implementation
}
```

#### Activity Injection:
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
    // Activity implementation
}
```

## State Management & UI State

### UI State with Sealed Classes:
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### ViewModel with State Management:
```kotlin
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<UiState<List<User>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<User>>> = _uiState
    
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            
            repository.getUsers()
                .onSuccess { userList ->
                    _users.value = userList
                    _uiState.value = UiState.Success(userList)
                }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}
```

### UI Implementation:
```kotlin
viewModel.uiState.observe(this) { state ->
    when (state) {
        is UiState.Loading -> {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            errorText.visibility = View.GONE
        }
        is UiState.Success -> {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorText.visibility = View.GONE
            adapter.submitList(state.data)
        }
        is UiState.Error -> {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            errorText.visibility = View.VISIBLE
            errorText.text = state.message
        }
    }
}
```

## Error Handling in ViewModels

### Comprehensive Error Handling:
```kotlin
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<UiState<List<User>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<User>>> = _uiState
    
    fun loadUser(id: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                val result = repository.getUser(id)
                result.fold(
                    onSuccess = { user ->
                        _uiState.value = UiState.Success(listOf(user))
                    },
                    onFailure = { exception ->
                        val errorMessage = when (exception) {
                            is IOException -> "Network error. Please check your connection."
                            is HttpException -> "Server error. Please try again later."
                            else -> "An unexpected error occurred: ${exception.message}"
                        }
                        _uiState.value = UiState.Error(errorMessage)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
}
```

### Error Handling Best Practices:
1. **Use Result type** for operations that can fail
2. **Handle specific exceptions** (Network, Server, etc.)
3. **Provide user-friendly error messages**
4. **Log errors** for debugging
5. **Implement retry mechanisms**

## Testing ViewModels & LiveData

### Test Dependencies:
```kotlin
dependencies {
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.8")
}
```

### ViewModel Test:
```kotlin
@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var repository: UserRepository
    
    private lateinit var viewModel: UserViewModel
    
    @Before
    fun setup() {
        viewModel = UserViewModel(repository)
    }
    
    @Test
    fun `loadUsers should emit loading then success state`() = runTest {
        // Given
        val users = listOf(User(1, "John"), User(2, "Jane"))
        whenever(repository.getUsers()).thenReturn(Result.success(users))
        
        // When
        viewModel.loadUsers()
        
        // Then
        val states = mutableListOf<UiState<List<User>>>()
        viewModel.uiState.observeForever { states.add(it) }
        
        assertEquals(2, states.size)
        assertTrue(states[0] is UiState.Loading)
        assertTrue(states[1] is UiState.Success)
        assertEquals(users, (states[1] as UiState.Success).data)
    }
    
    @Test
    fun `loadUsers should emit error state on failure`() = runTest {
        // Given
        val error = IOException("Network error")
        whenever(repository.getUsers()).thenReturn(Result.failure(error))
        
        // When
        viewModel.loadUsers()
        
        // Then
        val states = mutableListOf<UiState<List<User>>>()
        viewModel.uiState.observeForever { states.add(it) }
        
        assertEquals(2, states.size)
        assertTrue(states[0] is UiState.Loading)
        assertTrue(states[1] is UiState.Error)
        assertEquals("Network error. Please check your connection.", (states[1] as UiState.Error).message)
    }
}
```

### LiveData Test Extension:
```kotlin
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    afterObserve.invoke()
    
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }
    
    @Suppress("UNCHECKED_CAST")
    return data as T
}
```

## Architecture Components Integration

### Complete MVVM Architecture:
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│      View       │    │    ViewModel     │    │   Repository    │
│  (Activity/     │◄──►│                  │◄──►│                 │
│   Fragment)     │    │  - LiveData      │    │  - API Service  │
│                 │    │  - Business      │    │  - Database     │
│  - Observes     │    │    Logic         │    │  - Cache        │
│    LiveData     │    │  - State Mgmt    │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### Integration Example:
```kotlin
// Data Layer
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String
)

// Network Layer
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
}

// Database Layer
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<User>)
}

// Repository Layer
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = apiService.getUsers()
            userDao.insertUsers(users)
            Result.success(users)
        } catch (e: Exception) {
            val cachedUsers = userDao.getAllUsers()
            if (cachedUsers.isNotEmpty()) {
                Result.success(cachedUsers)
            } else {
                Result.failure(e)
            }
        }
    }
}

// ViewModel Layer
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableLiveData<UiState<List<User>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<User>>> = _uiState
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getUsers()
                .onSuccess { users ->
                    _uiState.value = UiState.Success(users)
                }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}

// View Layer
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        
        setupObservers()
        viewModel.loadUsers()
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showUsers(state.data)
                is UiState.Error -> showError(state.message)
            }
        }
    }
}
```

## Best Practices

### 1. ViewModel Best Practices:
- ✅ Keep ViewModels clean (no Android context)
- ✅ Use LiveData for reactive updates
- ✅ Handle configuration changes properly
- ✅ Use coroutines for async operations
- ❌ Don't store UI references in ViewModel
- ❌ Don't pass Activity/Fragment to ViewModel

### 2. LiveData Best Practices:
- ✅ Use MutableLiveData internally, expose LiveData
- ✅ Use Transformations for data manipulation
- ✅ Handle lifecycle properly
- ✅ Use MediatorLiveData for complex scenarios
- ❌ Don't observe LiveData in ViewModel

### 3. Repository Best Practices:
- ✅ Single source of truth
- ✅ Abstract data sources
- ✅ Handle caching strategy
- ✅ Provide clean API
- ❌ Don't expose implementation details

### 4. Error Handling Best Practices:
- ✅ Use sealed classes for state
- ✅ Provide user-friendly messages
- ✅ Implement retry mechanisms
- ✅ Log errors for debugging
- ❌ Don't crash the app on errors

### 5. Testing Best Practices:
- ✅ Test ViewModels independently
- ✅ Mock dependencies
- ✅ Test all state scenarios
- ✅ Use InstantTaskExecutorRule
- ❌ Don't test implementation details

## Hands-on Lab

### Mini Project: User Profile App

**Requirements:**
- Fetch user data from API (Retrofit)
- Display with LiveData + Data Binding
- Manage state (loading, success, error)
- Inject Repository with Hilt
- Handle configuration changes

**Features:**
1. User list with search functionality
2. User detail view
3. Offline support with caching
4. Error handling and retry
5. Loading states

**Implementation Steps:**
1. Set up project dependencies
2. Create data models
3. Implement API service
4. Create Repository
5. Build ViewModels
6. Design UI with Data Binding
7. Add error handling
8. Write unit tests

## Exercises

### Easy Level:
1. **Counter ViewModel**: Create a simple counter with increment/decrement
2. **Text Input**: Implement two-way data binding for text input
3. **Basic List**: Display a list of items using RecyclerView

### Intermediate Level:
1. **Image Loading**: Add binding adapter for ImageView with Glide
2. **Search Functionality**: Implement search with debouncing
3. **Pagination**: Add pagination to user list

### Advanced Level:
1. **Unit Tests**: Write comprehensive tests for ViewModel
2. **Custom Binding Adapters**: Create custom binding adapters
3. **Complex State Management**: Implement multi-step form with state

## Summary

### Key Takeaways:
- **MVVM improves testability & separation of concerns**
- **ViewModel survives configuration changes**
- **LiveData provides lifecycle-aware reactive updates**
- **Data Binding reduces boilerplate code**
- **Repository pattern provides clean data source abstraction**
- **Dependency Injection improves scalability and testability**
- **Use state management & sealed classes for robust UIs**

### Next Steps:
1. Explore other Jetpack components (Room, Navigation, WorkManager)
2. Learn about Kotlin Flow for reactive programming
3. Implement advanced patterns (Clean Architecture, MVI)
4. Study performance optimization techniques
5. Practice with real-world projects

### Resources:
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [ViewModel Guide](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData Guide](https://developer.android.com/topic/libraries/architecture/livedata)
- [Data Binding Guide](https://developer.android.com/topic/libraries/data-binding)
- [Hilt Documentation](https://dagger.dev/hilt/)

---

**Happy Coding! 🚀**
