package com.example.testingqa

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testingqa.data.AppDatabase
import com.example.testingqa.data.User
import com.example.testingqa.data.UserDao
import com.example.testingqa.utils.Calculator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class PerformanceTest {
    
    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao
    private lateinit var calculator: Calculator
    
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        calculator = Calculator()
    }
    
    @After
    fun cleanup() {
        db.close()
    }
    
    @Test
    fun testDatabaseInsertPerformance() = runBlocking {
        val users = (1..1000).map { 
            User(id = it.toLong(), name = "User$it", email = "user$it@example.com") 
        }
        
        val insertTime = measureTimeMillis {
            users.forEach { user ->
                userDao.insert(user)
            }
        }
        
        // Should complete within 2 seconds
        assertTrue("Database insert took too long: ${insertTime}ms", insertTime < 2000)
        
        // Verify all users were inserted
        val allUsers = userDao.getAllUsers()
        assertEquals(1000, allUsers.size)
    }
    
    @Test
    fun testDatabaseBatchInsertPerformance() = runBlocking {
        val users = (1..1000).map { 
            User(id = it.toLong(), name = "User$it", email = "user$it@example.com") 
        }
        
        val insertTime = measureTimeMillis {
            userDao.insertAll(users)
        }
        
        // Should complete within 1 second
        assertTrue("Batch insert took too long: ${insertTime}ms", insertTime < 1000)
        
        // Verify all users were inserted
        val allUsers = userDao.getAllUsers()
        assertEquals(1000, allUsers.size)
    }
    
    @Test
    fun testDatabaseSearchPerformance() = runBlocking {
        // Insert test data
        val users = (1..1000).map { 
            User(id = it.toLong(), name = "User$it", email = "user$it@example.com") 
        }
        userDao.insertAll(users)
        
        val searchTime = measureTimeMillis {
            repeat(100) {
                userDao.searchUsersByName("User")
            }
        }
        
        // Should complete within 1 second
        assertTrue("Search took too long: ${searchTime}ms", searchTime < 1000)
    }
    
    @Test
    fun testCalculatorPerformance() {
        val calculationTime = measureTimeMillis {
            repeat(10000) {
                calculator.add(it, it + 1)
                calculator.multiply(it, it + 1)
                calculator.divide(it + 1, it + 2)
            }
        }
        
        // Should complete within 500ms
        assertTrue("Calculator operations took too long: ${calculationTime}ms", calculationTime < 500)
    }
    
    @Test
    fun testFactorialPerformance() {
        val factorialTime = measureTimeMillis {
            repeat(100) {
                calculator.factorial(10)
            }
        }
        
        // Should complete within 1 second
        assertTrue("Factorial calculations took too long: ${factorialTime}ms", factorialTime < 1000)
    }
    
    @Test
    fun testPowerPerformance() {
        val powerTime = measureTimeMillis {
            repeat(1000) {
                calculator.power(2, 10)
                calculator.power(3, 5)
                calculator.power(5, 3)
            }
        }
        
        // Should complete within 500ms
        assertTrue("Power calculations took too long: ${powerTime}ms", powerTime < 500)
    }
    
    @Test
    fun testDatabaseCRUDPerformance() = runBlocking {
        val crudTime = measureTimeMillis {
            repeat(100) { index ->
                // Create
                val user = User(id = index.toLong(), name = "User$index", email = "user$index@example.com")
                val userId = userDao.insert(user)
                
                // Read
                val retrievedUser = userDao.getUserById(userId)
                assertNotNull(retrievedUser)
                
                // Update
                val updatedUser = user.copy(name = "UpdatedUser$index")
                userDao.update(updatedUser)
                
                // Delete
                userDao.delete(updatedUser)
            }
        }
        
        // Should complete within 2 seconds
        assertTrue("CRUD operations took too long: ${crudTime}ms", crudTime < 2000)
    }
    
    @Test
    fun testMemoryUsage() {
        val initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        
        // Perform some operations
        repeat(1000) {
            calculator.add(it, it + 1)
        }
        
        val finalMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val memoryIncrease = finalMemory - initialMemory
        
        // Memory increase should be reasonable (less than 10MB)
        assertTrue("Memory usage increased too much: ${memoryIncrease / 1024 / 1024}MB", 
                  memoryIncrease < 10 * 1024 * 1024)
    }
}
