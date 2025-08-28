# Topic 8: Data Classes and Sealed Classes

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Create and use data classes for automatic boilerplate code generation
- Understand sealed classes and their role in restricted hierarchies
- Use sealed classes with when expressions for exhaustive pattern matching
- Model API responses and UI states effectively
- Write type-safe, maintainable code for Android applications

## 📖 Explanation

### What are Data Classes?

**Data classes** are a special type of class in Kotlin that automatically generates useful methods like `toString()`, `equals()`, `hashCode()`, and `copy()`. They're perfect for representing data structures and API responses.

### What are Sealed Classes?

**Sealed classes** represent restricted class hierarchies where all possible subtypes are known at compile time. They're excellent for modeling different states, API responses, and ensuring exhaustive pattern matching.

### Why These Matter in Android

Data and sealed classes are essential in Android development for:
- **API Responses** - Model JSON data from network calls
- **UI States** - Represent different states (loading, success, error)
- **Database Entities** - Model data for Room database
- **Event Handling** - Represent different types of events
- **Type Safety** - Ensure all cases are handled at compile time

## 💻 Code Examples

### 1. Basic Data Classes

```kotlin
fun main() {
    // Creating data class instances
    val user1 = User("Alice", 25, "alice@email.com")
    val user2 = User("Bob", 30, "bob@email.com")
    val user3 = User("Alice", 25, "alice@email.com")
    
    // Automatic toString() method
    println("User 1: $user1")
    println("User 2: $user2")
    
    // Automatic equals() method
    println("User 1 == User 3: ${user1 == user3}")  // true (same data)
    println("User 1 == User 2: ${user1 == user2}")  // false (different data)
    
    // Automatic hashCode() method
    println("User 1 hashCode: ${user1.hashCode()}")
    println("User 3 hashCode: ${user3.hashCode()}")  // Same as user1
    
    // Automatic copy() method
    val user1Updated = user1.copy(age = 26)
    println("Updated user: $user1Updated")
    
    // Copy with multiple changes
    val user1NewEmail = user1.copy(email = "alice.new@email.com", age = 26)
    println("User with new email: $user1NewEmail")
    
    // Component functions (destructuring)
    val (name, age, email) = user1
    println("Name: $name, Age: $age, Email: $email")
    
    // Using component functions individually
    val userName = user1.component1()  // name
    val userAge = user1.component2()   // age
    val userEmail = user1.component3() // email
    println("Extracted: $userName, $userAge, $userEmail")
}

// Data class with primary constructor
data class User(
    val name: String,
    val age: Int,
    val email: String
)

// Data class with default parameters
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String = "General",
    val inStock: Boolean = true
)
```

### 2. Advanced Data Classes

```kotlin
fun main() {
    // Creating complex data classes
    val address = Address("123 Main St", "New York", "NY", "10001")
    val person = Person("Alice", 25, address)
    
    println("Person: $person")
    
    // Nested destructuring
    val (name, age, personAddress) = person
    val (street, city, state, zip) = personAddress
    println("$name lives at $street, $city, $state $zip")
    
    // Copy with nested objects
    val newAddress = address.copy(street = "456 Oak Ave")
    val personMoved = person.copy(address = newAddress)
    println("Person moved: $personMoved")
    
    // Data class with computed properties
    val rectangle = Rectangle(5.0, 3.0)
    println("Rectangle: $rectangle")
    println("Area: ${rectangle.area}")
    println("Perimeter: ${rectangle.perimeter}")
    
    // Data class with custom methods
    val book = Book("Kotlin Programming", "John Doe", 2023)
    println("Book: $book")
    println("Is new: ${book.isNew()}")
    println("Display info: ${book.getDisplayInfo()}")
}

// Data class for address
data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String
)

// Data class with nested data class
data class Person(
    val name: String,
    val age: Int,
    val address: Address
)

// Data class with computed properties
data class Rectangle(
    val width: Double,
    val height: Double
) {
    val area: Double
        get() = width * height
    
    val perimeter: Double
        get() = 2 * (width + height)
}

// Data class with custom methods
data class Book(
    val title: String,
    val author: String,
    val year: Int
) {
    fun isNew(): Boolean {
        return year >= 2020
    }
    
    fun getDisplayInfo(): String {
        return "$title by $author ($year)"
    }
}
```

### 3. Basic Sealed Classes

```kotlin
fun main() {
    // Creating sealed class instances
    val success = ApiResponse.Success("Data loaded successfully")
    val error = ApiResponse.Error("Network error")
    val loading = ApiResponse.Loading
    
    // Using sealed classes with when expression
    handleApiResponse(success)
    handleApiResponse(error)
    handleApiResponse(loading)
    
    // Sealed classes are exhaustive (no else needed)
    val message = when (success) {
        is ApiResponse.Success -> "Success: ${success.data}"
        is ApiResponse.Error -> "Error: ${success.message}"
        ApiResponse.Loading -> "Loading..."
    }
    println("Message: $message")
    
    // Sealed classes with data
    val userSuccess = UserResult.Success(User("Alice", 25, "alice@email.com"))
    val userError = UserResult.Error("User not found")
    
    processUserResult(userSuccess)
    processUserResult(userError)
}

// Sealed class for API responses
sealed class ApiResponse {
    data class Success(val data: String) : ApiResponse()
    data class Error(val message: String) : ApiResponse()
    object Loading : ApiResponse()
}

// Sealed class for user results
sealed class UserResult {
    data class Success(val user: User) : UserResult()
    data class Error(val message: String) : UserResult()
}

// Function to handle API responses
fun handleApiResponse(response: ApiResponse) {
    when (response) {
        is ApiResponse.Success -> {
            println("Success: ${response.data}")
        }
        is ApiResponse.Error -> {
            println("Error: ${response.message}")
        }
        ApiResponse.Loading -> {
            println("Loading...")
        }
    }
}

// Function to process user results
fun processUserResult(result: UserResult) {
    when (result) {
        is UserResult.Success -> {
            println("User loaded: ${result.user.name}")
        }
        is UserResult.Error -> {
            println("Failed to load user: ${result.message}")
        }
    }
}

// Data class for user (from previous example)
data class User(
    val name: String,
    val age: Int,
    val email: String
)
```

### 4. Advanced Sealed Classes

```kotlin
fun main() {
    // Creating different UI states
    val loadingState = UiState.Loading
    val successState = UiState.Success(listOf("Item 1", "Item 2", "Item 3"))
    val errorState = UiState.Error("Failed to load data")
    val emptyState = UiState.Empty
    
    // Handle different UI states
    handleUiState(loadingState)
    handleUiState(successState)
    handleUiState(errorState)
    handleUiState(emptyState)
    
    // Sealed class with complex data
    val networkResult = NetworkResult.Success(
        data = "API Response Data",
        timestamp = System.currentTimeMillis()
    )
    
    val networkError = NetworkResult.Error(
        code = 404,
        message = "Not Found",
        details = "The requested resource was not found"
    )
    
    processNetworkResult(networkResult)
    processNetworkResult(networkError)
    
    // Sealed class with multiple data classes
    val clickEvent = UserEvent.ButtonClick("submit")
    val textChangeEvent = UserEvent.TextChange("new text")
    val swipeEvent = UserEvent.Swipe(Direction.LEFT)
    
    handleUserEvent(clickEvent)
    handleUserEvent(textChangeEvent)
    handleUserEvent(swipeEvent)
}

// Sealed class for UI states
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<String>) : UiState()
    data class Error(val message: String) : UiState()
    object Empty : UiState()
}

// Sealed class for network results
sealed class NetworkResult {
    data class Success(
        val data: String,
        val timestamp: Long
    ) : NetworkResult()
    
    data class Error(
        val code: Int,
        val message: String,
        val details: String
    ) : NetworkResult()
}

// Sealed class for user events
sealed class UserEvent {
    data class ButtonClick(val buttonId: String) : UserEvent()
    data class TextChange(val newText: String) : UserEvent()
    data class Swipe(val direction: Direction) : UserEvent()
}

// Enum for direction
enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

// Function to handle UI states
fun handleUiState(state: UiState) {
    when (state) {
        is UiState.Loading -> {
            println("Showing loading spinner")
        }
        is UiState.Success -> {
            println("Showing data: ${state.data}")
        }
        is UiState.Error -> {
            println("Showing error: ${state.message}")
        }
        is UiState.Empty -> {
            println("Showing empty state")
        }
    }
}

// Function to process network results
fun processNetworkResult(result: NetworkResult) {
    when (result) {
        is NetworkResult.Success -> {
            println("Network success: ${result.data} at ${result.timestamp}")
        }
        is NetworkResult.Error -> {
            println("Network error ${result.code}: ${result.message}")
            println("Details: ${result.details}")
        }
    }
}

// Function to handle user events
fun handleUserEvent(event: UserEvent) {
    when (event) {
        is UserEvent.ButtonClick -> {
            println("Button clicked: ${event.buttonId}")
        }
        is UserEvent.TextChange -> {
            println("Text changed to: ${event.newText}")
        }
        is UserEvent.Swipe -> {
            println("Swiped ${event.direction}")
        }
    }
}
```

### 5. Sealed Classes with When Expressions

```kotlin
fun main() {
    // Testing different expressions
    val expression1 = Expression.Number(42)
    val expression2 = Expression.Sum(Expression.Number(10), Expression.Number(5))
    val expression3 = Expression.Multiply(Expression.Number(3), Expression.Number(4))
    
    // Evaluate expressions
    println("Expression 1: ${evaluate(expression1)}")
    println("Expression 2: ${evaluate(expression2)}")
    println("Expression 3: ${evaluate(expression3)}")
    
    // Format expressions
    println("Formatted 1: ${formatExpression(expression1)}")
    println("Formatted 2: ${formatExpression(expression2)}")
    println("Formatted 3: ${formatExpression(expression3)}")
    
    // Complex expression
    val complexExpression = Expression.Sum(
        Expression.Multiply(Expression.Number(2), Expression.Number(3)),
        Expression.Number(4)
    )
    println("Complex: ${formatExpression(complexExpression)} = ${evaluate(complexExpression)}")
}

// Sealed class for mathematical expressions
sealed class Expression {
    data class Number(val value: Int) : Expression()
    data class Sum(val left: Expression, val right: Expression) : Expression()
    data class Multiply(val left: Expression, val right: Expression) : Expression()
}

// Function to evaluate expressions
fun evaluate(expression: Expression): Int {
    return when (expression) {
        is Expression.Number -> expression.value
        is Expression.Sum -> evaluate(expression.left) + evaluate(expression.right)
        is Expression.Multiply -> evaluate(expression.left) * evaluate(expression.right)
    }
}

// Function to format expressions
fun formatExpression(expression: Expression): String {
    return when (expression) {
        is Expression.Number -> expression.value.toString()
        is Expression.Sum -> "(${formatExpression(expression.left)} + ${formatExpression(expression.right)})"
        is Expression.Multiply -> "(${formatExpression(expression.left)} * ${formatExpression(expression.right)})"
    }
}
```

### 6. Practical Android Example

```kotlin
fun main() {
    // Simulating Android app with data and sealed classes
    val userManager = UserManager()
    
    // Simulate different scenarios
    println("=== Loading User ===")
    userManager.loadUser(1)
    
    println("\n=== Loading Non-existent User ===")
    userManager.loadUser(999)
    
    println("\n=== Network Error ===")
    userManager.simulateNetworkError()
    
    println("\n=== Update User ===")
    userManager.updateUser(1, "Alice Updated", "alice.updated@email.com")
}

// Data class for user
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val age: Int = 0,
    val isActive: Boolean = true
)

// Sealed class for user states
sealed class UserState {
    object Loading : UserState()
    data class Success(val user: User) : UserState()
    data class Error(val message: String) : UserState()
    object Empty : UserState()
}

// Sealed class for user actions
sealed class UserAction {
    data class LoadUser(val id: Int) : UserAction()
    data class UpdateUser(val id: Int, val name: String, val email: String) : UserAction()
    data class DeleteUser(val id: Int) : UserAction()
    object RefreshUsers : UserAction()
}

// Simulated user manager
class UserManager {
    private var currentState: UserState = UserState.Empty
    
    fun loadUser(id: Int) {
        // Simulate loading
        currentState = UserState.Loading
        handleStateChange(currentState)
        
        // Simulate API call
        val user = simulateApiCall(id)
        currentState = when (user) {
            null -> UserState.Error("User not found")
            else -> UserState.Success(user)
        }
        handleStateChange(currentState)
    }
    
    fun updateUser(id: Int, name: String, email: String) {
        currentState = UserState.Loading
        handleStateChange(currentState)
        
        val updatedUser = simulateUpdateUser(id, name, email)
        currentState = when (updatedUser) {
            null -> UserState.Error("Failed to update user")
            else -> UserState.Success(updatedUser)
        }
        handleStateChange(currentState)
    }
    
    fun simulateNetworkError() {
        currentState = UserState.Error("Network connection failed")
        handleStateChange(currentState)
    }
    
    private fun handleStateChange(state: UserState) {
        when (state) {
            is UserState.Loading -> {
                println("🔄 Loading...")
            }
            is UserState.Success -> {
                println("✅ User loaded successfully:")
                println("   ID: ${state.user.id}")
                println("   Name: ${state.user.name}")
                println("   Email: ${state.user.email}")
            }
            is UserState.Error -> {
                println("❌ Error: ${state.message}")
            }
            is UserState.Empty -> {
                println("📭 No user data")
            }
        }
    }
    
    private fun simulateApiCall(id: Int): User? {
        return when (id) {
            1 -> User(1, "Alice", "alice@email.com", 25)
            2 -> User(2, "Bob", "bob@email.com", 30)
            else -> null
        }
    }
    
    private fun simulateUpdateUser(id: Int, name: String, email: String): User? {
        return when (id) {
            1 -> User(id, name, email, 25)
            else -> null
        }
    }
}
```

### 7. Data Classes with Collections

```kotlin
fun main() {
    // Creating data classes with collections
    val order = Order(
        id = 1,
        items = listOf(
            OrderItem("Apple", 1.99, 3),
            OrderItem("Banana", 0.99, 5),
            OrderItem("Orange", 2.49, 2)
        ),
        customer = Customer("Alice", "alice@email.com")
    )
    
    println("Order: $order")
    println("Total items: ${order.totalItems}")
    println("Total cost: $${order.totalCost}")
    
    // Copying with collection modifications
    val newItems = order.items + OrderItem("Grape", 3.99, 1)
    val updatedOrder = order.copy(items = newItems)
    println("Updated order: $updatedOrder")
    println("New total cost: $${updatedOrder.totalCost}")
    
    // Data class with maps
    val userPreferences = UserPreferences(
        userId = 1,
        settings = mapOf(
            "theme" to "dark",
            "notifications" to "enabled",
            "language" to "en"
        )
    )
    
    println("User preferences: $userPreferences")
    println("Theme: ${userPreferences.settings["theme"]}")
    
    // Updating preferences
    val updatedSettings = userPreferences.settings + ("theme" to "light")
    val updatedPreferences = userPreferences.copy(settings = updatedSettings)
    println("Updated preferences: $updatedPreferences")
}

// Data class with list
data class Order(
    val id: Int,
    val items: List<OrderItem>,
    val customer: Customer
) {
    val totalItems: Int
        get() = items.sumOf { it.quantity }
    
    val totalCost: Double
        get() = items.sumOf { it.price * it.quantity }
}

// Data class for order items
data class OrderItem(
    val name: String,
    val price: Double,
    val quantity: Int
)

// Data class for customer
data class Customer(
    val name: String,
    val email: String
)

// Data class with map
data class UserPreferences(
    val userId: Int,
    val settings: Map<String, String>
)
```

## 🔧 Android Development Notes

### Why Data and Sealed Classes Matter in Android:

1. **API Responses** - Model JSON data from network calls
2. **UI States** - Represent different states (loading, success, error)
3. **Database Entities** - Model data for Room database
4. **Event Handling** - Represent different types of events
5. **Type Safety** - Ensure all cases are handled at compile time

### Common Android Patterns:

```kotlin
// API Response modeling
sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val message: String) : ApiResponse<T>()
    object Loading : ApiResponse<Nothing>()
}

// UI State modeling
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Item>) : UiState()
    data class Error(val message: String) : UiState()
}

// Data class for API models
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Event handling
sealed class UserEvent {
    data class Click(val itemId: Int) : UserEvent()
    data class Swipe(val direction: Direction) : UserEvent()
    object Refresh : UserEvent()
}
```

### Best Practices for Android:

1. **Use data classes for models** - API responses, database entities, UI models
2. **Use sealed classes for states** - UI states, API responses, events
3. **Make sealed classes exhaustive** - Always handle all cases in when expressions
4. **Use object for singleton states** - Loading, Empty states
5. **Keep data classes simple** - Focus on data, not behavior

## 🎯 Exercises

### Easy Level

1. **Basic Data Classes**
   ```kotlin
   // Create data classes for:
   // - Book (title, author, year, pages)
   // - Point (x, y coordinates)
   // - Color (red, green, blue values)
   // - Test the automatic methods (toString, equals, copy)
   ```

2. **Simple Sealed Classes**
   ```kotlin
   // Create a sealed class for Result with:
   // - Success case with data
   // - Error case with message
   // - Loading case (object)
   // - Create functions to handle each case
   ```

3. **Data Class Operations**
   ```kotlin
   // Create a data class for Student with:
   // - name, age, grades (list of integers)
   // - Add computed properties for average grade
   // - Add method to add new grade
   // - Test copying and destructuring
   ```

### Intermediate Level

1. **API Response Modeling**
   ```kotlin
   // Create a complete API response system with:
   // - Sealed class for different response types
   // - Data classes for different data models
   // - Functions to handle each response type
   // - Simulate network calls and responses
   ```

2. **UI State Management**
   ```kotlin
   // Create a UI state management system with:
   // - Sealed class for different UI states
   // - Data classes for UI data
   // - Functions to handle state transitions
   // - Simulate user interactions
   ```

3. **Event Handling System**
   ```kotlin
   // Create an event handling system with:
   // - Sealed class for different event types
   // - Data classes for event data
   // - Event processor to handle events
   // - Simulate different user events
   ```

### Advanced Level

1. **Complete Android App State**
   ```kotlin
   // Create a complete app state system with:
   // - Data classes for all models (User, Post, Comment)
   // - Sealed classes for all states (Auth, Feed, Profile)
   // - Sealed classes for all events (Login, Logout, Refresh)
   // - State manager to handle all transitions
   ```

2. **Expression Parser**
   ```kotlin
   // Create an expression parser with:
   // - Sealed class for different expression types
   // - Data classes for expression data
   // - Parser to evaluate expressions
   // - Support for complex nested expressions
   ```

3. **Configuration Management**
   ```kotlin
   // Create a configuration management system with:
   // - Data classes for different config types
   // - Sealed class for config states
   // - Functions to load and save configurations
   // - Validation and error handling
   ```

## 📝 Summary

### Key Takeaways:

1. **Data Classes**:
   - Automatically generate `toString()`, `equals()`, `hashCode()`, `copy()`
   - Perfect for representing data structures
   - Support destructuring declarations
   - Can have computed properties and methods

2. **Sealed Classes**:
   - Represent restricted class hierarchies
   - All subtypes known at compile time
   - Enable exhaustive pattern matching
   - Perfect for modeling states and events

3. **When Expressions**:
   - Exhaustive with sealed classes (no else needed)
   - Type-safe pattern matching
   - Clean and readable code
   - Compile-time safety

4. **Best Practices**:
   - Use data classes for models and entities
   - Use sealed classes for states and events
   - Make sealed classes exhaustive
   - Keep data classes focused on data

### Android-Specific Insights:

1. **API Responses**: Use sealed classes for different response types
2. **UI States**: Use sealed classes for loading, success, error states
3. **Event Handling**: Use sealed classes for different event types
4. **Data Modeling**: Use data classes for API and database entities
5. **Type Safety**: Ensure all cases are handled at compile time

### Common Patterns:

```kotlin
// API Response
sealed class ApiResponse<T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val message: String) : ApiResponse<T>()
    object Loading : ApiResponse<Nothing>()
}

// UI State
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Item>) : UiState()
    data class Error(val message: String) : UiState()
}

// Data Model
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Event
sealed class UserEvent {
    data class Click(val itemId: Int) : UserEvent()
    object Refresh : UserEvent()
}
```

### Next Steps:
You're now ready to learn about Extension Functions and Properties in the next topic, where you'll understand how to add functionality to existing classes without modifying them.

---

**Ready for the next topic?** Continue to [Topic 5: Extension Functions and Properties](05-extension-functions.md)
