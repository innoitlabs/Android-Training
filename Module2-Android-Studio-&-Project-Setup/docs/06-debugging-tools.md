# 6. Debugging Tools and Logcat

## 🐛 What is Debugging?

Debugging is the process of finding and fixing errors (bugs) in your code. Android Studio provides powerful debugging tools to help you identify and resolve issues in your applications.

### Why Debugging is Important
- **Identify Issues**: Find problems before users do
- **Improve Performance**: Optimize app performance
- **Ensure Quality**: Deliver bug-free applications
- **Save Time**: Faster development cycles
- **User Experience**: Better app stability

## 🔧 Debug Mode vs Release Mode

### Debug Mode
```kotlin
// Debug configuration
android {
    buildTypes {
        debug {
            debuggable true
            minifyEnabled false
            buildConfigField "boolean", "DEBUG_MODE", "true"
        }
    }
}
```

**Features:**
- Debugging enabled
- Logging available
- Hot reload support
- Performance monitoring
- Crash reporting

### Release Mode
```kotlin
// Release configuration
android {
    buildTypes {
        release {
            debuggable false
            minifyEnabled true
            buildConfigField "boolean", "DEBUG_MODE", "false"
        }
    }
}
```

**Features:**
- Optimized performance
- Code obfuscation
- No debugging information
- Production-ready

## 🚀 Running in Debug Mode

### Method 1: Debug Button
1. **Click the Debug button** (🐛) in the toolbar
2. **Select target device** (AVD or physical device)
3. **App launches in debug mode**

### Method 2: Run Menu
1. **Run → Debug 'app'**
2. **Choose device**
3. **Debug session starts**

### Method 3: Keyboard Shortcut
- **Windows/Linux**: `Shift + F9`
- **macOS**: `Control + D`

## 📊 Logcat Overview

### What is Logcat?
Logcat is Android's logging system that displays system and application logs in real-time. It's essential for debugging and monitoring app behavior.

### Accessing Logcat
1. **View → Tool Windows → Logcat**
2. **Bottom panel** of Android Studio
3. **Keyboard shortcut**: `Alt + 6` (Windows/Linux) or `Cmd + 6` (macOS)

### Logcat Interface
```
Logcat Window
├── Device Selector      # Choose device/emulator
├── Process Selector     # Choose app process
├── Log Level Filter     # Verbose, Debug, Info, Warn, Error
├── Search Box          # Filter logs by text
├── Log Messages        # Actual log output
└── Actions
    ├── Clear Logcat
    ├── Export Logs
    └── Configure Logcat
```

## 📝 Log Levels

### Verbose (V)
```kotlin
Log.v(TAG, "Detailed debugging information")
```
- **Purpose**: Most detailed logging
- **Use case**: Step-by-step execution tracking
- **Example**: Method entry/exit, variable values

### Debug (D)
```kotlin
Log.d(TAG, "Debug information for development")
```
- **Purpose**: Development debugging
- **Use case**: Understanding app flow
- **Example**: Button clicks, data processing

### Info (I)
```kotlin
Log.i(TAG, "General information about app state")
```
- **Purpose**: General information
- **Use case**: App state changes
- **Example**: User actions, configuration changes

### Warning (W)
```kotlin
Log.w(TAG, "Warning about potential issues")
```
- **Purpose**: Potential problems
- **Use case**: Deprecated methods, unexpected states
- **Example**: Network timeouts, missing data

### Error (E)
```kotlin
Log.e(TAG, "Error that needs attention", exception)
```
- **Purpose**: Actual errors
- **Use case**: Crashes, failures
- **Example**: Network errors, null pointer exceptions

## 🔍 Using Logcat Effectively

### Basic Logging in Kotlin
```kotlin
class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        Log.d(TAG, "onCreate: Activity created")
        setupUI()
    }
    
    private fun setupUI() {
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            Log.d(TAG, "Button clicked")
            performAction()
        }
    }
    
    private fun performAction() {
        try {
            Log.i(TAG, "Performing action")
            // Some operation
            Log.d(TAG, "Action completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error performing action", e)
        }
    }
}
```

### Advanced Logging
```kotlin
class UserRepository {
    
    companion object {
        private const val TAG = "UserRepository"
    }
    
    fun getUser(userId: String): User? {
        Log.d(TAG, "getUser: userId=$userId")
        
        return try {
            val user = apiService.getUser(userId)
            Log.i(TAG, "getUser: Retrieved user ${user.name}")
            user
        } catch (e: Exception) {
            Log.e(TAG, "getUser: Failed to retrieve user $userId", e)
            null
        }
    }
}
```

### Conditional Logging
```kotlin
class DebugUtils {
    
    companion object {
        private const val TAG = "DebugUtils"
        private const val DEBUG = BuildConfig.DEBUG
    }
    
    fun logDebug(message: String) {
        if (DEBUG) {
            Log.d(TAG, message)
        }
    }
    
    fun logError(message: String, throwable: Throwable? = null) {
        Log.e(TAG, message, throwable)
    }
}
```

## 🎯 Logcat Filtering

### By Log Level
```yaml
# Show only errors and warnings
Log Level: Error + Warning

# Show debug and above
Log Level: Debug + Info + Warning + Error

# Show all logs
Log Level: Verbose
```

### By Package Name
```yaml
# Filter by your app package
Package Name: com.example.myapp

# Filter by system packages
Package Name: android
```

### By Tag
```yaml
# Filter by specific tag
Tag: MainActivity

# Filter by multiple tags
Tag: MainActivity|UserRepository|NetworkManager
```

### By Text Search
```yaml
# Search for specific text
Search: "Button clicked"

# Search with regex
Search: "User.*created"

# Exclude certain text
Search: -"DEBUG" "ERROR"
```

## 🔧 Breakpoints and Debugging

### Setting Breakpoints
1. **Click in the gutter** next to line numbers
2. **Red dot appears** indicating breakpoint
3. **Run in debug mode** to hit breakpoint

### Types of Breakpoints
```yaml
# Line Breakpoint
- Stops at specific line
- Most common type

# Conditional Breakpoint
- Stops only when condition is true
- Right-click breakpoint → More → Condition

# Exception Breakpoint
- Stops when exception is thrown
- Run → View Breakpoints → Exception Breakpoints

# Method Breakpoint
- Stops when method is called
- Right-click method → Toggle Method Breakpoint
```

### Debug Session
```yaml
# When breakpoint is hit:
- Code execution pauses
- Variables panel shows current values
- Call stack shows execution path
- Step controls become available
```

### Step Controls
```yaml
# Step Over (F8)
- Execute current line and move to next
- Don't enter method calls

# Step Into (F7)
- Enter method calls
- Go deeper into code execution

# Step Out (Shift + F8)
- Exit current method
- Return to calling method

# Resume Program (F9)
- Continue execution until next breakpoint
```

## 📊 Variable Inspection

### Variables Panel
```yaml
# Local Variables
- Variables in current scope
- Current values displayed

# Watches
- Custom expressions to monitor
- Add variables or expressions

# Evaluate Expression
- Execute code in debug context
- Test expressions on the fly
```

### Example Debug Session
```kotlin
fun calculateTotal(items: List<Item>): Double {
    var total = 0.0  // Breakpoint here
    
    for (item in items) {
        total += item.price  // Watch total variable
        Log.d(TAG, "Item: ${item.name}, Price: ${item.price}")
    }
    
    return total
}
```

**Debug Output:**
```
Variables Panel:
- items: List<Item> (size: 3)
- total: 15.99
- item: Item(name="Coffee", price=4.99)

Watches:
- items.size: 3
- total: 15.99
```

## 🚨 Common Debugging Scenarios

### Null Pointer Exception
```kotlin
// Problematic code
val textView = findViewById<TextView>(R.id.textView)
textView.text = "Hello"  // textView might be null

// Debugging approach
val textView = findViewById<TextView>(R.id.textView)
Log.d(TAG, "textView: $textView")
if (textView != null) {
    textView.text = "Hello"
} else {
    Log.e(TAG, "textView is null - check layout file")
}
```

### Network Issues
```kotlin
fun fetchData() {
    Log.d(TAG, "Starting network request")
    
    try {
        val response = apiService.getData()
        Log.i(TAG, "Network request successful: ${response.size} items")
        processData(response)
    } catch (e: IOException) {
        Log.e(TAG, "Network error: ${e.message}", e)
        showError("Network connection failed")
    } catch (e: Exception) {
        Log.e(TAG, "Unexpected error", e)
        showError("Something went wrong")
    }
}
```

### Performance Issues
```kotlin
fun performHeavyOperation() {
    val startTime = System.currentTimeMillis()
    Log.d(TAG, "Starting heavy operation")
    
    // Heavy operation here
    val result = heavyComputation()
    
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    Log.i(TAG, "Heavy operation completed in ${duration}ms")
    
    if (duration > 1000) {
        Log.w(TAG, "Heavy operation took longer than expected")
    }
}
```

## 🔍 Advanced Debugging Techniques

### Custom Logging
```kotlin
object Logger {
    private const val TAG = "MyApp"
    
    fun debug(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
    
    fun error(message: String, throwable: Throwable? = null, tag: String = TAG) {
        Log.e(tag, message, throwable)
    }
    
    fun performance(operation: String, block: () -> Unit) {
        val startTime = System.currentTimeMillis()
        block()
        val duration = System.currentTimeMillis() - startTime
        Log.d(tag, "$operation took ${duration}ms")
    }
}
```

### Debug Build Configuration
```kotlin
android {
    buildTypes {
        debug {
            buildConfigField "String", "LOG_TAG", "\"MyApp_Debug\""
            buildConfigField "boolean", "ENABLE_LOGGING", "true"
            buildConfigField "boolean", "ENABLE_CRASH_REPORTING", "false"
        }
        release {
            buildConfigField "String", "LOG_TAG", "\"MyApp\""
            buildConfigField "boolean", "ENABLE_LOGGING", "false"
            buildConfigField "boolean", "ENABLE_CRASH_REPORTING", "true"
        }
    }
}
```

### Logcat Configuration
```yaml
# Custom Logcat Configuration
- Buffer Size: 10000 lines
- Font Size: 12pt
- Color Scheme: Dark theme
- Auto-scroll: Enabled
- Show Timestamp: Enabled
- Show Process ID: Disabled
```

## 🚨 Troubleshooting Debugging Issues

### Common Problems

#### App Not Starting in Debug Mode
```bash
# Solution 1: Clean and rebuild
Build → Clean Project
Build → Rebuild Project

# Solution 2: Invalidate caches
File → Invalidate Caches and Restart

# Solution 3: Check device
# Ensure device is connected and USB debugging is enabled
```

#### Breakpoints Not Working
```bash
# Solution 1: Check debug configuration
Run → Edit Configurations → Debug

# Solution 2: Verify breakpoint is set
# Red dot should be solid, not hollow

# Solution 3: Check source code
# Ensure you're debugging the correct version
```

#### Logcat Not Showing Logs
```bash
# Solution 1: Check device selection
# Select correct device in Logcat

# Solution 2: Check log level
# Ensure appropriate log level is selected

# Solution 3: Clear and restart
# Clear Logcat and restart app
```

## 📚 Additional Resources

### Official Documentation
- [Android Debugging](https://developer.android.com/studio/debug)
- [Logcat](https://developer.android.com/studio/debug/am-logcat)
- [Debugging with Android Studio](https://developer.android.com/studio/debug/)

### Best Practices
- [Android Logging Best Practices](https://developer.android.com/topic/performance/logging)
- [Debugging Performance](https://developer.android.com/topic/performance/debugging)

### Tools
- [Android Profiler](https://developer.android.com/studio/profile)
- [Layout Inspector](https://developer.android.com/studio/debug/layout-inspector)
- [APK Analyzer](https://developer.android.com/studio/build/analyze-apk)

---

**Next**: [Project Templates and Wizards](./07-project-templates.md)
