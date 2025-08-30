package com.example.testingqa

import com.example.testingqa.data.ApiService
import com.example.testingqa.data.User
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    
    @get:Rule
    val server = MockWebServer()
    
    private lateinit var apiService: ApiService
    private val gson = Gson()
    
    @Before
    fun setup() {
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }
    
    @After
    fun tearDown() {
        server.shutdown()
    }
    
    @Test
    fun testGetUsers() = runBlocking {
        // Given
        val mockResponse = """
            [
                {"id": 1, "name": "Alice", "email": "alice@example.com"},
                {"id": 2, "name": "Bob", "email": "bob@example.com"}
            ]
        """.trimIndent()
        
        server.enqueue(MockResponse().setBody(mockResponse))
        
        // When
        val users = apiService.getUsers()
        
        // Then
        assertEquals(2, users.size)
        assertEquals("Alice", users[0].name)
        assertEquals("alice@example.com", users[0].email)
        assertEquals("Bob", users[1].name)
        assertEquals("bob@example.com", users[1].email)
    }
    
    @Test
    fun testGetUserById() = runBlocking {
        // Given
        val mockResponse = """
            {"id": 1, "name": "Alice", "email": "alice@example.com"}
        """.trimIndent()
        
        server.enqueue(MockResponse().setBody(mockResponse))
        
        // When
        val user = apiService.getUserById(1)
        
        // Then
        assertEquals(1, user.id)
        assertEquals("Alice", user.name)
        assertEquals("alice@example.com", user.email)
    }
    
    @Test
    fun testCreateUser() = runBlocking {
        // Given
        val newUser = User(id = 0, name = "Charlie", email = "charlie@example.com")
        val createdUser = User(id = 3, name = "Charlie", email = "charlie@example.com")
        
        server.enqueue(MockResponse().setBody(gson.toJson(createdUser)))
        
        // When
        val result = apiService.createUser(newUser)
        
        // Then
        assertEquals(3, result.id)
        assertEquals("Charlie", result.name)
        assertEquals("charlie@example.com", result.email)
    }
    
    @Test
    fun testUpdateUser() = runBlocking {
        // Given
        val updatedUser = User(id = 1, name = "Alice Updated", email = "alice.updated@example.com")
        
        server.enqueue(MockResponse().setBody(gson.toJson(updatedUser)))
        
        // When
        val result = apiService.updateUser(1, updatedUser)
        
        // Then
        assertEquals(1, result.id)
        assertEquals("Alice Updated", result.name)
        assertEquals("alice.updated@example.com", result.email)
    }
    
    @Test
    fun testDeleteUser() = runBlocking {
        // Given
        server.enqueue(MockResponse().setResponseCode(204))
        
        // When & Then (should not throw exception)
        apiService.deleteUser(1)
    }
    
    @Test
    fun testApiErrorHandling() = runBlocking {
        // Given
        server.enqueue(MockResponse().setResponseCode(500))
        
        // When & Then
        assertThrows(HttpException::class.java) {
            runBlocking { apiService.getUsers() }
        }
    }
    
    @Test
    fun testNotFoundError() = runBlocking {
        // Given
        server.enqueue(MockResponse().setResponseCode(404))
        
        // When & Then
        assertThrows(HttpException::class.java) {
            runBlocking { apiService.getUserById(999) }
        }
    }
    
    @Test
    fun testEmptyResponse() = runBlocking {
        // Given
        server.enqueue(MockResponse().setBody("[]"))
        
        // When
        val users = apiService.getUsers()
        
        // Then
        assertTrue(users.isEmpty())
    }
}
