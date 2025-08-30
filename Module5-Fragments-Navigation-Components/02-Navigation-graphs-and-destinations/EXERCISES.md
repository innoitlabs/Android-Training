# Navigation Graphs Exercises

## Exercise 1: Basic Navigation (Easy)

### Objective
Create a simple navigation between two fragments.

### Tasks
1. Create a new fragment called `SettingsFragment`
2. Add it to the navigation graph
3. Add a button in `HomeFragment` to navigate to `SettingsFragment`
4. Implement the navigation logic

### Expected Outcome
- App should have three fragments: Home, Detail, and Settings
- Navigation should work between all fragments
- Back button should work properly

---

## Exercise 2: Data Passing (Intermediate)

### Objective
Pass different types of data between fragments using Safe Args.

### Tasks
1. Modify `DetailFragment` to accept an integer parameter (user ID)
2. Add a string parameter for user email
3. Update `HomeFragment` to pass both parameters
4. Display the received data in `DetailFragment`

### Code Example
```kotlin
// In nav_graph.xml
<fragment android:id="@+id/detailFragment">
    <argument
        android:name="userId"
        app:argType="integer" />
    <argument
        android:name="userEmail"
        app:argType="string" />
</fragment>

// In HomeFragment.kt
val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
    username = "John Doe",
    userId = 12345,
    userEmail = "john@example.com"
)
findNavController().navigate(action)
```

### Expected Outcome
- Data should be passed safely between fragments
- All parameters should be displayed correctly
- Type safety should be maintained

---

## Exercise 3: Bottom Navigation (Advanced)

### Objective
Implement bottom navigation with multiple fragments.

### Tasks
1. Create a new activity with bottom navigation
2. Add three menu items: Home, Details, Profile
3. Connect bottom navigation to navigation graph
4. Handle navigation state properly

### Code Example
```xml
<!-- bottom_nav_menu.xml -->
<menu>
    <item android:id="@+id/homeFragment" android:title="Home" />
    <item android:id="@+id/detailFragment" android:title="Details" />
    <item android:id="@+id/profileFragment" android:title="Profile" />
</menu>
```

```kotlin
// In Activity
val navController = navHostFragment.navController
bottomNav.setupWithNavController(navController)
```

### Expected Outcome
- Bottom navigation should work smoothly
- Selected tab should be highlighted
- Navigation state should be preserved

---

## Exercise 4: Deep Linking (Advanced)

### Objective
Implement deep linking to specific fragments.

### Tasks
1. Add deep link URIs to navigation graph
2. Handle incoming deep links in activity
3. Test deep linking with adb commands

### Code Example
```xml
<!-- In nav_graph.xml -->
<fragment android:id="@+id/detailFragment">
    <deepLink app:uri="myapp://detail/{username}" />
</fragment>
```

### Testing Commands
```bash
adb shell am start -W -a android.intent.action.VIEW -d "myapp://detail/JohnDoe" com.example.navigationgraphs
```

### Expected Outcome
- Deep links should open the correct fragment
- Parameters should be passed correctly
- App should handle invalid deep links gracefully

---

## Exercise 5: Navigation Animations (Intermediate)

### Objective
Add custom animations to navigation transitions.

### Tasks
1. Create animation resource files
2. Apply animations to navigation actions
3. Test different animation types

### Code Example
```xml
<!-- In nav_graph.xml -->
<action
    android:id="@+id/action_homeFragment_to_detailFragment"
    app:destination="@id/detailFragment"
    app:enterAnim="@anim/slide_in_right"
    app:exitAnim="@anim/slide_out_left"
    app:popEnterAnim="@anim/slide_in_left"
    app:popExitAnim="@anim/slide_out_right" />
```

### Expected Outcome
- Smooth animations during navigation
- Consistent animation behavior
- Proper back navigation animations

---

## Exercise 6: Navigation Testing (Advanced)

### Objective
Write unit tests for navigation logic.

### Tasks
1. Create test cases for navigation actions
2. Test Safe Args functionality
3. Mock navigation controller for testing

### Code Example
```kotlin
@Test
fun testNavigationToDetail() {
    // Given
    val scenario = launchFragmentInContainer<HomeFragment>()
    
    // When
    scenario.onFragment { fragment ->
        fragment.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
    }
    
    // Then
    // Verify navigation occurred
}
```

### Expected Outcome
- All navigation paths should be tested
- Safe Args should work correctly in tests
- Test coverage should be comprehensive

---

## Exercise 7: Navigation with Arguments (Intermediate)

### Objective
Create a form-based navigation with multiple arguments.

### Tasks
1. Create a `UserFormFragment` with input fields
2. Add navigation to a `UserProfileFragment`
3. Pass form data using Safe Args
4. Validate input before navigation

### Code Example
```kotlin
// In UserFormFragment
private fun validateAndNavigate() {
    val name = nameEditText.text.toString()
    val age = ageEditText.text.toString().toIntOrNull()
    
    if (name.isNotEmpty() && age != null) {
        val action = UserFormFragmentDirections.actionUserFormFragmentToUserProfileFragment(
            name = name,
            age = age
        )
        findNavController().navigate(action)
    } else {
        // Show error message
    }
}
```

### Expected Outcome
- Form validation should work properly
- Data should be passed correctly
- Error handling should be implemented

---

## Exercise 8: Navigation Patterns (Advanced)

### Objective
Implement common navigation patterns.

### Tasks
1. Create a master-detail flow
2. Implement a wizard-style navigation
3. Add conditional navigation based on user state

### Code Example
```kotlin
// Conditional navigation
if (isUserLoggedIn) {
    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
} else {
    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
}
```

### Expected Outcome
- Different navigation patterns should work correctly
- User state should be properly managed
- Navigation should be intuitive

---

## Submission Guidelines

### For Each Exercise:
1. **Code Implementation**: Complete the required code changes
2. **Testing**: Test the functionality thoroughly
3. **Documentation**: Add comments explaining your implementation
4. **Screenshots**: Take screenshots of the working app

### Evaluation Criteria:
- **Functionality**: Does the navigation work as expected?
- **Code Quality**: Is the code clean and well-structured?
- **User Experience**: Is the navigation intuitive?
- **Error Handling**: Are edge cases handled properly?

### Bonus Points:
- Implement additional features beyond requirements
- Add custom animations or transitions
- Create comprehensive test coverage
- Optimize performance and memory usage

---

## Resources

- [Android Navigation Component Documentation](https://developer.android.com/guide/navigation)
- [Safe Args Documentation](https://developer.android.com/guide/navigation/navigation-pass-data)
- [Navigation Testing Guide](https://developer.android.com/guide/navigation/navigation-testing)
- [Material Design Navigation Patterns](https://material.io/design/navigation/understanding-navigation.html)
