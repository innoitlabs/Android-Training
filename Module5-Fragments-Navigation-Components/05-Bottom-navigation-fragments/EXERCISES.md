# Android Bottom Navigation - Practical Exercises

## Exercise Overview

This document contains progressive exercises to help you master Android Bottom Navigation with Fragments. Start with the basic exercises and work your way up to advanced implementations.

## Prerequisites

- Complete the basic bottom navigation setup from the main README
- Have Android Studio running with the project open
- Basic understanding of Kotlin and Android development

---

## Exercise 1: Basic Setup (Easy)

### Objective
Add a fourth tab (Settings) to the existing bottom navigation.

### Tasks
1. Create a new fragment called `SettingsFragment`
2. Create the corresponding layout `fragment_settings.xml`
3. Add a menu item for Settings in `bottom_nav_menu.xml`
4. Update `MainActivity.kt` to handle the new Settings tab

### Expected Outcome
- App should have 4 tabs: Home, Dashboard, Profile, Settings
- Settings tab should display "Settings Fragment" text
- Navigation should work smoothly between all tabs

### Solution Hints
- Follow the same pattern as existing fragments
- Use `@android:drawable/ic_menu_preferences` for the Settings icon
- Add the new case in the `when` statement in MainActivity

---

## Exercise 2: Enhanced UI (Easy)

### Objective
Improve the visual appearance of fragments with better layouts and styling.

### Tasks
1. **Home Fragment**: Add a welcome message, current date, and a simple card layout
2. **Dashboard Fragment**: Create a list of dummy data items with RecyclerView
3. **Profile Fragment**: Add user information fields (name, email, avatar placeholder)
4. **Settings Fragment**: Create a list of settings options with switches

### Expected Outcome
- Each fragment should have a more polished, professional appearance
- Dashboard should display a scrollable list of items
- Profile should show user information in a card format
- Settings should have toggle switches for various options

### Implementation Details

#### Home Fragment Enhancement
```xml
<!-- fragment_home.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:padding="16dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.card.MaterialCardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="8dp">

        <LinearLayout
            android:orientation="vertical"
            android:padding="16dp"
            android:gravity="center">

            <TextView
                android:id="@+id/welcomeText"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Welcome to Bottom Navigation Demo"
                android:textSize="24sp"
                android:textStyle="bold"
                android:layout_marginBottom="8dp"/>

            <TextView
                android:id="@+id/dateText"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="16sp"
                android:textColor="@android:color/darker_gray"/>

        </LinearLayout>

    </com.google.android.material.card.MaterialCardView>

</LinearLayout>
```

#### Dashboard Fragment with RecyclerView
```kotlin
// DashboardFragment.kt
class DashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        val items = listOf(
            "Dashboard Item 1",
            "Dashboard Item 2", 
            "Dashboard Item 3",
            "Dashboard Item 4",
            "Dashboard Item 5"
        )
        
        val adapter = DashboardAdapter(items)
        recyclerView.adapter = adapter
        
        return view
    }
}
```

---

## Exercise 3: State Preservation (Intermediate)

### Objective
Implement fragment state preservation so that user input and scroll positions are maintained when switching tabs.

### Tasks
1. Add form fields to Profile fragment (name, email, bio)
2. Implement state saving for user input
3. Preserve RecyclerView scroll position in Dashboard
4. Add a counter in Home fragment that persists across tab switches

### Expected Outcome
- User input in Profile fragment should be preserved
- Dashboard list should maintain scroll position
- Home fragment counter should not reset when switching tabs

### Implementation Approach

#### State Preservation in Profile Fragment
```kotlin
class ProfileFragment : Fragment() {
    private var nameText: String = ""
    private var emailText: String = ""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        
        // Restore state if available
        savedInstanceState?.let {
            nameText = it.getString("name", "")
            emailText = it.getString("email", "")
        }
        
        nameEditText.setText(nameText)
        emailEditText.setText(emailText)
        
        // Save state on text changes
        nameEditText.addTextChangedListener { text ->
            nameText = text.toString()
        }
        
        emailEditText.addTextChangedListener { text ->
            emailText = text.toString()
        }
        
        return view
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("name", nameText)
        outState.putString("email", emailText)
    }
}
```

---

## Exercise 4: Fragment Animations (Intermediate)

### Objective
Add smooth transition animations when switching between fragments.

### Tasks
1. Create custom fragment transition animations
2. Implement slide animations for fragment changes
3. Add fade animations for fragment entry/exit
4. Ensure animations work in both directions

### Expected Outcome
- Smooth slide animations when switching tabs
- Professional-looking transitions
- Consistent animation behavior across all fragment switches

### Implementation Details

#### Animation Resources
Create `res/anim/` folder and add animation files:

```xml
<!-- slide_in_right.xml -->
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="100%"
        android:toXDelta="0%"/>
</set>

<!-- slide_out_left.xml -->
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="0%"
        android:toXDelta="-100%"/>
</set>
```

#### Updated MainActivity with Animations
```kotlin
private fun loadFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        .replace(R.id.fragment_container, fragment)
        .commit()
}
```

---

## Exercise 5: Badge Notifications (Intermediate)

### Objective
Add badge notifications to bottom navigation items to show unread counts or notifications.

### Tasks
1. Add badge support to BottomNavigationView
2. Implement notification counters for each tab
3. Create methods to update badge counts dynamically
4. Add visual indicators for new content

### Expected Outcome
- Badge numbers appear on navigation items
- Badge counts can be updated programmatically
- Visual feedback for new content or notifications

### Implementation Details

#### Badge Implementation
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        bottomNav = findViewById(R.id.bottom_navigation)
        setupBadges()
        
        // ... existing code
    }
    
    private fun setupBadges() {
        val badge = bottomNav.getOrCreateBadge(R.id.nav_dashboard)
        badge.number = 5
        badge.isVisible = true
        
        val profileBadge = bottomNav.getOrCreateBadge(R.id.nav_profile)
        profileBadge.number = 2
        profileBadge.isVisible = true
    }
    
    fun updateBadgeCount(itemId: Int, count: Int) {
        val badge = bottomNav.getBadge(itemId)
        badge?.let {
            it.number = count
            it.isVisible = count > 0
        }
    }
}
```

---

## Exercise 6: Navigation Component Integration (Advanced)

### Objective
Migrate from manual fragment management to Navigation Component for more advanced navigation features.

### Tasks
1. Add Navigation Component dependencies
2. Create navigation graph
3. Update MainActivity to use Navigation Component
4. Implement deep linking support
5. Add fragment arguments and safe args

### Expected Outcome
- More robust navigation system
- Support for deep linking
- Better fragment lifecycle management
- Type-safe navigation with safe args

### Implementation Steps

#### 1. Add Dependencies
```kotlin
// app/build.gradle.kts
dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
}
```

#### 2. Create Navigation Graph
```xml
<!-- res/navigation/nav_graph.xml -->
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/nav_home">

    <fragment
        android:id="@+id/nav_home"
        android:name="com.example.bottomnavigation.HomeFragment"
        android:label="Home" />

    <fragment
        android:id="@+id/nav_dashboard"
        android:name="com.example.bottomnavigation.DashboardFragment"
        android:label="Dashboard" />

    <fragment
        android:id="@+id/nav_profile"
        android:name="com.example.bottomnavigation.ProfileFragment"
        android:label="Profile" />

    <fragment
        android:id="@+id/nav_settings"
        android:name="com.example.bottomnavigation.SettingsFragment"
        android:label="Settings" />

</navigation>
```

#### 3. Updated MainActivity
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
    }
}
```

---

## Exercise 7: Advanced Features (Advanced)

### Objective
Implement advanced bottom navigation features including custom behaviors and integrations.

### Tasks
1. Add ViewPager2 integration with bottom navigation
2. Implement custom tab selection behavior
3. Add fragment back stack management
4. Create custom bottom navigation styling
5. Implement accessibility features

### Expected Outcome
- Swipe gestures work with bottom navigation
- Custom styling and animations
- Proper back stack handling
- Full accessibility support

---

## Testing Your Exercises

### Manual Testing Checklist
- [ ] All tabs navigate correctly
- [ ] Fragment state is preserved
- [ ] Animations work smoothly
- [ ] Badge notifications display properly
- [ ] UI looks good on different screen sizes
- [ ] No memory leaks or crashes

### Automated Testing
Create unit tests for your fragment logic and UI tests for navigation interactions.

---

## Submission Guidelines

For each exercise:
1. Implement the required functionality
2. Test thoroughly on different devices/screen sizes
3. Document any challenges faced and solutions implemented
4. Be prepared to explain your implementation approach

## Resources

- [Navigation Component Documentation](https://developer.android.com/guide/navigation)
- [Material Design Components](https://material.io/components/bottom-navigation)
- [Fragment Lifecycle](https://developer.android.com/guide/fragments/lifecycle)
- [Android Testing Guide](https://developer.android.com/training/testing)
