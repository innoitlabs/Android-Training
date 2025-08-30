package com.example.performancesecurity.NotesApp

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkClient(private val context: Context) {
    private val okHttpClient: OkHttpClient
    private val apiService: ApiService
    
    init {
        okHttpClient = createOkHttpClient()
        apiService = createApiService()
    }
    
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("User-Agent", "NotesApp/1.0")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    private fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Replace with your API URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    suspend fun syncNotes(notes: List<Note>, lastSyncTime: Long): Result<SyncResponse> {
        return try {
            val request = SyncRequest(notes, lastSyncTime)
            val response = apiService.syncNotes(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Sync failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
