package com.example.coroutinesasync

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.example.coroutinesasync.data.User
import com.example.coroutinesasync.worker.SyncWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    
    private lateinit var counterText: TextView
    private lateinit var incrementButton: Button
    private lateinit var decrementButton: Button
    private lateinit var resultText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var addUserButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var loadPostsButton: Button
    private lateinit var backgroundTaskButton: Button
    private lateinit var usersListView: ListView
    private lateinit var postsListView: ListView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupClickListeners()
        observeViewModel()
        schedulePeriodicSync()
    }
    
    private fun initializeViews() {
        counterText = findViewById(R.id.counterText)
        incrementButton = findViewById(R.id.incrementButton)
        decrementButton = findViewById(R.id.decrementButton)
        resultText = findViewById(R.id.resultText)
        progressBar = findViewById(R.id.progressBar)
        addUserButton = findViewById(R.id.addUserButton)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        loadPostsButton = findViewById(R.id.loadPostsButton)
        backgroundTaskButton = findViewById(R.id.backgroundTaskButton)
        usersListView = findViewById(R.id.usersListView)
        postsListView = findViewById(R.id.postsListView)
    }
    
    private fun setupClickListeners() {
        incrementButton.setOnClickListener {
            viewModel.incrementCounter()
        }
        
        decrementButton.setOnClickListener {
            viewModel.decrementCounter()
        }
        
        addUserButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            viewModel.addUser(name, email)
            nameEditText.text.clear()
            emailEditText.text.clear()
        }
        
        loadPostsButton.setOnClickListener {
            viewModel.loadPosts()
        }
        
        backgroundTaskButton.setOnClickListener {
            viewModel.performBackgroundTask()
        }
    }
    
    private fun observeViewModel() {
        // Observe counter
        lifecycleScope.launch {
            viewModel.counter.collect { count ->
                counterText.text = "Counter: $count"
            }
        }
        
        // Observe UI state
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        resultText.text = "Loading..."
                        progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        resultText.text = state.data
                        progressBar.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        resultText.text = "Error: ${state.message}"
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
        
        // Observe users
        lifecycleScope.launch {
            viewModel.allUsers.collect { users ->
                val userNames = users.map { "${it.name} (${it.email})" }
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, userNames)
                usersListView.adapter = adapter
                
                // Add click listener for deletion
                usersListView.setOnItemClickListener { _, _, position, _ ->
                    val user = users[position]
                    viewModel.deleteUser(user)
                }
            }
        }
        
        // Observe posts
        lifecycleScope.launch {
            viewModel.posts.collect { posts ->
                val postTitles = posts.map { it.title }
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, postTitles)
                postsListView.adapter = adapter
            }
        }
    }
    
    private fun schedulePeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            15, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        ).build()
        
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "sync_work",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
    }
}