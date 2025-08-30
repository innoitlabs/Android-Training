# Navigation Graphs and Destinations in Android

## Table of Contents
1. [Introduction](#introduction)
2. [Setup](#setup)
3. [Navigation Graph Example](#navigation-graph-example)
4. [NavHostFragment Setup](#navhostfragment-setup)
5. [Home Fragment with Navigation Action](#home-fragment-with-navigation-action)
6. [Detail Fragment](#detail-fragment)
7. [Passing Data Between Destinations with Safe Args](#passing-data-between-destinations-with-safe-args)
8. [Best Practices](#best-practices)
9. [Hands-on Lab / Mini Project](#hands-on-lab--mini-project)
10. [Exercises](#exercises)
11. [Summary](#summary)

## Introduction

### Navigation Graph
- An XML resource that defines the navigation flow in an app
- Contains destinations (screens) and actions (transitions)
- Provides a visual representation of your app's navigation structure

### Destination
- A node in the navigation graph
- Can be a Fragment, Activity, or Dialog
- Represents a screen or UI component in your app

### Benefits of Navigation Graphs
- **Visual representation** of app flow
- **Automatic back stack handling**
- **Type-safe argument passing** with Safe Args
- **Simplified navigation logic**
- **Better code organization**

## Setup

### Dependencies in build.gradle (app)

```kotlin
dependencies {
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    
    // Safe Args Plugin (add to project-level build.gradle)
    // id("androidx.navigation.safeargs.kotlin") version "2.7.7"
}
```

## Navigation Graph Example

### Step 1: Create Navigation Graph
Create `res/navigation/nav_graph.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.navigationgraphs.HomeFragment"
        android:label="Home Screen">
        <action
            android:id="@+id/action_homeFragment_to_detailFragment"
            app:destination="@id/detailFragment" />
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.navigationgraphs.DetailFragment"
        android:label="Detail Screen"/>
</navigation>
```

## NavHostFragment Setup

### activity_main.xml

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
        android:name="androidx.navigation.fragment.NavHostFragment"
        app:navGraph="@navigation/nav_graph"
        app:defaultNavHost="true" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Home Fragment with Navigation Action

### fragment_home.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Welcome to Home Screen"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="32dp"/>

    <Button
        android:id="@+id/navigateButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Go to Details"
        android:padding="16dp"/>

</LinearLayout>
```

### HomeFragment.kt

```kotlin
package com.example.navigationgraphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val button = view.findViewById<Button>(R.id.navigateButton)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
        }

        return view
    }
}
```

## Detail Fragment

### fragment_detail.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/detailText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="This is the Detail Fragment"
        android:textSize="20sp"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/usernameText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Username will appear here"
        android:textSize="18sp"/>

</LinearLayout>
```

### DetailFragment.kt

```kotlin
package com.example.navigationgraphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
}
```

## Passing Data Between Destinations with Safe Args

### Add Argument in nav_graph.xml

```xml
<fragment
    android:id="@+id/detailFragment"
    android:name="com.example.navigationgraphs.DetailFragment">
    <argument
        android:name="username"
        app:argType="string" />
</fragment>
```

### Navigation with Data in HomeFragment.kt

```kotlin
// Using Safe Args
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John Doe")
findNavController().navigate(action)

// Alternative: Using bundle
val bundle = Bundle().apply {
    putString("username", "John Doe")
}
findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
```

### Receiving Data in DetailFragment.kt

```kotlin
package com.example.navigationgraphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val textView = view.findViewById<TextView>(R.id.detailText)
        val usernameText = view.findViewById<TextView>(R.id.usernameText)
        
        textView.text = "Welcome to Detail Screen"
        usernameText.text = "Hello, ${args.username}!"
        
        return view
    }
}
```

## Best Practices

1. **Use Navigation Graph** for visualizing flows
2. **Always define a start destination**
3. **Use Safe Args** for type-safe argument passing
4. **Avoid manual fragment transactions** when using Navigation Component
5. **Keep navigation logic in fragments**, not activities
6. **Use meaningful action names** in navigation graph
7. **Handle deep linking** properly
8. **Test navigation flows** thoroughly

## Hands-on Lab / Mini Project

### Mini Project: Create a 3-screen app with Navigation Graph

**Requirements:**
- Home Fragment → navigates to Detail Fragment
- Detail Fragment → receives data via Safe Args
- Profile Fragment → added as another destination
- Bottom Navigation View connecting all fragments

**Steps:**
1. Create the navigation graph with three destinations
2. Implement all three fragments
3. Add Safe Args for data passing
4. Implement Bottom Navigation View
5. Test the complete flow

## Exercises

### Easy Level
- Add a third fragment to the graph
- Implement basic navigation between all fragments
- Add simple UI elements to each fragment

### Intermediate Level
- Pass an integer (e.g., user ID) using Safe Args
- Add transition animations between fragments
- Implement a dialog destination

### Advanced Level
- Add a BottomNavigationView and connect it to the navigation graph
- Implement deep linking
- Add navigation drawer with navigation graph
- Handle configuration changes properly

## Summary

### Key Takeaways
- **Navigation Graphs** define app navigation flow visually and declaratively
- **Destinations** can be fragments, activities, or dialogs
- **Safe Args** ensure type-safe data passing
- **Navigation Component** simplifies fragment transactions, back stack handling, and argument passing

### What You've Learned
1. How to create and configure navigation graphs
2. How to set up NavHostFragment in activities
3. How to navigate between destinations using NavController
4. How to pass data safely between destinations
5. Best practices for Android navigation

### Next Steps
- Explore advanced navigation patterns
- Learn about deep linking
- Study navigation with Compose
- Practice with real-world app scenarios

---

## Project Structure

```
NavigationGraphs/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/navigationgraphs/
│   │   │   ├── MainActivity.kt
│   │   │   ├── HomeFragment.kt
│   │   │   └── DetailFragment.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   └── fragment_detail.xml
│   │   │   └── navigation/
│   │   │       └── nav_graph.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── build.gradle.kts
```

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on an emulator or device
5. Test the navigation flow:
   - Home Fragment loads first
   - Clicking button navigates to Detail Fragment
   - Data passes correctly using Safe Args
   - Back button works properly

## Troubleshooting

### Common Issues
1. **Build errors**: Ensure all dependencies are added
2. **Navigation not working**: Check NavHostFragment setup
3. **Safe Args not generating**: Verify plugin is applied correctly
4. **Runtime crashes**: Check fragment class names in navigation graph

### Solutions
- Clean and rebuild project
- Invalidate caches and restart
- Check logcat for specific error messages
- Verify all imports are correct
