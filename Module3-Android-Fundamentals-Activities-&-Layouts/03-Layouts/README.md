# Android UI Design: XML Layouts & Components

## Learning Objectives

By the end of this lesson, learners should be able to:
- Understand XML layout files and the ViewGroup hierarchy
- Use common UI components: TextView, EditText, Button, ImageView
- Apply layout managers like LinearLayout, RelativeLayout, ConstraintLayout
- Manage resources: strings, colors, dimensions, drawables
- Implement event handling and click listeners in Kotlin

## Table of Contents

1. [XML Layout Files & ViewGroup Hierarchy](#1-xml-layout-files--viewgroup-hierarchy)
2. [Common UI Components](#2-common-ui-components)
3. [Layout Managers](#3-layout-managers)
4. [Resource Management](#4-resource-management)
5. [Event Handling & Click Listeners](#5-event-handling--click-listeners)
6. [Hands-on Lab: Greeting App](#6-hands-on-lab-greeting-app)
7. [Exercises](#7-exercises)
8. [Summary](#8-summary)

---

## 1. XML Layout Files & ViewGroup Hierarchy

### What are XML Layout Files?

In Android, user interfaces are defined using XML layout files stored in the `res/layout` directory. These files describe the visual structure of your app's screens.

### ViewGroup Hierarchy

- **ViewGroup**: A container that can hold other views (child views)
- **View**: Individual UI elements like buttons, text fields, etc.
- **Hierarchy**: Parent ViewGroup → Child Views (tree structure)

### Basic Structure

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:id="@+id/titleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello Android!"
        android:textSize="20sp" />

</LinearLayout>
```

### Key Attributes Explained

- `android:layout_width` and `android:layout_height`: Define size
  - `match_parent`: Takes full available space
  - `wrap_content`: Sizes to content
  - Specific dimensions: `100dp`, `200sp`, etc.
- `android:id`: Unique identifier for referencing in Kotlin code
- `android:orientation`: For LinearLayout (vertical/horizontal)

---

## 2. Common UI Components

### TextView
Displays static text on the screen.

```xml
<TextView
    android:id="@+id/welcomeText"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Welcome to Android!"
    android:textSize="18sp"
    android:textColor="#333333"
    android:gravity="center" />
```

### EditText
Allows user input.

```xml
<EditText
    android:id="@+id/nameInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Enter your name"
    android:inputType="textPersonName"
    android:padding="12dp" />
```

### Button
Triggers actions when clicked.

```xml
<Button
    android:id="@+id/submitButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Submit"
    android:textSize="16sp"
    android:padding="12dp" />
```

### ImageView
Displays images.

```xml
<ImageView
    android:id="@+id/logoImage"
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:src="@drawable/ic_launcher_foreground"
    android:scaleType="centerCrop"
    android:contentDescription="App Logo" />
```

### Complete Example Layout

```xml
<LinearLayout
    android:orientation="vertical"
    android:padding="16dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:id="@+id/logoImage"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@drawable/ic_launcher_foreground"
        android:layout_gravity="center_horizontal"
        android:layout_marginBottom="24dp" />

    <EditText
        android:id="@+id/inputName"
        android:hint="Enter your name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp" />

    <Button
        android:id="@+id/submitButton"
        android:text="Submit"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginBottom="16dp" />

    <TextView
        android:id="@+id/outputText"
        android:text="Welcome!"
        android:textSize="18sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal" />

</LinearLayout>
```

---

## 3. Layout Managers

### LinearLayout
Arranges views in a single line (horizontal or vertical).

**Vertical Layout:**
```xml
<LinearLayout
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView android:text="First Item" />
    <TextView android:text="Second Item" />
    <TextView android:text="Third Item" />

</LinearLayout>
```

**Horizontal Layout:**
```xml
<LinearLayout
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center">

    <Button android:text="Left" />
    <Button android:text="Center" />
    <Button android:text="Right" />

</LinearLayout>
```

### RelativeLayout
Positions views relative to each other (less common now).

```xml
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/centerButton"
        android:text="Center"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true" />

    <Button
        android:text="Above"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@id/centerButton"
        android:layout_centerHorizontal="true" />

    <Button
        android:text="Below"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/centerButton"
        android:layout_centerHorizontal="true" />

</RelativeLayout>
```

### ConstraintLayout (Recommended)
Modern, flexible layout system recommended by Google.

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <TextView
        android:id="@+id/titleText"
        android:text="Welcome"
        android:textSize="24sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <EditText
        android:id="@+id/nameInput"
        android:hint="Enter your name"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        app:layout_constraintTop_toBottomOf="@id/titleText"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <Button
        android:id="@+id/loginButton"
        android:text="Login"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        app:layout_constraintTop_toBottomOf="@id/nameInput"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 4. Resource Management

### Strings (strings.xml)
Store text resources for easy localization and maintenance.

```xml
<!-- res/values/strings.xml -->
<resources>
    <string name="app_name">MyFirstApp</string>
    <string name="welcome_message">Welcome to Android!</string>
    <string name="enter_name_hint">Enter your name</string>
    <string name="submit_button">Submit</string>
    <string name="hello_format">Hello, %1$s!</string>
</resources>
```

### Colors (colors.xml)
Define color resources.

```xml
<!-- res/values/colors.xml -->
<resources>
    <color name="primaryColor">#6200EE</color>
    <color name="secondaryColor">#03DAC6</color>
    <color name="backgroundColor">#FFFFFF</color>
    <color name="textColor">#333333</color>
    <color name="hintColor">#757575</color>
</resources>
```

### Dimensions (dimens.xml)
Define spacing and font sizes.

```xml
<!-- res/values/dimens.xml -->
<resources>
    <dimen name="padding_normal">16dp</dimen>
    <dimen name="padding_small">8dp</dimen>
    <dimen name="padding_large">24dp</dimen>
    <dimen name="text_size_normal">16sp</dimen>
    <dimen name="text_size_large">20sp</dimen>
    <dimen name="text_size_small">14sp</dimen>
</resources>
```

### Using Resources in XML

```xml
<TextView
    android:text="@string/welcome_message"
    android:textColor="@color/textColor"
    android:textSize="@dimen/text_size_large"
    android:padding="@dimen/padding_normal" />

<Button
    android:text="@string/submit_button"
    android:backgroundTint="@color/primaryColor" />
```

### Using Resources in Kotlin

```kotlin
// Get string resource
val welcomeText = getString(R.string.welcome_message)

// Get string with formatting
val formattedText = getString(R.string.hello_format, "John")

// Get color resource
val primaryColor = ContextCompat.getColor(this, R.color.primaryColor)

// Get dimension resource
val padding = resources.getDimensionPixelSize(R.dimen.padding_normal)
```

---

## 5. Event Handling & Click Listeners

### Basic Click Listener

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views by ID
        val button = findViewById<Button>(R.id.submitButton)
        val input = findViewById<EditText>(R.id.inputName)
        val output = findViewById<TextView>(R.id.outputText)

        // Set click listener
        button.setOnClickListener {
            val name = input.text.toString()
            if (name.isNotEmpty()) {
                output.text = "Hello, $name!"
            } else {
                output.text = "Please enter a name"
            }
        }
    }
}
```

### Multiple Event Handlers

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val submitButton = findViewById<Button>(R.id.submitButton)
        val clearButton = findViewById<Button>(R.id.clearButton)
        val input = findViewById<EditText>(R.id.inputName)
        val output = findViewById<TextView>(R.id.outputText)

        // Submit button click
        submitButton.setOnClickListener {
            val name = input.text.toString()
            output.text = if (name.isNotEmpty()) {
                getString(R.string.hello_format, name)
            } else {
                getString(R.string.please_enter_name)
            }
        }

        // Clear button click
        clearButton.setOnClickListener {
            input.text.clear()
            output.text = ""
        }

        // Text change listener
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Enable/disable submit button based on input
                submitButton.isEnabled = s?.isNotEmpty() == true
            }
        })
    }
}
```

### Best Practices

1. **Avoid heavy logic in listeners**: Delegate complex operations to ViewModels or separate classes
2. **Use resource strings**: Never hardcode text in listeners
3. **Handle edge cases**: Check for null/empty values
4. **Use appropriate input types**: Set `android:inputType` for EditText
5. **Provide user feedback**: Show loading states, error messages, etc.

---

## 6. Hands-on Lab: Greeting App

### Project Overview
Build a simple app where:
1. User enters their name (EditText)
2. User clicks a button
3. A TextView updates with "Hello, [Name]!"
4. Use resources for strings and colors
5. Add an ImageView for branding

### Step-by-Step Implementation

#### Step 1: Create the Layout (activity_main.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="@dimen/padding_normal"
    android:gravity="center">

    <ImageView
        android:id="@+id/logoImage"
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:src="@drawable/ic_launcher_foreground"
        android:layout_marginBottom="@dimen/padding_large"
        android:contentDescription="@string/app_logo" />

    <TextView
        android:id="@+id/titleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/app_title"
        android:textSize="@dimen/text_size_large"
        android:textColor="@color/primaryColor"
        android:layout_marginBottom="@dimen/padding_large" />

    <EditText
        android:id="@+id/nameInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/enter_name_hint"
        android:inputType="textPersonName"
        android:padding="@dimen/padding_normal"
        android:layout_marginBottom="@dimen/padding_normal" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginBottom="@dimen/padding_normal">

        <Button
            android:id="@+id/submitButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/submit_button"
            android:layout_marginEnd="@dimen/padding_small" />

        <Button
            android:id="@+id/clearButton"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/clear_button" />

    </LinearLayout>

    <TextView
        android:id="@+id/outputText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/welcome_message"
        android:textSize="@dimen/text_size_normal"
        android:textColor="@color/textColor"
        android:gravity="center" />

</LinearLayout>
```

#### Step 2: Create Resources

**strings.xml:**
```xml
<resources>
    <string name="app_name">Greeting App</string>
    <string name="app_title">Welcome to Greeting App</string>
    <string name="app_logo">App Logo</string>
    <string name="enter_name_hint">Enter your name</string>
    <string name="submit_button">Submit</string>
    <string name="clear_button">Clear</string>
    <string name="welcome_message">Welcome!</string>
    <string name="hello_format">Hello, %1$s!</string>
    <string name="please_enter_name">Please enter a name</string>
</resources>
```

**colors.xml:**
```xml
<resources>
    <color name="primaryColor">#6200EE</color>
    <color name="secondaryColor">#03DAC6</color>
    <color name="backgroundColor">#FFFFFF</color>
    <color name="textColor">#333333</color>
    <color name="hintColor">#757575</color>
</resources>
```

**dimens.xml:**
```xml
<resources>
    <dimen name="padding_normal">16dp</dimen>
    <dimen name="padding_small">8dp</dimen>
    <dimen name="padding_large">24dp</dimen>
    <dimen name="text_size_normal">16sp</dimen>
    <dimen name="text_size_large">20sp</dimen>
    <dimen name="text_size_small">14sp</dimen>
</resources>
```

#### Step 3: Implement Kotlin Code (MainActivity.kt)

```kotlin
package com.example.layouts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var nameInput: EditText
    private lateinit var submitButton: Button
    private lateinit var clearButton: Button
    private lateinit var outputText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        initializeViews()
        
        // Set up event listeners
        setupEventListeners()
    }

    private fun initializeViews() {
        nameInput = findViewById(R.id.nameInput)
        submitButton = findViewById(R.id.submitButton)
        clearButton = findViewById(R.id.clearButton)
        outputText = findViewById(R.id.outputText)
    }

    private fun setupEventListeners() {
        // Submit button click listener
        submitButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            if (name.isNotEmpty()) {
                val greeting = getString(R.string.hello_format, name)
                outputText.text = greeting
            } else {
                outputText.text = getString(R.string.please_enter_name)
            }
        }

        // Clear button click listener
        clearButton.setOnClickListener {
            nameInput.text.clear()
            outputText.text = getString(R.string.welcome_message)
        }

        // Text change listener for real-time validation
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                submitButton.isEnabled = s?.toString()?.trim()?.isNotEmpty() == true
            }
        })
    }
}
```

---

## 7. Exercises

### Easy Exercise: Add Background Color Change
Add a button that changes the background color when clicked.

**Additional Layout:**
```xml
<Button
    android:id="@+id/colorButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/change_color"
    android:layout_marginTop="@dimen/padding_normal" />
```

**Additional Kotlin Code:**
```kotlin
private var isColorChanged = false

colorButton.setOnClickListener {
    val rootLayout = findViewById<LinearLayout>(R.id.rootLayout)
    if (isColorChanged) {
        rootLayout.setBackgroundColor(getColor(R.color.backgroundColor))
        colorButton.text = getString(R.string.change_color)
    } else {
        rootLayout.setBackgroundColor(getColor(R.color.secondaryColor))
        colorButton.text = getString(R.string.reset_color)
    }
    isColorChanged = !isColorChanged
}
```

### Intermediate Exercise: Input Validation
Add validation for the name input (minimum 2 characters, no numbers).

**Kotlin Code:**
```kotlin
private fun validateName(name: String): Boolean {
    return name.length >= 2 && name.all { it.isLetter() || it.isWhitespace() }
}

submitButton.setOnClickListener {
    val name = nameInput.text.toString().trim()
    when {
        name.isEmpty() -> {
            outputText.text = getString(R.string.please_enter_name)
            nameInput.error = getString(R.string.name_required)
        }
        !validateName(name) -> {
            outputText.text = getString(R.string.invalid_name)
            nameInput.error = getString(R.string.name_validation_error)
        }
        else -> {
            outputText.text = getString(R.string.hello_format, name)
            nameInput.error = null
        }
    }
}
```

### Advanced Exercise: ConstraintLayout Implementation
Convert the LinearLayout to ConstraintLayout for better responsiveness.

**ConstraintLayout Version:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="@dimen/padding_normal">

    <ImageView
        android:id="@+id/logoImage"
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:src="@drawable/ic_launcher_foreground"
        android:contentDescription="@string/app_logo"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp" />

    <TextView
        android:id="@+id/titleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/app_title"
        android:textSize="@dimen/text_size_large"
        android:textColor="@color/primaryColor"
        app:layout_constraintTop_toBottomOf="@id/logoImage"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/padding_large" />

    <EditText
        android:id="@+id/nameInput"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="@string/enter_name_hint"
        android:inputType="textPersonName"
        android:padding="@dimen/padding_normal"
        app:layout_constraintTop_toBottomOf="@id/titleText"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/padding_large" />

    <Button
        android:id="@+id/submitButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/submit_button"
        app:layout_constraintTop_toBottomOf="@id/nameInput"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toStartOf="@id/clearButton"
        android:layout_marginTop="@dimen/padding_normal" />

    <Button
        android:id="@+id/clearButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/clear_button"
        app:layout_constraintTop_toBottomOf="@id/nameInput"
        app:layout_constraintStart_toEndOf="@id/submitButton"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/padding_normal" />

    <TextView
        android:id="@+id/outputText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/welcome_message"
        android:textSize="@dimen/text_size_normal"
        android:textColor="@color/textColor"
        android:gravity="center"
        app:layout_constraintTop_toBottomOf="@id/submitButton"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="@dimen/padding_large" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 8. Summary

### Key Concepts Covered

1. **XML Layout Files**: UI is defined in XML files stored in `res/layout/`
2. **ViewGroup Hierarchy**: Parent containers hold child views in a tree structure
3. **Common UI Components**:
   - TextView: Display text
   - EditText: User input
   - Button: Trigger actions
   - ImageView: Display images

4. **Layout Managers**:
   - LinearLayout: Simple horizontal/vertical arrangement
   - RelativeLayout: Position relative to other views (legacy)
   - ConstraintLayout: Modern, flexible layout system (recommended)

5. **Resource Management**:
   - strings.xml: Text resources
   - colors.xml: Color definitions
   - dimens.xml: Dimensions and spacing
   - drawables/: Images and graphics

6. **Event Handling**:
   - setOnClickListener: Handle button clicks
   - TextWatcher: Monitor text changes
   - Best practices: Use resources, handle edge cases, avoid heavy logic

### Best Practices

- ✅ Use resource files for strings, colors, and dimensions
- ✅ Implement proper input validation
- ✅ Handle edge cases (empty input, null values)
- ✅ Use appropriate input types for EditText
- ✅ Provide user feedback for actions
- ✅ Use ConstraintLayout for complex layouts
- ❌ Avoid hardcoding text in code
- ❌ Don't put heavy logic in click listeners
- ❌ Don't ignore accessibility (contentDescription)

### Next Steps

1. Practice building different layouts
2. Experiment with different UI components
3. Learn about RecyclerView for lists
4. Explore Material Design components
5. Study ViewBinding for better view access
6. Learn about Data Binding for MVVM architecture

---

## Project Structure

```
Layouts/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/layouts/
│   │   │   └── MainActivity.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── values/
│   │   │   │   ├── strings.xml
│   │   │   │   ├── colors.xml
│   │   │   │   └── dimens.xml
│   │   │   └── drawable/
│   │   │       └── ic_launcher_foreground.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
└── README.md
```

This comprehensive guide provides everything needed to understand and implement Android UI design with XML layouts, from basic concepts to advanced techniques.
