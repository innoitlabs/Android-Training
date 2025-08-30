# Android Bottom Navigation with Fragments - Complete Learning Guide

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand the purpose of BottomNavigationView in Material Design
- Configure Bottom Navigation with Fragments
- Switch fragments dynamically based on menu item selection
- Maintain fragment state when switching tabs
- Follow best practices for bottom navigation implementation

## Table of Contents

1. [Introduction](#introduction)
2. [Dependencies](#dependencies)
3. [Layout Setup](#layout-setup)
4. [Menu Setup](#menu-setup)
5. [Fragments](#fragments)
6. [MainActivity Implementation](#mainactivity-implementation)
7. [Best Practices](#best-practices)
8. [Hands-on Lab](#hands-on-lab)
9. [Exercises](#exercises)
10. [Summary](#summary)

## Introduction

**BottomNavigationView** is a Material Design component that provides quick access to top-level app sections. It's commonly used in apps like YouTube, Instagram, and Gmail to organize primary navigation.

### Key Features:
- Typically hosts 3–5 fragments
- Provides consistent navigation across app sections
- Follows Material Design guidelines
- Supports icons and labels for each section

## Dependencies

Add the following dependency to your `app/build.gradle.kts`:

```kotlin
implementation("com.google.android.material:material:1.12.0")
```

## Layout Setup

### activity_main.xml

The main layout consists of:
- A `FrameLayout` to host fragments
- A `BottomNavigationView` for navigation

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu" />
</LinearLayout>
```

## Menu Setup

### res/menu/bottom_nav_menu.xml

Define navigation items with icons and labels:

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/nav_home"
        android:title="Home"
        android:icon="@android:drawable/ic_menu_view"/>
    <item
        android:id="@+id/nav_dashboard"
        android:title="Dashboard"
        android:icon="@android:drawable/ic_menu_compass"/>
    <item
        android:id="@+id/nav_profile"
        android:title="Profile"
        android:icon="@android:drawable/ic_menu_myplaces"/>
</menu>
```

## Fragments

### HomeFragment.kt

```kotlin
package com.example.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<TextView>(R.id.textView).text = "Home Fragment"
        return view
    }
}
```

### fragment_home.xml

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"/>
</LinearLayout>
```

### DashboardFragment.kt

```kotlin
package com.example.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView

class DashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        view.findViewById<TextView>(R.id.textView).text = "Dashboard Fragment"
        return view
    }
}
```

### fragment_dashboard.xml

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"/>
</LinearLayout>
```

### ProfileFragment.kt

```kotlin
package com.example.bottomnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view.findViewById<TextView>(R.id.textView).text = "Profile Fragment"
        return view
    }
}
```

### fragment_profile.xml

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="20sp"/>
</LinearLayout>
```

## MainActivity Implementation

### MainActivity.kt

```kotlin
package com.example.bottomnavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_dashboard -> loadFragment(DashboardFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
```

## Best Practices

1. **Navigation Items**: Use 3–5 fragments for bottom navigation (avoid clutter)
2. **Fragment Design**: Keep fragments lightweight and UI-focused
3. **State Management**: Save fragment state if needed using FragmentManager
4. **Icons and Labels**: Follow Material Design icon and label guidelines
5. **Performance**: Use `replace()` instead of `add()` to avoid fragment stacking
6. **Accessibility**: Ensure proper content descriptions for screen readers

## Hands-on Lab

### Mini Project: Enhanced Bottom Navigation App

Create a bottom navigation app with the following features:

1. **Home Fragment**: Displays welcome text and current date
2. **Dashboard Fragment**: Displays a list of dummy data items
3. **Profile Fragment**: Displays user information with edit functionality

### Advanced Features to Implement:

- Fragment state preservation
- Custom animations for fragment transitions
- Badge notifications on navigation items
- Deep linking support

## Exercises

### Easy Level
- Add a 4th tab (Settings) with a settings fragment
- Implement custom colors for the bottom navigation

### Intermediate Level
- Preserve fragment state when switching tabs
- Add fragment transition animations
- Implement badge notifications

### Advanced Level
- Integrate Navigation Component with BottomNavigationView
- Implement deep linking to specific fragments
- Add fragment back stack management

## Summary

### Key Takeaways:

1. **BottomNavigationView** is ideal for top-level navigation in Android apps
2. Each menu item maps to a fragment destination
3. Use `FragmentTransaction.replace()` to switch fragments efficiently
4. Follow Material Design best practices for optimal user experience
5. Consider state management for complex fragment interactions

### Next Steps:

- Explore Navigation Component for more advanced navigation patterns
- Learn about ViewPager2 integration with bottom navigation
- Study fragment lifecycle management
- Practice implementing custom navigation patterns

## Project Structure

```
BottomNavigation/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/bottomnavigation/
│   │   │   ├── MainActivity.kt
│   │   │   ├── HomeFragment.kt
│   │   │   ├── DashboardFragment.kt
│   │   │   └── ProfileFragment.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   ├── fragment_dashboard.xml
│   │   │   │   └── fragment_profile.xml
│   │   │   └── menu/
│   │   │       └── bottom_nav_menu.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── build.gradle.kts
```

## Testing Instructions

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on an emulator or device
5. Verify:
   - Home fragment loads by default
   - Clicking tabs switches fragments correctly
   - No build or runtime errors occur
   - UI elements are properly displayed

## Troubleshooting

### Common Issues:

1. **Build Errors**: Ensure Material Design dependency is added
2. **Runtime Errors**: Check fragment class names and layout IDs
3. **UI Issues**: Verify layout constraints and fragment container setup
4. **Navigation Problems**: Confirm menu item IDs match fragment loading logic

### Debug Tips:

- Use Logcat to monitor fragment lifecycle events
- Test on different screen sizes and orientations
- Verify resource files are properly named and located
