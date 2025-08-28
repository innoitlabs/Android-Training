# 3. Gradle Build System and Dependencies

## 🔧 What is Gradle?

Gradle is a powerful build automation system that Android uses to compile code, manage dependencies, and create APK files. It's designed to be flexible, fast, and supports multiple programming languages including Kotlin.

### Why Gradle for Android?
- **Dependency Management**: Automatically downloads and manages libraries
- **Build Variants**: Supports multiple build configurations (debug, release)
- **Incremental Builds**: Only rebuilds changed components
- **Plugin System**: Extensible with custom plugins
- **Multi-language Support**: Works with Java, Kotlin, and other JVM languages

## 🏗️ Gradle Architecture

### Gradle Wrapper
The Gradle wrapper ensures everyone uses the same Gradle version:
```
gradle/
├── wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
gradlew                    # Unix shell script
gradlew.bat               # Windows batch file
```

### Build Configuration Files
```
project/
├── build.gradle                 # Project-level configuration
├── app/build.gradle            # Module-level configuration
├── settings.gradle             # Project settings
├── gradle.properties           # Gradle properties
└── local.properties            # Local environment (not in VCS)
```

## 📄 Gradle Configuration Files

### settings.gradle
Defines which modules are part of the project:
```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyAndroidApp"
include ':app'
```

### Project-Level build.gradle
Configures build tools and plugins for the entire project:
```gradle
// Top-level build file
buildscript {
    ext {
        kotlin_version = '1.9.0'
        compose_version = '1.5.0'
        gradle_version = '8.1.0'
    }
    
    repositories {
        google()
        mavenCentral()
    }
    
    dependencies {
        classpath "com.android.tools.build:gradle:$gradle_version"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        
        // Other plugins
        classpath 'com.google.gms:google-services:4.3.15'
        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.5'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url "https://jitpack.io" }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

### App-Level build.gradle
Configures the specific Android application module:
```gradle
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'  // For annotation processing
    id 'kotlin-parcelize'  // For Parcelable
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
        
        // Build config fields
        buildConfigField "String", "API_BASE_URL", "\"https://api.example.com/\""
        buildConfigField "boolean", "DEBUG_MODE", "true"
    }

    buildTypes {
        debug {
            applicationIdSuffix ".debug"
            debuggable true
            minifyEnabled false
            buildConfigField "boolean", "DEBUG_MODE", "true"
        }
        
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            buildConfigField "boolean", "DEBUG_MODE", "false"
        }
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '1.8'
    }
    
    buildFeatures {
        viewBinding true
        dataBinding true
        buildConfig true
    }
    
    // Product flavors for different app variants
    flavorDimensions "version"
    productFlavors {
        free {
            dimension "version"
            applicationIdSuffix ".free"
            versionNameSuffix "-free"
        }
        pro {
            dimension "version"
            applicationIdSuffix ".pro"
            versionNameSuffix "-pro"
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Architecture Components
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.6.2'
    
    // Navigation
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.4'
    implementation 'androidx.navigation:navigation-ui-ktx:2.7.4'
    
    // Room Database
    implementation 'androidx.room:room-runtime:2.6.0'
    implementation 'androidx.room:room-ktx:2.6.0'
    kapt 'androidx.room:room-compiler:2.6.0'
    
    // Retrofit for networking
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Image loading
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    kapt 'com.github.bumptech.glide:compiler:4.16.0'
    
    // Testing dependencies
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.3.1'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
    
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
}
```

### gradle.properties
Global Gradle properties for the project:
```properties
# Project-wide Gradle settings
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8

# Android configuration
android.useAndroidX=true
android.enableJetifier=true
android.nonTransitiveRClass=true

# Kotlin configuration
kotlin.code.style=official

# Performance optimization
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true

# Build configuration
android.enableR8.fullMode=true
android.enableBuildConfigAsBytecode=true
```

## 📦 Dependency Management

### Dependency Types

#### Implementation
```gradle
implementation 'androidx.core:core-ktx:1.12.0'
```
- **Purpose**: Main dependencies required at runtime
- **Scope**: Available to the module and its dependencies
- **Use case**: Most common dependency type

#### API
```gradle
api 'androidx.core:core-ktx:1.12.0'
```
- **Purpose**: Dependencies that need to be exposed to consumers
- **Scope**: Transitive dependency (exposed to dependent modules)
- **Use case**: Library modules that need to expose dependencies

#### Compile Only
```gradle
compileOnly 'com.google.android.wearable:wearable:2.9.0'
```
- **Purpose**: Dependencies only needed at compile time
- **Scope**: Not included in final APK
- **Use case**: Annotation processors, optional features

#### Runtime Only
```gradle
runtimeOnly 'com.google.android.wearable:wearable:2.9.0'
```
- **Purpose**: Dependencies only needed at runtime
- **Scope**: Not available at compile time
- **Use case**: Dynamic libraries, optional runtime features

#### Test Dependencies
```gradle
testImplementation 'junit:junit:4.13.2'                    // Unit tests
androidTestImplementation 'androidx.test.ext:junit:1.1.5'  // Instrumented tests
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.12'  // Debug only
```

### Version Management
```gradle
// Define versions in ext block
ext {
    kotlin_version = '1.9.0'
    compose_version = '1.5.0'
    lifecycle_version = '2.6.2'
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
}
```

### Dynamic Versions
```gradle
dependencies {
    // Use latest patch version
    implementation 'androidx.core:core-ktx:1.12.+'
    
    // Use latest minor version
    implementation 'androidx.appcompat:appcompat:1.6.+'
    
    // Use latest major version (not recommended for production)
    implementation 'com.google.android.material:material:+'
}
```

## 🔄 Gradle Sync Process

### What Happens During Sync
1. **Download Dependencies**: Gradle downloads all declared dependencies
2. **Resolve Conflicts**: Handles version conflicts automatically
3. **Generate Build Files**: Creates necessary build configuration
4. **Index Project**: Android Studio indexes the project for features
5. **Validate Configuration**: Checks for configuration errors

### Sync Triggers
- **Manual**: Click "Sync Now" button
- **Automatic**: When build.gradle files change
- **Background**: Periodically for dependency updates

### Common Sync Issues

#### Version Conflicts
```gradle
// Force specific version
configurations.all {
    resolutionStrategy {
        force 'androidx.core:core-ktx:1.12.0'
    }
}
```

#### Repository Issues
```gradle
// Add missing repositories
repositories {
    google()
    mavenCentral()
    maven { url "https://jitpack.io" }
    maven { url "https://maven.google.com" }
}
```

## 🚀 Build Variants

### Build Types + Product Flavors
```gradle
android {
    buildTypes {
        debug { ... }
        release { ... }
    }
    
    flavorDimensions "version", "api"
    productFlavors {
        free {
            dimension "version"
            applicationIdSuffix ".free"
        }
        pro {
            dimension "version"
            applicationIdSuffix ".pro"
        }
        mock {
            dimension "api"
            buildConfigField "String", "API_BASE_URL", "\"https://mock.api.com/\""
        }
        real {
            dimension "api"
            buildConfigField "String", "API_BASE_URL", "\"https://real.api.com/\""
        }
    }
}
```

### Generated Variants
- `freeMockDebug`
- `freeMockRelease`
- `freeRealDebug`
- `freeRealRelease`
- `proMockDebug`
- `proMockRelease`
- `proRealDebug`
- `proRealRelease`

## 📊 Build Performance

### Optimization Techniques
```gradle
// gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m -XX:+HeapDumpOnOutOfMemoryError
```

### Build Cache
```gradle
// Enable build cache
android {
    buildCache {
        local {
            directory = new File(rootDir, 'build-cache')
            removeUnusedEntriesAfterDays = 30
        }
    }
}
```

## 🔧 Custom Tasks

### Simple Task
```gradle
task hello {
    doLast {
        println 'Hello, Gradle!'
    }
}
```

### Android-Specific Task
```gradle
android.applicationVariants.all { variant ->
    task "generate${variant.name.capitalize()}VersionInfo" {
        doLast {
            def versionFile = new File(variant.buildType.sourceSets[0].res.srcDirs[0], 'values/version.xml')
            versionFile.parentFile.mkdirs()
            versionFile.text = """
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="version_name">${variant.versionName}</string>
    <string name="version_code">${variant.versionCode}</string>
</resources>
"""
        }
    }
}
```

## 📱 APK Analysis

### APK Structure
```
app-debug.apk
├── AndroidManifest.xml
├── classes.dex
├── resources.arsc
├── res/
├── assets/
├── lib/
└── META-INF/
```

### Build Outputs
```
app/build/outputs/
├── apk/
│   ├── debug/
│   │   └── app-debug.apk
│   └── release/
│       └── app-release.apk
├── bundle/
│   └── release/
│       └── app-release.aab
└── logs/
```

## 🚨 Troubleshooting

### Common Issues

#### Gradle Sync Failed
```bash
# Solution 1: Invalidate caches
File → Invalidate Caches and Restart

# Solution 2: Clean project
Build → Clean Project

# Solution 3: Delete .gradle folder
rm -rf .gradle/
```

#### Dependency Resolution
```gradle
// Force resolution strategy
configurations.all {
    resolutionStrategy {
        force 'androidx.core:core-ktx:1.12.0'
        failOnVersionConflict()
    }
}
```

#### Build Performance
```gradle
// Profile build
./gradlew assembleDebug --profile

// View build scan
./gradlew assembleDebug --scan
```

## 📚 Additional Resources

### Official Documentation
- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Android Gradle Plugin](https://developer.android.com/studio/build)
- [Dependency Management](https://developer.android.com/studio/build/dependencies)

### Best Practices
- [Gradle Best Practices](https://docs.gradle.org/current/userguide/userguide_single.html#sec:gradle_best_practices)
- [Android Build Performance](https://developer.android.com/studio/build/optimize-your-build)

---

**Next**: [Android Virtual Device Setup](./04-avd-setup.md)
