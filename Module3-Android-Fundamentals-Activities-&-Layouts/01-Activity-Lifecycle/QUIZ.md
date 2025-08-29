# Android Activity Lifecycle - Quiz

## 🎯 Quiz Overview

This quiz tests your understanding of Android Activity lifecycle concepts. Each question is designed to reinforce key learning points and help you identify areas that need more practice.

---

## 📝 Quiz Instructions

- **Total Questions**: 20
- **Time Limit**: 30 minutes
- **Passing Score**: 70% (14 out of 20 correct)
- **Question Types**: Multiple choice, true/false, scenario-based

---

## 🧠 Quiz Questions

### Section 1: Basic Lifecycle Understanding

#### Question 1
**What is the correct order of lifecycle methods when an Activity is first created?**

A) `onCreate()` → `onStart()` → `onResume()`  
B) `onStart()` → `onCreate()` → `onResume()`  
C) `onResume()` → `onStart()` → `onCreate()`  
D) `onCreate()` → `onResume()` → `onStart()`

**Answer**: A) `onCreate()` → `onStart()` → `onResume()`

**Explanation**: When an Activity is first created, Android calls `onCreate()` to initialize it, then `onStart()` when it becomes visible, and finally `onResume()` when it's ready for user interaction.

---

#### Question 2
**Which lifecycle method is called when the user presses the Home button?**

A) `onPause()` and `onStop()`  
B) `onPause()` only  
C) `onStop()` only  
D) `onDestroy()`

**Answer**: A) `onPause()` and `onStop()`

**Explanation**: When the user presses Home, the Activity is partially hidden (`onPause()`) and then completely hidden (`onStop()`), but not destroyed.

---

#### Question 3
**True or False: `onDestroy()` is always called when an Activity is finished.**

A) True  
B) False

**Answer**: B) False

**Explanation**: `onDestroy()` may not be called if the system kills the process to free memory. You should not rely on it for critical operations.

---

#### Question 4
**Which lifecycle method is the best place to save user data that should persist when the app is paused?**

A) `onCreate()`  
B) `onResume()`  
C) `onPause()`  
D) `onDestroy()`

**Answer**: C) `onPause()`

**Explanation**: `onPause()` is called when the Activity is partially hidden and is the best place to save user data quickly before the Activity might be stopped.

---

### Section 2: State Management

#### Question 5
**What method should you override to save state during configuration changes like screen rotation?**

A) `onSaveInstanceState()`  
B) `onRestoreInstanceState()`  
C) `onPause()`  
D) `onStop()`

**Answer**: A) `onSaveInstanceState()`

**Explanation**: `onSaveInstanceState()` is specifically designed to save temporary state that should be restored after configuration changes.

---

#### Question 6
**In which lifecycle method should you restore state from a Bundle?**

A) `onCreate()`  
B) `onStart()`  
C) `onResume()`  
D) `onRestoreInstanceState()`

**Answer**: A) `onCreate()`

**Explanation**: You should check for saved state in `onCreate()` using the `savedInstanceState` parameter and restore it there.

---

#### Question 7
**Which of the following is NOT a good practice for state management?**

A) Save lightweight state in `onPause()`  
B) Save configuration change state in `onSaveInstanceState()`  
C) Save critical data in `onDestroy()`  
D) Restore state in `onCreate()`

**Answer**: C) Save critical data in `onDestroy()`

**Explanation**: `onDestroy()` may not be called, so saving critical data there is unreliable. Use `onPause()` for critical data.

---

### Section 3: Resource Management

#### Question 8
**Where should you start a Timer that updates the UI every second?**

A) `onCreate()`  
B) `onStart()`  
C) `onResume()`  
D) `onPause()`

**Answer**: C) `onResume()`

**Explanation**: `onResume()` is called when the Activity is interactive, making it the best place to start UI-updating operations.

---

#### Question 9
**Where should you stop a Timer to prevent memory leaks?**

A) `onPause()`  
B) `onStop()`  
C) `onDestroy()`  
D) All of the above

**Answer**: A) `onPause()`

**Explanation**: Stop the Timer in `onPause()` since the Activity is no longer interactive, and also ensure it's stopped in `onDestroy()` for complete cleanup.

---

#### Question 10
**Which lifecycle method is best for registering broadcast receivers?**

A) `onCreate()`  
B) `onStart()`  
C) `onResume()`  
D) `onPause()`

**Answer**: B) `onStart()`

**Explanation**: Register receivers in `onStart()` when the Activity becomes visible, and unregister them in `onStop()` when it's completely hidden.

---

### Section 4: Configuration Changes

#### Question 11
**What happens to an Activity when the user rotates the screen?**

A) Only `onPause()` and `onResume()` are called  
B) The Activity is recreated from scratch  
C) Nothing happens  
D) Only `onStop()` is called

**Answer**: B) The Activity is recreated from scratch

**Explanation**: Screen rotation is a configuration change that causes the Activity to be destroyed and recreated, calling the full lifecycle sequence.

---

#### Question 12
**Which method is called during screen rotation to save state before the Activity is destroyed?**

A) `onPause()`  
B) `onStop()`  
C) `onSaveInstanceState()`  
D) `onDestroy()`

**Answer**: C) `onSaveInstanceState()`

**Explanation**: `onSaveInstanceState()` is called before the Activity is destroyed during configuration changes to save state for restoration.

---

### Section 5: Real-World Scenarios

#### Question 13
**You're building a video player app. Where should you pause the video when the user receives a phone call?**

A) `onCreate()`  
B) `onStart()`  
C) `onPause()`  
D) `onStop()`

**Answer**: C) `onPause()`

**Explanation**: When a phone call comes in, the Activity is partially hidden, so `onPause()` is the right place to pause the video.

---

#### Question 14
**In a GPS navigation app, where should you start location updates?**

A) `onCreate()`  
B) `onStart()`  
C) `onResume()`  
D) `onPause()`

**Answer**: C) `onResume()`

**Explanation**: Start location updates in `onResume()` when the Activity is interactive and the user can see the navigation.

---

#### Question 15
**Where should you stop location updates to save battery?**

A) `onPause()`  
B) `onStop()`  
C) `onDestroy()`  
D) Both A and B

**Answer**: D) Both A and B

**Explanation**: Stop location updates in `onPause()` when the Activity is partially hidden, and ensure they're stopped in `onStop()` when completely hidden.

---

### Section 6: Code Analysis

#### Question 16
**Look at this code snippet. What's wrong with it?**

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    setContentView(R.layout.activity_main)
    super.onCreate(savedInstanceState)
    // Initialize views and data
}
```

A) Nothing is wrong  
B) `super.onCreate()` should be called first  
C) `setContentView()` should be called after `super.onCreate()`  
D) The Bundle parameter is not used

**Answer**: B) `super.onCreate()` should be called first

**Explanation**: Always call `super.onCreate()` before any other operations in `onCreate()`.

---

#### Question 17
**What will happen if you don't call `super.onPause()` in your `onPause()` method?**

A) Nothing, it's optional  
B) The app will crash  
C) The Activity won't pause properly  
D) Memory leaks will occur

**Answer**: C) The Activity won't pause properly

**Explanation**: Not calling `super.onPause()` prevents the parent class from performing necessary cleanup operations.

---

#### Question 18
**In this code, when will the counter be saved?**

```kotlin
override fun onPause() {
    super.onPause()
    sharedPreferences.edit().putInt("counter", counter).apply()
}

override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt("counter", counter)
}
```

A) Only when the app is paused  
B) Only during configuration changes  
C) Both when paused and during configuration changes  
D) Never

**Answer**: C) Both when paused and during configuration changes

**Explanation**: The counter is saved in `onPause()` for app lifecycle changes and in `onSaveInstanceState()` for configuration changes.

---

### Section 7: Advanced Concepts

#### Question 19
**What's the difference between `onPause()` and `onStop()`?**

A) There's no difference  
B) `onPause()` is called when partially hidden, `onStop()` when completely hidden  
C) `onPause()` is called when completely hidden, `onStop()` when partially hidden  
D) `onPause()` is called first, `onStop()` is called last

**Answer**: B) `onPause()` is called when partially hidden, `onStop()` when completely hidden

**Explanation**: `onPause()` is called when the Activity is partially visible (e.g., dialog overlay), while `onStop()` is called when it's completely hidden.

---

#### Question 20
**Which lifecycle method is best for performing heavy operations like network requests?**

A) `onCreate()`  
B) `onResume()`  
C) `onPause()`  
D) None of the above - use background threads

**Answer**: D) None of the above - use background threads

**Explanation**: Heavy operations should be performed on background threads, not in lifecycle methods, to keep the UI responsive.

---

## 📊 Quiz Results Interpretation

### Score 18-20 (90-100%): Expert Level
- Excellent understanding of Activity lifecycle
- Ready to build complex Android applications
- Consider learning advanced topics like ViewModel and LiveData

### Score 14-17 (70-89%): Proficient Level
- Good understanding of core concepts
- Can build basic to intermediate Android apps
- Review areas where you made mistakes

### Score 10-13 (50-69%): Intermediate Level
- Basic understanding but needs more practice
- Focus on the sections where you struggled
- Complete the exercises in the project

### Score 0-9 (0-49%): Beginner Level
- Need to review the fundamental concepts
- Start with the basic exercises
- Practice with the HelloLifecycle app

---

## 🔍 Review Questions

After taking the quiz, review these questions:

1. **Which lifecycle methods were most challenging?**
2. **What real-world scenarios confused you?**
3. **Which code patterns need more practice?**
4. **What concepts should you focus on next?**

---

## 📚 Further Learning

Based on your quiz performance, consider these next steps:

- **If you scored well**: Explore ViewModel, LiveData, and other lifecycle-aware components
- **If you need practice**: Complete all exercises in the HelloLifecycle project
- **If you struggled**: Review the README.md and LIFECYCLE_DIAGRAM.md files

---

## 🎯 Quiz Completion

Congratulations on completing the Activity Lifecycle quiz! Use your results to guide your learning and focus on areas that need improvement. Remember, understanding the Activity lifecycle is fundamental to building robust Android applications.

**Keep practicing and happy coding! 🚀**

