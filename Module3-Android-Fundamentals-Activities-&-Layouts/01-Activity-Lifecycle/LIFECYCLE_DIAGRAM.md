# Android Activity Lifecycle - Visual Guide

## 🔄 Activity Lifecycle Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    ACTIVITY LIFECYCLE FLOW                      │
└─────────────────────────────────────────────────────────────────┘

                    User opens app
                           ↓
                    ┌─────────────┐
                    │  onCreate() │ ← Initialize UI, data, listeners
                    └─────────────┘
                           ↓
                    ┌─────────────┐
                    │  onStart()  │ ← Activity becomes visible
                    └─────────────┘
                           ↓
                    ┌─────────────┐
                    │ onResume()  │ ← Activity is interactive
                    └─────────────┘
                           ↓
                    ┌─────────────────┐
                    │ [User interacts │ ← App is running
                    │  with app]      │
                    └─────────────────┘
                           ↓
                    ┌─────────────┐
                    │  onPause()  │ ← Activity partially hidden
                    └─────────────┘
                           ↓
                    ┌─────────────┐
                    │  onStop()   │ ← Activity completely hidden
                    └─────────────┘
                           ↓
                    ┌─────────────┐
                    │ onDestroy() │ ← Activity destroyed
                    └─────────────┘
```

## 📱 Lifecycle States Explained

### 1. **Created State** (`onCreate`)
- **When**: Activity is first created
- **Purpose**: Initialize the Activity
- **Common Tasks**:
  - Set the layout with `setContentView()`
  - Initialize UI components
  - Set up click listeners
  - Initialize data sources
  - Restore saved state

### 2. **Started State** (`onStart`)
- **When**: Activity becomes visible to user
- **Purpose**: Prepare for user interaction
- **Common Tasks**:
  - Register broadcast receivers
  - Start location services
  - Resume animations
  - Update UI with fresh data

### 3. **Resumed State** (`onResume`)
- **When**: Activity is in foreground and interactive
- **Purpose**: Resume user interactions
- **Common Tasks**:
  - Resume camera preview
  - Start animations
  - Acquire wake locks
  - Resume sensor listeners
  - Refresh data

### 4. **Paused State** (`onPause`)
- **When**: Activity is partially hidden
- **Purpose**: Pause ongoing operations
- **Common Tasks**:
  - Pause camera preview
  - Save form data
  - Pause animations
  - Release wake locks
  - Save lightweight state

### 5. **Stopped State** (`onStop`)
- **When**: Activity is completely hidden
- **Purpose**: Release resources
- **Common Tasks**:
  - Stop location updates
  - Unregister broadcast receivers
  - Release heavy resources
  - Stop background services

### 6. **Destroyed State** (`onDestroy`)
- **When**: Activity is being destroyed
- **Purpose**: Final cleanup
- **Common Tasks**:
  - Cancel all pending operations
  - Release all resources
  - Unbind from services
  - Clear references

## 🔄 Common Lifecycle Scenarios

### Scenario 1: Normal App Launch
```
User taps app icon
       ↓
   onCreate() → Initialize everything
       ↓
   onStart() → Activity becomes visible
       ↓
   onResume() → Activity is interactive
       ↓
   [App is running and user can interact]
```

### Scenario 2: User Presses Home Button
```
User presses Home
       ↓
   onPause() → Pause operations, save state
       ↓
   onStop() → Release resources
       ↓
   [App is in background]
```

### Scenario 3: User Returns to App
```
User returns to app
       ↓
   onStart() → Activity becomes visible again
       ↓
   onResume() → Resume operations
       ↓
   [App is running and user can interact]
```

### Scenario 4: Screen Rotation
```
User rotates screen
       ↓
   onPause() → Pause operations
       ↓
   onStop() → Release resources
       ↓
   onDestroy() → Activity destroyed
       ↓
   onCreate() → Recreate Activity
       ↓
   onStart() → Activity becomes visible
       ↓
   onResume() → Resume operations
```

### Scenario 5: User Presses Back Button
```
User presses Back
       ↓
   onPause() → Pause operations
       ↓
   onStop() → Release resources
       ↓
   onDestroy() → Activity destroyed
       ↓
   [App is closed]
```

## 🎯 Lifecycle Method Comparison

| Method | Called When | Purpose | Common Use Cases |
|--------|-------------|---------|------------------|
| `onCreate()` | Activity first created | Initialize Activity | Set layout, bind views, initialize data |
| `onStart()` | Activity becomes visible | Prepare for interaction | Register receivers, start services |
| `onResume()` | Activity is interactive | Resume operations | Start animations, acquire resources |
| `onPause()` | Activity partially hidden | Pause operations | Save state, pause media |
| `onStop()` | Activity completely hidden | Release resources | Stop services, unregister receivers |
| `onDestroy()` | Activity destroyed | Final cleanup | Release all resources |

## 🔧 State Management in Lifecycle

### Temporary State (Configuration Changes)
```kotlin
// Save state for configuration changes
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("counter", counter)
    outState.putString("text", editText.text.toString())
}

// Restore state after configuration change
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // ... initialization code ...
    
    savedInstanceState?.let { bundle ->
        counter = bundle.getInt("counter", 0)
        editText.setText(bundle.getString("text", ""))
    }
}
```

### Persistent State (App Lifecycle)
```kotlin
// Save persistent state
override fun onPause() {
    super.onPause()
    sharedPreferences.edit()
        .putInt("persistent_counter", counter)
        .apply()
}

// Load persistent state
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // ... initialization code ...
    
    counter = sharedPreferences.getInt("persistent_counter", 0)
}
```

## ⚠️ Important Lifecycle Rules

### 1. **Always Call Super Methods**
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState) // ← Always call this first!
    // Your code here
}
```

### 2. **Don't Rely on onDestroy()**
- `onDestroy()` may not be called if the system kills the process
- Use `onPause()` or `onStop()` for critical operations

### 3. **Keep Lifecycle Methods Fast**
- Avoid heavy operations in `onCreate()`, `onResume()`, `onPause()`
- Use background threads for heavy work

### 4. **Handle Configuration Changes**
- Screen rotation triggers Activity recreation
- Always save and restore state properly

## 🎮 Interactive Lifecycle Demo

The HelloLifecycle app demonstrates all these concepts:

1. **Toast Messages**: Visual feedback for each lifecycle event
2. **State Preservation**: Text input is saved and restored
3. **Configuration Changes**: Counter value persists after rotation
4. **Resource Management**: Timer starts/stops with lifecycle
5. **Logging**: Detailed logs in Logcat for debugging

## 📊 Lifecycle Visualization in Code

```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "🎬 onCreate: Activity is being created")
        // Initialize everything here
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "👁️ onStart: Activity is becoming visible")
        // Register receivers, start services
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "▶️ onResume: Activity is interactive")
        // Start animations, acquire resources
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "⏸️ onPause: Activity is partially hidden")
        // Save state, pause operations
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "⏹️ onStop: Activity is completely hidden")
        // Release resources, stop services
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "💀 onDestroy: Activity is being destroyed")
        // Final cleanup
    }
}
```

## 🎓 Key Takeaways

1. **Lifecycle is Predictable**: Each method has a specific purpose and timing
2. **State Management is Critical**: Always save and restore state appropriately
3. **Resource Management Matters**: Release resources to prevent memory leaks
4. **Configuration Changes Happen**: Handle screen rotation and other changes
5. **Performance is Important**: Keep lifecycle methods fast and efficient

Understanding the Activity lifecycle is fundamental to building robust Android apps. Use this knowledge to create apps that handle all scenarios gracefully!

