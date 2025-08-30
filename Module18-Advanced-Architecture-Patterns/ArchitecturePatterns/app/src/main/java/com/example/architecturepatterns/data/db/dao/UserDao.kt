package com.example.architecturepatterns.data.db.dao

import androidx.room.*
import com.example.architecturepatterns.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for user database operations.
 * 
 * This interface defines the database operations for user entities.
 * It uses Room annotations to define SQL queries and operations.
 */
@Dao
interface UserDao {
    
    /**
     * Retrieves all users from the database.
     * 
     * @return A list of all user entities
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
    
    /**
     * Retrieves a user by ID.
     * 
     * @param id The unique identifier of the user
     * @return The user entity, or null if not found
     */
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?
    
    /**
     * Searches for users by name or email.
     * 
     * @param query The search query
     * @return A list of matching user entities
     */
    @Query("SELECT * FROM users WHERE name LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%'")
    suspend fun searchUsers(query: String): List<UserEntity>
    
    /**
     * Inserts a single user into the database.
     * 
     * @param user The user entity to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    /**
     * Inserts multiple users into the database.
     * 
     * @param users The list of user entities to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<UserEntity>)
    
    /**
     * Updates a user in the database.
     * 
     * @param user The user entity to update
     */
    @Update
    suspend fun updateUser(user: UserEntity)
    
    /**
     * Deletes a user from the database.
     * 
     * @param user The user entity to delete
     */
    @Delete
    suspend fun deleteUser(user: UserEntity)
    
    /**
     * Deletes a user by ID.
     * 
     * @param id The unique identifier of the user to delete
     */
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)
    
    /**
     * Deletes all users from the database.
     */
    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
    
    /**
     * Observes changes to all users in the database.
     * 
     * @return A Flow that emits the list of users whenever it changes
     */
    @Query("SELECT * FROM users")
    fun observeAllUsers(): Flow<List<UserEntity>>
    
    /**
     * Observes changes to a specific user in the database.
     * 
     * @param id The unique identifier of the user to observe
     * @return A Flow that emits the user entity whenever it changes
     */
    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUserById(id: Int): Flow<UserEntity?>
}
