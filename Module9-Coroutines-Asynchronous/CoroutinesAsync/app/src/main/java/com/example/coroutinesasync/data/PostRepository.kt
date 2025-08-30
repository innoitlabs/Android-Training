package com.example.coroutinesasync.data

class PostRepository {
    private val apiService = RetrofitClient.apiService
    
    suspend fun getPosts(): List<Post> {
        return try {
            apiService.getPosts()
        } catch (e: Exception) {
            throw NetworkException("Failed to fetch posts: ${e.message}")
        }
    }
    
    suspend fun getPost(id: Int): Post {
        return try {
            apiService.getPost(id)
        } catch (e: Exception) {
            throw NetworkException("Failed to fetch post: ${e.message}")
        }
    }
}

class NetworkException(message: String) : Exception(message)
