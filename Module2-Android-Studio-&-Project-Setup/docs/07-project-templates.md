# 7. Project Templates and Wizards

## 🎯 What are Project Templates?

Project templates in Android Studio are pre-configured project structures that provide a starting point for different types of Android applications. They include boilerplate code, dependencies, and configurations specific to the chosen template.

### Benefits of Using Templates
- **Quick Start**: Get up and running faster
- **Best Practices**: Follow Android development conventions
- **Consistency**: Standardized project structure
- **Learning**: Understand common patterns
- **Time Saving**: Avoid repetitive setup

## 🚀 Creating a New Project

### Step 1: Launch New Project Wizard
1. **File → New → New Project**
2. **Welcome Screen**: Click "New Project"
3. **Keyboard Shortcut**: `Ctrl + Alt + Insert` (Windows/Linux) or `Cmd + Alt + Insert` (macOS)

### Step 2: Choose Project Template
```
Project Templates
├── Phone and Tablet
│   ├── Empty Activity
│   ├── Basic Activity
│   ├── Bottom Navigation Activity
│   ├── Navigation Drawer Activity
│   ├── Tabbed Activity
│   ├── Master/Detail Flow
│   └── Fullscreen Activity
├── Wear OS
│   ├── Empty Wear Activity
│   └── Watch Face
├── TV
│   ├── Empty TV Activity
│   └── Leanback Activity
└── Automotive
    └── Empty Automotive Activity
```

## 📱 Phone and Tablet Templates

### Empty Activity
**Best for**: Simple apps, learning, custom UI

```kotlin
// Generated MainActivity.kt
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

**Generated Files:**
- `MainActivity.kt` - Basic activity
- `activity_main.xml` - Simple layout
- `AndroidManifest.xml` - Basic configuration
- `build.gradle` - Essential dependencies

### Basic Activity
**Best for**: Apps with app bar, navigation, and basic structure

```kotlin
// Generated MainActivity.kt
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }
}
```

**Generated Files:**
- `MainActivity.kt` - Activity with app bar
- `activity_main.xml` - Layout with toolbar
- `content_main.xml` - Content layout
- `menu/menu_main.xml` - App bar menu
- `strings.xml` - String resources
- `themes.xml` - App themes

### Bottom Navigation Activity
**Best for**: Apps with multiple main sections

```kotlin
// Generated MainActivity.kt
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
```

**Generated Files:**
- `MainActivity.kt` - Activity with bottom navigation
- `activity_main.xml` - Layout with bottom navigation
- `mobile_navigation.xml` - Navigation graph
- `fragments/` - Home, Dashboard, Notifications fragments
- `menu/bottom_nav_menu.xml` - Bottom navigation menu

### Navigation Drawer Activity
**Best for**: Apps with complex navigation and side menu

```kotlin
// Generated MainActivity.kt
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
```

**Generated Files:**
- `MainActivity.kt` - Activity with navigation drawer
- `activity_main.xml` - Layout with drawer
- `app_bar_main.xml` - App bar layout
- `nav_header_main.xml` - Drawer header
- `mobile_navigation.xml` - Navigation graph
- `fragments/` - Home, Gallery, Slideshow fragments
- `menu/activity_main_drawer.xml` - Drawer menu

## ⚙️ Project Configuration

### Step 3: Configure Project
```yaml
# Project Configuration
Name: MyAndroidApp
Package name: com.example.myandroidapp
Save location: /path/to/project
Language: Kotlin
Minimum SDK: API 24 (Android 7.0)
Build configuration language: Kotlin DSL
Use legacy android.support libraries: No
```

### Project Name
- **Naming Convention**: Use descriptive names
- **Examples**: `TodoApp`, `WeatherApp`, `ShoppingList`
- **Avoid**: Spaces, special characters, reserved words

### Package Name
- **Format**: `com.company.appname` or `com.example.appname`
- **Convention**: Reverse domain name
- **Examples**: 
  - `com.google.android.gm` (Gmail)
  - `com.facebook.katana` (Facebook)
  - `com.example.myapp` (Learning projects)

### Minimum SDK
```yaml
# SDK Selection Guide
API 21 (Android 5.0): 98.5% of devices
API 24 (Android 7.0): 95.2% of devices
API 26 (Android 8.0): 89.1% of devices
API 29 (Android 10): 76.8% of devices
API 31 (Android 12): 58.3% of devices
API 33 (Android 13): 35.2% of devices
```

**Recommendation**: API 24 (Android 7.0) for most apps

## 🔧 Template Customization

### Modifying Generated Code
```kotlin
// Example: Customizing Basic Activity
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Custom setup
        setupToolbar()
        setupNavigation()
        setupObservers()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }
    
    private fun setupNavigation() {
        // Custom navigation setup
    }
    
    private fun setupObservers() {
        // Custom observers
    }
}
```

### Adding Dependencies
```kotlin
// app/build.gradle.kts
dependencies {
    // Template-generated dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    
    // Custom dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
}
```

## 📁 Generated Project Structure

### Empty Activity Structure
```
app/
├── src/main/
│   ├── java/com/example/myapp/
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   └── themes.xml
│   │   └── mipmap/
│   │       └── ic_launcher.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

### Basic Activity Structure
```
app/
├── src/main/
│   ├── java/com/example/myapp/
│   │   └── MainActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   └── content_main.xml
│   │   ├── menu/
│   │   │   └── menu_main.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   └── themes.xml
│   │   └── mipmap/
│   │       └── ic_launcher.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

### Navigation Activity Structure
```
app/
├── src/main/
│   ├── java/com/example/myapp/
│   │   ├── MainActivity.kt
│   │   └── ui/
│   │       ├── home/
│   │       │   ├── HomeFragment.kt
│   │       │   └── HomeViewModel.kt
│   │       ├── dashboard/
│   │       │   ├── DashboardFragment.kt
│   │       │   └── DashboardViewModel.kt
│   │       └── notifications/
│   │           ├── NotificationsFragment.kt
│   │           └── NotificationsViewModel.kt
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── fragment_home.xml
│   │   │   ├── fragment_dashboard.xml
│   │   │   └── fragment_notifications.xml
│   │   ├── menu/
│   │   │   └── bottom_nav_menu.xml
│   │   ├── navigation/
│   │   │   └── mobile_navigation.xml
│   │   └── values/
│   │       ├── colors.xml
│   │       ├── strings.xml
│   │       └── themes.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## 🎨 Template Themes and Styles

### Generated Themes
```xml
<!-- res/values/themes.xml -->
<resources>
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
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
    </style>
</resources>
```

### Customizing Themes
```xml
<!-- Custom theme -->
<resources>
    <style name="Theme.MyApp" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <!-- Custom colors -->
        <item name="colorPrimary">@color/brand_primary</item>
        <item name="colorPrimaryVariant">@color/brand_primary_dark</item>
        <item name="colorSecondary">@color/brand_accent</item>
        
        <!-- Custom attributes -->
        <item name="android:windowBackground">@color/background</item>
        <item name="android:textColorPrimary">@color/text_primary</item>
        <item name="android:textColorSecondary">@color/text_secondary</item>
    </style>
</resources>
```

## 🔄 Template Migration

### Updating Template Dependencies
```kotlin
// Check for updates
// File → Settings → Appearance & Behavior → System Settings → Updates

// Update Android Gradle Plugin
// build.gradle (Project level)
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
    }
}

// Update dependencies
// app/build.gradle.kts
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
}
```

### Migrating to New Templates
```yaml
# Migration Steps:
1. Create new project with desired template
2. Copy custom code from old project
3. Update dependencies and configurations
4. Test functionality
5. Remove old project
```

## 🚨 Template Best Practices

### Choosing the Right Template
```yaml
# Template Selection Guide:
Simple App: Empty Activity
App with Navigation: Basic Activity
Multi-section App: Bottom Navigation Activity
Complex App: Navigation Drawer Activity
Learning/Prototype: Empty Activity
Production App: Basic Activity or Navigation Activity
```

### Customization Guidelines
```yaml
# Do:
- Keep generated structure initially
- Add custom code incrementally
- Follow Android conventions
- Use meaningful names
- Document customizations

# Don't:
- Overwrite generated files completely
- Ignore template patterns
- Use inconsistent naming
- Skip documentation
```

### Project Organization
```yaml
# Recommended Structure:
app/src/main/java/com/example/myapp/
├── activities/          # Activity classes
├── fragments/           # Fragment classes
├── adapters/            # RecyclerView adapters
├── models/              # Data classes
├── repositories/        # Data repositories
├── viewmodels/          # ViewModel classes
├── utils/               # Utility classes
└── network/             # Network-related classes
```

## 📚 Additional Resources

### Official Documentation
- [Android Project Templates](https://developer.android.com/studio/projects/templates)
- [Creating New Projects](https://developer.android.com/studio/projects/create-project)
- [Project Structure](https://developer.android.com/studio/projects)

### Template Examples
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Material Design Components](https://material.io/develop/android)
- [Navigation Component](https://developer.android.com/guide/navigation)

### Best Practices
- [Android Development Best Practices](https://developer.android.com/topic/performance)
- [Kotlin Android Development](https://kotlinlang.org/docs/android-overview.html)

---

**Next**: [Resource Management and Organization](./08-resource-management.md)
