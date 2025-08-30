package com.example.materialdesign

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    
    // UI Components
    private lateinit var themeToggleButton: MaterialButton
    private lateinit var primaryButton: MaterialButton
    private lateinit var outlinedButton: MaterialButton
    private lateinit var profileCard: MaterialCardView
    private lateinit var settingsCard: MaterialCardView
    private lateinit var infoCard: MaterialCardView
    private lateinit var featuresCard: MaterialCardView
    
    // Text Views
    private lateinit var headerTitle: MaterialTextView
    private lateinit var headerSubtitle: MaterialTextView
    private lateinit var profileName: MaterialTextView
    private lateinit var profileRole: MaterialTextView
    private lateinit var profileDescription: MaterialTextView
    private lateinit var settingsTitle: MaterialTextView
    private lateinit var settingsDescription: MaterialTextView
    private lateinit var infoTitle: MaterialTextView
    private lateinit var infoDescription: MaterialTextView
    private lateinit var quoteText: MaterialTextView
    private lateinit var infoCaption: MaterialTextView
    private lateinit var featuresTitle: MaterialTextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupClickListeners()
        updateUI()
        setupChipInteractions()
    }
    
    private fun initializeViews() {
        // Buttons
        themeToggleButton = findViewById(R.id.themeToggleButton)
        primaryButton = findViewById(R.id.primaryButton)
        outlinedButton = findViewById(R.id.outlinedButton)
        
        // Cards
        profileCard = findViewById(R.id.profileCard)
        settingsCard = findViewById(R.id.settingsCard)
        infoCard = findViewById(R.id.infoCard)
        featuresCard = findViewById(R.id.featuresCard)
        
        // Text Views
        headerTitle = findViewById(R.id.headerTitle)
        headerSubtitle = findViewById(R.id.headerSubtitle)
        profileName = findViewById(R.id.profileName)
        profileRole = findViewById(R.id.profileRole)
        profileDescription = findViewById(R.id.profileDescription)
        settingsTitle = findViewById(R.id.settingsTitle)
        settingsDescription = findViewById(R.id.settingsDescription)
        infoTitle = findViewById(R.id.infoTitle)
        infoDescription = findViewById(R.id.infoDescription)
        quoteText = findViewById(R.id.quoteText)
        infoCaption = findViewById(R.id.infoCaption)
        featuresTitle = findViewById(R.id.featuresTitle)
    }
    
    private fun setupClickListeners() {
        // Theme toggle functionality
        themeToggleButton.setOnClickListener {
            toggleTheme()
        }
        
        // Primary button click with save simulation
        primaryButton.setOnClickListener {
            simulateSaveOperation()
        }
        
        // Outlined button click with edit simulation
        outlinedButton.setOnClickListener {
            simulateEditOperation()
        }
        
        // Card click listeners for demonstration
        profileCard.setOnClickListener {
            showToast("Profile card tapped!")
        }
        
        settingsCard.setOnClickListener {
            showToast("Settings card tapped!")
        }
        
        infoCard.setOnClickListener {
            showToast("Info card tapped!")
        }
        
        featuresCard.setOnClickListener {
            showToast("Features card tapped!")
        }
    }
    
    private fun setupChipInteractions() {
        // Find all chips and set click listeners
        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGroup)
        if (chipGroup != null) {
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as? Chip
                chip?.setOnClickListener {
                    showToast("Selected: ${chip.text}")
                    // Toggle chip selection
                    chip.isChecked = !chip.isChecked
                }
            }
        }
    }
    
    private fun toggleTheme() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = when (currentMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
        
        AppCompatDelegate.setDefaultNightMode(newMode)
        
        // Update button text
        themeToggleButton.text = if (newMode == AppCompatDelegate.MODE_NIGHT_YES) {
            "Switch to Light Theme"
        } else {
            "Switch to Dark Theme"
        }
        
        // Show feedback
        val themeName = if (newMode == AppCompatDelegate.MODE_NIGHT_YES) "Dark" else "Light"
        showToast("Switched to $themeName theme")
    }
    
    private fun simulateSaveOperation() {
        // Disable button and show loading state
        primaryButton.isEnabled = false
        primaryButton.text = "Saving..."
        
        // Simulate save operation
        primaryButton.postDelayed({
            primaryButton.text = "Changes Saved!"
            showToast("Profile updated successfully!")
            
            // Reset button after delay
            primaryButton.postDelayed({
                primaryButton.text = "Save Changes"
                primaryButton.isEnabled = true
            }, 1500)
        }, 1000)
    }
    
    private fun simulateEditOperation() {
        // Show editing state
        outlinedButton.text = "Editing..."
        outlinedButton.isEnabled = false
        
        // Simulate edit operation
        outlinedButton.postDelayed({
            outlinedButton.text = "Edit Complete!"
            showToast("Edit mode activated")
            
            // Reset button after delay
            outlinedButton.postDelayed({
                outlinedButton.text = "Edit Profile"
                outlinedButton.isEnabled = true
            }, 1000)
        }, 800)
    }
    
    private fun updateUI() {
        // Set initial button text based on current theme
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        themeToggleButton.text = if (isDarkMode) {
            "Switch to Light Theme"
        } else {
            "Switch to Dark Theme"
        }
        
        // Add some dynamic content updates
        updateProfileInfo()
    }
    
    private fun updateProfileInfo() {
        // Simulate dynamic content loading
        profileName.text = "John Doe"
        profileRole.text = "Android Developer"
        profileDescription.text = "Passionate about creating beautiful and functional Android applications using Material Design principles. Experienced in Kotlin, Jetpack Compose, and modern Android development practices."
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    override fun onResume() {
        super.onResume()
        // Update UI when returning to the app
        updateUI()
    }
}