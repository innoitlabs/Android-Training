package com.example.architecturepatterns.domain.model

import java.util.Date

/**
 * Domain model representing a user in the application.
 * 
 * This class contains business logic and validation rules for user data.
 * It is independent of any framework or external dependencies.
 * 
 * @property id The unique identifier for the user
 * @property name The user's display name
 * @property email The user's email address
 * @property avatar The URL to the user's avatar image
 * @property createdAt The date when the user was created
 * @property isActive Whether the user account is active
 * @property isVerified Whether the user's email is verified
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Date,
    val isActive: Boolean = true,
    val isVerified: Boolean = false
) {
    /**
     * The display name for the user, including verification status.
     */
    val displayName: String
        get() = if (isVerified) "$name ✓" else name
    
    /**
     * The profile URL for the user.
     */
    val profileUrl: String
        get() = "/users/$id"
    
    /**
     * Checks if the user is a new user (created within the last 7 days).
     */
    fun isNewUser(): Boolean {
        val daysSinceCreation = (Date().time - createdAt.time) / (1000 * 60 * 60 * 24)
        return daysSinceCreation <= 7
    }
    
    /**
     * Checks if the user can be followed by another user.
     */
    fun canBeFollowedBy(otherUser: User): Boolean {
        return id != otherUser.id && isActive
    }
    
    /**
     * Validates the user data.
     * 
     * @throws IllegalArgumentException if the user data is invalid
     */
    fun validate(): Boolean {
        require(name.isNotBlank()) { "Name cannot be empty" }
        require(email.isNotBlank()) { "Email cannot be empty" }
        require(email.contains("@")) { "Invalid email format" }
        require(id > 0) { "User ID must be positive" }
        return true
    }
}
