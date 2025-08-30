# Clean Architecture Principles

## Overview

Clean Architecture is a software design philosophy that emphasizes separation of concerns and dependency inversion. It was introduced by Robert C. Martin (Uncle Bob) and provides a way to structure applications that are independent of frameworks, databases, and external agencies.

## Core Principles

### 1. Dependency Rule
The most important rule in Clean Architecture is the **Dependency Rule**: source code dependencies must point inward, toward higher-level policies.

```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│         (UI, Controllers)           │
├─────────────────────────────────────┤
│           Business Logic            │
│         (Use Cases, Entities)       │
├─────────────────────────────────────┤
│           Data Layer                │
│      (Repositories, Data Sources)   │
└─────────────────────────────────────┘
```

### 2. Layers in Android

#### Domain Layer (Innermost)
- **Purpose**: Contains business logic and entities
- **Components**: Use Cases, Entities, Repository Interfaces
- **Dependencies**: None (pure Kotlin/Java)
- **Example**:
```kotlin
// Entity
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Use Case
class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: Int): Result<User> {
        return userRepository.getUser(id)
    }
}

// Repository Interface
interface UserRepository {
    suspend fun getUser(id: Int): Result<User>
}
```

#### Data Layer (Middle)
- **Purpose**: Implements data access and external services
- **Components**: Repository Implementations, API Services, Database
- **Dependencies**: Domain Layer
- **Example**:
```kotlin
class UserRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun getUser(id: Int): Result<User> {
        return try {
            val user = apiService.getUser(id)
            userDao.insertUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

#### Presentation Layer (Outermost)
- **Purpose**: Handles UI and user interactions
- **Components**: Activities, Fragments, ViewModels, Composables
- **Dependencies**: Domain Layer
- **Example**:
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadUser(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getUserUseCase(id)
                .onSuccess { user ->
                    _uiState.value = UiState.Success(user)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
```

## Benefits

### 1. Testability
- Each layer can be tested independently
- Business logic is isolated from framework dependencies
- Easy to mock dependencies

### 2. Independence
- Framework independent
- Database independent
- UI independent
- External agency independent

### 3. Maintainability
- Clear separation of concerns
- Easy to understand and modify
- Reduced coupling between components

### 4. Scalability
- Easy to add new features
- Simple to refactor
- Clear boundaries for team collaboration

## Implementation Guidelines

### 1. Package Structure
```
com.example.app
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── data/
│   ├── api/
│   ├── db/
│   ├── repository/
│   └── mapper/
└── presentation/
    ├── ui/
    ├── viewmodel/
    └── navigation/
```

### 2. Dependency Injection
Use Hilt to manage dependencies and ensure proper layer separation:

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
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao)
    }
}
```

### 3. Error Handling
Implement consistent error handling across layers:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## Best Practices

1. **Keep Domain Layer Pure**: No Android dependencies
2. **Use Interfaces**: Define contracts in domain layer
3. **Single Responsibility**: Each class has one reason to change
4. **Dependency Inversion**: Depend on abstractions, not concretions
5. **Test-Driven Development**: Write tests for each layer

## Common Anti-Patterns to Avoid

1. **Leaky Abstractions**: Domain layer knowing about Android specifics
2. **Circular Dependencies**: Layers depending on each other
3. **Fat Controllers**: Business logic in UI layer
4. **Tight Coupling**: Direct instantiation instead of dependency injection

## Summary

Clean Architecture provides a solid foundation for building maintainable, testable, and scalable Android applications. By following these principles, you create applications that are:

- ✅ Easy to test
- ✅ Easy to maintain
- ✅ Easy to understand
- ✅ Easy to extend
- ✅ Framework independent

In the next section, we'll explore the Repository Pattern and how it fits into the Data Layer of Clean Architecture.
