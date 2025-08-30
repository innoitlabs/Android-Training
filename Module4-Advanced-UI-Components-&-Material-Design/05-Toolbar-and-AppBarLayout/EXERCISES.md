# Android Toolbar and AppBarLayout - Exercises

## Exercise Levels

This document contains exercises of varying difficulty to help you master Android Toolbar and AppBarLayout concepts.

## Easy Level Exercises

### Exercise 1: Customize Toolbar Colors
**Objective**: Change the Toolbar appearance by modifying colors and styling.

**Tasks**:
1. Change the Toolbar background color to a different Material Design color
2. Modify the title text color
3. Update the status bar color to match the Toolbar

**Implementation**:
```xml
<!-- In activity_main.xml -->
<androidx.appcompat.widget.Toolbar
    android:background="@color/teal_500"
    app:titleTextColor="@color/white"/>
```

```xml
<!-- In themes.xml -->
<style name="Theme.ToolbarAndAppBarLayout" parent="Base.Theme.ToolbarAndAppBarLayout">
    <item name="android:statusBarColor">@color/teal_700</item>
</style>
```

### Exercise 2: Add Different Menu Icons
**Objective**: Replace the default menu icons with custom ones.

**Tasks**:
1. Create custom drawable resources for menu icons
2. Update the menu XML to use custom icons
3. Test the new menu appearance

**Implementation**:
```xml
<!-- Create custom drawable resources in res/drawable/ -->
<!-- Update toolbar_menu.xml -->
<item
    android:id="@+id/action_settings"
    android:title="Settings"
    android:icon="@drawable/ic_settings_custom"
    app:showAsAction="ifRoom"/>
```

### Exercise 3: Modify Scroll Behavior
**Objective**: Experiment with different scroll flags to understand their effects.

**Tasks**:
1. Change `scroll|enterAlways` to `scroll|enterAlwaysCollapsed`
2. Test `exitUntilCollapsed` behavior
3. Add `snap` flag to see snapping behavior

**Implementation**:
```xml
<androidx.appcompat.widget.Toolbar
    app:layout_scrollFlags="scroll|enterAlwaysCollapsed|snap"/>
```

## Intermediate Level Exercises

### Exercise 4: Multiple Menu Actions
**Objective**: Add more menu items and handle their actions properly.

**Tasks**:
1. Add a "Profile" menu item with custom icon
2. Add a "Notifications" menu item
3. Implement proper action handling for new menu items
4. Add menu item badges (notification count)

**Implementation**:
```xml
<!-- Add to toolbar_menu.xml -->
<item
    android:id="@+id/action_profile"
    android:title="Profile"
    android:icon="@drawable/ic_person"
    app:showAsAction="ifRoom"/>

<item
    android:id="@+id/action_notifications"
    android:title="Notifications"
    android:icon="@drawable/ic_notifications"
    app:showAsAction="ifRoom"/>
```

```kotlin
// Add to onOptionsItemSelected()
R.id.action_profile -> {
    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
    true
}
R.id.action_notifications -> {
    Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
    true
}
```

### Exercise 5: Different Scroll Behaviors
**Objective**: Implement various scroll behaviors for different use cases.

**Tasks**:
1. Create a layout with `CollapsingToolbarLayout`
2. Implement parallax scrolling with an ImageView
3. Add a floating action button that responds to scrolling
4. Test different scroll flag combinations

**Implementation**:
```xml
<com.google.android.material.appbar.CollapsingToolbarLayout
    android:layout_width="match_parent"
    android:layout_height="200dp"
    app:layout_scrollFlags="scroll|exitUntilCollapsed|parallax">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:src="@drawable/header_image"
        android:scaleType="centerCrop"
        app:layout_collapseMode="parallax"/>

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        app:layout_collapseMode="pin"/>

</com.google.android.material.appbar.CollapsingToolbarLayout>
```

### Exercise 6: Custom Toolbar Styling
**Objective**: Create a custom-styled Toolbar with advanced features.

**Tasks**:
1. Add a subtitle to the Toolbar
2. Implement custom navigation icon
3. Add a search view in the Toolbar
4. Create custom menu item layouts

**Implementation**:
```xml
<androidx.appcompat.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    app:title="Main Title"
    app:subtitle="Subtitle"
    app:navigationIcon="@drawable/custom_back_icon"/>
```

```kotlin
// Add search functionality
toolbar.inflateMenu(R.menu.toolbar_menu)
toolbar.setOnMenuItemClickListener { menuItem ->
    when (menuItem.itemId) {
        R.id.action_search -> {
            // Show search view
            true
        }
        else -> false
    }
}
```

## Advanced Level Exercises

### Exercise 7: CollapsingToolbarLayout with Complex Content
**Objective**: Create a sophisticated collapsing toolbar with multiple elements.

**Tasks**:
1. Implement a CollapsingToolbarLayout with multiple child views
2. Add a profile image that collapses to a small avatar
3. Include user information that animates during collapse
4. Add a floating action button that changes position during collapse

**Implementation**:
```xml
<com.google.android.material.appbar.CollapsingToolbarLayout
    android:layout_width="match_parent"
    android:layout_height="250dp"
    app:layout_scrollFlags="scroll|exitUntilCollapsed|parallax"
    app:contentScrim="?attr/colorPrimary"
    app:expandedTitleMarginStart="64dp"
    app:expandedTitleMarginBottom="16dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:gravity="center"
        app:layout_collapseMode="parallax">

        <ImageView
            android:id="@+id/profileImage"
            android:layout_width="120dp"
            android:layout_height="120dp"
            android:src="@drawable/profile_image"
            android:scaleType="centerCrop"/>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="User Name"
            android:textSize="24sp"
            android:textColor="@android:color/white"
            android:layout_marginTop="16dp"/>

    </LinearLayout>

    <androidx.appcompat.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        app:layout_collapseMode="pin"/>

</com.google.android.material.appbar.CollapsingToolbarLayout>
```

### Exercise 8: Custom Scrolling Behavior
**Objective**: Implement custom scrolling behaviors and animations.

**Tasks**:
1. Create a custom Behavior class for advanced scrolling
2. Implement custom animations during scroll
3. Add scroll-based color transitions
4. Create custom scroll callbacks

**Implementation**:
```kotlin
class CustomScrollBehavior : AppBarLayout.ScrollingViewBehavior() {
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val result = super.onDependentViewChanged(parent, child, dependency)
        
        // Custom scroll handling
        val appBarLayout = dependency as AppBarLayout
        val scrollRange = appBarLayout.totalScrollRange
        val scrollOffset = appBarLayout.scrollY
        
        // Calculate scroll percentage
        val scrollPercentage = scrollOffset.toFloat() / scrollRange.toFloat()
        
        // Apply custom animations based on scroll percentage
        child.alpha = 1f - scrollPercentage
        
        return result
    }
}
```

### Exercise 9: Dynamic Toolbar Content
**Objective**: Create a Toolbar that changes content dynamically.

**Tasks**:
1. Implement a Toolbar that shows different content based on scroll position
2. Add animated transitions between different Toolbar states
3. Create a search Toolbar that expands/collapses
4. Implement context-aware menu items

**Implementation**:
```kotlin
class DynamicToolbarActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private var isSearchMode = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_toolbar)
        
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        
        // Setup scroll listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                // Change toolbar content based on scroll
                if (dy > 0 && !isSearchMode) {
                    // Scrolling down - show search mode
                    showSearchMode()
                } else if (dy < 0 && isSearchMode) {
                    // Scrolling up - show normal mode
                    showNormalMode()
                }
            }
        })
    }
    
    private fun showSearchMode() {
        isSearchMode = true
        toolbar.title = ""
        // Inflate search view
        toolbar.inflateMenu(R.menu.search_menu)
    }
    
    private fun showNormalMode() {
        isSearchMode = false
        toolbar.title = "Dynamic Toolbar"
        toolbar.menu.clear()
        toolbar.inflateMenu(R.menu.toolbar_menu)
    }
}
```

## Challenge Projects

### Challenge 1: Social Media App Toolbar
Create a Toolbar similar to popular social media apps with:
- Profile picture that collapses to avatar
- Dynamic title based on content
- Floating action button for posting
- Search functionality
- Notification badge

### Challenge 2: E-commerce App Toolbar
Implement a Toolbar for an e-commerce app with:
- Category navigation
- Search with filters
- Shopping cart with item count
- Wishlist functionality
- User account menu

### Challenge 3: News App Toolbar
Create a news app Toolbar with:
- Category tabs that collapse
- Search functionality
- Bookmark/save articles
- Share functionality
- Dark/light theme toggle

## Testing Your Exercises

For each exercise, test the following:

1. **Functionality**: All features work as expected
2. **Performance**: Smooth scrolling and animations
3. **Accessibility**: Proper content descriptions and navigation
4. **Responsive Design**: Works on different screen sizes
5. **Material Design**: Follows Material Design guidelines

## Submission Guidelines

For each exercise:
1. Document your implementation approach
2. Include screenshots or videos of the result
3. Explain any challenges faced and solutions
4. Provide code snippets for key implementations
5. Test on multiple devices/emulators

## Resources

- [Material Design Guidelines](https://material.io/design)
- [Android Developer Documentation](https://developer.android.com/guide/topics/ui/layout/coordinatorlayout)
- [Material Components Library](https://github.com/material-components/material-components-android)
