# Android Fragment Lifecycle and Navigation Component

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand the Fragment lifecycle and how it differs from Activity lifecycle
- Create and manage Fragments dynamically and statically
- Use the FragmentManager and FragmentTransaction
- Set up Navigation Component in a project
- Configure NavHostFragment, NavController, and Navigation Graph
- Pass data between fragments safely using Safe Args
- Handle back stack and navigation UI integration

## Table of Contents

1. [Introduction](#introduction)
2. [Fragment Lifecycle](#fragment-lifecycle)
3. [Fragment Example](#fragment-example)
4. [Navigation Component Setup](#navigation-component-setup)
5. [Passing Data Between Fragments](#passing-data-between-fragments)
6. [Best Practices](#best-practices)
7. [Hands-on Lab](#hands-on-lab)
8. [Exercises](#exercises)
9. [Summary](#summary)

## Introduction

### What are Fragments?
- **Fragments** are reusable portions of UI that run inside an Activity
- They have their own lifecycle, but are tied to the Activity's lifecycle
- Useful for creating modular UI components and multi-pane layouts (tablet/phone)
- Can be added, removed, or replaced at runtime

### Why Navigation Component?
- **Navigation Component** simplifies handling fragment transactions, back stack, and argument passing
- Provides type-safe navigation with Safe Args
- Handles deep linking and navigation patterns
- Reduces boilerplate code compared to manual FragmentManager usage

## Fragment Lifecycle

The Fragment lifecycle consists of several key methods that are called in sequence:

### Lifecycle Methods Overview

1. **onAttach()** - Fragment is attached to its Activity
2. **onCreate()** - Initialize non-UI components
3. **onCreateView()** - Inflate Fragment layout and return the root view
4. **onViewCreated()** - Setup UI components after view is created
5. **onStart()** - Fragment becomes visible to user
6. **onResume()** - Fragment becomes interactive
7. **onPause()** - Fragment is no longer interactive
8. **onStop()** - Fragment is no longer visible
9. **onDestroyView()** - Clean up UI-related resources
10. **onDestroy()** - Fragment is being destroyed
11. **onDetach()** - Fragment is detached from Activity

### Lifecycle States

```
INITIALIZED → ATTACHED → CREATED → VIEW_CREATED → STARTED → RESUMED
                                                              ↓
DESTROYED ← DETACHED ← DESTROYED ← VIEW_DESTROYED ← STOPPED ← PAUSED
```

### Key Differences from Activity Lifecycle

- **onCreateView()**: Unique to Fragments - inflates the layout
- **onViewCreated()**: Called after view is created, good place for UI setup
- **onDestroyView()**: Clean up view references to prevent memory leaks
- **onDetach()**: Fragment is no longer attached to Activity

## Fragment Example

### Basic Fragment Implementation

A simple Fragment that logs its lifecycle methods:

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
        // Setup UI components here
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
    }
}
```

## Navigation Component Setup

### Step 1: Add Dependencies

Add Navigation Component dependencies to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
}
```

For Safe Args, add the plugin to your project-level `build.gradle.kts`:

```kotlin
plugins {
    id("androidx.navigation.safeargs.kotlin") version "2.7.7"
}
```

### Step 2: Create Navigation Graph

Create `res/navigation/nav_graph.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
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

### Step 3: Add NavHostFragment

Update your `activity_main.xml`:

```xml
<androidx.fragment.app.FragmentContainerView
    android:id="@+id/nav_host_fragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultNavHost="true"
    app:navGraph="@navigation/nav_graph"
    android:name="androidx.navigation.fragment.NavHostFragment"/>
```

### Step 4: Navigation in Kotlin

Navigate between fragments:

```kotlin
// In HomeFragment
view.findViewById<Button>(R.id.navigateButton).setOnClickListener {
    findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
}
```

## Passing Data Between Fragments

### Using Safe Args

1. **Add arguments to navigation graph**:

```xml
<fragment
    android:id="@+id/detailFragment"
    android:name="com.example.fragementlifecycle.DetailFragment"
    android:label="Detail">
    <argument
        android:name="username"
        app:argType="string"
        android:defaultValue="Guest"/>
</fragment>
```

2. **Pass data from HomeFragment**:

```kotlin
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John Doe")
findNavController().navigate(action)
```

3. **Receive data in DetailFragment**:

```kotlin
val args: DetailFragmentArgs by navArgs()
textView.text = "Hello, ${args.username}!"
```

### Supported Argument Types

- `string`, `string[]`
- `int`, `int[]`
- `long`, `long[]`
- `float`, `float[]`
- `boolean`, `boolean[]`
- `Parcelable` objects
- Custom types (with TypeConverter)

## Best Practices

### Fragment Best Practices

1. **Keep Fragments UI-focused**: Business logic should be in ViewModel
2. **Always clean up resources**: Use `onDestroyView()` for cleanup
3. **Avoid holding Activity references**: Use `requireActivity()` when needed
4. **Use ViewBinding**: For safer view access
5. **Handle configuration changes**: Use `setRetainInstance(true)` carefully

### Navigation Best Practices

1. **Use Navigation Component**: Instead of manual FragmentTransaction
2. **Use Safe Args**: For type-safe data passing
3. **Define clear navigation patterns**: In navigation graph
4. **Handle back stack properly**: Use `popUpTo` and `inclusive` attributes
5. **Test navigation flows**: Ensure proper back navigation

### Memory Management

```kotlin
override fun onDestroyView() {
    super.onDestroyView()
    // Clear view references to prevent memory leaks
    _binding = null
}
```

## Hands-on Lab

### Mini Project: 2-Screen Navigation App

Build a simple app with:
- **Home Fragment**: Displays a button and input field
- **Detail Fragment**: Shows passed data and a back button
- **Navigation**: Using Navigation Component with Safe Args
- **Lifecycle Logging**: Track all lifecycle method calls

### Project Structure

```
app/src/main/
├── java/com/example/fragementlifecycle/
│   ├── MainActivity.kt
│   ├── HomeFragment.kt
│   └── DetailFragment.kt
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── fragment_home.xml
│   │   └── fragment_detail.xml
│   └── navigation/
│       └── nav_graph.xml
```

### Implementation Steps

1. **Update build.gradle.kts** with Navigation dependencies
2. **Create fragment layouts** with UI components
3. **Implement Fragment classes** with lifecycle logging
4. **Create navigation graph** with actions and arguments
5. **Update MainActivity** to use NavHostFragment
6. **Test the app** and verify navigation flow

## Exercises

### Easy Level
- Add a third fragment and navigate sequentially
- Implement a simple form with validation
- Add animations to fragment transitions

### Intermediate Level
- Add multiple arguments (user ID, email, etc.)
- Implement a master-detail flow for a list
- Add bottom navigation with 3 fragments

### Advanced Level
- Create a complex navigation flow with nested graphs
- Implement deep linking to specific fragments
- Add custom transitions and shared element transitions
- Use Navigation Component with ViewPager2

## Summary

### Key Takeaways

1. **Fragment Lifecycle**: Understanding the complete lifecycle is crucial for proper resource management
2. **Navigation Component**: Simplifies fragment management and provides type-safe navigation
3. **Safe Args**: Ensures type safety when passing data between fragments
4. **Best Practices**: Follow Android guidelines for memory management and UI patterns

### Common Pitfalls to Avoid

- **Memory Leaks**: Not clearing view references in `onDestroyView()`
- **Configuration Changes**: Not handling screen rotation properly
- **Navigation Errors**: Using wrong action IDs or missing arguments
- **Lifecycle Confusion**: Mixing Activity and Fragment lifecycle methods

### Next Steps

- Explore advanced Navigation Component features
- Learn about Fragment Result API
- Study ViewModel integration with Fragments
- Practice with complex navigation patterns

---

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on emulator or device
5. Test navigation flow and verify lifecycle logs

The complete working example is available in the `FragementLifecycle/` directory.
