# MotionLayout for Complex Animations

## Overview
MotionLayout is a powerful layout that extends ConstraintLayout to create complex animations and transitions. It provides a declarative way to define animations through XML, making it easier to create sophisticated motion designs.

## Key Concepts

### 1. MotionLayout Structure
MotionLayout uses three main components:
- **MotionLayout**: The container view
- **ConstraintSet**: Defines view positions and properties
- **Transition**: Defines how to animate between constraint sets

### 2. Basic MotionLayout Setup
```xml
<androidx.constraintlayout.motion.widget.MotionLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layoutDescription="@xml/scene_motion">
    
    <View
        android:id="@+id/animated_view"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="@color/primary" />
        
</androidx.constraintlayout.motion.widget.MotionLayout>
```

## MotionScene XML Configuration

### 1. Basic MotionScene
```xml
<!-- res/xml/scene_motion.xml -->
<MotionScene xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:motion="http://schemas.android.com/apk/res/android">
    
    <Transition
        motion:constraintSetStart="@id/start"
        motion:constraintSetEnd="@id/end"
        motion:duration="1000">
        <OnSwipe motion:touchAnchorId="@id/animated_view"
                 motion:touchAnchorSide="right"
                 motion:dragDirection="dragRight" />
    </Transition>
    
    <ConstraintSet android:id="@+id/start">
        <Constraint
            android:id="@+id/animated_view"
            android:layout_width="100dp"
            android:layout_height="100dp"
            motion:layout_constraintStart_toStartOf="parent"
            motion:layout_constraintTop_toTopOf="parent"
            motion:layout_constraintBottom_toBottomOf="parent" />
    </ConstraintSet>
    
    <ConstraintSet android:id="@+id/end">
        <Constraint
            android:id="@+id/animated_view"
            android:layout_width="100dp"
            android:layout_height="100dp"
            motion:layout_constraintEnd_toEndOf="parent"
            motion:layout_constraintTop_toTopOf="parent"
            motion:layout_constraintBottom_toBottomOf="parent" />
    </ConstraintSet>
    
</MotionScene>
```

### 2. Complex MotionScene with Multiple Views
```xml
<!-- res/xml/complex_motion.xml -->
<MotionScene xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:motion="http://schemas.android.com/apk/res/android">
    
    <Transition
        motion:constraintSetStart="@id/collapsed"
        motion:constraintSetEnd="@id/expanded"
        motion:duration="1000">
        <OnSwipe motion:touchAnchorId="@id/card"
                 motion:touchAnchorSide="top"
                 motion:dragDirection="dragUp" />
    </Transition>
    
    <ConstraintSet android:id="@+id/collapsed">
        <Constraint
            android:id="@+id/card"
            android:layout_width="0dp"
            android:layout_height="200dp"
            motion:layout_constraintStart_toStartOf="parent"
            motion:layout_constraintEnd_toEndOf="parent"
            motion:layout_constraintBottom_toBottomOf="parent" />
            
        <Constraint
            android:id="@+id/title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            motion:layout_constraintStart_toStartOf="@id/card"
            motion:layout_constraintTop_toTopOf="@id/card"
            android:layout_margin="16dp" />
            
        <Constraint
            android:id="@+id/content"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:alpha="0"
            motion:layout_constraintStart_toStartOf="@id/card"
            motion:layout_constraintEnd_toEndOf="@id/card"
            motion:layout_constraintTop_toBottomOf="@id/title"
            motion:layout_constraintBottom_toBottomOf="@id/card" />
    </ConstraintSet>
    
    <ConstraintSet android:id="@+id/expanded">
        <Constraint
            android:id="@+id/card"
            android:layout_width="0dp"
            android:layout_height="0dp"
            motion:layout_constraintStart_toStartOf="parent"
            motion:layout_constraintEnd_toEndOf="parent"
            motion:layout_constraintTop_toTopOf="parent"
            motion:layout_constraintBottom_toBottomOf="parent" />
            
        <Constraint
            android:id="@+id/title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            motion:layout_constraintStart_toStartOf="@id/card"
            motion:layout_constraintTop_toTopOf="@id/card"
            android:layout_margin="32dp" />
            
        <Constraint
            android:id="@+id/content"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:alpha="1"
            motion:layout_constraintStart_toStartOf="@id/card"
            motion:layout_constraintEnd_toEndOf="@id/card"
            motion:layout_constraintTop_toBottomOf="@id/title"
            motion:layout_constraintBottom_toBottomOf="@id/card"
            android:layout_margin="16dp" />
    </ConstraintSet>
    
</MotionScene>
```

## Gesture-Driven Animations

### 1. Swipe Gestures
```xml
<Transition
    motion:constraintSetStart="@id/start"
    motion:constraintSetEnd="@id/end"
    motion:duration="1000">
    
    <!-- Swipe from left to right -->
    <OnSwipe motion:touchAnchorId="@id/view"
             motion:touchAnchorSide="left"
             motion:dragDirection="dragRight" />
             
    <!-- Swipe from top to bottom -->
    <OnSwipe motion:touchAnchorId="@id/view"
             motion:touchAnchorSide="top"
             motion:dragDirection="dragDown" />
             
    <!-- Swipe with custom behavior -->
    <OnSwipe motion:touchAnchorId="@id/view"
             motion:touchAnchorSide="right"
             motion:dragDirection="dragLeft"
             motion:onTouchUp="stop"
             motion:autoComplete="true" />
</Transition>
```

### 2. Click Gestures
```xml
<Transition
    motion:constraintSetStart="@id/start"
    motion:constraintSetEnd="@id/end"
    motion:duration="500">
    
    <OnClick motion:targetId="@id/button"
             motion:clickAction="toggle" />
</Transition>
```

## Custom Attributes and Properties

### 1. Custom Properties
```xml
<ConstraintSet android:id="@+id/start">
    <Constraint
        android:id="@+id/view"
        android:layout_width="100dp"
        android:layout_height="100dp"
        motion:layout_constraintStart_toStartOf="parent"
        motion:layout_constraintTop_toTopOf="parent">
        
        <!-- Custom properties -->
        <CustomAttribute
            motion:attributeName="backgroundColor"
            motion:customColorValue="#FF0000" />
            
        <CustomAttribute
            motion:attributeName="cornerRadius"
            motion:customFloatValue="0" />
    </Constraint>
</ConstraintSet>

<ConstraintSet android:id="@+id/end">
    <Constraint
        android:id="@+id/view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        motion:layout_constraintEnd_toEndOf="parent"
        motion:layout_constraintBottom_toBottomOf="parent">
        
        <CustomAttribute
            motion:attributeName="backgroundColor"
            motion:customColorValue="#00FF00" />
            
        <CustomAttribute
            motion:attributeName="cornerRadius"
            motion:customFloatValue="50" />
    </Constraint>
</ConstraintSet>
```

### 2. KeyFrame Animations
```xml
<Transition
    motion:constraintSetStart="@id/start"
    motion:constraintSetEnd="@id/end"
    motion:duration="1000">
    
    <KeyFrameSet>
        <!-- Bounce effect at 50% -->
        <KeyPosition
            motion:framePosition="50"
            motion:keyPositionType="pathRelative"
            motion:percentY="0.2" />
            
        <!-- Scale effect at 25% -->
        <KeyAttribute
            motion:framePosition="25"
            android:scaleX="1.2"
            android:scaleY="1.2" />
    </KeyFrameSet>
</Transition>
```

## Programmatic Control

### 1. Transition Control
```kotlin
class MotionLayoutExample : AppCompatActivity() {
    
    private lateinit var motionLayout: MotionLayout
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion)
        
        motionLayout = findViewById(R.id.motion_layout)
        
        // Set transition listener
        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
                // Transition started
            }
            
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                // Transition progress
            }
            
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                // Transition completed
            }
            
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
                // Trigger activated
            }
        })
    }
    
    fun startTransition() {
        // Start transition to end state
        motionLayout.transitionToEnd()
    }
    
    fun reverseTransition() {
        // Start transition to start state
        motionLayout.transitionToStart()
    }
    
    fun setProgress(progress: Float) {
        // Set transition progress (0.0 to 1.0)
        motionLayout.progress = progress
    }
}
```

### 2. Dynamic Constraint Sets
```kotlin
fun createDynamicMotionScene() {
    val constraintSet = ConstraintSet()
    constraintSet.clone(motionLayout)
    
    // Modify constraints programmatically
    constraintSet.setMargin(R.id.view, ConstraintSet.START, 100)
    constraintSet.setMargin(R.id.view, ConstraintSet.TOP, 200)
    
    // Apply the constraint set
    constraintSet.applyTo(motionLayout)
}
```

## Advanced Features

### 1. Multiple Transitions
```xml
<MotionScene>
    <!-- Transition 1: Swipe to expand -->
    <Transition
        motion:constraintSetStart="@id/collapsed"
        motion:constraintSetEnd="@id/expanded"
        motion:duration="1000">
        <OnSwipe motion:touchAnchorId="@id/card"
                 motion:dragDirection="dragUp" />
    </Transition>
    
    <!-- Transition 2: Click to highlight -->
    <Transition
        motion:constraintSetStart="@id/expanded"
        motion:constraintSetEnd="@id/highlighted"
        motion:duration="300">
        <OnClick motion:targetId="@id/card"
                 motion:clickAction="toggle" />
    </Transition>
    
    <!-- Transition 3: Auto-return -->
    <Transition
        motion:constraintSetStart="@id/highlighted"
        motion:constraintSetEnd="@id/expanded"
        motion:duration="500"
        motion:autoTransition="animateToEnd" />
</MotionScene>
```

### 2. Nested MotionLayout
```xml
<androidx.constraintlayout.motion.widget.MotionLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layoutDescription="@xml/parent_motion">
    
    <androidx.constraintlayout.motion.widget.MotionLayout
        android:id="@+id/nested_motion"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layoutDescription="@xml/nested_motion">
        
        <!-- Nested content -->
        
    </androidx.constraintlayout.motion.widget.MotionLayout>
    
</androidx.constraintlayout.motion.widget.MotionLayout>
```

## Performance Optimization

### 1. Hardware Acceleration
```xml
<!-- Enable in theme -->
<item name="android:hardwareAccelerated">true</item>
```

### 2. Efficient Constraint Sets
```kotlin
// Cache constraint sets
private val startConstraintSet by lazy {
    ConstraintSet().apply {
        clone(motionLayout, R.id.start)
    }
}

private val endConstraintSet by lazy {
    ConstraintSet().apply {
        clone(motionLayout, R.id.end)
    }
}
```

### 3. Memory Management
```kotlin
override fun onDestroy() {
    super.onDestroy()
    motionLayout.setTransitionListener(null)
}
```

## Common Use Cases

### 1. Expandable Cards
```xml
<!-- Card expansion animation -->
<Transition
    motion:constraintSetStart="@id/card_collapsed"
    motion:constraintSetEnd="@id/card_expanded"
    motion:duration="800">
    <OnSwipe motion:touchAnchorId="@id/card"
             motion:dragDirection="dragUp" />
</Transition>
```

### 2. Bottom Sheet
```xml
<!-- Bottom sheet animation -->
<Transition
    motion:constraintSetStart="@id/bottom_sheet_hidden"
    motion:constraintSetEnd="@id/bottom_sheet_visible"
    motion:duration="600">
    <OnSwipe motion:touchAnchorId="@id/bottom_sheet"
             motion:dragDirection="dragUp" />
</Transition>
```

### 3. Floating Action Button
```xml
<!-- FAB animation -->
<Transition
    motion:constraintSetStart="@id/fab_collapsed"
    motion:constraintSetEnd="@id/fab_expanded"
    motion:duration="400">
    <OnClick motion:targetId="@id/fab"
             motion:clickAction="toggle" />
</Transition>
```

## Debugging Tips

### 1. MotionLayout Inspector
- Use Android Studio's MotionLayout Inspector
- Visualize constraint sets and transitions
- Debug animation paths and keyframes

### 2. Transition Logging
```kotlin
motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
    override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
        Log.d("MotionLayout", "Progress: $progress")
    }
})
```

### 3. Performance Profiling
```kotlin
// Enable performance tracking
motionLayout.setDebugMode(MotionLayout.DEBUG_SHOW_PATH)
```

## Summary
MotionLayout provides a powerful, declarative way to create complex animations. Key points:
- Use XML to define constraint sets and transitions
- Leverage gesture-driven animations for interactive experiences
- Implement custom properties for advanced effects
- Use programmatic control for dynamic animations
- Follow performance best practices
- Debug using MotionLayout Inspector

## Next Steps
- Explore custom MotionLayout implementations
- Study Material Motion guidelines
- Learn about advanced keyframe animations
- Practice with complex gesture interactions
