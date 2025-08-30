# Use Case Pattern & Domain Logic

## Overview

Use Cases (also known as Interactors) represent the application's business logic. They encapsulate the specific actions that a user can perform in the application and coordinate between different domain objects to achieve a specific goal.

## Why Use Cases?

### Problems Without Use Cases
```kotlin
// ❌ Bad: Business logic scattered in ViewModel
class UserViewModel : ViewModel() {
    fun loadUserProfile(id: Int) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUser(id)
                val posts = postRepository.getUserPosts(id)
                val followers = userRepository.getUserFollowers(id)
                
                // Business logic mixed with UI logic
                val isVerified = user.isEmailVerified && user.postsCount > 10
                val canFollow = user.id != currentUserId && !followers.contains(currentUserId)
                
                _userProfile.value = UserProfile(user, posts, followers, isVerified, canFollow)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
```

### Benefits With Use Cases
- ✅ **Single Responsibility**: Each use case has one specific purpose
- ✅ **Testability**: Business logic can be tested independently
- ✅ **Reusability**: Use cases can be reused across different UI components
- ✅ **Maintainability**: Business rules are centralized and easy to modify
- ✅ **Readability**: Clear intent and purpose for each operation

## Use Case Implementation

### 1. Basic Use Case Structure

```kotlin
// Domain Layer - Use Case Interface
interface UseCase<in P, R> {
    suspend operator fun invoke(parameters: P): Result<R>
}

// Domain Layer - Use Case Implementation
class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<Int, User> {
    
    override suspend operator fun invoke(parameters: Int): Result<User> {
        return if (parameters > 0) {
            userRepository.getUser(parameters)
        } else {
            Result.failure(IllegalArgumentException("User ID must be positive"))
        }
    }
}
```

### 2. Complex Use Cases with Multiple Dependencies

```kotlin
// Domain Layer - Complex Use Case
class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository,
    private val currentUserRepository: CurrentUserRepository
) : UseCase<Int, UserProfile> {
    
    override suspend operator fun invoke(userId: Int): Result<UserProfile> {
        return try {
            // Validate input
            if (userId <= 0) {
                return Result.failure(IllegalArgumentException("Invalid user ID"))
            }
            
            // Fetch data in parallel
            val userDeferred = async { userRepository.getUser(userId) }
            val postsDeferred = async { postRepository.getUserPosts(userId) }
            val followersDeferred = async { followRepository.getUserFollowers(userId) }
            val currentUserDeferred = async { currentUserRepository.getCurrentUser() }
            
            // Wait for all results
            val user = userDeferred.await().getOrThrow()
            val posts = postsDeferred.await().getOrThrow()
            val followers = followersDeferred.await().getOrThrow()
            val currentUser = currentUserDeferred.await().getOrThrow()
            
            // Apply business rules
            val isVerified = calculateVerificationStatus(user, posts)
            val canFollow = calculateFollowPermission(user, currentUser, followers)
            val isFollowing = followers.any { it.id == currentUser.id }
            
            val userProfile = UserProfile(
                user = user,
                posts = posts,
                followers = followers,
                isVerified = isVerified,
                canFollow = canFollow,
                isFollowing = isFollowing
            )
            
            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun calculateVerificationStatus(user: User, posts: List<Post>): Boolean {
        return user.isEmailVerified && posts.size >= 10
    }
    
    private fun calculateFollowPermission(
        user: User, 
        currentUser: User, 
        followers: List<User>
    ): Boolean {
        return user.id != currentUser.id && 
               !followers.any { it.id == currentUser.id }
    }
}
```

### 3. Use Cases with Parameters

```kotlin
// Domain Layer - Use Case with Parameters
data class CreateUserParams(
    val name: String,
    val email: String,
    val password: String,
    val avatar: String? = null
)

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator
) : UseCase<CreateUserParams, User> {
    
    override suspend operator fun invoke(params: CreateUserParams): Result<User> {
        return try {
            // Validate input
            validateInput(params)
            
            // Check if user already exists
            val existingUser = userRepository.getUserByEmail(params.email)
            if (existingUser.isSuccess) {
                return Result.failure(UserAlreadyExistsException("User with this email already exists"))
            }
            
            // Create user
            val newUser = User(
                id = 0, // Will be assigned by the database
                name = params.name.trim(),
                email = params.email.lowercase(),
                avatar = params.avatar ?: generateDefaultAvatar(params.name),
                createdAt = Date()
            )
            
            val createdUser = userRepository.createUser(newUser)
            Result.success(createdUser.getOrThrow())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun validateInput(params: CreateUserParams) {
        if (params.name.isBlank()) {
            throw IllegalArgumentException("Name cannot be empty")
        }
        if (!emailValidator.isValid(params.email)) {
            throw IllegalArgumentException("Invalid email format")
        }
        if (!passwordValidator.isValid(params.password)) {
            throw IllegalArgumentException("Password does not meet requirements")
        }
    }
    
    private fun generateDefaultAvatar(name: String): String {
        return "https://ui-avatars.com/api/?name=${name.replace(" ", "+")}&size=200"
    }
}
```

## Domain Models and Business Logic

### 1. Rich Domain Models

```kotlin
// Domain Layer - Rich Domain Model
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Date,
    val isEmailVerified: Boolean = false,
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0
) {
    
    // Business logic methods
    fun canBeFollowedBy(otherUser: User): Boolean {
        return id != otherUser.id && !isEmailVerified.not()
    }
    
    fun isActive(): Boolean {
        val daysSinceCreation = (Date().time - createdAt.time) / (1000 * 60 * 60 * 24)
        return daysSinceCreation <= 30 || postsCount > 0
    }
    
    fun getDisplayName(): String {
        return if (isEmailVerified) "$name ✓" else name
    }
    
    fun getProfileUrl(): String {
        return "/users/$id"
    }
}
```

### 2. Value Objects

```kotlin
// Domain Layer - Value Objects
@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "Email cannot be empty" }
        require(value.contains("@")) { "Invalid email format" }
    }
    
    fun getDomain(): String {
        return value.substringAfter("@")
    }
    
    fun isCorporate(): Boolean {
        return !getDomain().contains("gmail.com") && 
               !getDomain().contains("yahoo.com") &&
               !getDomain().contains("hotmail.com")
    }
}

@JvmInline
value class Password(val value: String) {
    init {
        require(value.length >= 8) { "Password must be at least 8 characters" }
        require(value.any { it.isUpperCase() }) { "Password must contain uppercase letter" }
        require(value.any { it.isDigit() }) { "Password must contain digit" }
    }
    
    fun getStrength(): PasswordStrength {
        return when {
            value.length >= 12 && value.any { !it.isLetterOrDigit() } -> PasswordStrength.STRONG
            value.length >= 10 -> PasswordStrength.MEDIUM
            else -> PasswordStrength.WEAK
        }
    }
}

enum class PasswordStrength {
    WEAK, MEDIUM, STRONG
}
```

## Use Case Categories

### 1. Query Use Cases (Read Operations)

```kotlin
// Domain Layer - Query Use Cases
class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<SearchUsersParams, List<User>> {
    
    override suspend operator fun invoke(params: SearchUsersParams): Result<List<User>> {
        return try {
            if (params.query.length < 2) {
                return Result.success(emptyList())
            }
            
            val users = userRepository.searchUsers(params.query)
            val filteredUsers = users.getOrThrow()
                .filter { it.isActive() }
                .sortedBy { it.name }
                .take(params.limit)
            
            Result.success(filteredUsers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class SearchUsersParams(
    val query: String,
    val limit: Int = 20
)
```

### 2. Command Use Cases (Write Operations)

```kotlin
// Domain Layer - Command Use Cases
class FollowUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    private val currentUserRepository: CurrentUserRepository,
    private val notificationService: NotificationService
) : UseCase<Int, Unit> {
    
    override suspend operator fun invoke(userId: Int): Result<Unit> {
        return try {
            val currentUser = currentUserRepository.getCurrentUser().getOrThrow()
            val targetUser = userRepository.getUser(userId).getOrThrow()
            
            // Business rules
            if (currentUser.id == targetUser.id) {
                return Result.failure(IllegalArgumentException("Cannot follow yourself"))
            }
            
            if (!targetUser.canBeFollowedBy(currentUser)) {
                return Result.failure(IllegalArgumentException("Cannot follow this user"))
            }
            
            // Check if already following
            val isAlreadyFollowing = followRepository.isFollowing(currentUser.id, userId)
            if (isAlreadyFollowing) {
                return Result.failure(AlreadyFollowingException("Already following this user"))
            }
            
            // Create follow relationship
            followRepository.followUser(currentUser.id, userId)
            
            // Send notification
            notificationService.sendFollowNotification(targetUser.id, currentUser.name)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## Error Handling in Use Cases

### 1. Domain Exceptions

```kotlin
// Domain Layer - Domain Exceptions
sealed class DomainException(message: String) : Exception(message) {
    class UserNotFoundException(message: String) : DomainException(message)
    class UserAlreadyExistsException(message: String) : DomainException(message)
    class AlreadyFollowingException(message: String) : DomainException(message)
    class ValidationException(message: String) : DomainException(message)
    class BusinessRuleViolationException(message: String) : DomainException(message)
}

// Enhanced Use Case with Domain Exceptions
class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val emailValidator: EmailValidator
) : UseCase<UpdateUserProfileParams, User> {
    
    override suspend operator fun invoke(params: UpdateUserProfileParams): Result<User> {
        return try {
            // Validate input
            validateInput(params)
            
            // Get existing user
            val existingUser = userRepository.getUser(params.userId)
                .getOrThrow()
            
            // Check business rules
            if (params.email != existingUser.email) {
                val emailExists = userRepository.getUserByEmail(params.email).isSuccess
                if (emailExists) {
                    return Result.failure(DomainException.UserAlreadyExistsException("Email already in use"))
                }
            }
            
            // Update user
            val updatedUser = existingUser.copy(
                name = params.name,
                email = params.email,
                avatar = params.avatar ?: existingUser.avatar
            )
            
            val result = userRepository.updateUser(updatedUser)
            Result.success(result.getOrThrow())
        } catch (e: DomainException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(DomainException.ValidationException("Update failed: ${e.message}"))
        }
    }
    
    private fun validateInput(params: UpdateUserProfileParams) {
        if (params.name.isBlank()) {
            throw DomainException.ValidationException("Name cannot be empty")
        }
        if (!emailValidator.isValid(params.email)) {
            throw DomainException.ValidationException("Invalid email format")
        }
    }
}

data class UpdateUserProfileParams(
    val userId: Int,
    val name: String,
    val email: String,
    val avatar: String? = null
)
```

## Testing Use Cases

### 1. Unit Tests

```kotlin
@RunWith(MockitoJUnitRunner::class)
class GetUserProfileUseCaseTest {
    
    @Mock
    private lateinit var userRepository: UserRepository
    
    @Mock
    private lateinit var postRepository: PostRepository
    
    @Mock
    private lateinit var followRepository: FollowRepository
    
    @Mock
    private lateinit var currentUserRepository: CurrentUserRepository
    
    private lateinit var useCase: GetUserProfileUseCase
    
    @Before
    fun setup() {
        useCase = GetUserProfileUseCase(
            userRepository, 
            postRepository, 
            followRepository, 
            currentUserRepository
        )
    }
    
    @Test
    fun `invoke returns user profile when all data is available`() = runTest {
        // Given
        val userId = 1
        val user = User(1, "John", "john@example.com", "", Date())
        val posts = listOf(Post(1, "Hello", userId))
        val followers = listOf(User(2, "Jane", "jane@example.com", "", Date()))
        val currentUser = User(3, "Bob", "bob@example.com", "", Date())
        
        whenever(userRepository.getUser(userId)).thenReturn(Result.success(user))
        whenever(postRepository.getUserPosts(userId)).thenReturn(Result.success(posts))
        whenever(followRepository.getUserFollowers(userId)).thenReturn(Result.success(followers))
        whenever(currentUserRepository.getCurrentUser()).thenReturn(Result.success(currentUser))
        
        // When
        val result = useCase(userId)
        
        // Then
        assertTrue(result.isSuccess)
        val userProfile = result.getOrNull()
        assertEquals(user, userProfile?.user)
        assertEquals(posts, userProfile?.posts)
        assertEquals(followers, userProfile?.followers)
        assertTrue(userProfile?.canFollow == true)
    }
    
    @Test
    fun `invoke returns failure when user not found`() = runTest {
        // Given
        val userId = 999
        whenever(userRepository.getUser(userId)).thenReturn(Result.failure(Exception("User not found")))
        
        // When
        val result = useCase(userId)
        
        // Then
        assertTrue(result.isFailure)
    }
}
```

## Best Practices

1. **Single Responsibility**: Each use case should have one specific purpose
2. **Dependency Injection**: Use Hilt to inject dependencies
3. **Error Handling**: Use domain-specific exceptions
4. **Validation**: Validate input parameters early
5. **Business Logic**: Keep business rules in domain models
6. **Testing**: Write comprehensive unit tests
7. **Naming**: Use descriptive names that indicate the action
8. **Documentation**: Document complex business logic

## Summary

Use Cases provide:

- ✅ **Clear business logic** separation
- ✅ **Testable** domain operations
- ✅ **Reusable** business operations
- ✅ **Maintainable** code structure
- ✅ **Domain-driven** design approach

In the next section, we'll explore Modular App Architecture and how to structure large applications into manageable modules.
