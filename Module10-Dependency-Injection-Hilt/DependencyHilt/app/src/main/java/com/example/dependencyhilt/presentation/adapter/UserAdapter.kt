package com.example.dependencyhilt.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dependencyhilt.data.model.User
import com.example.dependencyhilt.databinding.ItemUserBinding

/**
 * RecyclerView adapter for displaying a list of users.
 * 
 * This adapter uses ViewBinding for efficient view access and
 * DiffUtil for optimal list updates.
 */
class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {
    
    /**
     * Callback for when a user item is clicked.
     */
    var onUserClickListener: ((User) -> Unit)? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    /**
     * ViewHolder for user items.
     */
    inner class UserViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            // Set click listener for the entire item
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUserClickListener?.invoke(getItem(position))
                }
            }
        }
        
        /**
         * Bind user data to the view.
         * 
         * @param user The user data to display
         */
        fun bind(user: User) {
            binding.apply {
                tvUserName.text = user.name
                tvUserEmail.text = user.email
                tvUserUsername.text = "@${user.username}"
                
                // Display company name if available
                tvUserCompany.text = user.company?.name ?: "No company info"
                
                // Display phone if available
                tvUserPhone.text = user.phone ?: "No phone info"
            }
        }
    }
    
    /**
     * DiffUtil callback for efficient list updates.
     */
    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
