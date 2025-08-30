package com.example.recyclerviewandadapters

/**
 * Data class representing a contact in the RecyclerView example
 * 
 * @param name The contact's full name
 * @param phone The contact's phone number
 * @param email The contact's email address (optional)
 * @param profileImageResId Resource ID for the contact's profile image (optional)
 */
data class Contact(
    val name: String,
    val phone: String,
    val email: String = "",
    val profileImageResId: Int = R.drawable.ic_launcher_foreground
)
