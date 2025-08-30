# Setup Guide: ViewPager2 and TabLayout Integration

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9+ 
- Android SDK API 24+ (Android 7.0)
- Material Design Components library

## Step-by-Step Setup

### 1. Project Configuration

#### Update build.gradle.kts (Project Level)
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}
```

#### Update build.gradle.kts (App Level)
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.viewpager2tablayout"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.viewpager2tablayout"
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

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### 2. Layout Files Setup

#### activity_main.xml
Create the main activity layout with TabLayout and ViewPager2.

#### Fragment Layouts
Create three fragment layouts:
- `fragment_home.xml`
- `fragment_dashboard.xml` 
- `fragment_profile.xml`

### 3. Kotlin Files Setup

#### MainActivity.kt
Implement the main activity with ViewPager2 and TabLayout integration.

#### ViewPagerAdapter.kt
Create the FragmentStateAdapter for managing fragments.

#### Fragment Classes
Create three fragment classes:
- `HomeFragment.kt`
- `DashboardFragment.kt`
- `ProfileFragment.kt`

### 4. AndroidManifest.xml
Ensure the manifest is properly configured.

## Verification Steps

### 1. Build Verification
```bash
# In Android Studio Terminal
./gradlew build
```

### 2. Runtime Verification
1. Launch the app on emulator/device
2. Verify tabs are displayed
3. Test swipe navigation
4. Test tab tap navigation
5. Check for any runtime errors

### 3. Common Issues and Solutions

#### Issue: Material Design Components not found
**Solution**: Add dependency in build.gradle.kts
```kotlin
implementation("com.google.android.material:material:1.12.0")
```

#### Issue: TabLayoutMediator not found
**Solution**: Add import
```kotlin
import com.google.android.material.tabs.TabLayoutMediator
```

#### Issue: ViewPager2 not found
**Solution**: Add dependency
```kotlin
implementation("androidx.viewpager2:viewpager2:1.0.0")
```

#### Issue: Fragments not showing
**Solution**: Check FragmentStateAdapter implementation and ensure proper fragment creation.

## Testing Checklist

- [ ] App builds successfully
- [ ] App launches without crashes
- [ ] Three tabs are visible (Home, Dashboard, Profile)
- [ ] Swiping left/right changes fragments
- [ ] Tapping tabs changes fragments
- [ ] Each fragment displays correct content
- [ ] No memory leaks (test with multiple swipes)
- [ ] Works on different screen sizes
- [ ] Works in both portrait and landscape

## Performance Considerations

1. **Memory Management**: FragmentStateAdapter automatically handles fragment lifecycle
2. **Smooth Scrolling**: ViewPager2 provides hardware acceleration
3. **State Preservation**: Fragments maintain their state during configuration changes
4. **Efficient Rendering**: Only visible and adjacent fragments are kept in memory

## Next Steps

After successful setup, try these enhancements:
1. Add tab icons
2. Implement custom tab layouts
3. Add tab selection listeners
4. Integrate with Navigation Component
5. Add animations and transitions
