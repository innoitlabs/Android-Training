# Android Accessibility Implementation Guide

## Quick Reference

### Essential Attributes
```xml
<!-- Content Description -->
android:contentDescription="Description for screen readers"

<!-- Focus Control -->
android:focusable="true"
android:nextFocusDown="@id/nextView"

<!-- Accessibility Importance -->
android:importantForAccessibility="yes|no|auto"

<!-- Label for -->
android:labelFor="@id/targetView"
```

### Common Accessibility Patterns

#### 1. Image with Description
```xml
<ImageView
    android:id="@+id/icon"
    android:layout_width="24dp"
    android:layout_height="24dp"
    android:src="@drawable/ic_info"
    android:contentDescription="Information icon" />
```

#### 2. Button with Action Description
```xml
<Button
    android:id="@+id/deleteButton"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Delete"
    android:contentDescription="Delete selected item" />
```

#### 3. EditText with Label
```xml
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
```

#### 4. Decorative Element
```xml
<View
    android:layout_width="match_parent"
    android:layout_height="1dp"
    android:background="@color/divider"
    android:importantForAccessibility="no" />
```

## Kotlin Implementation Examples

### 1. Programmatic Content Description
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        profileImage.contentDescription = "User profile picture"
    }
}
```

### 2. Custom Accessibility Actions
```kotlin
class AccessibleButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    
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
                
                // Add custom action
                info.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        R.id.accessibilityActionLongPress, "Long press to show options"
                    )
                )
                
                // Customize announcement
                info.text = "Custom button: ${text}"
            }
        }
    }
}
```

### 3. Accessibility Event Handling
```kotlin
class AccessibilityHelper {
    fun setupAccessibilityEvents(view: View) {
        view.setOnAccessibilityEventListener { event ->
            when (event.eventType) {
                AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                    // Handle click event
                    Log.d("Accessibility", "View clicked via accessibility")
                    true
                }
                AccessibilityEvent.TYPE_VIEW_LONG_CLICKED -> {
                    // Handle long click event
                    Log.d("Accessibility", "View long clicked via accessibility")
                    true
                }
                else -> false
            }
        }
    }
}
```

### 4. Live Region Updates
```kotlin
class LiveRegionExample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val statusText = findViewById<TextView>(R.id.statusText)
        
        // Set as live region
        statusText.accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
        
        // Update content (will be announced by TalkBack)
        statusText.text = "Status updated successfully"
    }
}
```

## Testing Checklist

### Manual Testing with TalkBack
1. **Enable TalkBack**
   - Settings > Accessibility > TalkBack > Turn on
   - Learn basic gestures (swipe right/left to navigate)

2. **Test Navigation**
   - [ ] All interactive elements are reachable
   - [ ] Focus order is logical
   - [ ] No focus traps

3. **Test Announcements**
   - [ ] Images have descriptive content descriptions
   - [ ] Buttons announce their purpose
   - [ ] Form fields have proper labels
   - [ ] Decorative elements are not announced

4. **Test Actions**
   - [ ] Custom accessibility actions work
   - [ ] Double-tap gestures work correctly
   - [ ] Long-press actions are available

### Automated Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class AccessibilityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testContentDescriptions() {
        onView(withId(R.id.profileImage))
            .check(matches(withContentDescription("User profile picture")))
    }
    
    @Test
    fun testFocusableElements() {
        onView(withId(R.id.saveButton))
            .check(matches(isFocusable()))
    }
}
```

## Common Accessibility Issues and Solutions

### Issue 1: Missing Content Descriptions
**Problem**: ImageViews without content descriptions
**Solution**: Add descriptive contentDescription attributes

### Issue 2: Poor Focus Order
**Problem**: Illogical tab order
**Solution**: Use nextFocus attributes to control order

### Issue 3: Decorative Elements Announced
**Problem**: Background images or dividers being read
**Solution**: Set importantForAccessibility="no"

### Issue 4: Custom Views Not Accessible
**Problem**: Custom views don't work with TalkBack
**Solution**: Implement AccessibilityDelegate

### Issue 5: Dynamic Content Not Announced
**Problem**: Updates not announced to screen readers
**Solution**: Use accessibilityLiveRegion

## Material Design Accessibility Guidelines

### Color and Contrast
- Minimum contrast ratio: 4.5:1 for normal text
- Minimum contrast ratio: 3:1 for large text
- Don't rely solely on color to convey information

### Touch Targets
- Minimum size: 48dp x 48dp
- Adequate spacing between interactive elements
- Clear visual feedback for touch states

### Typography
- Support large font sizes
- Use readable font families
- Maintain proper line spacing

### Motion and Animation
- Respect "Reduce motion" accessibility setting
- Provide option to disable animations
- Ensure animations don't cause motion sickness

## Resources

### Official Documentation
- [Android Accessibility Developer Guide](https://developer.android.com/guide/topics/ui/accessibility)
- [Material Design Accessibility](https://material.io/design/usability/accessibility.html)
- [WCAG Guidelines](https://www.w3.org/WAI/WCAG21/quickref/)

### Tools
- [Accessibility Scanner](https://play.google.com/store/apps/details?id=com.google.android.apps.accessibility.auditor)
- [Lint Accessibility Checks](https://developer.android.com/studio/write/lint)
- [TalkBack](https://support.google.com/accessibility/android/answer/6283677)

### Testing
- Test with TalkBack enabled
- Test with Switch Access
- Test with high contrast mode
- Test with large font sizes
- Test with reduced motion
