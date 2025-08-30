# Testing Guide for MVVM Architecture

## Table of Contents
1. [Testing Dependencies](#testing-dependencies)
2. [Testing ViewModels](#testing-viewmodels)
3. [Testing LiveData](#testing-livedata)
4. [Testing Repositories](#testing-repositories)
5. [Testing Data Binding](#testing-data-binding)
6. [Integration Testing](#integration-testing)
7. [UI Testing](#ui-testing)
8. [Best Practices](#best-practices)

## Testing Dependencies

Add these dependencies to your `app/build.gradle.kts`:

```kotlin
dependencies {
    // Core Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    
    // Coroutines Testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    
    // Mocking
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    
    // Truth Assertions
    testImplementation("com.google.truth:truth:1.1.5")
    
    // Android Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    
    // Hilt Testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48")
}
```

## Testing ViewModels

### Basic ViewModel Test Setup

```kotlin
@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var repository: UserRepository
    
    private lateinit var viewModel: UserViewModel
    
    @Before
    fun setup() {
        viewModel = UserViewModel(repository)
    }
    
    @Test
    fun `initial state should be loading`() {
        // Given
        val expectedState = UiState.Loading
        
        // When
        val actualState = viewModel.uiState.getOrAwaitValue()
        
        // Then
        assertEquals(expectedState, actualState)
    }
}
```

### Testing Success Scenarios

```kotlin
@Test
fun `loadUsers should emit loading then success state`() = runTest {
    // Given
    val users = listOf(
        User(1, "John Doe", "john@example.com"),
        User(2, "Jane Smith", "jane@example.com")
    )
    whenever(repository.getUsers()).thenReturn(Result.success(users))
    
    // When
    viewModel.loadUsers()
    
    // Then
    val states = mutableListOf<UiState<List<User>>>()
    viewModel.uiState.observeForever { states.add(it) }
    
    assertEquals(2, states.size)
    assertTrue(states[0] is UiState.Loading)
    assertTrue(states[1] is UiState.Success)
    assertEquals(users, (states[1] as UiState.Success).data)
}
```

### Testing Error Scenarios

```kotlin
@Test
fun `loadUsers should emit error state on failure`() = runTest {
    // Given
    val error = IOException("Network error")
    whenever(repository.getUsers()).thenReturn(Result.failure(error))
    
    // When
    viewModel.loadUsers()
    
    // Then
    val states = mutableListOf<UiState<List<User>>>()
    viewModel.uiState.observeForever { states.add(it) }
    
    assertEquals(2, states.size)
    assertTrue(states[0] is UiState.Loading)
    assertTrue(states[1] is UiState.Error)
    assertEquals("Network error. Please check your connection.", (states[1] as UiState.Error).message)
}
```

### Testing with MockK

```kotlin
@RunWith(MockKJUnitRunner::class)
class UserViewModelMockKTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val repository: UserRepository = mockk()
    private lateinit var viewModel: UserViewModel
    
    @Before
    fun setup() {
        viewModel = UserViewModel(repository)
    }
    
    @Test
    fun `loadUsers should handle network error`() = runTest {
        // Given
        coEvery { repository.getUsers() } throws IOException("Network error")
        
        // When
        viewModel.loadUsers()
        
        // Then
        val finalState = viewModel.uiState.getOrAwaitValue()
        assertTrue(finalState is UiState.Error)
        assertEquals("Network error. Please check your connection.", (finalState as UiState.Error).message)
    }
}
```

## Testing LiveData

### LiveData Test Extension

```kotlin
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    afterObserve.invoke()
    
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }
    
    @Suppress("UNCHECKED_CAST")
    return data as T
}
```

### Testing LiveData Transformations

```kotlin
@Test
fun `userDisplayName should transform userName correctly`() {
    // Given
    val userName = MutableLiveData<String>()
    val userDisplayName = Transformations.map(userName) { name ->
        "Welcome, $name!"
    }
    
    // When
    userName.value = "John"
    
    // Then
    assertEquals("Welcome, John!", userDisplayName.getOrAwaitValue())
}
```

### Testing MediatorLiveData

```kotlin
@Test
fun `combinedLiveData should combine two sources`() {
    // Given
    val source1 = MutableLiveData<String>()
    val source2 = MutableLiveData<String>()
    val combined = MediatorLiveData<String>()
    
    combined.addSource(source1) { value1 ->
        val value2 = source2.value
        if (value1 != null && value2 != null) {
            combined.value = "$value1 $value2"
        }
    }
    
    combined.addSource(source2) { value2 ->
        val value1 = source1.value
        if (value1 != null && value2 != null) {
            combined.value = "$value1 $value2"
        }
    }
    
    // When
    source1.value = "Hello"
    source2.value = "World"
    
    // Then
    assertEquals("Hello World", combined.getOrAwaitValue())
}
```

## Testing Repositories

### Repository Test with Mock API

```kotlin
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {
    
    @Mock
    private lateinit var apiService: ApiService
    
    @Mock
    private lateinit var userDao: UserDao
    
    private lateinit var repository: UserRepository
    
    @Before
    fun setup() {
        repository = UserRepository(apiService, userDao)
    }
    
    @Test
    fun `getUsers should return success with API data`() = runTest {
        // Given
        val users = listOf(User(1, "John", "john@example.com"))
        whenever(apiService.getUsers()).thenReturn(users)
        whenever(userDao.getAllUsers()).thenReturn(emptyList())
        
        // When
        val result = repository.getUsers()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(users, result.getOrNull())
        verify(userDao).insertUsers(users)
    }
    
    @Test
    fun `getUsers should return cached data on API failure`() = runTest {
        // Given
        val cachedUsers = listOf(User(1, "John", "john@example.com"))
        whenever(apiService.getUsers()).thenThrow(IOException("Network error"))
        whenever(userDao.getAllUsers()).thenReturn(cachedUsers)
        
        // When
        val result = repository.getUsers()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(cachedUsers, result.getOrNull())
    }
    
    @Test
    fun `getUsers should return failure when no cached data available`() = runTest {
        // Given
        whenever(apiService.getUsers()).thenThrow(IOException("Network error"))
        whenever(userDao.getAllUsers()).thenReturn(emptyList())
        
        // When
        val result = repository.getUsers()
        
        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)
    }
}
```

## Testing Data Binding

### Testing Binding Adapters

```kotlin
@RunWith(AndroidJUnit4::class)
class BindingAdaptersTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Test
    fun `setVisible should set visibility correctly`() {
        // Given
        val context = ApplicationProvider.getApplicationContext<Context>()
        val view = View(context)
        
        // When
        setVisible(view, true)
        
        // Then
        assertEquals(View.VISIBLE, view.visibility)
        
        // When
        setVisible(view, false)
        
        // Then
        assertEquals(View.GONE, view.visibility)
    }
    
    @Test
    fun `loadImage should load image with Glide`() {
        // Given
        val context = ApplicationProvider.getApplicationContext<Context>()
        val imageView = ImageView(context)
        val imageUrl = "https://example.com/image.jpg"
        
        // When
        loadImage(imageView, imageUrl)
        
        // Then
        // Note: In real tests, you might want to use a test image loader
        // or verify that Glide was called with correct parameters
    }
}
```

### Testing Data Binding in Activities

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var userViewModel: UserViewModel
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun `activity should display loading state initially`() {
        // Given
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        
        // When & Then
        scenario.onActivity { activity ->
            val binding = activity.binding
            assertNotNull(binding.viewModel)
            assertEquals(userViewModel, binding.viewModel)
        }
    }
}
```

## Integration Testing

### Testing Complete MVVM Flow

```kotlin
@RunWith(AndroidJUnit4::class)
class MVVMIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var repository: UserRepository
    
    @Inject
    lateinit var viewModel: UserViewModel
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun `complete flow should work from repository to UI`() = runTest {
        // Given
        val users = listOf(User(1, "John", "john@example.com"))
        
        // When
        viewModel.loadUsers()
        
        // Then
        val finalState = viewModel.uiState.getOrAwaitValue()
        assertTrue(finalState is UiState.Success)
        assertEquals(users.size, (finalState as UiState.Success).data.size)
    }
}
```

## UI Testing

### Espresso UI Tests

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityUITest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun `should display loading indicator when loading users`() {
        // Given
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        
        // When
        scenario.onActivity { activity ->
            onView(withId(R.id.loadUsersButton)).perform(click())
        }
        
        // Then
        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))
    }
    
    @Test
    fun `should display users list when loading succeeds`() {
        // Given
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        
        // When
        scenario.onActivity { activity ->
            onView(withId(R.id.loadUsersButton)).perform(click())
        }
        
        // Then
        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }
    
    @Test
    fun `should display error message when loading fails`() {
        // Given
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        
        // When
        scenario.onActivity { activity ->
            // Simulate network error
            onView(withId(R.id.loadUsersButton)).perform(click())
        }
        
        // Then
        onView(withId(R.id.errorText))
            .check(matches(isDisplayed()))
    }
}
```

## Best Practices

### 1. Test Structure (AAA Pattern)
```kotlin
@Test
fun `testName should do something when condition`() {
    // Arrange (Given)
    val input = "test"
    
    // Act (When)
    val result = functionUnderTest(input)
    
    // Assert (Then)
    assertEquals("expected", result)
}
```

### 2. Test Naming
- Use descriptive test names
- Follow the pattern: `methodName_should_expectedBehavior_when_stateUnderTest`
- Example: `loadUsers_should_emitSuccessState_when_apiReturnsData`

### 3. Test Isolation
- Each test should be independent
- Use `@Before` for setup, not `@BeforeClass`
- Clean up resources in `@After`

### 4. Mocking Best Practices
- Mock external dependencies (API, Database)
- Don't mock the class under test
- Use meaningful mock data

### 5. Assertion Best Practices
- Use specific assertions (assertEquals, assertTrue, etc.)
- Test one thing per test method
- Use descriptive assertion messages

### 6. Test Coverage
- Aim for high test coverage (80%+)
- Focus on business logic
- Test edge cases and error scenarios

### 7. Performance Testing
```kotlin
@Test
fun `should complete within reasonable time`() {
    val startTime = System.currentTimeMillis()
    
    // Perform operation
    viewModel.loadUsers()
    
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    
    assertTrue("Operation took too long: ${duration}ms", duration < 5000)
}
```

### 8. Test Data Builders
```kotlin
object TestData {
    fun createUser(
        id: Int = 1,
        name: String = "Test User",
        email: String = "test@example.com"
    ) = User(id, name, email)
    
    fun createUserList(count: Int = 3) = (1..count).map { 
        createUser(id = it, name = "User $it") 
    }
}
```

## Running Tests

### Command Line
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests UserViewModelTest

# Run with coverage
./gradlew testDebugUnitTestCoverage
```

### Android Studio
- Right-click on test file → Run
- Right-click on test method → Run
- Use the Test Runner window for detailed results

---

**Happy Testing! 🧪**
