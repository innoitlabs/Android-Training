package com.example.performancesecurity.NotesApp

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)

data class SyncRequest(
    val notes: List<Note>,
    val lastSyncTime: Long
)

data class SyncResponse(
    val notes: List<Note>,
    val serverTime: Long
)
