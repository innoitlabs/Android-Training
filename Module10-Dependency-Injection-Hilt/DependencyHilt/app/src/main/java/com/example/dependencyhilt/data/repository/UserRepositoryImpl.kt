package com.example.dependencyhilt.data.repository

import com.example.dependencyhilt.data.remote.ApiService
import com.example.dependencyhilt.data.model.User
import com.example.dependencyhilt.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Implementation of UserRepository that fetches data from the API.
 * 
 * This class uses constructor injection to receive its dependencies
 * (ApiService) from Hilt. It implements the UserRepository interface
 * and delegates the actual data fetching to the API service.
 */
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {
    
    /**
     * Get all users from the API.
     * 
     * @return List of User objects
     * @throws Exception if the API call fails
     */
    override suspend fun getUsers(): List<User> {
        return try {
            apiService.getUsers()
        } catch (e: Exception) {
            throw Exception("Failed to fetch users: ${e.message}")
        }
    }
    
    /**
     * Get a specific user by ID from the API.
     * 
     * @param id The user ID
     * @return User object
     * @throws Exception if the API call fails
     */
    override suspend fun getUserById(id: Int): User {
        return try {
            apiService.getUserById(id)
        } catch (e: Exception) {
            throw Exception("Failed to fetch user with ID $id: ${e.message}")
        }
    }
}
