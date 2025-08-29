# Android Activity Lifecycle - Exercises Guide

## 🎯 Exercise Overview

This guide provides hands-on exercises to reinforce your understanding of Android Activity lifecycle. Each exercise builds upon the previous one, gradually increasing in complexity.

---

## 📋 Prerequisites

Before starting these exercises, make sure you have:
- Android Studio installed
- The HelloLifecycle project open
- An Android emulator or physical device ready
- Logcat window open in Android Studio

---

## 🏋️ Exercise 1: Basic Lifecycle Awareness (Easy)

### Objective
Add Toast messages for each lifecycle method to visualize when they are called.

### Steps

1. **Open MainActivity.kt** in your project
2. **Add Toast messages** to each lifecycle method:

```kotlin
// In onCreate()
Toast.makeText(this, "onCreate called", Toast.LENGTH_SHORT).show()

// In onStart()
Toast.makeText(this, "onStart called", Toast.LENGTH_SHORT).show()

// In onResume()
Toast.makeText(this, "onResume called", Toast.LENGTH_SHORT).show()

// In onPause()
Toast.makeText(this, "onPause called", Toast.LENGTH_SHORT).show()

// In onStop()
Toast.makeText(this, "onStop called", Toast.LENGTH_SHORT).show()

// In onDestroy()
Toast.makeText(this, "onDestroy called", Toast.LENGTH_SHORT).show()
```

3. **Run the app** and observe the Toast messages
4. **Test different scenarios**:
   - Launch the app
   - Press the Home button
   - Return to the app
   - Press the Back button
   - Rotate the screen

### Expected Results
- You should see Toast messages appearing for each lifecycle event
- The sequence should match the lifecycle flow: onCreate → onStart → onResume

### Learning Points
- Understanding when each lifecycle method is called
- Visual feedback helps reinforce the lifecycle concept
- Different user actions trigger different lifecycle sequences

---

## 🏋️ Exercise 2: State Preservation (Intermediate)

### Objective
Save and restore user input when the app is paused and resumed.

### Steps

1. **Add a new EditText** to your layout (if not already present):

```xml
<EditText
    android:id="@+id/editText"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Enter some text..."
    android:layout_marginBottom="16dp" />
```

2. **Add state variables** to MainActivity:

```kotlin
private lateinit var editText: EditText
private var savedText = ""
```

3. **Initialize the EditText** in `initializeViews()`:

```kotlin
editText = findViewById(R.id.editText)
```

4. **Save text in onPause()**:

```kotlin
override fun onPause() {
    super.onPause()
    Log.d(TAG, "onPause called")
    Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show()
    
    // Save current text
    savedText = editText.text.toString()
}
```

5. **Restore text in onResume()**:

```kotlin
override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume called")
    Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
    
    // Restore saved text
    if (savedText.isNotEmpty()) {
        editText.setText(savedText)
    }
}
```

6. **Test the functionality**:
   - Type some text in the EditText
   - Press the Home button
   - Return to the app
   - Verify that your text is preserved

### Expected Results
- Text entered in the EditText should persist when you leave and return to the app
- The text should be restored when the app is resumed

### Learning Points
- Understanding when to save user data (onPause)
- Understanding when to restore user data (onResume)
- Real-world application of lifecycle methods

---

## 🏋️ Exercise 3: Configuration Change Handling (Advanced)

### Objective
Handle screen rotation and other configuration changes properly.

### Steps

1. **Add a counter variable** to MainActivity:

```kotlin
private var counter = 0
```

2. **Add a TextView** to display the counter (if not already present):

```xml
<TextView
    android:id="@+id/textView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Counter: 0"
    android:textSize="24sp"
    android:layout_marginBottom="16dp" />
```

3. **Add a Button** to increment the counter:

```xml
<Button
    android:id="@+id/button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Increment Counter" />
```

4. **Initialize the TextView and Button** in `initializeViews()`:

```kotlin
textView = findViewById(R.id.textView)
button = findViewById(R.id.button)

button.setOnClickListener {
    counter++
    updateUI()
}
```

5. **Add updateUI() method**:

```kotlin
private fun updateUI() {
    textView.text = "Counter: $counter"
}
```

6. **Save state in onSaveInstanceState()**:

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    
    // Save state for configuration changes
    outState.putInt("counter", counter)
    outState.putString("saved_text", editText.text.toString())
    
    Log.d(TAG, "onSaveInstanceState called")
}
```

7. **Restore state in onCreate()**:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    
    Log.d(TAG, "onCreate called")
    Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show()
    
    // Initialize UI components
    initializeViews()
    
    // Restore state if available
    savedInstanceState?.let { bundle ->
        counter = bundle.getInt("counter", 0)
        savedText = bundle.getString("saved_text", "")
        updateUI()
    }
}
```

8. **Test the functionality**:
   - Increment the counter a few times
   - Rotate the screen
   - Verify that the counter value is preserved
   - Check that the text in EditText is also preserved

### Expected Results
- Counter value should persist after screen rotation
- EditText content should also be preserved
- The app should handle configuration changes gracefully

### Learning Points
- Understanding configuration changes
- Using `onSaveInstanceState()` and `onCreate()` for state preservation
- The difference between temporary and permanent state loss

---

## 🏋️ Exercise 4: Resource Management (Advanced)

### Objective
Properly manage resources like timers to prevent memory leaks.

### Steps

1. **Add a Timer variable** to MainActivity:

```kotlin
private var timer: Timer? = null
private var timerCounter = 0
```

2. **Add a TextView** to display the timer (if not already present):

```xml
<TextView
    android:id="@+id/timerTextView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Timer: 0 seconds"
    android:textSize="20sp"
    android:layout_marginBottom="24dp" />
```

3. **Initialize the timer TextView** in `initializeViews()`:

```kotlin
timerTextView = findViewById(R.id.timerTextView)
```

4. **Add startTimer() method**:

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
    }, 0, 1000) // Update every 1 second
}
```

5. **Add stopTimer() method**:

```kotlin
private fun stopTimer() {
    timer?.cancel()
    timer = null
}
```

6. **Update updateUI() method**:

```kotlin
private fun updateUI() {
    textView.text = "Counter: $counter"
    timerTextView.text = "Timer: $timerCounter seconds"
}
```

7. **Start timer in onResume()**:

```kotlin
override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume called")
    Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show()
    
    // Restore saved text
    if (savedText.isNotEmpty()) {
        editText.setText(savedText)
    }
    
    // Start timer
    startTimer()
}
```

8. **Stop timer in onPause()**:

```kotlin
override fun onPause() {
    super.onPause()
    Log.d(TAG, "onPause called")
    Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show()
    
    // Save current text
    savedText = editText.text.toString()
    
    // Stop timer
    stopTimer()
}
```

9. **Ensure timer is stopped in onDestroy()**:

```kotlin
override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "onDestroy called")
    Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show()
    
    // Ensure timer is stopped
    stopTimer()
}
```

10. **Save timer state in onSaveInstanceState()**:

```kotlin
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    
    // Save state for configuration changes
    outState.putInt("counter", counter)
    outState.putString("saved_text", editText.text.toString())
    outState.putInt("timer_counter", timerCounter)
    
    Log.d(TAG, "onSaveInstanceState called")
}
```

11. **Restore timer state in onCreate()**:

```kotlin
// In onCreate(), after restoring other state:
savedInstanceState?.let { bundle ->
    counter = bundle.getInt("counter", 0)
    savedText = bundle.getString("saved_text", "")
    timerCounter = bundle.getInt("timer_counter", 0)
    updateUI()
}
```

12. **Test the functionality**:
    - Run the app and watch the timer count up
    - Press Home and return - timer should pause and resume
    - Rotate the screen - timer should continue from where it left off
    - Check Logcat for any memory leak warnings

### Expected Results
- Timer should start when the app is resumed
- Timer should stop when the app is paused
- Timer should continue from the correct value after configuration changes
- No memory leaks should occur

### Learning Points
- Proper resource management in lifecycle methods
- Preventing memory leaks
- Understanding when to start and stop background operations

---

## 🏋️ Exercise 5: Advanced State Management (Expert)

### Objective
Implement a more complex state management system using SharedPreferences.

### Steps

1. **Add SharedPreferences** for persistent storage:

```kotlin
private lateinit var sharedPreferences: SharedPreferences
```

2. **Initialize SharedPreferences** in `onCreate()`:

```kotlin
sharedPreferences = getSharedPreferences("LifecycleDemo", Context.MODE_PRIVATE)
```

3. **Load persistent state** in `onCreate()`:

```kotlin
// Load persistent state
counter = sharedPreferences.getInt("persistent_counter", 0)
updateUI()
```

4. **Save persistent state** in `onPause()`:

```kotlin
// Save persistent state
sharedPreferences.edit().putInt("persistent_counter", counter).apply()
```

5. **Add a reset button** to clear persistent state:

```xml
<Button
    android:id="@+id/resetButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Reset Counter"
    android:layout_marginTop="8dp" />
```

6. **Handle reset button click**:

```kotlin
resetButton = findViewById(R.id.resetButton)
resetButton.setOnClickListener {
    counter = 0
    sharedPreferences.edit().clear().apply()
    updateUI()
    Toast.makeText(this, "Counter reset!", Toast.LENGTH_SHORT).show()
}
```

7. **Test persistent storage**:
    - Increment the counter
    - Close the app completely
    - Reopen the app
    - Verify the counter value is preserved

### Expected Results
- Counter value should persist even after the app is completely closed
- Reset button should clear both the counter and persistent storage
- State should be preserved across app restarts

### Learning Points
- Understanding persistent vs. temporary state
- Using SharedPreferences for data persistence
- Different storage options in Android

---

## 🎯 Testing Scenarios

### Scenario 1: Normal App Usage
1. Launch the app
2. Interact with UI elements
3. Press Home button
4. Return to app
5. **Expected**: All state should be preserved

### Scenario 2: Configuration Changes
1. Launch the app
2. Interact with UI elements
3. Rotate the screen
4. **Expected**: All state should be preserved

### Scenario 3: System Memory Pressure
1. Launch the app
2. Interact with UI elements
3. Open multiple other apps
4. Return to your app
5. **Expected**: App should handle recreation gracefully

### Scenario 4: App Termination
1. Launch the app
2. Interact with UI elements
3. Close the app completely
4. Reopen the app
5. **Expected**: Persistent state should be restored

---

## 🔍 Debugging Tips

### Using Logcat
1. **Filter logs** by your TAG: `MainActivityLifecycle`
2. **Look for lifecycle sequence**:
   ```
   onCreate → onStart → onResume
   onPause → onStop (when leaving app)
   onStart → onResume (when returning)
   ```

### Common Issues
1. **State not preserved**: Check if you're saving in the right lifecycle method
2. **Memory leaks**: Ensure resources are properly released
3. **UI not updating**: Make sure you're calling `updateUI()` after state changes

### Testing Checklist
- [ ] App launches correctly
- [ ] Toast messages appear for each lifecycle event
- [ ] State is preserved when leaving/returning to app
- [ ] State is preserved during screen rotation
- [ ] No memory leaks in Logcat
- [ ] Timer starts/stops correctly
- [ ] Persistent state survives app restart

---

## 🎓 Exercise Completion

After completing all exercises, you should have:
- ✅ A working understanding of Activity lifecycle
- ✅ Practical experience with state management
- ✅ Knowledge of resource management best practices
- ✅ Ability to handle configuration changes
- ✅ Understanding of persistent vs. temporary state

**Congratulations!** You've successfully implemented a comprehensive Activity lifecycle demo that covers all the important concepts. Use this knowledge to build robust Android apps that handle lifecycle events properly.

