package com.example.performancesecurity.NotesApp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("sync")
    suspend fun syncNotes(@Body request: SyncRequest): Response<ApiResponse<SyncResponse>>
}
