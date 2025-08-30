# Build Instructions & Troubleshooting Guide

## 🚀 Quick Start

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API 24+
- Internet connection for API calls
- Java 11 or later

### Step-by-Step Build Instructions

1. **Open Project in Android Studio**
   ```bash
   # Open Android Studio
   # File → Open → Select DependencyHilt folder
   ```

2. **Sync Gradle Files**
   - Wait for Android Studio to sync Gradle files
   - If sync fails, try: File → Invalidate Caches and Restart

3. **Build Project**
   - Build → Make Project (Ctrl+F9 / Cmd+F9)
   - Or use: Build → Build Bundle(s) / APK(s) → Build APK(s)

4. **Run on Device/Emulator**
   - Run → Run 'app' (Shift+F10 / Ctrl+R)
   - Select your target device or emulator

## 🔧 Troubleshooting Common Issues

### Issue 1: Gradle Sync Fails
**Symptoms**: "Could not resolve dependencies" or "Multiple build operations failed"

**Solutions**:
1. **Clear Gradle Cache**:
   ```bash
   # On macOS/Linux
   rm -rf ~/.gradle/caches/
   
   # On Windows
   rmdir /s %USERPROFILE%\.gradle\caches
   ```

2. **Invalidate Android Studio Caches**:
   - File → Invalidate Caches and Restart
   - Choose "Invalidate and Restart"

3. **Update Gradle Version**:
   - Check `gradle/wrapper/gradle-wrapper.properties`
   - Update to latest stable version if needed

### Issue 2: Hilt Compilation Errors
**Symptoms**: "Cannot find symbol @HiltAndroidApp" or similar

**Solutions**:
1. **Verify Hilt Plugin**:
   ```kotlin
   // In project-level build.gradle.kts
   plugins {
       id("com.google.dagger.hilt.android") version "2.48" apply false
   }
   ```

2. **Check App-level Configuration**:
   ```kotlin
   // In app-level build.gradle.kts
   plugins {
       id("com.google.dagger.hilt.android")
       id("kotlin-kapt")
   }
   ```

3. **Clean and Rebuild**:
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

### Issue 3: Kotlin Version Conflicts
**Symptoms**: "Kotlin version compatibility" errors

**Solutions**:
1. **Check Kotlin Version**:
   ```kotlin
   // In gradle/libs.versions.toml
   kotlin = "2.0.21"  // Should match your Android Studio version
   ```

2. **Update Kotlin Plugin**:
   - File → Settings → Plugins
   - Update Kotlin plugin to latest version

### Issue 4: API Level Issues
**Symptoms**: "minSdk" or "targetSdk" related errors

**Solutions**:
1. **Check SDK Installation**:
   - Tools → SDK Manager
   - Install API level 24 and 36

2. **Update build.gradle.kts**:
   ```kotlin
   android {
       compileSdk = 36
       defaultConfig {
           minSdk = 24
           targetSdk = 36
       }
   }
   ```

## 📱 Running the App

### Expected Behavior
1. **App Launch**: Shows loading indicator
2. **Data Loading**: Fetches users from JSONPlaceholder API
3. **User List**: Displays users in a scrollable list
4. **Pull to Refresh**: Swipe down to refresh data
5. **Click Interaction**: Tap user items to see Toast message
6. **Error Handling**: Shows error message if network fails

### Testing Features
- ✅ **Dependency Injection**: All dependencies properly injected
- ✅ **Network Calls**: API calls work correctly
- ✅ **UI Updates**: LiveData updates UI reactively
- ✅ **Error States**: Proper error handling and display
- ✅ **Loading States**: Loading indicators work correctly

## 🧪 Running Tests

### Unit Tests
```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests UserViewModelTest
```

### Instrumented Tests
```bash
# Run instrumented tests
./gradlew connectedAndroidTest
```

### Test Troubleshooting
If tests fail due to missing dependencies:
1. **Add Missing Dependencies**:
   ```kotlin
   testImplementation("org.mockito:mockito-core:5.8.0")
   testImplementation("org.robolectric:robolectric:4.11.1")
   ```

2. **Update Test Configuration**:
   ```kotlin
   android {
       testOptions {
           unitTests {
               isIncludeAndroidResources = true
           }
       }
   }
   ```

## 🔍 Debugging Tips

### Enable Debug Logging
```kotlin
// Add to build.gradle.kts
android {
    buildTypes {
        debug {
            isDebuggable = true
        }
    }
}
```

### Check Network Calls
- Use Android Studio's Network Inspector
- Monitor Logcat for Retrofit logs
- Verify internet permission in AndroidManifest.xml

### Verify Hilt Setup
1. **Check Application Class**:
   ```kotlin
   @HiltAndroidApp
   class MyApplication : Application()
   ```

2. **Verify Activity Annotation**:
   ```kotlin
   @AndroidEntryPoint
   class MainActivity : AppCompatActivity()
   ```

3. **Check Module Installation**:
   ```kotlin
   @Module
   @InstallIn(SingletonComponent::class)
   object NetworkModule
   ```

## 📋 Pre-Build Checklist

Before building, ensure:

- [ ] Android Studio is up to date
- [ ] All dependencies are properly declared
- [ ] Hilt plugin is applied
- [ ] Application class is registered in AndroidManifest.xml
- [ ] Internet permission is added
- [ ] All Kotlin files have proper imports
- [ ] Gradle sync completes successfully

## 🆘 Getting Help

### Common Error Messages and Solutions

**"Cannot resolve symbol @HiltAndroidApp"**
- Ensure Hilt plugin is applied in build.gradle.kts
- Check that kotlin-kapt plugin is applied
- Verify Hilt dependencies are included

**"No injector factory bound"**
- Add @AndroidEntryPoint to your Activity/Fragment
- Ensure Application class has @HiltAndroidApp
- Check that Application class is registered in AndroidManifest.xml

**"Multiple bindings for the same type"**
- Use @Qualifier annotations for multiple implementations
- Check for duplicate @Provides methods
- Verify module installation scopes

**"Circular dependency"**
- Use @Lazy for circular dependencies
- Restructure dependencies to avoid cycles
- Consider using interfaces instead of concrete classes

### Resources
- [Hilt Official Documentation](https://dagger.dev/hilt/)
- [Android Developer Documentation](https://developer.android.com/)
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

---

## 🎯 Success Criteria

The project is successfully built when:

1. ✅ **Gradle sync completes** without errors
2. ✅ **Project builds** successfully
3. ✅ **App installs** on device/emulator
4. ✅ **App launches** without crashes
5. ✅ **User list loads** from API
6. ✅ **All UI interactions** work correctly
7. ✅ **Tests pass** (if running tests)

If you encounter any issues not covered here, please check the error logs and refer to the official documentation for the specific libraries being used.
