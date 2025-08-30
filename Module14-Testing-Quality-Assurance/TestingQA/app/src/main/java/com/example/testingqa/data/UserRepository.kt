package com.example.testingqa.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userDao: UserDao,
    private val apiService: ApiService
) {
    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Fetching users from database")
            userDao.getAllUsers()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching users from database", e)
            emptyList()
        }
    }
    
    suspend fun getUserById(id: Long): User? = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Fetching user with id: $id")
            userDao.getUserById(id)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching user with id: $id", e)
            null
        }
    }
    
    suspend fun searchUsers(query: String): List<User> = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Searching users with query: $query")
            userDao.searchUsersByName(query)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error searching users with query: $query", e)
            emptyList()
        }
    }
    
    suspend fun addUser(user: User): User = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Adding user: ${user.name}")
            val userId = userDao.insert(user)
            user.copy(id = userId)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error adding user: ${user.name}", e)
            throw e
        }
    }
    
    suspend fun updateUser(user: User): User = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Updating user: ${user.name}")
            userDao.update(user)
            user
        } catch (e: Exception) {
            Log.e("UserRepository", "Error updating user: ${user.name}", e)
            throw e
        }
    }
    
    suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Deleting user: ${user.name}")
            userDao.delete(user)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error deleting user: ${user.name}", e)
            throw e
        }
    }
    
    suspend fun syncUsers() = withContext(Dispatchers.IO) {
        try {
            Log.d("UserRepository", "Syncing users from API")
            val apiUsers = apiService.getUsers()
            userDao.insertAll(apiUsers)
            Log.d("UserRepository", "Successfully synced ${apiUsers.size} users")
        } catch (e: Exception) {
            Log.e("UserRepository", "Error syncing users from API", e)
            throw e
        }
    }
}
