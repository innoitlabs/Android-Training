# Android Hilt Exercises - Hands-on Practice

## 🎯 Exercise Overview

This document contains practical exercises to reinforce your understanding of Dependency Injection with Hilt. Complete these exercises in order, as they build upon each other.

## 📋 Prerequisites

- Android Studio installed
- Basic knowledge of Kotlin
- Understanding of Android fundamentals
- Project setup completed (from README.md)

---

## 🟢 Exercise 1: Basic Hilt Setup (Easy)

### Objective
Set up a basic Hilt configuration and inject a simple service.

### Tasks
1. Create a `Logger` interface and implementation
2. Create a `LoggingModule` to provide the logger
3. Inject the logger into MainActivity
4. Display log messages in the UI

### Starter Code

```kotlin
// Create Logger interface
interface Logger {
    fun log(message: String)
}

// Create ConsoleLogger implementation
class ConsoleLogger : Logger {
    override fun log(message: String) {
        println("LOG: $message")
    }
}
```

### Expected Output
- App should display log messages in a TextView
- Messages should be logged to console
- No build errors

### Solution Hints
- Use `@Module` and `@InstallIn(SingletonComponent::class)`
- Use `@Provides` to provide the Logger instance
- Use `@Inject` constructor in ConsoleLogger
- Use `@AndroidEntryPoint` in MainActivity

---

## 🟡 Exercise 2: Network Module with Retrofit (Intermediate)

### Objective
Create a network module that provides Retrofit and API service instances.

### Tasks
1. Create an `ApiService` interface for JSONPlaceholder API
2. Create a `NetworkModule` with Retrofit configuration
3. Create a `User` data class
4. Inject the API service into a repository

### API Endpoints to Use
```
GET https://jsonplaceholder.typicode.com/users
GET https://jsonplaceholder.typicode.com/users/{id}
```

### Starter Code

```kotlin
// User data class
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val username: String
)

// ApiService interface
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User
}
```

### Expected Output
- Network module should provide Retrofit instance
- API service should be injectable
- No network-related build errors

### Solution Hints
- Use `@Singleton` scope for network dependencies
- Configure base URL for JSONPlaceholder
- Add Gson converter factory
- Use `@Provides` for both Retrofit and ApiService

---

## 🟠 Exercise 3: Repository Pattern with Hilt (Intermediate)

### Objective
Implement the repository pattern with Hilt dependency injection.

### Tasks
1. Create a `UserRepository` interface
2. Create a `UserRepositoryImpl` class
3. Create a module to bind the interface to implementation
4. Inject the repository into a ViewModel

### Starter Code

```kotlin
// Repository interface
interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: Int): User
}

// Repository implementation
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    // Implement methods
}
```

### Expected Output
- Repository should be injectable via interface
- Should handle API calls through ApiService
- No circular dependency errors

### Solution Hints
- Use `@Binds` instead of `@Provides` for interface binding
- Use `@Singleton` scope for repository
- Handle exceptions in repository methods
- Use `@Inject` constructor in implementation

---

## 🔴 Exercise 4: ViewModel with Hilt (Intermediate)

### Objective
Create a ViewModel that uses Hilt for dependency injection and manages UI state.

### Tasks
1. Create a `UserViewModel` with `@HiltViewModel`
2. Inject the UserRepository
3. Use LiveData to manage UI state
4. Handle loading, success, and error states

### Starter Code

```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState
    
    sealed class UiState {
        object Loading : UiState()
        data class Success(val users: List<User>) : UiState()
        data class Error(val message: String) : UiState()
    }
    
    fun loadUsers() {
        // Implement loading logic
    }
}
```

### Expected Output
- ViewModel should be injectable in Activity
- Should handle different UI states
- Should use coroutines for async operations

### Solution Hints
- Use `viewModelScope.launch` for coroutines
- Update UI state in main thread
- Handle exceptions properly
- Use `@AndroidEntryPoint` in Activity

---

## 🟣 Exercise 5: Custom Qualifiers (Advanced)

### Objective
Use custom qualifiers to provide multiple instances of the same type.

### Tasks
1. Create qualifiers for different API services
2. Create separate modules for different APIs
3. Inject different services into different repositories
4. Use both services in the same app

### Starter Code

```kotlin
// Qualifiers
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataApi

// Different API services
interface AuthApiService {
    @POST("login")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse
}

interface DataApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
```

### Expected Output
- Two different API services should be injectable
- Different repositories should use different APIs
- No ambiguity errors in dependency injection

### Solution Hints
- Create separate modules for each API
- Use different base URLs
- Use `@Named` or custom qualifiers
- Bind services to different repositories

---

## 🔵 Exercise 6: Testing with Hilt (Advanced)

### Objective
Write comprehensive tests using Hilt testing utilities and fake implementations.

### Tasks
1. Create fake implementations of dependencies
2. Write unit tests for ViewModel
3. Write instrumented tests for Repository
4. Use `@BindValue` to replace real dependencies

### Starter Code

```kotlin
// Fake repository for testing
class FakeUserRepository : UserRepository {
    private var users: List<User> = emptyList()
    private var shouldReturnError = false
    
    fun setUsers(userList: List<User>) {
        users = userList
    }
    
    fun setShouldReturnError(shouldError: Boolean) {
        shouldReturnError = shouldError
    }
    
    override suspend fun getUsers(): List<User> {
        if (shouldReturnError) {
            throw Exception("Network error")
        }
        return users
    }
}

// Test class
@HiltAndroidTest
class UserViewModelTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @BindValue
    @JvmField
    val fakeRepository: UserRepository = FakeUserRepository()
}
```

### Expected Output
- Tests should pass with fake implementations
- Should test success and error scenarios
- Should verify LiveData updates
- Should test ViewModel lifecycle

### Solution Hints
- Use `@HiltAndroidTest` for instrumented tests
- Use `@BindValue` to replace dependencies
- Test different UI states
- Use `InstantTaskExecutorRule` for LiveData testing

---

## 🎯 Exercise 7: Clean Architecture Integration (Advanced)

### Objective
Implement clean architecture principles with Hilt dependency injection.

### Tasks
1. Create domain layer with use cases
2. Create data layer with repository implementations
3. Create presentation layer with ViewModels
4. Use proper dependency injection at layer boundaries

### Architecture Structure

```
Domain Layer:
├── entities/
│   └── User.kt
├── repositories/
│   └── UserRepository.kt
└── usecases/
    └── GetUsersUseCase.kt

Data Layer:
├── repositories/
│   └── UserRepositoryImpl.kt
├── datasources/
│   └── ApiService.kt
└── di/
    └── DataModule.kt

Presentation Layer:
├── viewmodels/
│   └── UserViewModel.kt
├── ui/
│   └── MainActivity.kt
└── di/
    └── PresentationModule.kt
```

### Expected Output
- Clean separation of concerns
- Proper dependency injection at boundaries
- Testable architecture
- Scalable code structure

### Solution Hints
- Use interfaces for repository contracts
- Inject use cases into ViewModels
- Keep modules focused and small
- Use proper scoping for each layer

---

## 📝 Exercise 8: Advanced Scoping (Expert)

### Objective
Master different scopes and understand when to use each one.

### Tasks
1. Create objects with different scopes
2. Understand scope lifecycle
3. Create custom scopes if needed
4. Test scope behavior

### Scopes to Implement

```kotlin
// Singleton scope
@Provides
@Singleton
fun provideSharedPreferences(context: Context): SharedPreferences

// Activity scope
@Provides
@ActivityScoped
fun provideActivityScopedData(): ActivityData

// ViewModel scope
@Provides
@ViewModelScoped
fun provideViewModelScopedData(): ViewModelData
```

### Expected Output
- Understanding of scope lifecycle
- Proper use of different scopes
- No memory leaks
- Efficient resource usage

### Solution Hints
- Use Singleton for app-wide shared resources
- Use ActivityScoped for activity-specific data
- Use ViewModelScoped for ViewModel-specific data
- Avoid over-scoping objects

---

## 🏆 Final Challenge: Complete App Integration

### Objective
Build a complete app that demonstrates all Hilt concepts learned.

### Requirements
1. **User Management App** with the following features:
   - User list display
   - User detail view
   - Search functionality
   - Offline caching
   - Error handling
   - Loading states

2. **Architecture Requirements**:
   - Clean architecture
   - Repository pattern
   - ViewModel with LiveData
   - Proper scoping
   - Comprehensive testing

3. **Technical Requirements**:
   - Hilt for dependency injection
   - Retrofit for API calls
   - Room for local storage
   - RecyclerView for lists
   - Navigation component
   - Unit and instrumented tests

### Evaluation Criteria
- ✅ Code compiles without errors
- ✅ All features work correctly
- ✅ Proper error handling
- ✅ Good user experience
- ✅ Comprehensive test coverage
- ✅ Clean, maintainable code
- ✅ Proper use of Hilt patterns

---

## 📚 Additional Resources

### Documentation
- [Hilt Official Documentation](https://dagger.dev/hilt/)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Retrofit Documentation](https://square.github.io/retrofit/)

### Sample Projects
- [Hilt Sample App](https://github.com/google/dagger/tree/master/examples/android-hilt)
- [Android Architecture Samples](https://github.com/android/architecture-samples)

### Best Practices
- Always use constructor injection when possible
- Keep modules small and focused
- Use appropriate scopes
- Test with fake implementations
- Follow clean architecture principles

---

## 🎉 Completion Checklist

- [ ] Exercise 1: Basic Hilt Setup
- [ ] Exercise 2: Network Module with Retrofit
- [ ] Exercise 3: Repository Pattern with Hilt
- [ ] Exercise 4: ViewModel with Hilt
- [ ] Exercise 5: Custom Qualifiers
- [ ] Exercise 6: Testing with Hilt
- [ ] Exercise 7: Clean Architecture Integration
- [ ] Exercise 8: Advanced Scoping
- [ ] Final Challenge: Complete App Integration

**Congratulations!** 🎊 You've mastered Android Dependency Injection with Hilt!

---

*Remember: Practice makes perfect! Keep experimenting with different patterns and scenarios to deepen your understanding.*
