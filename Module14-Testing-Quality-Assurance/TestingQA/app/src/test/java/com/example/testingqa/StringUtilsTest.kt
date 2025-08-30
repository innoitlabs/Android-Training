package com.example.testingqa

import com.example.testingqa.utils.StringUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StringUtilsTest {
    private lateinit var stringUtils: StringUtils

    @Before
    fun setup() {
        stringUtils = StringUtils()
    }

    @Test
    fun testReverse() {
        assertEquals("cba", stringUtils.reverse("abc"))
        assertEquals("", stringUtils.reverse(""))
        assertEquals("a", stringUtils.reverse("a"))
        assertEquals("!dlroW olleH", stringUtils.reverse("Hello World!"))
        assertEquals("12345", stringUtils.reverse("54321"))
    }

    @Test
    fun testIsPalindrome() {
        assertTrue(stringUtils.isPalindrome("racecar"))
        assertTrue(stringUtils.isPalindrome(""))
        assertTrue(stringUtils.isPalindrome("a"))
        assertTrue(stringUtils.isPalindrome("A man a plan a canal Panama"))
        assertTrue(stringUtils.isPalindrome("Madam, I'm Adam"))
        assertFalse(stringUtils.isPalindrome("hello"))
        assertFalse(stringUtils.isPalindrome("world"))
    }

    @Test
    fun testCountVowels() {
        assertEquals(0, stringUtils.countVowels(""))
        assertEquals(0, stringUtils.countVowels("bcdfgh"))
        assertEquals(2, stringUtils.countVowels("hello"))
        assertEquals(1, stringUtils.countVowels("world"))
        assertEquals(5, stringUtils.countVowels("aeiou"))
        assertEquals(5, stringUtils.countVowels("AEIOU"))
        assertEquals(3, stringUtils.countVowels("Hello World"))
    }

    @Test
    fun testCapitalizeWords() {
        assertEquals("", stringUtils.capitalizeWords(""))
        assertEquals("Hello", stringUtils.capitalizeWords("hello"))
        assertEquals("Hello World", stringUtils.capitalizeWords("hello world"))
        assertEquals("Hello World", stringUtils.capitalizeWords("HELLO WORLD"))
        assertEquals("Hello World", stringUtils.capitalizeWords("hElLo WoRlD"))
        assertEquals("The Quick Brown Fox", stringUtils.capitalizeWords("the quick brown fox"))
    }

    @Test
    fun testIsValidEmail() {
        assertTrue(stringUtils.isValidEmail("test@example.com"))
        assertTrue(stringUtils.isValidEmail("user.name@domain.co.uk"))
        assertTrue(stringUtils.isValidEmail("user+tag@example.org"))
        assertFalse(stringUtils.isValidEmail(""))
        assertFalse(stringUtils.isValidEmail("invalid-email"))
        assertFalse(stringUtils.isValidEmail("@example.com"))
        assertFalse(stringUtils.isValidEmail("user@"))
        assertFalse(stringUtils.isValidEmail("user@.com"))
    }

    @Test
    fun testIsValidPassword() {
        assertTrue(stringUtils.isValidPassword("password123"))
        assertTrue(stringUtils.isValidPassword("123456"))
        assertTrue(stringUtils.isValidPassword("abcdef"))
        assertFalse(stringUtils.isValidPassword(""))
        assertFalse(stringUtils.isValidPassword("12345"))
        assertFalse(stringUtils.isValidPassword("abc"))
    }
}
