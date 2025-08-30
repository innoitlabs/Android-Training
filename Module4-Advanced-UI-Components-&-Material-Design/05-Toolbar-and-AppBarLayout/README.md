# Android Toolbar and AppBarLayout - Complete Learning Guide

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand what a Toolbar is and how it replaces the default ActionBar
- Customize a Toolbar with titles, navigation icons, and menu actions
- Use AppBarLayout (from Material Components) to create scrollable, flexible app bars
- Combine Toolbar + AppBarLayout + CoordinatorLayout for Material scrolling behaviors
- Handle menu actions with Kotlin code

## Table of Contents

1. [Introduction](#introduction)
2. [Dependencies](#dependencies)
3. [Basic Toolbar Implementation](#basic-toolbar-implementation)
4. [AppBarLayout with Scrolling Content](#appbarlayout-with-scrolling-content)
5. [Advanced Features](#advanced-features)
6. [Best Practices](#best-practices)
7. [Hands-on Lab](#hands-on-lab)
8. [Exercises](#exercises)
9. [Summary](#summary)

## Introduction

### Toolbar
- A Material Design replacement for the old ActionBar
- More customizable, can be placed anywhere in the layout
- Can host a title, navigation button (back/up), and action menus
- Provides better control over styling and positioning

### AppBarLayout
- A vertical LinearLayout from the Material library
- Typically used inside CoordinatorLayout
- Provides scroll flags for collapsing/expanding Toolbars with scrolling content
- Enables Material Design scrolling behaviors

## Dependencies

The project already includes the necessary Material Components dependency in `app/build.gradle.kts`:

```kotlin
implementation(libs.material)
```

This provides access to:
- `com.google.android.material.appbar.AppBarLayout`
- `androidx.appcompat.widget.Toolbar`
- `androidx.coordinatorlayout.widget.CoordinatorLayout`

## Basic Toolbar Implementation

### XML Layout Structure

The basic structure combines CoordinatorLayout, AppBarLayout, and Toolbar:

```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout>
    <com.google.android.material.appbar.AppBarLayout>
        <androidx.appcompat.widget.Toolbar />
    </com.google.android.material.appbar.AppBarLayout>
    
    <!-- Main content -->
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

### Key Attributes

**Toolbar Attributes:**
- `app:title` - Sets the toolbar title
- `app:titleTextColor` - Title text color
- `app:popupTheme` - Theme for overflow menu
- `android:background` - Toolbar background color

**AppBarLayout Attributes:**
- `android:theme` - Theme for the app bar
- `app:elevation` - Shadow depth

## AppBarLayout with Scrolling Content

### Scroll Flags

The `app:layout_scrollFlags` attribute controls how the Toolbar behaves during scrolling:

- `scroll` - Toolbar scrolls away with content
- `enterAlways` - Toolbar reappears immediately when scrolling up
- `enterAlwaysCollapsed` - Toolbar reappears in collapsed state
- `exitUntilCollapsed` - Toolbar collapses but doesn't disappear completely
- `snap` - Toolbar snaps to either collapsed or expanded state

### Example with RecyclerView

```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout>
    <com.google.android.material.appbar.AppBarLayout>
        <androidx.appcompat.widget.Toolbar
            app:layout_scrollFlags="scroll|enterAlways" />
    </com.google.android.material.appbar.AppBarLayout>
    
    <androidx.recyclerview.widget.RecyclerView
        app:layout_behavior="@string/appbar_scrolling_view_behavior" />
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

## Advanced Features

### Menu Integration

1. **Create Menu Resource:**
   - Place menu XML in `res/menu/`
   - Define menu items with icons and actions

2. **Inflate Menu in Activity:**
   ```kotlin
   override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.toolbar_menu, menu)
       return true
   }
   ```

3. **Handle Menu Actions:**
   ```kotlin
   override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return when (item.itemId) {
           R.id.action_settings -> {
               // Handle settings action
               true
           }
           else -> super.onOptionsItemSelected(item)
       }
   }
   ```

### Navigation Icon

Enable back/up navigation:
```kotlin
supportActionBar?.setDisplayHomeAsUpEnabled(true)
```

## Best Practices

1. **Use Toolbar instead of ActionBar** for flexibility and customization
2. **Wrap Toolbar inside AppBarLayout** when combining with scrollable content
3. **Use CoordinatorLayout** for advanced Material Design scrolling behaviors
4. **Keep menu items contextual** - only actions relevant to that screen
5. **Use appropriate scroll flags** based on your content type
6. **Test scrolling behavior** on different screen sizes

## Hands-on Lab

### Project Goals
- Create an app with a Toolbar + Settings menu item
- Add a RecyclerView with 30 dummy items
- Make Toolbar scroll away when user scrolls down, reappear when scrolling up
- Implement proper Material Design scrolling behaviors

### Implementation Steps

1. **Update Layout** - Replace basic layout with CoordinatorLayout structure
2. **Add RecyclerView** - Create adapter and populate with dummy data
3. **Configure Toolbar** - Set up title, navigation, and menu
4. **Handle Scrolling** - Implement proper scroll flags and behaviors
5. **Add Menu Actions** - Create menu resource and handle clicks

## Exercises

### Easy Level
- Change Toolbar background color & title text color
- Add different menu icons
- Modify scroll behavior flags

### Intermediate Level
- Add multiple menu actions (Search, Profile, Share)
- Implement different scroll behaviors
- Add custom toolbar styling

### Advanced Level
- Implement CollapsingToolbarLayout with ImageView header
- Add parallax scrolling effects
- Create custom toolbar animations

## Summary

### Key Concepts
- **Toolbar** → customizable ActionBar replacement
- **AppBarLayout** → enables Material scrolling and collapsing behaviors
- **CoordinatorLayout** → orchestrates scrolling interactions
- **Scroll Flags** → control how toolbar responds to scrolling

### Benefits
- Modern Material Design implementation
- Flexible and customizable UI components
- Better user experience with smooth scrolling
- Consistent with Android design guidelines

### Next Steps
- Explore CollapsingToolbarLayout for more advanced effects
- Learn about Material Design theming
- Practice with different scroll behaviors
- Implement in real-world applications

---

## Project Structure

```
ToolbarAndAppBarLayout/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/toolbarandappbarlayout/
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── menu/
│   │   │   │   └── toolbar_menu.xml
│   │   │   └── values/
│   │   │       ├── colors.xml
│   │   │       ├── strings.xml
│   │   │       └── themes.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── README.md
```

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on emulator or device
5. Test scrolling behavior and menu actions

## Verification Checklist

- [ ] Toolbar displays with correct title
- [ ] Settings menu item appears in toolbar
- [ ] Clicking Settings shows Toast message
- [ ] RecyclerView displays 30 items
- [ ] Scrolling hides/shows toolbar correctly
- [ ] No build or runtime errors
- [ ] Material Design scrolling behavior works smoothly
