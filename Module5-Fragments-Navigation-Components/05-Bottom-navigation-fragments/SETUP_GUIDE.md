# Android Bottom Navigation Setup Guide

## Prerequisites

- Android Studio (latest version)
- Kotlin 1.9+ support
- Android SDK (API level 24+)
- An Android emulator or physical device

## Project Setup Instructions

### Step 1: Open the Project

1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the `BottomNavigation` folder
4. Click "OK" to open the project

### Step 2: Sync Gradle Files

1. Wait for Android Studio to sync the project
2. If prompted, click "Sync Now" in the notification bar
3. Ensure all dependencies are downloaded successfully

### Step 3: Verify Dependencies

Check that the following dependencies are present in `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### Step 4: Build Configuration

Ensure your `app/build.gradle.kts` has the correct configuration:

```kotlin
android {
    namespace = "com.example.bottomnavigation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bottomnavigation"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
```

## Running the Application

### Step 1: Set Up an Emulator (if needed)

1. Go to Tools → AVD Manager
2. Click "Create Virtual Device"
3. Select a device (e.g., Pixel 4)
4. Choose a system image (API level 24 or higher)
5. Complete the setup and start the emulator

### Step 2: Run the App

1. Click the green "Run" button (▶️) in the toolbar
2. Select your target device (emulator or physical device)
3. Click "OK" to install and run the app

### Step 3: Verify Functionality

1. **Home Tab**: Should display "Home Fragment" text
2. **Dashboard Tab**: Should display "Dashboard Fragment" text
3. **Profile Tab**: Should display "Profile Fragment" text
4. **Navigation**: Tapping tabs should switch between fragments smoothly

## Project Structure Overview

```
BottomNavigation/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/bottomnavigation/
│   │   │   ├── MainActivity.kt              # Main activity with bottom navigation
│   │   │   ├── HomeFragment.kt              # Home fragment implementation
│   │   │   ├── DashboardFragment.kt         # Dashboard fragment implementation
│   │   │   └── ProfileFragment.kt           # Profile fragment implementation
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml        # Main activity layout
│   │   │   │   ├── fragment_home.xml        # Home fragment layout
│   │   │   │   ├── fragment_dashboard.xml   # Dashboard fragment layout
│   │   │   │   └── fragment_profile.xml     # Profile fragment layout
│   │   │   ├── menu/
│   │   │   │   └── bottom_nav_menu.xml      # Bottom navigation menu
│   │   │   ├── values/
│   │   │   │   ├── colors.xml               # Color definitions
│   │   │   │   ├── strings.xml              # String resources
│   │   │   │   └── themes.xml               # App theme
│   │   │   └── mipmap-*/                    # App icons
│   │   └── AndroidManifest.xml              # App manifest
│   ├── build.gradle.kts                     # App-level build configuration
│   └── proguard-rules.pro                   # ProGuard rules
├── build.gradle.kts                         # Project-level build configuration
├── settings.gradle.kts                      # Project settings
└── gradle.properties                        # Gradle properties
```

## Common Setup Issues and Solutions

### Issue 1: Gradle Sync Failed

**Symptoms**: Red error messages in the build output

**Solutions**:
- Check internet connection
- Update Android Studio to the latest version
- Invalidate caches: File → Invalidate Caches and Restart
- Clean and rebuild project: Build → Clean Project, then Build → Rebuild Project

### Issue 2: Material Design Components Not Found

**Symptoms**: Red underlines under Material Design imports

**Solutions**:
- Ensure the Material Design dependency is added to `app/build.gradle.kts`
- Sync Gradle files after adding dependencies
- Check that the dependency version is compatible with your compileSdk

### Issue 3: Fragment Not Found

**Symptoms**: Runtime crashes when switching tabs

**Solutions**:
- Verify fragment class names match exactly
- Check that fragment layouts exist and are properly named
- Ensure fragment classes extend `androidx.fragment.app.Fragment`

### Issue 4: Layout Issues

**Symptoms**: UI elements not displaying correctly

**Solutions**:
- Check layout constraints and parent-child relationships
- Verify that fragment container has proper dimensions
- Test on different screen sizes and orientations

## Development Tips

### Code Organization

1. **Package Structure**: Keep fragments in the same package as MainActivity
2. **Resource Naming**: Use consistent naming conventions for layouts and menus
3. **Fragment Lifecycle**: Understand and properly handle fragment lifecycle events

### Testing

1. **Unit Tests**: Write tests for fragment logic
2. **UI Tests**: Test navigation interactions
3. **Device Testing**: Test on multiple device types and screen sizes

### Performance

1. **Fragment Transactions**: Use `replace()` instead of `add()` for better performance
2. **Memory Management**: Avoid memory leaks by properly managing fragment references
3. **State Preservation**: Implement state saving for complex fragments

## Next Steps

After successfully running the basic bottom navigation app:

1. **Enhance Fragments**: Add more complex UI elements to each fragment
2. **State Management**: Implement fragment state preservation
3. **Animations**: Add custom transition animations
4. **Navigation Component**: Migrate to Navigation Component for more advanced features
5. **Deep Linking**: Implement deep linking to specific fragments

## Resources

- [Material Design Guidelines](https://material.io/design/navigation/bottom-navigation.html)
- [Android Fragments Documentation](https://developer.android.com/guide/fragments)
- [BottomNavigationView API Reference](https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView)
- [Navigation Component](https://developer.android.com/guide/navigation)
