package com.example.intentnavigation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * SecondActivity - Demonstrates receiving data from Intent and returning data back
 * 
 * This activity shows:
 * 1. How to receive data passed via Intent extras
 * 2. How to return data back to the calling activity
 * 3. Best practices for data validation and handling
 */
class SecondActivity : AppCompatActivity() {
    
    // UI Components
    private lateinit var textViewWelcome: TextView
    private lateinit var textViewUserInfo: TextView
    private lateinit var buttonReturnData: Button
    private lateinit var buttonShareProfile: Button
    private lateinit var buttonBack: Button
    
    // Data received from MainActivity
    private var username: String = ""
    private var userId: Int = 0
    private var isLoggedIn: Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        
        // Initialize UI components
        initializeViews()
        
        // Extract data from the Intent that started this activity
        extractIntentData()
        
        // Display the received data
        displayUserInfo()
        
        // Setup click listeners
        setupClickListeners()
    }
    
    /**
     * Initialize all UI components
     */
    private fun initializeViews() {
        textViewWelcome = findViewById(R.id.textViewWelcome)
        textViewUserInfo = findViewById(R.id.textViewUserInfo)
        buttonReturnData = findViewById(R.id.buttonReturnData)
        buttonShareProfile = findViewById(R.id.buttonShareProfile)
        buttonBack = findViewById(R.id.buttonBack)
    }
    
    /**
     * Extract data from the Intent that started this activity
     * This demonstrates how to receive data passed via putExtra()
     */
    private fun extractIntentData() {
        // Get the intent that started this activity
        val intent = intent
        
        // Extract data using getStringExtra(), getIntExtra(), getBooleanExtra()
        username = intent.getStringExtra("USERNAME") ?: "Unknown User"
        userId = intent.getIntExtra("USER_ID", -1)  // Default value if not found
        isLoggedIn = intent.getBooleanExtra("IS_LOGGED_IN", false)
        
        // Log the received data for debugging
        println("Received data: Username=$username, UserID=$userId, IsLoggedIn=$isLoggedIn")
    }
    
    /**
     * Display the received user information in the UI
     */
    private fun displayUserInfo() {
        // Update welcome message
        textViewWelcome.text = "Welcome, $username!"
        
        // Create detailed user info text
        val userInfo = buildString {
            appendLine("User Information:")
            appendLine("Username: $username")
            appendLine("User ID: $userId")
            appendLine("Login Status: ${if (isLoggedIn) "Logged In" else "Not Logged In"}")
            appendLine()
            appendLine("This data was passed via Explicit Intent")
        }
        
        textViewUserInfo.text = userInfo
    }
    
    /**
     * Setup click listeners for all buttons
     */
    private fun setupClickListeners() {
        // Button to return data back to MainActivity
        buttonReturnData.setOnClickListener {
            returnDataToMainActivity()
        }
        
        // Button to share profile (implicit intent)
        buttonShareProfile.setOnClickListener {
            shareProfile()
        }
        
        // Button to go back
        buttonBack.setOnClickListener {
            finish()
        }
    }
    
    /**
     * Return data back to the calling activity (MainActivity)
     * This demonstrates how to send data back using setResult()
     */
    private fun returnDataToMainActivity() {
        // Create a result intent
        val resultIntent = Intent()
        
        // Add data to the result intent
        resultIntent.putExtra("RETURNED_DATA", "Profile viewed for user: $username")
        resultIntent.putExtra("PROFILE_VIEWED", true)
        resultIntent.putExtra("VIEW_TIMESTAMP", System.currentTimeMillis())
        
        // Set the result and finish the activity
        setResult(RESULT_OK, resultIntent)
        
        // Show a toast message
        Toast.makeText(this, "Data returned to MainActivity", Toast.LENGTH_SHORT).show()
        
        // Finish this activity and return to MainActivity
        finish()
    }
    
    /**
     * Share profile information using implicit intent
     * This demonstrates how to use implicit intents to interact with other apps
     */
    private fun shareProfile() {
        val profileText = buildString {
            appendLine("User Profile")
            appendLine("Username: $username")
            appendLine("User ID: $userId")
            appendLine("Status: ${if (isLoggedIn) "Active" else "Inactive"}")
            appendLine()
            appendLine("Shared from Intent Navigation App")
        }
        
        // Create share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, profileText)
            putExtra(Intent.EXTRA_SUBJECT, "Profile: $username")
        }
        
        // Create chooser and start activity
        val chooser = Intent.createChooser(shareIntent, "Share Profile")
        
        // Check if there are apps that can handle this intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(this, "No sharing app found", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Handle back button press
     * This ensures data is returned even if user presses back button
     */
    override fun onBackPressed() {
        // Return some data even when back button is pressed
        val resultIntent = Intent()
        resultIntent.putExtra("RETURNED_DATA", "User pressed back button")
        resultIntent.putExtra("BACK_PRESSED", true)
        setResult(RESULT_CANCELED, resultIntent)
        
        super.onBackPressed()
    }
}
