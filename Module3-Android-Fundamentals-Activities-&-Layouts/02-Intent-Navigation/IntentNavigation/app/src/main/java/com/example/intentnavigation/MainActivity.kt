package com.example.intentnavigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * MainActivity - Demonstrates Intent System and Navigation in Android
 * 
 * This activity showcases:
 * 1. Explicit Intents - Navigation within the app
 * 2. Implicit Intents - Interaction with external apps
 * 3. Data passing between activities
 * 4. Best practices for Intent usage
 */
class MainActivity : AppCompatActivity() {
    
    // UI Components
    private lateinit var editTextUsername: EditText
    private lateinit var buttonExplicitIntent: Button
    private lateinit var buttonImplicitIntent: Button
    private lateinit var buttonShareIntent: Button
    private lateinit var buttonNavigationComponent: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Setup window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Initialize UI components
        initializeViews()
        
        // Setup click listeners
        setupClickListeners()
        
        // Display welcome message if data was passed back
        handleReturnedData()
    }
    
    /**
     * Initialize all UI components by finding them in the layout
     */
    private fun initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonExplicitIntent = findViewById(R.id.buttonExplicitIntent)
        buttonImplicitIntent = findViewById(R.id.buttonImplicitIntent)
        buttonShareIntent = findViewById(R.id.buttonShareIntent)
        buttonNavigationComponent = findViewById(R.id.buttonNavigationComponent)
    }
    
    /**
     * Setup click listeners for all buttons
     */
    private fun setupClickListeners() {
        // 1. EXPLICIT INTENT - Navigate to SecondActivity within the app
        buttonExplicitIntent.setOnClickListener {
            navigateToSecondActivity()
        }
        
        // 2. IMPLICIT INTENT - Open web browser
        buttonImplicitIntent.setOnClickListener {
            openWebBrowser()
        }
        
        // 3. IMPLICIT INTENT - Share text
        buttonShareIntent.setOnClickListener {
            shareText()
        }
        
        // 4. NAVIGATION COMPONENT - Modern navigation approach
        buttonNavigationComponent.setOnClickListener {
            navigateWithNavigationComponent()
        }
    }
    
    /**
     * Example 1: EXPLICIT INTENT
     * Navigate to a specific activity within your app
     * This is used when you know exactly which activity to start
     */
    private fun navigateToSecondActivity() {
        val username = editTextUsername.text.toString().trim()
        
        // Validate input
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Create explicit intent - specify the exact activity to start
        val intent = Intent(this, SecondActivity::class.java)
        
        // Pass data to the next activity using putExtra()
        intent.putExtra("USERNAME", username)
        intent.putExtra("USER_ID", 12345)
        intent.putExtra("IS_LOGGED_IN", true)
        
        // Start the activity and expect a result back
        startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY)
        
        // Alternative: If you don't need a result back, use:
        // startActivity(intent)
    }
    
    /**
     * Example 2: IMPLICIT INTENT
     * Open a web browser - let the system choose which app to use
     * This is used when you want any app that can handle the action
     */
    private fun openWebBrowser() {
        // Create implicit intent - specify action and data
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"))
        
        // Check if there's an app that can handle this intent
        if (browserIntent.resolveActivity(packageManager) != null) {
            startActivity(browserIntent)
        } else {
            Toast.makeText(this, "No browser app found", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Example 3: IMPLICIT INTENT
     * Share text with other apps
     */
    private fun shareText() {
        val shareText = "Hello from Intent Navigation App! Username: ${editTextUsername.text}"
        
        // Create share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"  // MIME type for text
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "Sharing from Intent Navigation App")
        }
        
        // Create chooser to let user select which app to use
        val chooser = Intent.createChooser(shareIntent, "Share via")
        
        // Check if there are apps that can handle this intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(this, "No sharing app found", Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Example 4: NAVIGATION COMPONENT
     * Modern approach using Jetpack Navigation Component
     */
    private fun navigateWithNavigationComponent() {
        val username = editTextUsername.text.toString().trim()
        
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Navigate to NavigationActivity which uses Navigation Component
        val intent = Intent(this, NavigationActivity::class.java)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
    }
    
    /**
     * Handle data returned from SecondActivity
     */
    private fun handleReturnedData() {
        // Check if there's data returned from SecondActivity
        val returnedMessage = intent.getStringExtra("RETURNED_MESSAGE")
        if (returnedMessage != null) {
            Toast.makeText(this, returnedMessage, Toast.LENGTH_LONG).show()
        }
    }
    
    /**
     * Handle result from activities started with startActivityForResult
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val returnedData = data?.getStringExtra("RETURNED_DATA")
                Toast.makeText(this, "Data returned: $returnedData", Toast.LENGTH_LONG).show()
            }
        }
    }
    
    companion object {
        private const val REQUEST_CODE_SECOND_ACTIVITY = 1001
    }
}