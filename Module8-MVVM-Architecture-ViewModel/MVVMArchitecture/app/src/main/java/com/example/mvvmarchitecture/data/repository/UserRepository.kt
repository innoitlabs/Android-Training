package com.example.mvvmarchitecture.data.repository

import com.example.mvvmarchitecture.data.local.UserDao
import com.example.mvvmarchitecture.data.model.User
import com.example.mvvmarchitecture.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    
    suspend fun getUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                // Try to get from API first
                val users = apiService.getUsers()
                // Cache the data
                userDao.insertUsers(users)
                Result.success(users)
            } catch (e: HttpException) {
                // Server error, try to get cached data
                val cachedUsers = userDao.getAllUsers()
                if (cachedUsers.isNotEmpty()) {
                    Result.success(cachedUsers)
                } else {
                    Result.failure(e)
                }
            } catch (e: IOException) {
                // Network error, try to get cached data
                val cachedUsers = userDao.getAllUsers()
                if (cachedUsers.isNotEmpty()) {
                    Result.success(cachedUsers)
                } else {
                    Result.failure(e)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getUser(id: Int): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Try to get from cache first
                val cachedUser = userDao.getUser(id)
                if (cachedUser != null) {
                    return@withContext Result.success(cachedUser)
                }
                
                // Fetch from API
                val user = apiService.getUser(id)
                userDao.insertUser(user)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun searchUsers(query: String): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val users = userDao.searchUsers(query)
                Result.success(users)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun refreshUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val users = apiService.getUsers()
                userDao.deleteAllUsers()
                userDao.insertUsers(users)
                Result.success(users)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
