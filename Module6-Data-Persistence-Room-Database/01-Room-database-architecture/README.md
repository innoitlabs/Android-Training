# Android Room Database Architecture - Complete Guide

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand the Room persistence library and why it's used
- Define Entity classes to represent database tables
- Create DAO (Data Access Objects) for queries
- Set up a RoomDatabase to provide access to data
- Perform CRUD operations (Create, Read, Update, Delete)
- Use Coroutines/Flow to handle data asynchronously

## Table of Contents

1. [Introduction](#introduction)
2. [Dependencies](#dependencies)
3. [Define Entity](#define-entity)
4. [Define DAO](#define-dao)
5. [Create Room Database](#create-room-database)
6. [Repository Layer](#repository-layer)
7. [ViewModel Layer](#viewmodel-layer)
8. [UI Implementation](#ui-implementation)
9. [Best Practices](#best-practices)
10. [Hands-on Lab](#hands-on-lab)
11. [Exercises](#exercises)
12. [Summary](#summary)

## Introduction

Room is a persistence library from Android Jetpack that provides an abstraction layer over SQLite. It offers compile-time query verification and seamless integration with other Jetpack components.

### Key Components:
- **Entity**: Represents a database table
- **DAO**: Data Access Object, defines queries
- **Database**: Holds the database and serves as the main access point

### Why Room?
- Compile-time verification of SQL queries
- Convenient annotations for common database operations
- Integration with LiveData and Kotlin Coroutines
- Reduced boilerplate code compared to raw SQLite

## Dependencies

The following dependencies are required in `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")  // Enable Kotlin Annotation Processing
}

dependencies {
    // Room dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    
    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    
    // Other dependencies...
}
```

## Define Entity

An Entity represents a table in your database. Here's the User entity:

```kotlin
package com.example.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int
)
```

**Key Points:**
- `@Entity(tableName = "users")` creates a table named "users"
- `@PrimaryKey(autoGenerate = true)` creates an auto-incrementing primary key
- The class must be a data class for Room to work properly

## Define DAO

DAO (Data Access Object) defines the database operations:

```kotlin
package com.example.roomdatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User>
}
```

**Key Points:**
- `@Dao` annotation marks this as a Data Access Object
- `suspend` functions for database operations (must be called from coroutines)
- `Flow` for reactive data streams
- `@Query` for custom SQL queries

## Create Room Database

The database class ties everything together:

```kotlin
package com.example.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

**Key Points:**
- `@Database` annotation with entities list and version number
- Singleton pattern for database instance
- `exportSchema = false` disables schema export (set to true for production)

## Repository Layer

Repository provides a clean API for data operations:

```kotlin
package com.example.roomdatabase

class UserRepository(private val userDao: UserDao) {
    val allUsers = userDao.getAllUsers()

    suspend fun insert(user: User) = userDao.insertUser(user)
    suspend fun update(user: User) = userDao.updateUser(user)
    suspend fun delete(user: User) = userDao.deleteUser(user)
}
```

**Benefits:**
- Single source of truth for data
- Abstracts data source details
- Easier testing and maintenance

## ViewModel Layer

ViewModel manages UI-related data and survives configuration changes:

```kotlin
package com.example.roomdatabase

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()
    private val repository = UserRepository(userDao)

    val allUsers = repository.allUsers.asLiveData()

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        repository.delete(user)
    }
}
```

**Key Points:**
- `AndroidViewModel` for application context access
- `viewModelScope` for coroutine management
- `asLiveData()` converts Flow to LiveData for UI observation

## UI Implementation

### Layout (activity_main.xml)

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:padding="16dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <EditText
        android:id="@+id/nameInput"
        android:hint="Enter Name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="8dp"/>

    <EditText
        android:id="@+id/ageInput"
        android:hint="Enter Age"
        android:inputType="number"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp"/>

    <Button
        android:id="@+id/addButton"
        android:text="Add User"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/outputText"
        android:text="Users will appear here"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>
```

### MainActivity.kt

```kotlin
package com.example.roomdatabase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val outputText = findViewById<TextView>(R.id.outputText)

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull() ?: 0
            if (name.isNotEmpty()) {
                val user = User(name = name, age = age)
                userViewModel.insert(user)
                nameInput.text.clear()
                ageInput.text.clear()
            }
        }

        userViewModel.allUsers.observe(this) { users ->
            outputText.text = users.joinToString("\n") { 
                "ID: ${it.id} | Name: ${it.name} | Age: ${it.age}" 
            }
        }
    }
}
```

## Best Practices

1. **Always use suspend functions** for database operations
2. **Use Flow/LiveData** for observing query results
3. **Define a Repository** for separation of concerns
4. **Avoid database operations on the main thread**
5. **Use meaningful table and column names**
6. **Handle database migrations properly**
7. **Test your DAOs with unit tests**

## Hands-on Lab

### Mini Project: User Management App

Create a simple app that can:
- Add users with name and age
- Display list of all users
- Update a user's age
- Delete a user

**Steps:**
1. Set up the project with Room dependencies
2. Create the User entity
3. Implement UserDao with CRUD operations
4. Set up AppDatabase
5. Create UserRepository
6. Implement UserViewModel
7. Build the UI in MainActivity
8. Test all CRUD operations

## Exercises

### Easy Level
- Add an email field to the User entity
- Add validation for age (must be positive)
- Display user count in the UI

### Intermediate Level
- Add a new DAO query to find users by age range
- Implement search functionality by name
- Add sorting options (by name, age, ID)

### Advanced Level
- Add another entity (e.g., Task) with a relationship to User
- Implement One-to-Many relationship between User and Task
- Add cascade delete functionality
- Implement database migration for schema changes

## Summary

Room Database provides a powerful and convenient way to work with SQLite in Android:

- **Entity** → defines table structure
- **DAO** → defines database operations
- **Database** → provides database instance
- **Repository** → abstracts data access
- **ViewModel** → manages UI state
- **Use Room with MVVM** for clean architecture

The combination of Room, Kotlin Coroutines, and Flow provides a modern, reactive approach to data persistence in Android applications.

## Next Steps

After completing this tutorial, explore:
- Database migrations
- Complex relationships (One-to-Many, Many-to-Many)
- Room with Paging 3
- Testing Room databases
- Room with Hilt dependency injection

