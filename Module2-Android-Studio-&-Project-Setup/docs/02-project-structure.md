# 2. Project Structure

## 📁 Understanding Android Project Organization

Android projects follow a specific structure that separates concerns and organizes code efficiently. Understanding this structure is crucial for effective development.

## 🏗️ Project Hierarchy

### Top-Level Project vs App Module
```
MyAndroidApp/                    # Top-level project directory
├── .gradle/                     # Gradle cache and temporary files
├── .idea/                       # IntelliJ IDEA project settings
├── app/                         # Main application module
├── gradle/                      # Gradle wrapper files
├── build.gradle                 # Project-level build configuration
├── gradle.properties            # Gradle properties
├── gradlew                      # Gradle wrapper script (Unix)
├── gradlew.bat                  # Gradle wrapper script (Windows)
├── local.properties             # Local SDK path (not in version control)
└── settings.gradle              # Project settings and module inclusion
```

### App Module Structure
```
app/                             # Application module
├── build/                       # Build outputs (generated)
├── libs/                        # Local library dependencies
├── src/                         # Source code directory
│   ├── main/                    # Main source set
│   │   ├── java/                # Java/Kotlin source files
│   │   │   └── com/example/myapp/
│   │   │       ├── MainActivity.kt
│   │   │       └── ...
│   │   ├── res/                 # Resources directory
│   │   │   ├── drawable/        # Images and drawables
│   │   │   ├── layout/          # UI layout files
│   │   │   ├── values/          # String, color, style definitions
│   │   │   ├── menu/            # Menu definitions
│   │   │   └── mipmap/          # App icons
│   │   └── AndroidManifest.xml  # App configuration
│   ├── test/                    # Unit tests
│   └── androidTest/             # Instrumented tests
├── build.gradle                 # Module-level build configuration
└── proguard-rules.pro           # Code obfuscation rules
```

## 📄 Key Files Explained

### AndroidManifest.xml
The manifest file is the heart of your Android application. It declares essential information about your app.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myapp">

    <!-- Permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.MyApp">

        <!-- Main Activity -->
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>
</manifest>
```

**Key Elements:**
- **package**: Your app's unique identifier
- **permissions**: What your app needs to access
- **application**: Main app configuration
- **activity**: UI components of your app
- **intent-filter**: Defines how activities can be launched

### build.gradle Files

#### Project-Level build.gradle
```gradle
// Top-level build file
buildscript {
    ext {
        kotlin_version = '1.9.0'
        compose_version = '1.5.0'
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.0'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

#### App-Level build.gradle
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
}

android {
    namespace 'com.example.myapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.myapp"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## 📂 Resource Organization (res/)

### Layout Resources (res/layout/)
```xml
<!-- activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_world"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### String Resources (res/values/strings.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">My App</string>
    <string name="hello_world">Hello World!</string>
    <string name="welcome_message">Welcome to my Android app</string>
</resources>
```

### Color Resources (res/values/colors.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
```

### Style Resources (res/values/themes.xml)
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme -->
    <style name="Theme.MyApp" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand color -->
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        <!-- Secondary brand color -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>
        <item name="colorOnSecondary">@color/black</item>
        <!-- Status bar color -->
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
    </style>
</resources>
```

## 🎯 Source Code Organization

### Package Structure
```
com.example.myapp/
├── activities/          # Activity classes
│   ├── MainActivity.kt
│   └── SecondActivity.kt
├── fragments/           # Fragment classes
│   ├── HomeFragment.kt
│   └── DetailFragment.kt
├── adapters/            # RecyclerView adapters
│   └── ItemAdapter.kt
├── models/              # Data classes
│   ├── User.kt
│   └── Item.kt
├── utils/               # Utility classes
│   ├── Constants.kt
│   └── Extensions.kt
└── network/             # Network-related classes
    ├── ApiService.kt
    └── RetrofitClient.kt
```

### Example MainActivity.kt
```kotlin
package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        Log.d(TAG, "MainActivity created")
        setupUI()
    }
    
    private fun setupUI() {
        // Initialize UI components
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity resumed")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "MainActivity paused")
    }
}
```

## 🔧 Build Variants

### Debug vs Release
- **Debug**: Development version with debugging enabled
- **Release**: Production version with optimizations

### Build Types Configuration
```gradle
android {
    buildTypes {
        debug {
            applicationIdSuffix ".debug"
            debuggable true
            minifyEnabled false
        }
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }
}
```

## 📱 Resource Qualifiers

Android supports different resource qualifiers for various device configurations:

### Screen Density
- `drawable-mdpi/` - Medium density (160dpi)
- `drawable-hdpi/` - High density (240dpi)
- `drawable-xhdpi/` - Extra high density (320dpi)
- `drawable-xxhdpi/` - Extra extra high density (480dpi)

### Screen Size
- `layout/` - Default layout
- `layout-large/` - Large screens
- `layout-xlarge/` - Extra large screens

### Orientation
- `layout/` - Default orientation
- `layout-land/` - Landscape orientation
- `layout-port/` - Portrait orientation

### Language
- `values/` - Default language
- `values-es/` - Spanish
- `values-fr/` - French

## 🎨 Best Practices

### File Naming Conventions
- **Activities**: `MainActivity.kt`, `LoginActivity.kt`
- **Fragments**: `HomeFragment.kt`, `DetailFragment.kt`
- **Layouts**: `activity_main.xml`, `fragment_home.xml`
- **Drawables**: `ic_launcher.png`, `btn_background.xml`

### Resource Organization
1. **Group related resources** in appropriate folders
2. **Use meaningful names** for all resources
3. **Avoid hardcoded strings** - use string resources
4. **Organize drawables** by type (icons, backgrounds, etc.)

### Code Organization
1. **Follow package structure** conventions
2. **Keep activities focused** on UI logic
3. **Separate business logic** from UI code
4. **Use meaningful class and method names**

## 🔍 Project Views in Android Studio

### Android View
- Shows project structure optimized for Android development
- Groups files by type (manifests, java, res)
- Hides build files and temporary directories

### Project View
- Shows actual file system structure
- Includes all files and directories
- Useful for advanced file management

### Packages View
- Groups files by package structure
- Shows Java/Kotlin files organized by namespace
- Hides resource files

## 📚 Additional Resources

### Official Documentation
- [Android Project Structure](https://developer.android.com/studio/projects)
- [Resource Types](https://developer.android.com/guide/topics/resources/providing-resources)
- [Build Configuration](https://developer.android.com/studio/build)

### Best Practices
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Material Design Guidelines](https://material.io/design)
- [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)

---

**Next**: [Gradle Build System](./03-gradle-build-system.md)
