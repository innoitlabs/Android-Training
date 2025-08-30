package com.example.recyclerviewandadapters

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * MainActivity demonstrating RecyclerView implementation
 * 
 * This activity shows how to:
 * - Set up a RecyclerView with LinearLayoutManager
 * - Create and use a custom adapter
 * - Handle item click events
 * - Display sample contact data
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        
        // Create sample data
        val contacts = createSampleContacts()
        
        // Set up RecyclerView with LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ContactAdapter(
            contacts = contacts,
            onItemClick = { contact ->
                showContactDetails(contact)
            },
            onCallClick = { contact ->
                makePhoneCall(contact)
            }
        )
    }

    /**
     * Creates sample contact data for demonstration
     */
    private fun createSampleContacts(): List<Contact> {
        return listOf(
            Contact("Alice Johnson", "+1 (555) 123-4567", "alice@email.com"),
            Contact("Bob Smith", "+1 (555) 234-5678", "bob@email.com"),
            Contact("Charlie Brown", "+1 (555) 345-6789", "charlie@email.com"),
            Contact("Diana Prince", "+1 (555) 456-7890", "diana@email.com"),
            Contact("Eve Wilson", "+1 (555) 567-8901", "eve@email.com"),
            Contact("Frank Miller", "+1 (555) 678-9012", "frank@email.com"),
            Contact("Grace Lee", "+1 (555) 789-0123", "grace@email.com"),
            Contact("Henry Davis", "+1 (555) 890-1234", "henry@email.com"),
            Contact("Iris Thompson", "+1 (555) 901-2345", "iris@email.com"),
            Contact("Jack Wilson", "+1 (555) 012-3456", "jack@email.com")
        )
    }

    /**
     * Shows contact details when an item is clicked
     */
    private fun showContactDetails(contact: Contact) {
        Toast.makeText(
            this,
            "Selected: ${contact.name}\nPhone: ${contact.phone}\nEmail: ${contact.email}",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Simulates making a phone call when the call button is clicked
     */
    private fun makePhoneCall(contact: Contact) {
        Toast.makeText(
            this,
            "Calling ${contact.name} at ${contact.phone}",
            Toast.LENGTH_SHORT
        ).show()
        // In a real app, you would use Intent to make actual phone calls
        // Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
    }
}