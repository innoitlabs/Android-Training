# Practical Exercises - Fragment Lifecycle and Navigation Component

## Exercise Overview

This guide provides hands-on exercises to reinforce your understanding of Fragment lifecycle and Navigation Component. Each exercise builds upon the previous one, gradually increasing in complexity.

## Prerequisites

- Android Studio installed
- Basic knowledge of Kotlin
- Understanding of Android Activities
- The project from the main guide

## Exercise 1: Basic Fragment Lifecycle Logging

### Objective
Create a simple Fragment that logs all lifecycle methods to understand the lifecycle flow.

### Steps

1. **Create a new Fragment class**:

```kotlin
class LifecycleLoggingFragment : Fragment() {
    private val TAG = "LifecycleLogging"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_lifecycle_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach called")
    }
}
```

2. **Create the layout**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Lifecycle Logging Fragment"
        android:textSize="20sp"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Check Logcat for lifecycle logs"
        android:textSize="16sp"/>

</LinearLayout>
```

3. **Test the Fragment**:
   - Add the Fragment to your Activity
   - Run the app
   - Check Logcat for lifecycle method calls
   - Try rotating the device to see configuration change behavior

### Expected Output
You should see logs like:
```
D/LifecycleLogging: onAttach called
D/LifecycleLogging: onCreate called
D/LifecycleLogging: onCreateView called
D/LifecycleLogging: onViewCreated called
D/LifecycleLogging: onStart called
D/LifecycleLogging: onResume called
```

## Exercise 2: Basic Navigation Setup

### Objective
Set up Navigation Component with two fragments and basic navigation.

### Steps

1. **Add Navigation dependencies** to `build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
}
```

2. **Create navigation graph** (`res/navigation/nav_graph.xml`):

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.fragementlifecycle.HomeFragment"
        android:label="Home">
        
        <action
            android:id="@+id/action_homeFragment_to_detailFragment"
            app:destination="@id/detailFragment"/>
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.fragementlifecycle.DetailFragment"
        android:label="Detail"/>
</navigation>
```

3. **Update MainActivity layout**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph"
        android:name="androidx.navigation.fragment.NavHostFragment"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

4. **Create HomeFragment**:

```kotlin
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        
        view.findViewById<Button>(R.id.navigateButton).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }
    }
}
```

5. **Create DetailFragment**:

```kotlin
class DetailFragment : Fragment() {
    private val TAG = "DetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        
        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
```

6. **Create layouts**:

**fragment_home.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Home Fragment"
        android:textSize="24sp"
        android:layout_marginBottom="32dp"/>

    <Button
        android:id="@+id/navigateButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Navigate to Detail"/>

</LinearLayout>
```

**fragment_detail.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Detail Fragment"
        android:textSize="24sp"
        android:layout_marginBottom="32dp"/>

    <Button
        android:id="@+id/backButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Go Back"/>

</LinearLayout>
```

### Expected Result
- App starts with HomeFragment
- Clicking "Navigate to Detail" takes you to DetailFragment
- Back button or "Go Back" returns to HomeFragment
- Lifecycle logs show proper navigation flow

## Exercise 3: Safe Args Implementation

### Objective
Implement Safe Args for type-safe data passing between fragments.

### Steps

1. **Add Safe Args plugin** to project-level `build.gradle.kts`:

```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin") version "2.7.7"
}
```

2. **Apply plugin** in app-level `build.gradle.kts`:

```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin")
}
```

3. **Update navigation graph** with arguments:

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.fragementlifecycle.HomeFragment"
        android:label="Home">
        
        <action
            android:id="@+id/action_homeFragment_to_detailFragment"
            app:destination="@id/detailFragment"/>
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.fragementlifecycle.DetailFragment"
        android:label="Detail">
        
        <argument
            android:name="username"
            app:argType="string"
            android:defaultValue="Guest"/>
            
        <argument
            android:name="userId"
            app:argType="integer"
            android:defaultValue="0"/>
    </fragment>
</navigation>
```

4. **Update HomeFragment** to pass data:

```kotlin
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        
        val usernameInput = view.findViewById<EditText>(R.id.usernameInput)
        
        view.findViewById<Button>(R.id.navigateButton).setOnClickListener {
            val username = usernameInput.text.toString().ifEmpty { "Guest" }
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(username, 123)
            findNavController().navigate(action)
        }
    }
}
```

5. **Update DetailFragment** to receive data:

```kotlin
class DetailFragment : Fragment() {
    private val TAG = "DetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        
        val args: DetailFragmentArgs by navArgs()
        val username = args.username
        val userId = args.userId
        
        val welcomeText = view.findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome, $username! (ID: $userId)"
        
        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
```

6. **Update layouts**:

**fragment_home.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Home Fragment"
        android:textSize="24sp"
        android:layout_marginBottom="32dp"/>

    <EditText
        android:id="@+id/usernameInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter username"
        android:layout_marginBottom="16dp"/>

    <Button
        android:id="@+id/navigateButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Navigate to Detail"/>

</LinearLayout>
```

**fragment_detail.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Detail Fragment"
        android:textSize="24sp"
        android:layout_marginBottom="32dp"/>

    <TextView
        android:id="@+id/welcomeText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Welcome!"
        android:textSize="18sp"
        android:layout_marginBottom="32dp"/>

    <Button
        android:id="@+id/backButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Go Back"/>

</LinearLayout>
```

### Expected Result
- Enter username in HomeFragment
- Navigate to DetailFragment with passed data
- See personalized welcome message
- Safe Args ensures type safety

## Exercise 4: Advanced Navigation Patterns

### Objective
Implement more complex navigation patterns including back stack management and animations.

### Steps

1. **Add a third fragment** to the navigation graph:

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.fragementlifecycle.HomeFragment"
        android:label="Home">
        
        <action
            android:id="@+id/action_homeFragment_to_detailFragment"
            app:destination="@id/detailFragment"/>
            
        <action
            android:id="@+id/action_homeFragment_to_settingsFragment"
            app:destination="@id/settingsFragment"/>
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.fragementlifecycle.DetailFragment"
        android:label="Detail">
        
        <argument
            android:name="username"
            app:argType="string"
            android:defaultValue="Guest"/>
            
        <argument
            android:name="userId"
            app:argType="integer"
            android:defaultValue="0"/>
            
        <action
            android:id="@+id/action_detailFragment_to_settingsFragment"
            app:destination="@id/settingsFragment"/>
    </fragment>

    <fragment
        android:id="@+id/settingsFragment"
        android:name="com.example.fragementlifecycle.SettingsFragment"
        android:label="Settings"/>
</navigation>
```

2. **Create SettingsFragment**:

```kotlin
class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        
        view.findViewById<Button>(R.id.clearBackStackButton).setOnClickListener {
            findNavController().navigate(R.id.homeFragment) {
                popUpTo(R.id.homeFragment) { inclusive = true }
            }
        }
    }
}
```

3. **Update existing fragments** to navigate to settings:

```kotlin
// In HomeFragment
view.findViewById<Button>(R.id.settingsButton).setOnClickListener {
    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
}

// In DetailFragment
view.findViewById<Button>(R.id.settingsButton).setOnClickListener {
    findNavController().navigate(R.id.action_detailFragment_to_settingsFragment)
}
```

4. **Create settings layout**:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Settings Fragment"
        android:textSize="24sp"
        android:layout_marginBottom="32dp"/>

    <Button
        android:id="@+id/clearBackStackButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Clear Back Stack"/>

</LinearLayout>
```

5. **Add animations** to navigation actions:

```xml
<action
    android:id="@+id/action_homeFragment_to_detailFragment"
    app:destination="@id/detailFragment"
    app:enterAnim="@anim/slide_in_right"
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right"/>
```

6. **Create animation files**:

**slide_in_right.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="100%"
        android:toXDelta="0%"/>
</set>
```

**slide_out_left.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="0%"
        android:toXDelta="-100%"/>
</set>
```

**slide_in_left.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="-100%"
        android:toXDelta="0%"/>
</set>
```

**slide_out_right.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="0%"
        android:toXDelta="100%"/>
</set>
```

### Expected Result
- Navigate between three fragments
- Smooth animations during transitions
- Clear back stack functionality
- Proper back navigation

## Exercise 5: Bottom Navigation Integration

### Objective
Integrate Navigation Component with Bottom Navigation View.

### Steps

1. **Update MainActivity layout** to include Bottom Navigation:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:defaultNavHost="true"
        app:navGraph="@navigation/nav_graph"
        android:name="androidx.navigation.fragment.NavHostFragment"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/bottom_navigation"/>

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu"
        app:layout_constraintBottom_toBottomOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

2. **Create bottom navigation menu** (`res/menu/bottom_nav_menu.xml`):

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/homeFragment"
        android:icon="@drawable/ic_home"
        android:title="Home"/>
    <item
        android:id="@+id/detailFragment"
        android:icon="@drawable/ic_detail"
        android:title="Detail"/>
    <item
        android:id="@+id/settingsFragment"
        android:icon="@drawable/ic_settings"
        android:title="Settings"/>
</menu>
```

3. **Update MainActivity** to connect Bottom Navigation:

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        
        bottomNav.setupWithNavController(navController)
    }
}
```

4. **Create simple drawable icons** or use existing ones:

**ic_home.xml**:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#000000"
        android:pathData="M10,20v-6h4v6h5v-8h3L12,3 2,12h3v8z"/>
</vector>
```

**ic_detail.xml**:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#000000"
        android:pathData="M19,3H5C3.9,3 3,3.9 3,5v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2V5C21,3.9 20.1,3 19,3zM19,19H5V5h14V19z"/>
    <path
        android:fillColor="#000000"
        android:pathData="M7,7h10v2h-10z"/>
    <path
        android:fillColor="#000000"
        android:pathData="M7,11h10v2h-10z"/>
    <path
        android:fillColor="#000000"
        android:pathData="M7,15h7v2h-7z"/>
</vector>
```

**ic_settings.xml**:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#000000"
        android:pathData="M19.14,12.94c0.04,-0.3 0.06,-0.61 0.06,-0.94c0,-0.32 -0.02,-0.64 -0.07,-0.94l2.03,-1.58c0.18,-0.14 0.23,-0.41 0.12,-0.61l-1.92,-3.32c-0.12,-0.22 -0.37,-0.29 -0.59,-0.22l-2.39,0.96c-0.5,-0.38 -1.03,-0.7 -1.62,-0.94L14.4,2.81c-0.04,-0.24 -0.24,-0.41 -0.48,-0.41h-3.84c-0.24,0 -0.43,0.17 -0.47,0.41L9.25,5.35C8.66,5.59 8.12,5.92 7.63,6.29L5.24,5.33c-0.22,-0.08 -0.47,0 -0.59,0.22L2.74,8.87C2.62,9.08 2.66,9.34 2.86,9.48l2.03,1.58C4.84,11.36 4.8,11.69 4.8,12s0.02,0.64 0.07,0.94l-2.03,1.58c-0.18,0.14 -0.23,0.41 -0.12,0.61l1.92,3.32c0.12,0.22 0.37,0.29 0.59,0.22l2.39,-0.96c0.5,0.38 1.03,0.7 1.62,0.94l0.36,2.54c0.05,0.24 0.24,0.41 0.48,0.41h3.84c0.24,0 0.44,-0.17 0.47,-0.41l0.36,-2.54c0.59,-0.24 1.13,-0.56 1.62,-0.94l2.39,0.96c0.22,0.08 0.47,0 0.59,-0.22l1.92,-3.32c0.12,-0.22 0.07,-0.47 -0.12,-0.61L19.14,12.94zM12,15.6c-1.98,0 -3.6,-1.62 -3.6,-3.6s1.62,-3.6 3.6,-3.6s3.6,1.62 3.6,3.6S13.98,15.6 12,15.6z"/>
</vector>
```

### Expected Result
- Bottom navigation with three tabs
- Automatic navigation between fragments
- Proper back stack management
- Visual feedback for current tab

## Exercise 6: Advanced Features

### Objective
Implement advanced Navigation Component features including deep linking and custom transitions.

### Steps

1. **Add deep linking** to navigation graph:

```xml
<fragment
    android:id="@+id/detailFragment"
    android:name="com.example.fragementlifecycle.DetailFragment"
    android:label="Detail">
    
    <argument
        android:name="username"
        app:argType="string"
        android:defaultValue="Guest"/>
        
    <argument
        android:name="userId"
        app:argType="integer"
        android:defaultValue="0"/>
        
    <deepLink
        android:id="@+id/deepLink"
        app:uri="example://detail/{username}/{userId}"/>
</fragment>
```

2. **Test deep linking**:

```kotlin
// In MainActivity or a test button
val deepLink = NavDeepLinkRequest.Builder
    .fromUri("example://detail/JohnDoe/123".toUri())
    .build()

findNavController().navigate(deepLink)
```

3. **Add custom transitions**:

```xml
<action
    android:id="@+id/action_homeFragment_to_detailFragment"
    app:destination="@id/detailFragment"
    app:enterAnim="@anim/fade_in"
    app:exitAnim="@anim/fade_out"
    app:popEnterAnim="@anim/fade_in"
    app:popExitAnim="@anim/fade_out"/>
```

4. **Create fade animations**:

**fade_in.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300"
    android:fromAlpha="0.0"
    android:toAlpha="1.0"/>
```

**fade_out.xml**:
```xml
<?xml version="1.0" encoding="utf-8"?>
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300"
    android:fromAlpha="1.0"
    android:toAlpha="0.0"/>
```

## Testing Your Implementation

### Manual Testing Checklist

1. **Basic Navigation**:
   - [ ] App starts with correct fragment
   - [ ] Navigation buttons work
   - [ ] Back button works
   - [ ] Lifecycle logs appear correctly

2. **Safe Args**:
   - [ ] Data is passed correctly
   - [ ] Default values work
   - [ ] Type safety is maintained

3. **Back Stack**:
   - [ ] Back navigation works properly
   - [ ] Clear back stack works
   - [ ] Multiple navigation paths work

4. **Animations**:
   - [ ] Transitions are smooth
   - [ ] Animations work in both directions
   - [ ] No visual glitches

5. **Bottom Navigation**:
   - [ ] Tabs switch correctly
   - [ ] Current tab is highlighted
   - [ ] Back stack is managed properly

### Automated Testing

Create unit tests for your navigation:

```kotlin
@Test
fun testNavigationToDetail() {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    navController.setGraph(R.navigation.nav_graph)
    
    val fragment = HomeFragment()
    fragment.setNavController(navController)
    
    // Test navigation
    onView(withId(R.id.navigateButton)).perform(click())
    assertEquals(R.id.detailFragment, navController.currentDestination?.id)
}
```

## Common Issues and Solutions

### Issue 1: Safe Args Not Generated
**Solution**: Sync project and rebuild after adding the plugin.

### Issue 2: Navigation Not Working
**Solution**: Check that NavHostFragment is properly configured.

### Issue 3: Animations Not Working
**Solution**: Ensure animation files are in the correct location (`res/anim/`).

### Issue 4: Bottom Navigation Issues
**Solution**: Verify menu IDs match fragment IDs in navigation graph.

## Summary

These exercises provide a comprehensive understanding of:
- Fragment lifecycle management
- Navigation Component setup and usage
- Safe Args for type-safe navigation
- Advanced navigation patterns
- Bottom navigation integration
- Deep linking and animations

Complete all exercises to gain practical experience with Fragment lifecycle and Navigation Component in Android development.
