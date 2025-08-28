# Topic 6: Collections in Kotlin

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Understand the difference between immutable and mutable collections
- Work with Lists, Sets, and Maps effectively
- Use functional operations like map, filter, forEach, reduce, and sortedBy
- Apply collections in real-world Android scenarios
- Write clean, functional code for data processing

## 📖 Explanation

### What are Collections?

**Collections** are containers that hold multiple values. Kotlin provides a rich set of collection types and operations that make data manipulation clean and efficient.

### Why Collections Matter in Android

Collections are essential in Android development for:
- **RecyclerView adapters** - Displaying lists of data
- **API responses** - Handling arrays and objects from network calls
- **User preferences** - Storing configuration data
- **Data processing** - Filtering, sorting, and transforming data
- **UI state management** - Managing lists of UI elements

### Immutable vs Mutable Collections

Kotlin distinguishes between:
- **Immutable collections** - Cannot be modified after creation (read-only)
- **Mutable collections** - Can be modified after creation (read-write)

This distinction helps prevent accidental modifications and makes code more predictable.

## 💻 Code Examples

### 1. Basic List Operations

```kotlin
fun main() {
    // Immutable List (read-only)
    val immutableList = listOf("Apple", "Banana", "Orange")
    println("Immutable list: $immutableList")
    
    // This would cause a compilation error:
    // immutableList.add("Grape")  // Error: List is immutable
    
    // Mutable List (read-write)
    val mutableList = mutableListOf("Apple", "Banana", "Orange")
    println("Original mutable list: $mutableList")
    
    // Adding elements
    mutableList.add("Grape")
    mutableList.add(1, "Mango")  // Insert at specific index
    println("After adding elements: $mutableList")
    
    // Removing elements
    mutableList.remove("Banana")
    mutableList.removeAt(0)  // Remove by index
    println("After removing elements: $mutableList")
    
    // Accessing elements
    val firstFruit = mutableList[0]  // Get by index
    val lastFruit = mutableList.last()  // Get last element
    println("First fruit: $firstFruit")
    println("Last fruit: $lastFruit")
    
    // Checking list properties
    println("List size: ${mutableList.size}")
    println("Is empty: ${mutableList.isEmpty()}")
    println("Contains 'Orange': ${mutableList.contains("Orange")}")
    
    // Iterating through list
    println("All fruits:")
    for (fruit in mutableList) {
        println("  - $fruit")
    }
    
    // Iterating with index
    for ((index, fruit) in mutableList.withIndex()) {
        println("  $index: $fruit")
    }
}
```

### 2. Functional Operations on Lists

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    // map - transform each element
    val doubledNumbers = numbers.map { it * 2 }
    println("Doubled numbers: $doubledNumbers")
    
    // filter - keep only elements that match a condition
    val evenNumbers = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    
    // forEach - perform action on each element
    println("Numbers with their squares:")
    numbers.forEach { number ->
        println("  $number squared is ${number * number}")
    }
    
    // reduce - combine all elements into a single value
    val sum = numbers.reduce { acc, number -> acc + number }
    println("Sum of all numbers: $sum")
    
    // sortedBy - sort elements by a property
    val fruits = listOf("Banana", "Apple", "Orange", "Grape")
    val sortedFruits = fruits.sortedBy { it.length }  // Sort by length
    println("Fruits sorted by length: $sortedFruits")
    
    // Chaining operations
    val result = numbers
        .filter { it % 2 == 0 }  // Keep even numbers
        .map { it * it }         // Square them
        .reduce { acc, num -> acc + num }  // Sum them
    println("Sum of squares of even numbers: $result")
    
    // find - find first element that matches condition
    val firstEven = numbers.find { it % 2 == 0 }
    println("First even number: $firstEven")
    
    // any - check if any element matches condition
    val hasEven = numbers.any { it % 2 == 0 }
    println("Has even numbers: $hasEven")
    
    // all - check if all elements match condition
    val allPositive = numbers.all { it > 0 }
    println("All numbers are positive: $allPositive")
}
```

### 3. Set Operations

```kotlin
fun main() {
    // Immutable Set (no duplicates)
    val immutableSet = setOf("Apple", "Banana", "Apple", "Orange")
    println("Immutable set: $immutableSet")  // Duplicates are automatically removed
    
    // Mutable Set
    val mutableSet = mutableSetOf("Apple", "Banana", "Orange")
    println("Original mutable set: $mutableSet")
    
    // Adding elements
    mutableSet.add("Grape")
    mutableSet.add("Apple")  // Won't add duplicate
    println("After adding elements: $mutableSet")
    
    // Set operations
    val set1 = setOf("Apple", "Banana", "Orange")
    val set2 = setOf("Banana", "Grape", "Mango")
    
    // Union - combine all elements
    val union = set1.union(set2)
    println("Union: $union")
    
    // Intersection - common elements
    val intersection = set1.intersect(set2)
    println("Intersection: $intersection")
    
    // Difference - elements in set1 but not in set2
    val difference = set1.subtract(set2)
    println("Difference: $difference")
    
    // Checking set properties
    println("Set size: ${mutableSet.size}")
    println("Contains 'Apple': ${mutableSet.contains("Apple")}")
    println("Is empty: ${mutableSet.isEmpty()}")
}
```

### 4. Map Operations

```kotlin
fun main() {
    // Immutable Map
    val immutableMap = mapOf(
        "Alice" to 25,
        "Bob" to 30,
        "Charlie" to 35
    )
    println("Immutable map: $immutableMap")
    
    // Mutable Map
    val mutableMap = mutableMapOf(
        "Alice" to 25,
        "Bob" to 30
    )
    println("Original mutable map: $mutableMap")
    
    // Adding/updating entries
    mutableMap["Charlie"] = 35  // Add new entry
    mutableMap["Bob"] = 31      // Update existing entry
    println("After modifications: $mutableMap")
    
    // Accessing values
    val aliceAge = mutableMap["Alice"]
    val davidAge = mutableMap["David"]  // Returns null if key doesn't exist
    println("Alice's age: $aliceAge")
    println("David's age: $davidAge")
    
    // Safe access with default value
    val safeAge = mutableMap["David"] ?: 0
    println("David's age (safe): $safeAge")
    
    // Iterating through map
    println("All users and ages:")
    for ((name, age) in mutableMap) {
        println("  $name: $age years old")
    }
    
    // Map operations
    val names = mutableMap.keys
    val ages = mutableMap.values
    println("Names: $names")
    println("Ages: $ages")
    
    // Functional operations on maps
    val adultUsers = mutableMap.filter { (_, age) -> age >= 18 }
    println("Adult users: $adultUsers")
    
    val ageList = mutableMap.map { (name, age) -> "$name is $age years old" }
    println("Age descriptions: $ageList")
    
    val totalAge = mutableMap.values.reduce { acc, age -> acc + age }
    println("Total age: $totalAge")
}
```

### 5. Collection Builders and Initialization

```kotlin
fun main() {
    // List builders
    val list1 = listOf("A", "B", "C")  // Immutable
    val list2 = mutableListOf("A", "B", "C")  // Mutable
    val list3 = arrayListOf("A", "B", "C")  // ArrayList (mutable)
    
    // Set builders
    val set1 = setOf("A", "B", "C")  // Immutable
    val set2 = mutableSetOf("A", "B", "C")  // Mutable
    val set3 = hashSetOf("A", "B", "C")  // HashSet (mutable)
    
    // Map builders
    val map1 = mapOf("A" to 1, "B" to 2)  // Immutable
    val map2 = mutableMapOf("A" to 1, "B" to 2)  // Mutable
    val map3 = hashMapOf("A" to 1, "B" to 2)  // HashMap (mutable)
    
    // Creating collections from ranges
    val numberList = (1..10).toList()
    println("Numbers from range: $numberList")
    
    // Creating collections from arrays
    val array = arrayOf("Apple", "Banana", "Orange")
    val listFromArray = array.toList()
    println("List from array: $listFromArray")
    
    // Creating collections with specific size
    val fixedSizeList = List(5) { it * 2 }  // [0, 2, 4, 6, 8]
    println("Fixed size list: $fixedSizeList")
    
    val mutableFixedList = MutableList(3) { "Item $it" }
    println("Mutable fixed list: $mutableFixedList")
}
```

### 6. Practical Android Example

```kotlin
fun main() {
    // Simulating Android RecyclerView adapter data
    val userAdapter = UserAdapter()
    
    // Simulate loading users from API
    val users = listOf(
        User("Alice", 25, "alice@email.com"),
        User("Bob", 30, "bob@email.com"),
        User("Charlie", 35, "charlie@email.com"),
        User("Diana", 28, "diana@email.com")
    )
    
    println("=== Loading Users ===")
    userAdapter.loadUsers(users)
    
    println("\n=== Filtering Users ===")
    userAdapter.filterUsersByAge(30)
    
    println("\n=== Searching Users ===")
    userAdapter.searchUsers("alice")
    
    println("\n=== Sorting Users ===")
    userAdapter.sortUsersByName()
}

// Simulated User data class
data class User(
    val name: String,
    val age: Int,
    val email: String
)

// Simulated RecyclerView adapter
class UserAdapter {
    private var allUsers = listOf<User>()
    private var displayedUsers = listOf<User>()
    
    fun loadUsers(users: List<User>) {
        allUsers = users
        displayedUsers = users
        println("Loaded ${users.size} users")
        displayUsers()
    }
    
    fun filterUsersByAge(maxAge: Int) {
        displayedUsers = allUsers.filter { it.age <= maxAge }
        println("Filtered users (age <= $maxAge):")
        displayUsers()
    }
    
    fun searchUsers(query: String) {
        displayedUsers = allUsers.filter { 
            it.name.contains(query, ignoreCase = true) ||
            it.email.contains(query, ignoreCase = true)
        }
        println("Search results for '$query':")
        displayUsers()
    }
    
    fun sortUsersByName() {
        displayedUsers = allUsers.sortedBy { it.name }
        println("Users sorted by name:")
        displayUsers()
    }
    
    private fun displayUsers() {
        displayedUsers.forEachIndexed { index, user ->
            println("  ${index + 1}. ${user.name} (${user.age}) - ${user.email}")
        }
    }
}
```

### 7. Advanced Collection Operations

```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    
    // groupBy - group elements by a property
    val groupedByEvenness = numbers.groupBy { it % 2 == 0 }
    println("Grouped by evenness: $groupedByEvenness")
    
    // partition - split into two lists based on condition
    val (evenNumbers, oddNumbers) = numbers.partition { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    println("Odd numbers: $oddNumbers")
    
    // flatMap - transform and flatten
    val words = listOf("hello", "world", "kotlin")
    val letters = words.flatMap { it.toList() }
    println("All letters: $letters")
    
    // distinct - remove duplicates
    val duplicates = listOf(1, 2, 2, 3, 3, 3, 4)
    val unique = duplicates.distinct()
    println("Unique numbers: $unique")
    
    // take and drop - get subset of collection
    val firstThree = numbers.take(3)
    val lastThree = numbers.takeLast(3)
    val withoutFirstThree = numbers.drop(3)
    println("First three: $firstThree")
    println("Last three: $lastThree")
    println("Without first three: $withoutFirstThree")
    
    // zip - combine two collections
    val names = listOf("Alice", "Bob", "Charlie")
    val ages = listOf(25, 30, 35)
    val nameAgePairs = names.zip(ages)
    println("Name-age pairs: $nameAgePairs")
    
    // fold - reduce with initial value
    val product = numbers.fold(1) { acc, num -> acc * num }
    println("Product of all numbers: $product")
    
    // associate - create map from collection
    val numberMap = numbers.associate { it to (it * it) }
    println("Number to square map: $numberMap")
}
```

## 🔧 Android Development Notes

### Why Collections Matter in Android:

1. **RecyclerView Adapters** - Display lists of data efficiently
2. **API Responses** - Handle JSON arrays and objects
3. **User Preferences** - Store and retrieve configuration data
4. **Data Processing** - Filter, sort, and transform data
5. **UI State Management** - Manage lists of UI elements

### Common Android Patterns:

```kotlin
// RecyclerView adapter data
val items = mutableListOf<Item>()
adapter.submitList(items)

// API response handling
val users = response.users.map { it.toUserModel() }

// User preferences
val settings = mapOf(
    "theme" to "dark",
    "notifications" to "enabled"
)

// Data filtering
val activeUsers = users.filter { it.isActive }

// Data transformation
val displayNames = users.map { it.displayName }
```

### Best Practices for Android:

1. **Use immutable collections by default** - Only use mutable when you need to modify
2. **Use functional operations** - map, filter, reduce for data processing
3. **Handle empty collections** - Always check for empty states
4. **Use appropriate collection types** - List for ordered data, Set for unique data, Map for key-value pairs
5. **Consider performance** - Use sequences for large collections

## 🎯 Exercises

### Easy Level

1. **Basic List Operations**
   ```kotlin
   // Create a program that:
   // - Creates a list of your favorite programming languages
   // - Adds and removes elements
   // - Prints the list in different ways
   // - Finds the longest language name
   ```

2. **Simple Data Processing**
   ```kotlin
   // Create a program that:
   // - Takes a list of numbers
   // - Filters even numbers
   // - Doubles the remaining numbers
   // - Calculates the sum
   ```

3. **Set Operations**
   ```kotlin
   // Create a program that:
   // - Creates two sets of programming languages
   // - Finds common languages (intersection)
   // - Finds unique languages in each set
   // - Combines both sets
   ```

### Intermediate Level

1. **User Management System**
   ```kotlin
   // Create a user management system that:
   // - Stores users in a list
   // - Filters users by age range
   // - Sorts users by name
   // - Searches users by email
   // - Groups users by age category
   ```

2. **Shopping Cart System**
   ```kotlin
   // Create a shopping cart that:
   // - Stores items with prices
   // - Calculates total cost
   // - Applies discounts
   // - Finds most expensive item
   // - Removes items by name
   ```

3. **Data Analysis Tool**
   ```kotlin
   // Create a data analysis tool that:
   // - Takes a list of numbers
   // - Calculates statistics (min, max, average)
   // - Finds outliers
   // - Groups numbers by range
   // - Creates frequency distribution
   ```

### Advanced Level

1. **Android App State Manager**
   ```kotlin
   // Create an app state manager that:
   // - Manages different UI states
   // - Handles user preferences
   // - Tracks navigation history
   // - Manages cached data
   // - Handles offline/online states
   ```

2. **API Response Processor**
   ```kotlin
   // Create an API response processor that:
   // - Parses different response types
   // - Handles pagination
   // - Caches responses
   // - Filters and sorts data
   // - Handles error states
   ```

3. **Data Pipeline System**
   ```kotlin
   // Create a data pipeline that:
   // - Processes data through multiple stages
   // - Validates data at each stage
   // - Handles errors gracefully
   // - Provides data transformation
   // - Supports different data sources
   ```

## 📝 Summary

### Key Takeaways:

1. **Collection Types**:
   - **List**: Ordered collection with duplicates
   - **Set**: Unordered collection without duplicates
   - **Map**: Key-value pairs

2. **Mutability**:
   - **Immutable**: Cannot be modified after creation
   - **Mutable**: Can be modified after creation
   - Use immutable by default

3. **Functional Operations**:
   - **map**: Transform elements
   - **filter**: Keep elements that match condition
   - **forEach**: Perform action on each element
   - **reduce**: Combine elements into single value
   - **sortedBy**: Sort elements by property

4. **Best Practices**:
   - Use appropriate collection types
   - Prefer immutable collections
   - Use functional operations for data processing
   - Handle empty collections
   - Consider performance for large collections

### Android-Specific Insights:

1. **RecyclerView**: Use lists for adapter data
2. **API Responses**: Use collections for JSON arrays
3. **User Preferences**: Use maps for key-value storage
4. **Data Processing**: Use functional operations for clean code
5. **Performance**: Use sequences for large data sets

### Common Patterns:

```kotlin
// RecyclerView data
val items = mutableListOf<Item>()
adapter.submitList(items)

// API response processing
val users = response.users.map { it.toUserModel() }

// Data filtering
val activeItems = items.filter { it.isActive }

// Data transformation
val displayData = items.map { it.toDisplayModel() }

// Data grouping
val groupedData = items.groupBy { it.category }
```

### Next Steps:
You're now ready to learn about Object-Oriented Programming in the next topic, where you'll understand how to design clean, maintainable code with classes, inheritance, and interfaces.

---

**Ready for the next topic?** Continue to [Topic 3: Object-Oriented Programming](03-oop.md)
