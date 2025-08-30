# Advanced Kotlin Features & Patterns

## Overview

Kotlin provides powerful features that enable clean, concise, and type-safe code. Understanding these advanced features is essential for building maintainable Android applications.

## Sealed Classes for State Management

### 1. UI State with Sealed Classes

```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// Usage in ViewModel
@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val uiState: StateFlow<UiState<User>> = _uiState.asStateFlow()
    
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

// Usage in Compose
@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
        }
        is UiState.Success -> {
            UserCard(user = uiState.data)
        }
        is UiState.Error -> {
            ErrorMessage(message = uiState.message)
        }
    }
}
```

### 2. Network Result with Sealed Classes

```kotlin
sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
    
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

## Delegation Pattern

### 1. Property Delegation

```kotlin
// Lazy initialization
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    private val userCache by lazy {
        mutableMapOf<Int, User>()
    }
    
    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
}

// Observable properties
class UserViewModel : ViewModel() {
    private var _user by Delegates.observable<User?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            // Handle user change
            onUserChanged(newValue)
        }
    }
    
    private fun onUserChanged(user: User?) {
        // Update UI or perform actions
    }
}

// Vetoable properties
class UserSettings {
    var maxRetryCount by Delegates.vetoable(3) { _, oldValue, newValue ->
        newValue in 1..10 // Only allow values between 1 and 10
    }
}
```

### 2. Custom Delegates

```kotlin
class SharedPreferencesDelegate<T>(
    private val context: Context,
    private val key: String,
    private val defaultValue: T
) {
    private val prefs by lazy {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (defaultValue) {
            is String -> prefs.getString(key, defaultValue as String) as T
            is Int -> prefs.getInt(key, defaultValue as Int) as T
            is Boolean -> prefs.getBoolean(key, defaultValue as Boolean) as T
            is Float -> prefs.getFloat(key, defaultValue as Float) as T
            is Long -> prefs.getLong(key, defaultValue as Long) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        prefs.edit().apply {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> throw IllegalArgumentException("Unsupported type")
            }
        }.apply()
    }
}

// Usage
class UserPreferences(context: Context) {
    var userId by SharedPreferencesDelegate(context, "user_id", 0)
    var userName by SharedPreferencesDelegate(context, "user_name", "")
    var isLoggedIn by SharedPreferencesDelegate(context, "is_logged_in", false)
}
```

## Coroutines and Flow

### 1. Advanced Coroutines

```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    
    suspend fun getUserWithCache(id: Int): User {
        return withContext(Dispatchers.IO) {
            // Try cache first
            userDao.getUserById(id)?.let { cachedUser ->
                return@withContext cachedUser
            }
            
            // Fetch from network
            val networkUser = apiService.getUser(id)
            
            // Cache the result
            userDao.insertUser(networkUser)
            
            networkUser
        }
    }
    
    suspend fun getUsersParallel(): List<User> {
        return withContext(Dispatchers.IO) {
            val deferredUsers = (1..10).map { id ->
                async { apiService.getUser(id) }
            }
            
            deferredUsers.awaitAll()
        }
    }
    
    suspend fun getUserWithTimeout(id: Int): User {
        return withTimeout(5000L) { // 5 seconds timeout
            apiService.getUser(id)
        }
    }
    
    suspend fun getUserWithRetry(id: Int, maxRetries: Int = 3): User {
        repeat(maxRetries) { attempt ->
            try {
                return apiService.getUser(id)
            } catch (e: Exception) {
                if (attempt == maxRetries - 1) throw e
                delay(1000L * (attempt + 1)) // Exponential backoff
            }
        }
        throw IllegalStateException("Should not reach here")
    }
}
```

### 2. Flow Operations

```kotlin
class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    
    fun getUserFlow(id: Int): Flow<User> {
        return flow {
            // Emit cached user first
            userDao.getUserById(id)?.let { emit(it) }
            
            // Fetch from network
            val networkUser = apiService.getUser(id)
            userDao.insertUser(networkUser)
            
            // Emit updated user
            emit(networkUser)
        }.flowOn(Dispatchers.IO)
    }
    
    fun searchUsersFlow(query: String): Flow<List<User>> {
        return flow {
            emit(emptyList()) // Emit empty list initially
            
            if (query.length >= 2) {
                val users = apiService.searchUsers(query)
                emit(users)
            }
        }
        .debounce(300L) // Wait 300ms after last emission
        .distinctUntilChanged() // Only emit if result changed
        .flowOn(Dispatchers.IO)
    }
    
    fun getUserUpdatesFlow(): Flow<User> {
        return userDao.getUserUpdates()
            .map { userEntity -> userEntity.toDomain() }
            .flowOn(Dispatchers.IO)
    }
}
```

## Extension Functions

### 1. View Extensions

```kotlin
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

fun View.onClick(debounceTime: Long = 300L, action: () -> Unit) {
    setOnClickListener {
        isClickable = false
        action()
        postDelayed({ isClickable = true }, debounceTime)
    }
}

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}
```

### 2. String Extensions

```kotlin
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

fun String.toTitleCase(): String {
    return lowercase().capitalizeWords()
}

fun String.truncate(maxLength: Int, suffix: String = "..."): String {
    return if (length <= maxLength) this else substring(0, maxLength) + suffix
}
```

### 3. Collection Extensions

```kotlin
fun <T> List<T>.secondOrNull(): T? = getOrNull(1)

fun <T> List<T>.secondLastOrNull(): T? = getOrNull(size - 2)

fun <T> List<T>.randomOrNull(): T? = if (isEmpty()) null else random()

fun <T> List<T>.distinctBy(selector: (T) -> K): List<T> {
    val seen = mutableSetOf<K>()
    return filter { seen.add(selector(it)) }
}

fun <T> List<T>.chunked(size: Int): List<List<T>> {
    return chunked(size)
}
```

## Data Classes and Copy

### 1. Immutable Data Classes

```kotlin
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Date,
    val isVerified: Boolean = false
) {
    // Custom getters
    val displayName: String
        get() = if (isVerified) "$name ✓" else name
    
    val profileUrl: String
        get() = "/users/$id"
    
    // Custom setters (create new instances)
    fun withVerified(verified: Boolean): User = copy(isVerified = verified)
    
    fun withAvatar(newAvatar: String): User = copy(avatar = newAvatar)
    
    fun updateName(newName: String): User = copy(name = newName.trim())
}

// Usage
val user = User(1, "John Doe", "john@example.com", "", Date())
val verifiedUser = user.withVerified(true)
val userWithAvatar = user.withAvatar("https://example.com/avatar.jpg")
```

### 2. Builder Pattern with Data Classes

```kotlin
data class UserBuilder(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val avatar: String = "",
    val createdAt: Date = Date(),
    val isVerified: Boolean = false
) {
    fun withId(id: Int) = copy(id = id)
    fun withName(name: String) = copy(name = name)
    fun withEmail(email: String) = copy(email = email)
    fun withAvatar(avatar: String) = copy(avatar = avatar)
    fun withCreatedAt(createdAt: Date) = copy(createdAt = createdAt)
    fun withVerified(verified: Boolean) = copy(isVerified = verified)
    
    fun build(): User = User(id, name, email, avatar, createdAt, isVerified)
}

// Usage
val user = UserBuilder()
    .withId(1)
    .withName("John Doe")
    .withEmail("john@example.com")
    .withVerified(true)
    .build()
```

## Inline Functions and Reified Types

### 1. Inline Functions

```kotlin
inline fun <reified T> createInstance(): T {
    return T::class.java.getDeclaredConstructor().newInstance()
}

inline fun <reified T> List<*>.filterIsInstance(): List<T> {
    return filterIsInstanceTo(mutableListOf())
}

inline fun measureTimeMillis(action: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    action()
    return System.currentTimeMillis() - startTime
}

inline fun <T> T.applyIf(condition: Boolean, action: T.() -> T): T {
    return if (condition) action() else this
}

// Usage
val user = createInstance<User>()
val executionTime = measureTimeMillis {
    // Some expensive operation
    Thread.sleep(1000)
}
```

### 2. Reified Type Parameters

```kotlin
inline fun <reified T> Any.castOrNull(): T? {
    return if (this is T) this else null
}

inline fun <reified T> String.toObject(): T? {
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> SharedPreferences.getObject(key: String, defaultValue: T? = null): T? {
    val json = getString(key, null) ?: return defaultValue
    return json.toObject<T>()
}

// Usage
val userJson = """{"id": 1, "name": "John"}"""
val user: User? = userJson.toObject<User>()

val anyObject: Any = "Hello"
val string: String? = anyObject.castOrNull<String>()
```

## Smart Casts and Type Checking

### 1. Smart Casts

```kotlin
fun processUser(user: Any) {
    when (user) {
        is User -> {
            // Smart cast: user is now User type
            println("User: ${user.name}")
            println("Email: ${user.email}")
        }
        is String -> {
            // Smart cast: user is now String type
            println("User string: $user")
        }
        else -> {
            println("Unknown type: ${user::class.simpleName}")
        }
    }
}

fun validateUser(user: Any?): Boolean {
    if (user !is User) return false
    
    // Smart cast: user is now User type
    return user.name.isNotBlank() && user.email.isValidEmail()
}
```

### 2. Type-Safe Builders

```kotlin
class UserBuilder {
    private var id: Int = 0
    private var name: String = ""
    private var email: String = ""
    
    fun id(id: Int) = apply { this.id = id }
    fun name(name: String) = apply { this.name = name }
    fun email(email: String) = apply { this.email = email }
    
    fun build(): User = User(id, name, email, "", Date())
}

fun user(init: UserBuilder.() -> Unit): User {
    return UserBuilder().apply(init).build()
}

// Usage
val user = user {
    id(1)
    name("John Doe")
    email("john@example.com")
}
```

## Best Practices

1. **Use Sealed Classes**: For state management and error handling
2. **Leverage Delegation**: For property management and lazy initialization
3. **Use Extension Functions**: To add functionality to existing classes
4. **Prefer Data Classes**: For immutable data structures
5. **Use Inline Functions**: For performance-critical code
6. **Smart Casts**: For type-safe operations
7. **Coroutines and Flow**: For asynchronous operations
8. **Reified Types**: When you need type information at runtime

## Summary

Advanced Kotlin features provide:

- ✅ **Type-safe** state management
- ✅ **Concise** and readable code
- ✅ **Performance** optimizations
- ✅ **Reusable** components
- ✅ **Better** error handling
- ✅ **Modern** async programming

In the next section, we'll explore Design Patterns in Android for better code organization.
