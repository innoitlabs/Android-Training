# Topic 9: Extension Functions and Properties

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Create extension functions to add functionality to existing classes
- Use extension properties to add computed properties to classes
- Understand extension function scope and visibility
- Apply extension functions in real-world Android scenarios
- Write cleaner, more readable code using extensions

## 📖 Explanation

### What are Extension Functions?

**Extension functions** allow you to add new functionality to existing classes without modifying their source code. They're like "add-on" methods that make the class appear to have additional capabilities.

### What are Extension Properties?

**Extension properties** allow you to add computed properties to existing classes, similar to extension functions but for properties instead of methods.

### Why Extensions Matter in Android

Extension functions are essential in Android development for:
- **View Extensions** - Add convenience methods to Android Views
- **String Utilities** - Add formatting and validation methods
- **Collection Helpers** - Add custom operations to collections
- **Context Extensions** - Add utility methods to Context
- **Clean Code** - Make code more readable and maintainable

## 💻 Code Examples

### 1. Basic Extension Functions

```kotlin
fun main() {
    val name = "Alice"
    
    // Using extension function
    println("Greeting: ${name.greet()}")
    println("Reversed: ${name.reverse()}")
    println("Is long: ${name.isLong()}")
    
    // Using extension function with parameters
    println("Repeated: ${name.repeat(3)}")
    println("With prefix: ${name.withPrefix("Hello, ")}")
    
    // Using extension function on nullable types
    val nullableName: String? = "Bob"
    println("Safe greeting: ${nullableName.safeGreet()}")
    
    val nullName: String? = null
    println("Safe greeting for null: ${nullName.safeGreet()}")
    
    // Using extension function on numbers
    val number = 42
    println("Is even: ${number.isEven()}")
    println("Squared: ${number.squared()}")
    println("Is positive: ${number.isPositive()}")
}

// Extension function for String
fun String.greet(): String {
    return "Hello, $this!"
}

// Extension function that reverses a string
fun String.reverse(): String {
    return this.reversed()
}

// Extension function that checks if string is long
fun String.isLong(): Boolean {
    return this.length > 10
}

// Extension function with parameters
fun String.repeat(times: Int): String {
    return buildString {
        repeat(times) {
            append(this@repeat)
        }
    }
}

// Extension function with default parameter
fun String.withPrefix(prefix: String = "Hi, "): String {
    return "$prefix$this"
}

// Extension function for nullable String
fun String?.safeGreet(): String {
    return this?.greet() ?: "Hello, stranger!"
}

// Extension function for Int
fun Int.isEven(): Boolean {
    return this % 2 == 0
}

// Extension function for Int
fun Int.squared(): Int {
    return this * this
}

// Extension function for Int
fun Int.isPositive(): Boolean {
    return this > 0
}
```

### 2. Extension Properties

```kotlin
fun main() {
    val text = "Hello, World!"
    
    // Using extension properties
    println("Text: $text")
    println("Word count: ${text.wordCount}")
    println("Is palindrome: ${text.isPalindrome}")
    println("First word: ${text.firstWord}")
    println("Last word: ${text.lastWord}")
    
    // Using extension properties on numbers
    val number = 12345
    println("Number: $number")
    println("Digit count: ${number.digitCount}")
    println("Is palindrome: ${number.isPalindrome}")
    println("Reversed: ${number.reversed}")
    
    // Using extension properties on collections
    val list = listOf(1, 2, 3, 4, 5)
    println("List: $list")
    println("Is empty: ${list.isEmpty}")
    println("Has duplicates: ${list.hasDuplicates}")
    println("Sum: ${list.sum}")
    println("Average: ${list.average}")
}

// Extension property for String
val String.wordCount: Int
    get() = this.split(" ").size

// Extension property for String
val String.isPalindrome: Boolean
    get() = this.lowercase() == this.lowercase().reversed()

// Extension property for String
val String.firstWord: String
    get() = this.split(" ").firstOrNull() ?: ""

// Extension property for String
val String.lastWord: String
    get() = this.split(" ").lastOrNull() ?: ""

// Extension property for Int
val Int.digitCount: Int
    get() = this.toString().length

// Extension property for Int
val Int.isPalindrome: Boolean
    get() = this.toString() == this.toString().reversed()

// Extension property for Int
val Int.reversed: Int
    get() = this.toString().reversed().toIntOrNull() ?: 0

// Extension property for List<Int>
val List<Int>.isEmpty: Boolean
    get() = this.isEmpty()

// Extension property for List<Int>
val List<Int>.hasDuplicates: Boolean
    get() = this.size != this.toSet().size

// Extension property for List<Int>
val List<Int>.sum: Int
    get() = this.sum()

// Extension property for List<Int>
val List<Int>.average: Double
    get() = if (this.isNotEmpty()) this.sum().toDouble() / this.size else 0.0
```

### 3. Extension Functions with Generics

```kotlin
fun main() {
    // Using generic extension functions
    val numbers = listOf(1, 2, 3, 4, 5)
    val strings = listOf("apple", "banana", "cherry")
    
    // Generic extension functions
    println("Numbers: ${numbers.joinWithCommas()}")
    println("Strings: ${strings.joinWithCommas()}")
    
    println("First number: ${numbers.firstOrNull()}")
    println("First string: ${strings.firstOrNull()}")
    
    // Generic extension function with type constraints
    val comparableNumbers = listOf(5, 2, 8, 1, 9)
    println("Max number: ${comparableNumbers.maxOrNull()}")
    println("Min number: ${comparableNumbers.minOrNull()}")
    
    // Generic extension function for nullable types
    val nullableList: List<String>? = listOf("a", "b", "c")
    println("Safe size: ${nullableList.safeSize()}")
    
    val nullList: List<String>? = null
    println("Safe size of null: ${nullList.safeSize()}")
    
    // Generic extension function for pairs
    val pair = "Hello" to "World"
    println("Swapped pair: ${pair.swap()}")
    
    val numberPair = 1 to 2
    println("Swapped numbers: ${numberPair.swap()}")
}

// Generic extension function
fun <T> List<T>.joinWithCommas(): String {
    return this.joinToString(", ")
}

// Generic extension function with nullable return
fun <T> List<T>.firstOrNull(): T? {
    return this.firstOrNull()
}

// Generic extension function with type constraints
fun <T : Comparable<T>> List<T>.maxOrNull(): T? {
    return this.maxOrNull()
}

// Generic extension function with type constraints
fun <T : Comparable<T>> List<T>.minOrNull(): T? {
    return this.minOrNull()
}

// Generic extension function for nullable collections
fun <T> List<T>?.safeSize(): Int {
    return this?.size ?: 0
}

// Generic extension function for pairs
fun <A, B> Pair<A, B>.swap(): Pair<B, A> {
    return Pair(this.second, this.first)
}
```

### 4. Extension Functions for Collections

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val strings = listOf("apple", "banana", "cherry", "date", "elderberry")
    
    // Collection extension functions
    println("Even numbers: ${numbers.getEvenNumbers()}")
    println("Odd numbers: ${numbers.getOddNumbers()}")
    println("Numbers greater than 5: ${numbers.greaterThan(5)}")
    
    println("Strings starting with 'a': ${strings.startsWith("a")}")
    println("Long strings: ${strings.getLongStrings(5)}")
    
    // Extension functions for maps
    val userAges = mapOf(
        "Alice" to 25,
        "Bob" to 30,
        "Charlie" to 35,
        "Diana" to 28
    )
    
    println("Young users: ${userAges.getYoungUsers(30)}")
    println("Old users: ${userAges.getOldUsers(30)}")
    
    // Extension functions for sets
    val set1 = setOf(1, 2, 3, 4, 5)
    val set2 = setOf(4, 5, 6, 7, 8)
    
    println("Set 1: $set1")
    println("Set 2: $set2")
    println("Union: ${set1.unionWith(set2)}")
    println("Intersection: ${set1.intersectionWith(set2)}")
    println("Difference: ${set1.differenceFrom(set2)}")
}

// Extension function for List<Int>
fun List<Int>.getEvenNumbers(): List<Int> {
    return this.filter { it % 2 == 0 }
}

// Extension function for List<Int>
fun List<Int>.getOddNumbers(): List<Int> {
    return this.filter { it % 2 != 0 }
}

// Extension function for List<Int>
fun List<Int>.greaterThan(threshold: Int): List<Int> {
    return this.filter { it > threshold }
}

// Extension function for List<String>
fun List<String>.startsWith(prefix: String): List<String> {
    return this.filter { it.startsWith(prefix, ignoreCase = true) }
}

// Extension function for List<String>
fun List<String>.getLongStrings(minLength: Int): List<String> {
    return this.filter { it.length > minLength }
}

// Extension function for Map<String, Int>
fun Map<String, Int>.getYoungUsers(maxAge: Int): Map<String, Int> {
    return this.filter { it.value <= maxAge }
}

// Extension function for Map<String, Int>
fun Map<String, Int>.getOldUsers(minAge: Int): Map<String, Int> {
    return this.filter { it.value >= minAge }
}

// Extension function for Set<Int>
fun Set<Int>.unionWith(other: Set<Int>): Set<Int> {
    return this.union(other)
}

// Extension function for Set<Int>
fun Set<Int>.intersectionWith(other: Set<Int>): Set<Int> {
    return this.intersect(other)
}

// Extension function for Set<Int>
fun Set<Int>.differenceFrom(other: Set<Int>): Set<Int> {
    return this.subtract(other)
}
```

### 5. Extension Functions for Android Views

```kotlin
fun main() {
    // Simulating Android View extensions
    val button = Button("Submit")
    val textView = TextView("Hello World")
    val imageView = ImageView("profile.jpg")
    
    // Using View extensions
    button.makeVisible()
    button.makeInvisible()
    button.makeGone()
    
    textView.setText("Updated text")
    textView.setTextColor("red")
    textView.setTextSize(16)
    
    imageView.loadImage("new_image.jpg")
    imageView.setCornerRadius(8)
    
    // Using ViewGroup extensions
    val linearLayout = LinearLayout()
    linearLayout.addView(button)
    linearLayout.addView(textView)
    linearLayout.setOrientation("vertical")
    
    // Using Context extensions
    val context = Context()
    context.showToast("Hello from extension!")
    context.showSnackbar("This is a snackbar")
    context.getColor("primary")
}

// Simulated Android classes
class View {
    var visibility: String = "visible"
    var text: String = ""
    var textColor: String = "black"
    var textSize: Int = 14
    var imageUrl: String = ""
    var cornerRadius: Int = 0
}

class Button(text: String) : View()
class TextView(text: String) : View()
class ImageView(imageUrl: String) : View()
class LinearLayout : ViewGroup()
class ViewGroup : View()
class Context

// Extension functions for View
fun View.makeVisible() {
    this.visibility = "visible"
    println("View is now visible")
}

fun View.makeInvisible() {
    this.visibility = "invisible"
    println("View is now invisible")
}

fun View.makeGone() {
    this.visibility = "gone"
    println("View is now gone")
}

// Extension functions for TextView
fun TextView.setText(text: String) {
    this.text = text
    println("Text set to: $text")
}

fun TextView.setTextColor(color: String) {
    this.textColor = color
    println("Text color set to: $color")
}

fun TextView.setTextSize(size: Int) {
    this.textSize = size
    println("Text size set to: $size")
}

// Extension functions for ImageView
fun ImageView.loadImage(url: String) {
    this.imageUrl = url
    println("Loading image from: $url")
}

fun ImageView.setCornerRadius(radius: Int) {
    this.cornerRadius = radius
    println("Corner radius set to: $radius")
}

// Extension functions for ViewGroup
fun ViewGroup.addView(view: View) {
    println("Added view to ViewGroup")
}

fun ViewGroup.setOrientation(orientation: String) {
    println("Orientation set to: $orientation")
}

// Extension functions for Context
fun Context.showToast(message: String) {
    println("Toast: $message")
}

fun Context.showSnackbar(message: String) {
    println("Snackbar: $message")
}

fun Context.getColor(colorName: String): String {
    return when (colorName) {
        "primary" -> "#2196F3"
        "secondary" -> "#FF9800"
        else -> "#000000"
    }
}
```

### 6. Extension Functions for String and Number Formatting

```kotlin
fun main() {
    // String formatting extensions
    val name = "alice"
    val email = "alice@email.com"
    val phone = "1234567890"
    
    println("Capitalized: ${name.capitalize()}")
    println("Email valid: ${email.isValidEmail()}")
    println("Phone formatted: ${phone.formatPhone()}")
    
    // Number formatting extensions
    val price = 1234.56
    val percentage = 0.85
    val largeNumber = 1234567
    
    println("Price formatted: ${price.formatAsCurrency()}")
    println("Percentage: ${percentage.formatAsPercentage()}")
    println("Large number: ${largeNumber.formatWithCommas()}")
    
    // Date and time extensions
    val timestamp = System.currentTimeMillis()
    println("Date: ${timestamp.toDateString()}")
    println("Time: ${timestamp.toTimeString()}")
    println("Relative time: ${timestamp.toRelativeTimeString()}")
    
    // File path extensions
    val filePath = "/path/to/file.txt"
    println("File name: ${filePath.getFileName()}")
    println("File extension: ${filePath.getFileExtension()}")
    println("Is image: ${filePath.isImageFile()}")
}

// String extension functions
fun String.capitalize(): String {
    return this.replaceFirstChar { it.uppercase() }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(this)
}

fun String.formatPhone(): String {
    return if (this.length == 10) {
        "(${this.substring(0, 3)}) ${this.substring(3, 6)}-${this.substring(6)}"
    } else {
        this
    }
}

// Number extension functions
fun Double.formatAsCurrency(): String {
    return "$${String.format("%.2f", this)}"
}

fun Double.formatAsPercentage(): String {
    return "${String.format("%.1f", this * 100)}%"
}

fun Int.formatWithCommas(): String {
    return String.format("%,d", this)
}

// Long extension functions for timestamps
fun Long.toDateString(): String {
    val date = java.util.Date(this)
    return java.text.SimpleDateFormat("MMM dd, yyyy").format(date)
}

fun Long.toTimeString(): String {
    val date = java.util.Date(this)
    return java.text.SimpleDateFormat("HH:mm:ss").format(date)
}

fun Long.toRelativeTimeString(): String {
    val now = System.currentTimeMillis()
    val diff = now - this
    
    return when {
        diff < 60000 -> "Just now"
        diff < 3600000 -> "${diff / 60000} minutes ago"
        diff < 86400000 -> "${diff / 3600000} hours ago"
        else -> "${diff / 86400000} days ago"
    }
}

// String extension functions for file paths
fun String.getFileName(): String {
    return this.substringAfterLast("/")
}

fun String.getFileExtension(): String {
    return this.substringAfterLast(".", "")
}

fun String.isImageFile(): Boolean {
    val imageExtensions = setOf("jpg", "jpeg", "png", "gif", "bmp")
    return imageExtensions.contains(this.getFileExtension().lowercase())
}
```

### 7. Practical Android Example

```kotlin
fun main() {
    // Simulating Android app with extension functions
    val userManager = UserManager()
    
    // Using extension functions for user validation
    val user = User("alice", "alice@email.com", "1234567890")
    
    println("=== User Validation ===")
    println("Name valid: ${user.name.isValidName()}")
    println("Email valid: ${user.email.isValidEmail()}")
    println("Phone valid: ${user.phone.isValidPhone()}")
    
    // Using extension functions for data processing
    val users = listOf(
        User("alice", "alice@email.com", "1234567890"),
        User("bob", "bob@email.com", "0987654321"),
        User("charlie", "invalid-email", "123")
    )
    
    println("\n=== Data Processing ===")
    println("Valid users: ${users.getValidUsers()}")
    println("Invalid users: ${users.getInvalidUsers()}")
    println("User names: ${users.getUserNames()}")
    
    // Using extension functions for API responses
    val apiResponse = ApiResponse("success", "User data", 200)
    println("\n=== API Response ===")
    println("Is success: ${apiResponse.isSuccess()}")
    println("Is error: ${apiResponse.isError()}")
    println("Formatted response: ${apiResponse.formatResponse()}")
}

// Data classes
data class User(
    val name: String,
    val email: String,
    val phone: String
)

data class ApiResponse(
    val status: String,
    val data: String,
    val code: Int
)

// Simulated user manager
class UserManager {
    fun validateUser(user: User): Boolean {
        return user.name.isValidName() && 
               user.email.isValidEmail() && 
               user.phone.isValidPhone()
    }
}

// Extension functions for String validation
fun String.isValidName(): Boolean {
    return this.length >= 2 && this.all { it.isLetter() || it.isWhitespace() }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    return emailRegex.matches(this)
}

fun String.isValidPhone(): Boolean {
    return this.length == 10 && this.all { it.isDigit() }
}

// Extension functions for List<User>
fun List<User>.getValidUsers(): List<User> {
    return this.filter { user ->
        user.name.isValidName() && 
        user.email.isValidEmail() && 
        user.phone.isValidPhone()
    }
}

fun List<User>.getInvalidUsers(): List<User> {
    return this.filter { user ->
        !user.name.isValidName() || 
        !user.email.isValidEmail() || 
        !user.phone.isValidPhone()
    }
}

fun List<User>.getUserNames(): List<String> {
    return this.map { it.name.capitalize() }
}

// Extension functions for ApiResponse
fun ApiResponse.isSuccess(): Boolean {
    return this.status == "success" && this.code in 200..299
}

fun ApiResponse.isError(): Boolean {
    return this.status == "error" || this.code >= 400
}

fun ApiResponse.formatResponse(): String {
    return when {
        this.isSuccess() -> "✅ ${this.data}"
        this.isError() -> "❌ Error ${this.code}: ${this.data}"
        else -> "⚠️ ${this.data}"
    }
}
```

## 🔧 Android Development Notes

### Why Extension Functions Matter in Android:

1. **View Extensions** - Add convenience methods to Android Views
2. **String Utilities** - Add formatting and validation methods
3. **Collection Helpers** - Add custom operations to collections
4. **Context Extensions** - Add utility methods to Context
5. **Clean Code** - Make code more readable and maintainable

### Common Android Extension Patterns:

```kotlin
// View extensions
fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

// String extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// Context extensions
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Fragment extensions
fun Fragment.showSnackbar(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}
```

### Best Practices for Android:

1. **Use meaningful names** - Extension functions should clearly indicate their purpose
2. **Keep extensions focused** - Each extension should do one thing well
3. **Use appropriate scope** - Place extensions in the right package/class
4. **Document extensions** - Add comments for complex extensions
5. **Test extensions** - Write unit tests for your extensions

## 🎯 Exercises

### Easy Level

1. **Basic String Extensions**
   ```kotlin
   // Create extension functions for String:
   // - capitalize() - capitalize first letter
   // - reverse() - reverse the string
   // - isPalindrome() - check if string is palindrome
   // - repeat(times: Int) - repeat string n times
   ```

2. **Number Extensions**
   ```kotlin
   // Create extension functions for Int:
   // - isEven() - check if number is even
   // - isOdd() - check if number is odd
   // - squared() - return number squared
   // - isPositive() - check if number is positive
   ```

3. **List Extensions**
   ```kotlin
   // Create extension functions for List<Int>:
   // - getEvenNumbers() - return even numbers
   // - getOddNumbers() - return odd numbers
   // - sum() - return sum of all numbers
   // - average() - return average of all numbers
   ```

### Intermediate Level

1. **Validation Extensions**
   ```kotlin
   // Create validation extension functions:
   // - String.isValidEmail() - validate email format
   // - String.isValidPhone() - validate phone format
   // - String.isValidPassword() - validate password strength
   // - String.isValidUrl() - validate URL format
   ```

2. **Formatting Extensions**
   ```kotlin
   // Create formatting extension functions:
   // - Double.formatAsCurrency() - format as currency
   // - Double.formatAsPercentage() - format as percentage
   // - Long.toDateString() - convert timestamp to date string
   // - String.formatPhone() - format phone number
   ```

3. **Collection Processing**
   ```kotlin
   // Create collection processing extensions:
   // - List<T>.getDuplicates() - find duplicate elements
   // - List<T>.removeDuplicates() - remove duplicate elements
   // - List<T>.shuffle() - shuffle list elements
   // - List<T>.chunked(size: Int) - split into chunks
   ```

### Advanced Level

1. **Android View Extensions**
   ```kotlin
   // Create Android View extensions:
   // - View.visible(), View.gone(), View.invisible()
   // - TextView.setTextWithColor(text: String, color: Int)
   // - ImageView.loadFromUrl(url: String)
   // - ViewGroup.addViews(vararg views: View)
   ```

2. **Data Processing Pipeline**
   ```kotlin
   // Create a data processing pipeline with extensions:
   // - List<User>.filterValid() - filter valid users
   // - List<User>.sortByName() - sort by name
   // - List<User>.groupByAge() - group by age
   // - List<User>.toDisplayFormat() - convert to display format
   ```

3. **API Response Extensions**
   ```kotlin
   // Create API response extensions:
   // - ApiResponse<T>.isSuccess() - check if successful
   // - ApiResponse<T>.getDataOrNull() - get data safely
   // - ApiResponse<T>.getErrorMessage() - get error message
   // - ApiResponse<T>.toUiState() - convert to UI state
   ```

## 📝 Summary

### Key Takeaways:

1. **Extension Functions**:
   - Add functionality to existing classes
   - Use `fun ClassName.functionName()` syntax
   - Can access class properties and methods
   - Don't modify original class

2. **Extension Properties**:
   - Add computed properties to existing classes
   - Use `val ClassName.propertyName` syntax
   - Can be read-only or read-write
   - Provide convenient access to data

3. **Scope and Visibility**:
   - Extensions are scoped to their package
   - Can be imported and used anywhere
   - Follow Kotlin visibility rules
   - Can be private, internal, or public

4. **Best Practices**:
   - Use meaningful names
   - Keep extensions focused
   - Document complex extensions
   - Test your extensions

### Android-Specific Insights:

1. **View Extensions**: Add convenience methods to Android Views
2. **String Extensions**: Add validation and formatting utilities
3. **Context Extensions**: Add utility methods to Context
4. **Collection Extensions**: Add custom operations to collections
5. **Clean Code**: Make code more readable and maintainable

### Common Patterns:

```kotlin
// View extensions
fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }

// String extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

// Context extensions
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Collection extensions
fun List<Int>.sum(): Int = this.sum()
fun List<String>.joinWithCommas(): String = this.joinToString(", ")
```

### Next Steps:
You're now ready to learn about Scope Functions in the next topic, where you'll understand how to use let, run, apply, also, and with functions for cleaner, more readable code.

---

**Ready for the next topic?** Continue to [Topic 6: Scope Functions](06-scope-functions.md)
