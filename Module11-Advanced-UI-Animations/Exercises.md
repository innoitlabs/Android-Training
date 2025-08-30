# Hands-on Exercises - Android Animations & Gestures

## Overview
These exercises are designed to help you practice and reinforce the concepts learned in this module. Start with the easy exercises and progress to more advanced ones.

## Exercise 1: Basic Property Animations (Easy)

### Objective
Create a simple button that animates when pressed.

### Requirements
1. Create a button in your layout
2. Implement a scale animation on button press
3. Add a fade-in animation when the button appears
4. Use ObjectAnimator for the animations

### Starter Code
```kotlin
class Exercise1Activity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise1)
        
        val button = findViewById<Button>(R.id.animated_button)
        
        // TODO: Implement animations here
    }
}
```

### Expected Result
- Button scales down to 0.95 when pressed
- Button scales back to 1.0 when released
- Button fades in from alpha 0 to 1 when activity starts

### Hints
- Use `ObjectAnimator.ofFloat()` for scale animations
- Use `AnimatorSet` to combine animations
- Set appropriate duration and interpolators

---

## Exercise 2: Custom View with Drawing (Intermediate)

### Objective
Create a custom view that draws a progress circle and animates it.

### Requirements
1. Create a custom view class extending `View`
2. Implement `onDraw()` to draw a circular progress indicator
3. Add animation to fill the circle from 0% to 100%
4. Make the progress animatable via a property

### Starter Code
```kotlin
class ProgressCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var progress = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    // TODO: Implement drawing logic
    
    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }
}
```

### Expected Result
- A circular progress indicator that fills clockwise
- Smooth animation from 0% to 100%
- Customizable colors and stroke width

### Hints
- Use `Canvas.drawArc()` for the progress arc
- Use `ObjectAnimator` to animate the progress property
- Consider using `SweepGradient` for gradient effects

---

## Exercise 3: MotionLayout Card Expansion (Advanced)

### Objective
Create an expandable card using MotionLayout that responds to swipe gestures.

### Requirements
1. Create a MotionLayout with a card view
2. Define constraint sets for collapsed and expanded states
3. Add swipe gesture to expand/collapse the card
4. Include multiple views that animate together
5. Add custom attributes for background color and corner radius

### Layout Structure
```xml
<androidx.constraintlayout.motion.widget.MotionLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layoutDescription="@xml/exercise3_motion">
    
    <androidx.cardview.widget.CardView
        android:id="@+id/expandable_card"
        android:layout_width="0dp"
        android:layout_height="200dp"
        app:cardCornerRadius="8dp"
        app:cardElevation="4dp">
        
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            android:padding="16dp">
            
            <TextView
                android:id="@+id/card_title"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Expandable Card"
                android:textSize="18sp"
                android:textStyle="bold" />
                
            <TextView
                android:id="@+id/card_content"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="8dp"
                android:text="Swipe up to expand this card and see more content."
                android:alpha="0.7" />
                
        </LinearLayout>
        
    </androidx.cardview.widget.CardView>
    
</androidx.constraintlayout.motion.widget.MotionLayout>
```

### Expected Result
- Card expands to full screen when swiped up
- Card collapses when swiped down
- Smooth animation with proper easing
- Content fades in/out during transition

### Hints
- Use `OnSwipe` with `dragDirection="dragUp"`
- Define custom attributes for background color changes
- Use `KeyFrameSet` for complex animation paths

---

## Exercise 4: Gesture Detection Game (Advanced)

### Objective
Create a simple gesture-based game where users need to perform specific gestures to score points.

### Requirements
1. Create a custom view that detects various gestures
2. Implement scoring system for different gestures
3. Add visual feedback for detected gestures
4. Include haptic feedback for successful gestures
5. Display current score and gesture instructions

### Gesture Types to Detect
- Single tap (1 point)
- Double tap (3 points)
- Long press (2 points)
- Swipe in any direction (5 points)
- Pinch to zoom (10 points)

### Starter Code
```kotlin
class GestureGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var score = 0
    private val gestureDetector: GestureDetector
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    init {
        gestureDetector = GestureDetector(context, GameGestureListener())
        // TODO: Implement gesture detection
    }
    
    private inner class GameGestureListener : GestureDetector.SimpleOnGestureListener() {
        // TODO: Implement gesture callbacks
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // TODO: Draw game UI
    }
}
```

### Expected Result
- Interactive game area that responds to gestures
- Real-time score display
- Visual feedback for each gesture
- Haptic feedback for successful gestures
- Clear instructions for the user

### Hints
- Use `GestureDetector` for basic gestures
- Implement custom gesture detection for complex gestures
- Use `ValueAnimator` for visual feedback animations
- Consider using `VelocityTracker` for swipe detection

---

## Exercise 5: RecyclerView with Animations (Intermediate)

### Objective
Create a RecyclerView with animated items and custom item decorations.

### Requirements
1. Create a RecyclerView with a custom adapter
2. Implement item animations (fade in, slide in)
3. Add custom ItemDecoration for dividers
4. Implement staggered animations for items
5. Add swipe-to-dismiss functionality

### Data Model
```kotlin
data class AnimatedItem(
    val id: Int,
    val title: String,
    val description: String,
    val color: Int
)
```

### Expected Result
- Smooth item animations when data changes
- Custom dividers between items
- Staggered animation when items appear
- Swipe-to-dismiss with animation
- Proper handling of item removal

### Hints
- Use `DefaultItemAnimator` as a base
- Implement custom `ItemDecoration` for dividers
- Use `ItemTouchHelper` for swipe-to-dismiss
- Consider using `LayoutAnimationController` for staggered animations

---

## Exercise 6: Shared Element Transition (Intermediate)

### Objective
Create a list-detail interface with smooth shared element transitions.

### Requirements
1. Create a list activity with clickable items
2. Create a detail activity for selected items
3. Implement shared element transitions
4. Add custom transition animations
5. Handle back navigation with reverse transitions

### Expected Result
- Smooth transition between list and detail views
- Shared elements animate properly
- Custom enter/exit transitions
- Proper back navigation handling

### Hints
- Use `ActivityOptionsCompat.makeSceneTransitionAnimation()`
- Set `transitionName` on shared elements
- Implement custom transitions in XML
- Handle transition callbacks properly

---

## Exercise 7: Performance Optimization (Advanced)

### Objective
Create a complex animation system and optimize it for performance.

### Requirements
1. Create multiple animated views
2. Implement performance monitoring
3. Optimize animations for 60fps
4. Add memory management
5. Handle configuration changes

### Performance Metrics to Monitor
- Frame rate (target: 60fps)
- Memory usage
- CPU usage
- Animation smoothness

### Expected Result
- Smooth animations at 60fps
- Efficient memory usage
- Proper cleanup of resources
- Good performance on lower-end devices

### Hints
- Use `ViewTreeObserver` to monitor frame rates
- Implement proper animation cancellation
- Use `WeakReference` for callbacks
- Profile with Android Studio tools

---

## Exercise 8: Accessibility Integration (Intermediate)

### Objective
Make your animated components accessible to all users.

### Requirements
1. Add content descriptions to all interactive elements
2. Implement accessibility actions
3. Support screen readers
4. Add haptic feedback
5. Test with accessibility services

### Expected Result
- All animations are accessible
- Screen readers can describe animations
- Haptic feedback for important interactions
- Proper focus management

### Hints
- Use `AccessibilityDelegate` for custom accessibility
- Implement `AccessibilityAction` for custom gestures
- Test with TalkBack or other screen readers
- Consider users with motor impairments

---

## Mini Project: Animated Profile App

### Objective
Combine all the concepts learned to create a complete animated profile application.

### Features to Implement
1. **Animated Profile Picture Entry**
   - Circular image view with border animation
   - Tap to change profile picture
   - Smooth image loading animation

2. **MotionLayout Card Expansion**
   - Profile card that expands on swipe
   - Multiple content sections that animate together
   - Custom transition effects

3. **Gesture Detection**
   - Double-tap to like profile
   - Long press for more options
   - Swipe gestures for navigation

4. **RecyclerView with Animated Items**
   - List of posts or activities
   - Staggered item animations
   - Swipe-to-dismiss functionality

5. **Shared Element Transitions**
   - Smooth transition to detail screens
   - Shared profile picture and other elements
   - Custom transition animations

### Technical Requirements
- Use MotionLayout for complex animations
- Implement custom gesture detection
- Add RecyclerView with animations
- Include shared element transitions
- Optimize for performance
- Ensure accessibility compliance

### Expected Result
A polished, animated profile app that demonstrates all the concepts covered in this module.

---

## Submission Guidelines

### For Each Exercise
1. **Code Implementation**: Complete the required functionality
2. **Documentation**: Add comments explaining your approach
3. **Testing**: Test on different devices and screen sizes
4. **Performance**: Ensure smooth animations (60fps)
5. **Accessibility**: Add proper content descriptions

### Code Quality Requirements
- Clean, readable code with proper naming
- Error handling for edge cases
- Memory leak prevention
- Proper resource management
- Follow Android best practices

### Bonus Challenges
- Add unit tests for your implementations
- Create UI tests for gesture interactions
- Implement advanced animation effects
- Add internationalization support
- Optimize for different screen densities

---

## Getting Help

### Resources
- Android Developer Documentation
- Material Design Guidelines
- Android Studio Layout Inspector
- Performance Profiling Tools

### Debugging Tips
- Use `adb shell dumpsys gfxinfo` to monitor frame rates
- Enable "Show layout bounds" in developer options
- Use Android Studio's Layout Inspector
- Test on physical devices, not just emulators

### Common Issues
- Memory leaks from animation listeners
- Performance issues with complex animations
- Accessibility problems with custom gestures
- Configuration change handling

---

## Evaluation Criteria

### Code Quality (30%)
- Clean, readable code
- Proper error handling
- Memory management
- Resource cleanup

### Functionality (40%)
- All requirements implemented
- Smooth animations
- Proper gesture detection
- Accessibility support

### Performance (20%)
- 60fps animations
- Efficient memory usage
- Proper optimization

### Documentation (10%)
- Clear comments
- README file
- Code documentation

---

**Happy Coding! 🚀**

Remember to start with the easier exercises and gradually work your way up to the more complex ones. Each exercise builds upon the previous ones, so take your time to understand the concepts before moving forward.
