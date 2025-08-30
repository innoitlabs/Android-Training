package com.example.architecturepatterns

import com.example.architecturepatterns.domain.model.User
import org.junit.Test
import java.util.Date

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    
    @Test
    fun testUserDomainModel() {
        // Given
        val user = User(
            id = 1,
            name = "John Doe",
            email = "john@example.com",
            avatar = "https://example.com/avatar.jpg",
            createdAt = Date(),
            isActive = true,
            isVerified = true
        )
        
        // When & Then
        assertEquals(1, user.id)
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
        assertEquals("John Doe ✓", user.displayName)
        assertEquals("/users/1", user.profileUrl)
        assertTrue(user.isActive)
        assertTrue(user.isVerified)
        assertTrue(user.validate())
    }
    
    @Test
    fun testUserValidation() {
        // Given
        val validUser = User(
            id = 1,
            name = "Valid User",
            email = "valid@example.com",
            avatar = "",
            createdAt = Date()
        )
        
        // When & Then
        assertTrue(validUser.validate())
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun testUserValidationWithInvalidId() {
        // Given
        val invalidUser = User(
            id = 0, // Invalid ID
            name = "Test User",
            email = "test@example.com",
            avatar = "",
            createdAt = Date()
        )
        
        // When
        invalidUser.validate()
        
        // Then - should throw IllegalArgumentException
    }
}