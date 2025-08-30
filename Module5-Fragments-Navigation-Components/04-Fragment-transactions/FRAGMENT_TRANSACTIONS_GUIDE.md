# Fragment Transactions Complete Guide

## Understanding Fragment Transactions

Fragment transactions are the mechanism by which you add, replace, or remove fragments from an activity. They provide a way to build dynamic and flexible user interfaces.

## Core Components

### 1. FragmentManager
- Manages fragments within an activity
- Handles the back stack
- Provides transaction capabilities

### 2. FragmentTransaction
- Represents a set of changes to be made to fragments
- Must be committed to take effect
- Can be added to back stack

## Transaction Operations

### Adding Fragments
```kotlin
supportFragmentManager.beginTransaction()
    .add(R.id.container, MyFragment())
    .commit()
```

### Replacing Fragments
```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.container, NewFragment())
    .addToBackStack("fragment_tag")
    .commit()
```

### Removing Fragments
```kotlin
val fragment = supportFragmentManager.findFragmentById(R.id.container)
fragment?.let {
    supportFragmentManager.beginTransaction()
        .remove(it)
        .commit()
}
```

### Hiding/Showing Fragments
```kotlin
supportFragmentManager.beginTransaction()
    .hide(currentFragment)
    .show(newFragment)
    .addToBackStack(null)
    .commit()
```

## Back Stack Management

### Adding to Back Stack
```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.container, NewFragment())
    .addToBackStack("transaction_name") // Optional name for debugging
    .commit()
```

### Popping Back Stack
```kotlin
// Pop the last transaction
supportFragmentManager.popBackStack()

// Pop to specific transaction
supportFragmentManager.popBackStack("transaction_name", 0)

// Pop all transactions
supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
```

### Back Stack Listener
```kotlin
supportFragmentManager.addOnBackStackChangedListener {
    val backStackEntryCount = supportFragmentManager.backStackEntryCount
    Log.d("BackStack", "Current count: $backStackEntryCount")
}
```

## Transaction Lifecycle

### 1. Begin Transaction
```kotlin
val transaction = supportFragmentManager.beginTransaction()
```

### 2. Configure Operations
```kotlin
transaction
    .replace(R.id.container, NewFragment())
    .addToBackStack("new_fragment")
    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
```

### 3. Commit Transaction
```kotlin
transaction.commit() // Synchronous
// OR
transaction.commitNow() // Immediate execution
// OR
transaction.commitAllowingStateLoss() // Use with caution
```

## Advanced Features

### Custom Transitions
```kotlin
supportFragmentManager.beginTransaction()
    .setCustomAnimations(
        R.anim.slide_in_right,
        R.anim.slide_out_left,
        R.anim.slide_in_left,
        R.anim.slide_out_right
    )
    .replace(R.id.container, NewFragment())
    .addToBackStack(null)
    .commit()
```

### Fragment Tags
```kotlin
// Add with tag
supportFragmentManager.beginTransaction()
    .add(R.id.container, MyFragment(), "my_fragment_tag")
    .commit()

// Find by tag
val fragment = supportFragmentManager.findFragmentByTag("my_fragment_tag")
```

### State Loss Prevention
```kotlin
// Check if state loss is allowed
if (!supportFragmentManager.isStateSaved) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.container, NewFragment())
        .commit()
} else {
    // Handle state loss scenario
    Log.w("Fragment", "State already saved, cannot commit transaction")
}
```

## Best Practices

### 1. Transaction Timing
- Commit transactions before `onPause()`
- Use `commitAllowingStateLoss()` sparingly
- Check `isStateSaved` before committing

### 2. Back Stack Management
- Use meaningful names for back stack entries
- Don't add too many entries to prevent memory issues
- Consider using `POP_BACK_STACK_INCLUSIVE` for cleanup

### 3. Fragment Lifecycle
- Understand fragment lifecycle during transactions
- Handle configuration changes properly
- Clean up resources in `onDestroyView()`

### 4. Performance
- Batch multiple operations in single transaction
- Avoid unnecessary fragment recreations
- Use `commitNow()` for immediate execution when needed

## Common Patterns

### Navigation Pattern
```kotlin
fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    val transaction = supportFragmentManager.beginTransaction()
        .replace(R.id.container, fragment)
    
    if (addToBackStack) {
        transaction.addToBackStack(fragment.javaClass.simpleName)
    }
    
    transaction.commit()
}
```

### Fragment Factory Pattern
```kotlin
object FragmentFactory {
    fun createHomeFragment(): HomeFragment {
        return HomeFragment()
    }
    
    fun createDetailFragment(data: String): DetailFragment {
        return DetailFragment.newInstance(data)
    }
}
```

### Transaction Builder Pattern
```kotlin
class FragmentTransactionBuilder(private val fragmentManager: FragmentManager) {
    private var transaction = fragmentManager.beginTransaction()
    
    fun replace(containerId: Int, fragment: Fragment): FragmentTransactionBuilder {
        transaction = transaction.replace(containerId, fragment)
        return this
    }
    
    fun addToBackStack(name: String?): FragmentTransactionBuilder {
        transaction = transaction.addToBackStack(name)
        return this
    }
    
    fun commit() {
        transaction.commit()
    }
}
```

## Error Handling

### Common Issues and Solutions

1. **IllegalStateException: Can not perform this action after onSaveInstanceState**
   ```kotlin
   if (!supportFragmentManager.isStateSaved) {
       // Perform transaction
   }
   ```

2. **Fragment not found**
   ```kotlin
   val fragment = supportFragmentManager.findFragmentById(R.id.container)
   if (fragment != null) {
       // Fragment exists
   }
   ```

3. **Back stack empty**
   ```kotlin
   if (supportFragmentManager.backStackEntryCount > 0) {
       supportFragmentManager.popBackStack()
   }
   ```

## Testing Fragment Transactions

### Unit Testing
```kotlin
@Test
fun testFragmentTransaction() {
    val fragment = HomeFragment()
    val transaction = fragmentManager.beginTransaction()
        .replace(R.id.container, fragment)
        .addToBackStack("test")
    
    transaction.commit()
    
    assertEquals(1, fragmentManager.backStackEntryCount)
    assertNotNull(fragmentManager.findFragmentById(R.id.container))
}
```

### Integration Testing
```kotlin
@Test
fun testFragmentNavigation() {
    // Navigate to detail fragment
    onView(withId(R.id.navigate_button)).perform(click())
    
    // Verify detail fragment is displayed
    onView(withId(R.id.detail_text)).check(matches(isDisplayed()))
    
    // Navigate back
    pressBack()
    
    // Verify home fragment is displayed
    onView(withId(R.id.home_text)).check(matches(isDisplayed()))
}
```

This guide provides comprehensive coverage of fragment transactions, from basic operations to advanced patterns and best practices.
