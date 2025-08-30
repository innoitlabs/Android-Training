# Android Testing Step-by-Step Guide

## Quick Start

### 1. Setup Dependencies

First, update your `app/build.gradle.kts` with all necessary testing dependencies:

```kotlin
dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Timber for logging
    implementation("com.jakewharton.timber:timber:5.0.1")
    
    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.7")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("com.google.code.gson:gson:2.10.1")
    
    // Instrumented Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("androidx.recyclerview:recyclerview:1.3.2")
}
```

### 2. Configure Jacoco for Coverage

Add to your `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("jacoco")
}

android {
    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
    }
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

## Testing Workflow

### Step 1: Write Unit Tests

1. **Create Calculator class** (`app/src/main/java/com/example/testingqa/utils/Calculator.kt`)
2. **Write CalculatorTest** (`app/src/test/java/com/example/testingqa/CalculatorTest.kt`)
3. **Run tests**: `./gradlew test`

### Step 2: Test ViewModels

1. **Create UserViewModel** (`app/src/main/java/com/example/testingqa/ui/UserViewModel.kt`)
2. **Write UserViewModelTest** (`app/src/test/java/com/example/testingqa/UserViewModelTest.kt`)
3. **Run tests**: `./gradlew test`

### Step 3: UI Testing

1. **Create MainActivity** (`app/src/main/java/com/example/testingqa/MainActivity.kt`)
2. **Write MainActivityTest** (`app/src/androidTest/java/com/example/testingqa/MainActivityTest.kt`)
3. **Run tests**: `./gradlew connectedAndroidTest`

### Step 4: Integration Testing

1. **Create Room Database** (`app/src/main/java/com/example/testingqa/data/`)
2. **Write UserDaoTest** (`app/src/androidTest/java/com/example/testingqa/UserDaoTest.kt`)
3. **Create API Service** (`app/src/main/java/com/example/testingqa/data/ApiService.kt`)
4. **Write ApiServiceTest** (`app/src/test/java/com/example/testingqa/ApiServiceTest.kt`)

## Common Testing Patterns

### 1. Given-When-Then Pattern

```kotlin
@Test
fun testUserCreation() = runTest {
    // Given
    val newUser = User(id = 0, name = "Alice", email = "alice@example.com")
    coEvery { repository.createUser(newUser) } returns newUser.copy(id = 1)
    
    // When
    val result = viewModel.createUser(newUser)
    
    // Then
    assertEquals(1, result.id)
    coVerify { repository.createUser(newUser) }
}
```

### 2. Test Data Builders

```kotlin
object TestData {
    fun createUser(
        id: Long = 1L,
        name: String = "Test User",
        email: String = "test@example.com"
    ) = User(id = id, name = name, email = email)
    
    fun createUserList(count: Int = 3) = (1..count).map { 
        createUser(id = it.toLong(), name = "User$it") 
    }
}
```

### 3. Test Rules

```kotlin
@get:Rule
val instantExecutorRule = InstantTaskExecutorRule()

@get:Rule
val mainDispatcherRule = MainDispatcherRule()
```

## Running Tests

### Command Line

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.example.testingqa.CalculatorTest"

# Run specific test method
./gradlew test --tests "com.example.testingqa.CalculatorTest.testAddition"

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate coverage report
./gradlew testDebugUnitTestCoverage

# Generate Jacoco report
./gradlew jacocoTestReport
```

### Android Studio

1. Right-click on test file → "Run"
2. Right-click on test method → "Run"
3. View → Tool Windows → Run
4. Select test configuration and run

## Debugging Tests

### Common Issues

1. **LiveData not updating in tests**
   - Use `InstantTaskExecutorRule()`
   - Use `getOrAwaitValue()` extension

2. **Coroutines in tests**
   - Use `runTest` instead of `runBlocking`
   - Use `TestCoroutineDispatcher`

3. **Room database tests**
   - Use `Room.inMemoryDatabaseBuilder()`
   - Call `db.close()` in `@After`

4. **MockK issues**
   - Use `coEvery` for suspend functions
   - Use `every` for regular functions
   - Use `coVerify` to verify calls

### Debug Commands

```bash
# View test logs
./gradlew test --info

# Run tests with debug output
./gradlew test --debug

# View connected device logs
adb logcat

# Clear test cache
./gradlew cleanTest
```

## Best Practices

### 1. Test Organization

```
test/
├── java/com/example/testingqa/
│   ├── unit/           # Unit tests
│   ├── integration/    # Integration tests
│   └── utils/          # Test utilities
```

### 2. Naming Conventions

```kotlin
@Test
fun `when user is created then user id is assigned`() = runTest {
    // test implementation
}

@Test
fun `given invalid email when creating user then error is returned`() = runTest {
    // test implementation
}
```

### 3. Test Data Management

```kotlin
@Before
fun setup() {
    // Setup test data
}

@After
fun cleanup() {
    // Cleanup test data
}
```

### 4. Assertions

```kotlin
// Use descriptive assertions
assertThat(result).isEqualTo(expected)
assertThat(result).isNotNull()
assertThat(result).hasSize(3)
assertThat(result).contains(expectedItem)
```

## Coverage Goals

- **Unit Tests**: 80%+ line coverage
- **Integration Tests**: 70%+ line coverage
- **UI Tests**: Critical user flows
- **Overall**: 75%+ combined coverage

## CI/CD Integration

### GitHub Actions

```yaml
name: Android Tests

on: [push, pull_request]

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
    
    - name: Run Unit Tests
      run: ./gradlew test
    
    - name: Generate Coverage
      run: ./gradlew testDebugUnitTestCoverage
    
    - name: Upload Coverage
      uses: codecov/codecov-action@v3
```

## Performance Testing

### Database Performance

```kotlin
@Test
fun testDatabaseInsertPerformance() = runBlocking {
    val startTime = System.currentTimeMillis()
    
    repeat(1000) { index ->
        userDao.insert(TestData.createUser(id = index.toLong()))
    }
    
    val duration = System.currentTimeMillis() - startTime
    assertTrue("Insert took too long: ${duration}ms", duration < 2000)
}
```

### API Performance

```kotlin
@Test
fun testApiResponseTime() = runBlocking {
    val startTime = System.currentTimeMillis()
    
    val response = apiService.getUsers()
    
    val duration = System.currentTimeMillis() - startTime
    assertTrue("API call took too long: ${duration}ms", duration < 1000)
    assertNotNull(response)
}
```

## Troubleshooting

### Common Errors

1. **"No tests found"**
   - Check test class naming (ends with "Test")
   - Check test method naming (starts with "test")
   - Check package structure

2. **"Method not mocked"**
   - Add missing MockK setup
   - Use `relaxed = true` for partial mocking

3. **"LiveData not observed"**
   - Use `InstantTaskExecutorRule()`
   - Use `getOrAwaitValue()` extension

4. **"Database locked"**
   - Close database in `@After`
   - Use in-memory database for tests

### Debug Tips

1. Use `Timber` for logging in tests
2. Add `@Test(timeout = 5000)` for long-running tests
3. Use `@Ignore` to skip failing tests temporarily
4. Check test reports in `app/build/reports/`

---

This guide provides a comprehensive approach to Android testing. Follow the steps sequentially and refer to the main README for detailed examples.
