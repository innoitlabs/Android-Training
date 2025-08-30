package com.example.mvvmarchitecture

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val phone: String = "",
    val website: String = "",
    val company: String = ""
)
