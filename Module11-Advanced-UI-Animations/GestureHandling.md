# Gesture Detection & Touch Handling

## Overview
Gesture detection and touch handling are essential for creating interactive Android applications. Android provides powerful tools like `GestureDetector` and custom touch event handling to create responsive and intuitive user experiences.

## Key Concepts

### 1. Touch Event Flow
1. **ACTION_DOWN**: Finger touches the screen
2. **ACTION_MOVE**: Finger moves across the screen
3. **ACTION_UP**: Finger lifts from the screen
4. **ACTION_CANCEL**: Touch event is cancelled

### 2. GestureDetector
`GestureDetector` is a helper class that detects common gestures like taps, double-taps, long presses, and scrolls.

## Basic Gesture Detection

### 1. Simple GestureDetector Setup
```kotlin
class GestureExampleActivity : AppCompatActivity() {
    
    private lateinit var gestureDetector: GestureDetector
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        
        setupGestureDetector()
    }
    
    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                Toast.makeText(this@GestureExampleActivity, "Single Tap", Toast.LENGTH_SHORT).show()
                return true
            }
            
            override fun onDoubleTap(e: MotionEvent): Boolean {
                Toast.makeText(this@GestureExampleActivity, "Double Tap", Toast.LENGTH_SHORT).show()
                return true
            }
            
            override fun onLongPress(e: MotionEvent) {
                Toast.makeText(this@GestureExampleActivity, "Long Press", Toast.LENGTH_SHORT).show()
            }
            
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                // Handle scroll gesture
                return true
            }
            
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                // Handle fling gesture
                return true
            }
        })
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}
```

### 2. GestureDetector on Views
```kotlin
class GestureView : View {
    
    private val gestureDetector: GestureDetector
    
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            // Handle single tap
            return true
        }
        
        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Handle double tap
            return true
        }
        
        override fun onLongPress(e: MotionEvent) {
            // Handle long press
        }
    }
}
```

## Advanced Gesture Detection

### 1. Custom Gesture Detection
```kotlin
class CustomGestureDetector {
    
    private var startX = 0f
    private var startY = 0f
    private var isDragging = false
    private val minDragDistance = 50f
    
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                isDragging = false
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - startX
                val deltaY = event.y - startY
                val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
                
                if (distance > minDragDistance && !isDragging) {
                    isDragging = true
                    onDragStart(startX, startY)
                }
                
                if (isDragging) {
                    onDrag(event.x, event.y, deltaX, deltaY)
                }
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    onDragEnd(event.x, event.y)
                } else {
                    onClick(event.x, event.y)
                }
                isDragging = false
                return true
            }
        }
        return false
    }
    
    private fun onDragStart(x: Float, y: Float) {
        // Handle drag start
    }
    
    private fun onDrag(x: Float, y: Float, deltaX: Float, deltaY: Float) {
        // Handle drag
    }
    
    private fun onDragEnd(x: Float, y: Float) {
        // Handle drag end
    }
    
    private fun onClick(x: Float, y: Float) {
        // Handle click
    }
}
```

### 2. Multi-Touch Gestures
```kotlin
class MultiTouchGestureDetector {
    
    private var pointerCount = 0
    private var initialDistance = 0f
    private var initialAngle = 0f
    
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                pointerCount++
                if (pointerCount == 2) {
                    initialDistance = getDistance(event)
                    initialAngle = getAngle(event)
                }
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                if (pointerCount == 2) {
                    val currentDistance = getDistance(event)
                    val currentAngle = getAngle(event)
                    
                    val scale = currentDistance / initialDistance
                    val rotation = currentAngle - initialAngle
                    
                    onPinchZoom(scale, rotation)
                }
                return true
            }
            
            MotionEvent.ACTION_POINTER_UP -> {
                pointerCount--
                return true
            }
        }
        return false
    }
    
    private fun getDistance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }
    
    private fun getAngle(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return atan2(y, x) * 180 / PI.toFloat()
    }
    
    private fun onPinchZoom(scale: Float, rotation: Float) {
        // Handle pinch zoom and rotation
    }
}
```

## Velocity Tracking

### 1. VelocityTracker
```kotlin
class VelocityGestureDetector {
    
    private var velocityTracker: VelocityTracker? = null
    
    fun onTouchEvent(event: MotionEvent): Boolean {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker?.addMovement(event)
        
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                velocityTracker?.computeCurrentVelocity(1000) // pixels per second
                val velocityX = velocityTracker?.xVelocity ?: 0f
                val velocityY = velocityTracker?.yVelocity ?: 0f
                
                onFling(velocityX, velocityY)
                
                velocityTracker?.recycle()
                velocityTracker = null
            }
        }
        return true
    }
    
    private fun onFling(velocityX: Float, velocityY: Float) {
        val minVelocity = 1000f
        if (abs(velocityX) > minVelocity || abs(velocityY) > minVelocity) {
            // Handle fling gesture
        }
    }
}
```

## Common Gesture Patterns

### 1. Swipe Gestures
```kotlin
class SwipeGestureDetector {
    
    private var startX = 0f
    private var startY = 0f
    private val minSwipeDistance = 100f
    private val maxSwipeTime = 300L
    private var startTime = 0L
    
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                startTime = System.currentTimeMillis()
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                val deltaX = event.x - startX
                val deltaY = event.y - startY
                val deltaTime = System.currentTimeMillis() - startTime
                
                if (deltaTime < maxSwipeTime) {
                    when {
                        abs(deltaX) > abs(deltaY) && abs(deltaX) > minSwipeDistance -> {
                            if (deltaX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                        }
                        abs(deltaY) > abs(deltaX) && abs(deltaY) > minSwipeDistance -> {
                            if (deltaY > 0) {
                                onSwipeDown()
                            } else {
                                onSwipeUp()
                            }
                        }
                    }
                }
                return true
            }
        }
        return false
    }
    
    private fun onSwipeLeft() {
        // Handle left swipe
    }
    
    private fun onSwipeRight() {
        // Handle right swipe
    }
    
    private fun onSwipeUp() {
        // Handle up swipe
    }
    
    private fun onSwipeDown() {
        // Handle down swipe
    }
}
```

### 2. Pinch to Zoom
```kotlin
class PinchZoomGestureDetector {
    
    private var initialDistance = 0f
    private var initialScale = 1f
    private var currentScale = 1f
    
    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    initialDistance = getDistance(event)
                    initialScale = currentScale
                }
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 2) {
                    val currentDistance = getDistance(event)
                    val scale = currentDistance / initialDistance
                    currentScale = initialScale * scale
                    
                    onScaleChanged(currentScale)
                }
                return true
            }
        }
        return false
    }
    
    private fun getDistance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }
    
    private fun onScaleChanged(scale: Float) {
        // Apply scale transformation
    }
}
```

## Gesture with Animation

### 1. Animated Gesture Response
```kotlin
class AnimatedGestureView : View {
    
    private val gestureDetector: GestureDetector
    private var scale = 1f
    private var rotation = 0f
    
    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                animateDoubleTap()
                return true
            }
            
            override fun onLongPress(e: MotionEvent) {
                animateLongPress()
            }
        })
    }
    
    private fun animateDoubleTap() {
        val scaleAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 1.2f, 1f)
        val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 1.2f, 1f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleAnimator, scaleYAnimator)
        animatorSet.duration = 300
        animatorSet.start()
    }
    
    private fun animateLongPress() {
        val rotationAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 10f, -10f, 0f)
        rotationAnimator.duration = 500
        rotationAnimator.start()
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}
```

## Performance Optimization

### 1. Efficient Touch Handling
```kotlin
class OptimizedGestureView : View {
    
    private var isGestureEnabled = true
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isGestureEnabled) return false
        
        // Use touch slop to avoid accidental touches
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Start gesture detection
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - startX
                val deltaY = event.y - startY
                
                if (sqrt(deltaX * deltaX + deltaY * deltaY) > touchSlop) {
                    // Significant movement detected
                }
            }
        }
        return super.onTouchEvent(event)
    }
}
```

### 2. Memory Management
```kotlin
override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    // Clean up resources
    velocityTracker?.recycle()
    velocityTracker = null
}
```

## Accessibility Considerations

### 1. Content Descriptions
```kotlin
class AccessibleGestureView : View {
    
    init {
        contentDescription = "Double tap to like, long press for more options"
        isClickable = true
        isFocusable = true
    }
    
    override fun performClick(): Boolean {
        // Handle accessibility click
        return super.performClick()
    }
}
```

### 2. Haptic Feedback
```kotlin
private fun provideHapticFeedback() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    } else {
        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
    }
}
```

## Testing Gestures

### 1. Unit Testing
```kotlin
@Test
fun testSwipeGesture() {
    val gestureDetector = SwipeGestureDetector()
    
    // Simulate touch events
    val downEvent = MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
    val upEvent = MotionEvent.obtain(0, 100, MotionEvent.ACTION_UP, 200f, 0f, 0)
    
    gestureDetector.onTouchEvent(downEvent)
    gestureDetector.onTouchEvent(upEvent)
    
    // Verify gesture was detected
}
```

### 2. UI Testing
```kotlin
@Test
fun testDoubleTapGesture() {
    onView(withId(R.id.gesture_view))
        .perform(doubleClick())
        .check(matches(isDisplayed()))
}
```

## Summary
Gesture detection and touch handling are crucial for creating interactive Android applications. Key points:
- Use `GestureDetector` for common gestures
- Implement custom gesture detection for specific needs
- Track velocity for fling gestures
- Support multi-touch for complex interactions
- Provide haptic feedback for better UX
- Consider accessibility in gesture design
- Test gestures thoroughly

## Next Steps
- Explore advanced gesture patterns
- Learn about gesture-based navigation
- Study Material Design gesture guidelines
- Practice with complex multi-touch interactions
