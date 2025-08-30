package com.example.testingqa

import com.example.testingqa.utils.Calculator
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    private lateinit var calculator: Calculator

    @Before
    fun setup() {
        calculator = Calculator()
    }

    @Test
    fun testAddition() {
        assertEquals(4, calculator.add(2, 2))
        assertEquals(0, calculator.add(-2, 2))
        assertEquals(-4, calculator.add(-2, -2))
        assertEquals(100, calculator.add(50, 50))
    }

    @Test
    fun testSubtraction() {
        assertEquals(0, calculator.subtract(2, 2))
        assertEquals(-4, calculator.subtract(-2, 2))
        assertEquals(0, calculator.subtract(-2, -2))
        assertEquals(10, calculator.subtract(15, 5))
    }

    @Test
    fun testMultiplication() {
        assertEquals(4, calculator.multiply(2, 2))
        assertEquals(0, calculator.multiply(0, 5))
        assertEquals(-6, calculator.multiply(2, -3))
        assertEquals(100, calculator.multiply(10, 10))
    }

    @Test
    fun testDivision() {
        assertEquals(2.0, calculator.divide(4, 2), 0.001)
        assertEquals(0.5, calculator.divide(1, 2), 0.001)
        assertEquals(-2.0, calculator.divide(-4, 2), 0.001)
        assertEquals(0.0, calculator.divide(0, 5), 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDivisionByZero() {
        calculator.divide(5, 0)
    }

    @Test
    fun testPower() {
        assertEquals(1, calculator.power(5, 0))
        assertEquals(5, calculator.power(5, 1))
        assertEquals(25, calculator.power(5, 2))
        assertEquals(125, calculator.power(5, 3))
        assertEquals(1, calculator.power(1, 100))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPowerWithNegativeExponent() {
        calculator.power(5, -1)
    }

    @Test
    fun testFactorial() {
        assertEquals(1L, calculator.factorial(0))
        assertEquals(1L, calculator.factorial(1))
        assertEquals(2L, calculator.factorial(2))
        assertEquals(6L, calculator.factorial(3))
        assertEquals(24L, calculator.factorial(4))
        assertEquals(120L, calculator.factorial(5))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testFactorialWithNegativeNumber() {
        calculator.factorial(-1)
    }
}
