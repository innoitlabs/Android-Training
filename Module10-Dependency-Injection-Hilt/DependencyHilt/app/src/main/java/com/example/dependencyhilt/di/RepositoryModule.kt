package com.example.dependencyhilt.di

import com.example.dependencyhilt.data.repository.UserRepositoryImpl
import com.example.dependencyhilt.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that provides repository bindings.
 * 
 * This module uses @Binds to bind interface implementations to their interfaces.
 * This is more efficient than @Provides when you have an interface and its implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    /**
     * Binds UserRepositoryImpl to UserRepository interface.
     * 
     * This tells Hilt that whenever UserRepository is requested,
     * it should provide an instance of UserRepositoryImpl.
     * 
     * @param userRepositoryImpl The implementation to bind
     * @return UserRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}
