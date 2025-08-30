package com.example.architecturepatterns.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a user in the local database.
 * 
 * This entity is used for storing user data locally using Room database.
 * It maps to the domain User model through a mapper.
 * 
 * @property id The unique identifier for the user
 * @property name The user's display name
 * @property email The user's email address
 * @property avatar The URL to the user's avatar image
 * @property createdAt The timestamp when the user was created
 * @property isActive Whether the user account is active
 * @property isVerified Whether the user's email is verified
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val avatar: String,
    val createdAt: Long,
    val isActive: Boolean = true,
    val isVerified: Boolean = false
)
