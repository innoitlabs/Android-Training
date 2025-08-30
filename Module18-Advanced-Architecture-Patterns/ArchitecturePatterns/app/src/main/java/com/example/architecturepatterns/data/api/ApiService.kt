package com.example.architecturepatterns.data.api

import com.example.architecturepatterns.domain.model.User
import retrofit2.http.*

/**
 * API service interface for user-related network operations.
 * 
 * This interface defines the contract for communicating with the backend API.
 * It uses Retrofit annotations to define HTTP endpoints.
 */
interface ApiService {
    
    /**
     * Retrieves all users from the API.
     * 
     * @return A list of users
     */
    @GET("users")
    suspend fun getUsers(): List<User>
    
    /**
     * Retrieves a specific user by ID.
     * 
     * @param id The unique identifier of the user
     * @return The user data
     */
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
    
    /**
     * Creates a new user.
     * 
     * @param user The user data to create
     * @return The created user with assigned ID
     */
    @POST("users")
    suspend fun createUser(@Body user: User): User
    
    /**
     * Updates an existing user.
     * 
     * @param id The unique identifier of the user
     * @param user The updated user data
     * @return The updated user
     */
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): User
    
    /**
     * Deletes a user by ID.
     * 
     * @param id The unique identifier of the user to delete
     * @return An empty response indicating success
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): retrofit2.Response<Unit>
    
    /**
     * Searches for users by name or email.
     * 
     * @param query The search query
     * @return A list of matching users
     */
    @GET("users/search")
    suspend fun searchUsers(@Query("q") query: String): List<User>
}
