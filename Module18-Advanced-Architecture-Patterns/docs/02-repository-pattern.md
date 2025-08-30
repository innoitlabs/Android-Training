# Repository Pattern & Data Layers

## Overview

The Repository Pattern is a design pattern that abstracts the data persistence logic from the business logic. It provides a clean API for data access operations and encapsulates the complexity of data sources.

## Why Repository Pattern?

### Problems Without Repository Pattern
```kotlin
// ❌ Bad: Direct API calls in ViewModel
class UserViewModel : ViewModel() {
    fun loadUser(id: Int) {
        viewModelScope.launch {
            try {
                val user = apiService.getUser(id) // Direct API dependency
                _user.value = user
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
```

### Benefits With Repository Pattern
- ✅ **Abstraction**: Business logic doesn't know about data sources
- ✅ **Testability**: Easy to mock repositories
- ✅ **Flexibility**: Can switch data sources without changing business logic
- ✅ **Caching**: Centralized caching strategy
- ✅ **Error Handling**: Consistent error handling across data sources

## Repository Pattern Implementation

### 1. Repository Interface (Domain Layer)

```kotlin
// Domain Layer - Repository Interface
interface UserRepository {
    suspend fun getUser(id: Int): Result<User>
    suspend fun getAllUsers(): Result<List<User>>
    suspend fun saveUser(user: User): Result<Unit>
    suspend fun deleteUser(id: Int): Result<Unit>
    suspend fun searchUsers(query: String): Result<List<User>>
}
```

### 2. Repository Implementation (Data Layer)

```kotlin
// Data Layer - Repository Implementation
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val userMapper: UserMapper
) : UserRepository {
    
    override suspend fun getUser(id: Int): Result<User> {
        return try {
            // Try to get from cache first
            val cachedUser = userDao.getUserById(id)
            if (cachedUser != null) {
                return Result.success(userMapper.mapToDomain(cachedUser))
            }
            
            // Fetch from network
            val networkUser = apiService.getUser(id)
            val userEntity = userMapper.mapToEntity(networkUser)
            
            // Cache the result
            userDao.insertUser(userEntity)
            
            Result.success(networkUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getAllUsers(): Result<List<User>> {
        return try {
            val users = apiService.getAllUsers()
            val userEntities = users.map { userMapper.mapToEntity(it) }
            userDao.insertAllUsers(userEntities)
            Result.success(users)
        } catch (e: Exception) {
            // Fallback to cached data
            val cachedUsers = userDao.getAllUsers()
            if (cachedUsers.isNotEmpty()) {
                Result.success(cachedUsers.map { userMapper.mapToDomain(it) })
            } else {
                Result.failure(e)
            }
        }
    }
    
    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            val userEntity = userMapper.mapToEntity(user)
            userDao.insertUser(userEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteUser(id: Int): Result<Unit> {
        return try {
            userDao.deleteUser(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchUsers(query: String): Result<List<User>> {
        return try {
            val users = apiService.searchUsers(query)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## Data Sources

### 1. Remote Data Source (API)

```kotlin
// API Service Interface
interface ApiService {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
    
    @GET("users")
    suspend fun getAllUsers(): List<User>
    
    @POST("users")
    suspend fun createUser(@Body user: User): User
    
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): User
    
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
    
    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): List<User>
}

// API Response Models
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: String
)
```

### 2. Local Data Source (Database)

```kotlin
// Room Database
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

// Room Entity
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Long
)

// Room DAO
@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
    
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<UserEntity>)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)
    
    @Query("SELECT * FROM users WHERE name LIKE '%' || :query || '%'")
    suspend fun searchUsers(query: String): List<UserEntity>
}
```

## Data Mapping

### Mapper Pattern

```kotlin
// Mapper Interface
interface Mapper<Entity, Domain> {
    fun mapToDomain(entity: Entity): Domain
    fun mapToEntity(domain: Domain): Entity
}

// User Mapper Implementation
class UserMapper @Inject constructor() : Mapper<UserEntity, User> {
    
    override fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            avatar = entity.avatar,
            createdAt = Date(entity.createdAt)
        )
    }
    
    override fun mapToEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            avatar = domain.avatar,
            createdAt = domain.createdAt.time
        )
    }
}

// API Response Mapper
class UserResponseMapper @Inject constructor() : Mapper<UserResponse, User> {
    
    override fun mapToDomain(entity: UserResponse): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            avatar = entity.avatar,
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .parse(entity.createdAt) ?: Date()
        )
    }
    
    override fun mapToEntity(domain: User): UserResponse {
        return UserResponse(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            avatar = domain.avatar,
            createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .format(domain.createdAt)
        )
    }
}
```

## Caching Strategy

### 1. Cache-First Strategy
```kotlin
override suspend fun getUser(id: Int): Result<User> {
    return try {
        // 1. Check cache first
        val cachedUser = userDao.getUserById(id)
        if (cachedUser != null && !isCacheExpired(cachedUser)) {
            return Result.success(userMapper.mapToDomain(cachedUser))
        }
        
        // 2. Fetch from network
        val networkUser = apiService.getUser(id)
        
        // 3. Update cache
        val userEntity = userMapper.mapToEntity(networkUser)
        userDao.insertUser(userEntity)
        
        Result.success(networkUser)
    } catch (e: Exception) {
        // 4. Fallback to cache if available
        val cachedUser = userDao.getUserById(id)
        if (cachedUser != null) {
            Result.success(userMapper.mapToDomain(cachedUser))
        } else {
            Result.failure(e)
        }
    }
}
```

### 2. Network-First Strategy
```kotlin
override suspend fun getAllUsers(): Result<List<User>> {
    return try {
        // 1. Fetch from network
        val networkUsers = apiService.getAllUsers()
        
        // 2. Update cache
        val userEntities = networkUsers.map { userMapper.mapToEntity(it) }
        userDao.insertAllUsers(userEntities)
        
        Result.success(networkUsers)
    } catch (e: Exception) {
        // 3. Fallback to cache
        val cachedUsers = userDao.getAllUsers()
        if (cachedUsers.isNotEmpty()) {
            Result.success(cachedUsers.map { userMapper.mapToDomain(it) })
        } else {
            Result.failure(e)
        }
    }
}
```

## Error Handling

### Custom Exceptions
```kotlin
sealed class DataException(message: String) : Exception(message) {
    class NetworkException(message: String) : DataException(message)
    class DatabaseException(message: String) : DataException(message)
    class MappingException(message: String) : DataException(message)
}

// Enhanced Repository with Better Error Handling
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val userMapper: UserMapper
) : UserRepository {
    
    override suspend fun getUser(id: Int): Result<User> {
        return try {
            val cachedUser = userDao.getUserById(id)
            if (cachedUser != null) {
                return Result.success(userMapper.mapToDomain(cachedUser))
            }
            
            val networkUser = apiService.getUser(id)
            val userEntity = userMapper.mapToEntity(networkUser)
            userDao.insertUser(userEntity)
            
            Result.success(networkUser)
        } catch (e: IOException) {
            Result.failure(DataException.NetworkException("Network error: ${e.message}"))
        } catch (e: SQLiteException) {
            Result.failure(DataException.DatabaseException("Database error: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(DataException.MappingException("Mapping error: ${e.message}"))
        }
    }
}
```

## Testing Repository Pattern

### Unit Tests
```kotlin
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {
    
    @Mock
    private lateinit var apiService: ApiService
    
    @Mock
    private lateinit var userDao: UserDao
    
    @Mock
    private lateinit var userMapper: UserMapper
    
    private lateinit var repository: UserRepositoryImpl
    
    @Before
    fun setup() {
        repository = UserRepositoryImpl(apiService, userDao, userMapper)
    }
    
    @Test
    fun `getUser returns cached user when available`() = runTest {
        // Given
        val userId = 1
        val cachedUserEntity = UserEntity(1, "John", "john@example.com", "", 0L)
        val expectedUser = User(1, "John", "john@example.com", "", Date())
        
        whenever(userDao.getUserById(userId)).thenReturn(cachedUserEntity)
        whenever(userMapper.mapToDomain(cachedUserEntity)).thenReturn(expectedUser)
        
        // When
        val result = repository.getUser(userId)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
        verify(apiService, never()).getUser(userId)
    }
}
```

## Best Practices

1. **Single Responsibility**: Each repository handles one entity type
2. **Interface Segregation**: Keep repository interfaces focused
3. **Dependency Injection**: Use Hilt for repository dependencies
4. **Error Handling**: Implement consistent error handling
5. **Caching Strategy**: Choose appropriate caching strategy
6. **Testing**: Write comprehensive unit tests
7. **Documentation**: Document complex repository logic

## Summary

The Repository Pattern provides:

- ✅ **Abstraction** of data access logic
- ✅ **Testability** through dependency injection
- ✅ **Flexibility** to change data sources
- ✅ **Caching** capabilities
- ✅ **Error handling** consistency

In the next section, we'll explore the Use Case Pattern and how it implements business logic in the Domain Layer.
