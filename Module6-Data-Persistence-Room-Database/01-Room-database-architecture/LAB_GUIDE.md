# Room Database Hands-on Lab Guide

## Lab Overview

This hands-on lab will guide you through building a complete Room Database application from scratch. You'll learn how to implement the MVVM architecture pattern with Room, LiveData, and ViewModels.

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- Basic knowledge of Kotlin and Android development
- An Android emulator or physical device

## Lab Objectives

By the end of this lab, you will be able to:
1. Set up Room Database dependencies
2. Create Entity, DAO, and Database classes
3. Implement Repository and ViewModel patterns
4. Build a functional UI that interacts with the database
5. Test CRUD operations

## Lab Structure

### Part 1: Project Setup (15 minutes)
### Part 2: Database Layer Implementation (30 minutes)
### Part 3: Repository and ViewModel (20 minutes)
### Part 4: UI Implementation (25 minutes)
### Part 5: Testing and Validation (10 minutes)

---

## Part 1: Project Setup

### Step 1.1: Verify Dependencies

Open `app/build.gradle.kts` and ensure you have the following dependencies:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")  // This is crucial for Room
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

**Action**: Sync your project after making changes.

### Step 1.2: Verify Project Structure

Ensure your project has the following structure:
```
app/src/main/java/com/example/roomdatabase/
├── MainActivity.kt
├── User.kt (to be created)
├── UserDao.kt (to be created)
├── AppDatabase.kt (to be created)
├── UserRepository.kt (to be created)
└── UserViewModel.kt (to be created)
```

---

## Part 2: Database Layer Implementation

### Step 2.1: Create User Entity

Create a new Kotlin file `User.kt` in the main package:

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

**Key Points**:
- `@Entity` annotation creates a database table
- `@PrimaryKey(autoGenerate = true)` creates an auto-incrementing primary key
- The class must be a `data class`

### Step 2.2: Create UserDao

Create a new Kotlin file `UserDao.kt`:

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

**Key Points**:
- `@Dao` marks this as a Data Access Object
- `suspend` functions for database operations
- `Flow` for reactive data streams
- `@Query` for custom SQL queries

### Step 2.3: Create AppDatabase

Create a new Kotlin file `AppDatabase.kt`:

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

**Key Points**:
- `@Database` annotation with entities list and version
- Singleton pattern for database instance
- `exportSchema = false` for development (set to true for production)

---

## Part 3: Repository and ViewModel

### Step 3.1: Create UserRepository

Create a new Kotlin file `UserRepository.kt`:

```kotlin
package com.example.roomdatabase

class UserRepository(private val userDao: UserDao) {
    val allUsers = userDao.getAllUsers()

    suspend fun insert(user: User) = userDao.insertUser(user)
    suspend fun update(user: User) = userDao.updateUser(user)
    suspend fun delete(user: User) = userDao.deleteUser(user)
}
```

**Key Points**:
- Repository abstracts data access
- Single source of truth for data
- Easier to test and maintain

### Step 3.2: Create UserViewModel

Create a new Kotlin file `UserViewModel.kt`:

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

**Key Points**:
- `AndroidViewModel` for application context access
- `viewModelScope` for coroutine management
- `asLiveData()` converts Flow to LiveData

---

## Part 4: UI Implementation

### Step 4.1: Update Layout

Update `activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:padding="16dp"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Room Database Demo"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="24dp"/>

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
        android:id="@+id/userCountText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="User Count: 0"
        android:textStyle="bold"
        android:layout_marginBottom="8dp"/>

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <TextView
            android:id="@+id/outputText"
            android:text="Users will appear here"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

    </ScrollView>

</LinearLayout>
```

### Step 4.2: Update MainActivity

Update `MainActivity.kt`:

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
        val userCountText = findViewById<TextView>(R.id.userCountText)

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull() ?: 0
            
            if (name.isNotEmpty() && age > 0) {
                val user = User(name = name, age = age)
                userViewModel.insert(user)
                nameInput.text.clear()
                ageInput.text.clear()
            }
        }

        userViewModel.allUsers.observe(this) { users ->
            userCountText.text = "User Count: ${users.size}"
            if (users.isEmpty()) {
                outputText.text = "No users added yet"
            } else {
                outputText.text = users.joinToString("\n\n") { 
                    "ID: ${it.id}\nName: ${it.name}\nAge: ${it.age}" 
                }
            }
        }
    }
}
```

---

## Part 5: Testing and Validation

### Step 5.1: Build and Run

1. **Build the project** (Build → Make Project)
2. **Run on emulator/device**
3. **Test the following scenarios**:

### Step 5.2: Test Cases

**Test Case 1: Add User**
- Enter a name: "John Doe"
- Enter an age: "25"
- Click "Add User"
- **Expected**: User appears in the list with ID 1

**Test Case 2: Add Multiple Users**
- Add several users with different names and ages
- **Expected**: All users appear in the list, count updates

**Test Case 3: Validation**
- Try to add a user with empty name
- Try to add a user with age 0 or negative
- **Expected**: No user added, fields remain unchanged

**Test Case 4: Persistence**
- Add some users
- Close the app completely
- Reopen the app
- **Expected**: Users are still there

### Step 5.3: Debugging Tips

If you encounter issues:

1. **Check Logcat** for error messages
2. **Verify imports** are correct
3. **Clean and rebuild** the project
4. **Check Room annotations** are properly applied

---

## Lab Completion Checklist

- [ ] Project builds successfully
- [ ] App runs without crashes
- [ ] Can add users with name and age
- [ ] User count updates correctly
- [ ] Users persist after app restart
- [ ] Validation works (empty name, invalid age)
- [ ] UI is responsive and user-friendly

## Next Steps

After completing this lab:

1. **Try the exercises** in EXERCISES.md
2. **Add more features** like search, filtering, or sorting
3. **Implement relationships** between entities
4. **Add unit tests** for your DAOs and ViewModels
5. **Explore advanced Room features** like migrations and complex queries

## Troubleshooting

### Common Issues:

**Issue**: "Cannot resolve symbol @Entity"
**Solution**: Make sure kapt is enabled and Room dependencies are added

**Issue**: App crashes on startup
**Solution**: Check that all Room classes are properly annotated

**Issue**: Users don't persist
**Solution**: Verify database operations are in coroutines

**Issue**: UI doesn't update
**Solution**: Ensure LiveData observation is set up correctly

For additional help, refer to the main README.md and SETUP_GUIDE.md files.

