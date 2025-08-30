package com.example.dependencyhilt.domain.repository

import com.example.dependencyhilt.data.model.User

/**
 * Repository interface for user-related operations.
 * 
 * This interface defines the contract for user data operations,
 * following the repository pattern. The actual implementation
 * can handle data from different sources (API, database, etc.).
 */
interface UserRepository {
    
    /**
     * Get all users.
     * 
     * @return List of User objects
     * @throws Exception if the operation fails
     */
    suspend fun getUsers(): List<User>
    
    /**
     * Get a specific user by ID.
     * 
     * @param id The user ID
     * @return User object
     * @throws Exception if the operation fails
     */
    suspend fun getUserById(id: Int): User
}
