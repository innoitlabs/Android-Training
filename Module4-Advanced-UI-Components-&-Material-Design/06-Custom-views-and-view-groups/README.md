# Android Custom Views and Custom ViewGroups - Complete Learning Guide

## Table of Contents
1. [Introduction](#introduction)
2. [Creating a Custom View](#creating-a-custom-view)
3. [Adding Custom Attributes](#adding-custom-attributes)
4. [Creating a Custom ViewGroup](#creating-a-custom-viewgroup)
5. [Best Practices](#best-practices)
6. [Hands-on Lab / Mini Project](#hands-on-lab--mini-project)
7. [Exercises](#exercises)
8. [Summary](#summary)

## Introduction

### What are Custom Views?
- **Custom View**: A view built by extending the View class when standard components don't meet requirements
- **Custom ViewGroup**: A container (like LinearLayout, RelativeLayout) that manages the layout of its child views

### When to Use Custom Views?
- **Unique UI components**: Progress indicators, rating bars, custom charts
- **Custom layouts**: Advanced designs that require specific arrangement
- **Reusable widgets**: Components used across multiple screens
- **Performance optimization**: When standard views don't meet performance requirements

### Key Concepts
- **onDraw()**: Called when the view needs to be rendered
- **onMeasure()**: Determines the size requirements of the view
- **onLayout()**: Positions child views within a ViewGroup
- **Custom Attributes**: XML attributes for configuration

## Creating a Custom View

### Basic Structure
```kotlin
class CustomView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : View(context, attrs) {
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Drawing code here
    }
}
```

### Example: CircleView
The `CircleView` demonstrates:
- Extending the View class
- Using Paint and Canvas for drawing
- Basic shape rendering

**Key Methods:**
- `onDraw()`: Renders the circle
- `Paint`: Defines drawing properties (color, style, etc.)
- `Canvas`: Provides drawing surface

## Adding Custom Attributes

### Step 1: Define Attributes in attrs.xml
```xml
<resources>
    <declare-styleable name="CircleView">
        <attr name="circleColor" format="color"/>
        <attr name="strokeWidth" format="dimension"/>
    </declare-styleable>
</resources>
```

### Step 2: Read Attributes in Constructor
```kotlin
init {
    context.theme.obtainStyledAttributes(
        attrs,
        R.styleable.CircleView,
        0, 0
    ).apply {
        try {
            paint.color = getColor(R.styleable.CircleView_circleColor, Color.RED)
            paint.strokeWidth = getDimension(R.styleable.CircleView_strokeWidth, 5f)
        } finally {
            recycle()
        }
    }
}
```

### Step 3: Use in XML
```xml
<com.example.customviews.CircleView
    android:layout_width="120dp"
    android:layout_height="120dp"
    app:circleColor="@android:color/holo_blue_bright"
    app:strokeWidth="8dp"/>
```

## Creating a Custom ViewGroup

### Basic Structure
```kotlin
class CustomViewGroup @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure children and determine own size
    }
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // Position child views
    }
}
```

### Example: TwoColumnLayout
The `TwoColumnLayout` demonstrates:
- Arranging children in 2 columns
- Proper measurement of children
- Layout positioning logic

**Key Methods:**
- `onMeasure()`: Measures all children and determines container size
- `onLayout()`: Positions each child in the 2-column arrangement
- `measureChild()`: Measures individual child views

## Best Practices

### 1. Performance Optimization
- **Reuse Paint objects**: Don't create new Paint in onDraw()
- **Efficient onMeasure()**: Avoid heavy computations
- **Use hardware acceleration**: Enable when possible
- **Minimize overdraw**: Avoid unnecessary drawing operations

### 2. Custom Attributes
- **Always support XML attributes**: Makes views reusable
- **Provide sensible defaults**: Handle missing attributes gracefully
- **Use appropriate formats**: color, dimension, string, etc.

### 3. Testing
- **Test across screen sizes**: Different densities and orientations
- **Test with different content**: Various child views and sizes
- **Performance testing**: Ensure smooth scrolling and animations

### 4. Code Organization
- **Separate concerns**: Drawing logic separate from measurement
- **Use constants**: For magic numbers and default values
- **Documentation**: Clear comments for complex logic

## Hands-on Lab / Mini Project

### Custom Progress Indicator
Create a circular progress indicator with:
- Custom progress color attribute
- Animated progress updates
- Text display in center

**Features:**
- Smooth animations using `invalidate()`
- Custom attributes for colors and sizes
- Progress percentage display

## Exercises

### Easy Level
1. **Dynamic Color Change**: Modify CircleView to change color programmatically
2. **Size Animation**: Add smooth size transitions
3. **Border Support**: Add stroke/border to CircleView

### Intermediate Level
1. **Text Label**: Add text display inside CircleView (e.g., "50%")
2. **Gradient Support**: Implement gradient fills
3. **Touch Events**: Add click/touch handling

### Advanced Level
1. **Flexible Layout**: Extend TwoColumnLayout to support variable column counts
2. **Spacing Attributes**: Add custom spacing between columns and rows
3. **Animation Support**: Add smooth layout transitions
4. **Nested Scrolling**: Implement nested scrolling behavior

## Summary

### Key Takeaways
- **Custom Views**: Extend View, override onDraw() for visuals
- **Custom ViewGroups**: Extend ViewGroup, override onMeasure() + onLayout() for layouts
- **Custom Attributes**: Make views configurable and reusable
- **Performance**: Always consider performance implications
- **Testing**: Test across different scenarios and devices

### When to Use
- **Use Custom Views**: When you need unique UI components or performance optimization
- **Use Custom ViewGroups**: When you need custom layout arrangements
- **Consider Alternatives**: ViewBinding, Compose, or standard views for simpler cases

### Next Steps
1. Practice with the provided examples
2. Experiment with different drawing techniques
3. Build your own custom components
4. Explore advanced topics like animations and gestures

---

## Project Structure

```
CustomViews/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/customviews/
│           │   ├── MainActivity.kt
│           │   ├── CircleView.kt
│           │   ├── TwoColumnLayout.kt
│           │   └── CustomProgressView.kt
│           └── res/
│               ├── layout/
│               │   └── activity_main.xml
│               └── values/
│                   └── attrs.xml
└── README.md (this file)
```

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on emulator or device
5. Verify all custom views display correctly

## Troubleshooting

### Common Issues
- **Build errors**: Check Gradle sync and dependencies
- **Layout issues**: Verify custom attributes are properly defined
- **Performance**: Monitor for overdraw and inefficient drawing
- **Compatibility**: Test on different API levels

### Debug Tips
- Use Android Studio's Layout Inspector
- Enable "Show overdraw" in Developer Options
- Use Systrace for performance analysis
- Test on physical devices when possible
