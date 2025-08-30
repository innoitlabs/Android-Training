package com.example.architecturepatterns.di

import android.content.Context
import androidx.room.Room
import com.example.architecturepatterns.data.api.ApiService
import com.example.architecturepatterns.data.db.AppDatabase
import com.example.architecturepatterns.data.db.dao.UserDao
import com.example.architecturepatterns.data.mapper.UserMapper
import com.example.architecturepatterns.data.repository.UserRepositoryImpl
import com.example.architecturepatterns.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Hilt module for providing application-wide dependencies.
 * 
 * This module provides singleton instances of network, database, and repository
 * components that are used throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * Provides the base URL for the API.
     */
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return "https://jsonplaceholder.typicode.com/"
    }
    
    /**
     * Provides the HTTP logging interceptor for debugging network requests.
     */
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    /**
     * Provides the OkHttpClient with logging interceptor.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
    
    /**
     * Provides the Retrofit instance for network communication.
     */
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Provides the API service interface.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
    
    /**
     * Provides the Room database instance.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "architecture_patterns_db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    /**
     * Provides the UserDao for database operations.
     */
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
    
    /**
     * Provides the UserMapper for converting between domain and data models.
     */
    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }
    
    /**
     * Provides the UserRepository implementation.
     */
    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao,
        userMapper: UserMapper
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao, userMapper)
    }
}
