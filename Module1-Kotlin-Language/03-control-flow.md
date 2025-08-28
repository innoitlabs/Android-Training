# Topic 3: Control Flow

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Use `if/else` expressions for decision making
- Implement `when` expressions (Kotlin's enhanced switch statement)
- Control program flow with loops (`for`, `while`, `do-while`)
- Write clean, readable conditional logic for Android development

## 📖 Explanation

### What is Control Flow?

Control flow determines the order in which your code executes. It allows your program to:
- **Make decisions** based on conditions
- **Repeat actions** multiple times
- **Choose different paths** based on data

### Kotlin's Control Flow Features

Kotlin enhances traditional control flow with:
- **Expressions instead of statements** - Control flow can return values
- **Smart casts** - Automatic type checking and casting
- **Range-based loops** - Easy iteration over ranges and collections
- **Enhanced `when` expression** - More powerful than traditional switch statements

## 💻 Code Examples

### 1. If/Else Expressions

```kotlin
fun main() {
    val age = 18
    val hasLicense = true
    
    // Basic if/else
    if (age >= 18) {
        println("You are an adult")
    } else {
        println("You are a minor")
    }
    
    // If/else as expression (returns a value)
    val status = if (age >= 18) "adult" else "minor"
    println("Status: $status")
    
    // Multiple conditions
    val canDrive = if (age >= 18 && hasLicense) {
        "Yes, you can drive"
    } else if (age >= 16 && hasLicense) {
        "You can drive with restrictions"
    } else {
        "No, you cannot drive"
    }
    println(canDrive)
    
    // Nested if statements
    val score = 85
    val grade = if (score >= 90) {
        "A"
    } else if (score >= 80) {
        "B"
    } else if (score >= 70) {
        "C"
    } else if (score >= 60) {
        "D"
    } else {
        "F"
    }
    println("Grade: $grade")
}
```

### 2. When Expression (Enhanced Switch)

```kotlin
fun main() {
    val dayOfWeek = 3
    
    // Basic when expression
    val dayName = when (dayOfWeek) {
        1 -> "Monday"
        2 -> "Tuesday"
        3 -> "Wednesday"
        4 -> "Thursday"
        5 -> "Friday"
        6 -> "Saturday"
        7 -> "Sunday"
        else -> "Invalid day"
    }
    println("Day: $dayName")
    
    // When with multiple values
    val month = 12
    val season = when (month) {
        12, 1, 2 -> "Winter"
        3, 4, 5 -> "Spring"
        6, 7, 8 -> "Summer"
        9, 10, 11 -> "Fall"
        else -> "Invalid month"
    }
    println("Season: $season")
    
    // When with ranges
    val temperature = 25
    val weatherAdvice = when {
        temperature < 0 -> "Wear a heavy coat"
        temperature < 10 -> "Wear a warm jacket"
        temperature < 20 -> "Wear a light jacket"
        temperature < 30 -> "T-shirt weather"
        else -> "Stay cool, it's hot!"
    }
    println("Weather advice: $weatherAdvice")
    
    // When with expressions
    val number = 15
    val result = when {
        number % 2 == 0 -> "Even"
        number % 3 == 0 -> "Divisible by 3"
        number % 5 == 0 -> "Divisible by 5"
        else -> "Prime or other"
    }
    println("Number $number is: $result")
}
```

### 3. For Loops

```kotlin
fun main() {
    // Loop through a range
    println("Counting from 1 to 5:")
    for (i in 1..5) {
        println("Number: $i")
    }
    
    // Loop with step (increment by 2)
    println("\nEven numbers from 0 to 10:")
    for (i in 0..10 step 2) {
        println("Even: $i")
    }
    
    // Loop in reverse
    println("\nCounting down from 5 to 1:")
    for (i in 5 downTo 1) {
        println("Countdown: $i")
    }
    
    // Loop through array
    val fruits = arrayOf("Apple", "Banana", "Orange", "Grape")
    println("\nFruits:")
    for (fruit in fruits) {
        println("I like $fruit")
    }
    
    // Loop with index
    println("\nFruits with index:")
    for ((index, fruit) in fruits.withIndex()) {
        println("$index: $fruit")
    }
    
    // Loop through string characters
    val message = "Kotlin"
    println("\nCharacters in '$message':")
    for (char in message) {
        println("Character: $char")
    }
    
    // Loop with until (exclusive range)
    println("\nNumbers from 0 to 4 (exclusive):")
    for (i in 0 until 5) {
        println("Index: $i")
    }
}
```

### 4. While and Do-While Loops

```kotlin
fun main() {
    // While loop - checks condition before executing
    var count = 0
    println("While loop example:")
    while (count < 5) {
        println("Count: $count")
        count++
    }
    
    // Do-while loop - executes at least once, then checks condition
    var number = 0
    println("\nDo-while loop example:")
    do {
        println("Number: $number")
        number++
    } while (number < 3)
    
    // Practical example: Password guessing
    val correctPassword = "kotlin123"
    var attempts = 0
    val maxAttempts = 3
    var isCorrect = false
    
    println("\nPassword guessing game:")
    while (attempts < maxAttempts && !isCorrect) {
        attempts++
        println("Attempt $attempts: Enter password:")
        // Simulating user input
        val userInput = if (attempts == 2) "kotlin123" else "wrong"
        
        if (userInput == correctPassword) {
            isCorrect = true
            println("Correct! Welcome!")
        } else {
            println("Wrong password. ${maxAttempts - attempts} attempts left.")
        }
    }
    
    if (!isCorrect) {
        println("Too many attempts. Account locked.")
    }
}
```

### 5. Break and Continue

```kotlin
fun main() {
    // Break example - exit loop early
    println("Break example:")
    for (i in 1..10) {
        if (i == 5) {
            break // Exit the loop when i equals 5
        }
        println("Number: $i")
    }
    println("Loop ended")
    
    // Continue example - skip current iteration
    println("\nContinue example:")
    for (i in 1..10) {
        if (i % 2 == 0) {
            continue // Skip even numbers
        }
        println("Odd number: $i")
    }
    
    // Nested loops with break
    println("\nNested loops with break:")
    outer@ for (i in 1..3) {
        for (j in 1..3) {
            if (i == 2 && j == 2) {
                break@outer // Break out of outer loop
            }
            println("i=$i, j=$j")
        }
    }
    
    // Practical example: Finding first even number
    val numbers = arrayOf(1, 3, 5, 8, 9, 10, 12)
    var firstEven = -1
    
    for (number in numbers) {
        if (number % 2 == 0) {
            firstEven = number
            break
        }
    }
    
    if (firstEven != -1) {
        println("First even number found: $firstEven")
    } else {
        println("No even numbers found")
    }
}
```

### 6. Practical Android Example

```kotlin
fun main() {
    // Simulating Android UI state management
    var isLoading = false
    var errorMessage: String? = null
    var data: List<String>? = null
    
    // Simulate loading data
    fun loadData() {
        isLoading = true
        errorMessage = null
        
        // Simulate network delay
        repeat(3) { attempt ->
            println("Loading attempt ${attempt + 1}...")
            
            // Simulate success on third attempt
            if (attempt == 2) {
                data = listOf("Item 1", "Item 2", "Item 3")
                isLoading = false
                println("Data loaded successfully!")
            }
        }
    }
    
    // Simulate UI state handling
    fun updateUI() {
        when {
            isLoading -> {
                println("UI: Showing loading spinner")
            }
            errorMessage != null -> {
                println("UI: Showing error: $errorMessage")
            }
            data != null -> {
                println("UI: Showing data:")
                for ((index, item) in data!!.withIndex()) {
                    println("  ${index + 1}. $item")
                }
            }
            else -> {
                println("UI: Showing empty state")
            }
        }
    }
    
    // Simulate user interaction
    println("=== Android App Simulation ===")
    updateUI() // Initial state
    
    println("\nUser clicks 'Load Data' button:")
    loadData()
    updateUI()
}
```

### 7. Advanced Control Flow Patterns

```kotlin
fun main() {
    // Smart casts with if
    val value: Any = "Hello Kotlin"
    
    if (value is String) {
        // Kotlin automatically casts value to String
        println("String length: ${value.length}")
        println("Uppercase: ${value.uppercase()}")
    }
    
    // When with smart casts
    val mixedList = listOf("Text", 42, true, 3.14)
    
    for (item in mixedList) {
        val result = when (item) {
            is String -> "String: ${item.uppercase()}"
            is Int -> "Integer: ${item * 2}"
            is Boolean -> "Boolean: ${if (item) "Yes" else "No"}"
            is Double -> "Double: ${item + 1.0}"
            else -> "Unknown type"
        }
        println(result)
    }
    
    // Range-based validation
    fun validateAge(age: Int): String {
        return when (age) {
            in 0..12 -> "Child"
            in 13..17 -> "Teenager"
            in 18..64 -> "Adult"
            in 65..120 -> "Senior"
            else -> "Invalid age"
        }
    }
    
    println("Age 25: ${validateAge(25)}")
    println("Age 15: ${validateAge(15)}")
    println("Age 70: ${validateAge(70)}")
}
```

## 🔧 Android Development Notes

### Why Control Flow Matters in Android:

1. **UI State Management**: Control flow determines what UI elements to show/hide
2. **Data Validation**: Conditional logic validates user input
3. **Navigation**: Different screens based on user state
4. **Error Handling**: Different responses for different error types

### Common Android Patterns:

```kotlin
// UI State Management
when {
    isLoading -> showLoadingSpinner()
    error != null -> showErrorMessage(error)
    data.isEmpty() -> showEmptyState()
    else -> showData(data)
}

// Permission Handling
when (permission) {
    "camera" -> requestCameraPermission()
    "location" -> requestLocationPermission()
    "storage" -> requestStoragePermission()
    else -> handleUnknownPermission()
}

// API Response Handling
when (response.code) {
    200 -> handleSuccess(response.data)
    401 -> handleUnauthorized()
    404 -> handleNotFound()
    500 -> handleServerError()
    else -> handleUnknownError()
}
```

### Best Practices for Android:

1. **Use `when` for multiple conditions** - More readable than nested if/else
2. **Use ranges for validation** - `in 0..100` is cleaner than `>= 0 && <= 100`
3. **Use smart casts** - Let Kotlin handle type checking automatically
4. **Use expressions** - Control flow that returns values is more functional

## 🎯 Exercises

### Easy Level

1. **Basic If/Else Practice**
   ```kotlin
   // Create a program that:
   // - Takes a temperature (Int)
   // - Prints "Hot" if temperature > 30
   // - Prints "Warm" if temperature is 20-30
   // - Prints "Cool" if temperature is 10-19
   // - Prints "Cold" if temperature < 10
   ```

2. **When Expression Practice**
   ```kotlin
   // Create a program that:
   // - Takes a day number (1-7)
   // - Uses when expression to return day name
   // - Handles invalid input with else
   // - Prints the result
   ```

3. **Basic Loop Practice**
   ```kotlin
   // Create a program that:
   // - Prints numbers 1 to 10 using for loop
   // - Prints even numbers 2 to 20 using for loop with step
   // - Prints "Hello" 5 times using while loop
   ```

### Intermediate Level

1. **Grade Calculator**
   ```kotlin
   // Create a grade calculator that:
   // - Takes an array of test scores (Int)
   // - Calculates average score
   // - Uses when expression to assign letter grade:
   //   A: 90-100, B: 80-89, C: 70-79, D: 60-69, F: 0-59
   // - Prints detailed report with scores, average, and grade
   ```

2. **Number Guessing Game**
   ```kotlin
   // Create a number guessing game that:
   // - Generates random number 1-100
   // - Uses while loop for multiple guesses
   // - Gives hints (higher/lower)
   // - Limits to 10 attempts
   // - Uses break when correct
   // - Shows final result
   ```

3. **Menu System**
   ```kotlin
   // Create a menu system that:
   // - Shows numbered menu options
   // - Uses when expression to handle user choice
   // - Has options: Add, Edit, Delete, View, Exit
   // - Uses do-while loop to keep showing menu
   // - Exits when user chooses Exit
   ```

### Advanced Level

1. **Data Validation System**
   ```kotlin
   // Create a validation system that:
   // - Takes user input (name, age, email)
   // - Uses when expressions for validation rules
   // - Validates: name (2-50 chars), age (0-120), email (contains @ and .)
   // - Returns validation result with specific error messages
   // - Uses smart casts for type checking
   ```

2. **Pattern Recognition**
   ```kotlin
   // Create a pattern recognition program that:
   // - Takes an array of numbers
   // - Uses when expressions to identify patterns:
   //   - All even numbers
   //   - All odd numbers
   //   - Ascending sequence
   //   - Descending sequence
   //   - Alternating pattern
   // - Prints pattern type and analysis
   ```

3. **State Machine**
   ```kotlin
   // Create a simple state machine that:
   // - Has states: IDLE, LOADING, SUCCESS, ERROR
   // - Uses when expression for state transitions
   // - Simulates API calls with different outcomes
   // - Uses loops to demonstrate state changes
   // - Handles invalid state transitions
   // - Shows state history
   ```

## 📝 Summary

### Key Takeaways:

1. **If/Else Expressions**:
   - Can return values (unlike Java statements)
   - Support multiple conditions with `else if`
   - Use curly braces for multiple statements

2. **When Expression**:
   - More powerful than traditional switch statements
   - Supports ranges, expressions, and smart casts
   - Can be used as expression or statement
   - Requires `else` branch for exhaustive matching

3. **Loops**:
   - **For**: Iterate over ranges, arrays, collections
   - **While**: Check condition before executing
   - **Do-while**: Execute at least once, then check condition
   - **Break/Continue**: Control loop execution

4. **Smart Casts**: Kotlin automatically casts types after type checks

### Best Practices:

1. **Use `when` for multiple conditions** - More readable than nested if/else
2. **Use expressions when possible** - Control flow that returns values
3. **Use ranges for validation** - `in 1..10` is cleaner than `>= 1 && <= 10`
4. **Use meaningful variable names** - Makes conditions more readable
5. **Avoid deep nesting** - Use early returns or break statements

### Android-Specific Insights:

1. **UI State**: Use `when` expressions for complex UI state management
2. **Validation**: Use ranges and smart casts for input validation
3. **Error Handling**: Use `when` for different error types and responses
4. **Navigation**: Use conditional logic for screen navigation

### Common Patterns:

```kotlin
// UI State Management
when {
    isLoading -> showLoading()
    error != null -> showError(error)
    data.isEmpty() -> showEmpty()
    else -> showData(data)
}

// Input Validation
when {
    input.isEmpty() -> "Input required"
    input.length < 3 -> "Too short"
    input.length > 50 -> "Too long"
    else -> "Valid"
}

// API Response
when (response.code) {
    200 -> handleSuccess(response.data)
    401 -> handleUnauthorized()
    404 -> handleNotFound()
    else -> handleError()
}
```

### Next Steps:
You're now ready to learn about functions and lambda expressions in the next topic, where you'll understand how to organize code into reusable blocks and work with functional programming concepts.

---

**Ready for the next topic?** Continue to [Topic 4: Functions and Lambda Expressions](04-functions-lambdas.md)
