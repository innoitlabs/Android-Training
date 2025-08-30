# Room Database Setup Guide

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9+ 
- Android SDK API 24+ (Android 7.0)
- Basic knowledge of Kotlin and Android development

## Project Setup

### 1. Create New Android Project

1. Open Android Studio
2. Click "New Project"
3. Select "Empty Views Activity"
4. Configure project:
   - Name: RoomDatabase
   - Package name: com.example.roomdatabase
   - Language: Kotlin
   - Minimum SDK: API 24 (Android 7.0)

### 2. Configure Dependencies

Update your `app/build.gradle.kts` file:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")  // Add this line
}

android {
    namespace = "com.example.roomdatabase"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.roomdatabase"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Existing dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // Room dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

### 3. Sync Project

After updating the build.gradle.kts file:
1. Click "Sync Now" in the notification bar
2. Wait for the sync to complete
3. Ensure no build errors

## File Structure

Your project should have the following structure:

```
app/src/main/java/com/example/roomdatabase/
├── MainActivity.kt
├── User.kt (to be created)
├── UserDao.kt (to be created)
├── AppDatabase.kt (to be created)
├── UserRepository.kt (to be created)
└── UserViewModel.kt (to be created)

app/src/main/res/layout/
└── activity_main.xml (to be updated)
```

## Implementation Steps

### Step 1: Create User Entity
Create `User.kt` in the main package.

### Step 2: Create UserDao
Create `UserDao.kt` in the main package.

### Step 3: Create AppDatabase
Create `AppDatabase.kt` in the main package.

### Step 4: Create UserRepository
Create `UserRepository.kt` in the main package.

### Step 5: Create UserViewModel
Create `UserViewModel.kt` in the main package.

### Step 6: Update Layout
Update `activity_main.xml` with the UI components.

### Step 7: Update MainActivity
Update `MainActivity.kt` to use the ViewModel.

## Common Issues and Solutions

### Issue 1: Kapt not found
**Error**: `Could not find method kapt()`
**Solution**: Make sure you have `id("kotlin-kapt")` in the plugins block.

### Issue 2: Room annotation processor not working
**Error**: `Cannot find symbol @Entity`
**Solution**: 
1. Clean and rebuild project
2. Invalidate caches and restart (File → Invalidate Caches)
3. Check that kapt is properly configured

### Issue 3: Flow not found
**Error**: `Cannot resolve symbol 'Flow'`
**Solution**: Make sure you have `room-ktx` dependency and import:
```kotlin
import kotlinx.coroutines.flow.Flow
```

### Issue 4: Build fails with Room
**Error**: Various Room-related build errors
**Solution**:
1. Check that all Room annotations are properly imported
2. Ensure entities are data classes
3. Verify DAO methods are properly annotated
4. Clean and rebuild project

## Testing the Setup

After implementing all files:

1. **Build the project** (Build → Make Project)
2. **Run on emulator/device**
3. **Test functionality**:
   - Add a user with name and age
   - Verify the user appears in the list
   - Check that the app doesn't crash

## Next Steps

Once the basic setup is working:
1. Try the exercises in the main README
2. Add more complex queries
3. Implement additional features
4. Add unit tests for your DAOs

## Resources

- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Android Architecture Components](https://developer.android.com/topic/architecture)

