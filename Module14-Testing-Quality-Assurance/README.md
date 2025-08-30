# Android Testing & QA Best Practices

## Learning Objectives

By the end of this lesson, learners will be able to:
- Write unit tests with JUnit & MockK
- Test ViewModels and LiveData
- Perform UI tests with Espresso
- Run integration tests with Room and Retrofit
- Apply TDD (Test-Driven Development) practices
- Use mocking & test doubles effectively
- Measure test coverage & quality metrics
- Automate testing in CI/CD pipelines
- Run performance testing & profiling
- Debug and troubleshoot effectively

## Table of Contents

1. [Unit Testing with JUnit & MockK](#unit-testing-with-junit--mockk)
2. [Testing ViewModels & LiveData](#testing-viewmodels--livedata)
3. [UI Testing with Espresso](#ui-testing-with-espresso)
4. [Integration Testing with Room & Retrofit](#integration-testing-with-room--retrofit)
5. [TDD Practices](#tdd-practices)
6. [Mocking & Test Doubles](#mocking--test-doubles)
7. [Test Coverage & Quality Metrics](#test-coverage--quality-metrics)
8. [Continuous Integration Testing](#continuous-integration-testing)
9. [Performance Testing & Profiling](#performance-testing--profiling)
10. [Debugging & Troubleshooting](#debugging--troubleshooting)
11. [Hands-on Lab / Mini Project](#hands-on-lab--mini-project)
12. [Exercises](#exercises)

## 1. Unit Testing with JUnit & MockK

### Dependencies
```kotlin
testImplementation "junit:junit:4.13.2"
testImplementation "io.mockk:mockk:1.13.7"
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
testImplementation "androidx.arch.core:core-testing:2.2.0"
```

### Example Test
```kotlin
class Calculator {
    fun add(a: Int, b: Int) = a + b
    fun multiply(a: Int, b: Int) = a * b
    fun divide(a: Int, b: Int): Double {
        require(b != 0) { "Division by zero is not allowed" }
        return a.toDouble() / b
    }
}

class CalculatorTest {
    private val calculator = Calculator()

    @Test
    fun testAddition() {
        assertEquals(4, calculator.add(2, 2))
        assertEquals(0, calculator.add(-2, 2))
        assertEquals(-4, calculator.add(-2, -2))
    }

    @Test
    fun testMultiplication() {
        assertEquals(4, calculator.multiply(2, 2))
        assertEquals(0, calculator.multiply(0, 5))
        assertEquals(-6, calculator.multiply(2, -3))
    }

    @Test
    fun testDivision() {
        assertEquals(2.0, calculator.divide(4, 2), 0.001)
        assertEquals(0.5, calculator.divide(1, 2), 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDivisionByZero() {
        calculator.divide(5, 0)
    }
}
```

## 2. Testing ViewModels & LiveData

### Dependencies
```kotlin
testImplementation "androidx.arch.core:core-testing:2.2.0"
testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
```

### Example Test
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
    fun testLoadUserName() = runTest {
        // Given
        val expectedName = "Alice"
        coEvery { repository.getUserName() } returns expectedName

        // When
        viewModel.loadUserName()

        // Then
        assertEquals(expectedName, viewModel.userName.getOrAwaitValue())
    }

    @Test
    fun testLoadUserNameError() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repository.getUserName() } throws Exception(errorMessage)

        // When
        viewModel.loadUserName()

        // Then
        assertEquals(errorMessage, viewModel.error.getOrAwaitValue())
    }
}
```

## 3. UI Testing with Espresso

### Dependencies
```kotlin
androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
androidTestImplementation "androidx.test.ext:junit:1.1.5"
androidTestImplementation "androidx.test:runner:1.5.2"
androidTestImplementation "androidx.test:rules:1.5.0"
```

### Example Test
```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testButtonClickChangesText() {
        // Click the button
        onView(withId(R.id.button))
            .perform(click())
        
        // Verify text changed
        onView(withId(R.id.textView))
            .check(matches(withText("Hello")))
    }

    @Test
    fun testInputFieldAndSubmit() {
        // Type in input field
        onView(withId(R.id.editText))
            .perform(typeText("Test User"), closeSoftKeyboard())
        
        // Click submit button
        onView(withId(R.id.submitButton))
            .perform(click())
        
        // Verify result
        onView(withId(R.id.resultText))
            .check(matches(withText("Hello Test User")))
    }

    @Test
    fun testRecyclerViewItemClick() {
        // Click on first item in RecyclerView
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<UserAdapter.ViewHolder>(
                    0, click()
                )
            )
        
        // Verify detail activity opened
        intended(hasComponent(UserDetailActivity::class.java.name))
    }
}
```

## 4. Integration Testing with Room & Retrofit

### Room DAO Test
```kotlin
@RunWith(AndroidJUnit4::class)
class UserDaoTest {
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
    fun insertAndRetrieveUser() = runBlocking {
        // Given
        val user = User(id = 1, name = "Alice", email = "alice@example.com")

        // When
        userDao.insert(user)
        val retrievedUser = userDao.getUserById(1)

        // Then
        assertNotNull(retrievedUser)
        assertEquals(user.name, retrievedUser.name)
        assertEquals(user.email, retrievedUser.email)
    }

    @Test
    fun deleteUser() = runBlocking {
        // Given
        val user = User(id = 1, name = "Alice", email = "alice@example.com")
        userDao.insert(user)

        // When
        userDao.delete(user)
        val retrievedUser = userDao.getUserById(1)

        // Then
        assertNull(retrievedUser)
    }
}
```

### Retrofit MockWebServer Test
```kotlin
class ApiServiceTest {
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
    fun testGetUsers() = runBlocking {
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
    fun testCreateUser() = runBlocking {
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
}
```

## 5. TDD Practices

### Test-Driven Development Cycle
1. **Red**: Write a failing test
2. **Green**: Write minimal code to make the test pass
3. **Refactor**: Improve the code while keeping tests green

### Example TDD Workflow
```kotlin
// Step 1: Write failing test
@Test
fun testStringReverser() {
    val reverser = StringReverser()
    assertEquals("cba", reverser.reverse("abc"))
}

// Step 2: Implement minimal code
class StringReverser {
    fun reverse(input: String): String {
        return input.reversed()
    }
}

// Step 3: Add more tests and refactor
@Test
fun testEmptyString() {
    val reverser = StringReverser()
    assertEquals("", reverser.reverse(""))
}

@Test
fun testSingleCharacter() {
    val reverser = StringReverser()
    assertEquals("a", reverser.reverse("a"))
}
```

## 6. Mocking & Test Doubles

### Types of Test Doubles
- **Stub**: Pre-defined return values
- **Mock**: Verify interactions
- **Fake**: Working implementation for testing
- **Spy**: Real object with some mocked methods

### MockK Examples
```kotlin
class UserServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @Before
    fun setup() {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    @Test
    fun testGetUserById() = runTest {
        // Given
        val userId = 1L
        val expectedUser = User(id = userId, name = "Alice", email = "alice@example.com")
        coEvery { userRepository.getUserById(userId) } returns expectedUser

        // When
        val result = userService.getUserById(userId)

        // Then
        assertEquals(expectedUser, result)
        coVerify { userRepository.getUserById(userId) }
    }

    @Test
    fun testCreateUser() = runTest {
        // Given
        val newUser = User(id = 0, name = "Bob", email = "bob@example.com")
        val createdUser = User(id = 2, name = "Bob", email = "bob@example.com")
        coEvery { userRepository.createUser(newUser) } returns createdUser

        // When
        val result = userService.createUser(newUser)

        // Then
        assertEquals(createdUser, result)
        coVerify { userRepository.createUser(newUser) }
    }
}
```

## 7. Test Coverage & Quality Metrics

### Jacoco Configuration
```kotlin
// In app/build.gradle.kts
android {
    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
    }
}

// Jacoco plugin
plugins {
    id("jacoco")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )
    
    val debugTree = fileTree("${buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    
    val mainSrc = "${project.projectDir}/src/main/java"
    
    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(buildDir) {
        include("/jacoco/testDebugUnitTest.exec")
    })
}
```

### Running Coverage
```bash
./gradlew testDebugUnitTestCoverage
./gradlew jacocoTestReport
```

## 8. Continuous Integration Testing

### GitHub Actions Example
```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    
    - name: Run Unit Tests
      run: ./gradlew test
    
    - name: Run Instrumented Tests
      run: ./gradlew connectedAndroidTest
    
    - name: Generate Test Coverage
      run: ./gradlew testDebugUnitTestCoverage
    
    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        file: ./app/build/reports/jacoco/testDebugUnitTestCoverage/testDebugUnitTestCoverage.xml
```

## 9. Performance Testing & Profiling

### Performance Test Example
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
        
        users.forEach { user ->
            userDao.insert(user)
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        // Should complete within 2 seconds
        assertTrue("Database insert took too long: ${duration}ms", duration < 2000)
        
        db.close()
    }
    
    @Test
    fun testCoroutinePerformance() = runBlocking {
        val startTime = System.currentTimeMillis()
        
        val deferreds = (1..100).map {
            async {
                delay(10) // Simulate work
                it * 2
            }
        }
        
        val results = deferreds.awaitAll()
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        // Should complete within 500ms
        assertTrue("Coroutine execution took too long: ${duration}ms", duration < 500)
        assertEquals(100, results.size)
    }
}
```

## 10. Debugging & Troubleshooting

### Logging with Timber
```kotlin
class UserRepository(private val apiService: ApiService) {
    
    suspend fun getUserById(id: Long): User? {
        return try {
            Timber.d("Fetching user with id: $id")
            val user = apiService.getUserById(id)
            Timber.d("Successfully fetched user: ${user.name}")
            user
        } catch (e: Exception) {
            Timber.e(e, "Failed to fetch user with id: $id")
            null
        }
    }
}
```

### Debug Configuration
```kotlin
// In Application class
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
```

### Common Debugging Commands
```bash
# View logs
adb logcat

# Filter logs by tag
adb logcat -s MyTag

# Clear logs
adb logcat -c

# Run specific test
./gradlew test --tests "com.example.testingqa.CalculatorTest.testAddition"

# Run tests with coverage
./gradlew testDebugUnitTestCoverage
```

## 11. Hands-on Lab / Mini Project

### User Directory App Features
- Add new users
- Search users by name
- View user details
- Edit user information
- Delete users

### Testing Requirements
- Unit tests for business logic
- ViewModel + LiveData tests
- Espresso UI tests for add/search user
- Room integration tests
- Retrofit integration test with MockWebServer

### Project Structure
```
app/src/
├── main/
│   ├── java/com/example/testingqa/
│   │   ├── data/
│   │   │   ├── User.kt
│   │   │   ├── UserDao.kt
│   │   │   ├── UserRepository.kt
│   │   │   └── ApiService.kt
│   │   ├── ui/
│   │   │   ├── MainActivity.kt
│   │   │   ├── UserViewModel.kt
│   │   │   └── UserAdapter.kt
│   │   └── utils/
│   │       └── Calculator.kt
│   └── res/
│       └── layout/
│           ├── activity_main.xml
│           └── item_user.xml
├── test/
│   └── java/com/example/testingqa/
│       ├── CalculatorTest.kt
│       ├── UserViewModelTest.kt
│       ├── UserRepositoryTest.kt
│       └── ApiServiceTest.kt
└── androidTest/
    └── java/com/example/testingqa/
        ├── MainActivityTest.kt
        └── UserDaoTest.kt
```

## 12. Exercises

### Easy Level
Write a unit test for a string utility function that:
- Reverses a string
- Handles empty strings
- Handles single characters
- Handles special characters

### Intermediate Level
Write Espresso test for login form validation that:
- Tests empty username/password
- Tests invalid email format
- Tests successful login
- Tests error messages display

### Advanced Level
Test Retrofit API with MockWebServer + Room DB sync that:
- Mocks API responses
- Tests database operations
- Verifies data synchronization
- Tests error handling and retry logic

## Summary

### Key Testing Concepts
- **JUnit & MockK** → Unit testing with mocking
- **ViewModels & LiveData** → Lifecycle-aware testing
- **Espresso** → UI testing and user interactions
- **Room + Retrofit** → Integration testing
- **TDD** → Improves design & confidence
- **Mocking** → Isolates dependencies
- **CI pipelines** → Automate QA processes
- **Profiling** → Ensure performance standards
- **Debugging** → Fix issues faster with logs & tools

### Best Practices
1. Write tests first (TDD)
2. Keep tests simple and focused
3. Use descriptive test names
4. Mock external dependencies
5. Test edge cases and error scenarios
6. Maintain high test coverage
7. Automate testing in CI/CD
8. Monitor performance metrics
9. Use proper logging for debugging
10. Regular test maintenance

### Next Steps
1. Build and run the project in Android Studio
2. Execute all test suites
3. Generate coverage reports
4. Set up CI/CD pipeline
5. Practice with the provided exercises
6. Apply these concepts to your own projects

---

**Note**: This learning material is designed for Android developers with basic Kotlin knowledge. The project examples are fully functional and ready to run in Android Studio.
