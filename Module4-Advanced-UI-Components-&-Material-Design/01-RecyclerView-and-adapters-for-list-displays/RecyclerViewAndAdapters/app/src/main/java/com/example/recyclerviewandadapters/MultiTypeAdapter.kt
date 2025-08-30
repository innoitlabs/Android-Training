package com.example.recyclerviewandadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Multi-type adapter that can display different types of items
 * 
 * This adapter demonstrates how to handle multiple view types in a RecyclerView:
 * - Headers (section titles)
 * - Contact items
 */
class MultiTypeAdapter(
    private val onContactClick: (Contact) -> Unit,
    private val onCallClick: (Contact) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_CONTACT = 1
    }

    private val items = mutableListOf<ListItem>()

    /**
     * Sealed class representing different types of list items
     */
    sealed class ListItem {
        data class Header(val title: String) : ListItem()
        data class ContactItem(val contact: Contact) : ListItem()
    }

    /**
     * ViewHolder for header items
     */
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.headerTitle)
        
        fun bind(header: ListItem.Header) {
            titleText.text = header.title
        }
    }

    /**
     * ViewHolder for contact items
     */
    class ContactViewHolder(
        itemView: View,
        private val onContactClick: (Contact) -> Unit,
        private val onCallClick: (Contact) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val profileImage: android.widget.ImageView = itemView.findViewById(R.id.profileImage)
        private val nameText: TextView = itemView.findViewById(R.id.nameText)
        private val phoneText: TextView = itemView.findViewById(R.id.phoneText)
        private val phoneButton: android.widget.ImageButton = itemView.findViewById(R.id.phoneButton)
        
        private var currentContact: Contact? = null
        
        init {
            itemView.setOnClickListener {
                currentContact?.let { onContactClick(it) }
            }
            
            phoneButton.setOnClickListener {
                currentContact?.let { onCallClick(it) }
            }
        }
        
        fun bind(contactItem: ListItem.ContactItem) {
            currentContact = contactItem.contact
            val contact = contactItem.contact
            
            nameText.text = contact.name
            phoneText.text = contact.phone
            profileImage.setImageResource(contact.profileImageResId)
        }
    }

    /**
     * Determines the view type for the item at the given position
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.Header -> VIEW_TYPE_HEADER
            is ListItem.ContactItem -> VIEW_TYPE_CONTACT
        }
    }

    /**
     * Creates the appropriate ViewHolder based on the view type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_CONTACT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contact, parent, false)
                ContactViewHolder(view, onContactClick, onCallClick)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    /**
     * Binds data to the appropriate ViewHolder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.Header -> {
                (holder as HeaderViewHolder).bind(item)
            }
            is ListItem.ContactItem -> {
                (holder as ContactViewHolder).bind(item)
            }
        }
    }

    /**
     * Returns the total number of items
     */
    override fun getItemCount(): Int = items.size

    /**
     * Updates the list with new items
     */
    fun updateItems(newItems: List<ListItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * Creates sample data with headers and contacts
     */
    fun loadSampleData() {
        val sampleItems = mutableListOf<ListItem>()
        
        // Add header for family
        sampleItems.add(ListItem.Header("Family"))
        sampleItems.add(ListItem.ContactItem(Contact("Alice Johnson", "+1 (555) 123-4567", "alice@email.com")))
        sampleItems.add(ListItem.ContactItem(Contact("Bob Smith", "+1 (555) 234-5678", "bob@email.com")))
        
        // Add header for friends
        sampleItems.add(ListItem.Header("Friends"))
        sampleItems.add(ListItem.ContactItem(Contact("Charlie Brown", "+1 (555) 345-6789", "charlie@email.com")))
        sampleItems.add(ListItem.ContactItem(Contact("Diana Prince", "+1 (555) 456-7890", "diana@email.com")))
        sampleItems.add(ListItem.ContactItem(Contact("Eve Wilson", "+1 (555) 567-8901", "eve@email.com")))
        
        // Add header for work
        sampleItems.add(ListItem.Header("Work"))
        sampleItems.add(ListItem.ContactItem(Contact("Frank Miller", "+1 (555) 678-9012", "frank@email.com")))
        sampleItems.add(ListItem.ContactItem(Contact("Grace Lee", "+1 (555) 789-0123", "grace@email.com")))
        
        updateItems(sampleItems)
    }
}
