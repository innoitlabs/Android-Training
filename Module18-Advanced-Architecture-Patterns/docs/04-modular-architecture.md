# Modular App Architecture

## Overview

Modular architecture is a software design pattern that breaks down a large application into smaller, independent modules. Each module has its own responsibility and can be developed, tested, and deployed independently.

## Why Modular Architecture?

### Problems with Monolithic Apps
- ❌ **Slow Build Times**: Entire app rebuilds for small changes
- ❌ **Team Conflicts**: Multiple developers working on same module
- ❌ **Tight Coupling**: Changes in one area affect others
- ❌ **Large APK Size**: Includes unused features
- ❌ **Difficult Testing**: Hard to test individual components

### Benefits of Modular Architecture
- ✅ **Faster Builds**: Only changed modules rebuild
- ✅ **Parallel Development**: Teams can work independently
- ✅ **Loose Coupling**: Modules are independent
- ✅ **Dynamic Delivery**: Features can be downloaded on-demand
- ✅ **Better Testing**: Each module can be tested in isolation
- ✅ **Reusability**: Modules can be reused across projects

## Module Types

### 1. App Module (`:app`)
- **Purpose**: Main application entry point
- **Contains**: MainActivity, Application class, app-level dependencies
- **Dependencies**: All feature modules, core modules

### 2. Feature Modules (`:feature_*`)
- **Purpose**: Self-contained features
- **Contains**: UI, ViewModels, feature-specific logic
- **Dependencies**: Core modules, domain modules

### 3. Core Modules (`:core_*`)
- **Purpose**: Shared functionality across features
- **Contains**: Common utilities, base classes, shared resources
- **Dependencies**: Minimal dependencies

### 4. Domain Modules (`:domain`)
- **Purpose**: Business logic and entities
- **Contains**: Use cases, entities, repository interfaces
- **Dependencies**: None (pure Kotlin)

### 5. Data Modules (`:data`)
- **Purpose**: Data access and external services
- **Contains**: Repositories, API services, database
- **Dependencies**: Domain modules

## Project Structure

```
MyApp/
├── app/                           # Main application module
├── feature_home/                  # Home feature module
├── feature_profile/               # Profile feature module
├── feature_chat/                  # Chat feature module
├── feature_settings/              # Settings feature module
├── core_ui/                       # Shared UI components
├── core_network/                  # Network utilities
├── core_database/                 # Database utilities
├── core_common/                   # Common utilities
├── domain/                        # Domain layer
├── data/                          # Data layer
└── buildSrc/                      # Build configuration
```

## Module Configuration

### 1. App Module (`app/build.gradle.kts`)

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
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
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    // Feature modules
    implementation(project(":feature_home"))
    implementation(project(":feature_profile"))
    implementation(project(":feature_chat"))
    implementation(project(":feature_settings"))
    
    // Core modules
    implementation(project(":core_ui"))
    implementation(project(":core_network"))
    implementation(project(":core_database"))
    implementation(project(":core_common"))
    
    // Domain and Data
    implementation(project(":domain"))
    implementation(project(":data"))
    
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

### 2. Feature Module (`feature_home/build.gradle.kts`)

```kotlin
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.myapp.feature_home"
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
    // Core modules
    implementation(project(":core_ui"))
    implementation(project(":core_common"))
    
    // Domain
    implementation(project(":domain"))
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### 3. Core Module (`core_ui/build.gradle.kts`)

```kotlin
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.myapp.core_ui"
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
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    // Core dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}
```

## Module Communication

### 1. Navigation Between Modules

```kotlin
// Core UI - Navigation
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: Int) = "profile/$userId"
    }
    object Chat : Screen("chat/{chatId}") {
        fun createRoute(chatId: String) = "chat/$chatId"
    }
    object Settings : Screen("settings")
}

// App Module - Navigation Setup
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        // Home Feature
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        
        // Profile Feature
        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            ProfileScreen(userId = userId, navController = navController)
        }
        
        // Chat Feature
        composable(
            route = Screen.Chat.route,
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatScreen(chatId = chatId, navController = navController)
        }
        
        // Settings Feature
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
```

### 2. Dependency Injection Across Modules

```kotlin
// Core Common - Hilt Modules
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    
    @Provides
    @Singleton
    fun provideNetworkManager(): NetworkManager {
        return NetworkManagerImpl()
    }
    
    @Provides
    @Singleton
    fun provideAnalyticsTracker(): AnalyticsTracker {
        return AnalyticsTrackerImpl()
    }
}

// Feature Module - Hilt Module
@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {
    
    @Provides
    fun provideHomeRepository(
        apiService: ApiService,
        userDao: UserDao
    ): HomeRepository {
        return HomeRepositoryImpl(apiService, userDao)
    }
    
    @Provides
    fun provideGetHomeDataUseCase(
        homeRepository: HomeRepository
    ): GetHomeDataUseCase {
        return GetHomeDataUseCase(homeRepository)
    }
}
```

## Feature Module Structure

### 1. Feature Module Package Structure

```
feature_home/
├── src/main/java/com/example/myapp/feature_home/
│   ├── ui/
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   └── components/
│   │       ├── HomeHeader.kt
│   │       └── UserCard.kt
│   ├── domain/
│   │   ├── HomeRepository.kt
│   │   └── usecase/
│   │       └── GetHomeDataUseCase.kt
│   ├── data/
│   │   ├── HomeRepositoryImpl.kt
│   │   └── model/
│   │       └── HomeData.kt
│   └── di/
│       └── HomeModule.kt
└── build.gradle.kts
```

### 2. Feature Module Implementation

```kotlin
// Feature Home - UI
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }
    
    HomeScreenContent(
        uiState = uiState,
        onUserClick = { userId ->
            navController.navigate(Screen.Profile.createRoute(userId))
        },
        onSettingsClick = {
            navController.navigate(Screen.Settings.route)
        }
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onUserClick: (Int) -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeHeader(
            onSettingsClick = onSettingsClick
        )
        
        when (uiState) {
            is HomeUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is HomeUiState.Success -> {
                LazyColumn {
                    items(uiState.data.users) { user ->
                        UserCard(
                            user = user,
                            onClick = { onUserClick(user.id) }
                        )
                    }
                }
            }
            is HomeUiState.Error -> {
                ErrorMessage(
                    message = uiState.message,
                    onRetry = { /* Retry logic */ }
                )
            }
        }
    }
}

// Feature Home - ViewModel
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            getHomeDataUseCase()
                .onSuccess { homeData ->
                    _uiState.value = HomeUiState.Success(homeData)
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: HomeData) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
```

## Build Configuration

### 1. Root `build.gradle.kts`

```kotlin
buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

### 2. `settings.gradle.kts`

```kotlin
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

rootProject.name = "MyApp"

include(":app")
include(":feature_home")
include(":feature_profile")
include(":feature_chat")
include(":feature_settings")
include(":core_ui")
include(":core_network")
include(":core_database")
include(":core_common")
include(":domain")
include(":data")
```

## Testing Modular Architecture

### 1. Unit Testing Feature Modules

```kotlin
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    
    @Mock
    private lateinit var getHomeDataUseCase: GetHomeDataUseCase
    
    private lateinit var viewModel: HomeViewModel
    
    @Before
    fun setup() {
        viewModel = HomeViewModel(getHomeDataUseCase)
    }
    
    @Test
    fun `loadHomeData updates state to success when use case succeeds`() = runTest {
        // Given
        val homeData = HomeData(listOf(User(1, "John", "john@example.com")))
        whenever(getHomeDataUseCase()).thenReturn(Result.success(homeData))
        
        // When
        viewModel.loadHomeData()
        
        // Then
        val uiState = viewModel.uiState.value
        assertTrue(uiState is HomeUiState.Success)
        assertEquals(homeData, (uiState as HomeUiState.Success).data)
    }
}
```

### 2. Integration Testing

```kotlin
@HiltAndroidTest
class HomeFeatureIntegrationTest {
    
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    
    @Inject
    lateinit var getHomeDataUseCase: GetHomeDataUseCase
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun testHomeDataFlow() = runTest {
        // Given
        val expectedUsers = listOf(User(1, "John", "john@example.com"))
        
        // When
        val result = getHomeDataUseCase()
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUsers, result.getOrNull()?.users)
    }
}
```

## Best Practices

1. **Module Independence**: Each module should be self-contained
2. **Clear Dependencies**: Define clear dependency boundaries
3. **Shared Resources**: Use core modules for shared functionality
4. **Navigation**: Centralize navigation logic
5. **Testing**: Test each module independently
6. **Documentation**: Document module interfaces and dependencies
7. **Version Management**: Use consistent versioning across modules

## Summary

Modular architecture provides:

- ✅ **Scalable** application structure
- ✅ **Maintainable** codebase
- ✅ **Testable** components
- ✅ **Reusable** modules
- ✅ **Parallel development** capabilities

In the next section, we'll explore Feature Modules & Dynamic Delivery for on-demand feature loading.
