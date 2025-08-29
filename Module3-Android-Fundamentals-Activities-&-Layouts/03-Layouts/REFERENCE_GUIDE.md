# Android UI Development Reference Guide

## Quick Reference Tables

### Layout Managers Comparison

| Layout Manager | Use Case | Pros | Cons | Performance |
|----------------|----------|------|------|-------------|
| **LinearLayout** | Simple lists, forms | Easy to use, predictable | Limited flexibility | Good |
| **RelativeLayout** | Complex positioning | Flexible positioning | Hard to maintain | Poor |
| **ConstraintLayout** | Modern complex layouts | Most flexible, flat hierarchy | Steep learning curve | Excellent |
| **FrameLayout** | Single child, overlays | Simple, fast | Limited functionality | Excellent |

### Common UI Components

| Component | Purpose | Key Attributes | Common Use Cases |
|-----------|---------|----------------|------------------|
| **TextView** | Display text | `text`, `textSize`, `textColor` | Labels, headings, content |
| **EditText** | User input | `hint`, `inputType`, `text` | Forms, search, comments |
| **Button** | Trigger actions | `text`, `onClick`, `enabled` | Submit, navigation, actions |
| **ImageView** | Display images | `src`, `scaleType`, `contentDescription` | Icons, photos, graphics |
| **CheckBox** | Boolean selection | `checked`, `text` | Settings, preferences |
| **RadioButton** | Single choice | `checked`, `text` | Options, surveys |
| **Switch** | Toggle setting | `checked`, `text` | Settings, features |
| **SeekBar** | Range selection | `max`, `progress` | Volume, brightness, ratings |

### Layout Parameters

| Parameter | Values | Description |
|-----------|--------|-------------|
| `layout_width` | `match_parent`, `wrap_content`, `dp` | Width of the view |
| `layout_height` | `match_parent`, `wrap_content`, `dp` | Height of the view |
| `layout_margin` | `dp` | External spacing |
| `layout_padding` | `dp` | Internal spacing |
| `layout_weight` | `float` | Distribution of space (LinearLayout) |
| `layout_gravity` | `center`, `start`, `end`, etc. | Alignment within parent |

### Common Dimensions

| Dimension | Value | Usage |
|-----------|-------|-------|
| `8dp` | Small spacing | Between related elements |
| `16dp` | Standard spacing | Between sections |
| `24dp` | Large spacing | Between major sections |
| `48dp` | Touch target | Minimum button size |
| `56dp` | Standard touch target | Material Design buttons |

### Color System

| Color Type | Hex Value | Usage |
|------------|-----------|-------|
| Primary | `#6200EE` | Main brand color |
| Primary Dark | `#3700B3` | Status bar, dark theme |
| Secondary | `#03DAC6` | Accent color |
| Background | `#FFFFFF` | Screen background |
| Surface | `#FFFFFF` | Card backgrounds |
| Error | `#B00020` | Error states |
| On Primary | `#FFFFFF` | Text on primary color |
| On Secondary | `#000000` | Text on secondary color |

---

## Common Layout Patterns

### 1. Form Layout Pattern

```xml
<LinearLayout
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:text="Form Title"
        android:textSize="20sp"
        android:layout_marginBottom="16dp" />

    <EditText
        android:hint="Field 1"
        android:layout_marginBottom="8dp" />

    <EditText
        android:hint="Field 2"
        android:layout_marginBottom="8dp" />

    <Button
        android:text="Submit"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

### 2. Card Layout Pattern

```xml
<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardElevation="4dp"
    app:cardCornerRadius="8dp">

    <LinearLayout
        android:orientation="vertical"
        android:padding="16dp">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:scaleType="centerCrop" />

        <TextView
            android:text="Card Title"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginTop="8dp" />

        <TextView
            android:text="Card description goes here..."
            android:layout_marginTop="4dp" />

    </LinearLayout>

</androidx.cardview.widget.CardView>
```

### 3. List Item Pattern

```xml
<LinearLayout
    android:orientation="horizontal"
    android:padding="16dp"
    android:gravity="center_vertical">

    <ImageView
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:layout_marginEnd="16dp" />

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:orientation="vertical">

        <TextView
            android:text="Primary Text"
            android:textSize="16sp"
            android:textStyle="bold" />

        <TextView
            android:text="Secondary Text"
            android:textSize="14sp"
            android:textColor="@color/hintColor" />

    </LinearLayout>

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Action" />

</LinearLayout>
```

---

## Event Handling Patterns

### 1. Button Click Pattern

```kotlin
// Basic click listener
button.setOnClickListener {
    // Handle click
}

// With view reference
button.setOnClickListener { view ->
    // Access the clicked view
    view.isEnabled = false
}

// Lambda with multiple statements
button.setOnClickListener {
    performAction()
    updateUI()
    showFeedback()
}
```

### 2. Text Change Pattern

```kotlin
editText.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Called before text changes
    }
    
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Called when text is changing
    }
    
    override fun afterTextChanged(s: Editable?) {
        // Called after text has changed
        validateInput(s?.toString())
    }
})
```

### 3. Form Validation Pattern

```kotlin
private fun validateForm(): Boolean {
    var isValid = true
    
    // Validate name
    if (nameInput.text.isNullOrBlank()) {
        nameInput.error = "Name is required"
        isValid = false
    }
    
    // Validate email
    if (!isValidEmail(emailInput.text.toString())) {
        emailInput.error = "Invalid email format"
        isValid = false
    }
    
    return isValid
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
```

---

## Resource Management Best Practices

### 1. String Resources

```xml
<!-- Always use string resources -->
<string name="welcome_message">Welcome!</string>
<string name="hello_format">Hello, %1$s!</string>
<string name="price_format">$%1$.2f</string>
<string name="items_count">%1$d items</string>
```

### 2. Color Resources

```xml
<!-- Define semantic colors -->
<color name="primary">#6200EE</color>
<color name="primary_dark">#3700B3</color>
<color name="accent">#03DAC6</color>
<color name="text_primary">#212121</color>
<color name="text_secondary">#757575</color>
```

### 3. Dimension Resources

```xml
<!-- Define consistent spacing -->
<dimen name="spacing_small">8dp</dimen>
<dimen name="spacing_medium">16dp</dimen>
<dimen name="spacing_large">24dp</dimen>
<dimen name="text_size_small">14sp</dimen>
<dimen name="text_size_medium">16sp</dimen>
<dimen name="text_size_large">20sp</dimen>
```

---

## Common Input Types

| Input Type | Description | Example |
|------------|-------------|---------|
| `text` | Plain text | Names, descriptions |
| `textPersonName` | Person names | First name, last name |
| `textEmailAddress` | Email addresses | user@example.com |
| `textPassword` | Passwords | ******** |
| `textUri` | URLs | https://example.com |
| `number` | Numbers | 123, 456 |
| `phone` | Phone numbers | +1-555-123-4567 |
| `date` | Dates | 2024-03-15 |
| `time` | Time | 14:30 |

---

## Accessibility Guidelines

### 1. Content Descriptions

```xml
<!-- Always provide content descriptions for images -->
<ImageView
    android:contentDescription="@string/app_logo_description" />

<!-- Use meaningful descriptions -->
<string name="app_logo_description">App logo showing a green Android robot</string>
```

### 2. Touch Targets

```xml
<!-- Ensure minimum 48dp touch targets -->
<Button
    android:minHeight="48dp"
    android:minWidth="48dp" />
```

### 3. Text Contrast

```xml
<!-- Use sufficient color contrast -->
<TextView
    android:textColor="@color/text_primary"
    android:background="@color/background_light" />
```

---

## Performance Tips

### 1. Layout Optimization

- Use `ConstraintLayout` for complex layouts
- Avoid nested `LinearLayout` with `layout_weight`
- Use `merge` tag for included layouts
- Minimize view hierarchy depth

### 2. View Inflation

```kotlin
// Use ViewBinding for better performance
private lateinit var binding: ActivityMainBinding

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
}
```

### 3. Resource Management

- Reuse drawables and layouts
- Use vector drawables when possible
- Optimize images for different densities
- Use appropriate resource qualifiers

---

## Debugging Tools

### 1. Layout Inspector

- Use Android Studio's Layout Inspector
- Inspect view hierarchy in real-time
- Debug layout issues
- Analyze performance

### 2. Hierarchy Viewer

- Analyze view hierarchy
- Identify performance bottlenecks
- Optimize layout structure

### 3. Lint Checks

```xml
<!-- Enable lint checks in build.gradle -->
android {
    lintOptions {
        checkReleaseBuilds false
        abortOnError false
    }
}
```

---

## Common Mistakes to Avoid

### ❌ Don't Do This

```xml
<!-- Hardcoded strings -->
<TextView android:text="Hello World" />

<!-- Hardcoded colors -->
<TextView android:textColor="#FF0000" />

<!-- Hardcoded dimensions -->
<TextView android:textSize="16sp" />

<!-- Deep nested layouts -->
<LinearLayout>
    <LinearLayout>
        <LinearLayout>
            <LinearLayout>
                <TextView />
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>
</LinearLayout>
```

### ✅ Do This Instead

```xml
<!-- Use string resources -->
<TextView android:text="@string/hello_world" />

<!-- Use color resources -->
<TextView android:textColor="@color/error_red" />

<!-- Use dimension resources -->
<TextView android:textSize="@dimen/text_size_medium" />

<!-- Use ConstraintLayout for complex layouts -->
<androidx.constraintlayout.widget.ConstraintLayout>
    <TextView app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## Quick Troubleshooting

### Common Issues and Solutions

| Issue | Cause | Solution |
|-------|-------|----------|
| Views not visible | Wrong layout parameters | Check `layout_width` and `layout_height` |
| Text cut off | Insufficient space | Use `wrap_content` or adjust constraints |
| Buttons not responding | Missing click listener | Add `setOnClickListener` |
| Layout looks wrong on different screens | Hardcoded dimensions | Use `dp` and `sp` units |
| App crashes on startup | Missing resource | Check all resource references |

This reference guide provides quick access to common patterns, best practices, and solutions for Android UI development.
