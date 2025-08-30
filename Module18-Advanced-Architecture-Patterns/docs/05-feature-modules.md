# Feature Modules & Dynamic Delivery

## Overview

Feature Modules with Dynamic Delivery allow you to deliver features on-demand to users. This approach reduces the initial app size and enables A/B testing, phased rollouts, and feature toggles.

## Benefits of Dynamic Delivery

- ✅ **Smaller Initial APK**: Core app is smaller
- ✅ **On-Demand Features**: Download features when needed
- ✅ **A/B Testing**: Test features with specific user groups
- ✅ **Phased Rollouts**: Gradually release features
- ✅ **Feature Toggles**: Enable/disable features remotely
- ✅ **Conditional Delivery**: Target specific devices or users

## Module Types for Dynamic Delivery

### 1. Install-Time Modules
- **Purpose**: Core functionality required at app launch
- **Delivery**: Included in base APK
- **Examples**: Core UI, authentication, main navigation

### 2. On-Demand Modules
- **Purpose**: Optional features downloaded when needed
- **Delivery**: Downloaded from Play Store
- **Examples**: Advanced features, premium content, experimental features

### 3. Conditional Delivery Modules
- **Purpose**: Features for specific device types or user segments
- **Delivery**: Based on device capabilities or user properties
- **Examples**: AR features for ARCore devices, premium features for VIP users

## Implementation

### 1. Module Configuration

```kotlin
// feature_premium/build.gradle.kts
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.myapp.feature_premium"
    compileSdk = 36
    
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(project(":core_ui"))
    implementation(project(":core_common"))
    implementation(project(":domain"))
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Dynamic Feature
    implementation("com.google.android.play:core:1.10.3")
}
```

### 2. App Module Configuration

```kotlin
// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.android.play.feature-delivery")
}

android {
    namespace = "com.example.myapp"
    compileSdk = 36
    
    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        compose = true
    }
    
    dynamicFeatures += setOf(":feature_premium", ":feature_ar")
}

dependencies {
    // Core modules
    implementation(project(":core_ui"))
    implementation(project(":core_common"))
    implementation(project(":domain"))
    implementation(project(":data"))
    
    // Dynamic feature support
    implementation("com.google.android.play:core:1.10.3")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.6")
    
    // Android dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")
}
```

### 3. Dynamic Feature Module

```kotlin
// feature_premium/src/main/java/com/example/myapp/feature_premium/PremiumFeatureModule.kt
@Module
@InstallIn(SingletonComponent::class)
object PremiumFeatureModule {
    
    @Provides
    @Singleton
    fun providePremiumRepository(
        apiService: ApiService,
        premiumDao: PremiumDao
    ): PremiumRepository {
        return PremiumRepositoryImpl(apiService, premiumDao)
    }
    
    @Provides
    @Singleton
    fun providePremiumUseCases(
        premiumRepository: PremiumRepository
    ): PremiumUseCases {
        return PremiumUseCases(
            getPremiumContent = GetPremiumContentUseCase(premiumRepository),
            unlockPremiumFeature = UnlockPremiumFeatureUseCase(premiumRepository)
        )
    }
}

data class PremiumUseCases(
    val getPremiumContent: GetPremiumContentUseCase,
    val unlockPremiumFeature: UnlockPremiumFeatureUseCase
)
```

## Dynamic Feature Loading

### 1. Feature Manager

```kotlin
// core_common/src/main/java/com/example/myapp/core_common/feature/FeatureManager.kt
@Singleton
class FeatureManager @Inject constructor(
    private val context: Context,
    private val splitInstallManager: SplitInstallManager
) {
    
    suspend fun loadFeature(featureName: String): Result<Unit> {
        return try {
            val request = SplitInstallRequest.newBuilder()
                .addModule(featureName)
                .build()
            
            val sessionId = splitInstallManager.startInstall(request).await()
            
            // Monitor installation progress
            monitorInstallation(sessionId)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isFeatureInstalled(featureName: String): Boolean {
        return splitInstallManager.installedModules.contains(featureName)
    }
    
    private suspend fun monitorInstallation(sessionId: Int) {
        splitInstallManager.getSessionState(sessionId).collect { state ->
            when (state.status()) {
                SplitInstallSessionStatus.INSTALLED -> {
                    // Feature installed successfully
                }
                SplitInstallSessionStatus.FAILED -> {
                    throw Exception("Feature installation failed: ${state.errorCode()}")
                }
                else -> {
                    // Handle other states (DOWNLOADING, INSTALLING, etc.)
                }
            }
        }
    }
}
```

### 2. Navigation with Dynamic Features

```kotlin
// app/src/main/java/com/example/myapp/navigation/DynamicNavigation.kt
@Composable
fun DynamicNavigation() {
    val navController = rememberNavController()
    val featureManager: FeatureManager = hiltViewModel<FeatureManager>()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onPremiumClick = {
                    navController.navigate("premium")
                }
            )
        }
        
        // Dynamic feature navigation
        dynamicComposable(
            route = "premium",
            featureName = "feature_premium"
        ) {
            PremiumScreen()
        }
        
        dynamicComposable(
            route = "ar",
            featureName = "feature_ar"
        ) {
            ArScreen()
        }
    }
}

@Composable
fun HomeScreen(onPremiumClick: () -> Unit) {
    val featureManager: FeatureManager = hiltViewModel()
    val isPremiumInstalled = featureManager.isFeatureInstalled("feature_premium")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to the App",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onPremiumClick,
            enabled = isPremiumInstalled
        ) {
            Text(
                text = if (isPremiumInstalled) "Premium Features" else "Loading Premium..."
            )
        }
    }
}
```

### 3. Feature Loading ViewModel

```kotlin
// app/src/main/java/com/example/myapp/feature/FeatureLoadingViewModel.kt
@HiltViewModel
class FeatureLoadingViewModel @Inject constructor(
    private val featureManager: FeatureManager
) : ViewModel() {
    
    private val _loadingState = MutableStateFlow<FeatureLoadingState>(FeatureLoadingState.Idle)
    val loadingState: StateFlow<FeatureLoadingState> = _loadingState.asStateFlow()
    
    fun loadFeature(featureName: String) {
        viewModelScope.launch {
            _loadingState.value = FeatureLoadingState.Loading(featureName)
            
            featureManager.loadFeature(featureName)
                .onSuccess {
                    _loadingState.value = FeatureLoadingState.Success(featureName)
                }
                .onFailure { error ->
                    _loadingState.value = FeatureLoadingState.Error(featureName, error.message)
                }
        }
    }
    
    fun isFeatureInstalled(featureName: String): Boolean {
        return featureManager.isFeatureInstalled(featureName)
    }
}

sealed class FeatureLoadingState {
    object Idle : FeatureLoadingState()
    data class Loading(val featureName: String) : FeatureLoadingState()
    data class Success(val featureName: String) : FeatureLoadingState()
    data class Error(val featureName: String, val message: String?) : FeatureLoadingState()
}
```

## Conditional Delivery

### 1. Device-Based Delivery

```kotlin
// feature_ar/build.gradle.kts
android {
    // ... other config
    
    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}

// feature_ar/src/main/AndroidManifest.xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-feature android:name="android.hardware.camera.ar" android:required="true" />
    <uses-permission android:name="android.permission.CAMERA" />
    
    <application>
        <meta-data android:name="com.google.ar.core" android:value="required" />
    </application>
</manifest>
```

### 2. User-Based Delivery

```kotlin
// core_common/src/main/java/com/example/myapp/core_common/feature/UserFeatureManager.kt
@Singleton
class UserFeatureManager @Inject constructor(
    private val featureManager: FeatureManager,
    private val userPreferences: UserPreferences,
    private val analyticsTracker: AnalyticsTracker
) {
    
    suspend fun loadFeatureForUser(featureName: String): Result<Unit> {
        return try {
            // Check if user is eligible for the feature
            if (!isUserEligible(featureName)) {
                return Result.failure(Exception("User not eligible for this feature"))
            }
            
            // Track feature access
            analyticsTracker.trackFeatureAccess(featureName)
            
            // Load the feature
            featureManager.loadFeature(featureName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun isUserEligible(featureName: String): Boolean {
        return when (featureName) {
            "feature_premium" -> userPreferences.isPremiumUser()
            "feature_beta" -> userPreferences.isBetaTester()
            "feature_ar" -> isArSupported()
            else -> true
        }
    }
    
    private fun isArSupported(): Boolean {
        return ArCoreApk.getInstance().checkAvailability(context) == ArAvailability.SUPPORTED_INSTALLED
    }
}
```

## Testing Dynamic Features

### 1. Unit Testing

```kotlin
@RunWith(MockitoJUnitRunner::class)
class FeatureManagerTest {
    
    @Mock
    private lateinit var splitInstallManager: SplitInstallManager
    
    private lateinit var featureManager: FeatureManager
    
    @Before
    fun setup() {
        featureManager = FeatureManager(context, splitInstallManager)
    }
    
    @Test
    fun `loadFeature returns success when installation succeeds`() = runTest {
        // Given
        val featureName = "feature_premium"
        val sessionId = 123
        val request = SplitInstallRequest.newBuilder().addModule(featureName).build()
        
        whenever(splitInstallManager.startInstall(request)).thenReturn(
            Tasks.forResult(sessionId)
        )
        
        // When
        val result = featureManager.loadFeature(featureName)
        
        // Then
        assertTrue(result.isSuccess)
        verify(splitInstallManager).startInstall(request)
    }
}
```

### 2. Integration Testing

```kotlin
@HiltAndroidTest
class DynamicFeatureIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var featureManager: FeatureManager
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun testFeatureLoading() = runTest {
        // Given
        val featureName = "feature_premium"
        
        // When
        val result = featureManager.loadFeature(featureName)
        
        // Then
        assertTrue(result.isSuccess)
        assertTrue(featureManager.isFeatureInstalled(featureName))
    }
}
```

## Best Practices

1. **Feature Size**: Keep dynamic features under 10MB
2. **Dependencies**: Minimize dependencies in dynamic features
3. **Error Handling**: Handle installation failures gracefully
4. **User Experience**: Show loading states during feature installation
5. **Caching**: Cache installed features to avoid re-downloading
6. **Analytics**: Track feature usage and installation success rates
7. **Testing**: Test feature installation on different devices and network conditions

## Summary

Dynamic Delivery provides:

- ✅ **Reduced app size** for initial download
- ✅ **On-demand feature loading**
- ✅ **A/B testing capabilities**
- ✅ **Conditional feature delivery**
- ✅ **Better user experience**

In the next section, we'll explore Jetpack Compose basics for modern UI development.
