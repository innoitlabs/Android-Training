package com.example.performancesecurity.NotesApp

import java.util.Date

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isEncrypted: Boolean = false
)
