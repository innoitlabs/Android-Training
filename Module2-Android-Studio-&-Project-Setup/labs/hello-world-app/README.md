# Lab Project: Hello World App

## 🎯 Project Overview

Welcome to your first Android app! This hands-on lab will guide you through creating a simple "Hello World" application using Android Studio and Kotlin. You'll learn the fundamentals of Android development while building a functional app.

### Learning Objectives
- Create a new Android project using Android Studio
- Understand the basic project structure
- Write Kotlin code for Android
- Design a simple user interface
- Run the app on an emulator
- Use Git for version control
- Debug with Logcat

## 📋 Prerequisites

Before starting this lab, ensure you have:
- ✅ Completed Module 1 (Kotlin Fundamentals)
- ✅ Installed Android Studio (latest version)
- ✅ Set up Android SDK
- ✅ Created an Android Virtual Device (AVD)
- ✅ Basic understanding of Git

## 🚀 Step-by-Step Instructions

### Step 1: Create a New Project

1. **Launch Android Studio**
2. **Click "New Project"** or go to `File → New → New Project`
3. **Select "Empty Activity"** template
4. **Configure the project:**
   - **Name**: `HelloWorldApp`
   - **Package name**: `com.example.helloworldapp`
   - **Save location**: Choose your preferred directory
   - **Language**: `Kotlin`
   - **Minimum SDK**: `API 24 (Android 7.0)`
5. **Click "Finish"**

### Step 2: Explore Project Structure

Take a moment to explore the generated project:

```
HelloWorldApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/helloworldapp/
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── mipmap/
│   │   │       └── ic_launcher.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### Step 3: Configure Git Repository

1. **Enable Version Control**: `VCS → Enable Version Control Integration`
2. **Select Git** and click "OK"
3. **Make initial commit**: `VCS → Commit`
4. **Add commit message**: "Initial commit: Hello World app setup"
5. **Click "Commit"**

### Step 4: Modify the Layout

Open `app/src/main/res/layout/activity_main.xml` and replace the content with:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_world"
        android:textSize="24sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toTopOf="@+id/button"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <Button
        android:id="@+id/button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/click_me"
        android:padding="16dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### Step 5: Update String Resources

Open `app/src/main/res/values/strings.xml` and update it:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">Hello World App</string>
    <string name="hello_world">Hello, World!</string>
    <string name="click_me">Click Me!</string>
    <string name="button_clicked">Hello, Kotlin!</string>
</resources>
```

### Step 6: Implement Button Click Functionality

Open `app/src/main/java/com/example/helloworldapp/MainActivity.kt` and replace the content with:

```kotlin
package com.example.helloworldapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.util.Log

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        Log.d(TAG, "MainActivity created")
        setupUI()
    }
    
    private fun setupUI() {
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        
        button.setOnClickListener {
            Log.d(TAG, "Button clicked!")
            textView.text = getString(R.string.button_clicked)
        }
    }
}
```

### Step 7: Run the App

1. **Create an AVD** (if not already done):
   - `Tools → AVD Manager`
   - `Create Virtual Device`
   - Select "Pixel 6" and "API 34"
   - Click "Finish"

2. **Run the app**:
   - Click the green "Run" button (▶️)
   - Select your AVD
   - Click "OK"

3. **Wait for the app to launch** on the emulator

### Step 8: Test the App

1. **Verify the app launches** and shows "Hello, World!"
2. **Click the "Click Me!" button**
3. **Observe the text changes** to "Hello, Kotlin!"
4. **Check Logcat** for the debug messages

### Step 9: Debug with Logcat

1. **Open Logcat**: `View → Tool Windows → Logcat`
2. **Filter by your app**: Select your app package
3. **Click the button** and observe the log messages
4. **Look for**: "MainActivity created" and "Button clicked!"

### Step 10: Commit Your Changes

1. **Open Version Control**: `VCS → Commit`
2. **Review changes**: See modified files
3. **Add commit message**: "feat: implement button click functionality"
4. **Click "Commit"**

## 🔍 Understanding the Code

### MainActivity.kt Breakdown

```kotlin
// Package declaration
package com.example.helloworldapp

// Import statements
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.util.Log

// Main activity class
class MainActivity : AppCompatActivity() {
    
    // Companion object for constants
    companion object {
        private const val TAG = "MainActivity"
    }
    
    // Called when activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Log debug message
        Log.d(TAG, "MainActivity created")
        setupUI()
    }
    
    // Setup user interface
    private fun setupUI() {
        // Find views by ID
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        
        // Set click listener
        button.setOnClickListener {
            Log.d(TAG, "Button clicked!")
            textView.text = getString(R.string.button_clicked)
        }
    }
}
```

### Key Concepts Explained

1. **Activity**: The main entry point of your app
2. **onCreate()**: Called when the activity is first created
3. **setContentView()**: Sets the layout for the activity
4. **findViewById()**: Finds UI elements by their ID
5. **setOnClickListener()**: Handles button clicks
6. **Log.d()**: Outputs debug messages to Logcat
7. **getString()**: Retrieves string resources

## 🎯 Exercises

### Easy Level
1. **Change button text** to "Press Me!"
2. **Add another TextView** below the button
3. **Update the layout** to accommodate the new TextView

### Intermediate Level
1. **Create a second button** that changes text color
2. **Add a counter** that increments with each button click
3. **Display the counter value** in a TextView

### Advanced Level
1. **Add different colors** for each button click
2. **Implement a reset button** to clear the counter
3. **Save the counter value** using SharedPreferences
4. **Add animations** when buttons are clicked

## 🚨 Troubleshooting

### Common Issues

#### App Won't Build
```bash
# Solution 1: Sync Gradle
File → Sync Project with Gradle Files

# Solution 2: Clean and rebuild
Build → Clean Project
Build → Rebuild Project

# Solution 3: Invalidate caches
File → Invalidate Caches and Restart
```

#### Emulator Won't Start
```bash
# Solution 1: Check AVD settings
Tools → AVD Manager → Edit AVD

# Solution 2: Increase memory allocation
# Edit AVD → Advanced Settings → Memory

# Solution 3: Enable hardware acceleration
# Graphics: Hardware - GLES 2.0
```

#### Button Not Responding
```bash
# Solution 1: Check layout file
# Ensure button has correct ID

# Solution 2: Check MainActivity.kt
# Ensure findViewById uses correct ID

# Solution 3: Check Logcat for errors
# Look for exception messages
```

#### Logcat Not Showing Logs
```bash
# Solution 1: Check device selection
# Select correct device in Logcat

# Solution 2: Check log level
# Ensure Debug level is selected

# Solution 3: Clear Logcat
# Click "Clear logcat" button
```

## 📊 Assessment Criteria

### Basic Requirements (Pass)
- ✅ Project creates successfully
- ✅ App runs on emulator
- ✅ Button click changes text
- ✅ Logcat shows debug messages
- ✅ Git repository initialized

### Intermediate Requirements (Good)
- ✅ Code is well-commented
- ✅ Proper error handling
- ✅ Clean project structure
- ✅ Meaningful commit messages
- ✅ Exercises completed

### Advanced Requirements (Excellent)
- ✅ All exercises completed
- ✅ Code follows best practices
- ✅ UI is responsive and attractive
- ✅ Proper resource management
- ✅ Documentation is complete

## 📚 Additional Resources

### Documentation
- [Android Developer Guide](https://developer.android.com/guide)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [Android Studio User Guide](https://developer.android.com/studio/intro)

### Next Steps
- **Module 3**: UI Fundamentals and Layouts
- **Module 4**: Activities and Lifecycle
- **Module 5**: User Input and Events

## 🎉 Congratulations!

You've successfully created your first Android app! You now understand:
- How to create Android projects
- Basic Kotlin syntax for Android
- UI layout fundamentals
- Debugging with Logcat
- Version control with Git

**Next Module**: UI Fundamentals and Layouts - Learn about different layout types, UI components, and responsive design.

---

**Project Files**: All project files are included in this directory for reference and practice.
