package com.example.architecturepatterns.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.architecturepatterns.data.db.dao.UserDao
import com.example.architecturepatterns.data.db.entity.UserEntity

/**
 * Room database for the application.
 * 
 * This class defines the database configuration and provides access to DAOs.
 * It uses Room annotations to define the database schema.
 * 
 * @property userDao The DAO for user operations
 */
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Returns the DAO for user operations.
     * 
     * @return The UserDao instance
     */
    abstract fun userDao(): UserDao
}
