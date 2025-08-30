# Safe Args Setup Guide

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Kotlin 1.9+ 
- Android Gradle Plugin 8.0+
- Navigation Component 2.7.7+

## Step-by-Step Setup

### 1. Project Configuration

#### Project-level build.gradle.kts
```kotlin
buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}
```

#### App-level build.gradle.kts
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.safeargs"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.safeargs"
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

### 2. Sync Project

After updating the build files:
1. Click "Sync Now" in Android Studio
2. Wait for Gradle sync to complete
3. Verify no build errors

### 3. Create Navigation Graph

1. Right-click on `res` folder
2. Select `New` → `Android Resource File`
3. Choose `Navigation` as Resource type
4. Name it `nav_graph`
5. Click OK

### 4. Configure MainActivity

Update `MainActivity.kt` to use Navigation Component:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

Update `activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/nav_graph" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 5. Verify Setup

After completing the setup:

1. **Build the project** (Build → Make Project)
2. **Check for generated classes**:
   - Look for generated classes in `build/generated/source/navigation-args/`
   - Classes like `HomeFragmentDirections` and `DetailFragmentArgs` should be generated

3. **Test navigation**:
   - Run the app
   - Verify fragments load correctly
   - Test navigation between fragments

## Troubleshooting

### Common Issues:

1. **"Cannot resolve symbol" errors for Safe Args classes**
   - Clean and rebuild project
   - Check plugin application in build.gradle files
   - Verify navigation graph XML syntax

2. **Gradle sync fails**
   - Check internet connection
   - Update Android Studio
   - Invalidate caches and restart

3. **Navigation not working**
   - Verify NavHostFragment setup in activity_main.xml
   - Check navigation graph start destination
   - Ensure fragments are properly defined

### Verification Checklist:

- [ ] Project syncs without errors
- [ ] Safe Args plugin is applied
- [ ] Navigation graph is created
- [ ] MainActivity uses NavHostFragment
- [ ] Generated classes are available
- [ ] App builds successfully
- [ ] Navigation works in runtime

## Next Steps

Once setup is complete:
1. Create your fragments
2. Define arguments in navigation graph
3. Use Safe Args for type-safe navigation
4. Test your implementation

For detailed implementation examples, see the main README.md file.
