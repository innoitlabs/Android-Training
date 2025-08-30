# Android Custom Views and ViewGroups - Step-by-Step Learning Guide

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand why and when to use Custom Views in Android
- Create a Custom View by extending View
- Override important lifecycle methods (onMeasure, onDraw)
- Handle custom attributes with attrs.xml
- Create a Custom ViewGroup (e.g., custom layout manager)
- Integrate custom views into XML layouts
- Apply best practices for reusability and performance

## Prerequisites

- Basic Android development knowledge
- Familiarity with Kotlin
- Understanding of XML layouts
- Android Studio installed

## Lesson Structure

### Part 1: Understanding Custom Views

#### What is a Custom View?
A Custom View is a class that extends the Android `View` class to create a unique UI component that doesn't exist in the standard Android framework.

#### Why Create Custom Views?
1. **Unique UI Requirements**: When standard views don't meet your design needs
2. **Performance**: Optimize rendering for specific use cases
3. **Reusability**: Create components that can be used across multiple screens
4. **Complex Interactions**: Handle custom touch events and gestures

#### Key Methods to Override
- `onDraw(Canvas)`: Called when the view needs to be rendered
- `onMeasure(int, int)`: Determines the size requirements
- `onTouchEvent(MotionEvent)`: Handle touch interactions
- `onSizeChanged(int, int, int, int)`: Called when view size changes

### Part 2: Creating Your First Custom View

#### Step 1: Basic CircleView Structure
```kotlin
class CircleView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : View(context, attrs) {
    
    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Drawing code will go here
    }
}
```

#### Step 2: Implement Drawing Logic
```kotlin
override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    
    val centerX = width / 2f
    val centerY = height / 2f
    val radius = (width.coerceAtMost(height) / 2).toFloat()
    
    canvas?.drawCircle(centerX, centerY, radius, paint)
}
```

#### Step 3: Add to XML Layout
```xml
<com.example.customviews.CircleView
    android:layout_width="100dp"
    android:layout_height="100dp"
    android:layout_margin="16dp"/>
```

### Part 3: Adding Custom Attributes

#### Step 1: Define Attributes
Create `res/values/attrs.xml`:
```xml
<resources>
    <declare-styleable name="CircleView">
        <attr name="circleColor" format="color"/>
        <attr name="strokeWidth" format="dimension"/>
        <attr name="fillStyle" format="enum">
            <enum name="fill" value="0"/>
            <enum name="stroke" value="1"/>
        </attr>
    </declare-styleable>
</resources>
```

#### Step 2: Read Attributes in Constructor
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
            
            val fillStyle = getInteger(R.styleable.CircleView_fillStyle, 0)
            paint.style = if (fillStyle == 0) Paint.Style.FILL else Paint.Style.STROKE
        } finally {
            recycle()
        }
    }
}
```

#### Step 3: Use Custom Attributes
```xml
<com.example.customviews.CircleView
    android:layout_width="120dp"
    android:layout_height="120dp"
    app:circleColor="@android:color/holo_blue_bright"
    app:strokeWidth="8dp"
    app:fillStyle="stroke"/>
```

### Part 4: Creating Custom ViewGroups

#### Understanding ViewGroups
A ViewGroup is a container that can hold other views and manage their layout. Examples include LinearLayout, RelativeLayout, and ConstraintLayout.

#### TwoColumnLayout Example
```kotlin
class TwoColumnLayout @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure all children
        var maxHeight = 0
        var totalWidth = 0
        
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            totalWidth = maxOf(totalWidth, child.measuredWidth)
            maxHeight += child.measuredHeight
        }
        
        // Set measured dimensions
        val desiredWidth = totalWidth * 2
        setMeasuredDimension(
            resolveSize(desiredWidth, widthMeasureSpec),
            resolveSize(maxHeight / 2, heightMeasureSpec)
        )
    }
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val columnWidth = (right - left) / 2
        var x = 0
        var y = 0
        var column = 0
        
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childHeight = child.measuredHeight
            val childWidth = columnWidth
            
            child.layout(x, y, x + childWidth, y + childHeight)
            
            column++
            if (column % 2 == 0) {
                x = 0
                y += childHeight
            } else {
                x += columnWidth
            }
        }
    }
}
```

### Part 5: Advanced Custom View - Progress Indicator

#### CustomProgressView Implementation
```kotlin
class CustomProgressView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : View(context, attrs) {
    
    private val backgroundPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }
    
    private val progressPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }
    
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    
    private var progress = 0f
    private var maxProgress = 100f
    
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomProgressView,
            0, 0
        ).apply {
            try {
                progressPaint.color = getColor(R.styleable.CustomProgressView_progressColor, Color.BLUE)
                backgroundPaint.color = getColor(R.styleable.CustomProgressView_backgroundColor, Color.LTGRAY)
                progress = getFloat(R.styleable.CustomProgressView_progress, 0f)
                maxProgress = getFloat(R.styleable.CustomProgressView_maxProgress, 100f)
            } finally {
                recycle()
            }
        }
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2 - 30).toFloat()
        
        // Draw background circle
        canvas?.drawCircle(centerX, centerY, radius, backgroundPaint)
        
        // Draw progress arc
        val sweepAngle = (progress / maxProgress) * 360f
        canvas?.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f, // Start from top
            sweepAngle,
            false,
            progressPaint
        )
        
        // Draw progress text
        val progressText = "${progress.toInt()}%"
        canvas?.drawText(progressText, centerX, centerY + textPaint.textSize / 3, textPaint)
    }
    
    fun setProgress(newProgress: Float) {
        progress = newProgress.coerceIn(0f, maxProgress)
        invalidate()
    }
    
    fun animateProgress(targetProgress: Float, duration: Long = 1000) {
        val animator = ValueAnimator.ofFloat(progress, targetProgress)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            setProgress(animation.animatedValue as Float)
        }
        animator.start()
    }
}
```

### Part 6: Best Practices and Performance

#### Performance Optimization
1. **Reuse Paint Objects**: Create Paint objects once and reuse them
2. **Efficient onMeasure()**: Avoid heavy computations in measurement
3. **Minimize Overdraw**: Only draw what's necessary
4. **Use Hardware Acceleration**: Enable when possible

#### Code Organization
1. **Separate Concerns**: Keep drawing logic separate from measurement
2. **Use Constants**: Define magic numbers as constants
3. **Documentation**: Add clear comments for complex logic
4. **Error Handling**: Handle edge cases gracefully

#### Testing Strategies
1. **Multiple Screen Sizes**: Test on different devices and orientations
2. **Performance Testing**: Monitor frame rates and memory usage
3. **Edge Cases**: Test with extreme values and empty states
4. **Accessibility**: Ensure custom views are accessible

### Part 7: Hands-on Exercises

#### Exercise 1: Dynamic CircleView
Modify CircleView to support:
- Color changes via setter methods
- Size animations
- Click listeners

#### Exercise 2: Enhanced TwoColumnLayout
Add features to TwoColumnLayout:
- Configurable column count
- Spacing between columns and rows
- Animation support for layout changes

#### Exercise 3: Interactive Progress View
Enhance CustomProgressView with:
- Touch interaction to set progress
- Different progress styles (circular, linear)
- Custom animations

### Part 8: Integration and Testing

#### Adding to MainActivity
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Find custom views
        val circleView = findViewById<CircleView>(R.id.circleView)
        val progressView = findViewById<CustomProgressView>(R.id.progressView)
        
        // Interact with custom views
        circleView.setOnClickListener {
            // Change color randomly
            val colors = arrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW)
            circleView.setColor(colors.random())
        }
        
        // Animate progress
        progressView.animateProgress(75f, 2000)
    }
}
```

#### Testing Checklist
- [ ] All custom views render correctly
- [ ] Custom attributes work as expected
- [ ] Performance is smooth (60fps)
- [ ] Works on different screen sizes
- [ ] Handles edge cases gracefully
- [ ] No memory leaks
- [ ] Accessibility features work

## Conclusion

Custom Views and ViewGroups are powerful tools for creating unique and performant Android UI components. By following this guide, you've learned:

1. **How to create custom views** by extending the View class
2. **How to implement custom attributes** for configuration
3. **How to create custom ViewGroups** for layout management
4. **Best practices** for performance and maintainability
5. **Testing strategies** to ensure quality

Remember to always consider whether a custom view is necessary - sometimes standard views with custom styling are sufficient. Use custom views when you need unique functionality or performance optimization.

## Next Steps

1. **Practice**: Build more complex custom views
2. **Explore**: Learn about advanced topics like gestures and animations
3. **Contribute**: Share your custom views with the community
4. **Optimize**: Continuously improve performance and usability

## Resources

- [Android Developer Documentation](https://developer.android.com/guide/topics/ui/custom-components)
- [Android Canvas and Drawables](https://developer.android.com/guide/topics/graphics)
- [Android Performance Patterns](https://www.youtube.com/playlist?list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE)
