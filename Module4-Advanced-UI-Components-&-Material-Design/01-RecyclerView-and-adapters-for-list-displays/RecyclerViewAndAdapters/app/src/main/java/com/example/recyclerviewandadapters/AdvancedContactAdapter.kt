package com.example.recyclerviewandadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Advanced ContactAdapter using ListAdapter and DiffUtil for efficient updates
 * 
 * This adapter demonstrates best practices for RecyclerView:
 * - Uses ListAdapter for automatic DiffUtil handling
 * - Efficient updates with minimal view recycling
 * - Proper separation of concerns
 */
class AdvancedContactAdapter(
    private val onItemClick: (Contact) -> Unit,
    private val onCallClick: (Contact) -> Unit
) : ListAdapter<Contact, AdvancedContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    /**
     * ViewHolder class that holds references to the views in each item
     */
    class ContactViewHolder(
        private val binding: View,
        private val onItemClick: (Contact) -> Unit,
        private val onCallClick: (Contact) -> Unit
    ) : RecyclerView.ViewHolder(binding) {
        
        private val profileImage: ImageView = binding.findViewById(R.id.profileImage)
        private val nameText: TextView = binding.findViewById(R.id.nameText)
        private val phoneText: TextView = binding.findViewById(R.id.phoneText)
        private val phoneButton: ImageButton = binding.findViewById(R.id.phoneButton)
        
        private var currentContact: Contact? = null
        
        init {
            // Set click listeners once during ViewHolder creation
            binding.setOnClickListener {
                currentContact?.let { onItemClick(it) }
            }
            
            phoneButton.setOnClickListener {
                currentContact?.let { onCallClick(it) }
            }
        }
        
        /**
         * Binds contact data to the views
         */
        fun bind(contact: Contact) {
            currentContact = contact
            
            nameText.text = contact.name
            phoneText.text = contact.phone
            profileImage.setImageResource(contact.profileImageResId)
        }
    }

    /**
     * Creates a new ViewHolder when needed
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view, onItemClick, onCallClick)
    }

    /**
     * Binds data to the ViewHolder at the specified position
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }
}

/**
 * DiffUtil callback for efficient list updates
 * 
 * This class determines how to handle updates to the contact list:
 * - areItemsTheSame: Determines if two items represent the same contact
 * - areContentsTheSame: Determines if the content of two contacts is the same
 */
class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    
    /**
     * Determines if two items represent the same contact
     * In a real app, you would use a unique ID here
     */
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        // For this example, we'll use name + phone as a composite key
        // In a real app, you should use a unique ID from your database
        return oldItem.name == newItem.name && oldItem.phone == newItem.phone
    }
    
    /**
     * Determines if the content of two contacts is the same
     */
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}
