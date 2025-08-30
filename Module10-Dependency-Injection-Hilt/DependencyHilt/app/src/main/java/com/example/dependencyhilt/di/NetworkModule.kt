package com.example.dependencyhilt.di

import com.example.dependencyhilt.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module that provides network-related dependencies.
 * 
 * This module is installed in SingletonComponent, meaning the provided
 * dependencies will be singletons throughout the application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * Provides a Retrofit instance configured for the JSONPlaceholder API.
     * 
     * @return Configured Retrofit instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Provides an ApiService instance using the provided Retrofit instance.
     * 
     * @param retrofit The Retrofit instance to create the API service from
     * @return ApiService implementation
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
