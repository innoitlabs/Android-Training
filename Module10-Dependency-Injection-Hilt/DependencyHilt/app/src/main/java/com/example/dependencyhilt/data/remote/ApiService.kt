package com.example.dependencyhilt.data.remote

import com.example.dependencyhilt.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit API service interface for JSONPlaceholder API.
 * 
 * This interface defines the API endpoints and their corresponding HTTP methods.
 * Retrofit uses this interface to generate the actual HTTP client implementation.
 */
interface ApiService {
    
    /**
     * Get all users from the API.
     * 
     * @return List of User objects
     */
    @GET("users")
    suspend fun getUsers(): List<User>
    
    /**
     * Get a specific user by ID.
     * 
     * @param id The user ID
     * @return User object
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User
}
