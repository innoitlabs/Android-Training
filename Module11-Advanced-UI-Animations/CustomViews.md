# Custom Views & Drawing

## Overview
Custom views allow you to create unique UI components by extending Android's View class and implementing custom drawing logic. This enables you to create specialized animations, visual effects, and interactive elements.

## Key Concepts

### 1. View Lifecycle
- **onMeasure()**: Determine view dimensions
- **onLayout()**: Position child views
- **onDraw()**: Draw the view content
- **invalidate()**: Trigger redraw

### 2. Canvas Drawing
The Canvas class provides methods for drawing shapes, text, and images.

## Basic Custom View Structure

### 1. Simple Custom View
```kotlin
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f
        
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}
```

### 2. Custom Attributes
```xml
<!-- res/values/attrs.xml -->
<resources>
    <declare-styleable name="CircleView">
        <attr name="circleColor" format="color" />
        <attr name="circleRadius" format="dimension" />
    </declare-styleable>
</resources>
```

```kotlin
class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circleColor = Color.RED
    private var circleRadius = 100f
    
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        circleColor = typedArray.getColor(R.styleable.CircleView_circleColor, Color.RED)
        circleRadius = typedArray.getDimension(R.styleable.CircleView_circleRadius, 100f)
        typedArray.recycle()
        
        paint.color = circleColor
        paint.style = Paint.Style.FILL
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        canvas.drawCircle(centerX, centerY, circleRadius, paint)
    }
}
```

## Advanced Drawing Techniques

### 1. Path Drawing
```kotlin
class PathView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    
    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        path.reset()
        path.moveTo(100f, 100f)
        path.quadTo(200f, 50f, 300f, 100f)
        path.quadTo(400f, 150f, 500f, 100f)
        
        canvas.drawPath(path, paint)
    }
}
```

### 2. Gradient Drawing
```kotlin
class GradientView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var gradient: LinearGradient
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        
        gradient = LinearGradient(
            0f, 0f, w.toFloat(), h.toFloat(),
            Color.RED, Color.BLUE,
            Shader.TileMode.CLAMP
        )
        paint.shader = gradient
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}
```

## Animated Custom Views

### 1. ValueAnimator Integration
```kotlin
class AnimatedCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius = 0f
    private var animator: ValueAnimator? = null
    
    init {
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
    }
    
    fun animateRadius(targetRadius: Float) {
        animator?.cancel()
        
        animator = ValueAnimator.ofFloat(radius, targetRadius).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            
            addUpdateListener { animation ->
                radius = animation.animatedValue as Float
                invalidate()
            }
        }
        animator?.start()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}
```

### 2. Custom Property Animation
```kotlin
class ProgressCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0f
    
    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
    }
    
    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 3f
        
        // Draw background circle
        paint.color = Color.LTGRAY
        canvas.drawCircle(centerX, centerY, radius, paint)
        
        // Draw progress arc
        paint.color = Color.BLUE
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            progress * 360f,
            false,
            paint
        )
    }
}
```

## Touch Handling in Custom Views

### 1. Basic Touch Handling
```kotlin
class TouchableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var touchX = 0f
    private var touchY = 0f
    private var isTouching = false
    
    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                isTouching = true
                invalidate()
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                touchX = event.x
                touchY = event.y
                invalidate()
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                isTouching = false
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        if (isTouching) {
            canvas.drawCircle(touchX, touchY, 50f, paint)
        }
    }
}
```

### 2. Gesture Detection
```kotlin
class GestureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val gestureDetector: GestureDetector
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var scale = 1f
    
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                animateScale()
                return true
            }
        })
        
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
    }
    
    private fun animateScale() {
        val animator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.5f, 1f)
        val animatorY = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.5f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animator, animatorY)
        animatorSet.duration = 300
        animatorSet.start()
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 4f
        
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}
```

## Performance Optimization

### 1. Efficient Drawing
```kotlin
class OptimizedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var isPathDirty = true
    
    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }
    
    private fun updatePath() {
        if (!isPathDirty) return
        
        path.reset()
        // Build complex path here
        path.addCircle(width / 2f, height / 2f, 100f, Path.Direction.CW)
        
        isPathDirty = false
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        isPathDirty = true
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        updatePath()
        canvas.drawPath(path, paint)
    }
}
```

### 2. Hardware Acceleration
```xml
<!-- Enable in theme or activity -->
<item name="android:hardwareAccelerated">true</item>
```

## Common Use Cases

### 1. Progress Indicators
```kotlin
class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0f
    private var maxProgress = 100f
    
    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 3f
        
        // Background circle
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 10f
        canvas.drawCircle(centerX, centerY, radius, paint)
        
        // Progress arc
        paint.color = Color.BLUE
        val sweepAngle = (progress / maxProgress) * 360f
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            sweepAngle,
            false,
            paint
        )
    }
}
```

### 2. Custom Buttons
```kotlin
class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var isPressed = false
    
    init {
        paint.color = Color.BLUE
        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
        
        isClickable = true
        isFocusable = true
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        val cornerRadius = 20f
        
        paint.color = if (isPressed) Color.DARKBLUE else Color.BLUE
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        
        // Draw text
        val textX = width / 2f
        val textY = height / 2f + textPaint.textSize / 3
        canvas.drawText("Button", textX, textY, textPaint)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressed = true
                invalidate()
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                isPressed = false
                invalidate()
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
```

## Debugging Tips

### 1. Enable Debug Drawing
```kotlin
override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    
    if (BuildConfig.DEBUG) {
        // Draw debug information
        val debugPaint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), debugPaint)
    }
}
```

### 2. Performance Monitoring
```kotlin
override fun onDraw(canvas: Canvas) {
    val startTime = System.nanoTime()
    
    super.onDraw(canvas)
    // Your drawing code here
    
    val endTime = System.nanoTime()
    val duration = (endTime - startTime) / 1_000_000 // Convert to milliseconds
    
    if (duration > 16) { // 60fps = 16ms per frame
        Log.w("CustomView", "Slow drawing: ${duration}ms")
    }
}
```

## Summary
Custom views provide powerful capabilities for creating unique UI components. Key points:
- Extend View class and override onDraw() for custom drawing
- Use Canvas and Paint for rendering
- Implement touch handling for interactivity
- Add animations with ValueAnimator
- Optimize performance with efficient drawing
- Consider hardware acceleration
- Test on different screen sizes and densities

## Next Steps
- Explore advanced drawing techniques
- Learn about custom view groups
- Study Material Design components
- Practice with complex animations
