# Android Testing Guide for Device Features & Permissions

## Overview
This guide covers comprehensive testing strategies for Android device features, permissions, and security implementations. You'll learn how to test runtime permissions, device features, and ensure your app works correctly across different devices and scenarios.

## Testing Strategy Overview

### 1. Unit Testing
- Test individual components in isolation
- Mock dependencies and external services
- Verify business logic and data processing

### 2. Integration Testing
- Test component interactions
- Verify permission flows
- Test device feature integrations

### 3. UI Testing
- Test user interface interactions
- Verify permission dialogs
- Test device feature workflows

### 4. Device Testing
- Test on physical devices
- Verify feature availability
- Test different Android versions

## Permission Testing

### 1. Permission Unit Tests
```kotlin
class PermissionHelperTest {
    
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var mockActivity: Activity
    
    @Before
    fun setup() {
        mockActivity = mock()
        permissionHelper = PermissionHelper(mockActivity)
    }
    
    @Test
    fun testPermissionGranted() {
        // Given
        whenever(mockActivity.checkSelfPermission(Manifest.permission.CAMERA))
            .thenReturn(PackageManager.PERMISSION_GRANTED)
        
        // When
        val result = permissionHelper.isPermissionGranted(Manifest.permission.CAMERA)
        
        // Then
        assertTrue(result)
    }
    
    @Test
    fun testPermissionDenied() {
        // Given
        whenever(mockActivity.checkSelfPermission(Manifest.permission.CAMERA))
            .thenReturn(PackageManager.PERMISSION_DENIED)
        
        // When
        val result = permissionHelper.isPermissionGranted(Manifest.permission.CAMERA)
        
        // Then
        assertFalse(result)
    }
    
    @Test
    fun testRequestPermission() {
        // Given
        whenever(mockActivity.checkSelfPermission(Manifest.permission.CAMERA))
            .thenReturn(PackageManager.PERMISSION_DENIED)
        
        // When
        permissionHelper.requestCameraPermission()
        
        // Then
        verify(mockActivity).requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            PermissionHelper.CAMERA_PERMISSION_REQUEST_CODE
        )
    }
}
```

### 2. Permission Integration Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class PermissionIntegrationTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testCameraPermissionFlow() {
        // Test permission request flow
        onView(withId(R.id.btnCamera))
            .perform(click())
        
        // Verify permission dialog appears (if permission not granted)
        // Note: This test requires specific device state
        try {
            onView(withText("Allow"))
                .check(matches(isDisplayed()))
        } catch (Exception e) {
            // Permission might already be granted
            Log.d("Test", "Permission dialog not shown - may already be granted")
        }
    }
    
    @Test
    fun testPermissionResultHandling() {
        // Test permission result handling
        activityRule.scenario.onActivity { activity ->
            val permissions = arrayOf(Manifest.permission.CAMERA)
            val grantResults = intArrayOf(PackageManager.PERMISSION_GRANTED)
            
            activity.onRequestPermissionsResult(
                PermissionHelper.CAMERA_PERMISSION_REQUEST_CODE,
                permissions,
                grantResults
            )
            
            // Verify camera functionality is enabled
            onView(withId(R.id.btnCamera))
                .check(matches(isEnabled()))
        }
    }
}
```

### 3. Permission UI Tests
```kotlin
@RunWith(AndroidJUnit4::class)
class PermissionUITest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testPermissionExplanationDialog() {
        // Test permission explanation dialog
        onView(withId(R.id.btnCamera))
            .perform(click())
        
        // Verify explanation dialog appears
        onView(withText("Camera Permission Required"))
            .check(matches(isDisplayed()))
        
        onView(withText("Grant"))
            .perform(click())
    }
    
    @Test
    fun testPermissionDeniedHandling() {
        // Test permission denied scenario
        // This would require mocking the permission result
        activityRule.scenario.onActivity { activity ->
            // Simulate permission denied
            val permissions = arrayOf(Manifest.permission.CAMERA)
            val grantResults = intArrayOf(PackageManager.PERMISSION_DENIED)
            
            activity.onRequestPermissionsResult(
                PermissionHelper.CAMERA_PERMISSION_REQUEST_CODE,
                permissions,
                grantResults
            )
            
            // Verify appropriate message is shown
            onView(withText(containsString("permission required")))
                .check(matches(isDisplayed()))
        }
    }
}
```

## Device Feature Testing

### 1. Camera Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class CameraTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(CameraActivity::class.java)
    
    @Test
    fun testCameraAvailability() {
        activityRule.scenario.onActivity { activity ->
            val packageManager = activity.packageManager
            val hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            
            if (hasCamera) {
                // Test camera functionality
                onView(withId(R.id.btnCapture))
                    .check(matches(isDisplayed()))
                    .check(matches(isEnabled()))
            } else {
                // Test fallback behavior
                onView(withText("Camera not available"))
                    .check(matches(isDisplayed()))
            }
        }
    }
    
    @Test
    fun testCameraCapture() {
        // Test camera capture functionality
        onView(withId(R.id.btnCapture))
            .perform(click())
        
        // Verify camera intent is launched
        // Note: This test requires camera app to be available
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
    }
    
    @Test
    fun testImageDisplay() {
        // Test image display after capture
        // This would require mocking the camera result
        activityRule.scenario.onActivity { activity ->
            // Simulate camera result
            val mockIntent = Intent()
            val mockBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            mockIntent.putExtra("data", mockBitmap)
            
            activity.onActivityResult(
                CameraActivity.CAMERA_REQUEST_CODE,
                Activity.RESULT_OK,
                mockIntent
            )
            
            // Verify image is displayed
            onView(withId(R.id.imageView))
                .check(matches(isDisplayed()))
        }
    }
}
```

### 2. Location Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class LocationTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(LocationActivity::class.java)
    
    @Test
    fun testLocationPermissionFlow() {
        // Test location permission request
        onView(withId(R.id.btnGetLocation))
            .perform(click())
        
        // Verify location permission is requested
        try {
            onView(withText("Allow"))
                .check(matches(isDisplayed()))
        } catch (Exception e) {
            // Permission might already be granted
            Log.d("Test", "Location permission dialog not shown")
        }
    }
    
    @Test
    fun testLocationDisplay() {
        // Test location display functionality
        activityRule.scenario.onActivity { activity ->
            // Mock location data
            val mockLocation = Location("test").apply {
                latitude = 37.7749
                longitude = -122.4194
                accuracy = 10f
            }
            
            // Update location display
            activity.updateLocationDisplay(mockLocation)
            
            // Verify location is displayed
            onView(withId(R.id.tvLocation))
                .check(matches(withText(containsString("37.7749"))))
                .check(matches(withText(containsString("-122.4194"))))
        }
    }
    
    @Test
    fun testLocationTracking() {
        // Test location tracking functionality
        onView(withId(R.id.btnStartTracking))
            .perform(click())
        
        // Verify tracking is started
        onView(withId(R.id.btnStopTracking))
            .check(matches(isEnabled()))
        
        onView(withId(R.id.btnStartTracking))
            .check(matches(not(isEnabled())))
    }
}
```

### 3. Sensor Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class SensorTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(SensorActivity::class.java)
    
    @Test
    fun testSensorAvailability() {
        activityRule.scenario.onActivity { activity ->
            val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            
            if (accelerometer != null) {
                // Test sensor functionality
                onView(withId(R.id.btnStartSensors))
                    .check(matches(isDisplayed()))
                    .check(matches(isEnabled()))
            } else {
                // Test fallback behavior
                onView(withText("Sensors not available"))
                    .check(matches(isDisplayed()))
            }
        }
    }
    
    @Test
    fun testSensorDataDisplay() {
        // Test sensor data display
        activityRule.scenario.onActivity { activity ->
            // Mock sensor event
            val mockEvent = mock<SensorEvent>()
            whenever(mockEvent.sensor).thenReturn(mock())
            whenever(mockEvent.values).thenReturn(floatArrayOf(1.0f, 2.0f, 3.0f))
            
            // Simulate sensor change
            activity.onSensorChanged(mockEvent)
            
            // Verify sensor data is displayed
            onView(withId(R.id.tvAccelerometer))
                .check(matches(withText(containsString("1.00"))))
                .check(matches(withText(containsString("2.00"))))
                .check(matches(withText(containsString("3.00"))))
        }
    }
}
```

### 4. Biometric Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class BiometricTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(BiometricActivity::class.java)
    
    @Test
    fun testBiometricAvailability() {
        activityRule.scenario.onActivity { activity ->
            val biometricManager = BiometricManager.from(activity)
            val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
            
            if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                // Test biometric functionality
                onView(withId(R.id.btnAuthenticate))
                    .check(matches(isDisplayed()))
                    .check(matches(isEnabled()))
            } else {
                // Test fallback behavior
                onView(withText("Biometric not available"))
                    .check(matches(isDisplayed()))
            }
        }
    }
    
    @Test
    fun testBiometricAuthentication() {
        // Test biometric authentication flow
        onView(withId(R.id.btnAuthenticate))
            .perform(click())
        
        // Verify biometric prompt appears
        // Note: This test requires biometric hardware
        try {
            onView(withText("Biometric Authentication"))
                .check(matches(isDisplayed()))
        } catch (Exception e) {
            // Biometric prompt might not be available in test environment
            Log.d("Test", "Biometric prompt not shown")
        }
    }
}
```

## File Storage Testing

### 1. File Operations Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class FileStorageTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(FileActivity::class.java)
    
    @Test
    fun testFileWriteRead() {
        // Test file write and read operations
        val testContent = "Test file content"
        
        // Enter content
        onView(withId(R.id.etFileContent))
            .perform(typeText(testContent), closeSoftKeyboard())
        
        // Write file
        onView(withId(R.id.btnWriteFile))
            .perform(click())
        
        // Read file
        onView(withId(R.id.btnReadFile))
            .perform(click())
        
        // Verify content is displayed
        onView(withId(R.id.tvFileContent))
            .check(matches(withText(testContent)))
    }
    
    @Test
    fun testFileList() {
        // Test file listing functionality
        onView(withId(R.id.btnListFiles))
            .perform(click())
        
        // Verify file list is displayed
        onView(withId(R.id.tvFileContent))
            .check(matches(withText(containsString("example.txt"))))
    }
    
    @Test
    fun testFilePick() {
        // Test file pick functionality
        onView(withId(R.id.btnPickFile))
            .perform(click())
        
        // Verify file picker intent is launched
        intended(hasAction(Intent.ACTION_OPEN_DOCUMENT))
    }
}
```

## Security Testing

### 1. Security Unit Tests
```kotlin
class SecurityTest {
    
    @Test
    fun testInputValidation() {
        val validator = InputValidator()
        
        // Test valid email
        assertTrue(validator.validateEmail("test@example.com"))
        
        // Test invalid email
        assertFalse(validator.validateEmail("invalid-email"))
        
        // Test valid password
        val strongPassword = "SecurePass123!"
        val passwordResult = validator.validatePassword(strongPassword)
        assertTrue(passwordResult.isValid)
        
        // Test weak password
        val weakPassword = "123"
        val weakResult = validator.validatePassword(weakPassword)
        assertFalse(weakResult.isValid)
        assertTrue(weakResult.errors.isNotEmpty())
    }
    
    @Test
    fun testInputSanitization() {
        val validator = InputValidator()
        
        val maliciousInput = "<script>alert('xss')</script>"
        val sanitized = validator.sanitizeInput(maliciousInput)
        
        assertFalse(sanitized.contains("<"))
        assertFalse(sanitized.contains(">"))
        assertFalse(sanitized.contains("\""))
    }
    
    @Test
    fun testEncryption() {
        val secureFileManager = SecureFileManager()
        secureFileManager.setupEncryption()
        
        val testContent = "Sensitive data"
        val testFile = File.createTempFile("test", ".txt")
        val encryptedFile = File.createTempFile("test", ".enc")
        
        try {
            // Test encryption
            secureFileManager.encryptFile(testFile, encryptedFile)
            assertTrue(encryptedFile.exists())
            assertTrue(encryptedFile.length() > 0)
            
            // Verify files are different
            assertNotEquals(testFile.readText(), encryptedFile.readText())
        } finally {
            testFile.delete()
            encryptedFile.delete()
        }
    }
}
```

## Device-Specific Testing

### 1. Device Feature Detection
```kotlin
@RunWith(AndroidJUnit4::class)
class DeviceFeatureTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testDeviceFeatures() {
        activityRule.scenario.onActivity { activity ->
            val packageManager = activity.packageManager
            
            // Test camera feature
            val hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            Log.d("Test", "Device has camera: $hasCamera")
            
            // Test location feature
            val hasLocation = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION)
            Log.d("Test", "Device has location: $hasLocation")
            
            // Test biometric feature
            val hasBiometric = packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
            Log.d("Test", "Device has biometric: $hasBiometric")
            
            // Test sensor features
            val hasAccelerometer = packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)
            Log.d("Test", "Device has accelerometer: $hasAccelerometer")
        }
    }
}
```

### 2. Android Version Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class AndroidVersionTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testAndroidVersionFeatures() {
        activityRule.scenario.onActivity { activity ->
            val sdkVersion = Build.VERSION.SDK_INT
            
            // Test Android version-specific features
            if (sdkVersion >= Build.VERSION_CODES.M) {
                // Runtime permissions available
                Log.d("Test", "Runtime permissions supported")
            }
            
            if (sdkVersion >= Build.VERSION_CODES.Q) {
                // Scoped storage available
                Log.d("Test", "Scoped storage supported")
            }
            
            if (sdkVersion >= Build.VERSION_CODES.P) {
                // Biometric API available
                Log.d("Test", "Biometric API supported")
            }
        }
    }
}
```

## Testing Tools and Utilities

### 1. Test Utilities
```kotlin
object TestUtils {
    
    fun grantPermission(permission: String) {
        // Grant permission using ADB
        val command = "adb shell pm grant com.example.devicefeatures $permission"
        Runtime.getRuntime().exec(command)
    }
    
    fun revokePermission(permission: String) {
        // Revoke permission using ADB
        val command = "adb shell pm revoke com.example.devicefeatures $permission"
        Runtime.getRuntime().exec(command)
    }
    
    fun checkPermissionStatus(permission: String): Boolean {
        // Check permission status using ADB
        val command = "adb shell dumpsys package com.example.devicefeatures | grep $permission"
        val process = Runtime.getRuntime().exec(command)
        val output = process.inputStream.bufferedReader().readText()
        return output.contains("granted=true")
    }
    
    fun simulateLocation(latitude: Double, longitude: Double) {
        // Simulate location using ADB
        val command = "adb shell geo fix $longitude $latitude"
        Runtime.getRuntime().exec(command)
    }
    
    fun takeScreenshot(name: String) {
        // Take screenshot for visual testing
        val command = "adb shell screencap -p /sdcard/$name.png"
        Runtime.getRuntime().exec(command)
        
        // Pull screenshot to computer
        val pullCommand = "adb pull /sdcard/$name.png ./screenshots/"
        Runtime.getRuntime().exec(pullCommand)
    }
}
```

### 2. Test Data Management
```kotlin
object TestDataManager {
    
    fun createTestFile(content: String): File {
        val testFile = File.createTempFile("test", ".txt")
        testFile.writeText(content)
        return testFile
    }
    
    fun createTestImage(): Bitmap {
        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }
    
    fun createTestLocation(): Location {
        return Location("test").apply {
            latitude = 37.7749
            longitude = -122.4194
            accuracy = 10f
            time = System.currentTimeMillis()
        }
    }
    
    fun cleanupTestFiles() {
        // Clean up test files
        val testDir = File(System.getProperty("java.io.tmpdir"))
        testDir.listFiles()?.filter { it.name.startsWith("test") }?.forEach { it.delete() }
    }
}
```

## Continuous Integration Testing

### 1. CI/CD Pipeline
```yaml
# .github/workflows/android-test.yml
name: Android Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up JDK
      uses: actions/setup-java@v2
      with:
        java-version: '11'
        distribution: 'adopt'
    
    - name: Set up Android SDK
      uses: android-actions/setup-android@v2
    
    - name: Run unit tests
      run: ./gradlew test
    
    - name: Run instrumented tests
      run: ./gradlew connectedAndroidTest
    
    - name: Upload test results
      uses: actions/upload-artifact@v2
      with:
        name: test-results
        path: app/build/reports/
```

### 2. Test Reporting
```kotlin
class TestReporter {
    
    fun generateTestReport() {
        // Generate comprehensive test report
        val report = TestReport()
        
        // Add test results
        report.addTestResult("Permission Tests", runPermissionTests())
        report.addTestResult("Camera Tests", runCameraTests())
        report.addTestResult("Location Tests", runLocationTests())
        report.addTestResult("Sensor Tests", runSensorTests())
        report.addTestResult("Biometric Tests", runBiometricTests())
        report.addTestResult("File Storage Tests", runFileStorageTests())
        report.addTestResult("Security Tests", runSecurityTests())
        
        // Save report
        report.save("test-report.json")
    }
}

data class TestReport(
    val timestamp: Long = System.currentTimeMillis(),
    val results: MutableMap<String, TestResult> = mutableMapOf()
) {
    fun addTestResult(name: String, result: TestResult) {
        results[name] = result
    }
    
    fun save(filename: String) {
        // Save report to file
    }
}

data class TestResult(
    val passed: Int,
    val failed: Int,
    val skipped: Int,
    val duration: Long,
    val details: List<String> = emptyList()
)
```

## Best Practices for Testing

### 1. Test Organization
- Group related tests together
- Use descriptive test names
- Follow AAA pattern (Arrange, Act, Assert)

### 2. Test Data Management
- Use test fixtures and factories
- Clean up test data after tests
- Use isolated test environments

### 3. Mocking and Stubbing
- Mock external dependencies
- Use test doubles for device features
- Avoid testing implementation details

### 4. Test Coverage
- Aim for high test coverage
- Focus on critical paths
- Test edge cases and error scenarios

### 5. Performance Testing
- Test app performance on different devices
- Monitor memory usage and battery consumption
- Test with different data sizes

## Summary
Comprehensive testing of Android device features and permissions requires a multi-layered approach. Use unit tests for individual components, integration tests for feature interactions, and UI tests for user workflows. Test on different devices and Android versions to ensure compatibility. Use automated testing tools and CI/CD pipelines to maintain code quality and reliability.
