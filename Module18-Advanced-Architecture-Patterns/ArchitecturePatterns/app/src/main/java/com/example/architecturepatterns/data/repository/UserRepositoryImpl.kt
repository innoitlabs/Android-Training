package com.example.architecturepatterns.data.repository

import com.example.architecturepatterns.data.api.ApiService
import com.example.architecturepatterns.data.db.dao.UserDao
import com.example.architecturepatterns.data.mapper.UserMapper
import com.example.architecturepatterns.domain.model.Result
import com.example.architecturepatterns.domain.model.User
import com.example.architecturepatterns.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of UserRepository that provides data access with caching.
 * 
 * This class implements the repository pattern with a cache-first strategy:
 * 1. First checks the local database for cached data
 * 2. If not found or cache is expired, fetches from remote API
 * 3. Updates local cache with fresh data
 * 4. Returns the data to the caller
 * 
 * @param apiService The API service for remote data operations
 * @param userDao The DAO for local database operations
 * @param userMapper The mapper for converting between domain and data models
 */
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val userMapper: UserMapper
) : UserRepository {
    
    override suspend fun getUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                // Try to get from cache first
                val cachedUsers = userDao.getAllUsers()
                if (cachedUsers.isNotEmpty()) {
                    return@withContext Result.Success(userMapper.mapToDomainList(cachedUsers))
                }
                
                // Fetch from network
                val networkUsers = apiService.getUsers()
                val userEntities = userMapper.mapToEntityList(networkUsers)
                
                // Cache the results
                userDao.insertAllUsers(userEntities)
                
                Result.Success(networkUsers)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    override suspend fun getUser(id: Int): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                // Try cache first
                userDao.getUserById(id)?.let { cachedUser ->
                    return@withContext Result.Success(userMapper.mapToDomain(cachedUser))
                }
                
                // Fetch from network
                val networkUser = apiService.getUser(id)
                val userEntity = userMapper.mapToEntity(networkUser)
                
                // Cache the result
                userDao.insertUser(userEntity)
                
                Result.Success(networkUser)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    override suspend fun createUser(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val createdUser = apiService.createUser(user)
                val userEntity = userMapper.mapToEntity(createdUser)
                userDao.insertUser(userEntity)
                Result.Success(createdUser)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    override suspend fun updateUser(user: User): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val updatedUser = apiService.updateUser(user.id, user)
                val userEntity = userMapper.mapToEntity(updatedUser)
                userDao.updateUser(userEntity)
                Result.Success(updatedUser)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    override suspend fun deleteUser(id: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.deleteUser(id)
                userDao.deleteUserById(id)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
    
    override suspend fun searchUsers(query: String): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                // Try local search first
                val localResults = userDao.searchUsers(query)
                if (localResults.isNotEmpty()) {
                    return@withContext Result.Success(userMapper.mapToDomainList(localResults))
                }
                
                // Search on network
                val networkResults = apiService.searchUsers(query)
                val userEntities = userMapper.mapToEntityList(networkResults)
                
                // Cache the results
                userDao.insertAllUsers(userEntities)
                
                Result.Success(networkResults)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
