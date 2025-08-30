# Safe Args Exercises and Practice

## Exercise 1: Basic Safe Args Implementation (Easy)

### Objective
Create a simple app that passes basic data types between fragments using Safe Args.

### Requirements
1. Create two fragments: `InputFragment` and `DisplayFragment`
2. `InputFragment` should have:
   - EditText for name (String)
   - EditText for age (Int)
   - CheckBox for "isStudent" (Boolean)
   - Button to navigate to DisplayFragment
3. `DisplayFragment` should display all passed data

### Navigation Graph
```xml
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:startDestination="@id/inputFragment">

    <fragment
        android:id="@+id/inputFragment"
        android:name="com.example.safeargs.InputFragment"
        android:label="Input">
        <action
            android:id="@+id/action_inputFragment_to_displayFragment"
            app:destination="@id/displayFragment" />
    </fragment>

    <fragment
        android:id="@+id/displayFragment"
        android:name="com.example.safeargs.DisplayFragment"
        android:label="Display">
        <argument
            android:name="name"
            app:argType="string" />
        <argument
            android:name="age"
            app:argType="integer" />
        <argument
            android:name="isStudent"
            app:argType="boolean" />
    </fragment>
</navigation>
```

### Tasks
1. Implement input validation (name not empty, age > 0)
2. Show different messages based on age range (child, teen, adult)
3. Add a "Back" button in DisplayFragment

---

## Exercise 2: Complex Object Passing (Intermediate)

### Objective
Pass complex objects between fragments using Parcelable.

### Requirements
1. Create a `Product` data class with:
   - id (Int)
   - name (String)
   - price (Double)
   - category (String)
   - isAvailable (Boolean)
2. Create `ProductListFragment` and `ProductDetailFragment`
3. Pass Product object between fragments

### Product Data Class
```kotlin
@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val isAvailable: Boolean
) : Parcelable
```

### Tasks
1. Create a list of sample products
2. Display products in a RecyclerView
3. Pass selected product to detail fragment
4. Show product details with formatting
5. Add "Add to Cart" functionality (just UI, no backend)

---

## Exercise 3: Multi-Fragment Navigation (Advanced)

### Objective
Create a multi-step form with Safe Args navigation.

### Requirements
1. Create three fragments:
   - `PersonalInfoFragment` (name, email, phone)
   - `AddressFragment` (street, city, zip)
   - `SummaryFragment` (display all collected info)
2. Navigate between fragments with data preservation
3. Allow back navigation with data retention

### Navigation Flow
```
PersonalInfoFragment → AddressFragment → SummaryFragment
```

### Tasks
1. Implement form validation at each step
2. Show progress indicator
3. Allow editing previous steps
4. Add "Submit" functionality in SummaryFragment
5. Handle configuration changes (rotation)

---

## Exercise 4: Search and Filter (Advanced)

### Objective
Implement search functionality with Safe Args.

### Requirements
1. Create `SearchFragment` with search input
2. Create `ResultsFragment` to display search results
3. Pass search query and filters between fragments

### Features
1. Search by name, category, or price range
2. Filter by availability
3. Sort by price or name
4. Pagination support (optional)

---

## Exercise 5: Deep Linking (Expert)

### Objective
Implement deep linking with Safe Args.

### Requirements
1. Create deep links for product details
2. Handle external app launches
3. Navigate directly to specific fragments

### Implementation
1. Add deep link URIs to navigation graph
2. Handle intent filters in AndroidManifest
3. Test with adb commands

---

## Practice Projects

### Project 1: E-commerce App
Build a simple e-commerce app with:
- Product listing
- Product details
- Shopping cart
- User profile
- Order history

Use Safe Args for all navigation between these features.

### Project 2: Social Media App
Create a social media app with:
- User profiles
- Post details
- Comments
- User search
- Settings

Implement Safe Args for all data passing.

### Project 3: Task Manager
Build a task management app with:
- Task list
- Task details
- Task creation/editing
- Category management
- Task filtering

Use Safe Args for task data and filtering parameters.

---

## Testing Your Implementation

### Manual Testing Checklist
- [ ] Navigation works correctly
- [ ] Data is passed accurately
- [ ] Back navigation preserves data
- [ ] Input validation works
- [ ] UI updates properly
- [ ] No crashes on rotation
- [ ] Deep links work (if implemented)

### Automated Testing
Create unit tests for:
1. Safe Args generated classes
2. Fragment argument handling
3. Navigation logic
4. Data validation

### Example Test
```kotlin
@Test
fun testSafeArgsNavigation() {
    val action = InputFragmentDirections
        .actionInputFragmentToDisplayFragment("John", 25, true)
    
    assertEquals("John", action.name)
    assertEquals(25, action.age)
    assertTrue(action.isStudent)
}
```

---

## Common Mistakes to Avoid

1. **Forgetting to rebuild** after navigation graph changes
2. **Wrong argument types** in navigation graph
3. **Missing required arguments** when navigating
4. **Not handling null values** for optional arguments
5. **Passing large objects** that could impact performance
6. **Not validating input** before navigation

## Tips for Success

1. **Start simple** with basic data types
2. **Test frequently** as you build
3. **Use meaningful argument names**
4. **Provide default values** for optional arguments
5. **Handle edge cases** in your validation
6. **Document your navigation flow**

---

## Submission Guidelines

For each exercise, submit:
1. Complete source code
2. Screenshots of the app running
3. Brief explanation of your implementation
4. Any challenges you faced and how you solved them

## Resources

- [Navigation Component Documentation](https://developer.android.com/guide/navigation)
- [Safe Args Documentation](https://developer.android.com/guide/navigation/navigation-pass-data#Safe-args)
- [Parcelable Documentation](https://developer.android.com/reference/android/os/Parcelable)
