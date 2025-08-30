# Navigation Component - Complete Guide

## Overview

The Navigation Component is a library that simplifies navigation between destinations in your app. It provides a framework for managing fragment transactions, handling the back stack, and passing arguments between destinations.

## Key Benefits

1. **Type Safety**: Safe Args plugin generates type-safe classes for navigation
2. **Visual Navigation**: Navigation graph editor in Android Studio
3. **Deep Linking**: Easy implementation of deep links
4. **Animation Support**: Built-in support for transitions
5. **Back Stack Management**: Automatic handling of back navigation

## Setup

### Step 1: Add Dependencies

Add to your `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
}
```

### Step 2: Add Safe Args Plugin

Add to your project-level `build.gradle.kts`:

```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin") version "2.7.7"
}
```

Apply in your app-level `build.gradle.kts`:

```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin")
}
```

## Navigation Graph

### Creating a Navigation Graph

1. Right-click on `res` folder
2. New → Navigation Resource File
3. Name it `nav_graph.xml`

### Basic Navigation Graph Structure

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
            app:destination="@id/detailFragment"
            app:enterAnim="@anim/slide_in_right"
            app:exitAnim="@anim/slide_out_left"
            app:popEnterAnim="@anim/slide_in_left"
            app:popExitAnim="@anim/slide_out_right"/>
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

### Navigation Graph Attributes

- **startDestination**: The default destination when the graph is loaded
- **label**: The title shown in the action bar
- **name**: The fully qualified class name of the Fragment

### Action Attributes

- **destination**: The destination to navigate to
- **enterAnim/exitAnim**: Animations for entering/leaving the destination
- **popEnterAnim/popExitAnim**: Animations when popping from back stack
- **popUpTo**: Clear the back stack up to a specific destination
- **inclusive**: Whether to include the popUpTo destination in the clearing

## NavHostFragment

### Setting up NavHostFragment

Add to your `activity_main.xml`:

```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/nav_host_fragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultNavHost="true"
    app:navGraph="@navigation/nav_graph"
    android:name="androidx.navigation.fragment.NavHostFragment"/>
```

### NavHostFragment Attributes

- **defaultNavHost**: Whether this NavHost should intercept the system back button
- **navGraph**: The navigation graph resource to use
- **name**: The NavHostFragment class name

## Navigation in Kotlin

### Basic Navigation

```kotlin
// Navigate to a destination
findNavController().navigate(R.id.detailFragment)

// Navigate with action
findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
```

### Navigation with Arguments

```kotlin
// Using Safe Args (recommended)
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John Doe", 123)
findNavController().navigate(action)

// Manual argument passing (not recommended)
val bundle = Bundle().apply {
    putString("username", "John Doe")
    putInt("userId", 123)
}
findNavController().navigate(R.id.detailFragment, bundle)
```

### Receiving Arguments

```kotlin
// Using Safe Args (recommended)
val args: DetailFragmentArgs by navArgs()
val username = args.username
val userId = args.userId

// Manual argument retrieval (not recommended)
val username = arguments?.getString("username")
val userId = arguments?.getInt("userId", 0)
```

## Safe Args

### Supported Argument Types

```xml
<argument android:name="stringArg" app:argType="string"/>
<argument android:name="intArg" app:argType="integer"/>
<argument android:name="longArg" app:argType="long"/>
<argument android:name="floatArg" app:argType="float"/>
<argument android:name="booleanArg" app:argType="boolean"/>
<argument android:name="stringArrayArg" app:argType="string[]"/>
<argument android:name="intArrayArg" app:argType="integer[]"/>
<argument android:name="parcelableArg" app:argType="com.example.User"/>
<argument android:name="enumArg" app:argType="com.example.UserType"/>
```

### Custom Argument Types

For custom types, implement `Parcelable`:

```kotlin
@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String
) : Parcelable
```

Or use TypeConverter for complex types:

```kotlin
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
```

Add to navigation graph:

```xml
<navigation>
    <argument
        android:name="date"
        app:argType="com.example.Converters$Date"/>
</navigation>
```

## Back Stack Management

### Pop Up To

```kotlin
// Pop up to home fragment (inclusive)
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John")
    .setPopUpTo(R.id.homeFragment, true)
findNavController().navigate(action)

// Pop up to home fragment (exclusive)
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John")
    .setPopUpTo(R.id.homeFragment, false)
findNavController().navigate(action)
```

### Clear Back Stack

```kotlin
// Clear entire back stack
findNavController().navigate(R.id.homeFragment) {
    popUpTo(0) { inclusive = true }
}
```

### Navigate and Pop

```kotlin
// Navigate and pop current destination
findNavController().navigate(R.id.detailFragment) {
    popUpTo(R.id.homeFragment) { inclusive = false }
}
```

## Deep Linking

### Implicit Deep Links

```xml
<fragment
    android:id="@+id/detailFragment"
    android:name="com.example.DetailFragment">
    
    <deepLink
        android:id="@+id/deepLink"
        app:uri="example://detail/{userId}"/>
        
    <argument
        android:name="userId"
        app:argType="integer"/>
</fragment>
```

### Explicit Deep Links

```kotlin
val deepLink = NavDeepLinkRequest.Builder
    .fromUri("example://detail/123".toUri())
    .build()

findNavController().navigate(deepLink)
```

## Navigation with Bottom Navigation

### Setup Bottom Navigation

```xml
<com.google.android.material.bottomnavigation.BottomNavigationView
    android:id="@+id/bottom_navigation"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:menu="@menu/bottom_nav_menu"/>
```

### Menu Resource

```xml
<!-- res/menu/bottom_nav_menu.xml -->
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/homeFragment"
        android:icon="@drawable/ic_home"
        android:title="Home"/>
    <item
        android:id="@+id/profileFragment"
        android:icon="@drawable/ic_profile"
        android:title="Profile"/>
    <item
        android:id="@+id/settingsFragment"
        android:icon="@drawable/ic_settings"
        android:title="Settings"/>
</menu>
```

### Connect to Navigation

```kotlin
val navController = findNavController(R.id.nav_host_fragment)
val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

bottomNav.setupWithNavController(navController)
```

## Navigation with Toolbar

### Setup Toolbar

```xml
<com.google.android.material.appbar.AppBarLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"/>

</com.google.android.material.appbar.AppBarLayout>
```

### Connect to Navigation

```kotlin
val navController = findNavController(R.id.nav_host_fragment)
val toolbar = findViewById<Toolbar>(R.id.toolbar)

setSupportActionBar(toolbar)
toolbar.setupWithNavController(navController)
```

## Navigation Patterns

### Master-Detail Flow

```xml
<navigation>
    <fragment
        android:id="@+id/listFragment"
        android:name="com.example.ListFragment">
        
        <action
            android:id="@+id/action_listFragment_to_detailFragment"
            app:destination="@id/detailFragment"/>
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.DetailFragment">
        
        <argument
            android:name="itemId"
            app:argType="integer"/>
    </fragment>
</navigation>
```

### Login Flow

```xml
<navigation>
    <fragment
        android:id="@+id/loginFragment"
        android:name="com.example.LoginFragment">
        
        <action
            android:id="@+id/action_loginFragment_to_mainFragment"
            app:destination="@id/mainFragment"
            app:popUpTo="@id/loginFragment"
            app:popUpToInclusive="true"/>
    </fragment>

    <fragment
        android:id="@+id/mainFragment"
        android:name="com.example.MainFragment"/>
</navigation>
```

## Testing Navigation

### Unit Testing

```kotlin
@Test
fun testNavigation() {
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
    navController.setGraph(R.navigation.nav_graph)
    
    val fragment = HomeFragment()
    fragment.setNavController(navController)
    
    // Test navigation
    onView(withId(R.id.navigateButton)).perform(click())
    assertEquals(R.id.detailFragment, navController.currentDestination?.id)
}
```

### UI Testing

```kotlin
@Test
fun testNavigationFlow() {
    // Navigate to detail fragment
    onView(withId(R.id.navigateButton)).perform(click())
    
    // Verify we're on detail fragment
    onView(withId(R.id.detailText)).check(matches(isDisplayed()))
    
    // Navigate back
    onView(withId(R.id.backButton)).perform(click())
    
    // Verify we're back on home fragment
    onView(withId(R.id.homeText)).check(matches(isDisplayed()))
}
```

## Best Practices

### 1. Use Safe Args

Always use Safe Args for type-safe navigation:

```kotlin
// Good
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John")
findNavController().navigate(action)

// Bad
val bundle = Bundle().apply { putString("username", "John") }
findNavController().navigate(R.id.detailFragment, bundle)
```

### 2. Handle Navigation Results

Use Fragment Result API for communication:

```kotlin
// Set result listener
setFragmentResultListener("requestKey") { key, bundle ->
    val result = bundle.getString("result")
    // Handle result
}

// Send result
val result = Bundle().apply { putString("result", "success") }
setFragmentResult("requestKey", result)
```

### 3. Use Navigation Actions

Define actions in navigation graph instead of direct navigation:

```xml
<action
    android:id="@+id/action_homeFragment_to_detailFragment"
    app:destination="@id/detailFragment"/>
```

### 4. Handle Configuration Changes

```kotlin
class MyFragment : Fragment() {
    private val args: MyFragmentArgs by navArgs()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Args are automatically restored
    }
}
```

### 5. Use Navigation Components with ViewModels

```kotlin
class MyViewModel : ViewModel() {
    private val _navigationEvent = MutableLiveData<NavigationCommand>()
    val navigationEvent: LiveData<NavigationCommand> = _navigationEvent
    
    fun navigateToDetail(userId: Int) {
        _navigationEvent.value = NavigationCommand.To(DetailFragmentDirections.actionToDetail(userId))
    }
}
```

## Common Issues and Solutions

### Issue 1: Safe Args Not Generated

**Solution**: Make sure the Safe Args plugin is applied and sync the project.

### Issue 2: Navigation Not Working

**Solution**: Check that NavHostFragment is properly configured and the navigation graph is correct.

### Issue 3: Arguments Not Received

**Solution**: Ensure arguments are defined in the navigation graph and use Safe Args.

### Issue 4: Back Stack Issues

**Solution**: Use `popUpTo` and `inclusive` attributes to control back stack behavior.

## Summary

The Navigation Component provides a robust framework for handling navigation in Android apps. Key points:

1. **Setup**: Add dependencies and Safe Args plugin
2. **Navigation Graph**: Define destinations and actions visually
3. **Safe Args**: Use for type-safe argument passing
4. **NavHostFragment**: Container for navigation
5. **Best Practices**: Follow recommended patterns for maintainable code

The Navigation Component significantly reduces boilerplate code and provides a more maintainable approach to navigation compared to manual Fragment transactions.
