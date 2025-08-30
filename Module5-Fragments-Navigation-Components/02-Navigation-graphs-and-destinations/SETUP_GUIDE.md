# Setup Guide for Navigation Graphs Project

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9+ 
- Android SDK API 34 (Android 14)
- Minimum SDK: API 24 (Android 7.0)

## Project Setup Steps

### 1. Open the Project

1. Launch Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the `NavigationGraphs` folder
4. Click "OK"

### 2. Sync Gradle Files

1. Wait for the initial Gradle sync to complete
2. If prompted, click "Sync Now"
3. Ensure all dependencies are downloaded successfully

### 3. Verify Dependencies

Check that the following dependencies are present in `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### 4. Safe Args Plugin Setup (Optional)

For Safe Args functionality, add to project-level `build.gradle.kts`:

```kotlin
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}
```

And in app-level `build.gradle.kts`:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}
```

### 5. Build Configuration

Ensure your `build.gradle.kts` has the correct configuration:

```kotlin
android {
    namespace = "com.example.navigationgraphs"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.navigationgraphs"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

## File Structure Verification

Ensure the following files exist in your project:

```
NavigationGraphs/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/navigationgraphs/
│   │   │   ├── MainActivity.kt
│   │   │   ├── HomeFragment.kt
│   │   │   └── DetailFragment.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   └── fragment_detail.xml
│   │   │   ├── navigation/
│   │   │   │   └── nav_graph.xml
│   │   │   └── values/
│   │   │       ├── colors.xml
│   │   │       ├── strings.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
└── settings.gradle.kts
```

## Common Setup Issues

### Issue 1: Gradle Sync Failed
**Solution:**
1. File → Invalidate Caches and Restart
2. Delete `.gradle` folder and sync again
3. Check internet connection for dependency downloads

### Issue 2: Navigation Graph Not Found
**Solution:**
1. Ensure `nav_graph.xml` is in `res/navigation/` folder
2. Clean and rebuild project
3. Check XML syntax in navigation graph

### Issue 3: Fragment Classes Not Found
**Solution:**
1. Verify package names match in navigation graph
2. Check that fragment classes extend `Fragment`
3. Ensure proper imports in fragment classes

### Issue 4: Safe Args Not Working
**Solution:**
1. Verify Safe Args plugin is applied
2. Clean and rebuild project
3. Check that argument names match in navigation graph and code

## Testing the Setup

### 1. Build the Project
1. Build → Make Project (Ctrl+F9 / Cmd+F9)
2. Ensure no compilation errors

### 2. Run on Emulator
1. Tools → AVD Manager
2. Create or start an emulator
3. Run → Run 'app' (Shift+F10 / Ctrl+R)

### 3. Verify Functionality
1. App should launch with Home Fragment
2. Click "Go to Details" button
3. Should navigate to Detail Fragment
4. Back button should return to Home Fragment

## Next Steps

After successful setup:
1. Read the main README.md for learning content
2. Follow the hands-on exercises
3. Experiment with the code examples
4. Try the advanced exercises

## Support

If you encounter issues:
1. Check the troubleshooting section in README.md
2. Verify all file paths and package names
3. Ensure Android Studio is up to date
4. Check the Android Developer documentation
