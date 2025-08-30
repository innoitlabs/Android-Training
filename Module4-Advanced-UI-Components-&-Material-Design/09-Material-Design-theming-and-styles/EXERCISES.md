# Material Design Theming Exercises

This document contains hands-on exercises to practice and master Material Design theming concepts.

## Exercise 1: Color Customization (Easy)

**Objective**: Change the primary color scheme of the app.

**Tasks**:
1. Open `app/src/main/res/values/colors.xml`
2. Change the primary colors from purple to blue:
   - `purple_500` → `blue_500`
   - `purple_700` → `blue_700`
   - `purple_200` → `blue_200`
3. Update `themes.xml` to use the new blue colors
4. Test the app and observe the color changes

**Expected Outcome**: The app should now use blue as the primary color instead of purple.

**Learning Points**:
- Understanding color inheritance in themes
- How primary colors affect the entire app
- Material Design color system

---

## Exercise 2: Custom Button Styles (Easy)

**Objective**: Create a new button style with different styling.

**Tasks**:
1. Open `app/src/main/res/values/styles.xml`
2. Add a new button style called `Widget.MaterialDesign.Button.Rounded`:
   ```xml
   <style name="Widget.MaterialDesign.Button.Rounded" parent="Widget.Material3.Button">
       <item name="cornerRadius">32dp</item>
       <item name="android:padding">20dp</item>
       <item name="android:textSize">18sp</item>
       <item name="android:textStyle">bold</item>
   </style>
   ```
3. Apply this style to the "Save Changes" button in the layout
4. Test the app and observe the new button appearance

**Expected Outcome**: The "Save Changes" button should have larger padding, rounded corners, and bold text.

**Learning Points**:
- Style inheritance in Material Design
- Customizing button appearance
- Applying styles to specific components

---

## Exercise 3: Text Style Variations (Intermediate)

**Objective**: Create custom text styles for different content types.

**Tasks**:
1. Add new text styles to `styles.xml`:
   ```xml
   <style name="TextAppearance.MaterialDesign.Quote" parent="TextAppearance.Material3.BodyLarge">
       <item name="android:textColor">@color/teal_700</item>
       <item name="android:textStyle">italic</item>
       <item name="android:lineSpacingMultiplier">1.3</item>
   </style>
   
   <style name="TextAppearance.MaterialDesign.Code" parent="TextAppearance.Material3.BodyMedium">
       <item name="android:fontFamily">monospace</item>
       <item name="android:background">@color/gray_100</item>
       <item name="android:padding">8dp</item>
   </style>
   ```
2. Apply these styles to new TextViews in the layout
3. Test the app and observe the different text appearances

**Expected Outcome**: Different text styles should be visible with distinct typography and formatting.

**Learning Points**:
- Material Design typography system
- Creating semantic text styles
- Text styling best practices

---

## Exercise 4: Card Style Variations (Intermediate)

**Objective**: Create different card styles for various content types.

**Tasks**:
1. Add new card styles to `styles.xml`:
   ```xml
   <style name="Widget.MaterialDesign.Card.Compact" parent="Widget.MaterialDesign.Card">
       <item name="contentPadding">12dp</item>
       <item name="cardElevation">4dp</item>
   </style>
   
   <style name="Widget.MaterialDesign.Card.Highlight" parent="Widget.MaterialDesign.Card">
       <item name="cardElevation">16dp</item>
       <item name="cardCornerRadius">20dp</item>
       <item name="strokeColor">@color/purple_500</item>
       <item name="strokeWidth">2dp</item>
   </style>
   ```
2. Apply these styles to different cards in the layout
3. Test the app and observe the visual hierarchy

**Expected Outcome**: Different cards should have distinct elevations, padding, and styling.

**Learning Points**:
- Material Design elevation system
- Visual hierarchy in UI design
- Card styling variations

---

## Exercise 5: Dark Theme Customization (Intermediate)

**Objective**: Customize the dark theme colors and observe changes.

**Tasks**:
1. Open `app/src/main/res/values-night/colors.xml`
2. Change the dark theme colors:
   - Use different shades of blue for dark theme
   - Adjust background colors for better contrast
3. Update `values-night/themes.xml` accordingly
4. Test the app in dark mode and observe the changes

**Expected Outcome**: The dark theme should have a different color scheme while maintaining good contrast.

**Learning Points**:
- Dark theme color considerations
- Contrast and accessibility
- Theme-specific color management

---

## Exercise 6: Dynamic Theming Enhancement (Advanced)

**Objective**: Add more interactive theme features.

**Tasks**:
1. Add a new button to switch between different color schemes
2. Implement color scheme switching in MainActivity:
   ```kotlin
   private fun switchColorScheme(scheme: String) {
       when (scheme) {
           "purple" -> // Apply purple theme
           "blue" -> // Apply blue theme
           "green" -> // Apply green theme
       }
   }
   ```
3. Add color scheme buttons to the settings card
4. Test the dynamic color switching

**Expected Outcome**: Users should be able to switch between different color schemes dynamically.

**Learning Points**:
- Dynamic theme switching
- Runtime theme customization
- User preference management

---

## Exercise 7: Accessibility Enhancements (Advanced)

**Objective**: Improve accessibility features.

**Tasks**:
1. Add content descriptions to all interactive elements
2. Implement high contrast mode support
3. Add large text support:
   ```xml
   <style name="TextAppearance.MaterialDesign.Title.Large" parent="TextAppearance.MaterialDesign.Title">
       <item name="android:textSize">24sp</item>
   </style>
   ```
4. Test with accessibility tools

**Expected Outcome**: The app should be more accessible to users with disabilities.

**Learning Points**:
- Accessibility best practices
- Inclusive design principles
- Testing accessibility features

---

## Exercise 8: Material You Integration (Advanced)

**Objective**: Implement Material You dynamic colors (Android 12+).

**Tasks**:
1. Update the theme to support Material You:
   ```xml
   <style name="Theme.MaterialDesign" parent="Theme.Material3.DayNight.NoActionBar">
       <item name="colorPrimary">?attr/colorPrimary</item>
       <item name="colorSecondary">?attr/colorSecondary</item>
   </style>
   ```
2. Add dynamic color support in MainActivity
3. Test on Android 12+ devices

**Expected Outcome**: The app should adapt to the user's wallpaper colors on supported devices.

**Learning Points**:
- Material You implementation
- Dynamic color system
- Android 12+ features

---

## Exercise 9: Custom Theme Attributes (Advanced)

**Objective**: Create custom theme attributes for brand-specific styling.

**Tasks**:
1. Define custom attributes in `res/values/attrs.xml`:
   ```xml
   <declare-styleable name="CustomTheme">
       <attr name="brandColor" format="color" />
       <attr name="brandFont" format="string" />
   </declare-styleable>
   ```
2. Use these attributes in your theme
3. Apply custom styling based on these attributes

**Expected Outcome**: Custom theme attributes should be available for brand-specific styling.

**Learning Points**:
- Custom theme attributes
- Brand consistency
- Advanced theming techniques

---

## Exercise 10: Performance Optimization (Advanced)

**Objective**: Optimize theme and style performance.

**Tasks**:
1. Analyze theme inheritance hierarchy
2. Optimize style definitions to reduce redundancy
3. Implement efficient theme switching
4. Profile the app for theme-related performance issues

**Expected Outcome**: The app should have optimized theme performance with minimal overhead.

**Learning Points**:
- Theme performance optimization
- Style inheritance efficiency
- Profiling and optimization techniques

---

## Bonus Challenge: Complete App Redesign

**Objective**: Redesign the entire app with a new theme and style system.

**Tasks**:
1. Create a completely new color palette
2. Design new typography system
3. Create custom component styles
4. Implement comprehensive dark theme
5. Add animations and transitions
6. Test across different devices and configurations

**Expected Outcome**: A completely redesigned app with a cohesive and modern appearance.

**Learning Points**:
- Complete theme system design
- Design system implementation
- Cross-device compatibility
- User experience optimization

---

## Testing Guidelines

For each exercise, test the following:

1. **Visual Consistency**: Ensure all elements follow the same design language
2. **Dark/Light Mode**: Verify both themes work correctly
3. **Accessibility**: Test with screen readers and accessibility tools
4. **Performance**: Ensure no performance degradation
5. **Cross-Device**: Test on different screen sizes and densities
6. **User Experience**: Verify intuitive and pleasant user experience

## Submission Guidelines

For each completed exercise:

1. Document the changes made
2. Include screenshots of before/after
3. Explain the learning outcomes
4. Note any challenges encountered
5. Suggest improvements or variations

## Resources

- [Material Design Guidelines](https://material.io/design)
- [Material Components Documentation](https://material.io/components)
- [Android Theme Documentation](https://developer.android.com/guide/topics/ui/themes)
- [Material Design Color Tool](https://material.io/resources/color/)

---

**Happy Learning! 🎨**
