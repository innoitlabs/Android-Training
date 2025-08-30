# Android SharedPreferences Tutorial

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand the role of SharedPreferences for storing simple data
- Save and retrieve key-value pairs such as strings, integers, and booleans
- Differentiate between `apply()` and `commit()` methods
- Implement persistent user preferences (e.g., dark mode toggle, login status)
- Apply best practices for managing SharedPreferences in Android apps

## Table of Contents

1. [Introduction](#introduction)
2. [Creating SharedPreferences](#creating-sharedpreferences)
3. [Writing Data](#writing-data)
4. [Reading Data](#reading-data)
5. [Clearing Data](#clearing-data)
6. [Project Example](#project-example)
7. [Best Practices](#best-practices)
8. [Hands-on Lab](#hands-on-lab)
9. [Exercises](#exercises)
10. [Summary](#summary)

## Introduction

**SharedPreferences** is a lightweight storage mechanism for small amounts of primitive data in Android applications. It stores data as key-value pairs in an XML file inside the app's private storage.

### What SharedPreferences is Best For:
- User settings (e.g., dark mode preference)
- Simple flags (e.g., first-time login)
- Session tokens (non-sensitive)
- App configuration data

⚠️ **Not suitable for large/complex data** → Use Room Database or DataStore instead.

### How it Works:
- Data is stored in XML format in the app's private directory
- Accessible only by your application
- Persists across app restarts
- Automatically handles data serialization

## Creating SharedPreferences

### Basic Access Pattern

```kotlin
val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
```

**Parameters:**
- `"MyPrefs"` → preference file name (creates `MyPrefs.xml`)
- `Context.MODE_PRIVATE` → only your app can access it

### Alternative Access Methods

```kotlin
// Using default preferences
val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this)

// Using activity-specific preferences
val activityPrefs = getPreferences(Context.MODE_PRIVATE)
```

## Writing Data

### Basic Writing Pattern

```kotlin
val editor = sharedPref.edit()
editor.putString("username", "Alice")
editor.putInt("age", 25)
editor.putBoolean("isLoggedIn", true)
editor.apply() // saves asynchronously
```

### Available Data Types

```kotlin
editor.putString("name", "John")
editor.putInt("score", 100)
editor.putLong("timestamp", System.currentTimeMillis())
editor.putFloat("rating", 4.5f)
editor.putBoolean("darkMode", true)
editor.putStringSet("tags", setOf("android", "kotlin"))
```

### Saving Methods

**`apply()`** (Recommended):
- Saves asynchronously
- No return value
- Better performance
- Use for most cases

**`commit()`**:
- Saves synchronously
- Returns boolean indicating success
- Blocks the calling thread
- Use when you need immediate confirmation

```kotlin
// Using apply() (recommended)
editor.apply()

// Using commit() (when you need confirmation)
val success = editor.commit()
if (success) {
    // Data saved successfully
}
```

## Reading Data

### Basic Reading Pattern

```kotlin
val username = sharedPref.getString("username", "DefaultName")
val age = sharedPref.getInt("age", 0)
val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
```

### Available Getter Methods

```kotlin
val name = sharedPref.getString("name", "Unknown")
val score = sharedPref.getInt("score", 0)
val timestamp = sharedPref.getLong("timestamp", 0L)
val rating = sharedPref.getFloat("rating", 0.0f)
val darkMode = sharedPref.getBoolean("darkMode", false)
val tags = sharedPref.getStringSet("tags", setOf())
```

**Note:** The second parameter is the default value returned if the key doesn't exist.

## Clearing Data

### Clear All Data

```kotlin
sharedPref.edit().clear().apply()
```

### Remove Specific Keys

```kotlin
sharedPref.edit().remove("username").apply()
```

### Remove Multiple Keys

```kotlin
val editor = sharedPref.edit()
editor.remove("username")
editor.remove("age")
editor.apply()
```

## Project Example

The project in the `SharedPreferences/` folder demonstrates a complete SharedPreferences implementation with the following features:

- Save user information (name, age, email)
- Load and display saved information
- Clear all saved data
- Toggle dark mode preference
- Show login status

### Key Features Demonstrated:

1. **User Input Management**: Save and retrieve user-entered data
2. **Preference Toggles**: Dark mode toggle with visual feedback
3. **Data Persistence**: Information survives app restarts
4. **Data Validation**: Check for empty inputs
5. **UI Updates**: Real-time display of saved preferences

## Best Practices

### 1. Use `apply()` Instead of `commit()`
```kotlin
// Good
editor.apply()

// Avoid unless you need immediate confirmation
editor.commit()
```

### 2. Store Only Lightweight Data
```kotlin
// Good - lightweight data
editor.putString("username", "john_doe")
editor.putBoolean("darkMode", true)

// Avoid - large data
editor.putString("largeJsonData", veryLargeJsonString)
```

### 3. Use Meaningful Key Names
```kotlin
// Good - descriptive keys
editor.putString("user_preference_username", username)
editor.putBoolean("app_setting_dark_mode", isDarkMode)

// Avoid - unclear keys
editor.putString("key1", username)
editor.putBoolean("flag", isDarkMode)
```

### 4. Handle Sensitive Data Securely
```kotlin
// For sensitive data, use EncryptedSharedPreferences
val masterKey = MasterKey.Builder(this)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val encryptedPrefs = EncryptedSharedPreferences.create(
    this,
    "secure_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

### 5. Group Related Preferences
```kotlin
// User preferences
val userPrefs = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

// App settings
val appPrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

// Session data
val sessionPrefs = getSharedPreferences("session_data", Context.MODE_PRIVATE)
```

## Hands-on Lab

### Mini Project: User Preferences App

Build a comprehensive preferences app that demonstrates:

1. **User Profile Management**
   - Save name, age, email
   - Load and display profile information
   - Clear profile data

2. **App Settings**
   - Dark mode toggle
   - Notification preferences
   - Language selection

3. **Session Management**
   - Login status tracking
   - Last login timestamp
   - User session token

### Implementation Steps:

1. Create the UI layout with input fields and buttons
2. Implement SharedPreferences access
3. Add save/load functionality
4. Implement data validation
5. Add clear data functionality
6. Test persistence across app restarts

## Exercises

### Easy Level
1. Save and load a boolean preference (e.g., "first_time_user")
2. Implement a simple counter that persists across app restarts
3. Save user's favorite color preference

### Intermediate Level
1. Add a "Clear Data" button with confirmation dialog
2. Implement multiple preference categories (user, app, session)
3. Add data validation and error handling
4. Create a settings screen with various preference types

### Advanced Level
1. Use `EncryptedSharedPreferences` for sensitive data
2. Implement preference migration from old to new versions
3. Add preference change listeners
4. Create a preference backup/restore system
5. Implement preference synchronization across app components

## Common Use Cases

### 1. User Settings
```kotlin
// Save user preferences
val prefs = getSharedPreferences("user_settings", Context.MODE_PRIVATE)
prefs.edit()
    .putString("display_name", "John Doe")
    .putBoolean("notifications_enabled", true)
    .putString("language", "en")
    .apply()
```

### 2. App Configuration
```kotlin
// Save app configuration
val config = getSharedPreferences("app_config", Context.MODE_PRIVATE)
config.edit()
    .putBoolean("dark_mode", true)
    .putInt("refresh_interval", 30)
    .putString("api_endpoint", "https://api.example.com")
    .apply()
```

### 3. Session Management
```kotlin
// Save session data
val session = getSharedPreferences("session", Context.MODE_PRIVATE)
session.edit()
    .putString("user_token", "abc123")
    .putLong("login_time", System.currentTimeMillis())
    .putBoolean("is_logged_in", true)
    .apply()
```

## Troubleshooting

### Common Issues and Solutions

1. **Data Not Persisting**
   - Ensure you're calling `apply()` or `commit()`
   - Check if you're using the same preference file name
   - Verify the app isn't being cleared from memory

2. **Wrong Data Types**
   - Use the correct getter method for the data type
   - Provide appropriate default values
   - Handle null values for strings

3. **Performance Issues**
   - Use `apply()` instead of `commit()`
   - Batch multiple operations together
   - Avoid storing large amounts of data

## Summary

**Key Takeaways:**

- **SharedPreferences** = key-value storage for simple data
- Use `putX()` to save, `getX()` to retrieve
- Always call `apply()` or `commit()` after editing
- Best for lightweight settings or flags
- Not suitable for large or sensitive data → use Room or DataStore

**When to Use SharedPreferences:**
- ✅ User preferences and settings
- ✅ Simple flags and state
- ✅ Non-sensitive configuration data
- ✅ Small amounts of data

**When NOT to Use SharedPreferences:**
- ❌ Large datasets
- ❌ Sensitive information (passwords, tokens)
- ❌ Complex data structures
- ❌ Data that needs querying or relationships

**Next Steps:**
- Explore Jetpack DataStore for more advanced preference management
- Learn about Room Database for complex data storage
- Study EncryptedSharedPreferences for secure data storage

---

## Running the Project

1. Open the `SharedPreferences/` folder in Android Studio
2. Sync the project with Gradle files
3. Build the project (Build → Make Project)
4. Run on an emulator or device
5. Test the functionality:
   - Enter user information and save
   - Restart the app to verify persistence
   - Test the dark mode toggle
   - Use the clear data functionality

The project demonstrates all the concepts covered in this tutorial and provides a working example for learners to study and modify.
