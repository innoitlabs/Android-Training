# Practical Guide: Building the Greeting App

## Overview
This guide walks you through building a complete Android app that demonstrates XML layouts, UI components, resource management, and event handling.

## Prerequisites
- Android Studio installed
- Basic understanding of Kotlin
- Android device or emulator

## Step-by-Step Implementation

### Step 1: Project Setup

1. Open Android Studio
2. Create a new project with the following settings:
   - **Name**: Greeting App
   - **Package name**: com.example.layouts
   - **Language**: Kotlin
   - **Minimum SDK**: API 24 (Android 7.0)

### Step 2: Update Resource Files

#### 2.1 Update strings.xml
Navigate to `app/src/main/res/values/strings.xml` and replace the content:

```xml
<?xml version="1.0" encoding="utf-8"?>
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
    <string name="change_color">Change Color</string>
    <string name="reset_color">Reset Color</string>
    <string name="name_required">Name is required</string>
    <string name="invalid_name">Invalid name format</string>
    <string name="name_validation_error">Name must be at least 2 characters and contain only letters</string>
</resources>
```

#### 2.2 Update colors.xml
Navigate to `app/src/main/res/values/colors.xml` and replace the content:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="primaryColor">#6200EE</color>
    <color name="secondaryColor">#03DAC6</color>
    <color name="backgroundColor">#FFFFFF</color>
    <color name="textColor">#333333</color>
    <color name="hintColor">#757575</color>
    <color name="errorColor">#B00020</color>
</resources>
```

#### 2.3 Create dimens.xml
Create a new file `app/src/main/res/values/dimens.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <dimen name="padding_normal">16dp</dimen>
    <dimen name="padding_small">8dp</dimen>
    <dimen name="padding_large">24dp</dimen>
    <dimen name="text_size_normal">16sp</dimen>
    <dimen name="text_size_large">20sp</dimen>
    <dimen name="text_size_small">14sp</dimen>
</resources>
```

### Step 3: Create the Layout

Navigate to `app/src/main/res/layout/activity_main.xml` and replace the content:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/rootLayout"
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

    <Button
        android:id="@+id/colorButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/change_color"
        android:layout_marginBottom="@dimen/padding_normal" />

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

### Step 4: Implement the Kotlin Code

Navigate to `app/src/main/java/com/example/layouts/MainActivity.kt` and replace the content:

```kotlin
package com.example.layouts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var nameInput: EditText
    private lateinit var submitButton: Button
    private lateinit var clearButton: Button
    private lateinit var colorButton: Button
    private lateinit var outputText: TextView
    private lateinit var rootLayout: LinearLayout
    
    private var isColorChanged = false

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
        colorButton = findViewById(R.id.colorButton)
        outputText = findViewById(R.id.outputText)
        rootLayout = findViewById(R.id.rootLayout)
    }

    private fun setupEventListeners() {
        // Submit button click listener
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
                    val greeting = getString(R.string.hello_format, name)
                    outputText.text = greeting
                    nameInput.error = null
                }
            }
        }

        // Clear button click listener
        clearButton.setOnClickListener {
            nameInput.text.clear()
            outputText.text = getString(R.string.welcome_message)
            nameInput.error = null
        }

        // Color change button click listener
        colorButton.setOnClickListener {
            if (isColorChanged) {
                rootLayout.setBackgroundColor(getColor(R.color.backgroundColor))
                colorButton.text = getString(R.string.change_color)
            } else {
                rootLayout.setBackgroundColor(getColor(R.color.secondaryColor))
                colorButton.text = getString(R.string.reset_color)
            }
            isColorChanged = !isColorChanged
        }

        // Text change listener for real-time validation
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                submitButton.isEnabled = s?.toString()?.trim()?.isNotEmpty() == true
                // Clear error when user starts typing
                if (s?.isNotEmpty() == true) {
                    nameInput.error = null
                }
            }
        })
    }

    private fun validateName(name: String): Boolean {
        return name.length >= 2 && name.all { it.isLetter() || it.isWhitespace() }
    }
}
```

### Step 5: Build and Run

1. Click the "Sync Project with Gradle Files" button (elephant icon)
2. Wait for the sync to complete
3. Connect your Android device or start an emulator
4. Click the "Run" button (green play icon)
5. Select your device/emulator and click "OK"

## Expected Behavior

When you run the app, you should see:

1. **App Logo**: An image at the top
2. **Title**: "Welcome to Greeting App"
3. **Input Field**: Where you can enter your name
4. **Submit Button**: Processes the name input
5. **Clear Button**: Clears the input and output
6. **Change Color Button**: Toggles background color
7. **Output Text**: Shows greeting or messages

## Testing the App

1. **Basic Functionality**:
   - Enter a name and click "Submit"
   - Should display "Hello, [Name]!"

2. **Validation**:
   - Try submitting with empty input
   - Try entering numbers or special characters
   - Should show appropriate error messages

3. **Clear Function**:
   - Enter text and click "Clear"
   - Should clear both input and output

4. **Color Change**:
   - Click "Change Color" button
   - Background should change color
   - Button text should change to "Reset Color"

## Troubleshooting

### Common Issues:

1. **Build Errors**:
   - Make sure all resource files are properly formatted
   - Check that all string references exist in strings.xml

2. **Runtime Errors**:
   - Ensure all view IDs match between XML and Kotlin
   - Check that findViewById calls use correct view types

3. **Layout Issues**:
   - Verify that all dimensions are properly defined
   - Check that color resources are correctly referenced

### Debug Tips:

1. Use Logcat to view error messages
2. Add breakpoints in your Kotlin code
3. Use the Layout Inspector to debug UI issues
4. Test on different screen sizes using the AVD Manager

## Next Steps

After completing this basic app, try these enhancements:

1. **Add More UI Components**:
   - CheckBox for terms and conditions
   - RadioButton for gender selection
   - SeekBar for age input

2. **Improve Layout**:
   - Convert to ConstraintLayout
   - Add scrollable content
   - Implement responsive design

3. **Add Animations**:
   - Fade in/out effects
   - Button press animations
   - Text change animations

4. **Implement Data Persistence**:
   - Save user preferences
   - Store recent names
   - Add a history feature

This practical guide provides a complete foundation for Android UI development with XML layouts and Kotlin event handling.
