package com.example.testingqa

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testingqa.data.AppDatabase
import com.example.testingqa.data.User
import com.example.testingqa.data.UserDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun insertAndRetrieveUser() = runBlocking {
        // Given
        val user = User(id = 1, name = "Alice", email = "alice@example.com")

        // When
        val userId = userDao.insert(user)
        val retrievedUser = userDao.getUserById(userId)

        // Then
        assertNotNull(retrievedUser)
        assertEquals(user.name, retrievedUser?.name)
        assertEquals(user.email, retrievedUser?.email)
    }

    @Test
    fun insertMultipleUsers() = runBlocking {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "Bob", email = "bob@example.com"),
            User(id = 3, name = "Charlie", email = "charlie@example.com")
        )

        // When
        userDao.insertAll(users)
        val allUsers = userDao.getAllUsers()

        // Then
        assertEquals(3, allUsers.size)
        assertTrue(allUsers.any { it.name == "Alice" })
        assertTrue(allUsers.any { it.name == "Bob" })
        assertTrue(allUsers.any { it.name == "Charlie" })
    }

    @Test
    fun updateUser() = runBlocking {
        // Given
        val user = User(id = 1, name = "Alice", email = "alice@example.com")
        userDao.insert(user)

        // When
        val updatedUser = user.copy(name = "Alice Updated", email = "alice.updated@example.com")
        userDao.update(updatedUser)
        val retrievedUser = userDao.getUserById(1)

        // Then
        assertNotNull(retrievedUser)
        assertEquals("Alice Updated", retrievedUser?.name)
        assertEquals("alice.updated@example.com", retrievedUser?.email)
    }

    @Test
    fun deleteUser() = runBlocking {
        // Given
        val user = User(id = 1, name = "Alice", email = "alice@example.com")
        userDao.insert(user)

        // When
        userDao.delete(user)
        val retrievedUser = userDao.getUserById(1)

        // Then
        assertNull(retrievedUser)
    }

    @Test
    fun searchUsersByName() = runBlocking {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "Bob", email = "bob@example.com"),
            User(id = 3, name = "Alice Smith", email = "alice.smith@example.com")
        )
        users.forEach { userDao.insert(it) }

        // When
        val aliceUsers = userDao.searchUsersByName("Alice")

        // Then
        assertEquals(2, aliceUsers.size)
        assertTrue(aliceUsers.all { it.name.contains("Alice") })
    }

    @Test
    fun searchUsersCaseInsensitive() = runBlocking {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "alice smith", email = "alice.smith@example.com")
        )
        users.forEach { userDao.insert(it) }

        // When
        val aliceUsers = userDao.searchUsersByName("alice")

        // Then
        assertEquals(2, aliceUsers.size)
    }

    @Test
    fun deleteAllUsers() = runBlocking {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "Bob", email = "bob@example.com")
        )
        users.forEach { userDao.insert(it) }

        // When
        userDao.deleteAll()
        val allUsers = userDao.getAllUsers()

        // Then
        assertTrue(allUsers.isEmpty())
    }

    @Test
    fun getUserByIdNotFound() = runBlocking {
        // When
        val user = userDao.getUserById(999)

        // Then
        assertNull(user)
    }

    @Test
    fun searchUsersEmptyResult() = runBlocking {
        // Given
        val users = listOf(
            User(id = 1, name = "Alice", email = "alice@example.com"),
            User(id = 2, name = "Bob", email = "bob@example.com")
        )
        users.forEach { userDao.insert(it) }

        // When
        val searchResults = userDao.searchUsersByName("Charlie")

        // Then
        assertTrue(searchResults.isEmpty())
    }
}
