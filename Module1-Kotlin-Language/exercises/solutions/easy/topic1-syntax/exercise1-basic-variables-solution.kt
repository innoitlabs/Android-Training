/*
 * Solution for Exercise 1: Basic Variable Declaration
 * 
 * This solution demonstrates:
 * - Proper variable declaration with appropriate data types
 * - Type inference usage
 * - String interpolation for output
 * - Mutable variable modification
 */

fun main() {
    // Create variables with appropriate data types
    val age = 25  // Int - type inferred
    val name = "John Doe"  // String - type inferred
    val isLearningAndroid = true  // Boolean - type inferred
    var experience = 2.5  // Double - type inferred, mutable
    val favoriteLanguage = 'K'  // Char - type inferred
    
    // Print all variables with descriptive labels
    println("Age: $age")
    println("Name: $name")
    println("Learning Android: $isLearningAndroid")
    println("Experience: $experience years")
    println("Favorite Language: $favoriteLanguage")
    
    // Demonstrate changing a mutable variable
    experience = 3.0  // Update the experience
    
    println("\nAfter updating experience:")
    println("Experience: $experience years")
}

/*
 * Expected Output:
 * Age: 25
 * Name: John Doe
 * Learning Android: true
 * Experience: 2.5 years
 * Favorite Language: K
 * 
 * After updating experience:
 * Experience: 3.0 years
 * 
 * Key Learning Points:
 * 1. Type inference works well for basic types
 * 2. Use 'val' for immutable variables (age, name, etc.)
 * 3. Use 'var' for mutable variables (experience)
 * 4. String interpolation with $ makes output clean
 * 5. Char literals use single quotes
 */
