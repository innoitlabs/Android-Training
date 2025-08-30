package com.example.coroutinesasync.data

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
    
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }
    
    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}
