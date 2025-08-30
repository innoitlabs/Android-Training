# Quick Reference: FloatingActionButton and Snackbar

## FloatingActionButton (FAB)

### Basic XML Structure
```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    android:contentDescription="Add"
    app:srcCompat="@android:drawable/ic_input_add"
    app:backgroundTint="@color/purple_500"
    app:tint="@android:color/white"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent" />
```

### Common Attributes
| Attribute | Description | Example |
|-----------|-------------|---------|
| `android:id` | Unique identifier | `@+id/fab` |
| `android:contentDescription` | Accessibility description | `"Add new item"` |
| `app:srcCompat` | Icon resource | `@android:drawable/ic_input_add` |
| `app:backgroundTint` | Background color | `@color/purple_500` |
| `app:tint` | Icon color | `@android:color/white` |
| `app:fabSize` | Size of FAB | `normal`, `large`, `mini` |
| `app:elevation` | Shadow depth | `6dp` |

### FAB Sizes
- `normal`: 56dp (default)
- `large`: 64dp
- `mini`: 40dp

## Snackbar

### Basic Usage
```kotlin
Snackbar.make(view, "Message", Snackbar.LENGTH_LONG)
    .setAction("Action") {
        // Action code here
    }
    .show()
```

### Duration Options
- `Snackbar.LENGTH_SHORT`: Brief display
- `Snackbar.LENGTH_LONG`: Longer display
- `Snackbar.LENGTH_INDEFINITE`: Until dismissed

### Custom Styling
```kotlin
val snackbar = Snackbar.make(view, "Message", Snackbar.LENGTH_LONG)
snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_green_light))
snackbar.setTextColor(resources.getColor(android.R.color.white))
snackbar.show()
```

## Common Patterns

### FAB + Snackbar Pattern
```kotlin
fab.setOnClickListener { view ->
    // Perform action
    performAction()
    
    // Show feedback
    Snackbar.make(view, "Action completed", Snackbar.LENGTH_LONG)
        .setAction("Undo") {
            undoAction()
        }
        .show()
}
```

### Multiple FABs
```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab1"
    app:srcCompat="@android:drawable/ic_input_add"
    app:backgroundTint="@color/blue" />

<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab2"
    app:srcCompat="@android:drawable/ic_menu_edit"
    app:backgroundTint="@color/green" />
```

### FAB with Speed Dial
```kotlin
// Show/hide secondary FABs based on primary FAB state
private fun toggleSpeedDial() {
    val isExpanded = fab2.visibility == View.VISIBLE
    
    if (isExpanded) {
        fab2.animate().alpha(0f).withEndAction {
            fab2.visibility = View.GONE
        }.start()
    } else {
        fab2.visibility = View.VISIBLE
        fab2.animate().alpha(1f).start()
    }
}
```

## Material Design Guidelines

### FAB Placement
- **Primary Action**: Bottom-right corner (most common)
- **Secondary Actions**: Can be positioned elsewhere
- **Avoid**: Top of screen, center of content

### FAB Behavior
- **Scroll**: FAB should hide on scroll down, show on scroll up
- **Snackbar**: FAB should move up when Snackbar appears
- **Animation**: Use smooth transitions

### Snackbar Guidelines
- **Message Length**: Keep under 2 lines
- **Action Button**: Use for undo, retry, or dismiss
- **Duration**: Use SHORT for confirmations, LONG for important info
- **Position**: Always at bottom of screen

## Troubleshooting

### Common Issues

#### FAB Not Visible
```kotlin
// Check if Material Components is included
implementation 'com.google.android.material:material:1.12.0'

// Verify layout constraints
app:layout_constraintBottom_toBottomOf="parent"
app:layout_constraintEnd_toEndOf="parent"
```

#### Snackbar Not Showing
```kotlin
// Ensure view parameter is correct
Snackbar.make(findViewById(android.R.id.content), "Message", Snackbar.LENGTH_LONG)

// Check if activity is properly initialized
setContentView(R.layout.activity_main)
```

#### API Compatibility Issues
```kotlin
// Use compatible methods for older API levels
// Instead of: tasks.removeLast()
val removedTask = tasks.removeAt(tasks.lastIndex)
```

## Best Practices

### Code Organization
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private lateinit var taskList: MutableList<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupClickListeners()
    }
    
    private fun initializeViews() {
        fab = findViewById(R.id.fab)
        // Initialize other views
    }
    
    private fun setupClickListeners() {
        fab.setOnClickListener { view ->
            handleFabClick(view)
        }
    }
    
    private fun handleFabClick(view: View) {
        // Handle FAB click
        showSnackbar(view, "Action performed")
    }
    
    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Undo") { /* Undo action */ }
            .show()
    }
}
```

### Error Handling
```kotlin
private fun safeAddTask(view: View) {
    try {
        addTask()
        showSuccessSnackbar(view)
    } catch (e: Exception) {
        showErrorSnackbar(view, "Failed to add task")
    }
}
```

## Performance Tips

1. **Reuse Snackbar**: Don't create new instances unnecessarily
2. **Efficient Animations**: Use hardware acceleration
3. **Memory Management**: Clear references in onDestroy()
4. **Layout Optimization**: Use ConstraintLayout for complex layouts

---

**Remember**: Always follow Material Design guidelines and test on different screen sizes and API levels.
