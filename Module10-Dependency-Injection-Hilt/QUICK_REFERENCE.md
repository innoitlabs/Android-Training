# Hilt Quick Reference Guide

## 🏷️ Core Annotations

### Application Level
```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

### Activity/Fragment Level
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity()

@AndroidEntryPoint
class UserFragment : Fragment()
```

### ViewModel Level
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel()
```

### Service Level
```kotlin
@AndroidEntryPoint
class MyService : Service()
```

---

## 📦 Module Annotations

### Basic Module
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

### Interface Binding
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
```

---

## 🎭 Scope Annotations

| Scope | Lifetime | Use Case |
|-------|----------|----------|
| `@Singleton` | App-wide | Shared resources, databases, API clients |
| `@ActivityScoped` | Activity lifetime | Activity-specific data |
| `@FragmentScoped` | Fragment lifetime | Fragment-specific data |
| `@ViewModelScoped` | ViewModel lifetime | ViewModel-specific data |
| `@ServiceScoped` | Service lifetime | Service-specific data |

### Usage Examples
```kotlin
@Provides
@Singleton
fun provideSharedPreferences(context: Context): SharedPreferences

@Provides
@ActivityScoped
fun provideActivityScopedData(): ActivityData

@Provides
@ViewModelScoped
fun provideViewModelScopedData(): ViewModelData
```

---

## 🏷️ Qualifier Annotations

### Custom Qualifiers
```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DataApi
```

### Usage with Qualifiers
```kotlin
@AuthApi
@Provides
@Singleton
fun provideAuthApi(): ApiService { ... }

@DataApi
@Provides
@Singleton
fun provideDataApi(): ApiService { ... }

class AuthRepository @Inject constructor(
    @AuthApi private val api: ApiService
) { }
```

---

## 🔧 Provider Methods

### @Provides
```kotlin
@Provides
@Singleton
fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.example.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
```

### @Binds
```kotlin
@Binds
@Singleton
abstract fun bindUserRepository(
    userRepositoryImpl: UserRepositoryImpl
): UserRepository
```

### Constructor Injection
```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService
) { }
```

---

## 🏗️ Component Hierarchy

```
SingletonComponent (App-wide)
├── ActivityComponent (Activity-scoped)
│   ├── FragmentComponent (Fragment-scoped)
│   └── ViewModelComponent (ViewModel-scoped)
└── ServiceComponent (Service-scoped)
```

### Installation Targets
```kotlin
@InstallIn(SingletonComponent::class)    // App-wide
@InstallIn(ActivityComponent::class)     // Activity-scoped
@InstallIn(FragmentComponent::class)     // Fragment-scoped
@InstallIn(ViewModelComponent::class)    // ViewModel-scoped
@InstallIn(ServiceComponent::class)      // Service-scoped
```

---

## 🧪 Testing Annotations

### Test Setup
```kotlin
@HiltAndroidTest
class UserViewModelTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @BindValue
    @JvmField
    val fakeRepository: UserRepository = FakeUserRepository()
}
```

### Test Module
```kotlin
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {
    
    @Provides
    @Singleton
    fun provideFakeRepository(): UserRepository {
        return FakeUserRepository()
    }
}
```

---

## 📱 Common Patterns

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

### Activity with ViewModel
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel.users.observe(this) { users ->
            // Update UI
        }
        
        viewModel.loadUsers()
    }
}
```

### Repository Pattern
```kotlin
interface UserRepository {
    suspend fun getUsers(): List<User>
}

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}
```

---

## 🔄 Dependency Injection Flow

### 1. Define Dependencies
```kotlin
// Interface
interface ApiService {
    suspend fun getUsers(): List<User>
}

// Implementation
class ApiServiceImpl @Inject constructor() : ApiService {
    override suspend fun getUsers(): List<User> {
        // Implementation
    }
}
```

### 2. Create Module
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiServiceImpl()
    }
}
```

### 3. Inject Dependencies
```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService
) { }

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() { }
```

---

## ⚠️ Common Issues & Solutions

### Issue: "Cannot find symbol @HiltAndroidApp"
**Solution**: Add Hilt plugin to build.gradle
```kotlin
plugins {
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}
```

### Issue: "No injector factory bound"
**Solution**: Add @AndroidEntryPoint to Activity/Fragment
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity()
```

### Issue: "Multiple bindings for the same type"
**Solution**: Use qualifiers
```kotlin
@AuthApi
@Provides
fun provideAuthApi(): ApiService { ... }

@DataApi
@Provides
fun provideDataApi(): ApiService { ... }
```

### Issue: "Circular dependency"
**Solution**: Restructure dependencies or use @Lazy
```kotlin
class UserRepository @Inject constructor(
    @Lazy private val apiService: ApiService
) { }
```

---

## 📋 Best Practices Checklist

- ✅ Use constructor injection whenever possible
- ✅ Keep modules small and focused
- ✅ Use appropriate scopes (don't over-scope)
- ✅ Use qualifiers for multiple bindings of the same type
- ✅ Test with fake implementations
- ✅ Follow single responsibility principle
- ✅ Use meaningful names for qualifiers
- ✅ Handle exceptions in repositories
- ✅ Use @Lazy for circular dependencies
- ✅ Keep modules in appropriate components

---

## 🚀 Quick Start Template

### 1. Application Class
```kotlin
@HiltAndroidApp
class MyApplication : Application()
```

### 2. Module
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```

### 3. Repository
```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService
) { }
```

### 4. ViewModel
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() { }
```

### 5. Activity
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()
}
```

---

*This quick reference covers the most common Hilt patterns and annotations. For detailed explanations, refer to the main README.md file.*
