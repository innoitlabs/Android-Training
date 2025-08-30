# Android Accessibility Exercises

## Exercise 1: Basic Content Descriptions (Easy)

### Objective
Add content descriptions to all ImageViews in a simple layout.

### Task
Create a layout with the following elements and add appropriate content descriptions:

```xml
<!-- Add content descriptions to these elements -->
<ImageView
    android:id="@+id/logo"
    android:layout_width="120dp"
    android:layout_height="60dp"
    android:src="@drawable/ic_launcher_foreground" />

<ImageView
    android:id="@+id/avatar"
    android:layout_width="80dp"
    android:layout_height="80dp"
    android:src="@drawable/ic_launcher_foreground" />

<ImageView
    android:id="@+id/divider"
    android:layout_width="match_parent"
    android:layout_height="1dp"
    android:background="@color/black" />
```

### Expected Solution
```xml
<ImageView
    android:id="@+id/logo"
    android:layout_width="120dp"
    android:layout_height="60dp"
    android:src="@drawable/ic_launcher_foreground"
    android:contentDescription="Company logo" />

<ImageView
    android:id="@+id/avatar"
    android:layout_width="80dp"
    android:layout_height="80dp"
    android:src="@drawable/ic_launcher_foreground"
    android:contentDescription="User profile picture" />

<ImageView
    android:id="@+id/divider"
    android:layout_width="match_parent"
    android:layout_height="1dp"
    android:background="@color/black"
    android:contentDescription="@null" />
```

### Testing
1. Enable TalkBack
2. Navigate to each ImageView
3. Verify that logo and avatar are announced, but divider is not

---

## Exercise 2: Focus Order Management (Intermediate)

### Objective
Implement proper focus order for a form layout.

### Task
Create a registration form with the following elements and set up logical focus order:

```xml
<EditText
    android:id="@+id/nameInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Full Name" />

<EditText
    android:id="@+id/emailInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Email Address" />

<EditText
    android:id="@+id/passwordInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Password"
    android:inputType="textPassword" />

<Button
    android:id="@+id/registerButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Register" />

<Button
    android:id="@+id/cancelButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Cancel" />
```

### Expected Solution
```xml
<EditText
    android:id="@+id/nameInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Full Name"
    android:nextFocusDown="@id/emailInput" />

<EditText
    android:id="@+id/emailInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Email Address"
    android:nextFocusDown="@id/passwordInput" />

<EditText
    android:id="@+id/passwordInput"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Password"
    android:inputType="textPassword"
    android:nextFocusDown="@id/registerButton" />

<Button
    android:id="@+id/registerButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Register"
    android:nextFocusRight="@id/cancelButton" />

<Button
    android:id="@+id/cancelButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Cancel"
    android:nextFocusLeft="@id/registerButton" />
```

### Testing
1. Enable TalkBack
2. Use swipe gestures to navigate through the form
3. Verify focus moves logically from top to bottom
4. Test horizontal navigation between buttons

---

## Exercise 3: Custom Accessibility Actions (Advanced)

### Objective
Create a custom button with additional accessibility actions.

### Task
Implement a custom button that supports both click and long-press actions with proper accessibility announcements.

### Implementation
```kotlin
class AccessibleActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    
    private var onLongClickListener: OnLongClickListener? = null
    
    init {
        setupAccessibility()
    }
    
    private fun setupAccessibility() {
        accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfo
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                
                // Add custom long press action
                info.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_LONG_CLICK,
                        "Long press to show options"
                    )
                )
                
                // Customize the announcement
                val actionText = if (text.isNotEmpty()) {
                    "Button, $text. Double tap to activate, long press for options"
                } else {
                    "Button. Double tap to activate, long press for options"
                }
                info.text = actionText
            }
        }
    }
    
    override fun setOnLongClickListener(listener: OnLongClickListener?) {
        super.setOnLongClickListener(listener)
        onLongClickListener = listener
    }
    
    override fun performAccessibilityAction(action: Int, arguments: Bundle?): Boolean {
        return when (action) {
            AccessibilityNodeInfo.ACTION_LONG_CLICK -> {
                onLongClickListener?.onLongClick(this) ?: false
            }
            else -> super.performAccessibilityAction(action, arguments)
        }
    }
}
```

### Usage
```kotlin
val actionButton = findViewById<AccessibleActionButton>(R.id.actionButton)
actionButton.text = "Save Profile"
actionButton.setOnClickListener {
    // Handle normal click
    saveProfile()
}
actionButton.setOnLongClickListener {
    // Handle long press
    showOptionsDialog()
    true
}
```

### Testing
1. Enable TalkBack
2. Navigate to the button
3. Verify it announces both click and long-press actions
4. Test both double-tap and long-press gestures

---

## Exercise 4: Live Region Updates (Advanced)

### Objective
Implement live region updates for dynamic content.

### Task
Create a progress indicator that announces updates to screen readers.

### Implementation
```kotlin
class AccessibleProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var progress = 0
    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    
    init {
        isFocusable = true
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
    }
    
    fun setProgress(newProgress: Int) {
        progress = newProgress.coerceIn(0, 100)
        updateAccessibilityInfo()
        invalidate()
    }
    
    private fun updateAccessibilityInfo() {
        contentDescription = "Progress: $progress%"
        announceForAccessibility("Progress updated to $progress%")
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val progressWidth = (width * progress / 100f)
        canvas.drawRect(0f, 0f, progressWidth, height.toFloat(), paint)
    }
    
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = "ProgressIndicator"
        info.contentDescription = "Progress: $progress%"
        info.addAction(
            AccessibilityNodeInfo.AccessibilityAction(
                AccessibilityNodeInfo.ACTION_CLICK,
                "Update progress"
            )
        )
    }
}
```

### Usage
```kotlin
val progressView = findViewById<AccessibleProgressView>(R.id.progressView)

// Simulate progress updates
Handler(Looper.getMainLooper()).postDelayed({
    progressView.setProgress(25)
}, 1000)

Handler(Looper.getMainLooper()).postDelayed({
    progressView.setProgress(50)
}, 2000)

Handler(Looper.getMainLooper()).postDelayed({
    progressView.setProgress(100)
}, 3000)
```

### Testing
1. Enable TalkBack
2. Start the progress simulation
3. Verify that progress updates are announced
4. Test navigation to the progress view

---

## Exercise 5: Form Validation with Accessibility (Advanced)

### Objective
Create a form with validation that provides accessible error messages.

### Task
Implement a form with real-time validation and accessible error announcements.

### Implementation
```kotlin
class AccessibleFormActivity : AppCompatActivity() {
    
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var errorText: TextView
    private lateinit var submitButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accessible_form)
        
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        errorText = findViewById(R.id.errorText)
        submitButton = findViewById(R.id.submitButton)
        
        setupAccessibility()
        setupValidation()
    }
    
    private fun setupAccessibility() {
        // Set up live region for error messages
        errorText.accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE
        
        // Link labels to inputs
        findViewById<TextView>(R.id.nameLabel).labelFor = R.id.nameInput
        findViewById<TextView>(R.id.emailLabel).labelFor = R.id.emailInput
    }
    
    private fun setupValidation() {
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        emailInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    
    private fun validateName(name: String) {
        if (name.length < 2) {
            showError("Name must be at least 2 characters long")
        } else {
            clearError()
        }
    }
    
    private fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            showError("Please enter a valid email address")
        } else {
            clearError()
        }
    }
    
    private fun showError(message: String) {
        errorText.text = message
        errorText.announceForAccessibility(message)
    }
    
    private fun clearError() {
        errorText.text = ""
    }
}
```

### Layout
```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">
    
    <TextView
        android:id="@+id/nameLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Name:"
        android:labelFor="@id/nameInput" />
    
    <EditText
        android:id="@+id/nameInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter your name" />
    
    <TextView
        android:id="@+id/emailLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Email:"
        android:labelFor="@id/emailInput" />
    
    <EditText
        android:id="@+id/emailInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter your email"
        android:inputType="textEmailAddress" />
    
    <TextView
        android:id="@+id/errorText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@color/error_red"
        android:visibility="gone" />
    
    <Button
        android:id="@+id/submitButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Submit" />
    
</LinearLayout>
```

### Testing
1. Enable TalkBack
2. Navigate through the form fields
3. Enter invalid data and verify error announcements
4. Test that error messages are announced immediately
5. Verify that labels are properly associated with inputs

---

## Final Challenge: Complete Accessible App

### Objective
Create a complete app that demonstrates all accessibility features learned.

### Requirements
1. **Multiple Screens**: At least 2 activities with proper navigation
2. **Forms**: Input validation with accessible error messages
3. **Custom Views**: At least one custom view with accessibility support
4. **Dynamic Content**: Live region updates for status changes
5. **Custom Actions**: Buttons with additional accessibility actions
6. **Focus Management**: Logical tab order throughout the app
7. **Content Descriptions**: All images and icons properly described

### Suggested App: Task Manager
- **Main Screen**: List of tasks with add/delete actions
- **Add Task Screen**: Form with validation
- **Task Detail Screen**: View and edit task details
- **Settings Screen**: App preferences

### Testing Checklist
- [ ] All screens are navigable with TalkBack
- [ ] All interactive elements have proper descriptions
- [ ] Form validation provides accessible feedback
- [ ] Custom actions work with accessibility tools
- [ ] Focus order is logical and complete
- [ ] Dynamic updates are announced
- [ ] No decorative elements are announced
- [ ] App works with Switch Access
- [ ] App works with high contrast mode
- [ ] App works with large font sizes

### Submission
Submit your complete app with:
1. Source code
2. Screenshots of the app
3. Video demonstration with TalkBack enabled
4. Documentation of accessibility features implemented
5. Testing report with results from each checklist item
