# ViewPager2 and TabLayout Exercises

## Exercise 1: Basic Implementation (Beginner)

### Task
Create a basic ViewPager2 with TabLayout that displays three fragments with different colored backgrounds.

### Requirements
- Three tabs: Home, Dashboard, Profile
- Each fragment should have a distinct background color
- Implement swipe navigation
- Implement tab tap navigation

### Expected Output
- App launches with three tabs visible
- Swiping left/right changes fragments
- Tapping tabs changes fragments
- Each fragment has a different colored background

### Solution Hints
1. Use `FragmentStateAdapter` for the ViewPager2
2. Use `TabLayoutMediator` to sync TabLayout with ViewPager2
3. Set different background colors in fragment layouts

---

## Exercise 2: Adding Icons to Tabs (Easy)

### Task
Enhance the basic implementation by adding icons to each tab.

### Requirements
- Add appropriate icons for each tab
- Icons should be visible alongside text
- Maintain existing functionality

### Implementation Steps
1. Add drawable resources for icons
2. Modify `TabLayoutMediator` to set icons
3. Test on different screen sizes

### Code Example
```kotlin
TabLayoutMediator(tabLayout, viewPager) { tab, position ->
    when (position) {
        0 -> {
            tab.text = "Home"
            tab.setIcon(R.drawable.ic_home)
        }
        1 -> {
            tab.text = "Dashboard"
            tab.setIcon(R.drawable.ic_dashboard)
        }
        2 -> {
            tab.text = "Profile"
            tab.setIcon(R.drawable.ic_profile)
        }
    }
}.attach()
```

---

## Exercise 3: Custom Tab Layouts (Intermediate)

### Task
Create custom tab layouts with more complex designs.

### Requirements
- Create custom tab item layouts
- Include icon, title, and subtitle
- Add badge support for notifications
- Implement custom tab selection styling

### Implementation Steps
1. Create custom tab item layout XML
2. Use `TabLayout.Tab.setCustomView()` method
3. Implement tab selection listeners
4. Add badge functionality

### Custom Tab Layout Example
```xml
<!-- custom_tab_item.xml -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="8dp">

    <ImageView
        android:id="@+id/tab_icon"
        android:layout_width="24dp"
        android:layout_height="24dp"
        android:layout_marginBottom="4dp"/>

    <TextView
        android:id="@+id/tab_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="12sp"
        android:textStyle="bold"/>

    <TextView
        android:id="@+id/tab_subtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="10sp"
        android:visibility="gone"/>
</LinearLayout>
```

---

## Exercise 4: Tab Selection Listeners (Intermediate)

### Task
Implement tab selection listeners to handle tab changes programmatically.

### Requirements
- Log tab selection events
- Update UI based on selected tab
- Handle tab selection from code
- Implement custom animations

### Implementation Steps
1. Implement `TabLayout.OnTabSelectedListener`
2. Add logging for tab selection events
3. Update fragment content based on selection
4. Add custom animations

### Code Example
```kotlin
tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.d("TabSelection", "Tab selected: ${tab?.text}")
        // Handle tab selection
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        // Handle tab unselection
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        // Handle tab reselection
    }
})
```

---

## Exercise 5: Dynamic Tab Management (Advanced)

### Task
Implement dynamic tab addition and removal.

### Requirements
- Add/remove tabs at runtime
- Handle adapter updates
- Maintain state during tab changes
- Implement tab reordering

### Implementation Steps
1. Create a dynamic adapter
2. Implement tab addition/removal methods
3. Handle adapter notifications
4. Preserve fragment state

### Code Example
```kotlin
class DynamicViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = mutableListOf<Fragment>()
    private val titles = mutableListOf<String>()

    fun addTab(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
        notifyItemInserted(fragments.size - 1)
    }

    fun removeTab(position: Int) {
        if (position < fragments.size) {
            fragments.removeAt(position)
            titles.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
```

---

## Exercise 6: Nested ViewPager2 (Advanced)

### Task
Implement nested ViewPager2 for complex navigation hierarchies.

### Requirements
- Create a parent ViewPager2 with tabs
- Each tab contains a child ViewPager2
- Handle nested swipe gestures
- Implement proper state management

### Implementation Steps
1. Create parent and child adapters
2. Handle nested swipe conflicts
3. Implement proper fragment management
4. Test performance with multiple levels

---

## Exercise 7: Integration with Navigation Component (Advanced)

### Task
Integrate ViewPager2 with Navigation Component for complex navigation.

### Requirements
- Use Navigation Component for fragment management
- Integrate with ViewPager2
- Handle deep linking
- Implement proper back navigation

### Implementation Steps
1. Set up Navigation Component
2. Create navigation graph
3. Integrate with ViewPager2
4. Handle navigation events

---

## Exercise 8: Performance Optimization (Advanced)

### Task
Optimize ViewPager2 performance for large datasets.

### Requirements
- Implement efficient fragment recycling
- Optimize memory usage
- Handle configuration changes
- Implement lazy loading

### Implementation Steps
1. Use `FragmentStateAdapter` properly
2. Implement fragment caching
3. Handle memory pressure
4. Optimize fragment creation

---

## Exercise 9: Accessibility (Intermediate)

### Task
Make the ViewPager2 and TabLayout accessible.

### Requirements
- Add content descriptions
- Implement proper focus management
- Support screen readers
- Handle accessibility gestures

### Implementation Steps
1. Add content descriptions to tabs
2. Implement proper focus handling
3. Test with accessibility tools
4. Add accessibility actions

---

## Exercise 10: Testing (Intermediate)

### Task
Write comprehensive tests for ViewPager2 and TabLayout.

### Requirements
- Unit tests for adapters
- Instrumented tests for UI
- Test tab selection
- Test swipe gestures

### Implementation Steps
1. Write unit tests for ViewPagerAdapter
2. Create instrumented tests for UI
3. Test tab selection listeners
4. Test fragment lifecycle

---

## Bonus Exercise: Custom Animations (Advanced)

### Task
Implement custom page transitions and tab animations.

### Requirements
- Custom page transition animations
- Tab selection animations
- Smooth gesture animations
- Performance-optimized animations

### Implementation Steps
1. Create custom page transformers
2. Implement tab selection animations
3. Optimize animation performance
4. Handle animation interruptions

---

## Submission Guidelines

### For Each Exercise
1. **Code Quality**: Clean, well-commented code
2. **Documentation**: Clear README with setup instructions
3. **Testing**: Basic functionality tests
4. **Performance**: No memory leaks or performance issues

### Evaluation Criteria
- **Functionality**: Does it work as expected?
- **Code Quality**: Is the code clean and maintainable?
- **Performance**: Are there any performance issues?
- **User Experience**: Is the UI smooth and intuitive?

### Tips for Success
1. Start with basic implementation
2. Test frequently
3. Use version control
4. Document your progress
5. Ask for help when stuck
