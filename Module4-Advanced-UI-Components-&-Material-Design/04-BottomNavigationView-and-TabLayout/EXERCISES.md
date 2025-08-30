# Exercises: BottomNavigationView and TabLayout

## Easy Level Exercises

### Exercise 1: Add a Fourth Tab
**Objective**: Add a fourth tab to the TabLayout in the HomeFragment.

**Steps**:
1. Open `ViewPagerAdapter.kt`
2. Add a fourth fragment to the `fragments` list
3. Update the `getItemCount()` method
4. Test the app to see the new tab

**Expected Result**: The TabLayout should now show 4 tabs instead of 3.

### Exercise 2: Change Bottom Navigation Icons
**Objective**: Replace the default Android icons with custom icons.

**Steps**:
1. Add custom drawable resources to `res/drawable/`
2. Update `bottom_nav_menu.xml` to use custom icons
3. Test the app to see the new icons

**Hint**: You can use Material Design icons or create custom vector drawables.

### Exercise 3: Add Custom Colors to Tabs
**Objective**: Customize the tab colors and indicators.

**Steps**:
1. Open `fragment_home.xml`
2. Add custom colors to the TabLayout
3. Customize the tab indicator color
4. Test the visual changes

**Code Example**:
```xml
<com.google.android.material.tabs.TabLayout
    android:id="@+id/tabLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tabIndicatorColor="@color/your_color"
    app:tabSelectedTextColor="@color/your_color"/>
```

## Intermediate Level Exercises

### Exercise 4: Implement Tab Badges
**Objective**: Add notification badges to tabs.

**Steps**:
1. Modify `HomeFragment.kt`
2. Add badge functionality to tabs
3. Test with different badge counts

**Code Example**:
```kotlin
val tab = tabLayout.getTabAt(0)
tab?.orCreateBadge?.apply {
    number = 5
    isVisible = true
}
```

### Exercise 5: Add Custom Tab Indicators
**Objective**: Create custom tab indicator styles.

**Steps**:
1. Create custom drawable for tab indicators
2. Apply custom indicator to TabLayout
3. Test different indicator styles

**Code Example**:
```xml
<com.google.android.material.tabs.TabLayout
    app:tabIndicator="@drawable/custom_indicator"
    app:tabIndicatorHeight="4dp"/>
```

### Exercise 6: Create Custom Tab Layouts
**Objective**: Design custom tab layouts with icons and text.

**Steps**:
1. Create custom tab layout XML
2. Modify `TabLayoutMediator` to use custom layouts
3. Test the custom tab appearance

**Code Example**:
```kotlin
TabLayoutMediator(tabLayout, viewPager) { tab, position ->
    tab.setCustomView(R.layout.custom_tab_layout)
    // Set custom content
}.attach()
```

## Advanced Level Exercises

### Exercise 7: Add Dynamic Data Loading
**Objective**: Implement RecyclerView in one of the tabs.

**Steps**:
1. Create a new fragment with RecyclerView
2. Implement RecyclerView adapter
3. Add sample data
4. Replace one of the TabFragments with the new fragment

**Requirements**:
- Create a data class for your items
- Implement RecyclerView.Adapter
- Add sample data to display

### Exercise 8: Implement Tab State Persistence
**Objective**: Save and restore tab state when the app is restarted.

**Steps**:
1. Use SharedPreferences to save current tab
2. Restore tab state in `onCreate`
3. Handle configuration changes

**Code Example**:
```kotlin
// Save state
viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        // Save position to SharedPreferences
    }
})
```

### Exercise 9: Add Tab Animations and Transitions
**Objective**: Implement smooth animations between tabs.

**Steps**:
1. Add custom page transformers to ViewPager2
2. Implement tab transition animations
3. Add fragment transition animations

**Code Example**:
```kotlin
viewPager.setPageTransformer { page, position ->
    // Implement custom page transformation
    page.alpha = 1 - abs(position)
    page.translationX = page.width * position
}
```

## Challenge Exercise

### Exercise 10: Build a Complete News App
**Objective**: Create a news app using BottomNavigationView and TabLayout.

**Requirements**:
- **BottomNavigationView**: Home, Categories, Bookmarks, Profile
- **Home Tab**: TabLayout with "Latest", "Trending", "Featured" tabs
- **Categories Tab**: Different news categories
- **Bookmarks Tab**: Saved articles
- **Profile Tab**: User settings

**Features to Implement**:
1. RecyclerView for news articles
2. Card-based layouts
3. Swipe-to-refresh functionality
4. Search functionality
5. Article detail views

## Testing Your Solutions

### Manual Testing Checklist:
- [ ] All navigation works smoothly
- [ ] No crashes or errors
- [ ] UI elements are properly aligned
- [ ] Animations are smooth
- [ ] State is preserved correctly

### Code Quality Checklist:
- [ ] Code follows Kotlin conventions
- [ ] Proper error handling
- [ ] Efficient resource usage
- [ ] Clean and readable code
- [ ] Proper documentation

## Submission Guidelines

For each exercise:
1. **Document your changes**: Explain what you modified and why
2. **Include screenshots**: Show before and after results
3. **Test thoroughly**: Ensure no regressions
4. **Follow best practices**: Use Material Design guidelines

## Resources

- [Material Design Guidelines](https://material.io/design)
- [Android Developer Documentation](https://developer.android.com/guide)
- [ViewPager2 Documentation](https://developer.android.com/jetpack/androidx/releases/viewpager2)
- [TabLayout Documentation](https://developer.android.com/reference/com/google/android/material/tabs/TabLayout)

## Tips for Success

1. **Start Simple**: Begin with easy exercises and gradually increase complexity
2. **Test Incrementally**: Test each small change before moving forward
3. **Use Logs**: Add logging to debug issues
4. **Follow Patterns**: Use established Android patterns and conventions
5. **Ask Questions**: Don't hesitate to seek help when stuck

## Next Steps

After completing these exercises:
1. Explore Navigation Component for more advanced navigation
2. Learn about Navigation Drawer
3. Study Material Design components
4. Practice with real-world app scenarios
5. Contribute to open-source Android projects
