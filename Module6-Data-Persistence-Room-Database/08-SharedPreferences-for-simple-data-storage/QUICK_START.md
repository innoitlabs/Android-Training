# SharedPreferences Quick Start Guide

## 🚀 Get Started in 5 Minutes

This guide will help you quickly understand and implement SharedPreferences in your Android app.

## Basic Implementation

### 1. Create SharedPreferences Instance

```kotlin
val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
```

### 2. Save Data

```kotlin
val editor = sharedPref.edit()
editor.putString("username", "john_doe")
editor.putInt("score", 100)
editor.putBoolean("darkMode", true)
editor.apply()
```

### 3. Read Data

```kotlin
val username = sharedPref.getString("username", "Guest")
val score = sharedPref.getInt("score", 0)
val darkMode = sharedPref.getBoolean("darkMode", false)
```

## Complete Example

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        
        // Load saved data
        loadSavedData()
        
        // Setup save button
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveUserData()
        }
    }
    
    private fun saveUserData() {
        val name = findViewById<EditText>(R.id.nameInput).text.toString()
        val age = findViewById<EditText>(R.id.ageInput).text.toString().toIntOrNull() ?: 0
        
        sharedPref.edit()
            .putString("user_name", name)
            .putInt("user_age", age)
            .putLong("last_saved", System.currentTimeMillis())
            .apply()
            
        Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show()
    }
    
    private fun loadSavedData() {
        val name = sharedPref.getString("user_name", "")
        val age = sharedPref.getInt("user_age", 0)
        
        findViewById<EditText>(R.id.nameInput).setText(name)
        findViewById<EditText>(R.id.ageInput).setText(age.toString())
    }
}
```

## Common Patterns

### Save User Settings
```kotlin
fun saveUserSettings(darkMode: Boolean, notifications: Boolean) {
    sharedPref.edit()
        .putBoolean("dark_mode", darkMode)
        .putBoolean("notifications", notifications)
        .apply()
}
```

### Check First Time User
```kotlin
fun isFirstTimeUser(): Boolean {
    val isFirstTime = sharedPref.getBoolean("first_time", true)
    if (isFirstTime) {
        sharedPref.edit().putBoolean("first_time", false).apply()
    }
    return isFirstTime
}
```

### Save Login Status
```kotlin
fun saveLoginStatus(isLoggedIn: Boolean, userId: String) {
    sharedPref.edit()
        .putBoolean("is_logged_in", isLoggedIn)
        .putString("user_id", userId)
        .putLong("login_time", System.currentTimeMillis())
        .apply()
}
```

## Quick Tips

- ✅ Use `apply()` for better performance
- ✅ Provide default values when reading data
- ✅ Use meaningful key names
- ✅ Group related preferences together
- ❌ Don't store sensitive data (use EncryptedSharedPreferences)
- ❌ Don't store large amounts of data

## Next Steps

1. Run the complete project in the `SharedPreferences/` folder
2. Read the full `README.md` for detailed explanations
3. Try the exercises in the documentation
4. Experiment with different data types and use cases

Happy coding! 🎉
