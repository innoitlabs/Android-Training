# View Animations & Transition Animations

## Overview
View animations are the traditional way to animate views in Android. They operate on the view's visual representation and are useful for simple animations like fade, scale, slide, and rotate effects.

## Key Concepts

### 1. View Animation Types
- **Alpha**: Fade in/out animations
- **Scale**: Grow/shrink animations
- **Translate**: Move animations
- **Rotate**: Rotation animations

### 2. Animation XML Structure
```xml
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="1000"
    android:interpolator="@android:interpolator/accelerate_decelerate">
    
    <alpha
        android:fromAlpha="0.0"
        android:toAlpha="1.0" />
    
    <scale
        android:fromXScale="0.0"
        android:toXScale="1.0"
        android:fromYScale="0.0"
        android:toYScale="1.0"
        android:pivotX="50%"
        android:pivotY="50%" />
</set>
```

## Animation XML Examples

### 1. Fade In Animation
```xml
<!-- res/anim/fade_in.xml -->
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromAlpha="0.0"
    android:toAlpha="1.0"
    android:duration="1000"
    android:interpolator="@android:interpolator/decelerate" />
```

### 2. Fade Out Animation
```xml
<!-- res/anim/fade_out.xml -->
<alpha xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromAlpha="1.0"
    android:toAlpha="0.0"
    android:duration="500"
    android:interpolator="@android:interpolator/accelerate" />
```

### 3. Scale Animation
```xml
<!-- res/anim/scale_in.xml -->
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="1.0"
    android:fromYScale="0.0"
    android:toYScale="1.0"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="800"
    android:interpolator="@android:interpolator/overshoot" />
```

### 4. Slide Animation
```xml
<!-- res/anim/slide_in_from_left.xml -->
<translate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXDelta="-100%p"
    android:toXDelta="0%p"
    android:duration="600"
    android:interpolator="@android:interpolator/decelerate" />
```

### 5. Rotate Animation
```xml
<!-- res/anim/rotate.xml -->
<rotate xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromDegrees="0"
    android:toDegrees="360"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="1000"
    android:interpolator="@android:interpolator/linear" />
```

### 6. Complex Animation Set
```xml
<!-- res/anim/complex_animation.xml -->
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="1000"
    android:interpolator="@android:interpolator/accelerate_decelerate">
    
    <alpha
        android:fromAlpha="0.0"
        android:toAlpha="1.0" />
    
    <scale
        android:fromXScale="0.5"
        android:toXScale="1.0"
        android:fromYScale="0.5"
        android:toYScale="1.0"
        android:pivotX="50%"
        android:pivotY="50%" />
    
    <translate
        android:fromYDelta="50"
        android:toYDelta="0" />
</set>
```

## Using View Animations in Kotlin

### 1. Loading and Starting Animations
```kotlin
class ViewAnimationExample {
    
    fun startFadeInAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        view.startAnimation(animation)
    }
    
    fun startComplexAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.complex_animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                view.visibility = View.VISIBLE
            }
            
            override fun onAnimationEnd(animation: Animation?) {
                // Animation completed
            }
            
            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated
            }
        })
        view.startAnimation(animation)
    }
}
```

### 2. Creating Animations Programmatically
```kotlin
fun createProgrammaticAnimation(view: View) {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.duration = 1000
    fadeIn.interpolator = DecelerateInterpolator()
    
    val scaleUp = ScaleAnimation(0f, 1f, 0f, 1f, 
        Animation.RELATIVE_TO_SELF, 0.5f, 
        Animation.RELATIVE_TO_SELF, 0.5f)
    scaleUp.duration = 800
    scaleUp.interpolator = OvershootInterpolator()
    
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(fadeIn)
    animationSet.addAnimation(scaleUp)
    
    view.startAnimation(animationSet)
}
```

## Transition Animations

### 1. Activity Transitions
```kotlin
// Starting activity with transition
val intent = Intent(this, DetailActivity::class.java)
val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
    this, 
    view, 
    "sharedElement"
)
startActivity(intent, options.toBundle())
```

### 2. Shared Element Transitions
```kotlin
// In the starting activity
val imageView = findViewById<ImageView>(R.id.profile_image)
imageView.transitionName = "profileImage"

val intent = Intent(this, DetailActivity::class.java)
val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
    this, 
    imageView, 
    "profileImage"
)
startActivity(intent, options.toBundle())

// In the target activity
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    
    val detailImageView = findViewById<ImageView>(R.id.detail_image)
    detailImageView.transitionName = "profileImage"
}
```

### 3. Custom Activity Transitions
```kotlin
// Define custom transitions in styles.xml
<style name="CustomTransition" parent="Theme.AppCompat">
    <item name="android:windowEnterTransition">@transition/slide_in</item>
    <item name="android:windowExitTransition">@transition/slide_out</item>
</style>

// Apply in AndroidManifest.xml
<activity
    android:name=".DetailActivity"
    android:theme="@style/CustomTransition" />
```

## Transition XML Examples

### 1. Slide Transition
```xml
<!-- res/transition/slide_in.xml -->
<slide xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300"
    android:interpolator="@android:interpolator/decelerate">
    <targets>
        <target android:targetId="@id/content" />
    </targets>
</slide>
```

### 2. Fade Transition
```xml
<!-- res/transition/fade.xml -->
<fade xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300">
    <targets>
        <target android:targetId="@id/content" />
    </targets>
</fade>
```

### 3. Explode Transition
```xml
<!-- res/transition/explode.xml -->
<explode xmlns:android="http://schemas.android.com/apk/res/android"
    android:duration="300"
    android:interpolator="@android:interpolator/decelerate">
    <targets>
        <target android:targetId="@id/content" />
    </targets>
</explode>
```

## Fragment Transitions

### 1. Fragment Animation
```kotlin
class FragmentTransitionExample {
    
    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        
        // Set custom animations
        transaction.setCustomAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        )
        
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
```

### 2. Fragment Transition Animations
```xml
<!-- res/anim/slide_in_from_right.xml -->
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="100%p"
        android:toXDelta="0%p"
        android:duration="300"
        android:interpolator="@android:interpolator/decelerate" />
</set>

<!-- res/anim/slide_out_to_left.xml -->
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:fromXDelta="0%p"
        android:toXDelta="-100%p"
        android:duration="300"
        android:interpolator="@android:interpolator/accelerate" />
</set>
```

## Animation Listeners

### 1. Animation.AnimationListener
```kotlin
animation.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {
        // Animation started
    }
    
    override fun onAnimationEnd(animation: Animation?) {
        // Animation ended
    }
    
    override fun onAnimationRepeat(animation: Animation?) {
        // Animation repeated
    }
})
```

### 2. AnimationListenerAdapter
```kotlin
animation.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {
        view.visibility = View.VISIBLE
    }
    
    override fun onAnimationEnd(animation: Animation?) {
        // Clean up if needed
    }
    
    override fun onAnimationRepeat(animation: Animation?) {
        // Handle repeat
    }
})
```

## Common Use Cases

### 1. Loading States
```kotlin
fun showLoadingAnimation(view: View) {
    val rotation = AnimationUtils.loadAnimation(context, R.anim.rotate)
    rotation.repeatCount = Animation.INFINITE
    view.startAnimation(rotation)
}

fun hideLoadingAnimation(view: View) {
    view.clearAnimation()
}
```

### 2. Error States
```kotlin
fun showErrorAnimation(view: View) {
    val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
    view.startAnimation(shake)
}
```

### 3. Success States
```kotlin
fun showSuccessAnimation(view: View) {
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)
    view.startAnimation(bounce)
}
```

## Performance Considerations

### 1. Hardware Acceleration
```xml
<!-- Enable in theme or activity -->
<item name="android:hardwareAccelerated">true</item>
```

### 2. Animation Caching
```kotlin
// Cache animations to avoid repeated loading
private val fadeInAnimation by lazy {
    AnimationUtils.loadAnimation(context, R.anim.fade_in)
}

fun animateView(view: View) {
    view.startAnimation(fadeInAnimation)
}
```

### 3. Memory Management
```kotlin
override fun onDestroy() {
    super.onDestroy()
    // Clear animations to prevent memory leaks
    view.clearAnimation()
}
```

## Debugging Tips

### 1. Animation Duration Scale
```kotlin
// Slow down animations for debugging
Settings.Global.putFloat(contentResolver, Settings.Global.ANIMATION_DURATION_SCALE, 10f)
```

### 2. Layout Inspector
- Use Layout Inspector to verify view properties during animation
- Check that animations are running on the UI thread

### 3. Animation Logging
```kotlin
animation.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {
        Log.d("Animation", "Started: ${animation?.duration}ms")
    }
    
    override fun onAnimationEnd(animation: Animation?) {
        Log.d("Animation", "Ended")
    }
    
    override fun onAnimationRepeat(animation: Animation?) {
        Log.d("Animation", "Repeated")
    }
})
```

## Summary
View animations provide a simple and effective way to add visual feedback to your app. Key points:
- Use XML animations for reusable, declarative animations
- Use programmatic animations for dynamic, complex animations
- Implement proper animation listeners for state management
- Consider performance and accessibility
- Use transitions for smooth navigation experiences

## Next Steps
- Explore Property Animations for more advanced use cases
- Learn about MotionLayout for complex constraint-based animations
- Study Material Motion guidelines for design consistency
