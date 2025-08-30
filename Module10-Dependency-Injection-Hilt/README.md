# Android Dependency Injection with Hilt - Complete Learning Guide

## 📚 Learning Objectives

By the end of this lesson, learners will be able to:
- ✅ Understand dependency injection principles and benefits
- ✅ Set up Hilt in Android Studio
- ✅ Define modules and providers
- ✅ Manage scopes and lifecycles
- ✅ Explore component hierarchy and dependencies
- ✅ Write tests with Hilt and fake modules
- ✅ Use custom scopes and qualifiers
- ✅ Integrate Hilt with ViewModels and repositories
- ✅ Apply clean architecture with DI
- ✅ Follow best practices and common patterns for DI

## 🎯 What is Dependency Injection?

Dependency Injection (DI) is a design pattern where objects receive their dependencies from an external source rather than creating them internally.

### Core Principle
**Objects should not create their own dependencies; dependencies should be injected.**

### Benefits
- 🔄 **Separation of Concerns**: Each class focuses on its primary responsibility
- 🧪 **Easier Testing**: Replace real dependencies with fake/mock implementations
- ♻️ **Reusability**: Dependencies can be shared across multiple classes
- 📈 **Scalability**: Easy to add new dependencies without changing existing code

### Example Comparison

**❌ Bad Practice (Tight Coupling):**
```kotlin
class UserRepository {
    private val api = ApiService() // tightly coupled
    private val database = Database() // tightly coupled
}
```

**✅ Good Practice (Dependency Injection):**
```kotlin
class UserRepository(
    private val api: ApiService,
    private val database: Database
) { }
```

## 🛠️ Hilt Setup & Configuration

### 1. Project-Level Dependencies

Update your project-level `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
```

### 2. App-Level Dependencies

Update your app-level `build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    // ... existing config ...
    
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
    
    // ... existing dependencies ...
}
```

### 3. Application Class Setup

Create your Application class with `@HiltAndroidApp`:

```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

Don't forget to register it in `AndroidManifest.xml`:

```xml
<application
    android:name=".MyApplication"
    ...>
```

## 📦 Module Definitions & Providers

### Basic Module Structure

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
```

### Key Annotations Explained

- **@Module**: Marks a class as a Hilt module
- **@InstallIn**: Specifies which Hilt component to install the module in
- **@Provides**: Tells Hilt how to provide an instance of a type
- **@Singleton**: Ensures only one instance is created and shared

## 🎭 Scopes & Lifecycle Management

### Available Scopes

| Scope | Lifetime | Use Case |
|-------|----------|----------|
| `@Singleton` | App-wide | Shared resources, databases, API clients |
| `@ActivityScoped` | Activity lifetime | Activity-specific data |
| `@FragmentScoped` | Fragment lifetime | Fragment-specific data |
| `@ViewModelScoped` | ViewModel lifetime | ViewModel-specific data |

### Example Usage

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideUserRepository(api: ApiService): UserRepository {
        return UserRepository(api)
    }
    
    @Provides
    @ActivityScoped
    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences(context)
    }
}
```

## 🏗️ Component Hierarchy & Dependencies

### Hilt Component Hierarchy

```
SingletonComponent (App-wide)
├── ActivityComponent (Activity-scoped)
│   ├── FragmentComponent (Fragment-scoped)
│   └── ViewModelComponent (ViewModel-scoped)
└── ServiceComponent (Service-scoped)
```

### Component Dependencies

- **SingletonComponent**: Available throughout the app
- **ActivityComponent**: Available to the activity and its fragments
- **FragmentComponent**: Available to the fragment and its child fragments
- **ViewModelComponent**: Available to the ViewModel

## 🎯 Hilt with ViewModels

### ViewModel with Hilt

```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    
    fun loadUsers() {
        viewModelScope.launch {
            try {
                val userList = repository.getUsers()
                _users.value = userList
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
```

### Activity Usage

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel.users.observe(this) { users ->
            // Update UI with users
        }
        
        viewModel.loadUsers()
    }
}
```

## 🗄️ Repository Pattern with Hilt

### Repository Implementation

```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
    
    suspend fun getUserById(id: Int): User {
        return apiService.getUserById(id)
    }
}
```

### API Service Interface

```kotlin
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User
}
```

## 🏷️ Custom Scopes & Qualifiers

### Qualifiers for Multiple Bindings

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataApi

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    
    @AuthApi
    @Provides
    @Singleton
    fun provideAuthApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://auth-api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    @DataApi
    @Provides
    @Singleton
    fun provideDataApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://data-api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

### Usage with Qualifiers

```kotlin
class AuthRepository @Inject constructor(
    @AuthApi private val authApi: ApiService
) { }

class DataRepository @Inject constructor(
    @DataApi private val dataApi: ApiService
) { }
```

## 🧪 Testing with Hilt & Fake Modules

### Test Setup

```kotlin
@HiltAndroidTest
class UserViewModelTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @BindValue
    @JvmField
    val fakeRepository: UserRepository = FakeUserRepository()
    
    @Test
    fun `loadUsers should update LiveData with users`() {
        // Given
        val viewModel = UserViewModel(fakeRepository)
        val testUsers = listOf(User(1, "John"), User(2, "Jane"))
        fakeRepository.setUsers(testUsers)
        
        // When
        viewModel.loadUsers()
        
        // Then
        assertEquals(testUsers, viewModel.users.value)
    }
}
```

### Fake Repository Implementation

```kotlin
class FakeUserRepository : UserRepository {
    private var users: List<User> = emptyList()
    
    fun setUsers(userList: List<User>) {
        users = userList
    }
    
    override suspend fun getUsers(): List<User> {
        return users
    }
}
```

## 🏛️ Clean Architecture with Hilt

### Layer Structure

```
Presentation Layer (UI)
├── Activities/Fragments
├── ViewModels
└── Adapters

Domain Layer (Business Logic)
├── Use Cases
├── Entities
└── Repository Interfaces

Data Layer (Data Sources)
├── Repository Implementations
├── API Services
└── Database
```

### Example Implementation

```kotlin
// Domain Layer
interface UserRepository {
    suspend fun getUsers(): List<User>
}

// Data Layer
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}

// Domain Layer
class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        return repository.getUsers()
    }
}

// Presentation Layer
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {
    // Implementation
}
```

## ✅ Best Practices

### Do's ✅
- Use constructor injection whenever possible
- Keep modules small and focused
- Scope objects properly (Singleton vs ActivityScoped)
- Use qualifiers for multiple bindings of the same type
- Always test with fake modules
- Follow the single responsibility principle
- Use meaningful names for qualifiers

### Don'ts ❌
- Don't create circular dependencies
- Don't over-scope objects (use Singleton only when necessary)
- Don't mix DI frameworks
- Don't forget to handle errors in ViewModels
- Don't create overly complex modules

## 🎓 Hands-on Lab / Mini Project

### Project: User List App

**Features:**
- Fetch users from API using Retrofit
- Inject ApiService & Repository with Hilt
- Use ViewModel + LiveData to update UI
- Display results in RecyclerView

**Files to Create:**
- `MyApplication.kt` (Hilt setup)
- `ApiService.kt`, `RetrofitModule.kt`
- `UserRepository.kt`
- `UserViewModel.kt`
- `MainActivity.kt`
- `activity_main.xml`

## 📝 Exercises

### Easy Level
Create a `LoggingModule` that injects a `Logger` instance:

```kotlin
interface Logger {
    fun log(message: String)
}

class ConsoleLogger : Logger {
    override fun log(message: String) {
        println("LOG: $message")
    }
}
```

### Intermediate Level
Use `@Qualifier` for two Retrofit instances (Auth vs Data API):

```kotlin
@AuthApi
@Provides
fun provideAuthRetrofit(): Retrofit { ... }

@DataApi
@Provides
fun provideDataRetrofit(): Retrofit { ... }
```

### Advanced Level
Write instrumented tests with fake Repository using Hilt test utilities:

```kotlin
@HiltAndroidTest
class UserRepositoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @BindValue
    @JvmField
    val fakeApiService: ApiService = FakeApiService()
}
```

## 📋 Summary

### Key Takeaways
- 🔄 **DI decouples dependencies** → cleaner, testable code
- 🎯 **Hilt simplifies DI** with annotations
- 📦 **Use modules** to provide dependencies
- ⏱️ **Manage lifecycle** with scopes
- 🎭 **Apply DI to ViewModels & Repositories**
- 🏷️ **Qualifiers handle** multiple bindings
- 🧪 **Test with fake modules**
- 🏛️ **Clean architecture + Hilt** → maintainable apps

### Next Steps
1. Build and run the provided project
2. Experiment with different scopes
3. Add more complex dependencies
4. Write comprehensive tests
5. Explore advanced Hilt features

---

## 🚀 Getting Started

1. **Clone/Download** this project
2. **Open** in Android Studio
3. **Sync** Gradle files
4. **Build** the project
5. **Run** on emulator/device
6. **Follow** the code examples
7. **Complete** the exercises

Happy Learning! 🎉
