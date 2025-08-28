# Topic 4: Functions and Lambda Expressions

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Define and call functions with different parameter types
- Use single-expression functions and function expressions
- Understand higher-order functions and function types
- Work with lambda expressions and closures
- Apply functional programming concepts in Android development

## 📖 Explanation

### What are Functions?

Functions are reusable blocks of code that perform specific tasks. In Kotlin, functions are:
- **First-class citizens** - Can be assigned to variables, passed as parameters, returned from other functions
- **Expressions** - Can return values
- **Flexible** - Support default parameters, named arguments, and variable parameter lists

### What are Lambda Expressions?

Lambda expressions are anonymous functions that can be:
- **Passed as parameters** to other functions
- **Stored in variables** for later use
- **Returned from functions**
- **Used for functional programming** patterns

### Why This Matters in Android

Functions and lambdas are essential for:
- **Event handling** (button clicks, touch events)
- **Data processing** (filtering, mapping, reducing collections)
- **Asynchronous programming** (callbacks, coroutines)
- **Clean architecture** (separation of concerns)

## 💻 Code Examples

### 1. Basic Function Definition

```kotlin
fun main() {
    // Call the function
    greetUser("Alice")
    greetUser("Bob", "Good evening")
    
    // Store function result
    val result = addNumbers(5, 3)
    println("Sum: $result")
    
    // Function with multiple parameters
    val fullName = createFullName("John", "Doe")
    println("Full name: $fullName")
}

// Basic function with one parameter
fun greetUser(name: String) {
    println("Hello, $name!")
}

// Function with default parameter
fun greetUser(name: String, greeting: String = "Hello") {
    println("$greeting, $name!")
}

// Function that returns a value
fun addNumbers(a: Int, b: Int): Int {
    return a + b
}

// Function with multiple parameters
fun createFullName(firstName: String, lastName: String): String {
    return "$firstName $lastName"
}
```

### 2. Single-Expression Functions

```kotlin
fun main() {
    println("Square of 5: ${square(5)}")
    println("Is 7 even? ${isEven(7)}")
    println("Maximum of 10 and 15: ${max(10, 15)}")
    println("Greeting: ${getGreeting("Kotlin")}")
}

// Single-expression function (no return keyword needed)
fun square(number: Int) = number * number

// Single-expression function with type inference
fun isEven(number: Int) = number % 2 == 0

// Single-expression function with explicit return type
fun max(a: Int, b: Int): Int = if (a > b) a else b

// Single-expression function with string
fun getGreeting(name: String) = "Hello, $name!"
```

### 3. Function Parameters and Arguments

```kotlin
fun main() {
    // Positional arguments
    printUserInfo("Alice", 25, "alice@email.com")
    
    // Named arguments (order doesn't matter)
    printUserInfo(age = 30, email = "bob@email.com", name = "Bob")
    
    // Mix of positional and named arguments
    printUserInfo("Charlie", email = "charlie@email.com", age = 35)
    
    // Function with variable number of arguments
    println("Sum of 1, 2, 3: ${sum(1, 2, 3)}")
    println("Sum of 10, 20: ${sum(10, 20)}")
    println("Sum of single number 5: ${sum(5)}")
    
    // Function with nullable parameters
    printOptionalInfo("David", 28)
    printOptionalInfo("Eve") // email is null
}

// Function with multiple parameters
fun printUserInfo(name: String, age: Int, email: String) {
    println("Name: $name, Age: $age, Email: $email")
}

// Function with variable number of arguments (vararg)
fun sum(vararg numbers: Int): Int {
    var total = 0
    for (number in numbers) {
        total += number
    }
    return total
}

// Function with nullable parameters
fun printOptionalInfo(name: String, age: Int, email: String? = null) {
    println("Name: $name, Age: $age")
    if (email != null) {
        println("Email: $email")
    } else {
        println("Email: Not provided")
    }
}
```

### 4. Function Types and Function Variables

```kotlin
fun main() {
    // Assign function to variable
    val greetingFunction: (String) -> String = ::greet
    println(greetingFunction("World"))
    
    // Function type with multiple parameters
    val mathFunction: (Int, Int) -> Int = ::add
    println("Result: ${mathFunction(10, 5)}")
    
    // Function type with no parameters
    val getCurrentTime: () -> Long = ::getTime
    println("Current time: ${getCurrentTime()}")
    
    // Function type with no return value (Unit)
    val printFunction: (String) -> Unit = ::printMessage
    printFunction("Hello from function variable!")
    
    // Using function variables
    val operations = listOf(
        ::add,
        ::subtract,
        ::multiply
    )
    
    val a = 10
    val b = 5
    
    for (operation in operations) {
        println("${operation.name}: ${operation(a, b)}")
    }
}

// Helper functions
fun greet(name: String): String = "Hello, $name!"
fun add(a: Int, b: Int): Int = a + b
fun subtract(a: Int, b: Int): Int = a - b
fun multiply(a: Int, b: Int): Int = a * b
fun getTime(): Long = System.currentTimeMillis()
fun printMessage(message: String) = println(message)
```

### 5. Higher-Order Functions

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    // Using higher-order functions
    val evenNumbers = filterNumbers(numbers, ::isEven)
    println("Even numbers: $evenNumbers")
    
    val doubledNumbers = transformNumbers(numbers, ::double)
    println("Doubled numbers: $doubledNumbers")
    
    val sum = reduceNumbers(numbers, ::add)
    println("Sum of all numbers: $sum")
    
    // Using lambda expressions directly
    val oddNumbers = filterNumbers(numbers) { it % 2 != 0 }
    println("Odd numbers: $oddNumbers")
    
    val squaredNumbers = transformNumbers(numbers) { it * it }
    println("Squared numbers: $squaredNumbers")
    
    val product = reduceNumbers(numbers) { acc, num -> acc * num }
    println("Product of all numbers: $product")
}

// Higher-order function that takes a function as parameter
fun filterNumbers(numbers: List<Int>, predicate: (Int) -> Boolean): List<Int> {
    val result = mutableListOf<Int>()
    for (number in numbers) {
        if (predicate(number)) {
            result.add(number)
        }
    }
    return result
}

// Higher-order function for transformation
fun transformNumbers(numbers: List<Int>, transformer: (Int) -> Int): List<Int> {
    val result = mutableListOf<Int>()
    for (number in numbers) {
        result.add(transformer(number))
    }
    return result
}

// Higher-order function for reduction
fun reduceNumbers(numbers: List<Int>, reducer: (Int, Int) -> Int): Int {
    if (numbers.isEmpty()) return 0
    
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = reducer(result, numbers[i])
    }
    return result
}

// Helper functions
fun isEven(number: Int): Boolean = number % 2 == 0
fun double(number: Int): Int = number * 2
fun add(a: Int, b: Int): Int = a + b
```

### 6. Lambda Expressions

```kotlin
fun main() {
    // Basic lambda expression
    val greet: (String) -> String = { name -> "Hello, $name!" }
    println(greet("Kotlin"))
    
    // Lambda with single parameter (using 'it')
    val square: (Int) -> Int = { it * it }
    println("Square of 5: ${square(5)}")
    
    // Lambda with multiple parameters
    val add: (Int, Int) -> Int = { a, b -> a + b }
    println("Sum: ${add(10, 20)}")
    
    // Lambda with no parameters
    val getRandom: () -> Int = { (1..100).random() }
    println("Random number: ${getRandom()}")
    
    // Lambda with no return value
    val printMessage: (String) -> Unit = { message -> println("Message: $message") }
    printMessage("Hello from lambda!")
    
    // Using lambdas with collections
    val numbers = listOf(1, 2, 3, 4, 5)
    
    // Filter with lambda
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    
    // Map with lambda
    val doubledNumbers = numbers.map { it * 2 }
    println("Doubled numbers: $doubledNumbers")
    
    // Reduce with lambda
    val sum = numbers.reduce { acc, num -> acc + num }
    println("Sum: $sum")
    
    // ForEach with lambda
    numbers.forEach { println("Number: $it") }
}
```

### 7. Practical Android Example

```kotlin
fun main() {
    // Simulating Android button click handling
    val button = Button("Submit")
    
    // Set click listener using lambda
    button.setOnClickListener { 
        println("Button clicked!") 
    }
    
    // Simulate button click
    button.click()
    
    // Simulating data processing
    val userList = listOf(
        User("Alice", 25, "alice@email.com"),
        User("Bob", 30, "bob@email.com"),
        User("Charlie", 35, "charlie@email.com")
    )
    
    // Filter users by age
    val youngUsers = userList.filter { it.age < 30 }
    println("Young users: $youngUsers")
    
    // Transform users to names
    val userNames = userList.map { it.name }
    println("User names: $userNames")
    
    // Find user by email
    val user = userList.find { it.email == "bob@email.com" }
    println("Found user: $user")
    
    // Simulating API call with callback
    fetchUserData { users ->
        println("Received ${users.size} users from API")
        users.forEach { println("User: ${it.name}") }
    }
}

// Simulated classes for Android example
class Button(val text: String) {
    private var clickListener: (() -> Unit)? = null
    
    fun setOnClickListener(listener: () -> Unit) {
        clickListener = listener
    }
    
    fun click() {
        clickListener?.invoke()
    }
}

data class User(val name: String, val age: Int, val email: String)

// Simulated API call with callback
fun fetchUserData(callback: (List<User>) -> Unit) {
    // Simulate network delay
    println("Fetching user data...")
    
    val users = listOf(
        User("API User 1", 25, "api1@email.com"),
        User("API User 2", 30, "api2@email.com")
    )
    
    // Call the callback with the result
    callback(users)
}
```

### 8. Advanced Lambda Features

```kotlin
fun main() {
    // Lambda with receiver (extension function style)
    val buildString = buildString {
        append("Hello, ")
        append("Kotlin!")
    }
    println(buildString)
    
    // Lambda with multiple statements
    val processNumber: (Int) -> String = { number ->
        val doubled = number * 2
        val squared = number * number
        "Number: $number, Doubled: $doubled, Squared: $squared"
    }
    println(processNumber(5))
    
    // Lambda capturing variables (closure)
    val multiplier = 10
    val multiplyByTen: (Int) -> Int = { it * multiplier }
    println("5 * $multiplier = ${multiplyByTen(5)}")
    
    // Lambda with destructuring
    val users = listOf(
        Pair("Alice", 25),
        Pair("Bob", 30),
        Pair("Charlie", 35)
    )
    
    val userInfo = users.map { (name, age) -> 
        "$name is $age years old" 
    }
    println(userInfo)
    
    // Lambda with conditional logic
    val categorize: (Int) -> String = { number ->
        when {
            number < 0 -> "Negative"
            number == 0 -> "Zero"
            number < 10 -> "Small"
            number < 100 -> "Medium"
            else -> "Large"
        }
    }
    
    println("Category of 5: ${categorize(5)}")
    println("Category of 50: ${categorize(50)}")
    println("Category of 500: ${categorize(500)}")
}

// Extension function for building strings
fun buildString(builder: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.builder()
    return stringBuilder.toString()
}
```

## 🔧 Android Development Notes

### Why Functions and Lambdas Matter in Android:

1. **Event Handling**: Lambdas are perfect for button clicks, touch events, and callbacks
2. **Data Processing**: Higher-order functions make data manipulation clean and readable
3. **Asynchronous Programming**: Callbacks and coroutines use function types extensively
4. **Clean Architecture**: Functions help separate concerns and make code testable

### Common Android Patterns:

```kotlin
// Button click handling
button.setOnClickListener { 
    // Handle click
    navigateToNextScreen()
}

// RecyclerView adapter
recyclerView.adapter = MyAdapter { item ->
    // Handle item click
    showItemDetails(item)
}

// API callbacks
apiService.getUsers { users ->
    // Handle success
    updateUI(users)
}

// Data processing
val filteredUsers = users.filter { it.isActive }
    .map { it.toDisplayModel() }
    .sortedBy { it.name }
```

### Best Practices for Android:

1. **Use lambdas for simple callbacks** - More concise than anonymous classes
2. **Use higher-order functions for data processing** - Cleaner than loops
3. **Use function types for dependency injection** - Makes testing easier
4. **Use extension functions** - Add functionality to existing classes

## 🎯 Exercises

### Easy Level

1. **Basic Function Practice**
   ```kotlin
   // Create functions for:
   // - calculateArea(length, width) - returns area of rectangle
   // - isPositive(number) - returns true if number > 0
   // - getGreeting(name, time) - returns greeting based on time
   // - calculateAverage(numbers) - returns average of array
   // 
   // Test all functions with different inputs
   ```

2. **Single-Expression Functions**
   ```kotlin
   // Convert these to single-expression functions:
   // - isEven(number: Int): Boolean
   // - max(a: Int, b: Int): Int
   // - getInitials(firstName: String, lastName: String): String
   // - isAdult(age: Int): Boolean
   // - formatPrice(price: Double): String
   ```

3. **Lambda Basics**
   ```kotlin
   // Create lambda expressions for:
   // - Doubling a number
   // - Checking if string is empty
   // - Converting string to uppercase
   // - Adding two numbers
   // - Printing a message
   // 
   // Test each lambda with appropriate inputs
   ```

### Intermediate Level

1. **User Management System**
   ```kotlin
   // Create a user management system with functions:
   // - filterUsers(users, predicate) - filter users by condition
   // - mapUsers(users, transformer) - transform user data
   // - findUser(users, predicate) - find specific user
   // - sortUsers(users, comparator) - sort users
   // 
   // Use lambdas for predicates and transformers
   // Test with sample user data
   ```

2. **Calculator with Functions**
   ```kotlin
   // Create a calculator that:
   // - Takes two numbers and an operation function
   // - Supports add, subtract, multiply, divide operations
   // - Uses function types for operations
   // - Handles division by zero
   // - Returns calculation result
   // 
   // Test with different operations
   ```

3. **Data Processing Pipeline**
   ```kotlin
   // Create a data processing system that:
   // - Takes a list of numbers
   // - Filters even numbers
   // - Doubles remaining numbers
   // - Sums all results
   // - Uses higher-order functions and lambdas
   // 
   // Test with different input lists
   ```

### Advanced Level

1. **Event System**
   ```kotlin
   // Create an event system that:
   // - Registers event handlers (functions)
   // - Triggers events with data
   // - Supports multiple handlers per event
   // - Uses function types and lambdas
   // - Handles event data transformation
   // 
   // Test with different event types
   ```

2. **Functional Data Structures**
   ```kotlin
   // Create functional data structures:
   // - Immutable List with map, filter, reduce
   // - Option/Maybe type for nullable values
   // - Result type for error handling
   // - Use higher-order functions extensively
   // - Implement common functional patterns
   ```

3. **Async Task Manager**
   ```kotlin
   // Create an async task manager that:
   // - Executes tasks with callbacks
   // - Supports task chaining
   // - Handles success and error cases
   // - Uses function types for callbacks
   // - Simulates network operations
   // 
   // Test with multiple async operations
   ```

## 📝 Summary

### Key Takeaways:

1. **Function Definition**:
   - Use `fun` keyword to define functions
   - Functions can have parameters and return types
   - Single-expression functions use `=` syntax

2. **Function Types**:
   - `(Type) -> ReturnType` for function types
   - Functions are first-class citizens
   - Can be assigned to variables and passed as parameters

3. **Higher-Order Functions**:
   - Functions that take other functions as parameters
   - Enable functional programming patterns
   - Make code more flexible and reusable

4. **Lambda Expressions**:
   - Anonymous functions using `{ }` syntax
   - Can capture variables from surrounding scope
   - Use `it` for single parameter
   - Perfect for callbacks and data processing

### Best Practices:

1. **Use single-expression functions** - When function body is simple
2. **Use named arguments** - For functions with many parameters
3. **Use default parameters** - To reduce function overloads
4. **Use lambdas for callbacks** - More concise than anonymous classes
5. **Use higher-order functions** - For data processing and transformation

### Android-Specific Insights:

1. **Event Handling**: Lambdas are perfect for UI event callbacks
2. **Data Processing**: Higher-order functions make data manipulation clean
3. **API Callbacks**: Function types are essential for async operations
4. **Testing**: Function types make dependency injection and mocking easier

### Common Patterns:

```kotlin
// Event handling
button.setOnClickListener { handleClick() }

// Data processing
val result = data.filter { it.isValid }
    .map { it.transform() }
    .reduce { acc, item -> acc + item }

// API callbacks
api.getData { result ->
    when (result) {
        is Success -> handleSuccess(result.data)
        is Error -> handleError(result.message)
    }
}

// Configuration
val config = buildConfig {
    setApiKey("key")
    setBaseUrl("url")
    setTimeout(30)
}
```

### Next Steps:
Congratulations! You've completed the Kotlin Fundamentals module. You now have a solid foundation in:
- Kotlin syntax and data types
- Variables and constants
- Control flow
- Functions and lambda expressions

You're ready to apply these concepts to Android development and explore more advanced Kotlin features like:
- Classes and objects
- Collections and sequences
- Coroutines and async programming
- Android-specific Kotlin features

---

**🎉 Congratulations on completing the Kotlin Fundamentals module!**
