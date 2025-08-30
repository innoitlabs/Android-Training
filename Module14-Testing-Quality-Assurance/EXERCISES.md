# Android Testing Exercises

## Exercise 1: String Utility Functions (Easy)

### Problem
Create a `StringUtils` class with the following methods and write comprehensive unit tests:

```kotlin
class StringUtils {
    fun reverse(input: String): String
    fun isPalindrome(input: String): Boolean
    fun countVowels(input: String): Int
    fun capitalizeWords(input: String): String
}
```

### Requirements
- Test all methods with various inputs
- Test edge cases (empty string, null, special characters)
- Test performance for large strings
- Achieve 100% code coverage

### Example Test Structure
```kotlin
class StringUtilsTest {
    private val stringUtils = StringUtils()

    @Test
    fun testReverse() {
        assertEquals("cba", stringUtils.reverse("abc"))
        assertEquals("", stringUtils.reverse(""))
        assertEquals("a", stringUtils.reverse("a"))
        assertEquals("!dlroW olleH", stringUtils.reverse("Hello World!"))
    }

    @Test
    fun testIsPalindrome() {
        assertTrue(stringUtils.isPalindrome("racecar"))
        assertTrue(stringUtils.isPalindrome(""))
        assertTrue(stringUtils.isPalindrome("a"))
        assertFalse(stringUtils.isPalindrome("hello"))
    }

    // Add more tests...
}
```

## Exercise 2: Login Form Validation (Intermediate)

### Problem
Create a login form with validation and write Espresso UI tests:

### UI Components
- Email input field
- Password input field
- Login button
- Error message text view
- Success message text view

### Validation Rules
- Email must be valid format
- Password must be at least 6 characters
- Both fields are required

### Test Requirements
```kotlin
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testEmptyFieldsValidation() {
        // Test empty email and password
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.errorMessage))
            .check(matches(withText("Email and password are required")))
    }

    @Test
    fun testInvalidEmailFormat() {
        // Test invalid email format
        onView(withId(R.id.emailInput))
            .perform(typeText("invalid-email"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.errorMessage))
            .check(matches(withText("Please enter a valid email")))
    }

    @Test
    fun testShortPassword() {
        // Test password too short
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput))
            .perform(typeText("123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.errorMessage))
            .check(matches(withText("Password must be at least 6 characters")))
    }

    @Test
    fun testSuccessfulLogin() {
        // Test successful login
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordInput))
            .perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())
        onView(withId(R.id.successMessage))
            .check(matches(withText("Login successful!")))
    }
}
```

## Exercise 3: User Management System (Advanced)

### Problem
Create a complete user management system with Room database, Retrofit API, and comprehensive testing.

### Features
- Add new users
- Search users by name
- Update user information
- Delete users
- Sync with remote API

### Architecture
```
UserRepository (Repository Pattern)
├── UserDao (Room Database)
├── ApiService (Retrofit)
└── UserViewModel (ViewModel)
```

### Test Requirements

#### 1. Unit Tests for Repository
```kotlin
class UserRepositoryTest {
    private lateinit var repository: UserRepository
    private lateinit var userDao: UserDao
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        userDao = mockk()
        apiService = mockk()
        repository = UserRepository(userDao, apiService)
    }

    @Test
    fun testGetUsersFromDatabase() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "Bob", email = "bob@example.com")
        )
        coEvery { userDao.getAllUsers() } returns users

        // When
        val result = repository.getUsers()

        // Then
        assertEquals(users, result)
        coVerify { userDao.getAllUsers() }
    }

    @Test
    fun testSyncUsersFromApi() = runTest {
        // Given
        val apiUsers = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com")
        )
        coEvery { apiService.getUsers() } returns apiUsers
        coEvery { userDao.insertAll(any()) } returns Unit

        // When
        repository.syncUsers()

        // Then
        coVerify { apiService.getUsers() }
        coVerify { userDao.insertAll(apiUsers) }
    }
}
```

#### 2. Integration Tests for Database
```kotlin
@RunWith(AndroidJUnit4::class)
class UserDaoIntegrationTest {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun testUserCRUDOperations() = runBlocking {
        // Create
        val user = User(id = 1, name = "Alice", email = "alice@example.com")
        userDao.insert(user)

        // Read
        val retrievedUser = userDao.getUserById(1)
        assertNotNull(retrievedUser)
        assertEquals("Alice", retrievedUser.name)

        // Update
        val updatedUser = user.copy(name = "Alice Updated")
        userDao.update(updatedUser)
        val updatedRetrievedUser = userDao.getUserById(1)
        assertEquals("Alice Updated", updatedRetrievedUser.name)

        // Delete
        userDao.delete(updatedUser)
        val deletedUser = userDao.getUserById(1)
        assertNull(deletedUser)
    }

    @Test
    fun testSearchUsersByName() = runBlocking {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "Bob", email = "bob@example.com"),
            User(id = 3, name = "Alice Smith", email = "alice.smith@example.com")
        )
        users.forEach { userDao.insert(it) }

        // When
        val aliceUsers = userDao.searchUsersByName("Alice")

        // Then
        assertEquals(2, aliceUsers.size)
        assertTrue(aliceUsers.all { it.name.contains("Alice") })
    }
}
```

#### 3. API Tests with MockWebServer
```kotlin
class ApiServiceIntegrationTest {
    @get:Rule
    val server = MockWebServer()

    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    fun testGetUsersApi() = runBlocking {
        // Given
        val mockResponse = """
            [
                {"id": 1, "name": "Alice", "email": "alice@example.com"},
                {"id": 2, "name": "Bob", "email": "bob@example.com"}
            ]
        """.trimIndent()
        server.enqueue(MockResponse().setBody(mockResponse))

        // When
        val users = apiService.getUsers()

        // Then
        assertEquals(2, users.size)
        assertEquals("Alice", users[0].name)
        assertEquals("Bob", users[1].name)
    }

    @Test
    fun testCreateUserApi() = runBlocking {
        // Given
        val newUser = User(id = 0, name = "Charlie", email = "charlie@example.com")
        val createdUser = User(id = 3, name = "Charlie", email = "charlie@example.com")
        server.enqueue(MockResponse().setBody(Gson().toJson(createdUser)))

        // When
        val result = apiService.createUser(newUser)

        // Then
        assertEquals(3, result.id)
        assertEquals("Charlie", result.name)
    }

    @Test
    fun testApiErrorHandling() = runBlocking {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))

        // When & Then
        assertThrows(HttpException::class.java) {
            runBlocking { apiService.getUsers() }
        }
    }
}
```

#### 4. ViewModel Tests
```kotlin
@get:Rule
val instantExecutorRule = InstantTaskExecutorRule()

class UserViewModelTest {
    private lateinit var viewModel: UserViewModel
    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        repository = mockk()
        viewModel = UserViewModel(repository)
    }

    @Test
    fun testLoadUsers() = runTest {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com")
        )
        coEvery { repository.getUsers() } returns users

        // When
        viewModel.loadUsers()

        // Then
        assertEquals(users, viewModel.users.getOrAwaitValue())
        assertEquals(false, viewModel.isLoading.getOrAwaitValue())
    }

    @Test
    fun testAddUser() = runTest {
        // Given
        val newUser = User(id = 0, name = "Bob", email = "bob@example.com")
        val createdUser = newUser.copy(id = 1)
        coEvery { repository.addUser(newUser) } returns createdUser

        // When
        viewModel.addUser(newUser)

        // Then
        coVerify { repository.addUser(newUser) }
        assertEquals(createdUser, viewModel.lastAddedUser.getOrAwaitValue())
    }

    @Test
    fun testSearchUsers() = runTest {
        // Given
        val searchQuery = "Alice"
        val searchResults = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com")
        )
        coEvery { repository.searchUsers(searchQuery) } returns searchResults

        // When
        viewModel.searchUsers(searchQuery)

        // Then
        assertEquals(searchResults, viewModel.searchResults.getOrAwaitValue())
    }
}
```

#### 5. UI Tests for User Management
```kotlin
@RunWith(AndroidJUnit4::class)
class UserManagementActivityTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(UserManagementActivity::class.java)

    @Test
    fun testAddNewUser() {
        // Click add user button
        onView(withId(R.id.addUserButton)).perform(click())
        
        // Fill user details
        onView(withId(R.id.nameInput))
            .perform(typeText("John Doe"), closeSoftKeyboard())
        onView(withId(R.id.emailInput))
            .perform(typeText("john@example.com"), closeSoftKeyboard())
        
        // Submit
        onView(withId(R.id.saveButton)).perform(click())
        
        // Verify user appears in list
        onView(withId(R.id.userRecyclerView))
            .check(matches(hasDescendant(withText("John Doe"))))
    }

    @Test
    fun testSearchUsers() {
        // Type in search field
        onView(withId(R.id.searchInput))
            .perform(typeText("Alice"), closeSoftKeyboard())
        
        // Verify search results
        onView(withId(R.id.userRecyclerView))
            .check(matches(hasDescendant(withText("Alice"))))
    }

    @Test
    fun testDeleteUser() {
        // Long click on user item
        onView(withId(R.id.userRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(
                    0, longClick()
                )
            )
        
        // Confirm deletion
        onView(withText("Delete"))
            .perform(click())
        onView(withText("OK"))
            .perform(click())
        
        // Verify user is removed
        onView(withId(R.id.userRecyclerView))
            .check(matches(not(hasDescendant(withText("Alice")))))
    }
}
```

## Exercise 4: Performance Testing (Advanced)

### Problem
Create performance tests for the user management system.

### Requirements
```kotlin
class PerformanceTest {
    
    @Test
    fun testDatabaseInsertPerformance() = runBlocking {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        
        val userDao = db.userDao()
        val users = (1..1000).map { 
            User(id = it, name = "User$it", email = "user$it@example.com") 
        }
        
        val startTime = System.currentTimeMillis()
        users.forEach { userDao.insert(it) }
        val duration = System.currentTimeMillis() - startTime
        
        // Should complete within 2 seconds
        assertTrue("Database insert took too long: ${duration}ms", duration < 2000)
        db.close()
    }
    
    @Test
    fun testApiResponseTime() = runBlocking {
        val server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        
        server.enqueue(MockResponse().setBody("[]"))
        
        val startTime = System.currentTimeMillis()
        apiService.getUsers()
        val duration = System.currentTimeMillis() - startTime
        
        // Should complete within 1 second
        assertTrue("API call took too long: ${duration}ms", duration < 1000)
    }
}
```

## Exercise 5: Test Coverage Analysis (Advanced)

### Problem
Analyze and improve test coverage for the user management system.

### Requirements
1. Generate coverage reports
2. Identify uncovered code
3. Write additional tests to achieve 90%+ coverage
4. Document coverage improvements

### Coverage Goals
- **Unit Tests**: 90%+ line coverage
- **Integration Tests**: 80%+ line coverage
- **UI Tests**: Critical user flows covered

### Coverage Analysis
```bash
# Generate coverage report
./gradlew testDebugUnitTestCoverage

# View coverage in browser
open app/build/reports/jacoco/testDebugUnitTestCoverage/html/index.html
```

## Submission Guidelines

### For Each Exercise
1. **Complete Implementation**: All required functionality
2. **Comprehensive Tests**: All test requirements met
3. **Documentation**: Clear comments and README
4. **Code Quality**: Clean, readable, maintainable code
5. **Coverage Report**: Minimum coverage requirements met

### Evaluation Criteria
- **Functionality**: 30%
- **Test Coverage**: 30%
- **Code Quality**: 20%
- **Documentation**: 10%
- **Performance**: 10%

### Bonus Points
- Additional edge case testing
- Performance optimizations
- Accessibility testing
- Security testing
- CI/CD integration

---

Complete these exercises to master Android testing concepts and best practices!
