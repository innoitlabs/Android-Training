# Topic 1: Kotlin Syntax and Basic Data Types

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Understand Kotlin's clean and concise syntax
- Work with all basic data types (Int, String, Boolean, Double, Float, Char)
- Use Arrays and understand their properties
- Write Kotlin code that's ready for Android development

## 📖 Explanation

### What is Kotlin?
Kotlin is a modern programming language that runs on the Java Virtual Machine (JVM). It was designed by JetBrains (creators of Android Studio) to be:
- **Concise**: Less boilerplate code than Java
- **Safe**: Null safety built into the type system
- **Interoperable**: Works seamlessly with Java
- **Android-friendly**: Official language for Android development

### Kotlin Syntax Basics
- **Semicolons are optional** (but you can use them)
- **Functions start with `fun`**
- **Main function is the entry point**
- **Comments use `//` for single line and `/* */` for multi-line**

### Data Types in Kotlin
Kotlin has two categories of types:
1. **Primitive types**: Int, Double, Float, Boolean, Char
2. **Reference types**: String, Array, and all custom classes

**Key Point**: In Kotlin, even primitive types are objects (unlike Java), which means you can call methods on them!

## 💻 Code Examples

### 1. Basic Kotlin Program Structure

```kotlin
// This is a single-line comment
/*
   This is a multi-line comment
   You can write multiple lines here
*/

fun main() {
    // This is the entry point of your program
    println("Hello, Kotlin!") // println is like System.out.println in Java
}
```

### 2. Integer Types (Int, Long, Short, Byte)

```kotlin
fun main() {
    // Int: 32-bit integer (most commonly used)
    val age: Int = 25
    val temperature = -5 // Type inference: Kotlin knows this is Int
    
    // Long: 64-bit integer (for large numbers)
    val population: Long = 7_800_000_000L // Underscores for readability
    val bigNumber = 123456789L // 'L' suffix for Long
    
    // Short: 16-bit integer
    val year: Short = 2024
    
    // Byte: 8-bit integer
    val grade: Byte = 95
    
    println("Age: $age") // String interpolation with $
    println("Population: $population")
    println("Year: $year")
    println("Grade: $grade")
}
```

### 3. Floating-Point Types (Double, Float)

```kotlin
fun main() {
    // Double: 64-bit floating point (default for decimals)
    val pi: Double = 3.14159265359
    val price = 19.99 // Type inference: Kotlin knows this is Double
    
    // Float: 32-bit floating point (use 'f' suffix)
    val temperature: Float = 98.6f
    val weight = 65.5f
    
    println("Pi: $pi")
    println("Price: $price")
    println("Temperature: $temperature")
    println("Weight: $weight")
    
    // Be careful with floating-point precision
    val result = 0.1 + 0.2
    println("0.1 + 0.2 = $result") // Might not be exactly 0.3
}
```

### 4. Boolean Type

```kotlin
fun main() {
    // Boolean: true or false
    val isAndroidDeveloper: Boolean = true
    val isLearningKotlin = true // Type inference
    
    val hasExperience = false
    val isReady = true
    
    println("Is Android Developer: $isAndroidDeveloper")
    println("Is Learning Kotlin: $isLearningKotlin")
    println("Has Experience: $hasExperience")
    println("Is Ready: $isReady")
    
    // Boolean operations
    val andResult = isAndroidDeveloper && isLearningKotlin // AND
    val orResult = hasExperience || isReady // OR
    val notResult = !hasExperience // NOT
    
    println("AND result: $andResult")
    println("OR result: $orResult")
    println("NOT result: $notResult")
}
```

### 5. Character Type (Char)

```kotlin
fun main() {
    // Char: single Unicode character (use single quotes)
    val firstLetter: Char = 'A'
    val digit = '5'
    val symbol = '$'
    val emoji = '😀' // Unicode emoji
    
    println("First letter: $firstLetter")
    println("Digit: $digit")
    println("Symbol: $symbol")
    println("Emoji: $emoji")
    
    // Char operations
    val nextLetter = firstLetter + 1 // 'B'
    val isLetter = firstLetter.isLetter() // true
    val isDigit = digit.isDigit() // true
    
    println("Next letter: $nextLetter")
    println("Is letter: $isLetter")
    println("Is digit: $isDigit")
}
```

### 6. String Type

```kotlin
fun main() {
    // String: sequence of characters (use double quotes)
    val name: String = "Android Developer"
    val message = "Welcome to Kotlin!" // Type inference
    
    // String interpolation (much better than Java's concatenation)
    val greeting = "Hello, $name!"
    val calculation = "2 + 3 = ${2 + 3}" // Expressions in ${}
    
    println(greeting)
    println(calculation)
    
    // Multi-line strings (useful for long text)
    val longMessage = """
        This is a multi-line string.
        You can write multiple lines
        without using \n escape characters.
        Perfect for HTML, JSON, or long messages.
    """.trimIndent() // Removes common leading whitespace
    
    println(longMessage)
    
    // String methods
    val text = "  Hello, Kotlin!  "
    println("Original: '$text'")
    println("Length: ${text.length}")
    println("Uppercase: ${text.uppercase()}")
    println("Lowercase: ${text.lowercase()}")
    println("Trimmed: '${text.trim()}'")
    println("Contains 'Kotlin': ${text.contains("Kotlin")}")
}
```

### 7. Arrays

```kotlin
fun main() {
    // Array: fixed-size collection of elements
    val numbers: Array<Int> = arrayOf(1, 2, 3, 4, 5)
    val names = arrayOf("Alice", "Bob", "Charlie") // Type inference
    
    // Accessing elements (zero-indexed)
    println("First number: ${numbers[0]}")
    println("Second name: ${names[1]}")
    
    // Array size
    println("Numbers array size: ${numbers.size}")
    
    // Iterating through arrays
    println("All numbers:")
    for (number in numbers) {
        println(number)
    }
    
    // Array with specific size and default value
    val scores = Array(5) { 0 } // Array of 5 zeros
    val temperatures = Array(7) { 20.0 } // Array of 7 doubles with value 20.0
    
    println("Scores: ${scores.contentToString()}")
    println("Temperatures: ${temperatures.contentToString()}")
    
    // Modifying array elements
    scores[0] = 95
    scores[1] = 87
    println("Updated scores: ${scores.contentToString()}")
    
    // Array of different types (not recommended, but possible)
    val mixed = arrayOf("Hello", 42, true, 3.14)
    println("Mixed array: ${mixed.contentToString()}")
}
```

## 🔧 Android Development Notes

### Why These Concepts Matter in Android:

1. **Type Safety**: Kotlin's type system helps prevent crashes in Android apps
2. **String Interpolation**: Makes UI text formatting much cleaner than Java
3. **Arrays**: Used for storing data like user preferences, sensor readings, etc.
4. **Boolean**: Essential for UI state management (isEnabled, isVisible, etc.)

### Common Android Use Cases:
- **Int**: Screen dimensions, resource IDs, counts
- **String**: User input, display text, API responses
- **Boolean**: Toggle states, feature flags, validation results
- **Double/Float**: Sensor data, calculations, coordinates
- **Array**: Storing lists of data before converting to more flexible collections

## 🎯 Exercises

### Easy Level

1. **Variable Declaration Practice**
   ```kotlin
   // Create variables for the following:
   // - Your age (Int)
   // - Your name (String)
   // - Whether you're learning Android (Boolean)
   // - Your programming experience in years (Double)
   // - Your favorite programming language (Char - first letter)
   ```

2. **String Operations**
   ```kotlin
   // Create a string with your full name
   // Print it in uppercase
   // Print it in lowercase
   // Print its length
   // Check if it contains your first name
   ```

3. **Array Basics**
   ```kotlin
   // Create an array of 5 programming languages you know
   // Print the first and last language
   // Print the total number of languages
   // Print all languages using a loop
   ```

### Intermediate Level

1. **Temperature Converter**
   ```kotlin
   // Create a program that:
   // - Stores a temperature in Celsius (Double)
   // - Converts it to Fahrenheit using the formula: (C * 9/5) + 32
   // - Prints both temperatures with proper formatting
   // - Uses string interpolation for the output
   ```

2. **User Profile**
   ```kotlin
   // Create variables for a user profile:
   // - Name (String)
   // - Age (Int)
   // - Height in meters (Double)
   // - Is premium user (Boolean)
   // - Programming skills (Array of Strings)
   // 
   // Print a formatted profile summary
   ```

3. **Number Operations**
   ```kotlin
   // Create an array of 10 random numbers (1-100)
   // Find and print:
   // - The largest number
   // - The smallest number
   // - The average of all numbers
   // - How many even numbers are in the array
   ```

### Advanced Level

1. **Data Validation System**
   ```kotlin
   // Create a system that validates user input:
   // - Email (String): must contain '@' and '.'
   // - Age (Int): must be between 0 and 120
   // - Score (Double): must be between 0.0 and 100.0
   // - Username (String): must be 3-20 characters, alphanumeric only
   // 
   // Test with valid and invalid inputs
   ```

2. **Grade Calculator**
   ```kotlin
   // Create a grade calculator that:
   // - Takes an array of test scores (Double)
   // - Calculates the average
   // - Assigns a letter grade (A: 90+, B: 80-89, C: 70-79, D: 60-69, F: <60)
   // - Prints a detailed report with all scores, average, and grade
   ```

3. **Text Analyzer**
   ```kotlin
   // Create a text analyzer that:
   // - Takes a string input
   // - Counts characters, words, sentences
   // - Finds the most common character
   // - Checks if it's a palindrome
   // - Provides a readability score
   ```

## 📝 Summary

### Key Takeaways:

1. **Kotlin Syntax**:
   - Clean and concise syntax
   - Optional semicolons
   - Functions start with `fun`
   - Main function is the entry point

2. **Data Types**:
   - **Int**: Whole numbers (most common)
   - **Double**: Decimal numbers (default for decimals)
   - **Float**: Decimal numbers with 'f' suffix
   - **Boolean**: true/false values
   - **Char**: Single characters (single quotes)
   - **String**: Text (double quotes)
   - **Array**: Fixed-size collections

3. **Type Inference**: Kotlin can often guess the type, so you don't need to specify it explicitly

4. **String Interpolation**: Use `$variable` or `${expression}` for clean string formatting

5. **Arrays**: Fixed-size collections accessed by index, starting from 0

### Best Practices:
- Use type inference when the type is obvious
- Prefer `Double` over `Float` unless you need to save memory
- Use string interpolation instead of concatenation
- Use meaningful variable names
- Add comments to explain complex logic

### Next Steps:
You're now ready to learn about variables and constants in the next topic, where you'll understand the difference between `var` and `val` and why immutability is important in Android development.

---

**Ready for the next topic?** Continue to [Topic 2: Variables and Constants](02-variables-constants.md)
