# Topic 10: Scope Functions

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Understand the five scope functions: let, run, apply, also, and with
- Choose the appropriate scope function based on your use case
- Use scope functions for object initialization and configuration
- Apply scope functions in real-world Android scenarios
- Write cleaner, more readable code using scope functions

## 📖 Explanation

### What are Scope Functions?

**Scope functions** are functions that create a temporary scope for executing a block of code. They help you write more concise and readable code by providing different ways to access and manipulate objects.

### The Five Scope Functions

Kotlin provides five scope functions, each with different characteristics:

1. **let** - Executes a block and returns the result of the lambda
2. **run** - Executes a block and returns the result of the lambda
3. **apply** - Executes a block and returns the object itself
4. **also** - Executes a block and returns the object itself
5. **with** - Executes a block and returns the result of the lambda

### Key Differences

The main differences are:
- **Return value**: Some return the lambda result, others return the object
- **Context object**: How you access the object inside the lambda (`it` vs `this`)
- **Use cases**: When to use each function

## 💻 Code Examples

### 1. Basic Scope Functions

```kotlin
fun main() {
    val name = "Alice"
    
    // Using let - returns lambda result, context object as 'it'
    val greeting = name.let { 
        "Hello, $it!" 
    }
    println(greeting)
    
    // Using run - returns lambda result, context object as 'this'
    val uppercaseName = name.run { 
        this.uppercase() 
    }
    println(uppercaseName)
    
    // Using apply - returns the object itself, context object as 'this'
    val person = Person().apply {
        name = "Bob"
        age = 25
        email = "bob@email.com"
    }
    println("Person: $person")
    
    // Using also - returns the object itself, context object as 'it'
    val numbers = mutableListOf(1, 2, 3).also {
        it.add(4)
        it.add(5)
        println("Added numbers to list")
    }
    println("Numbers: $numbers")
    
    // Using with - returns lambda result, context object as 'this'
    val result = with(name) {
        "Length: ${this.length}, Uppercase: ${this.uppercase()}"
    }
    println(result)
}

// Data class for examples
data class Person(
    var name: String = "",
    var age: Int = 0,
    var email: String = ""
)
```

### 2. Understanding Context Objects

```kotlin
fun main() {
    val text = "Hello, Kotlin!"
    
    // let - context object as 'it'
    text.let { str ->
        println("let: $str")  // 'it' is available as 'str'
        println("Length: ${str.length}")
    }
    
    // run - context object as 'this'
    text.run {
        println("run: $this")  // 'this' refers to the string
        println("Length: $length")  // Can omit 'this.'
    }
    
    // apply - context object as 'this'
    val person = Person().apply {
        name = "Alice"  // 'this.name' is implied
        age = 25       // 'this.age' is implied
        email = "alice@email.com"
    }
    println("Person: $person")
    
    // also - context object as 'it'
    val numbers = mutableListOf(1, 2, 3).also { list ->
        list.add(4)  // 'it' is available as 'list'
        list.add(5)
        println("Modified list: $list")
    }
    println("Final numbers: $numbers")
    
    // with - context object as 'this'
    with(text) {
        println("with: $this")  // 'this' refers to the string
        println("Length: $length")
        println("Uppercase: ${uppercase()}")
    }
}
```

### 3. Return Values and Use Cases

```kotlin
fun main() {
    val name = "Alice"
    
    // let - returns lambda result
    val greeting = name.let { 
        "Hello, $it!" 
    }
    println("let result: $greeting")
    
    // run - returns lambda result
    val info = name.run { 
        "Name: $this, Length: $length" 
    }
    println("run result: $info")
    
    // apply - returns the object itself
    val person = Person().apply {
        name = "Bob"
        age = 30
    }
    println("apply result: $person")
    
    // also - returns the object itself
    val numbers = mutableListOf(1, 2, 3).also {
        it.add(4)
        it.add(5)
    }
    println("also result: $numbers")
    
    // with - returns lambda result
    val description = with(name) {
        "The name '$this' has $length characters"
    }
    println("with result: $description")
    
    // Chaining scope functions
    val result = name
        .let { it.uppercase() }
        .run { "UPPERCASE: $this" }
        .also { println("Processing: $it") }
    
    println("Final result: $result")
}
```

### 4. Practical Use Cases

```kotlin
fun main() {
    // 1. Object initialization with apply
    val button = Button().apply {
        text = "Click me"
        isEnabled = true
        onClick = { println("Button clicked!") }
    }
    println("Button: $button")
    
    // 2. Null safety with let
    val nullableName: String? = "Alice"
    nullableName?.let { name ->
        println("Hello, $name!")
        println("Name length: ${name.length}")
    }
    
    // 3. Multiple operations with run
    val result = "Hello, World!".run {
        val upper = this.uppercase()
        val length = this.length
        "Uppercase: $upper, Length: $length"
    }
    println("Run result: $result")
    
    // 4. Side effects with also
    val numbers = mutableListOf(1, 2, 3).also {
        println("Original list: $it")
        it.add(4)
        println("After adding 4: $it")
    }
    println("Final list: $numbers")
    
    // 5. Multiple object operations with with
    val person = Person("Alice", 25, "alice@email.com")
    val info = with(person) {
        "Name: $name, Age: $age, Email: $email"
    }
    println("Person info: $info")
}

// Simulated classes
class Button {
    var text: String = ""
    var isEnabled: Boolean = false
    var onClick: (() -> Unit)? = null
    
    override fun toString(): String {
        return "Button(text='$text', enabled=$isEnabled)"
    }
}

data class Person(
    val name: String,
    val age: Int,
    val email: String
)
```

### 5. Scope Functions with Collections

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5)
    
    // Using let for transformation
    val doubled = numbers.let { list ->
        list.map { it * 2 }
    }
    println("Doubled: $doubled")
    
    // Using run for multiple operations
    val result = numbers.run {
        val sum = this.sum()
        val average = this.average()
        val max = this.maxOrNull()
        "Sum: $sum, Average: $average, Max: $max"
    }
    println("Statistics: $result")
    
    // Using apply for mutable collections
    val mutableNumbers = mutableListOf<Int>().apply {
        add(1)
        add(2)
        add(3)
        addAll(listOf(4, 5))
    }
    println("Mutable numbers: $mutableNumbers")
    
    // Using also for side effects
    val filteredNumbers = numbers.also { list ->
        println("Original list: $list")
        println("List size: ${list.size}")
    }.filter { it % 2 == 0 }
    println("Filtered numbers: $filteredNumbers")
    
    // Using with for multiple operations
    val info = with(numbers) {
        "List with ${this.size} elements: ${this.joinToString(", ")}"
    }
    println("Info: $info")
}
```

### 6. Scope Functions for Null Safety

```kotlin
fun main() {
    // Nullable string handling
    val nullableName: String? = "Alice"
    val nullName: String? = null
    
    // Safe handling with let
    nullableName?.let { name ->
        println("Hello, $name!")
        println("Name length: ${name.length}")
    }
    
    nullName?.let { name ->
        println("This won't execute")
    } ?: run {
        println("Name is null")
    }
    
    // Multiple nullable values
    val user = getUser()
    user?.let { u ->
        println("User: ${u.name}")
        println("Email: ${u.email}")
    } ?: run {
        println("User not found")
    }
    
    // Safe property access
    val result = nullableName?.let { name ->
        name.uppercase()
    }?.let { upper ->
        "Uppercase: $upper"
    } ?: "No name provided"
    
    println("Result: $result")
}

// Simulated function that might return null
fun getUser(): User? {
    return User("Bob", "bob@email.com")
}

data class User(
    val name: String,
    val email: String
)
```

### 7. Android-Style Examples

```kotlin
fun main() {
    // Simulating Android View initialization
    val button = createButton()
    val textView = createTextView()
    val layout = createLayout()
    
    // Using apply for View configuration
    button.apply {
        text = "Submit"
        isEnabled = true
        setOnClickListener { println("Button clicked!") }
    }
    
    textView.apply {
        text = "Hello, World!"
        textSize = 16
        setTextColor("black")
    }
    
    layout.apply {
        addView(button)
        addView(textView)
        setOrientation("vertical")
    }
    
    // Using let for safe operations
    val user = getUserFromApi()
    user?.let { u ->
        textView.text = "Welcome, ${u.name}!"
        button.isEnabled = true
    } ?: run {
        textView.text = "Please log in"
        button.isEnabled = false
    }
    
    // Using run for complex operations
    val result = user?.run {
        val greeting = "Hello, $name!"
        val emailInfo = "Email: $email"
        "$greeting\n$emailInfo"
    } ?: "No user data"
    
    println("Result: $result")
    
    // Using also for logging
    user?.also { u ->
        println("User loaded: ${u.name}")
        println("Email: ${u.email}")
    }
}

// Simulated Android classes
class View {
    var text: String = ""
    var isEnabled: Boolean = false
    var textSize: Int = 14
    var textColor: String = "black"
    
    fun setOnClickListener(listener: () -> Unit) {
        println("ClickListener set")
    }
    
    fun setTextColor(color: String) {
        this.textColor = color
        println("Text color set to: $color")
    }
}

class Button : View()
class TextView : View()

class LinearLayout : View() {
    fun addView(view: View) {
        println("View added to layout")
    }
    
    fun setOrientation(orientation: String) {
        println("Orientation set to: $orientation")
    }
}

// Simulated functions
fun createButton() = Button()
fun createTextView() = TextView()
fun createLayout() = LinearLayout()

fun getUserFromApi(): User? {
    return User("Alice", "alice@email.com")
}

data class User(
    val name: String,
    val email: String
)
```

### 8. Advanced Scope Function Patterns

```kotlin
fun main() {
    // 1. Builder pattern with apply
    val person = PersonBuilder().apply {
        setName("Alice")
        setAge(25)
        setEmail("alice@email.com")
        setAddress("123 Main St")
    }.build()
    
    println("Built person: $person")
    
    // 2. Configuration with apply
    val config = AppConfig().apply {
        apiUrl = "https://api.example.com"
        timeout = 30
        retryCount = 3
        enableLogging = true
    }
    println("Config: $config")
    
    // 3. Validation with let
    val input = "alice@email.com"
    val validatedEmail = input.let { email ->
        if (email.contains("@")) {
            email
        } else {
            null
        }
    }
    println("Validated email: $validatedEmail")
    
    // 4. Transformation chain with let
    val result = "hello, world"
        .let { it.capitalize() }
        .let { it.replace(",", "") }
        .let { it.uppercase() }
    println("Transformed: $result")
    
    // 5. Multiple operations with run
    val data = "1,2,3,4,5".run {
        val numbers = this.split(",").map { it.toInt() }
        val sum = numbers.sum()
        val average = numbers.average()
        "Numbers: $numbers, Sum: $sum, Average: $average"
    }
    println("Data: $data")
    
    // 6. Side effects with also
    val processedData = listOf(1, 2, 3, 4, 5).also { list ->
        println("Processing ${list.size} items")
        println("Items: ${list.joinToString()}")
    }.map { it * 2 }
    println("Processed: $processedData")
}

// Builder pattern example
class PersonBuilder {
    private var name: String = ""
    private var age: Int = 0
    private var email: String = ""
    private var address: String = ""
    
    fun setName(name: String) { this.name = name }
    fun setAge(age: Int) { this.age = age }
    fun setEmail(email: String) { this.email = email }
    fun setAddress(address: String) { this.address = address }
    
    fun build() = Person(name, age, email, address)
}

data class Person(
    val name: String,
    val age: Int,
    val email: String,
    val address: String = ""
)

data class AppConfig(
    var apiUrl: String = "",
    var timeout: Int = 0,
    var retryCount: Int = 0,
    var enableLogging: Boolean = false
)

// Extension function for String
fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}
```

## 🔧 Android Development Notes

### Why Scope Functions Matter in Android:

1. **View Initialization** - Clean object configuration with apply
2. **Null Safety** - Safe handling of nullable values with let
3. **Object Configuration** - Setting up complex objects with apply
4. **Side Effects** - Logging and debugging with also
5. **Clean Code** - More readable and maintainable code

### Common Android Patterns:

```kotlin
// View initialization with apply
val button = Button(context).apply {
    text = "Click me"
    isEnabled = true
    setOnClickListener { /* handle click */ }
}

// Null safety with let
user?.let { user ->
    textView.text = "Welcome, ${user.name}!"
    button.isEnabled = true
}

// Configuration with apply
val retrofit = Retrofit.Builder().apply {
    baseUrl("https://api.example.com")
    addConverterFactory(GsonConverterFactory.create())
    client(okHttpClient)
}.build()

// Side effects with also
apiResponse.also { response ->
    Log.d("API", "Response: $response")
}
```

### Best Practices for Android:

1. **Use apply for object initialization** - Clean configuration of Views and objects
2. **Use let for null safety** - Safe handling of nullable values
3. **Use also for side effects** - Logging, debugging, and side operations
4. **Use run for complex operations** - Multiple operations on an object
5. **Use with for multiple operations** - When you don't need the object reference

## 🎯 Exercises

### Easy Level

1. **Basic Scope Functions**
   ```kotlin
   // Create examples using each scope function:
   // - let: transform a string and return the result
   // - run: perform multiple operations on a number
   // - apply: configure a Person object
   // - also: add side effects to a list operation
   // - with: get information about a string
   ```

2. **Null Safety with let**
   ```kotlin
   // Create functions that:
   // - Handle nullable strings safely with let
   // - Process nullable user objects
   // - Provide fallback values for null cases
   ```

3. **Object Configuration**
   ```kotlin
   // Create classes and use apply to:
   // - Configure a Car object with properties
   // - Set up a Book object with details
   // - Initialize a Student object with information
   ```

### Intermediate Level

1. **Collection Processing**
   ```kotlin
   // Use scope functions to:
   // - Transform a list with let
   // - Configure a mutable list with apply
   // - Add side effects to list operations with also
   // - Process multiple list operations with run
   ```

2. **Builder Pattern**
   ```kotlin
   // Create a builder pattern using apply:
   // - PersonBuilder with name, age, email
   // - CarBuilder with make, model, year
   // - HouseBuilder with rooms, address, price
   ```

3. **Configuration Objects**
   ```kotlin
   // Create configuration classes and use apply:
   // - DatabaseConfig with connection settings
   // - NetworkConfig with API endpoints
   // - AppConfig with user preferences
   ```

### Advanced Level

1. **Android View System**
   ```kotlin
   // Create a complete Android-like view system:
   // - View classes with properties
   // - Layout classes that can contain views
   // - Use apply for view configuration
   // - Use let for safe view operations
   ```

2. **Data Processing Pipeline**
   ```kotlin
   // Create a data processing pipeline using scope functions:
   // - Load data with let
   // - Transform data with run
   // - Configure processing with apply
   // - Add logging with also
   ```

3. **API Response Handling**
   ```kotlin
   // Create an API response system:
   // - Handle different response types with let
   // - Configure response objects with apply
   // - Add logging and debugging with also
   // - Process responses with run
   ```

## 📝 Summary

### Key Takeaways:

1. **Scope Functions Overview**:
   - **let**: Returns lambda result, context as `it`
   - **run**: Returns lambda result, context as `this`
   - **apply**: Returns object itself, context as `this`
   - **also**: Returns object itself, context as `it`
   - **with**: Returns lambda result, context as `this`

2. **When to Use Each**:
   - **let**: Null safety, transformations, single operations
   - **run**: Multiple operations, complex logic
   - **apply**: Object initialization, configuration
   - **also**: Side effects, logging, debugging
   - **with**: Multiple operations on existing object

3. **Context Objects**:
   - `it`: Used in let and also
   - `this`: Used in run, apply, and with
   - Can be renamed for clarity

4. **Best Practices**:
   - Use apply for object initialization
   - Use let for null safety
   - Use also for side effects
   - Use run for complex operations
   - Use with for multiple operations

### Android-Specific Insights:

1. **View Initialization**: Use apply for configuring Views
2. **Null Safety**: Use let for safe handling of nullable values
3. **Object Configuration**: Use apply for setting up complex objects
4. **Side Effects**: Use also for logging and debugging
5. **Clean Code**: Scope functions make code more readable

### Common Patterns:

```kotlin
// View initialization
val button = Button(context).apply {
    text = "Click me"
    isEnabled = true
    setOnClickListener { /* handle click */ }
}

// Null safety
user?.let { user ->
    textView.text = "Welcome, ${user.name}!"
}

// Object configuration
val config = AppConfig().apply {
    apiUrl = "https://api.example.com"
    timeout = 30
}

// Side effects
data.also { 
    Log.d("TAG", "Processing: $it") 
}
```

### Next Steps:
Congratulations! You've completed the Advanced Kotlin module. You now have a comprehensive understanding of:
- Null safety and safe call operators
- Collections and functional programming
- Object-oriented programming
- Data classes and sealed classes
- Extension functions and properties
- Scope functions

You're ready to apply these concepts to real Android development projects and explore more advanced Kotlin features like coroutines, sequences, and advanced functional programming patterns.

---

**🎉 Congratulations on completing the Advanced Kotlin module!**
