# Code Organization & Package Structure

## Overview

Proper code organization and package structure are essential for maintaining scalable and readable Android applications. A well-organized codebase makes it easier for teams to collaborate, understand, and maintain the code.

## Package Structure Principles

### 1. Feature-Based Organization
- **Group by Feature**: Organize code by features rather than technical layers
- **High Cohesion**: Keep related code together
- **Low Coupling**: Minimize dependencies between features
- **Scalability**: Easy to add new features without affecting existing ones

### 2. Clean Architecture Layers
- **Domain Layer**: Business logic and entities
- **Data Layer**: Data access and external services
- **Presentation Layer**: UI and user interactions

## Recommended Package Structure

```
com.example.myapp/
├── app/                           # Main application module
│   ├── di/                        # Dependency injection
│   ├── navigation/                # Navigation components
│   └── MainActivity.kt            # Entry point
├── feature_home/                  # Home feature module
│   ├── ui/                        # UI components
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   └── components/
│   │       ├── HomeHeader.kt
│   │       └── UserCard.kt
│   ├── domain/                    # Feature-specific domain
│   │   ├── HomeRepository.kt
│   │   └── usecase/
│   │       └── GetHomeDataUseCase.kt
│   ├── data/                      # Feature-specific data
│   │   ├── HomeRepositoryImpl.kt
│   │   └── model/
│   │       └── HomeData.kt
│   └── di/                        # Feature-specific DI
│       └── HomeModule.kt
├── feature_profile/               # Profile feature module
│   ├── ui/
│   ├── domain/
│   ├── data/
│   └── di/
├── core_ui/                       # Shared UI components
│   ├── components/
│   │   ├── LoadingIndicator.kt
│   │   ├── ErrorMessage.kt
│   │   └── EmptyState.kt
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── navigation/
│       └── Screen.kt
├── core_network/                  # Network utilities
│   ├── api/
│   │   ├── ApiService.kt
│   │   └── NetworkResult.kt
│   ├── interceptor/
│   │   └── AuthInterceptor.kt
│   └── di/
│       └── NetworkModule.kt
├── core_database/                 # Database utilities
│   ├── dao/
│   │   ├── UserDao.kt
│   │   └── PostDao.kt
│   ├── entity/
│   │   ├── UserEntity.kt
│   │   └── PostEntity.kt
│   ├── AppDatabase.kt
│   └── di/
│       └── DatabaseModule.kt
├── core_common/                   # Common utilities
│   ├── extension/
│   │   ├── StringExtensions.kt
│   │   ├── ViewExtensions.kt
│   │   └── DateExtensions.kt
│   ├── util/
│   │   ├── Constants.kt
│   │   ├── DateUtils.kt
│   │   └── ValidationUtils.kt
│   └── di/
│       └── CommonModule.kt
├── domain/                        # Domain layer
│   ├── model/
│   │   ├── User.kt
│   │   ├── Post.kt
│   │   └── Result.kt
│   ├── repository/
│   │   ├── UserRepository.kt
│   │   └── PostRepository.kt
│   ├── usecase/
│   │   ├── GetUserUseCase.kt
│   │   ├── GetUsersUseCase.kt
│   │   └── CreateUserUseCase.kt
│   └── exception/
│       └── DomainException.kt
└── data/                          # Data layer
    ├── repository/
    │   ├── UserRepositoryImpl.kt
    │   └── PostRepositoryImpl.kt
    ├── api/
    │   ├── UserApiService.kt
    │   └── PostApiService.kt
    ├── mapper/
    │   ├── UserMapper.kt
    │   └── PostMapper.kt
    └── di/
        └── DataModule.kt
```

## Feature Module Structure

### 1. UI Package (`ui/`)

```kotlin
// feature_home/ui/HomeScreen.kt
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    HomeScreenContent(
        uiState = uiState,
        onUserClick = { userId -> viewModel.onUserClick(userId) },
        onRefresh = { viewModel.refresh() }
    )
}

// feature_home/ui/HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadHomeData()
    }
    
    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            getHomeDataUseCase()
                .onSuccess { data ->
                    _uiState.value = HomeUiState.Success(data)
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
    
    fun onUserClick(userId: Int) {
        // Handle user click
    }
    
    fun refresh() {
        loadHomeData()
    }
}

// feature_home/ui/components/HomeHeader.kt
@Composable
fun HomeHeader(
    title: String,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings"
            )
        }
    }
}
```

### 2. Domain Package (`domain/`)

```kotlin
// feature_home/domain/HomeRepository.kt
interface HomeRepository {
    suspend fun getHomeData(): Result<HomeData>
}

// feature_home/domain/usecase/GetHomeDataUseCase.kt
class GetHomeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeData> {
        return homeRepository.getHomeData()
    }
}

// feature_home/domain/model/HomeData.kt
data class HomeData(
    val users: List<User>,
    val recentPosts: List<Post>,
    val statistics: Statistics
)

data class Statistics(
    val totalUsers: Int,
    val totalPosts: Int,
    val activeUsers: Int
)
```

### 3. Data Package (`data/`)

```kotlin
// feature_home/data/HomeRepositoryImpl.kt
class HomeRepositoryImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : HomeRepository {
    
    override suspend fun getHomeData(): Result<HomeData> {
        return try {
            val usersDeferred = async { userRepository.getUsers() }
            val postsDeferred = async { postRepository.getRecentPosts() }
            
            val users = usersDeferred.await().getOrThrow()
            val posts = postsDeferred.await().getOrThrow()
            
            val statistics = Statistics(
                totalUsers = users.size,
                totalPosts = posts.size,
                activeUsers = users.count { it.isActive }
            )
            
            val homeData = HomeData(users, posts, statistics)
            Result.success(homeData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## Core Module Structure

### 1. Core UI (`core_ui/`)

```kotlin
// core_ui/components/LoadingIndicator.kt
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

// core_ui/components/ErrorMessage.kt
@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

// core_ui/navigation/Screen.kt
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: Int) = "profile/$userId"
    }
    object Settings : Screen("settings")
}
```

### 2. Core Network (`core_network/`)

```kotlin
// core_network/api/ApiService.kt
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
    
    @POST("users")
    suspend fun createUser(@Body user: User): User
}

// core_network/api/NetworkResult.kt
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

// core_network/interceptor/AuthInterceptor.kt
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        val token = tokenManager.getToken()
        if (token != null) {
            val authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            return chain.proceed(authenticatedRequest)
        }
        
        return chain.proceed(originalRequest)
    }
}
```

### 3. Core Database (`core_database/`)

```kotlin
// core_database/AppDatabase.kt
@Database(
    entities = [UserEntity::class, PostEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}

// core_database/entity/UserEntity.kt
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Long,
    val isActive: Boolean
)

// core_database/dao/UserDao.kt
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
    
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<UserEntity>)
    
    @Delete
    suspend fun deleteUser(user: UserEntity)
}
```

### 4. Core Common (`core_common/`)

```kotlin
// core_common/extension/StringExtensions.kt
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return length >= 8 && any { it.isUpperCase() } && any { it.isDigit() }
}

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}

// core_common/extension/ViewExtensions.kt
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

// core_common/util/Constants.kt
object Constants {
    const val BASE_URL = "https://api.example.com/"
    const val DATABASE_NAME = "app_database"
    const val PREF_NAME = "app_preferences"
    
    object Timeouts {
        const val CONNECT_TIMEOUT = 30L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 30L
    }
    
    object Cache {
        const val MAX_SIZE = 100
        const val EXPIRY_TIME = 24 * 60 * 60 * 1000L // 24 hours
    }
}

// core_common/util/DateUtils.kt
object DateUtils {
    fun formatDate(date: Date, pattern: String = "yyyy-MM-dd"): String {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }
    
    fun parseDate(dateString: String, pattern: String = "yyyy-MM-dd"): Date? {
        return try {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }
    
    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val targetDate = Calendar.getInstance().apply { time = date }
        
        return today.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == targetDate.get(Calendar.DAY_OF_YEAR)
    }
}
```

## Domain Layer Structure

### 1. Models (`domain/model/`)

```kotlin
// domain/model/User.kt
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Date,
    val isActive: Boolean = true,
    val isVerified: Boolean = false
) {
    val displayName: String
        get() = if (isVerified) "$name ✓" else name
    
    val profileUrl: String
        get() = "/users/$id"
    
    fun isNewUser(): Boolean {
        val daysSinceCreation = (Date().time - createdAt.time) / (1000 * 60 * 60 * 24)
        return daysSinceCreation <= 7
    }
}

// domain/model/Post.kt
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val authorId: Int,
    val createdAt: Date,
    val likes: Int = 0,
    val comments: Int = 0
) {
    val isPopular: Boolean
        get() = likes > 100 || comments > 50
    
    val timeAgo: String
        get() = getTimeAgo(createdAt)
    
    private fun getTimeAgo(date: Date): String {
        val now = Date()
        val diffInMillis = now.time - date.time
        val diffInMinutes = diffInMillis / (1000 * 60)
        
        return when {
            diffInMinutes < 1 -> "Just now"
            diffInMinutes < 60 -> "$diffInMinutes minutes ago"
            diffInMinutes < 1440 -> "${diffInMinutes / 60} hours ago"
            else -> "${diffInMinutes / 1440} days ago"
        }
    }
}

// domain/model/Result.kt
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
    
    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading
    
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }
    
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Result is still loading")
    }
}
```

### 2. Repositories (`domain/repository/`)

```kotlin
// domain/repository/UserRepository.kt
interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUser(id: Int): Result<User>
    suspend fun createUser(user: User): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(id: Int): Result<Unit>
    suspend fun searchUsers(query: String): Result<List<User>>
}

// domain/repository/PostRepository.kt
interface PostRepository {
    suspend fun getPosts(): Result<List<Post>>
    suspend fun getPost(id: Int): Result<Post>
    suspend fun getUserPosts(userId: Int): Result<List<Post>>
    suspend fun getRecentPosts(limit: Int = 10): Result<List<Post>>
    suspend fun createPost(post: Post): Result<Post>
    suspend fun updatePost(post: Post): Result<Post>
    suspend fun deletePost(id: Int): Result<Unit>
}
```

### 3. Use Cases (`domain/usecase/`)

```kotlin
// domain/usecase/GetUserUseCase.kt
class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int): Result<User> {
        return if (id > 0) {
            userRepository.getUser(id)
        } else {
            Result.Error(IllegalArgumentException("Invalid user ID"))
        }
    }
}

// domain/usecase/GetUsersUseCase.kt
class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}

// domain/usecase/CreateUserUseCase.kt
class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String, email: String): Result<User> {
        return try {
            validateInput(name, email)
            
            val user = User(
                id = 0, // Will be assigned by the database
                name = name.trim(),
                email = email.lowercase(),
                avatar = generateAvatar(name),
                createdAt = Date()
            )
            
            userRepository.createUser(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    private fun validateInput(name: String, email: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Name cannot be empty")
        }
        if (!email.isValidEmail()) {
            throw IllegalArgumentException("Invalid email format")
        }
    }
    
    private fun generateAvatar(name: String): String {
        return "https://ui-avatars.com/api/?name=${name.replace(" ", "+")}&size=200"
    }
}
```

## Data Layer Structure

### 1. Repositories (`data/repository/`)

```kotlin
// data/repository/UserRepositoryImpl.kt
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val userMapper: UserMapper
) : UserRepository {
    
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            // Try to get from cache first
            val cachedUsers = userDao.getAllUsers()
            if (cachedUsers.isNotEmpty()) {
                return Result.Success(cachedUsers.map { userMapper.mapToDomain(it) })
            }
            
            // Fetch from network
            val networkUsers = apiService.getUsers()
            val userEntities = networkUsers.map { userMapper.mapToEntity(it) }
            
            // Cache the results
            userDao.insertAllUsers(userEntities)
            
            Result.Success(networkUsers)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun getUser(id: Int): Result<User> {
        return try {
            // Try cache first
            userDao.getUserById(id)?.let { cachedUser ->
                return Result.Success(userMapper.mapToDomain(cachedUser))
            }
            
            // Fetch from network
            val networkUser = apiService.getUser(id)
            val userEntity = userMapper.mapToEntity(networkUser)
            
            // Cache the result
            userDao.insertUser(userEntity)
            
            Result.Success(networkUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun createUser(user: User): Result<User> {
        return try {
            val createdUser = apiService.createUser(user)
            val userEntity = userMapper.mapToEntity(createdUser)
            userDao.insertUser(userEntity)
            Result.Success(createdUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun updateUser(user: User): Result<User> {
        return try {
            val updatedUser = apiService.updateUser(user.id, user)
            val userEntity = userMapper.mapToEntity(updatedUser)
            userDao.insertUser(userEntity)
            Result.Success(updatedUser)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun deleteUser(id: Int): Result<Unit> {
        return try {
            apiService.deleteUser(id)
            userDao.deleteUserById(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override suspend fun searchUsers(query: String): Result<List<User>> {
        return try {
            val users = apiService.searchUsers(query)
            Result.Success(users)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
```

### 2. Mappers (`data/mapper/`)

```kotlin
// data/mapper/UserMapper.kt
class UserMapper @Inject constructor() {
    
    fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            avatar = entity.avatar,
            createdAt = Date(entity.createdAt),
            isActive = entity.isActive
        )
    }
    
    fun mapToEntity(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            avatar = domain.avatar,
            createdAt = domain.createdAt.time,
            isActive = domain.isActive
        )
    }
    
    fun mapToDomainList(entities: List<UserEntity>): List<User> {
        return entities.map { mapToDomain(it) }
    }
    
    fun mapToEntityList(domains: List<User>): List<UserEntity> {
        return domains.map { mapToEntity(it) }
    }
}
```

## Best Practices

1. **Feature-First Organization**: Group code by features rather than technical layers
2. **Clear Package Names**: Use descriptive package names that indicate purpose
3. **Consistent Structure**: Maintain consistent structure across all modules
4. **Separation of Concerns**: Keep different concerns in separate packages
5. **Dependency Direction**: Ensure dependencies flow in the correct direction
6. **Naming Conventions**: Follow consistent naming conventions
7. **Documentation**: Document package purposes and responsibilities
8. **Testing Structure**: Mirror the main package structure in test packages

## Summary

Proper code organization provides:

- ✅ **Better maintainability**
- ✅ **Easier navigation**
- ✅ **Clear dependencies**
- ✅ **Scalable architecture**
- ✅ **Team collaboration**
- ✅ **Reduced complexity**

In the next section, we'll explore Documentation & Code Standards for maintaining code quality.
