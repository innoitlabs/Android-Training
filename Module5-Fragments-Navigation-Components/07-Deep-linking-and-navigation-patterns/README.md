# Android Deep Linking and Navigation Patterns

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand what deep links are and why they are important
- Implement explicit and implicit deep links in Android
- Configure Navigation Component with deep links
- Use pending deep links (notifications, web URLs, app links)
- Explore different navigation patterns (Single Activity, Multi-Activity, Nested Navigation Graphs)
- Handle back stack behavior when navigating with deep links

## Table of Contents

1. [Introduction](#introduction)
2. [Deep Link Setup](#deep-link-setup)
3. [Manifest Setup](#manifest-setup)
4. [Triggering Deep Links](#triggering-deep-links)
5. [Receiving Arguments](#receiving-arguments)
6. [Navigation Patterns](#navigation-patterns)
7. [Best Practices](#best-practices)
8. [Hands-on Lab](#hands-on-lab)
9. [Exercises](#exercises)
10. [Summary](#summary)

## Introduction

### What are Deep Links?

Deep linking is a mechanism to open a specific screen or content in the app using a URL, intent, or external trigger. It allows users to navigate directly to a particular section of your app from external sources like:
- Web browsers
- Notifications
- Other apps
- Search results
- Social media links

**Example**: Clicking `myapp://profile/123` should open the Profile screen for user 123.

### Navigation Patterns

Navigation patterns are strategies to organize screens and flows in Android applications:

1. **Single-Activity Pattern**: All screens as Fragments inside one Activity
   - Modern approach recommended by Google
   - Easier state management and back stack handling
   - Better integration with Navigation Component

2. **Multi-Activity Pattern**: Different Activities for major app sections
   - Traditional approach
   - More complex back stack management
   - Higher memory usage

3. **Nested Navigation Graphs**: Modularizing navigation for large apps
   - Divides navigation into logical modules
   - Improves scalability and maintainability
   - Better team collaboration

## Deep Link Setup

### Navigation Graph Configuration

The Navigation Component makes deep linking simple with the `deepLink` tag in your navigation graph:

```xml
<fragment
    android:id="@+id/detailFragment"
    android:name="com.example.deeplinking.DetailFragment"
    android:label="Detail">
    <argument
        android:name="itemId"
        app:argType="string" />
    <deepLink
        app:uri="myapp://detail/{itemId}" />
</fragment>
```

### Types of Deep Links

1. **Explicit Deep Links**: Direct navigation to a specific destination
2. **Implicit Deep Links**: Pattern-based matching for dynamic content
3. **Pending Deep Links**: Deferred navigation (e.g., from notifications)

## Manifest Setup

Configure your `AndroidManifest.xml` to handle deep links automatically:

```xml
<activity
    android:name=".MainActivity"
    android:exported="true">
    <nav-graph android:value="@navigation/nav_graph" />
</activity>
```

This allows the Navigation Component to handle deep links automatically without manual intent handling.

## Triggering Deep Links

### Using ADB (Android Debug Bridge)

Test your deep links using the command line:

```bash
adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking
```

This command opens the DetailFragment with `itemId = 42`.

### From Web Browser

You can also test deep links by typing the URI in a browser:
```
myapp://detail/42
```

### From Another App

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://detail/42"))
startActivity(intent)
```

## Receiving Arguments

### Fragment Implementation

```kotlin
class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        val textView = view.findViewById<TextView>(R.id.detailText)
        textView.text = "Item ID from Deep Link: ${args.itemId}"
        return view
    }
}
```

### Layout File

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:gravity="center"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/detailText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Detail Screen"
        android:textSize="20sp"/>
</LinearLayout>
```

## Navigation Patterns

### Single-Activity Pattern

**Advantages:**
- One Activity hosts all fragments
- Preferred with Navigation Component
- Easier to manage shared state & back stack
- Better performance and memory usage
- Consistent UI/UX

**Implementation:**
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val navController = findNavController(R.id.nav_host_fragment)
        // Navigation is handled automatically by Navigation Component
    }
}
```

### Multi-Activity Pattern

**Use Cases:**
- Legacy applications
- Different themes for different sections
- Independent app modules

**Challenges:**
- More complex back stack management
- Higher memory usage
- Harder to share data between activities

### Nested Navigation Graphs

**Benefits:**
- Modular navigation structure
- Better scalability for large projects
- Easier team collaboration
- Reusable navigation components

**Structure:**
```
nav_graph.xml (Main)
├── auth_nav_graph.xml
├── main_nav_graph.xml
└── settings_nav_graph.xml
```

## Best Practices

### Deep Link Security
- Always validate input before rendering sensitive data
- Use HTTPS for web-based deep links
- Implement proper authentication for protected content

### URI Design
- Use consistent URI patterns
- Make URIs human-readable
- Include version information if needed
- Handle missing or invalid parameters gracefully

### Testing
- Test deep links via ADB
- Test from browser
- Test from notifications
- Test with invalid URIs
- Test back stack behavior

### Back Stack Management
- Ensure users don't return to launcher unexpectedly
- Handle deep links that should replace the current back stack
- Consider using `popUpTo` and `inclusive` flags

## Hands-on Lab

### Mini Project: Deep Link Demo App

Build an app with the following features:
- **HomeFragment**: Shows a button that navigates to DetailFragment
- **DetailFragment**: Supports deep linking with `myapp://detail/{itemId}`
- **Test**: Launch with ADB deep link command

### Project Structure
```
app/
├── src/main/
│   ├── java/com/example/deeplinking/
│   │   ├── MainActivity.kt
│   │   ├── HomeFragment.kt
│   │   └── DetailFragment.kt
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── fragment_home.xml
│   │   │   └── fragment_detail.xml
│   │   └── navigation/
│   │       └── nav_graph.xml
│   └── AndroidManifest.xml
```

### Testing Steps
1. Build and run the app
2. Verify HomeFragment loads by default
3. Test button navigation to DetailFragment
4. Test deep link: `adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/42" com.example.deeplinking`
5. Verify DetailFragment opens with correct data

## Exercises

### Easy Level
- Add a deep link for HomeFragment (`myapp://home`)
- Implement a simple counter that persists across deep link navigation

### Intermediate Level
- Pass two arguments via deep link (e.g., `userId` and `postId`)
- Implement a search functionality that accepts query parameters
- Add validation for deep link arguments

### Advanced Level
- Trigger deep link from a notification using PendingIntent
- Implement web-based deep links (https://yourapp.com/detail/42)
- Create a custom URI scheme with multiple path segments
- Handle deep links that should replace the entire back stack

## Summary

### Key Takeaways
- **Deep links** let apps open specific screens from external sources (URLs, notifications, Intents)
- **Navigation Component** simplifies deep link handling with `deepLink` tags
- **Single-Activity pattern** is the modern, recommended approach
- **Nested Graphs** improve scalability for large applications
- **Proper testing** is crucial for reliable deep link functionality

### Next Steps
- Explore App Links (automatic deep linking from web URLs)
- Learn about Dynamic Links (Firebase)
- Study advanced navigation patterns
- Implement deep link analytics and tracking

### Resources
- [Android Navigation Component Documentation](https://developer.android.com/guide/navigation)
- [Deep Links Documentation](https://developer.android.com/training/app-links/deep-linking)
- [Navigation Architecture Component](https://developer.android.com/guide/navigation/navigation-getting-started)

---

**Note**: This project demonstrates the Single-Activity pattern with Navigation Component, which is the recommended approach for modern Android applications. The code is compatible with Kotlin 1.9+ and Android Studio.
