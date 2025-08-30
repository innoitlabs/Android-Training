# Exercises: FloatingActionButton and Snackbar

## Exercise Levels

This document contains exercises of varying difficulty to help you master FloatingActionButton and Snackbar concepts.

## Easy Level Exercises

### Exercise 1: Customize FAB Appearance
**Objective**: Modify the FAB's visual properties

**Tasks**:
1. Change the FAB background color to blue
2. Change the FAB icon to a different system icon
3. Modify the FAB size to be larger

**Code Hints**:
```xml
<!-- Change background color -->
app:backgroundTint="@android:color/holo_blue_dark"

<!-- Change icon -->
app:srcCompat="@android:drawable/ic_menu_add"

<!-- Change size -->
app:fabSize="large"
```

### Exercise 2: Modify Snackbar Messages
**Objective**: Customize Snackbar content and behavior

**Tasks**:
1. Change the Snackbar message text
2. Modify the action button text
3. Change Snackbar duration to SHORT

**Code Hints**:
```kotlin
Snackbar.make(view, "Your custom message here", Snackbar.LENGTH_SHORT)
    .setAction("Custom Action") {
        // Your action here
    }
    .show()
```

### Exercise 3: Add Multiple Snackbar Types
**Objective**: Create different types of feedback messages

**Tasks**:
1. Add a success Snackbar (green background)
2. Add an error Snackbar (red background)
3. Add an info Snackbar (blue background)

**Code Hints**:
```kotlin
// Success Snackbar
val snackbar = Snackbar.make(view, "Success!", Snackbar.LENGTH_LONG)
snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_green_light))
snackbar.show()
```

## Intermediate Level Exercises

### Exercise 4: Task Categories
**Objective**: Add different types of tasks with category-specific FABs

**Tasks**:
1. Create multiple FABs for different task types (Work, Personal, Shopping)
2. Each FAB should add tasks with different prefixes
3. Show category-specific Snackbar messages

**Implementation Steps**:
1. Add multiple FABs to the layout
2. Create separate click handlers for each FAB
3. Implement category-specific task creation logic

### Exercise 5: Task Completion System
**Objective**: Add the ability to mark tasks as complete

**Tasks**:
1. Add checkboxes next to each task
2. Implement task completion functionality
3. Show Snackbar when task is completed
4. Add "Undo Complete" functionality

**Code Structure**:
```kotlin
data class Task(
    val id: Int,
    val name: String,
    var isCompleted: Boolean = false
)
```

### Exercise 6: Custom Snackbar Styling
**Objective**: Create visually distinct Snackbar styles

**Tasks**:
1. Create custom Snackbar with different colors
2. Add icons to Snackbar messages
3. Implement custom Snackbar animations

**Code Hints**:
```kotlin
// Custom styled Snackbar
val snackbar = Snackbar.make(view, "Custom Message", Snackbar.LENGTH_LONG)
snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_orange_light))
snackbar.setTextColor(resources.getColor(android.R.color.white))
snackbar.show()
```

## Advanced Level Exercises

### Exercise 7: RecyclerView Integration
**Objective**: Replace TextView with RecyclerView for better task management

**Tasks**:
1. Create a TaskAdapter for RecyclerView
2. Implement task item layout with completion checkbox
3. Add swipe-to-delete functionality
4. Show Snackbar with undo for deleted tasks

**Implementation Steps**:
1. Add RecyclerView dependency
2. Create Task data class
3. Implement TaskAdapter
4. Add swipe gesture detection
5. Implement undo functionality

### Exercise 8: Persistent Storage
**Objective**: Save tasks to device storage

**Tasks**:
1. Implement SharedPreferences for task storage
2. Save tasks when app is closed
3. Load tasks when app is opened
4. Show Snackbar for storage operations

**Code Hints**:
```kotlin
// Save tasks
val sharedPrefs = getSharedPreferences("tasks", Context.MODE_PRIVATE)
val editor = sharedPrefs.edit()
editor.putString("task_list", tasks.joinToString(","))
editor.apply()

// Load tasks
val savedTasks = sharedPrefs.getString("task_list", "")
tasks.clear()
tasks.addAll(savedTasks?.split(",")?.filter { it.isNotEmpty() } ?: emptyList())
```

### Exercise 9: Advanced FAB Behavior
**Objective**: Implement complex FAB interactions

**Tasks**:
1. Add FAB animation on scroll
2. Implement FAB state changes (normal, loading, success)
3. Add FAB with multiple actions (speed dial)
4. Create FAB that transforms into different shapes

**Implementation Steps**:
1. Use CoordinatorLayout for scroll behavior
2. Implement custom FAB animations
3. Add state management for FAB
4. Create speed dial FAB with multiple options

## Challenge Exercise

### Exercise 10: Complete Task Manager App
**Objective**: Build a full-featured task management application

**Requirements**:
1. **Core Features**:
   - Add, edit, delete tasks
   - Mark tasks as complete/incomplete
   - Task categories and priorities
   - Due dates and reminders

2. **UI/UX Features**:
   - Material Design 3 components
   - Dark/Light theme support
   - Smooth animations and transitions
   - Accessibility features

3. **Advanced Features**:
   - Cloud sync capability
   - Task sharing
   - Statistics and analytics
   - Search and filtering

4. **Technical Requirements**:
   - MVVM architecture
   - Room database
   - ViewModel and LiveData
   - Repository pattern
   - Unit tests

## Exercise Submission Guidelines

### For Each Exercise:
1. **Documentation**: Explain what you implemented
2. **Code**: Provide clean, well-commented code
3. **Screenshots**: Show the app in action
4. **Challenges**: Describe any issues you encountered
5. **Improvements**: Suggest potential enhancements

### Evaluation Criteria:
- **Functionality**: Does the feature work as expected?
- **Code Quality**: Is the code clean and well-structured?
- **User Experience**: Is the UI intuitive and responsive?
- **Material Design**: Does it follow Material Design guidelines?
- **Error Handling**: Are edge cases properly handled?

## Resources for Exercises

### Documentation:
- [Material Design Guidelines](https://material.io/design)
- [Android Developer Documentation](https://developer.android.com/guide/topics/ui/floating-action-button)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)

### Libraries:
- [Material Components for Android](https://github.com/material-components/material-components-android)
- [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [Room Database](https://developer.android.com/training/data-storage/room)

### Tools:
- [Android Studio](https://developer.android.com/studio)
- [Layout Inspector](https://developer.android.com/studio/debug/layout-inspector)
- [Material Theme Editor](https://material.io/resources/theme-editor/)

---

**Note**: Start with Easy exercises and progress to more challenging ones. Each exercise builds upon the previous ones, so complete them in order for the best learning experience.
