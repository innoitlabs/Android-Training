package com.example.mvvmarchitecture

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmarchitecture.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup data binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        // Initialize ViewModel with dependencies
        val apiService = RetrofitClient.apiService
        val database = AppDatabase.getDatabase(this)
        val userDao = database.userDao()
        val repository = UserRepository(apiService, userDao)
        viewModel = UserViewModel(repository)
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        
        setupRecyclerView()
        setupSwipeRefresh()
        setupSearch()
        setupObservers()
    }
    
    private fun setupRecyclerView() {
        userAdapter = UserAdapter { user ->
            Toast.makeText(this, "Clicked: ${user.name}", Toast.LENGTH_SHORT).show()
        }
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshUsers()
        }
    }
    
    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchUsers(s?.toString() ?: "")
            }
        })
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            binding.swipeRefreshLayout.isRefreshing = false
            
            when (state) {
                is UiState.Loading -> {
                    // Loading state handled by data binding
                }
                is UiState.Success -> {
                    userAdapter.submitList(state.data)
                }
                is UiState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}