package com.example.recyclerviewandadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for displaying contacts in a RecyclerView
 * 
 * @param contacts List of contacts to display
 * @param onItemClick Callback for when a contact item is clicked
 * @param onCallClick Callback for when the call button is clicked
 */
class ContactAdapter(
    private val contacts: List<Contact>,
    private val onItemClick: (Contact) -> Unit,
    private val onCallClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    /**
     * ViewHolder class that holds references to the views in each item
     */
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val phoneText: TextView = itemView.findViewById(R.id.phoneText)
        val phoneButton: ImageButton = itemView.findViewById(R.id.phoneButton)
    }

    /**
     * Creates a new ViewHolder when needed
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    /**
     * Binds data to the ViewHolder at the specified position
     */
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        
        // Bind data to views
        holder.nameText.text = contact.name
        holder.phoneText.text = contact.phone
        holder.profileImage.setImageResource(contact.profileImageResId)
        
        // Set click listeners
        holder.itemView.setOnClickListener {
            onItemClick(contact)
        }
        
        holder.phoneButton.setOnClickListener {
            onCallClick(contact)
        }
    }

    /**
     * Returns the total number of items in the data set
     */
    override fun getItemCount(): Int = contacts.size
}
