# Property Animations & ObjectAnimator

## Overview
Property animations allow you to animate any property of an object over time. The Android animation system provides powerful tools like `ObjectAnimator` and `ViewPropertyAnimator` to create smooth, hardware-accelerated animations.

## Key Concepts

### 1. ObjectAnimator
`ObjectAnimator` is the core class for creating property animations. It animates a specific property of a target object.

#### Basic Syntax
```kotlin
val animator = ObjectAnimator.ofFloat(view, "translationY", 0f, 500f)
animator.duration = 1000
animator.start()
```

#### Properties You Can Animate
- **Position**: `translationX`, `translationY`, `translationZ`
- **Size**: `scaleX`, `scaleY`
- **Rotation**: `rotation`, `rotationX`, `rotationY`
- **Transparency**: `alpha`
- **Background**: `backgroundColor` (ColorAnimator)

### 2. ViewPropertyAnimator
A simplified API for animating View properties with method chaining.

```kotlin
view.animate()
    .alpha(0f)
    .translationY(100f)
    .setDuration(500)
    .setInterpolator(AccelerateDecelerateInterpolator())
    .withEndAction { /* Animation completed */ }
    .start()
```

### 3. AnimatorSet
Combine multiple animations to run in sequence or parallel.

```kotlin
val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
val slideUp = ObjectAnimator.ofFloat(view, "translationY", 100f, 0f)

val animatorSet = AnimatorSet()
animatorSet.playTogether(fadeIn, slideUp) // Run simultaneously
// OR
animatorSet.playSequentially(fadeIn, slideUp) // Run in sequence
animatorSet.duration = 1000
animatorSet.start()
```

## Practical Examples

### Example 1: Basic Property Animation
```kotlin
class PropertyAnimationExample {
    
    fun animateView(view: View) {
        // Animate translation (move view)
        val translationAnimator = ObjectAnimator.ofFloat(view, "translationY", 0f, 200f)
        translationAnimator.duration = 1000
        translationAnimator.interpolator = AccelerateDecelerateInterpolator()
        translationAnimator.start()
        
        // Animate scale
        val scaleAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f)
        scaleAnimator.duration = 800
        scaleAnimator.start()
    }
}
```

### Example 2: Complex Animation Sequence
```kotlin
fun createComplexAnimation(view: View) {
    val fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
    val scaleUp = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
    val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
    val slideIn = ObjectAnimator.ofFloat(view, "translationX", -200f, 0f)
    
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(fadeIn, scaleUp, scaleUpY, slideIn)
    animatorSet.duration = 1000
    animatorSet.interpolator = OvershootInterpolator()
    
    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            view.visibility = View.VISIBLE
        }
        
        override fun onAnimationEnd(animation: Animator) {
            // Animation completed
        }
    })
    
    animatorSet.start()
}
```

### Example 3: Color Animation
```kotlin
fun animateBackgroundColor(view: View) {
    val colorAnimator = ObjectAnimator.ofInt(
        view,
        "backgroundColor",
        Color.RED,
        Color.BLUE,
        Color.GREEN
    )
    colorAnimator.duration = 2000
    colorAnimator.setEvaluator(ArgbEvaluator())
    colorAnimator.repeatCount = ObjectAnimator.INFINITE
    colorAnimator.repeatMode = ObjectAnimator.REVERSE
    colorAnimator.start()
}
```

## Interpolators

Interpolators define the rate of change of an animation over time.

### Common Interpolators
```kotlin
// Linear - constant speed
LinearInterpolator()

// Accelerate - starts slow, ends fast
AccelerateInterpolator()

// Decelerate - starts fast, ends slow
DecelerateInterpolator()

// AccelerateDecelerate - starts slow, speeds up, ends slow
AccelerateDecelerateInterpolator()

// Overshoot - goes beyond target, then settles
OvershootInterpolator()

// Bounce - bounces at the end
BounceInterpolator()

// Anticipate - starts backward, then forward
AnticipateInterpolator()

// Custom interpolator
class CustomInterpolator : Interpolator {
    override fun getInterpolation(input: Float): Float {
        return input * input * (3f - 2f * input) // Smooth step
    }
}
```

## Animation Listeners

### AnimatorListenerAdapter
```kotlin
animator.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationStart(animation: Animator) {
        // Animation started
    }
    
    override fun onAnimationEnd(animation: Animator) {
        // Animation ended
    }
    
    override fun onAnimationCancel(animation: Animator) {
        // Animation cancelled
    }
    
    override fun onAnimationRepeat(animation: Animator) {
        // Animation repeated
    }
})
```

### AnimatorUpdateListener
```kotlin
animator.addUpdateListener { animation ->
    val animatedValue = animation.animatedValue as Float
    // Handle animation updates
}
```

## Performance Best Practices

### 1. Use Hardware Acceleration
```kotlin
// Enable hardware acceleration in your theme or activity
android:hardwareAccelerated="true"
```

### 2. Avoid Object Creation in Animations
```kotlin
// ❌ Bad - creates new objects every frame
animator.addUpdateListener { animation ->
    val paint = Paint() // Don't create objects here
}

// ✅ Good - reuse objects
private val paint = Paint()
animator.addUpdateListener { animation ->
    // Use existing paint object
}
```

### 3. Use ViewPropertyAnimator for Simple Animations
```kotlin
// ✅ Efficient for simple animations
view.animate()
    .alpha(0f)
    .translationY(100f)
    .setDuration(500)
    .start()
```

### 4. Cancel Animations When Needed
```kotlin
private var currentAnimator: Animator? = null

fun startAnimation(view: View) {
    currentAnimator?.cancel() // Cancel previous animation
    currentAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
    currentAnimator?.start()
}
```

## Common Use Cases

### 1. Loading Animations
```kotlin
fun showLoadingAnimation(view: View) {
    val rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
    rotation.duration = 1000
    rotation.repeatCount = ObjectAnimator.INFINITE
    rotation.interpolator = LinearInterpolator()
    rotation.start()
}
```

### 2. Button Press Animation
```kotlin
fun animateButtonPress(button: View) {
    val scaleDown = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.95f)
    val scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.95f)
    
    val scaleUp = ObjectAnimator.ofFloat(button, "scaleX", 0.95f, 1f)
    val scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 0.95f, 1f)
    
    val pressAnim = AnimatorSet()
    pressAnim.playTogether(scaleDown, scaleDownY)
    pressAnim.duration = 100
    
    val releaseAnim = AnimatorSet()
    releaseAnim.playTogether(scaleUp, scaleUpY)
    releaseAnim.duration = 100
    
    val fullAnim = AnimatorSet()
    fullAnim.playSequentially(pressAnim, releaseAnim)
    fullAnim.start()
}
```

### 3. Fade In/Out Transitions
```kotlin
fun fadeInView(view: View) {
    view.alpha = 0f
    view.visibility = View.VISIBLE
    
    view.animate()
        .alpha(1f)
        .setDuration(300)
        .setInterpolator(DecelerateInterpolator())
        .start()
}

fun fadeOutView(view: View) {
    view.animate()
        .alpha(0f)
        .setDuration(300)
        .setInterpolator(AccelerateInterpolator())
        .withEndAction {
            view.visibility = View.GONE
        }
        .start()
}
```

## Debugging Tips

### 1. Enable Animation Duration Scale
```kotlin
// In developer options, set animation duration scale to 10x for debugging
Settings.Global.putFloat(contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 10f)
```

### 2. Use Layout Inspector
- Use Android Studio's Layout Inspector to verify view properties during animation
- Check that animations are actually running on the UI thread

### 3. Log Animation Values
```kotlin
animator.addUpdateListener { animation ->
    Log.d("Animation", "Value: ${animation.animatedValue}")
}
```

## Summary
Property animations provide a powerful way to create smooth, engaging user experiences. Key points:
- Use `ObjectAnimator` for complex property animations
- Use `ViewPropertyAnimator` for simple view animations
- Use `AnimatorSet` to combine multiple animations
- Choose appropriate interpolators for natural motion
- Follow performance best practices
- Always consider accessibility and user preferences

## Next Steps
- Explore View Animations for legacy animation support
- Learn about MotionLayout for complex constraint-based animations
- Study Material Motion guidelines for design consistency
