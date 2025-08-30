# Android Custom Views - Exercises and Projects

## Exercise Categories

### Beginner Level (Easy)
### Intermediate Level (Medium)
### Advanced Level (Hard)

---

## Beginner Level Exercises

### Exercise 1: Dynamic CircleView Enhancement

**Objective**: Modify the basic CircleView to support dynamic color changes and size animations.

**Requirements**:
- Add a `setColor(int color)` method
- Add a `setSize(int size)` method with animation
- Implement click listener to change color randomly
- Add a border/stroke option

**Implementation Steps**:
1. Add color and size properties to CircleView
2. Create setter methods with `invalidate()` calls
3. Use `ValueAnimator` for smooth size transitions
4. Add stroke paint for border rendering

**Expected Output**:
- Circle changes color when clicked
- Smooth size animations
- Optional border display

### Exercise 2: Simple Progress Bar

**Objective**: Create a horizontal progress bar with custom styling.

**Requirements**:
- Custom progress color attribute
- Background color attribute
- Corner radius attribute
- Animated progress updates

**Implementation Steps**:
1. Create `SimpleProgressBar` class extending View
2. Define custom attributes in attrs.xml
3. Implement `onDraw()` for horizontal progress bar
4. Add `setProgress()` method with animation

**Expected Output**:
- Horizontal progress bar with rounded corners
- Smooth progress animations
- Customizable colors

### Exercise 3: Custom Rating Bar

**Objective**: Create a star-based rating component.

**Requirements**:
- Configurable number of stars
- Custom star color and size
- Touch interaction to set rating
- Half-star support (optional)

**Implementation Steps**:
1. Create `CustomRatingBar` class
2. Draw star shapes using Path
3. Handle touch events to set rating
4. Implement rating change listener

**Expected Output**:
- Interactive star rating component
- Visual feedback on touch
- Rating change callbacks

---

## Intermediate Level Exercises

### Exercise 4: Enhanced TwoColumnLayout

**Objective**: Extend the basic TwoColumnLayout with advanced features.

**Requirements**:
- Configurable column count (not just 2)
- Spacing between columns and rows
- Animation support for layout changes
- Support for different child sizes

**Implementation Steps**:
1. Add column count attribute
2. Add spacing attributes
3. Modify `onMeasure()` and `onLayout()` for variable columns
4. Implement layout change animations

**Expected Output**:
- Flexible multi-column layout
- Smooth layout transitions
- Proper spacing between elements

### Exercise 5: Circular Progress Indicator

**Objective**: Create an advanced circular progress indicator with multiple features.

**Requirements**:
- Multiple progress styles (solid, dashed, gradient)
- Custom start angle and sweep direction
- Progress text display
- Multiple color schemes

**Implementation Steps**:
1. Create `CircularProgressView` class
2. Implement different drawing styles
3. Add text rendering for progress percentage
4. Support gradient fills

**Expected Output**:
- Versatile circular progress indicator
- Multiple visual styles
- Smooth animations

### Exercise 6: Custom SeekBar

**Objective**: Create a custom seek bar with unique styling.

**Requirements**:
- Custom thumb design
- Custom track design
- Value display
- Touch interaction

**Implementation Steps**:
1. Create `CustomSeekBar` class
2. Draw custom thumb and track
3. Handle touch events for value changes
4. Implement value change listener

**Expected Output**:
- Custom styled seek bar
- Smooth touch interaction
- Value change callbacks

---

## Advanced Level Exercises

### Exercise 7: Multi-Touch Gesture View

**Objective**: Create a view that responds to complex multi-touch gestures.

**Requirements**:
- Pinch to zoom functionality
- Rotation gestures
- Pan/drag support
- Gesture state callbacks

**Implementation Steps**:
1. Create `GestureView` class
2. Implement `ScaleGestureDetector`
3. Handle rotation and pan gestures
4. Provide gesture state callbacks

**Expected Output**:
- Multi-touch gesture support
- Smooth gesture animations
- Gesture state feedback

### Exercise 8: Custom Chart Component

**Objective**: Create a reusable chart component (line chart, bar chart, or pie chart).

**Requirements**:
- Multiple chart types
- Data binding interface
- Custom styling options
- Animation support

**Implementation Steps**:
1. Create base `ChartView` class
2. Implement specific chart types
3. Create data binding interface
4. Add animation support

**Expected Output**:
- Reusable chart component
- Multiple chart types
- Smooth data animations

### Exercise 9: Advanced Layout Manager

**Objective**: Create a complex custom ViewGroup with advanced layout logic.

**Requirements**:
- Masonry-style layout (Pinterest-like)
- Dynamic item sizing
- Smooth scrolling
- Memory optimization

**Implementation Steps**:
1. Create `MasonryLayout` class
2. Implement complex measurement logic
3. Optimize for scrolling performance
4. Handle dynamic content changes

**Expected Output**:
- Pinterest-style masonry layout
- Smooth scrolling performance
- Dynamic content support

---

## Mini Projects

### Project 1: Custom Music Player Controls

**Description**: Create a complete set of custom views for a music player interface.

**Components**:
- Circular progress indicator for song progress
- Custom play/pause button with animations
- Volume slider with custom styling
- Equalizer visualization

**Features**:
- Smooth animations
- Touch interactions
- Real-time updates
- Custom styling

### Project 2: Weather Dashboard

**Description**: Create a weather app dashboard with custom weather indicators.

**Components**:
- Custom temperature display
- Weather condition icons
- Wind direction indicator
- UV index meter

**Features**:
- Animated weather transitions
- Real-time data updates
- Responsive design
- Accessibility support

### Project 3: Fitness Tracker

**Description**: Create custom views for a fitness tracking application.

**Components**:
- Step counter with circular progress
- Heart rate monitor display
- Calorie burn indicator
- Activity level gauge

**Features**:
- Real-time data visualization
- Smooth animations
- Multiple data sources
- Progress tracking

---

## Code Examples

### Exercise 1 Solution: Enhanced CircleView

```kotlin
class EnhancedCircleView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : View(context, attrs) {
    
    private val fillPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    
    private val strokePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }
    
    private var currentColor = Color.RED
    private var currentSize = 100f
    private var showBorder = false
    
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EnhancedCircleView,
            0, 0
        ).apply {
            try {
                currentColor = getColor(R.styleable.EnhancedCircleView_circleColor, Color.RED)
                showBorder = getBoolean(R.styleable.EnhancedCircleView_showBorder, false)
            } finally {
                recycle()
            }
        }
        
        fillPaint.color = currentColor
        strokePaint.color = currentColor
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (currentSize.coerceAtMost(width.coerceAtMost(height)) / 2).toFloat()
        
        // Draw fill
        canvas?.drawCircle(centerX, centerY, radius, fillPaint)
        
        // Draw border if enabled
        if (showBorder) {
            canvas?.drawCircle(centerX, centerY, radius, strokePaint)
        }
    }
    
    fun setColor(color: Int) {
        currentColor = color
        fillPaint.color = color
        strokePaint.color = color
        invalidate()
    }
    
    fun setSize(size: Float, animate: Boolean = true) {
        if (animate) {
            ValueAnimator.ofFloat(currentSize, size).apply {
                duration = 300
                addUpdateListener { animation ->
                    currentSize = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        } else {
            currentSize = size
            invalidate()
        }
    }
    
    fun toggleBorder() {
        showBorder = !showBorder
        invalidate()
    }
}
```

### Exercise 4 Solution: Enhanced TwoColumnLayout

```kotlin
class EnhancedTwoColumnLayout @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {
    
    private var columnCount = 2
    private var columnSpacing = 16f
    private var rowSpacing = 16f
    
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EnhancedTwoColumnLayout,
            0, 0
        ).apply {
            try {
                columnCount = getInteger(R.styleable.EnhancedTwoColumnLayout_columnCount, 2)
                columnSpacing = getDimension(R.styleable.EnhancedTwoColumnLayout_columnSpacing, 16f)
                rowSpacing = getDimension(R.styleable.EnhancedTwoColumnLayout_rowSpacing, 16f)
            } finally {
                recycle()
            }
        }
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val availableWidth = width - (columnCount - 1) * columnSpacing.toInt()
        val columnWidth = availableWidth / columnCount
        
        var totalHeight = 0
        var currentRowHeight = 0
        var itemsInCurrentRow = 0
        
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childSpec = MeasureSpec.makeMeasureSpec(columnWidth, MeasureSpec.EXACTLY)
            measureChild(child, childSpec, heightMeasureSpec)
            
            currentRowHeight = maxOf(currentRowHeight, child.measuredHeight)
            itemsInCurrentRow++
            
            if (itemsInCurrentRow == columnCount) {
                totalHeight += currentRowHeight
                if (i < childCount - 1) totalHeight += rowSpacing.toInt()
                currentRowHeight = 0
                itemsInCurrentRow = 0
            }
        }
        
        // Handle remaining items in the last row
        if (itemsInCurrentRow > 0) {
            totalHeight += currentRowHeight
        }
        
        setMeasuredDimension(width, totalHeight)
    }
    
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val availableWidth = width - (columnCount - 1) * columnSpacing.toInt()
        val columnWidth = availableWidth / columnCount
        
        var currentX = left
        var currentY = top
        var currentRowHeight = 0
        var itemsInCurrentRow = 0
        
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childHeight = child.measuredHeight
            
            child.layout(currentX, currentY, currentX + columnWidth, currentY + childHeight)
            
            currentRowHeight = maxOf(currentRowHeight, childHeight)
            itemsInCurrentRow++
            
            if (itemsInCurrentRow == columnCount) {
                currentY += currentRowHeight + rowSpacing.toInt()
                currentX = left
                currentRowHeight = 0
                itemsInCurrentRow = 0
            } else {
                currentX += columnWidth + columnSpacing.toInt()
            }
        }
    }
}
```

---

## Testing Guidelines

### Unit Testing
- Test custom view properties and methods
- Verify attribute parsing
- Test edge cases and error conditions

### Integration Testing
- Test custom views in different layouts
- Verify interaction with other components
- Test performance with multiple instances

### UI Testing
- Test on different screen sizes and orientations
- Verify accessibility features
- Test touch interactions and gestures

### Performance Testing
- Monitor frame rates during animations
- Check memory usage with multiple instances
- Test scrolling performance for complex layouts

---

## Submission Guidelines

### Code Quality
- Follow Kotlin coding conventions
- Add comprehensive comments
- Handle edge cases gracefully
- Implement proper error handling

### Documentation
- Document all public methods
- Explain complex algorithms
- Provide usage examples
- Include performance considerations

### Testing
- Include unit tests for custom views
- Test on multiple devices
- Verify accessibility compliance
- Performance testing results

---

## Resources

### Documentation
- [Android Custom Views Guide](https://developer.android.com/guide/topics/ui/custom-components)
- [Android Canvas API](https://developer.android.com/reference/android/graphics/Canvas)
- [Android Paint API](https://developer.android.com/reference/android/graphics/Paint)

### Tools
- Android Studio Layout Inspector
- Android Studio Profiler
- Android Studio Layout Editor

### Community
- Stack Overflow Android Custom Views
- Android Developers Community
- GitHub Android Custom Views Examples
