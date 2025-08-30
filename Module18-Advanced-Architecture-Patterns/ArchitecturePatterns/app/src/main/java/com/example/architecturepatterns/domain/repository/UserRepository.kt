package com.example.architecturepatterns.domain.repository

import com.example.architecturepatterns.domain.model.Result
import com.example.architecturepatterns.domain.model.User

/**
 * Repository interface for user data operations.
 * 
 * This interface defines the contract for user data access operations.
 * Implementations should handle data persistence, caching, and synchronization
 * between local and remote data sources.
 * 
 * @see UserRepositoryImpl
 * @see User
 */
interface UserRepository {
    
    /**
     * Retrieves all users from the data source.
     * 
     * @return A [Result] containing a list of [User] objects
     */
    suspend fun getUsers(): Result<List<User>>
    
    /**
     * Retrieves a specific user by ID.
     * 
     * @param id The unique identifier of the user
     * @return A [Result] containing the [User] or an error
     */
    suspend fun getUser(id: Int): Result<User>
    
    /**
     * Creates a new user.
     * 
     * @param user The user data to create
     * @return A [Result] containing the created [User] with assigned ID
     */
    suspend fun createUser(user: User): Result<User>
    
    /**
     * Updates an existing user.
     * 
     * @param user The updated user data
     * @return A [Result] containing the updated [User]
     */
    suspend fun updateUser(user: User): Result<User>
    
    /**
     * Deletes a user by ID.
     * 
     * @param id The unique identifier of the user to delete
     * @return A [Result] indicating success or failure
     */
    suspend fun deleteUser(id: Int): Result<Unit>
    
    /**
     * Searches for users by name or email.
     * 
     * @param query The search query
     * @return A [Result] containing matching [User] objects
     */
    suspend fun searchUsers(query: String): Result<List<User>>
}
