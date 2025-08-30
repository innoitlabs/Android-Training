# ViewPager2 and TabLayout Integration in Android

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand the purpose of ViewPager2 in Android apps
- Integrate TabLayout with ViewPager2 for swipe navigation
- Implement FragmentStateAdapter to provide pages to ViewPager2
- Synchronize Tab titles/icons with fragments
- Follow best practices for tab navigation

## Table of Contents

1. [Introduction](#introduction)
2. [Dependencies](#dependencies)
3. [Layout Setup](#layout-setup)
4. [Fragments for Pages](#fragments-for-pages)
5. [Adapter for ViewPager2](#adapter-for-viewpager2)
6. [MainActivity Setup](#mainactivity-setup)
7. [Best Practices](#best-practices)
8. [Hands-on Lab](#hands-on-lab)
9. [Exercises](#exercises)
10. [Summary](#summary)

## Introduction

### What is ViewPager2?
ViewPager2 is a widget that allows users to swipe left/right between pages (fragments or views). It's the successor to the original ViewPager and provides better performance and more features.

### What is TabLayout?
TabLayout is a Material Design component for horizontal tabs that can be synchronized with ViewPager2 to provide a seamless navigation experience.

### Common Use Cases
- **WhatsApp**: Chats, Status, Calls tabs
- **Gmail**: Primary, Social, Promotions tabs
- **YouTube**: Home, Trending, Subscriptions tabs
- **Instagram**: Feed, Reels, IGTV tabs

## Dependencies

Ensure Material Components is added in your `build.gradle.kts` (app level):

```kotlin
dependencies {
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
}
```

## Layout Setup

### activity_main.xml
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.tabs.TabLayout
        android:id="@+id/tab_layout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:tabMode="fixed"
        app:tabGravity="fill"/>

    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/view_pager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

## Fragments for Pages

### HomeFragment.kt
```kotlin
package com.example.viewpager2tablayout

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
    android:layout_height="match_parent"
    android:background="#E8F5E8">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="24sp"
        android:textStyle="bold"
        android:textColor="#2E7D32"/>
</LinearLayout>
```

## Adapter for ViewPager2

### ViewPagerAdapter.kt
```kotlin
package com.example.viewpager2tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> DashboardFragment()
            2 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}
```

## MainActivity Setup

### MainActivity.kt
```kotlin
package com.example.viewpager2tablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Dashboard"
                2 -> tab.text = "Profile"
            }
        }.attach()
    }
}
```

## Best Practices

1. **Use FragmentStateAdapter** for dynamic fragments that may be recreated
2. **Keep 3-5 tabs maximum** for better usability
3. **Use TabLayoutMediator** to synchronize tabs with ViewPager2
4. **Combine icons + text** for better user experience
5. **Save state** if tabs contain user input
6. **Use appropriate tab modes**:
   - `fixed`: All tabs have equal width
   - `scrollable`: Tabs can scroll horizontally

## Hands-on Lab

### Mini Project: 3-Tab App
Build a complete app with:
- **Home Tab**: Display "Home Fragment" with green background
- **Dashboard Tab**: Show dummy statistics with blue background
- **Profile Tab**: Display user profile information with orange background
- **Features**: Swipe left/right and tap on tabs to switch pages

## Exercises

### Easy Level
- Add icons to the tabs using `tab.setIcon()`
- Change tab colors and styles

### Intermediate Level
- Add a 4th tab (e.g., Settings)
- Implement tab selection listeners
- Add custom tab layouts

### Advanced Level
- Integrate BottomNavigationView + ViewPager2 together
- Implement nested ViewPager2
- Add tab badges for notifications

## Summary

### Key Takeaways
- **ViewPager2** provides smooth swipe-based navigation between pages
- **TabLayout** integrates seamlessly with ViewPager2 using TabLayoutMediator
- **Fragments** are supplied by FragmentStateAdapter for better memory management
- **Best for apps** needing tabbed navigation with swipe gestures

### Common Patterns
1. **TabLayout + ViewPager2**: Most common pattern for tab navigation
2. **BottomNavigationView + ViewPager2**: For bottom tab navigation
3. **Nested ViewPager2**: For complex navigation hierarchies

### Performance Tips
- Use FragmentStateAdapter for large numbers of fragments
- Implement proper lifecycle management
- Consider using RecyclerView for simple content
- Cache fragments when appropriate

## Project Structure

```
ViewPager2TabLayout/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/viewpager2tablayout/
│   │   │   ├── MainActivity.kt
│   │   │   ├── ViewPagerAdapter.kt
│   │   │   ├── HomeFragment.kt
│   │   │   ├── DashboardFragment.kt
│   │   │   └── ProfileFragment.kt
│   │   ├── res/layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── fragment_home.xml
│   │   │   ├── fragment_dashboard.xml
│   │   │   └── fragment_profile.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── README.md
```

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on emulator or device
5. Verify:
   - Tabs are displayed correctly
   - Swiping left/right switches fragments
   - Tapping tabs also switches fragments
   - No build or runtime errors

## Troubleshooting

### Common Issues
1. **Material Design dependency missing**: Add to build.gradle.kts
2. **TabLayoutMediator not found**: Ensure correct imports
3. **Fragments not showing**: Check FragmentStateAdapter implementation
4. **Tabs not syncing**: Verify TabLayoutMediator.attach() call

### Debug Tips
- Use Logcat to monitor fragment lifecycle
- Check ViewPager2 orientation settings
- Verify tab titles are set correctly
- Test on different screen sizes
