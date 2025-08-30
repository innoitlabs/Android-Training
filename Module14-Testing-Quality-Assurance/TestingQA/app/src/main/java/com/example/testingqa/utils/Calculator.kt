package com.example.testingqa.utils

class Calculator {
    fun add(a: Int, b: Int): Int = a + b
    
    fun subtract(a: Int, b: Int): Int = a - b
    
    fun multiply(a: Int, b: Int): Int = a * b
    
    fun divide(a: Int, b: Int): Double {
        require(b != 0) { "Division by zero is not allowed" }
        return a.toDouble() / b
    }
    
    fun power(base: Int, exponent: Int): Int {
        require(exponent >= 0) { "Exponent must be non-negative" }
        return when (exponent) {
            0 -> 1
            1 -> base
            else -> {
                var result = 1
                repeat(exponent) { result *= base }
                result
            }
        }
    }
    
    fun factorial(n: Int): Long {
        require(n >= 0) { "Factorial is not defined for negative numbers" }
        return when (n) {
            0, 1 -> 1
            else -> {
                var result = 1L
                for (i in 2..n) {
                    result *= i
                }
                result
            }
        }
    }
}
