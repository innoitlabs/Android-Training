# Android BottomNavigationView and TabLayout Tutorial

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand the role of BottomNavigationView and TabLayout in modern app navigation
- Implement a BottomNavigationView with multiple fragments
- Implement a TabLayout with ViewPager2 to swipe between tabs
- Combine navigation patterns to create multi-screen, user-friendly apps
- Follow Material Design guidelines for bottom navigation and tabs

## Table of Contents

1. [Introduction](#introduction)
2. [Dependencies](#dependencies)
3. [BottomNavigationView Implementation](#bottomnavigationview-implementation)
4. [TabLayout with ViewPager2 Implementation](#tablayout-with-viewpager2-implementation)
5. [Combined Example](#combined-example)
6. [Best Practices](#best-practices)
7. [Hands-on Lab](#hands-on-lab)
8. [Exercises](#exercises)
9. [Summary](#summary)

## Introduction

### BottomNavigationView
- Used for primary navigation between 3–5 top-level destinations in an app
- Displays persistent icons/text at the bottom of the screen
- Follows Material Design guidelines for bottom navigation
- Ideal for apps with distinct, equally important sections

### TabLayout
- Used for organizing content into categories/tabs
- Often paired with ViewPager2 for swipe navigation
- Provides horizontal scrolling tabs
- Great for organizing related content within a section

## Dependencies

The project already includes the necessary dependencies in `app/build.gradle.kts`:

```kotlin
implementation(libs.material) // Material Components including BottomNavigationView and TabLayout
```

For ViewPager2 functionality, we'll add:
```kotlin
implementation("androidx.viewpager2:viewpager2:1.1.0")
```

## BottomNavigationView Implementation

### Key Components:
1. **BottomNavigationView** - The main navigation component
2. **Menu Resource** - Defines navigation items
3. **Fragments** - Content for each navigation destination
4. **Fragment Transactions** - Switching between fragments

### Implementation Steps:
1. Create menu resource for navigation items
2. Add BottomNavigationView to layout
3. Create fragments for each destination
4. Handle navigation in MainActivity
5. Implement fragment switching logic

## TabLayout with ViewPager2 Implementation

### Key Components:
1. **TabLayout** - Horizontal tab navigation
2. **ViewPager2** - Swipeable content area
3. **ViewPagerAdapter** - Manages tab content
4. **TabLayoutMediator** - Connects tabs to ViewPager2

### Implementation Steps:
1. Add TabLayout and ViewPager2 to layout
2. Create ViewPagerAdapter
3. Create tab fragments
4. Connect TabLayout with ViewPager2 using TabLayoutMediator

## Combined Example

This project demonstrates:
- **Home Tab**: Contains TabLayout + ViewPager2 with 3 swipeable tabs
- **Search Tab**: Static fragment with search functionality
- **Profile Tab**: Static fragment with user profile

## Best Practices

### BottomNavigationView:
- Use for 3-5 top-level destinations
- Avoid nesting navigation too deep
- Use descriptive icons and labels
- Maintain consistent navigation patterns

### TabLayout:
- Use for related categories within a section
- Keep tab count reasonable (2-5 tabs)
- Use clear, concise tab labels
- Consider swipe gestures for better UX

### Combined Usage:
- BottomNavigationView for main app sections
- TabLayout for organizing content within sections
- Example: YouTube (Home/Subscriptions/Library as bottom nav, video categories as tabs)

## Hands-on Lab

### Mini Project: Multi-Navigation App

Build an app with:
- **BottomNavigationView** with 3 destinations
- **Home**: Contains TabLayout + ViewPager2 with 3 tabs
- **Search**: Static fragment with search interface
- **Profile**: Static fragment with user information

### Features to Implement:
1. Smooth navigation between bottom nav items
2. Swipeable tabs in the Home section
3. Proper fragment lifecycle management
4. Material Design styling

## Exercises

### Easy Level:
- Add a 4th tab to the TabLayout
- Change icons in BottomNavigationView
- Add custom colors to tabs

### Intermediate Level:
- Implement tab badges for notifications
- Add custom tab indicators
- Create custom tab layouts

### Advanced Level:
- Add dynamic data loading in tabs (RecyclerView)
- Implement tab state persistence
- Add tab animations and transitions

## Summary

### Key Takeaways:
- **BottomNavigationView** handles top-level navigation between major app sections
- **TabLayout + ViewPager2** organizes related content into swipeable categories
- **Combined usage** creates structured, intuitive navigation patterns
- **Material Design** provides guidelines for consistent user experience

### When to Use Each:
- **BottomNavigationView**: Main app navigation (Home, Search, Profile, etc.)
- **TabLayout**: Content organization within a section (Categories, Filters, etc.)
- **Combined**: Complex apps requiring both top-level and content-level navigation

### Next Steps:
- Explore Navigation Component for more advanced navigation
- Learn about Navigation Drawer for additional navigation patterns
- Study Material Design guidelines for comprehensive UI/UX understanding

---

## Project Structure

```
BottomNavigationViewAndTabLayout/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/bottomnavigationviewandtablayout/
│   │   │   ├── MainActivity.kt
│   │   │   ├── fragments/
│   │   │   │   ├── HomeFragment.kt
│   │   │   │   ├── SearchFragment.kt
│   │   │   │   └── ProfileFragment.kt
│   │   │   ├── adapters/
│   │   │   │   └── ViewPagerAdapter.kt
│   │   │   └── fragments/
│   │   │       └── TabFragment.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── fragment_home.xml
│   │   │   │   ├── fragment_search.xml
│   │   │   │   └── fragment_profile.xml
│   │   │   └── menu/
│   │   │       └── bottom_nav_menu.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── README.md
```

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on an emulator or device
5. Test navigation between bottom nav items and tabs

## Expected Behavior

- BottomNavigationView switches between Home, Search, and Profile
- Home contains TabLayout + ViewPager2 with 3 swipeable tabs
- Smooth transitions between fragments
- No build or runtime errors
- Material Design styling throughout
