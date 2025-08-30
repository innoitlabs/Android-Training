package com.example.dependencyhilt

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dependencyhilt.databinding.ActivityMainBinding
import com.example.dependencyhilt.presentation.adapter.UserAdapter
import com.example.dependencyhilt.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity that demonstrates Hilt dependency injection.
 * 
 * This activity uses @AndroidEntryPoint to enable Hilt injection and
 * demonstrates how to use ViewModels with LiveData for reactive UI updates.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        
        // Load users when activity starts
        viewModel.loadUsers()
    }
    
    /**
     * Set up the RecyclerView with adapter and layout manager.
     */
    private fun setupRecyclerView() {
        userAdapter = UserAdapter().apply {
            onUserClickListener = { user ->
                Toast.makeText(
                    this@MainActivity,
                    "Clicked on ${user.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        
        binding.recyclerViewUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }
    
    /**
     * Set up observers for ViewModel LiveData.
     */
    private fun setupObservers() {
        // Observe UI state changes
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UserViewModel.UiState.Loading -> {
                    showLoading(true)
                    hideError()
                }
                is UserViewModel.UiState.Success -> {
                    showLoading(false)
                    hideError()
                    userAdapter.submitList(state.users)
                }
                is UserViewModel.UiState.Error -> {
                    showLoading(false)
                    showError(state.message)
                }
            }
        }
        
        // Observe users list changes
        viewModel.users.observe(this) { users ->
            userAdapter.submitList(users)
        }
    }
    
    /**
     * Set up click listeners for UI elements.
     */
    private fun setupClickListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshUsers()
        }
        
        binding.fabRefresh.setOnClickListener {
            viewModel.refreshUsers()
        }
    }
    
    /**
     * Show or hide loading state.
     */
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.swipeRefreshLayout.isRefreshing = show
    }
    
    /**
     * Show error message.
     */
    private fun showError(message: String) {
        binding.textViewError.text = message
        binding.textViewError.visibility = View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    /**
     * Hide error message.
     */
    private fun hideError() {
        binding.textViewError.visibility = View.GONE
    }
}