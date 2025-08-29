# LifecycleExample - Android Activity Lifecycle Demo

## 🎯 Project Overview

This is a comprehensive Android application that demonstrates Activity lifecycle concepts through interactive examples and real-time monitoring. The app provides visual feedback, logging, and educational features to help understand how Android Activity lifecycle works.

## ✨ Features

### 🔄 Lifecycle Monitoring
- **Real-time lifecycle counting**: Tracks how many times each lifecycle method is called
- **Visual status updates**: Shows current Activity state
- **Toast notifications**: Immediate feedback for each lifecycle event
- **Comprehensive logging**: Detailed logs in Logcat for debugging

### 💾 State Management
- **Counter preservation**: Maintains counter value across lifecycle events
- **Text input persistence**: Saves and restores user input
- **Configuration change handling**: Survives screen rotation
- **Persistent storage**: Uses SharedPreferences for long-term data

### ⏰ Resource Management
- **Timer implementation**: Starts/stops with lifecycle events
- **Memory leak prevention**: Proper resource cleanup
- **Background operation handling**: Demonstrates best practices

### 🎨 User Interface
- **Modern Material Design**: Clean, intuitive interface
- **Color-coded sections**: Easy to understand different features
- **Interactive elements**: Buttons, text input, real-time updates
- **Comprehensive instructions**: Built-in testing guide

## 📱 What You'll See

### App Interface
```
┌─────────────────────────────────────┐
│     Activity Lifecycle Demo         │
│                                     │
│ Lifecycle Counts:                   │
│ onCreate: 1                         │
│ onStart: 1                          │
│ onResume: 1                         │
│ onPause: 0                          │
│ onStop: 0                           │
│ onDestroy: 0                        │
└─────────────────────────────────────┘

Status: Activity is interactive

Counter: 5
Timer: 12 seconds

[Text Input Field]

[Increment Counter] [Reset All]

📱 Testing Instructions:
• Watch Toast messages for lifecycle events
• Observe the counter and timer updates
• Type text and see state preservation

🔄 Lifecycle Testing:
• Press Home button → onPause + onStop
• Return to app → onStart + onResume
• Rotate screen → Full recreation cycle
• Press Back button → onPause + onStop + onDestroy
```

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version)
- Android SDK (API level 24+)
- Kotlin plugin
- Android emulator or physical device

### Setup Instructions

1. **Open the project in Android Studio**
   ```bash
   # Open Android Studio and select "Open an existing project"
   # Navigate to the LifecycleExample folder
   ```

2. **Sync Gradle files**
   - Wait for the Gradle sync to complete
   - Resolve any dependency issues if they arise

3. **Run the application**
   - Click the green "Run" button or press `Shift + F10`
   - Select your target device (emulator or physical device)

4. **Monitor the results**
   - Watch Toast messages appear for each lifecycle event
   - Check Logcat for detailed logs (filter by "MainActivityLifecycle")
   - Observe the UI updates in real-time

## 🧪 Testing Scenarios

### 1. Basic App Launch
**Expected Behavior:**
- Toast messages: `🎬 onCreate called` → `👁️ onStart called` → `▶️ onResume called`
- Counter starts at 0 (or restored value)
- Timer begins counting
- Status shows "Activity is interactive"

### 2. Press Home Button
**Expected Behavior:**
- Toast messages: `⏸️ onPause called` → `⏹️ onStop called`
- Timer stops counting
- Status shows "Activity is completely hidden"
- Counter and text input are preserved

### 3. Return to App
**Expected Behavior:**
- Toast messages: `👁️ onStart called` → `▶️ onResume called`
- Timer resumes counting
- Status shows "Activity is interactive"
- Text input is restored

### 4. Screen Rotation
**Expected Behavior:**
- Full lifecycle sequence: `⏸️ onPause` → `⏹️ onStop` → `💀 onDestroy` → `🎬 onCreate` → `👁️ onStart` → `▶️ onResume`
- Counter value is preserved
- Text input is preserved
- Timer continues from where it left off

### 5. Press Back Button
**Expected Behavior:**
- Toast messages: `⏸️ onPause called` → `⏹️ onStop called` → `💀 onDestroy called`
- App closes completely
- All resources are cleaned up

## 📊 Logcat Monitoring

### Filter Setup
1. Open Logcat in Android Studio
2. Set filter to: `MainActivityLifecycle`
3. Look for logs with emojis for easy identification

### Sample Log Output
```
D/MainActivityLifecycle: 🎬 onCreate called (Count: 1)
D/MainActivityLifecycle: 📦 State restored from savedInstanceState
D/MainActivityLifecycle: 💾 Persistent state loaded
D/MainActivityLifecycle: 👁️ onStart called (Count: 1)
D/MainActivityLifecycle: ▶️ onResume called (Count: 1)
D/MainActivityLifecycle: ⏰ Timer started
D/MainActivityLifecycle: 🔢 Counter incremented to: 1
D/MainActivityLifecycle: ⏸️ onPause called (Count: 1)
D/MainActivityLifecycle: 💾 Text saved: Hello World
D/MainActivityLifecycle: 💾 Persistent state saved
D/MainActivityLifecycle: ⏰ Timer stopped
```

## 🎓 Learning Objectives

After using this app, you should understand:

### Core Concepts
- ✅ When each lifecycle method is called
- ✅ The purpose of each lifecycle method
- ✅ How to save and restore state
- ✅ Proper resource management

### Best Practices
- ✅ Always call super methods
- ✅ Don't rely on onDestroy() for critical operations
- ✅ Keep lifecycle methods fast
- ✅ Use appropriate methods for different tasks

### Real-World Applications
- ✅ Video player pausing/resuming
- ✅ GPS location updates
- ✅ Game state preservation
- ✅ Form data saving

## 🔧 Code Highlights

### Lifecycle Method Implementation
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    onCreateCount++
    Log.d(TAG, "🎬 onCreate called (Count: $onCreateCount)")
    showToast("🎬 onCreate called")
    
    // Initialize components and restore state
}

override fun onPause() {
    super.onPause()
    onPauseCount++
    
    // Save current state
    savedText = editText.text.toString()
    sharedPreferences.edit().putInt("persistent_counter", counter).apply()
    
    // Stop background operations
    stopTimer()
}
```

### State Management
```kotlin
// Save state for configuration changes
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("counter", counter)
    outState.putString("saved_text", editText.text.toString())
    outState.putInt("timer_counter", timerCounter)
}

// Restore state after configuration change
savedInstanceState?.let { bundle ->
    counter = bundle.getInt("counter", 0)
    savedText = bundle.getString("saved_text", "")
    timerCounter = bundle.getInt("timer_counter", 0)
    updateUI()
}
```

### Resource Management
```kotlin
private fun startTimer() {
    timer = Timer()
    timer?.scheduleAtFixedRate(object : TimerTask() {
        override fun run() {
            runOnUiThread {
                timerCounter++
                updateUI()
            }
        }
    }, 0, 1000)
}

private fun stopTimer() {
    timer?.cancel()
    timer = null
}
```

## 🐛 Troubleshooting

### Common Issues

1. **App crashes on launch**
   - Check that all dependencies are synced
   - Verify Android SDK version compatibility
   - Check Logcat for specific error messages

2. **Toast messages not appearing**
   - Ensure notifications are enabled
   - Check if device is in Do Not Disturb mode
   - Verify the showToast() method is being called

3. **State not being preserved**
   - Check that onSaveInstanceState() is implemented
   - Verify SharedPreferences is properly initialized
   - Ensure state restoration logic is correct

4. **Timer not working**
   - Check that Timer is properly started in onResume()
   - Verify Timer is stopped in onPause()
   - Look for memory leak warnings in Logcat

### Debug Tips
- Use Logcat with filter "MainActivityLifecycle" for detailed logs
- Check the lifecycle counts in the UI for unexpected behavior
- Monitor the status text for current Activity state
- Use the reset button to clear all state and start fresh

## 📚 Further Learning

After mastering this example, explore:

1. **ViewModel and LiveData** - Lifecycle-aware data management
2. **Fragment Lifecycle** - Component lifecycle management
3. **Service Lifecycle** - Background operation lifecycle
4. **Process Lifecycle** - App-level lifecycle management
5. **Lifecycle Components** - Architecture Components library

## 🤝 Contributing

This project is designed for educational purposes. Feel free to:
- Modify the code to experiment with different scenarios
- Add new features to demonstrate additional concepts
- Improve the UI or add more interactive elements
- Share your findings and improvements

## 📄 License

This project is created for educational purposes. Use it to learn and experiment with Android Activity lifecycle concepts.

---

**Happy Learning! 🚀**

Remember: Understanding the Activity lifecycle is fundamental to building robust Android applications. This example provides hands-on experience with all the key concepts you need to know.
