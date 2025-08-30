# FloatingActionButton and Snackbar Tutorial

## Learning Objectives

By the end of this lesson, learners should be able to:
- Understand the FloatingActionButton (FAB) and its role in Material Design
- Add and configure a FAB in XML
- Handle click events on FAB in Kotlin
- Display Snackbar messages with action buttons
- Combine FAB + Snackbar for interactive UI flows

## 1. Introduction

### FloatingActionButton (FAB)
- A circular button that triggers a primary action in the app
- Commonly used for actions like Add, Compose, or Create
- Part of Material Components library
- Follows Material Design guidelines for placement and behavior

### Snackbar
- A lightweight feedback message displayed at the bottom of the screen
- Can include an optional Action button
- Replaces the older Toast for interactive, modern UX
- Automatically handles positioning and animations

## 2. Dependencies

The project already includes Material Components in `app/build.gradle.kts`:

```kotlin
implementation(libs.material)
```

This provides access to:
- `FloatingActionButton`
- `Snackbar`
- Other Material Design components

## 3. Adding a FloatingActionButton

### XML Example (activity_main.xml)

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    android:contentDescription="Add"
    app:srcCompat="@android:drawable/ic_input_add"
    app:layout_anchorGravity="bottom|end"
    app:backgroundTint="@color/purple_500"
    app:tint="@android:color/white"
    android:layout_gravity="bottom|end"/>
```

### Key Attributes Explained:
- `android:id`: Unique identifier for the FAB
- `android:contentDescription`: Accessibility description
- `app:srcCompat`: Icon to display on the FAB
- `app:backgroundTint`: Background color of the FAB
- `app:tint`: Color of the icon
- `android:layout_gravity`: Position of the FAB (bottom|end = bottom-right)

## 4. Showing a Snackbar

### Kotlin Example (MainActivity.kt)

```kotlin
package com.example.floatingbuttonsnackbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "FAB clicked!", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    Snackbar.make(view, "Undo action triggered", Snackbar.LENGTH_SHORT).show()
                }
                .show()
        }
    }
}
```

### Snackbar Methods Explained:
- `Snackbar.make(view, message, duration)`: Creates a Snackbar
- `setAction(actionText, clickListener)`: Adds an action button
- `show()`: Displays the Snackbar

### Duration Options:
- `Snackbar.LENGTH_SHORT`: Brief display
- `Snackbar.LENGTH_LONG`: Longer display
- `Snackbar.LENGTH_INDEFINITE`: Until dismissed

## 5. Behavior

### Expected Flow:
1. When FAB is clicked → Snackbar appears
2. Snackbar includes an "Undo" action button → shows another Snackbar
3. FAB remains anchored above the Snackbar (Material behavior)

### Material Design Behavior:
- FAB automatically moves up when Snackbar appears
- Snackbar slides up from the bottom
- Smooth animations and transitions

## 6. Best Practices

### FAB Guidelines:
- Use FAB only for the primary action on a screen
- Keep it simple - one main action per screen
- Use meaningful icons that represent the action
- Follow Material Design placement guidelines

### Snackbar Guidelines:
- Keep messages short and actionable
- Provide meaningful actions (Undo, Retry, Dismiss)
- Use appropriate duration for the message type
- Don't overuse - reserve for important feedback

### Material Theming:
- Use consistent colors from your theme
- Follow Material Design color guidelines
- Ensure proper contrast for accessibility

## 7. Hands-on Lab: Task Manager Demo

### Goal:
Create a FAB that adds a "New Task" and shows a Snackbar with Undo option.

### Features:
- FAB adds a new task to a list
- Snackbar confirms the action with Undo option
- On Undo, removes the last added task
- Demonstrates real-world usage pattern

## 8. Exercises

### Easy Level:
- Change FAB icon and color
- Modify Snackbar message text
- Adjust Snackbar duration

### Intermediate Level:
- Add a Snackbar without an action (auto-dismiss)
- Create multiple FABs with different actions
- Implement custom Snackbar styling

### Advanced Level:
- Combine FAB + RecyclerView
- Add new list items via FAB
- Show Snackbar for confirmation
- Implement undo functionality for list operations

## 9. Common Issues and Solutions

### FAB Not Visible:
- Check if Material Components dependency is added
- Verify layout constraints or gravity settings
- Ensure proper z-order in layout

### Snackbar Not Showing:
- Make sure the view parameter is correct
- Check if the activity is properly set up
- Verify Material theme is applied

### Styling Issues:
- Use Material theme in styles.xml
- Apply proper color attributes
- Check for theme conflicts

## 10. Summary

### Key Takeaways:
- FAB = Material circular button for the main action
- Snackbar = modern feedback UI with optional action
- Together, they create an interactive, user-friendly workflow
- Always follow Material Design guidelines for placement and usage

### Next Steps:
- Explore other Material Design components
- Learn about CoordinatorLayout for advanced FAB behavior
- Study Material Design principles and guidelines

## 11. Additional Resources

- [Material Design Guidelines](https://material.io/design)
- [Android Developer Documentation](https://developer.android.com/guide/topics/ui/floating-action-button)
- [Material Components for Android](https://github.com/material-components/material-components-android)

---

**Note:** This tutorial assumes basic knowledge of Android development with Kotlin. The project is set up with Material Components and ready to run.
