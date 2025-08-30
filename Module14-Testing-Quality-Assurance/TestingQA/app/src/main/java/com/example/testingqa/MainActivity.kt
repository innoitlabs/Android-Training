package com.example.testingqa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.testingqa.data.AppDatabase
import com.example.testingqa.data.User
import com.example.testingqa.data.UserRepository
import com.example.testingqa.ui.UserViewModel
import com.example.testingqa.utils.StringUtils
import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    
    private lateinit var viewModel: UserViewModel
    private lateinit var stringUtils: StringUtils
    
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var addButton: Button
    private lateinit var searchInput: EditText
    private lateinit var searchButton: Button
    private lateinit var resultText: TextView
    private lateinit var calculatorButton: Button
    private lateinit var calculatorResult: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize logging
        Log.d("MainActivity", "Application started")
        
        // Initialize utilities
        stringUtils = StringUtils()
        
        // Initialize UI components
        initializeViews()
        
        // Initialize ViewModel
        initializeViewModel()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun initializeViews() {
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        addButton = findViewById(R.id.addButton)
        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)
        resultText = findViewById(R.id.resultText)
        calculatorButton = findViewById(R.id.calculatorButton)
        calculatorResult = findViewById(R.id.calculatorResult)
    }
    
    private fun initializeViewModel() {
        val database = AppDatabase.getDatabase(this)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(com.example.testingqa.data.ApiService::class.java)
        val repository = UserRepository(database.userDao(), apiService)
        
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        // Note: In a real app, you'd use a ViewModelFactory to inject the repository
        
        // Observe LiveData
        observeViewModel()
    }
    
    private fun observeViewModel() {
        viewModel.users.observe(this) { users ->
            updateUserList(users)
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            addButton.isEnabled = !isLoading
            searchButton.isEnabled = !isLoading
        }
        
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        
        viewModel.lastAddedUser.observe(this) { user ->
            user?.let {
                Toast.makeText(this, "User added: ${it.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun setupClickListeners() {
        addButton.setOnClickListener {
            addUser()
        }
        
        searchButton.setOnClickListener {
            searchUsers()
        }
        
        calculatorButton.setOnClickListener {
            performCalculation()
        }
    }
    
    private fun addUser() {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        
        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (!stringUtils.isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }
        
        val user = User(name = name, email = email)
        viewModel.addUser(user)
        
        // Clear inputs
        nameInput.text.clear()
        emailInput.text.clear()
    }
    
    private fun searchUsers() {
        val query = searchInput.text.toString().trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show()
            return
        }
        
        viewModel.searchUsers(query)
    }
    
    private fun performCalculation() {
        val calculator = com.example.testingqa.utils.Calculator()
        val result = calculator.add(5, 3)
        calculatorResult.text = "5 + 3 = $result"
    }
    
    private fun updateUserList(users: List<User>) {
        val userText = users.joinToString("\n") { "${it.name} (${it.email})" }
        resultText.text = if (users.isEmpty()) "No users found" else userText
    }
}