# Exercises and Challenges

## Overview

This section provides hands-on exercises and challenges to reinforce your understanding of responsive design concepts. Start with the easy exercises and work your way up to the advanced challenges.

## Easy Exercises

### Exercise 1: Convert px to dp/sp

**Objective**: Convert all hardcoded pixel values to appropriate dp/sp units.

**Starting Code**:
```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16px">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Hello World"
        android:textSize="18px"
        android:layout_margin="8px"/>

    <Button
        android:layout_width="120px"
        android:layout_height="50px"
        android:text="Click Me"/>

</LinearLayout>
```

**Task**: Convert all `px` values to appropriate `dp` or `sp` values.

**Solution**:
```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Hello World"
        android:textSize="18sp"
        android:layout_margin="8dp"/>

    <Button
        android:layout_width="120dp"
        android:layout_height="50dp"
        android:text="Click Me"/>

</LinearLayout>
```

### Exercise 2: Create Dimension Resources

**Objective**: Extract hardcoded dimensions into resource files.

**Starting Code**:
```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Title"
    android:textSize="24sp"
    android:layout_margin="16dp"
    android:padding="8dp"/>
```

**Task**: Create a `dimens.xml` file and replace hardcoded values with references.

**Solution**:
```xml
<!-- res/values/dimens.xml -->
<resources>
    <dimen name="text_size_title">24sp</dimen>
    <dimen name="margin_standard">16dp</dimen>
    <dimen name="padding_small">8dp</dimen>
</resources>
```

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Title"
    android:textSize="@dimen/text_size_title"
    android:layout_margin="@dimen/margin_standard"
    android:padding="@dimen/padding_small"/>
```

### Exercise 3: Basic ConstraintLayout

**Objective**: Convert a LinearLayout to ConstraintLayout.

**Starting Code**:
```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Centered Text"/>

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button"/>

</LinearLayout>
```

**Task**: Convert to ConstraintLayout with the same visual result.

**Solution**:
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Centered Text"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textView"
        app:layout_constraintBottom_toBottomOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

## Intermediate Exercises

### Exercise 4: Responsive Card Layout

**Objective**: Create a card layout that adapts to different screen sizes.

**Requirements**:
- Card should be 300dp wide on phones
- Card should be 400dp wide on tablets
- Card should have proper margins and padding
- Card should contain an image, title, and description

**Solution**:
```xml
<!-- res/layout/card_item.xml -->
<androidx.cardview.widget.CardView
    android:layout_width="300dp"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="4dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:src="@drawable/card_image"
            android:scaleType="centerCrop"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Card Title"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginTop="8dp"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Card description goes here..."
            android:textSize="14sp"
            android:layout_marginTop="4dp"/>

    </LinearLayout>

</androidx.cardview.widget.CardView>
```

```xml
<!-- res/layout-sw600dp/card_item.xml -->
<androidx.cardview.widget.CardView
    android:layout_width="400dp"
    android:layout_height="wrap_content"
    android:layout_margin="12dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="6dp">

    <!-- Same content with larger padding -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp">

        <!-- Same content structure -->

    </LinearLayout>

</androidx.cardview.widget.CardView>
```

### Exercise 5: Alternative Layouts

**Objective**: Create different layouts for portrait and landscape orientations.

**Requirements**:
- Portrait: Vertical layout with image on top, text below
- Landscape: Horizontal layout with image on left, text on right
- Use appropriate resource qualifiers

**Solution**:
```xml
<!-- res/layout/activity_detail.xml (Portrait) -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:src="@drawable/detail_image"
        android:scaleType="centerCrop"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Detail Title"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginTop="16dp"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Detail description..."
        android:textSize="16sp"
        android:layout_marginTop="8dp"/>

</LinearLayout>
```

```xml
<!-- res/layout-land/activity_detail.xml (Landscape) -->
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="horizontal"
    android:padding="16dp">

    <ImageView
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:src="@drawable/detail_image"
        android:scaleType="centerCrop"/>

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_weight="1"
        android:orientation="vertical"
        android:layout_marginStart="16dp">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Detail Title"
            android:textSize="24sp"
            android:textStyle="bold"/>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Detail description..."
            android:textSize="16sp"
            android:layout_marginTop="8dp"/>

    </LinearLayout>

</LinearLayout>
```

### Exercise 6: Responsive Grid

**Objective**: Create a grid that shows different numbers of columns based on screen size.

**Requirements**:
- 2 columns on phones
- 3 columns on tablets
- 4 columns on large tablets

**Solution**:
```xml
<!-- res/layout/grid_layout.xml (Phone) -->
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="8dp"/>
```

```kotlin
// In your Activity or Fragment
val layoutManager = GridLayoutManager(this, 2) // 2 columns for phones
recyclerView.layoutManager = layoutManager
```

```kotlin
// Runtime adaptation
private fun setupGridColumns() {
    val screenWidth = resources.displayMetrics.widthPixels
    val density = resources.displayMetrics.density
    val screenWidthDp = screenWidth / density
    
    val columns = when {
        screenWidthDp >= 720 -> 4 // Large tablet
        screenWidthDp >= 600 -> 3 // Tablet
        else -> 2 // Phone
    }
    
    val layoutManager = GridLayoutManager(this, columns)
    recyclerView.layoutManager = layoutManager
}
```

## Advanced Challenges

### Challenge 1: Master-Detail Layout

**Objective**: Create a master-detail layout that adapts to screen size.

**Requirements**:
- Phone: Show list, tap to show detail in new activity
- Tablet: Show list and detail side by side
- Handle configuration changes properly

**Solution Structure**:
```kotlin
// MainActivity.kt
class MainActivity : AppCompatActivity() {
    
    private var isTablet = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if we're on a tablet
        isTablet = resources.getBoolean(R.bool.is_tablet)
        
        if (isTablet) {
            setContentView(R.layout.activity_main_tablet)
            setupTabletLayout()
        } else {
            setContentView(R.layout.activity_main_phone)
            setupPhoneLayout()
        }
    }
    
    private fun setupTabletLayout() {
        // Setup master-detail fragments
        supportFragmentManager.beginTransaction()
            .replace(R.id.master_container, MasterFragment())
            .replace(R.id.detail_container, DetailFragment())
            .commit()
    }
    
    private fun setupPhoneLayout() {
        // Setup list fragment only
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MasterFragment())
            .commit()
    }
}
```

```xml
<!-- res/values/bools.xml -->
<resources>
    <bool name="is_tablet">false</bool>
</resources>

<!-- res/values-sw600dp/bools.xml -->
<resources>
    <bool name="is_tablet">true</bool>
</resources>
```

### Challenge 2: Responsive Navigation

**Objective**: Create navigation that adapts to screen size.

**Requirements**:
- Phone: Bottom navigation or drawer
- Tablet: Top navigation tabs
- Large tablet: Side navigation

**Solution**:
```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupNavigation()
    }
    
    private fun setupNavigation() {
        val screenWidth = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        val screenWidthDp = screenWidth / density
        
        when {
            screenWidthDp >= 720 -> setupSideNavigation()
            screenWidthDp >= 600 -> setupTopNavigation()
            else -> setupBottomNavigation()
        }
    }
    
    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setOnItemSelectedListener { item ->
            // Handle navigation
            true
        }
    }
    
    private fun setupTopNavigation() {
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        
        // Setup tabs and view pager
    }
    
    private fun setupSideNavigation() {
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { item ->
            // Handle navigation
            true
        }
    }
}
```

### Challenge 3: Dynamic Content Adaptation

**Objective**: Create a layout that dynamically shows/hides content based on screen size.

**Requirements**:
- Show full content on large screens
- Show condensed content on small screens
- Animate transitions between states

**Solution**:
```kotlin
class AdaptiveContentActivity : AppCompatActivity() {
    
    private lateinit var contentContainer: View
    private lateinit var condensedContent: View
    private lateinit var fullContent: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adaptive_content)
        
        initializeViews()
        setupContentAdaptation()
    }
    
    private fun initializeViews() {
        contentContainer = findViewById(R.id.content_container)
        condensedContent = findViewById(R.id.condensed_content)
        fullContent = findViewById(R.id.full_content)
    }
    
    private fun setupContentAdaptation() {
        val screenWidth = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        val screenWidthDp = screenWidth / density
        
        if (screenWidthDp >= 600) {
            showFullContent()
        } else {
            showCondensedContent()
        }
    }
    
    private fun showFullContent() {
        condensedContent.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                condensedContent.visibility = View.GONE
                fullContent.visibility = View.VISIBLE
                fullContent.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
    
    private fun showCondensedContent() {
        fullContent.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                fullContent.visibility = View.GONE
                condensedContent.visibility = View.VISIBLE
                condensedContent.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
}
```

## Testing Challenges

### Challenge 4: Comprehensive Testing

**Objective**: Create a comprehensive testing strategy for responsive design.

**Requirements**:
- Test on multiple screen sizes
- Test orientation changes
- Test configuration changes
- Test accessibility features

**Solution**:
```kotlin
// Test class for responsive design
@RunWith(AndroidJUnit4::class)
class ResponsiveDesignTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun testPhoneLayout() {
        // Test on phone-sized screen
        onView(withId(R.id.profile_image))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.name_text))
            .check(matches(withText("John Doe")))
    }
    
    @Test
    fun testTabletLayout() {
        // Test on tablet-sized screen
        // This would require setting up a tablet configuration
    }
    
    @Test
    fun testOrientationChange() {
        // Test layout adaptation on rotation
        onView(withId(R.id.profile_image))
            .check(matches(isDisplayed()))
        
        // Rotate device
        // Verify layout still works
    }
    
    @Test
    fun testAccessibility() {
        // Test with large text enabled
        // Test with high contrast enabled
        // Test with screen reader
    }
}
```

## Submission Guidelines

For each exercise/challenge:

1. **Create a new branch** for your solution
2. **Implement the solution** following best practices
3. **Test thoroughly** on multiple configurations
4. **Document your approach** in comments
5. **Submit a pull request** with your solution

## Evaluation Criteria

- **Correctness**: Solution works as expected
- **Responsiveness**: Adapts properly to different screen sizes
- **Performance**: Efficient implementation
- **Code Quality**: Clean, readable, maintainable code
- **Testing**: Comprehensive testing approach
- **Documentation**: Clear comments and documentation

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes)
- [Material Design Guidelines](https://material.io/design/layout/responsive-layout-grid.html)
- [ConstraintLayout Examples](https://github.com/android/views-widgets-samples)

Complete these exercises to master responsive design in Android!
