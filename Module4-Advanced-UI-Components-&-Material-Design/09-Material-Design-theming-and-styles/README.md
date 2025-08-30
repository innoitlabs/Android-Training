# Material Design Theming and Styles in Android

## Table of Contents
1. [Introduction](#introduction)
2. [Learning Objectives](#learning-objectives)
3. [Prerequisites](#prerequisites)
4. [Dependencies](#dependencies)
5. [Setting Up Material Theme](#setting-up-material-theme)
6. [Creating Styles](#creating-styles)
7. [Applying Styles in Layouts](#applying-styles-in-layouts)
8. [Dark Theme Support](#dark-theme-support)
9. [Dynamic Theming](#dynamic-theming)
10. [Best Practices](#best-practices)
11. [Hands-on Lab](#hands-on-lab)
12. [Exercises](#exercises)
13. [Summary](#summary)

## Introduction

Material Design is a comprehensive design system developed by Google that provides guidelines, components, and tools for creating beautiful, functional, and consistent user interfaces across different platforms and devices.

### Key Concepts

- **Material Design**: A design system by Google for building visually consistent and usable apps
- **Themes**: Define app-wide attributes (colors, typography, shapes, elevation)
- **Styles**: Define reusable appearance rules for specific widgets
- **Material Components**: Pre-built UI components that follow Material Design guidelines

### Why Material Design Theming?

1. **Consistency**: Ensures your app looks and feels consistent across all screens
2. **Maintainability**: Centralized styling makes updates easier
3. **Accessibility**: Built-in support for accessibility features
4. **Dark Mode**: Automatic support for light and dark themes
5. **Brand Identity**: Easy to apply your brand colors and styling

## Learning Objectives

By the end of this lesson, learners will be able to:

- ✅ Understand Material Design theming principles
- ✅ Define and apply styles and themes in XML
- ✅ Use MaterialComponents Theme as a base
- ✅ Apply color schemes (primary, secondary, error, background, surface)
- ✅ Create custom styles for TextViews, Buttons, and other widgets
- ✅ Apply dark theme and dynamic theming
- ✅ Understand how themes and styles improve consistency and maintainability

## Prerequisites

- Basic knowledge of Android development
- Familiarity with XML layouts
- Understanding of Kotlin syntax
- Android Studio installed

## Dependencies

The project already includes the necessary Material Components library:

```kotlin
// In app/build.gradle.kts
implementation("com.google.android.material:material:1.12.0")
```

## Setting Up Material Theme

### 1. Base Theme Configuration

Create or update `res/values/themes.xml`:

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme -->
    <style name="Theme.MaterialDesignDemo" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Primary brand colors -->
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/white</item>
        
        <!-- Secondary brand colors -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_700</item>
        <item name="colorOnSecondary">@color/black</item>
        
        <!-- Error colors -->
        <item name="colorError">@color/red_500</item>
        <item name="colorOnError">@color/white</item>
        
        <!-- Background colors -->
        <item name="android:colorBackground">@color/white</item>
        <item name="colorSurface">@color/white</item>
        <item name="colorOnSurface">@color/black</item>
        
        <!-- Typography -->
        <item name="android:fontFamily">@font/roboto</item>
        
        <!-- Status bar and navigation bar -->
        <item name="android:statusBarColor">@color/purple_700</item>
        <item name="android:navigationBarColor">@color/purple_700</item>
    </style>
</resources>
```

### 2. Color Definitions

Create or update `res/values/colors.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Primary Colors -->
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    
    <!-- Secondary Colors -->
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    
    <!-- Error Colors -->
    <color name="red_500">#FFE53935</color>
    
    <!-- Neutral Colors -->
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="gray_100">#FFF5F5F5</color>
    <color name="gray_800">#FF424242</color>
</resources>
```

### 3. Apply Theme in AndroidManifest.xml

```xml
<application
    android:theme="@style/Theme.MaterialDesignDemo"
    ... >
```

## Creating Styles

### 1. Button Styles

Create `res/values/styles.xml`:

```xml
<resources>
    <!-- Custom Button Style -->
    <style name="Widget.MaterialDesignDemo.Button" parent="Widget.Material3.Button">
        <item name="android:padding">16dp</item>
        <item name="android:textSize">16sp</item>
        <item name="cornerRadius">24dp</item>
        <item name="android:textAllCaps">false</item>
        <item name="android:letterSpacing">0.05</item>
    </style>
    
    <!-- Outlined Button Style -->
    <style name="Widget.MaterialDesignDemo.Button.Outlined" parent="Widget.Material3.Button.OutlinedButton">
        <item name="android:padding">16dp</item>
        <item name="android:textSize">16sp</item>
        <item name="cornerRadius">24dp</item>
        <item name="android:textAllCaps">false</item>
        <item name="strokeColor">@color/purple_500</item>
    </style>
    
    <!-- Text Button Style -->
    <style name="Widget.MaterialDesignDemo.Button.Text" parent="Widget.Material3.Button.TextButton">
        <item name="android:padding">12dp</item>
        <item name="android:textSize">14sp</item>
        <item name="android:textAllCaps">false</item>
    </style>
</resources>
```

### 2. Text Styles

```xml
<resources>
    <!-- Title Text Style -->
    <style name="TextAppearance.MaterialDesignDemo.Title" parent="TextAppearance.Material3.HeadlineMedium">
        <item name="android:textColor">@color/purple_500</item>
        <item name="android:textStyle">bold</item>
        <item name="android:letterSpacing">0.02</item>
    </style>
    
    <!-- Subtitle Text Style -->
    <style name="TextAppearance.MaterialDesignDemo.Subtitle" parent="TextAppearance.Material3.TitleMedium">
        <item name="android:textColor">@color/gray_800</item>
        <item name="android:letterSpacing">0.01</item>
    </style>
    
    <!-- Body Text Style -->
    <style name="TextAppearance.MaterialDesignDemo.Body" parent="TextAppearance.Material3.BodyMedium">
        <item name="android:textColor">@color/black</item>
        <item name="android:lineSpacingMultiplier">1.2</item>
    </style>
    
    <!-- Caption Text Style -->
    <style name="TextAppearance.MaterialDesignDemo.Caption" parent="TextAppearance.Material3.BodySmall">
        <item name="android:textColor">@color/gray_800</item>
        <item name="android:alpha">0.7</item>
    </style>
</resources>
```

### 3. Card Styles

```xml
<resources>
    <!-- Material Card Style -->
    <style name="Widget.MaterialDesignDemo.Card" parent="Widget.Material3.CardView.Elevated">
        <item name="cardElevation">8dp</item>
        <item name="cardCornerRadius">16dp</item>
        <item name="cardBackgroundColor">@color/white</item>
        <item name="contentPadding">16dp</item>
    </style>
    
    <!-- Profile Card Style -->
    <style name="Widget.MaterialDesignDemo.Card.Profile" parent="Widget.MaterialDesignDemo.Card">
        <item name="cardElevation">12dp</item>
        <item name="cardCornerRadius">24dp</item>
        <item name="contentPadding">24dp</item>
    </style>
</resources>
```

## Applying Styles in Layouts

### Example Layout: Profile Screen

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/gray_100">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <!-- Header Section -->
        <TextView
            android:id="@+id/headerTitle"
            style="@style/TextAppearance.MaterialDesignDemo.Title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="8dp"
            android:text="Material Design Demo" />

        <TextView
            android:id="@+id/headerSubtitle"
            style="@style/TextAppearance.MaterialDesignDemo.Subtitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="24dp"
            android:text="Exploring themes and styles" />

        <!-- Profile Card -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/profileCard"
            style="@style/Widget.MaterialDesignDemo.Card.Profile"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/profileName"
                    style="@style/TextAppearance.MaterialDesignDemo.Title"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="8dp"
                    android:text="John Doe" />

                <TextView
                    android:id="@+id/profileRole"
                    style="@style/TextAppearance.MaterialDesignDemo.Subtitle"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="16dp"
                    android:text="Android Developer" />

                <TextView
                    android:id="@+id/profileDescription"
                    style="@style/TextAppearance.MaterialDesignDemo.Body"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="16dp"
                    android:text="Passionate about creating beautiful and functional Android applications using Material Design principles." />

                <!-- Action Buttons -->
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:gravity="end">

                    <com.google.android.material.button.MaterialButton
                        android:id="@+id/outlinedButton"
                        style="@style/Widget.MaterialDesignDemo.Button.Outlined"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginEnd="8dp"
                        android:text="Edit Profile" />

                    <com.google.android.material.button.MaterialButton
                        android:id="@+id/primaryButton"
                        style="@style/Widget.MaterialDesignDemo.Button"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Save Changes" />

                </LinearLayout>
            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Settings Section -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/settingsCard"
            style="@style/Widget.MaterialDesignDemo.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/settingsTitle"
                    style="@style/TextAppearance.MaterialDesignDemo.Subtitle"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="16dp"
                    android:text="Theme Settings" />

                <com.google.android.material.button.MaterialButton
                    android:id="@+id/themeToggleButton"
                    style="@style/Widget.MaterialDesignDemo.Button.Text"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Toggle Dark/Light Theme" />

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

        <!-- Info Section -->
        <com.google.android.material.card.MaterialCardView
            android:id="@+id/infoCard"
            style="@style/Widget.MaterialDesignDemo.Card"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <TextView
                    android:id="@+id/infoTitle"
                    style="@style/TextAppearance.MaterialDesignDemo.Subtitle"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="8dp"
                    android:text="About This Demo" />

                <TextView
                    android:id="@+id/infoDescription"
                    style="@style/TextAppearance.MaterialDesignDemo.Body"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginBottom="8dp"
                    android:text="This demo showcases Material Design theming and styling principles including custom themes, styles, and dark mode support." />

                <TextView
                    android:id="@+id/infoCaption"
                    style="@style/TextAppearance.MaterialDesignDemo.Caption"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Built with Material Components 1.12.0" />

            </LinearLayout>
        </com.google.android.material.card.MaterialCardView>

    </LinearLayout>
</ScrollView>
```

## Dark Theme Support

### 1. Dark Theme Colors

Create `res/values-night/colors.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Dark theme colors -->
    <color name="purple_200">#FFBB86FC</color>
    <color name="purple_500">#FF6200EE</color>
    <color name="purple_700">#FF3700B3</color>
    <color name="teal_200">#FF03DAC5</color>
    <color name="teal_700">#FF018786</color>
    <color name="red_500">#FFE53935</color>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="gray_100">#FF121212</color>
    <color name="gray_800">#FFE0E0E0</color>
</resources>
```

### 2. Dark Theme Configuration

Create `res/values-night/themes.xml`:

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Dark theme -->
    <style name="Theme.MaterialDesignDemo" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Primary brand colors -->
        <item name="colorPrimary">@color/purple_200</item>
        <item name="colorPrimaryVariant">@color/purple_700</item>
        <item name="colorOnPrimary">@color/black</item>
        
        <!-- Secondary brand colors -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_200</item>
        <item name="colorOnSecondary">@color/black</item>
        
        <!-- Error colors -->
        <item name="colorError">@color/red_500</item>
        <item name="colorOnError">@color/black</item>
        
        <!-- Background colors -->
        <item name="android:colorBackground">@color/gray_100</item>
        <item name="colorSurface">@color/gray_100</item>
        <item name="colorOnSurface">@color/white</item>
        
        <!-- Status bar and navigation bar -->
        <item name="android:statusBarColor">@color/black</item>
        <item name="android:navigationBarColor">@color/black</item>
    </style>
</resources>
```

## Dynamic Theming

### Kotlin Implementation

```kotlin
package com.example.materialdesign

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    
    private lateinit var themeToggleButton: MaterialButton
    private lateinit var primaryButton: MaterialButton
    private lateinit var outlinedButton: MaterialButton
    private lateinit var profileCard: MaterialCardView
    private lateinit var settingsCard: MaterialCardView
    private lateinit var infoCard: MaterialCardView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupClickListeners()
        updateUI()
    }
    
    private fun initializeViews() {
        themeToggleButton = findViewById(R.id.themeToggleButton)
        primaryButton = findViewById(R.id.primaryButton)
        outlinedButton = findViewById(R.id.outlinedButton)
        profileCard = findViewById(R.id.profileCard)
        settingsCard = findViewById(R.id.settingsCard)
        infoCard = findViewById(R.id.infoCard)
    }
    
    private fun setupClickListeners() {
        // Theme toggle functionality
        themeToggleButton.setOnClickListener {
            toggleTheme()
        }
        
        // Primary button click
        primaryButton.setOnClickListener {
            primaryButton.text = "Changes Saved!"
            // Simulate save operation
            primaryButton.isEnabled = false
            primaryButton.postDelayed({
                primaryButton.text = "Save Changes"
                primaryButton.isEnabled = true
            }, 2000)
        }
        
        // Outlined button click
        outlinedButton.setOnClickListener {
            outlinedButton.text = "Editing..."
            outlinedButton.postDelayed({
                outlinedButton.text = "Edit Profile"
            }, 1500)
        }
    }
    
    private fun toggleTheme() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
        
        AppCompatDelegate.setDefaultNightMode(newMode)
        
        // Update button text
        themeToggleButton.text = if (newMode == AppCompatDelegate.MODE_NIGHT_YES) {
            "Switch to Light Theme"
        } else {
            "Switch to Dark Theme"
        }
    }
    
    private fun updateUI() {
        // Set initial button text based on current theme
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        themeToggleButton.text = if (isDarkMode) {
            "Switch to Light Theme"
        } else {
            "Switch to Dark Theme"
        }
    }
}
```

## Best Practices

### 1. Theme Organization
- ✅ Always base themes on MaterialComponents (Theme.Material3.*)
- ✅ Define all colors in colors.xml instead of hardcoding
- ✅ Use semantic color names (primary, secondary, error, etc.)
- ✅ Provide both light and dark theme resources

### 2. Style Inheritance
- ✅ Use style inheritance to avoid duplicate code
- ✅ Create base styles and extend them for specific use cases
- ✅ Follow Material Design naming conventions

### 3. Color Usage
- ✅ Use colorPrimary for main brand elements
- ✅ Use colorSecondary for accent elements
- ✅ Use colorError for error states
- ✅ Ensure sufficient contrast ratios for accessibility

### 4. Typography
- ✅ Use Material Design type scale
- ✅ Maintain consistent text styles across the app
- ✅ Consider readability in both light and dark themes

### 5. Testing
- ✅ Test across different device sizes
- ✅ Verify dark/light theme switching
- ✅ Check accessibility with screen readers
- ✅ Test on different Android versions

## Hands-on Lab

### Mini Project: Enhanced Profile Screen

**Objective**: Build a comprehensive Material-themed profile screen with dynamic theming.

**Features to implement**:
1. ✅ Profile card with avatar, name, and role
2. ✅ Custom-styled buttons (primary, outlined, text)
3. ✅ Theme toggle functionality
4. ✅ Responsive layout with proper spacing
5. ✅ Dark/light mode support
6. ✅ Material Design typography and colors

**Learning Outcomes**:
- Understanding theme inheritance
- Creating reusable styles
- Implementing dynamic theming
- Following Material Design guidelines

## Exercises

### Easy Level
1. **Change Primary Color**: Modify the primary color of the app to blue or green
2. **Add New Button Style**: Create a new button style with different corner radius
3. **Custom Text Style**: Create a custom text style for quotes or highlights

### Intermediate Level
1. **Create Card Variations**: Add different card styles for different content types
2. **Implement Color Palette**: Create a complete color palette with primary, secondary, and accent colors
3. **Add Animation Styles**: Create styles for button press animations

### Advanced Level
1. **Dynamic Color Theming**: Implement Material You dynamic colors (Android 12+)
2. **Custom Theme Attributes**: Create custom theme attributes for brand-specific styling
3. **Accessibility Enhancements**: Add high contrast mode and large text support

## Summary

### Key Takeaways

1. **Themes vs Styles**:
   - Themes = app-wide styling rules
   - Styles = reusable component-level styles

2. **Material Design Benefits**:
   - Consistent visual language
   - Built-in accessibility features
   - Automatic dark mode support
   - Modern, professional appearance

3. **Best Practices**:
   - Always use MaterialComponents as base
   - Define colors centrally
   - Support both light and dark themes
   - Test across different devices and configurations

4. **Implementation Steps**:
   - Set up Material Components dependency
   - Configure base theme in themes.xml
   - Create custom styles in styles.xml
   - Apply styles in layouts
   - Implement dark theme support
   - Add dynamic theming functionality

### Next Steps

1. Explore more Material Design components
2. Learn about Material Design motion and animations
3. Implement Material You dynamic colors
4. Study accessibility guidelines
5. Practice creating custom themes for different app types

---

**Project Status**: ✅ Complete and ready for testing
**Compatibility**: Android API 24+ (Android 7.0+)
**Material Components**: Version 1.12.0
**Kotlin Version**: 1.9+
