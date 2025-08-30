package com.example.mvvmarchitecture.data.network

import com.example.mvvmarchitecture.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
    
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User
}
