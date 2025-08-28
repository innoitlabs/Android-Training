# Topic 2: Variables and Constants

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Understand the difference between `var` (mutable) and `val` (immutable)
- Use type inference effectively
- Apply immutability best practices
- Write clean, maintainable Kotlin code for Android development

## 📖 Explanation

### Variables vs Constants in Kotlin

Kotlin has two ways to declare variables:
1. **`var`** - Mutable variable (can be changed)
2. **`val`** - Immutable variable (cannot be changed after initialization)

### Why Immutability Matters

**Immutability** means once a value is assigned, it cannot be changed. This is important because:
- **Thread Safety**: Immutable objects are safe to share between threads
- **Predictable Code**: You know a value won't change unexpectedly
- **Fewer Bugs**: Prevents accidental modifications
- **Better Performance**: Compiler can optimize immutable code

### Type Inference

Kotlin can often guess the type of a variable based on the value you assign. This makes code cleaner and more readable.

## 💻 Code Examples

### 1. Basic Variable Declaration

```kotlin
fun main() {
    // Mutable variable (can be changed)
    var count = 0
    println("Initial count: $count")
    
    count = 5 // Changing the value
    println("Updated count: $count")
    
    count += 3 // Adding to the value
    println("After adding 3: $count")
    
    // Immutable variable (cannot be changed)
    val name = "Android Developer"
    println("Name: $name")
    
    // This would cause a compilation error:
    // name = "New Name" // Error: Val cannot be reassigned
    
    // Explicit type declaration (usually not needed)
    val age: Int = 25
    val isStudent: Boolean = true
    val height: Double = 1.75
    
    println("Age: $age, Student: $isStudent, Height: $height")
}
```

### 2. Type Inference Examples

```kotlin
fun main() {
    // Kotlin infers the type based on the value
    val number = 42 // Kotlin knows this is Int
    val decimal = 3.14 // Kotlin knows this is Double
    val text = "Hello" // Kotlin knows this is String
    val flag = true // Kotlin knows this is Boolean
    
    // You can check the inferred type
    println("number is of type: ${number::class.simpleName}")
    println("decimal is of type: ${decimal::class.simpleName}")
    println("text is of type: ${text::class.simpleName}")
    println("flag is of type: ${flag::class.simpleName}")
    
    // Sometimes you need to be explicit about the type
    val longNumber = 42L // Explicitly Long
    val floatNumber = 3.14f // Explicitly Float
    
    println("longNumber is of type: ${longNumber::class.simpleName}")
    println("floatNumber is of type: ${floatNumber::class.simpleName}")
}
```

### 3. When to Use var vs val

```kotlin
fun main() {
    // Use val for values that shouldn't change
    val PI = 3.14159
    val MAX_RETRY_COUNT = 3
    val APP_NAME = "My Android App"
    
    // Use var for values that need to change
    var currentScore = 0
    var attempts = 0
    var isGameActive = true
    
    // Game logic example
    while (isGameActive && attempts < MAX_RETRY_COUNT) {
        attempts++
        currentScore += 10
        
        println("Attempt $attempts: Score = $currentScore")
        
        if (currentScore >= 50) {
            isGameActive = false
            println("Game won!")
        }
    }
    
    println("Final score: $currentScore")
    println("Total attempts: $attempts")
}
```

### 4. Late Initialization

```kotlin
fun main() {
    // Sometimes you need to declare a variable but assign it later
    var userName: String? = null // Nullable type
    
    // Later in the code...
    userName = "John Doe"
    println("User: $userName")
    
    // Alternative: lateinit (for non-nullable types)
    lateinit var userEmail: String
    
    // You can check if lateinit variable is initialized
    if (::userEmail.isInitialized) {
        println("Email is initialized: $userEmail")
    } else {
        println("Email is not initialized yet")
    }
    
    // Assign it later
    userEmail = "john@example.com"
    
    if (::userEmail.isInitialized) {
        println("Email is now: $userEmail")
    }
}
```

### 5. Constants and Compile-Time Constants

```kotlin
// Constants declared at file level (outside any function)
const val APP_VERSION = "1.0.0"
const val API_BASE_URL = "https://api.example.com"
const val MAX_FILE_SIZE = 1024 * 1024 // 1MB in bytes

fun main() {
    // Using constants
    println("App Version: $APP_VERSION")
    println("API URL: $API_BASE_URL")
    println("Max file size: $MAX_FILE_SIZE bytes")
    
    // Constants must be:
    // 1. Declared at top level (outside functions)
    // 2. Initialized with a value known at compile time
    // 3. Of primitive types or String
    
    // This would not work (not a compile-time constant):
    // const val currentTime = System.currentTimeMillis() // Error!
    
    // But this works:
    val currentTime = System.currentTimeMillis() // Regular val
    println("Current time: $currentTime")
}
```

### 6. Variable Scope and Shadowing

```kotlin
fun main() {
    val globalVar = "I'm global to this function"
    
    // Block scope
    {
        val localVar = "I'm local to this block"
        println("Inside block: $localVar")
        println("Global var accessible: $globalVar")
    }()
    
    // This would cause an error:
    // println(localVar) // Error: localVar is not in scope
    
    // Variable shadowing (not recommended, but possible)
    val name = "Global Name"
    
    {
        val name = "Local Name" // Shadows the outer name
        println("Inside block: $name") // Prints "Local Name"
    }()
    
    println("Outside block: $name") // Prints "Global Name"
}
```

### 7. Practical Android Example

```kotlin
fun main() {
    // Simulating Android UI state
    var isButtonEnabled = true
    var currentText = "Click me!"
    var clickCount = 0
    
    val maxClicks = 5
    val buttonId = "main_button"
    
    // Simulating button click
    fun simulateButtonClick() {
        if (isButtonEnabled && clickCount < maxClicks) {
            clickCount++
            currentText = "Clicked $clickCount times!"
            
            if (clickCount >= maxClicks) {
                isButtonEnabled = false
                currentText = "Button disabled!"
            }
            
            println("Button state: enabled=$isButtonEnabled, text='$currentText'")
        }
    }
    
    // Simulate multiple clicks
    repeat(7) {
        simulateButtonClick()
    }
}
```

## 🔧 Android Development Notes

### Why Immutability is Critical in Android:

1. **UI Thread Safety**: Android UI runs on the main thread. Immutable objects prevent race conditions
2. **State Management**: Immutable state makes your app's behavior predictable
3. **Memory Efficiency**: Immutable objects can be shared safely
4. **Testing**: Immutable code is easier to test and debug

### Common Android Patterns:

```kotlin
// Good: Immutable configuration
val apiKey = "your_api_key_here"
val baseUrl = "https://api.example.com"

// Good: Mutable UI state
var isLoading = false
var currentUser: User? = null
var errorMessage: String? = null

// Good: Constants for resources
const val REQUEST_CODE_CAMERA = 1001
const val REQUEST_CODE_GALLERY = 1002
```

### Best Practices for Android:

1. **Use `val` by default** - Only use `var` when you really need to change the value
2. **Use `const val` for compile-time constants** - Especially for resource IDs, request codes
3. **Use `lateinit` for dependency injection** - When you know a value will be set before use
4. **Use nullable types (`String?`) when appropriate** - Don't force non-null when value might be null

## 🎯 Exercises

### Easy Level

1. **Variable Declaration Practice**
   ```kotlin
   // Create variables for an Android app:
   // - App name (val, String)
   // - Version code (val, Int)
   // - Is debug mode (val, Boolean)
   // - Current user score (var, Int)
   // - Is user logged in (var, Boolean)
   // 
   // Print all variables and demonstrate changing the mutable ones
   ```

2. **Type Inference Challenge**
   ```kotlin
   // Declare variables without specifying types:
   // - A number that represents your age
   // - A decimal number for your height in meters
   // - A text string with your favorite color
   // - A true/false value for whether you like programming
   // 
   // Print each variable and its inferred type
   ```

3. **Constants and Variables**
   ```kotlin
   // Create a simple calculator program:
   // - Use const val for mathematical constants (PI, E)
   // - Use val for calculator settings (max digits, decimal places)
   // - Use var for current calculation result
   // - Demonstrate using all three types
   ```

### Intermediate Level

1. **User Profile Manager**
   ```kotlin
   // Create a user profile system:
   // - User ID (val, Int) - never changes
   // - Username (var, String) - can be updated
   // - Email (var, String) - can be updated
   // - Join date (val, String) - never changes
   // - Is premium (var, Boolean) - can be upgraded
   // - Login count (var, Int) - increments with each login
   // 
   // Create functions to:
   // - Display profile
   // - Update username
   // - Upgrade to premium
   // - Record login
   ```

2. **Game State Manager**
   ```kotlin
   // Create a simple game state:
   // - Game name (const val)
   // - Max level (val)
   // - Current level (var)
   // - Score (var)
   // - Is game over (var)
   // - Player name (var)
   // 
   // Create functions to:
   // - Start new game
   // - Advance level
   // - Add score
   // - End game
   // - Display game status
   ```

3. **Configuration Manager**
   ```kotlin
   // Create an app configuration system:
   // - App version (const val)
   // - API timeout (val)
   // - Max retry attempts (val)
   // - Current theme (var) - "light" or "dark"
   // - Language (var) - "en", "es", "fr"
   // - Is notifications enabled (var)
   // 
   // Create functions to:
   // - Load default configuration
   // - Change theme
   // - Change language
   // - Toggle notifications
   // - Display current config
   ```

### Advanced Level

1. **Data Validation System**
   ```kotlin
   // Create a validation system with immutable rules:
   // - Email regex pattern (val)
   // - Password min length (val)
   // - Username allowed characters (val)
   // - Current validation errors (var, List<String>)
   // - Is form valid (var, Boolean)
   // 
   // Create functions to:
   // - Validate email
   // - Validate password
   // - Validate username
   // - Clear all errors
   // - Get validation summary
   ```

2. **Shopping Cart System**
   ```kotlin
   // Create a shopping cart with:
   // - Tax rate (const val)
   // - Shipping threshold (val)
   // - Shipping cost (val)
   // - Items in cart (var, List<String>)
   // - Total before tax (var, Double)
   // - Is checkout available (var, Boolean)
   // 
   // Create functions to:
   // - Add item
   // - Remove item
   // - Calculate total with tax
   // - Check if free shipping applies
   // - Clear cart
   // - Display cart summary
   ```

3. **State Machine**
   ```kotlin
   // Create a simple state machine:
   // - Valid states (val, List<String>)
   // - Current state (var, String)
   // - Transition history (var, List<String>)
   // - Is state valid (var, Boolean)
   // 
   // Create functions to:
   // - Change state (with validation)
   // - Get next valid states
   // - Undo last transition
   // - Display state history
   // - Reset to initial state
   ```

## 📝 Summary

### Key Takeaways:

1. **Variable Types**:
   - **`var`**: Mutable variable (can be changed)
   - **`val`**: Immutable variable (cannot be changed after initialization)
   - **`const val`**: Compile-time constant (must be at top level)

2. **Type Inference**: Kotlin can often guess the type, making code cleaner

3. **Immutability Benefits**:
   - Thread safety
   - Predictable code
   - Fewer bugs
   - Better performance

4. **Best Practices**:
   - Use `val` by default
   - Only use `var` when you need to change the value
   - Use `const val` for compile-time constants
   - Use meaningful variable names

### Android-Specific Insights:

1. **UI State**: Use `var` for UI state that changes (loading, error messages)
2. **Configuration**: Use `val` for app configuration that doesn't change
3. **Constants**: Use `const val` for resource IDs, request codes, API keys
4. **Thread Safety**: Immutable objects are safe to share between threads

### Common Patterns:

```kotlin
// Configuration (immutable)
val apiKey = "your_key"
val baseUrl = "https://api.example.com"

// State (mutable)
var isLoading = false
var currentUser: User? = null

// Constants
const val REQUEST_CODE = 1001
const val MAX_RETRIES = 3
```

### Next Steps:
You're now ready to learn about control flow in the next topic, where you'll understand how to make decisions and repeat actions in your Kotlin code.

---

**Ready for the next topic?** Continue to [Topic 3: Control Flow](03-control-flow.md)
