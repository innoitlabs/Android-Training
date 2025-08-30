# SharedPreferences Troubleshooting Guide

## Common Issues and Solutions

This guide helps you resolve common problems when working with SharedPreferences in Android.

## Data Not Persisting

### Problem: Data disappears after app restart
**Symptoms:**
- Saved data is lost when the app is closed and reopened
- Preferences don't persist across app launches

**Possible Causes:**
1. Not calling `apply()` or `commit()`
2. Using different preference file names
3. App data being cleared by the system
4. Incorrect context usage

**Solutions:**

#### 1. Ensure you're calling apply() or commit()
```kotlin
// ❌ Wrong - data won't be saved
editor.putString("key", "value")

// ✅ Correct - data will be saved
editor.putString("key", "value")
editor.apply()
```

#### 2. Use consistent preference file names
```kotlin
// ❌ Wrong - different file names
val prefs1 = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
val prefs2 = getSharedPreferences("MyPrefs2", Context.MODE_PRIVATE)

// ✅ Correct - same file name
val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
```

#### 3. Check if app data is being cleared
- Go to Settings > Apps > Your App > Storage
- Check if "Clear Data" was accidentally pressed
- Verify the app isn't being cleared by battery optimization

#### 4. Use the correct context
```kotlin
// ❌ Wrong - using application context in some cases
val prefs = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

// ✅ Correct - use activity context
val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
```

## Wrong Data Types

### Problem: Getting wrong data type or null values
**Symptoms:**
- `getString()` returns null when expecting a string
- `getInt()` returns 0 when expecting a number
- Type casting errors

**Solutions:**

#### 1. Always provide default values
```kotlin
// ❌ Wrong - can return null
val name = prefs.getString("name", null)

// ✅ Correct - provide meaningful default
val name = prefs.getString("name", "Unknown")
```

#### 2. Handle null values properly
```kotlin
val name = prefs.getString("name", null)
if (name != null) {
    // Use the name
} else {
    // Handle null case
}
```

#### 3. Use correct getter methods
```kotlin
// ❌ Wrong - using wrong getter
val age = prefs.getString("age", "0").toInt()

// ✅ Correct - use appropriate getter
val age = prefs.getInt("age", 0)
```

## Performance Issues

### Problem: App becomes slow when using SharedPreferences
**Symptoms:**
- UI freezes when saving data
- App becomes unresponsive
- Slow startup times

**Solutions:**

#### 1. Use apply() instead of commit()
```kotlin
// ❌ Wrong - blocks the calling thread
val success = editor.commit()

// ✅ Correct - asynchronous, better performance
editor.apply()
```

#### 2. Batch multiple operations
```kotlin
// ❌ Wrong - multiple apply() calls
editor.putString("name", "John").apply()
editor.putInt("age", 25).apply()
editor.putBoolean("active", true).apply()

// ✅ Correct - single apply() call
editor.putString("name", "John")
    .putInt("age", 25)
    .putBoolean("active", true)
    .apply()
```

#### 3. Avoid storing large data
```kotlin
// ❌ Wrong - storing large data
editor.putString("large_json", veryLargeJsonString)

// ✅ Correct - store only necessary data
editor.putString("user_id", "12345")
editor.putString("user_name", "John")
```

## Security Issues

### Problem: Sensitive data stored in plain text
**Symptoms:**
- Passwords visible in preference files
- Tokens stored without encryption
- Security vulnerabilities

**Solutions:**

#### 1. Use EncryptedSharedPreferences for sensitive data
```kotlin
// Add dependency in build.gradle
// implementation "androidx.security:security-crypto:1.1.0-alpha06"

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

#### 2. Don't store sensitive data in regular SharedPreferences
```kotlin
// ❌ Wrong - storing password in plain text
editor.putString("password", "mypassword123")

// ✅ Correct - use encrypted storage or don't store at all
// Store only non-sensitive data
editor.putString("user_id", "12345")
editor.putBoolean("is_logged_in", true)
```

## Debugging Techniques

### 1. Check preference file contents
```kotlin
// Add this method to your activity for debugging
private fun debugPreferences() {
    val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val allPrefs = prefs.all
    
    Log.d("SharedPrefs", "All preferences: $allPrefs")
    
    for ((key, value) in allPrefs) {
        Log.d("SharedPrefs", "$key = $value (${value?.javaClass?.simpleName})")
    }
}
```

### 2. Verify preference file location
```kotlin
// Check where preference files are stored
private fun getPreferenceFileLocation() {
    val prefsDir = File(filesDir.parent, "shared_prefs")
    Log.d("SharedPrefs", "Preferences directory: ${prefsDir.absolutePath}")
    
    if (prefsDir.exists()) {
        prefsDir.listFiles()?.forEach { file ->
            Log.d("SharedPrefs", "Preference file: ${file.name}")
        }
    }
}
```

### 3. Test with simple values first
```kotlin
// Start with simple test to verify basic functionality
private fun testBasicFunctionality() {
    val prefs = getSharedPreferences("TestPrefs", Context.MODE_PRIVATE)
    
    // Save a simple value
    prefs.edit().putString("test_key", "test_value").apply()
    
    // Read it back
    val value = prefs.getString("test_key", "not_found")
    Log.d("SharedPrefs", "Test value: $value")
    
    // Clean up
    prefs.edit().remove("test_key").apply()
}
```

## Common Error Messages

### "SharedPreferences.Editor is not an Editor"
**Cause:** Trying to use an editor that has already been committed or applied.

**Solution:**
```kotlin
// ❌ Wrong - reusing editor
val editor = prefs.edit()
editor.putString("key1", "value1").apply()
editor.putString("key2", "value2").apply() // Error!

// ✅ Correct - create new editor for each operation
prefs.edit().putString("key1", "value1").apply()
prefs.edit().putString("key2", "value2").apply()
```

### "Context is null"
**Cause:** Using SharedPreferences before context is initialized.

**Solution:**
```kotlin
// ❌ Wrong - using context too early
class MyClass {
    private val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE) // Error!
}

// ✅ Correct - initialize in onCreate or similar
class MainActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }
}
```

## Best Practices Checklist

- [ ] Always call `apply()` or `commit()` after editing
- [ ] Provide meaningful default values
- [ ] Use consistent preference file names
- [ ] Group related preferences together
- [ ] Handle null values appropriately
- [ ] Use `apply()` for better performance
- [ ] Don't store sensitive data in plain SharedPreferences
- [ ] Test persistence across app restarts
- [ ] Validate data before saving
- [ ] Use appropriate data types

## Testing Your Fixes

1. **Test persistence:** Save data, close app, reopen, verify data is still there
2. **Test data types:** Save different types, read them back, verify correct types
3. **Test performance:** Save large amounts of data, check for UI freezing
4. **Test edge cases:** Empty strings, null values, very large numbers
5. **Test on different devices:** Different Android versions, screen sizes

## Getting Help

If you're still having issues:

1. Check the Android documentation
2. Search Stack Overflow for similar problems
3. Use Android Studio's debugger to inspect preference values
4. Add logging to track when data is saved/loaded
5. Test with a minimal example to isolate the problem

Remember: SharedPreferences is designed for simple, lightweight data storage. If you're having persistent issues, consider whether your use case might be better served by Room Database or DataStore.
