package com.example.testingqa.utils

class StringUtils {
    fun reverse(input: String): String {
        return input.reversed()
    }
    
    fun isPalindrome(input: String): Boolean {
        val cleaned = input.lowercase().replace(Regex("[^a-z0-9]"), "")
        return cleaned == cleaned.reversed()
    }
    
    fun countVowels(input: String): Int {
        val vowels = setOf('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U')
        return input.count { it in vowels }
    }
    
    fun capitalizeWords(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            if (word.isNotEmpty()) {
                word[0].uppercase() + word.substring(1).lowercase()
            } else {
                word
            }
        }
    }
    
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
    
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
