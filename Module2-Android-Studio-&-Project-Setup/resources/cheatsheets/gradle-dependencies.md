# Gradle Dependencies Cheatsheet

## 📦 Essential Android Dependencies

### Core Android Dependencies
```gradle
dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.activity:activity-ktx:1.8.0'
    implementation 'androidx.fragment:fragment-ktx:1.6.2'
    
    // Material Design
    implementation 'com.google.android.material:material:1.10.0'
    
    // Layout
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.cardview:cardview:1.0.0'
    
    // Lifecycle
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
    
    // Retrofit for Networking
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
    
    // Image Loading
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    kapt 'com.github.bumptech.glide:compiler:4.16.0'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## 🎨 UI and Design Dependencies

### Material Design Components
```gradle
dependencies {
    // Material Design
    implementation 'com.google.android.material:material:1.10.0'
    
    // Material Design Icons
    implementation 'com.google.android.material:material-icons-core:1.10.0'
    implementation 'com.google.android.material:material-icons-extended:1.10.0'
    
    // Material Design Components
    implementation 'com.google.android.material:material-components:1.10.0'
    implementation 'com.google.android.material:material-components-android:1.10.0'
}
```

### Animation and Graphics
```gradle
dependencies {
    // Lottie Animations
    implementation 'com.airbnb.android:lottie:6.1.0'
    
    // Shimmer Loading
    implementation 'com.facebook.shimmer:shimmer:0.5.0'
    
    // Rounded ImageView
    implementation 'com.makeramen:roundedimageview:2.3.0'
    
    // Circle ImageView
    implementation 'de.hdodenhof:circleimageview:3.1.0'
}
```

## 🌐 Networking Dependencies

### HTTP Clients
```gradle
dependencies {
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.retrofit2:converter-scalars:2.9.0'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.9.0'
    
    // OkHttp
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.11.0'
    
    // Volley (Alternative)
    implementation 'com.android.volley:volley:1.2.1'
}
```

### JSON Parsing
```gradle
dependencies {
    // Gson
    implementation 'com.google.code.gson:gson:2.10.1'
    
    // Jackson
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
    
    // Moshi
    implementation 'com.squareup.moshi:moshi:1.15.0'
    implementation 'com.squareup.moshi:moshi-kotlin:1.15.0'
}
```

## 💾 Data Storage Dependencies

### Database
```gradle
dependencies {
    // Room Database
    implementation 'androidx.room:room-runtime:2.6.0'
    implementation 'androidx.room:room-ktx:2.6.0'
    kapt 'androidx.room:room-compiler:2.6.0'
    
    // SQLite
    implementation 'androidx.sqlite:sqlite-ktx:2.4.0'
    
    // Realm (Alternative)
    implementation 'io.realm.kotlin:library-base:1.11.0'
}
```

### SharedPreferences
```gradle
dependencies {
    // DataStore (Modern SharedPreferences)
    implementation 'androidx.datastore:datastore-preferences:1.0.0'
    
    // Encrypted SharedPreferences
    implementation 'androidx.security:security-crypto:1.1.0-alpha06'
}
```

## 🔄 Reactive Programming

### RxJava
```gradle
dependencies {
    // RxJava
    implementation 'io.reactivex.rxjava3:rxjava:3.1.7'
    implementation 'io.reactivex.rxjava3:rxandroid:3.0.2'
    implementation 'io.reactivex.rxjava3:rxkotlin:3.0.1'
    
    // RxJava with Retrofit
    implementation 'com.squareup.retrofit2:adapter-rxjava3:2.9.0'
    
    // RxJava with Room
    implementation 'androidx.room:room-rxjava3:2.6.0'
}
```

### Coroutines
```gradle
dependencies {
    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
    
    // Coroutines with Retrofit
    implementation 'com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2'
    
    // Coroutines with Room
    implementation 'androidx.room:room-ktx:2.6.0'
}
```

## 🖼️ Image Loading Dependencies

### Image Loading Libraries
```gradle
dependencies {
    // Glide
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    kapt 'com.github.bumptech.glide:compiler:4.16.0'
    
    // Picasso
    implementation 'com.squareup.picasso:picasso:2.8'
    
    // Coil (Kotlin-first)
    implementation 'io.coil-kt:coil:2.5.0'
    implementation 'io.coil-kt:coil-compose:2.5.0'
    
    // Fresco (Facebook)
    implementation 'com.facebook.fresco:fresco:3.1.3'
}
```

### Image Processing
```gradle
dependencies {
    // Image Cropping
    implementation 'com.github.yalantis:ucrop:2.2.8'
    
    // Image Compression
    implementation 'id.zelory:compressor:3.0.1'
    
    // Image Filters
    implementation 'jp.wasabeef:glide-transformations:4.3.0'
}
```

## 🔐 Security Dependencies

### Encryption and Security
```gradle
dependencies {
    // Encrypted SharedPreferences
    implementation 'androidx.security:security-crypto:1.1.0-alpha06'
    
    // Biometric Authentication
    implementation 'androidx.biometric:biometric:1.1.0'
    
    // SSL Pinning
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
}
```

## 📊 Analytics and Monitoring

### Analytics
```gradle
dependencies {
    // Firebase Analytics
    implementation 'com.google.firebase:firebase-analytics-ktx:21.4.0'
    
    // Crashlytics
    implementation 'com.google.firebase:firebase-crashlytics-ktx:18.5.1'
    
    // Performance Monitoring
    implementation 'com.google.firebase:firebase-perf-ktx:20.5.0'
}
```

### Performance Monitoring
```gradle
dependencies {
    // LeakCanary (Debug only)
    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.12'
    
    // Stetho (Debug only)
    debugImplementation 'com.facebook.stetho:stetho:1.6.0'
    debugImplementation 'com.facebook.stetho:stetho-okhttp3:1.6.0'
}
```

## 🧪 Testing Dependencies

### Unit Testing
```gradle
dependencies {
    // JUnit
    testImplementation 'junit:junit:4.13.2'
    
    // Mockito
    testImplementation 'org.mockito:mockito-core:5.3.1'
    testImplementation 'org.mockito:mockito-inline:5.2.0'
    testImplementation 'org.mockito.kotlin:mockito-kotlin:5.1.0'
    
    // Coroutines Testing
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
    
    // Robolectric
    testImplementation 'org.robolectric:robolectric:4.11.1'
}
```

### UI Testing
```gradle
dependencies {
    // Espresso
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.5.1'
    androidTestImplementation 'androidx.test.espresso:espresso-intents:3.5.1'
    
    // UI Automator
    androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.2.0'
    
    // Test Rules
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
}
```

## 🎯 Dependency Management Best Practices

### Version Management
```gradle
// build.gradle (Project level)
ext {
    // Core versions
    kotlin_version = '1.9.0'
    gradle_version = '8.1.0'
    
    // Android versions
    compileSdk_version = 34
    targetSdk_version = 34
    minSdk_version = 24
    
    // Library versions
    core_ktx_version = '1.12.0'
    appcompat_version = '1.6.1'
    material_version = '1.10.0'
    lifecycle_version = '2.6.2'
    navigation_version = '2.7.4'
    room_version = '2.6.0'
    retrofit_version = '2.9.0'
    coroutines_version = '1.7.3'
    glide_version = '4.16.0'
}

// app/build.gradle
dependencies {
    implementation "androidx.core:core-ktx:$core_ktx_version"
    implementation "androidx.appcompat:appcompat:$appcompat_version"
    implementation "com.google.android.material:material:$material_version"
    // ... more dependencies
}
```

### Dependency Resolution
```gradle
// Force specific versions
configurations.all {
    resolutionStrategy {
        force 'androidx.core:core-ktx:1.12.0'
        force 'org.jetbrains.kotlin:kotlin-stdlib:1.9.0'
    }
}

// Exclude transitive dependencies
implementation('com.example:library:1.0.0') {
    exclude group: 'com.unwanted', module: 'dependency'
}
```

### Build Variants
```gradle
android {
    buildTypes {
        debug {
            // Debug-only dependencies
            debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.12'
        }
        release {
            // Release-only dependencies
            // None typically
        }
    }
    
    flavorDimensions "version"
    productFlavors {
        free {
            dimension "version"
            // Free version dependencies
        }
        pro {
            dimension "version"
            // Pro version dependencies
        }
    }
}
```

## 🚨 Common Issues and Solutions

### Version Conflicts
```gradle
// Solution 1: Force resolution
configurations.all {
    resolutionStrategy {
        force 'androidx.core:core-ktx:1.12.0'
    }
}

// Solution 2: Exclude conflicting dependency
implementation('com.example:library:1.0.0') {
    exclude group: 'androidx.core', module: 'core-ktx'
}
```

### Missing Dependencies
```gradle
// Common missing dependencies
dependencies {
    // For ViewBinding
    implementation 'androidx.databinding:viewbinding:8.1.0'
    
    // For DataBinding
    implementation 'androidx.databinding:databinding-runtime:8.1.0'
    
    // For Compose
    implementation 'androidx.compose.ui:ui:1.5.0'
    implementation 'androidx.compose.material3:material3:1.1.1'
}
```

## 📚 Additional Resources

- [Android Developer Dependencies](https://developer.android.com/jetpack/androidx/versions)
- [Google Maven Repository](https://maven.google.com/web/index.html)
- [Gradle Dependency Management](https://docs.gradle.org/current/userguide/dependency_management.html)

---

**Remember**: Always check for the latest versions and compatibility before adding new dependencies!
