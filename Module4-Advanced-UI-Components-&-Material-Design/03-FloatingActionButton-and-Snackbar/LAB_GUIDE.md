# Hands-on Lab: FloatingActionButton and Snackbar

## Lab Overview

This lab will guide you through creating a simple task manager app that demonstrates the use of FloatingActionButton (FAB) and Snackbar in Android.

## Prerequisites

- Android Studio installed
- Basic knowledge of Kotlin and Android development
- Android emulator or physical device

## Lab Objectives

1. Add a FloatingActionButton to the main activity
2. Implement FAB click handling
3. Display Snackbar messages with action buttons
4. Create a simple task management system
5. Implement undo functionality

## Step 1: Update the Layout

### 1.1 Modify activity_main.xml

Replace the current content with a layout that includes:
- A TextView to display tasks
- A FloatingActionButton for adding tasks

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/titleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Task Manager"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginTop="32dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <TextView
        android:id="@+id/taskListText"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_margin="16dp"
        android:text="No tasks yet. Tap the + button to add a task!"
        android:textSize="16sp"
        android:gravity="top|start"
        app:layout_constraintTop_toBottomOf="@id/titleText"
        app:layout_constraintBottom_toTopOf="@id/fab"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:contentDescription="Add Task"
        app:srcCompat="@android:drawable/ic_input_add"
        app:backgroundTint="@color/purple_500"
        app:tint="@android:color/white"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Step 2: Update MainActivity.kt

### 2.1 Replace the MainActivity content

```kotlin
package com.example.floatingbuttonsnackbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    
    private lateinit var taskListText: TextView
    private val tasks = mutableListOf<String>()
    private var taskCounter = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        taskListText = findViewById(R.id.taskListText)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        
        // Set up FAB click listener
        fab.setOnClickListener { view ->
            addNewTask(view)
        }
    }
    
    private fun addNewTask(view: android.view.View) {
        val newTask = "Task $taskCounter"
        tasks.add(newTask)
        taskCounter++
        
        updateTaskDisplay()
        
        // Show Snackbar with undo option
        Snackbar.make(view, "Task added: $newTask", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                undoLastTask(view)
            }
            .show()
    }
    
    private fun undoLastTask(view: android.view.View) {
        if (tasks.isNotEmpty()) {
            val removedTask = tasks.removeLast()
            taskCounter--
            updateTaskDisplay()
            
            Snackbar.make(view, "Undone: $removedTask", Snackbar.LENGTH_SHORT).show()
        }
    }
    
    private fun updateTaskDisplay() {
        if (tasks.isEmpty()) {
            taskListText.text = "No tasks yet. Tap the + button to add a task!"
        } else {
            val taskText = tasks.joinToString("\n") { "• $it" }
            taskListText.text = taskText
        }
    }
}
```

## Step 3: Test the Application

### 3.1 Build and Run

1. Click the "Run" button in Android Studio
2. Select your emulator or device
3. Wait for the app to install and launch

### 3.2 Expected Behavior

1. **Initial State**: App shows "No tasks yet" message with a purple FAB in bottom-right
2. **Tap FAB**: Adds a new task and shows Snackbar with "Undo" option
3. **Tap Undo**: Removes the last task and shows confirmation
4. **Multiple Tasks**: Continue adding tasks to see the list grow

## Step 4: Experimentation

### 4.1 Easy Modifications

Try these changes to understand the components better:

1. **Change FAB Color**:
   ```xml
   app:backgroundTint="@android:color/holo_blue_dark"
   ```

2. **Change FAB Icon**:
   ```xml
   app:srcCompat="@android:drawable/ic_menu_add"
   ```

3. **Modify Snackbar Message**:
   ```kotlin
   Snackbar.make(view, "New task created successfully!", Snackbar.LENGTH_LONG)
   ```

### 4.2 Intermediate Challenges

1. **Add Task Names**: Modify the app to accept custom task names
2. **Task Completion**: Add checkboxes to mark tasks as complete
3. **Multiple FABs**: Add a second FAB for clearing all tasks

### 4.3 Advanced Features

1. **Persistent Storage**: Save tasks to SharedPreferences
2. **Task Categories**: Add different types of tasks
3. **Animations**: Add custom animations for task addition/removal

## Step 5: Troubleshooting

### Common Issues:

1. **FAB Not Visible**:
   - Check if Material Components dependency is included
   - Verify layout constraints are correct

2. **Snackbar Not Showing**:
   - Ensure the view parameter is correct
   - Check if the activity is properly initialized

3. **Build Errors**:
   - Sync project with Gradle files
   - Clean and rebuild the project

## Step 6: Lab Completion Checklist

- [ ] FAB appears in bottom-right corner
- [ ] FAB has proper styling and icon
- [ ] Clicking FAB adds a new task
- [ ] Snackbar appears with undo option
- [ ] Undo functionality works correctly
- [ ] Task list updates properly
- [ ] No build or runtime errors

## Next Steps

After completing this lab, you should:

1. Understand how FAB and Snackbar work together
2. Be comfortable with Material Design components
3. Know how to implement interactive feedback
4. Be ready to apply these concepts to your own projects

## Additional Resources

- [Material Design Guidelines](https://material.io/design)
- [Android Developer Documentation](https://developer.android.com/guide/topics/ui/floating-action-button)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
