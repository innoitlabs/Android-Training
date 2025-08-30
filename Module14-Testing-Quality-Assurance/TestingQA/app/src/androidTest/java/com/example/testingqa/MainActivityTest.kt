package com.example.testingqa

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAppTitleIsDisplayed() {
        onView(withText("Android Testing & QA Demo"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testAddUserSectionIsDisplayed() {
        onView(withText("Add New User"))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.nameInput))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.emailInput))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.addButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSearchSectionIsDisplayed() {
        onView(withText("Search Users"))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.searchInput))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.searchButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCalculatorSectionIsDisplayed() {
        onView(withText("Calculator Demo"))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.calculatorButton))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.calculatorResult))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCalculatorButtonClick() {
        // Click the calculator button
        onView(withId(R.id.calculatorButton))
            .perform(click())
        
        // Verify result is displayed
        onView(withId(R.id.calculatorResult))
            .check(matches(withText("5 + 3 = 8")))
    }

    @Test
    fun testInputFieldsAreEditable() {
        // Test name input
        onView(withId(R.id.nameInput))
            .perform(typeText("John Doe"), closeSoftKeyboard())
        
        onView(withId(R.id.nameInput))
            .check(matches(withText("John Doe")))
        
        // Test email input
        onView(withId(R.id.emailInput))
            .perform(typeText("john@example.com"), closeSoftKeyboard())
        
        onView(withId(R.id.emailInput))
            .check(matches(withText("john@example.com")))
    }

    @Test
    fun testSearchInputIsEditable() {
        onView(withId(R.id.searchInput))
            .perform(typeText("Alice"), closeSoftKeyboard())
        
        onView(withId(R.id.searchInput))
            .check(matches(withText("Alice")))
    }

    @Test
    fun testButtonsAreClickable() {
        onView(withId(R.id.addButton))
            .check(matches(isEnabled()))
        
        onView(withId(R.id.searchButton))
            .check(matches(isEnabled()))
        
        onView(withId(R.id.calculatorButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun testUserListSectionIsDisplayed() {
        onView(withText("User List"))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.resultText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testInitialUserListState() {
        onView(withId(R.id.resultText))
            .check(matches(withText("No users found")))
    }

    @Test
    fun testAddUserWithValidData() {
        // Fill in user data
        onView(withId(R.id.nameInput))
            .perform(typeText("Test User"), closeSoftKeyboard())
        
        onView(withId(R.id.emailInput))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        
        // Click add button
        onView(withId(R.id.addButton))
            .perform(click())
        
        // Note: In a real app, you'd verify the user was added to the list
        // This test just verifies the button click works
    }

    @Test
    fun testSearchFunctionality() {
        // Enter search term
        onView(withId(R.id.searchInput))
            .perform(typeText("test"), closeSoftKeyboard())
        
        // Click search button
        onView(withId(R.id.searchButton))
            .perform(click())
        
        // Note: In a real app, you'd verify search results
        // This test just verifies the button click works
    }

    @Test
    fun testInputHintsAreDisplayed() {
        onView(withId(R.id.nameInput))
            .check(matches(withHint("Enter name")))
        
        onView(withId(R.id.emailInput))
            .check(matches(withHint("Enter email")))
        
        onView(withId(R.id.searchInput))
            .check(matches(withHint("Enter search term")))
    }

    @Test
    fun testCalculatorButtonText() {
        onView(withId(R.id.calculatorButton))
            .check(matches(withText("Calculate 5 + 3")))
    }

    @Test
    fun testInitialCalculatorResult() {
        onView(withId(R.id.calculatorResult))
            .check(matches(withText("Result will appear here")))
    }
}
