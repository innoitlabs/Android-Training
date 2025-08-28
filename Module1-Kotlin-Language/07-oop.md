# Topic 7: Object-Oriented Programming

## 🎯 Learning Objectives

By the end of this topic, you will be able to:
- Create and use classes and objects in Kotlin
- Understand primary and secondary constructors
- Implement inheritance and override methods
- Work with interfaces and abstract classes
- Design clean, maintainable object-oriented code for Android

## 📖 Explanation

### What is Object-Oriented Programming?

**Object-Oriented Programming (OOP)** is a programming paradigm that organizes code into objects that contain data and behavior. It helps create modular, reusable, and maintainable code.

### Why OOP Matters in Android

OOP is fundamental in Android development because:
- **Activities and Fragments** are classes that represent UI screens
- **ViewModels** encapsulate UI-related data and logic
- **Adapters** handle data binding and user interactions
- **Services** provide background functionality
- **Data classes** represent API responses and database entities

### Key OOP Concepts in Kotlin

Kotlin enhances traditional OOP with:
- **Data classes** - Automatic boilerplate code generation
- **Primary constructors** - Concise object initialization
- **Smart casts** - Automatic type checking and casting
- **Interface delegation** - Composition over inheritance
- **Sealed classes** - Restricted class hierarchies

## 💻 Code Examples

### 1. Basic Classes and Objects

```kotlin
fun main() {
    // Creating objects from classes
    val person1 = Person("Alice", 25)
    val person2 = Person("Bob", 30)
    
    // Accessing properties
    println("${person1.name} is ${person1.age} years old")
    println("${person2.name} is ${person2.age} years old")
    
    // Calling methods
    person1.greet()
    person2.greet()
    
    // Using computed properties
    println("${person1.name} is ${person1.ageCategory}")
    println("${person2.name} is ${person2.ageCategory}")
    
    // Modifying properties
    person1.age = 26
    println("${person1.name} is now ${person1.age} years old")
}

// Basic class definition
class Person(
    val name: String,  // Immutable property
    var age: Int       // Mutable property
) {
    // Computed property
    val ageCategory: String
        get() = when {
            age < 18 -> "Minor"
            age < 65 -> "Adult"
            else -> "Senior"
        }
    
    // Method
    fun greet() {
        println("Hello, my name is $name!")
    }
    
    // Method with parameters
    fun introduceTo(other: Person) {
        println("Hi ${other.name}, I'm $name!")
    }
}
```

### 2. Constructors (Primary and Secondary)

```kotlin
fun main() {
    // Using primary constructor
    val user1 = User("alice@email.com", "Alice", 25)
    
    // Using secondary constructor
    val user2 = User("bob@email.com", "Bob")
    val user3 = User("charlie@email.com")
    
    // Using factory method
    val user4 = User.createGuest()
    
    println("User 1: $user1")
    println("User 2: $user2")
    println("User 3: $user3")
    println("User 4: $user4")
}

// Class with primary constructor and default parameters
class User(
    val email: String,
    val name: String = "Unknown",
    val age: Int = 0
) {
    // Secondary constructor
    constructor(email: String, name: String) : this(email, name, 0)
    
    // Another secondary constructor
    constructor(email: String) : this(email, "Unknown", 0)
    
    // Factory method (companion object)
    companion object {
        fun createGuest(): User {
            return User("guest@example.com", "Guest", 0)
        }
    }
    
    // toString method for nice printing
    override fun toString(): String {
        return "User(email='$email', name='$name', age=$age)"
    }
}
```

### 3. Inheritance

```kotlin
fun main() {
    // Creating objects of different types
    val animal = Animal("Generic Animal", 5)
    val dog = Dog("Buddy", 3, "Golden Retriever")
    val cat = Cat("Whiskers", 2, "Orange")
    
    // Calling inherited methods
    animal.makeSound()
    dog.makeSound()
    cat.makeSound()
    
    // Using polymorphic behavior
    val animals = listOf(animal, dog, cat)
    animals.forEach { it.makeSound() }
    
    // Accessing specific properties
    println("Dog breed: ${dog.breed}")
    println("Cat color: ${cat.color}")
    
    // Using overridden methods
    println("Animal info: ${animal.getInfo()}")
    println("Dog info: ${dog.getInfo()}")
    println("Cat info: ${cat.getInfo()}")
}

// Base class (open keyword allows inheritance)
open class Animal(
    val name: String,
    val age: Int
) {
    // Method that can be overridden
    open fun makeSound() {
        println("$name makes a sound")
    }
    
    // Method that can be overridden
    open fun getInfo(): String {
        return "Animal: $name, Age: $age"
    }
    
    // Final method (cannot be overridden)
    fun isOld(): Boolean {
        return age > 10
    }
}

// Derived class (inherits from Animal)
class Dog(
    name: String,
    age: Int,
    val breed: String
) : Animal(name, age) {  // Call to parent constructor
    
    // Override parent method
    override fun makeSound() {
        println("$name barks: Woof! Woof!")
    }
    
    // Override parent method
    override fun getInfo(): String {
        return "Dog: $name, Age: $age, Breed: $breed"
    }
    
    // New method specific to Dog
    fun fetch() {
        println("$name fetches the ball")
    }
}

// Another derived class
class Cat(
    name: String,
    age: Int,
    val color: String
) : Animal(name, age) {
    
    override fun makeSound() {
        println("$name meows: Meow!")
    }
    
    override fun getInfo(): String {
        return "Cat: $name, Age: $age, Color: $color"
    }
    
    // New method specific to Cat
    fun purr() {
        println("$name purrs contentedly")
    }
}
```

### 4. Interfaces

```kotlin
fun main() {
    // Creating objects that implement interfaces
    val car = Car("Toyota", "Camry")
    val bicycle = Bicycle("Mountain Bike")
    val plane = Plane("Boeing", "747")
    
    // Using interface methods
    car.start()
    car.stop()
    car.getFuelLevel()
    
    bicycle.start()
    bicycle.stop()
    
    plane.start()
    plane.stop()
    plane.getFuelLevel()
    
    // Polymorphic behavior with interfaces
    val vehicles = listOf<Vehicle>(car, bicycle, plane)
    vehicles.forEach { vehicle ->
        vehicle.start()
        vehicle.stop()
    }
    
    // Using interface with properties
    val electricCar = ElectricCar("Tesla", "Model 3")
    electricCar.start()
    println("Battery level: ${electricCar.batteryLevel}%")
}

// Interface definition
interface Vehicle {
    fun start()
    fun stop()
}

// Interface with properties
interface FuelVehicle {
    val fuelLevel: Int
    fun getFuelLevel(): Int
}

// Interface with default implementation
interface ElectricVehicle {
    val batteryLevel: Int
        get() = 100  // Default implementation
    
    fun charge() {
        println("Charging...")
    }
}

// Class implementing multiple interfaces
class Car(
    val brand: String,
    val model: String
) : Vehicle, FuelVehicle {
    
    private var fuel = 50
    
    override val fuelLevel: Int
        get() = fuel
    
    override fun start() {
        println("$brand $model engine starts")
    }
    
    override fun stop() {
        println("$brand $model engine stops")
    }
    
    override fun getFuelLevel(): Int {
        println("Fuel level: $fuel%")
        return fuel
    }
}

// Simple interface implementation
class Bicycle(val type: String) : Vehicle {
    override fun start() {
        println("$type starts pedaling")
    }
    
    override fun stop() {
        println("$type stops pedaling")
    }
}

// Class implementing multiple interfaces
class Plane(
    val manufacturer: String,
    val model: String
) : Vehicle, FuelVehicle {
    
    private var fuel = 80
    
    override val fuelLevel: Int
        get() = fuel
    
    override fun start() {
        println("$manufacturer $model engines start")
    }
    
    override fun stop() {
        println("$manufacturer $model engines stop")
    }
    
    override fun getFuelLevel(): Int {
        println("Fuel level: $fuel%")
        return fuel
    }
}

// Class implementing interface with default implementation
class ElectricCar(
    val brand: String,
    val model: String
) : Vehicle, ElectricVehicle {
    
    override fun start() {
        println("$brand $model electric motor starts")
    }
    
    override fun stop() {
        println("$brand $model electric motor stops")
    }
}
```

### 5. Abstract Classes

```kotlin
fun main() {
    // Cannot create instance of abstract class
    // val shape = Shape()  // This would cause an error
    
    // Creating concrete implementations
    val circle = Circle(5.0)
    val rectangle = Rectangle(4.0, 6.0)
    val triangle = Triangle(3.0, 4.0, 5.0)
    
    // Using abstract class methods
    val shapes = listOf<Shape>(circle, rectangle, triangle)
    shapes.forEach { shape ->
        println("${shape.name}: Area = ${shape.calculateArea()}, Perimeter = ${shape.calculatePerimeter()}")
    }
    
    // Using concrete methods from abstract class
    circle.displayInfo()
    rectangle.displayInfo()
    triangle.displayInfo()
}

// Abstract class
abstract class Shape(
    val name: String
) {
    // Abstract properties (must be implemented by subclasses)
    abstract val area: Double
    abstract val perimeter: Double
    
    // Abstract methods (must be implemented by subclasses)
    abstract fun calculateArea(): Double
    abstract fun calculatePerimeter(): Double
    
    // Concrete methods (can be used by subclasses)
    fun displayInfo() {
        println("Shape: $name")
        println("Area: $area")
        println("Perimeter: $perimeter")
    }
    
    // Concrete method with default implementation
    open fun getDescription(): String {
        return "A $name shape"
    }
}

// Concrete implementation
class Circle(val radius: Double) : Shape("Circle") {
    override val area: Double
        get() = Math.PI * radius * radius
    
    override val perimeter: Double
        get() = 2 * Math.PI * radius
    
    override fun calculateArea(): Double {
        return area
    }
    
    override fun calculatePerimeter(): Double {
        return perimeter
    }
    
    override fun getDescription(): String {
        return "A circle with radius $radius"
    }
}

// Another concrete implementation
class Rectangle(
    val width: Double,
    val height: Double
) : Shape("Rectangle") {
    
    override val area: Double
        get() = width * height
    
    override val perimeter: Double
        get() = 2 * (width + height)
    
    override fun calculateArea(): Double {
        return area
    }
    
    override fun calculatePerimeter(): Double {
        return perimeter
    }
}

// Another concrete implementation
class Triangle(
    val sideA: Double,
    val sideB: Double,
    val sideC: Double
) : Shape("Triangle") {
    
    override val area: Double
        get() {
            val s = (sideA + sideB + sideC) / 2
            return Math.sqrt(s * (s - sideA) * (s - sideB) * (s - sideC))
        }
    
    override val perimeter: Double
        get() = sideA + sideB + sideC
    
    override fun calculateArea(): Double {
        return area
    }
    
    override fun calculatePerimeter(): Double {
        return perimeter
    }
}
```

### 6. Practical Android Example

```kotlin
fun main() {
    // Simulating Android app architecture
    val userRepository = UserRepository()
    val userViewModel = UserViewModel(userRepository)
    val userActivity = UserActivity(userViewModel)
    
    // Simulate user interactions
    println("=== Loading Users ===")
    userActivity.loadUsers()
    
    println("\n=== Adding User ===")
    userActivity.addUser("Alice", "alice@email.com")
    
    println("\n=== Updating User ===")
    userActivity.updateUser(1, "Alice Updated", "alice.updated@email.com")
    
    println("\n=== Deleting User ===")
    userActivity.deleteUser(1)
}

// Data class for user
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Repository pattern (data layer)
class UserRepository {
    private val users = mutableListOf<User>()
    private var nextId = 1
    
    fun getUsers(): List<User> {
        return users.toList()
    }
    
    fun addUser(name: String, email: String): User {
        val user = User(nextId++, name, email)
        users.add(user)
        return user
    }
    
    fun updateUser(id: Int, name: String, email: String): User? {
        val index = users.indexOfFirst { it.id == id }
        return if (index != -1) {
            val updatedUser = User(id, name, email)
            users[index] = updatedUser
            updatedUser
        } else null
    }
    
    fun deleteUser(id: Int): Boolean {
        return users.removeIf { it.id == id }
    }
}

// ViewModel (business logic layer)
class UserViewModel(
    private val repository: UserRepository
) {
    private val _users = mutableListOf<User>()
    val users: List<User> get() = _users.toList()
    
    fun loadUsers() {
        _users.clear()
        _users.addAll(repository.getUsers())
    }
    
    fun addUser(name: String, email: String) {
        val user = repository.addUser(name, email)
        _users.add(user)
    }
    
    fun updateUser(id: Int, name: String, email: String): Boolean {
        val updatedUser = repository.updateUser(id, name, email)
        if (updatedUser != null) {
            val index = _users.indexOfFirst { it.id == id }
            if (index != -1) {
                _users[index] = updatedUser
            }
            return true
        }
        return false
    }
    
    fun deleteUser(id: Int): Boolean {
        val success = repository.deleteUser(id)
        if (success) {
            _users.removeIf { it.id == id }
        }
        return success
    }
}

// Activity (UI layer)
class UserActivity(
    private val viewModel: UserViewModel
) {
    fun loadUsers() {
        viewModel.loadUsers()
        displayUsers()
    }
    
    fun addUser(name: String, email: String) {
        viewModel.addUser(name, email)
        println("User added successfully")
        displayUsers()
    }
    
    fun updateUser(id: Int, name: String, email: String) {
        val success = viewModel.updateUser(id, name, email)
        if (success) {
            println("User updated successfully")
            displayUsers()
        } else {
            println("Failed to update user")
        }
    }
    
    fun deleteUser(id: Int) {
        val success = viewModel.deleteUser(id)
        if (success) {
            println("User deleted successfully")
            displayUsers()
        } else {
            println("Failed to delete user")
        }
    }
    
    private fun displayUsers() {
        println("Current users:")
        viewModel.users.forEach { user ->
            println("  ${user.id}: ${user.name} (${user.email})")
        }
    }
}
```

## 🔧 Android Development Notes

### Why OOP is Critical in Android:

1. **Activities and Fragments** - Represent UI screens as classes
2. **ViewModels** - Encapsulate UI-related data and business logic
3. **Repositories** - Handle data operations and caching
4. **Services** - Provide background functionality
5. **Adapters** - Handle data binding and user interactions

### Common Android Patterns:

```kotlin
// Activity class
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}

// ViewModel class
class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<List<Item>>()
    val data: LiveData<List<Item>> = _data
    
    fun loadData() {
        // Load data logic
    }
}

// Repository interface
interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun saveUser(user: User)
}

// Repository implementation
class UserRepositoryImpl : UserRepository {
    override suspend fun getUsers(): List<User> {
        // Implementation
    }
    
    override suspend fun saveUser(user: User) {
        // Implementation
    }
}
```

### Best Practices for Android:

1. **Use data classes** - For API responses and database entities
2. **Implement interfaces** - For dependency injection and testing
3. **Use abstract classes** - For shared functionality between related classes
4. **Follow SOLID principles** - Single responsibility, open/closed, etc.
5. **Use composition over inheritance** - When possible

## 🎯 Exercises

### Easy Level

1. **Basic Class Creation**
   ```kotlin
   // Create a Book class that:
   // - Has properties: title, author, year, pages
   // - Has methods: getInfo(), isOld(), isLong()
   // - Create several Book objects and test the methods
   ```

2. **Simple Inheritance**
   ```kotlin
   // Create a base class Animal with:
   // - Properties: name, age
   // - Methods: makeSound(), getInfo()
   // - Create Dog and Cat classes that inherit from Animal
   // - Override methods appropriately
   ```

3. **Interface Implementation**
   ```kotlin
   // Create interfaces: Drawable, Movable
   // - Drawable: draw() method
   // - Movable: move() method
   // - Create classes that implement these interfaces
   ```

### Intermediate Level

1. **Library Management System**
   ```kotlin
   // Create a library system with:
   // - Abstract class: Item (with title, id)
   // - Classes: Book, DVD, Magazine (inherit from Item)
   // - Interface: Borrowable (with borrow, return methods)
   // - Class: Library (manages items and borrowing)
   ```

2. **Bank Account System**
   ```kotlin
   // Create a bank system with:
   // - Abstract class: Account (with balance, accountNumber)
   // - Classes: SavingsAccount, CheckingAccount
   // - Interface: Transactionable (with deposit, withdraw)
   // - Different interest rates and withdrawal limits
   ```

3. **Shape Calculator**
   ```kotlin
   // Create a shape system with:
   // - Abstract class: Shape (with area, perimeter)
   // - Classes: Circle, Rectangle, Triangle
   // - Interface: Scalable (with scale method)
   // - Calculate areas and perimeters
   ```

### Advanced Level

1. **Android App Architecture**
   ```kotlin
   // Create a complete app architecture with:
   // - Data classes for models
   // - Repository interface and implementation
   // - ViewModel with LiveData
   // - Activity/Fragment classes
   // - Dependency injection setup
   ```

2. **Plugin System**
   ```kotlin
   // Create a plugin system with:
   // - Interface: Plugin (with execute method)
   // - Abstract class: BasePlugin (with common functionality)
   // - Multiple plugin implementations
   // - Plugin manager to handle plugins
   ```

3. **Game Engine**
   ```kotlin
   // Create a simple game engine with:
   // - Abstract class: GameObject (with position, update method)
   // - Classes: Player, Enemy, Item (inherit from GameObject)
   // - Interface: Collidable (with collision detection)
   // - Game world to manage all objects
   ```

## 📝 Summary

### Key Takeaways:

1. **Classes and Objects**:
   - Classes are blueprints for objects
   - Objects are instances of classes
   - Properties store data, methods define behavior

2. **Constructors**:
   - Primary constructor is part of class declaration
   - Secondary constructors provide alternative initialization
   - Default parameters reduce constructor overloads

3. **Inheritance**:
   - Use `open` keyword to allow inheritance
   - Use `override` keyword to override methods
   - Use `super` to call parent methods

4. **Interfaces**:
   - Define contracts that classes must implement
   - Can have default implementations
   - Support multiple inheritance

5. **Abstract Classes**:
   - Cannot be instantiated directly
   - Can have abstract and concrete members
   - Provide shared functionality for subclasses

### Android-Specific Insights:

1. **Activities**: Represent UI screens as classes
2. **ViewModels**: Encapsulate UI-related data and logic
3. **Repositories**: Handle data operations and caching
4. **Interfaces**: Enable dependency injection and testing
5. **Inheritance**: Use for related UI components

### Common Patterns:

```kotlin
// Activity class
class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

// ViewModel class
class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<List<Item>>()
    val data: LiveData<List<Item>> = _data
}

// Repository interface
interface UserRepository {
    suspend fun getUsers(): List<User>
}

// Data class
data class User(val id: Int, val name: String, val email: String)
```

### Next Steps:
You're now ready to learn about Data Classes and Sealed Classes in the next topic, where you'll understand how to create type-safe models and handle different states in your applications.

---

**Ready for the next topic?** Continue to [Topic 4: Data Classes and Sealed Classes](04-data-sealed-classes.md)
