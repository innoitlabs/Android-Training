package com.example.responsivedesign

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneText: TextView
    private lateinit var locationText: TextView
    private lateinit var bioText: TextView
    private lateinit var editButton: Button
    private lateinit var followButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupClickListeners()
        handleScreenConfiguration()
    }

    private fun initializeViews() {
        nameText = findViewById(R.id.nameText)
        emailText = findViewById(R.id.emailText)
        phoneText = findViewById(R.id.phoneText)
        locationText = findViewById(R.id.locationText)
        bioText = findViewById(R.id.bioText)
        editButton = findViewById(R.id.editButton)
        followButton = findViewById(R.id.followButton)
    }

    private fun setupClickListeners() {
        editButton.setOnClickListener {
            // Handle edit profile action
            showEditProfileDialog()
        }

        followButton.setOnClickListener {
            // Handle follow action
            toggleFollowState()
        }
    }

    private fun handleScreenConfiguration() {
        val screenWidth = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density
        val screenWidthDp = screenWidth / density

        when {
            screenWidthDp >= 600 -> {
                // Tablet layout
                setupTabletUI()
            }
            screenWidthDp >= 480 -> {
                // Large phone
                setupLargePhoneUI()
            }
            else -> {
                // Small phone
                setupSmallPhoneUI()
            }
        }
    }

    private fun setupTabletUI() {
        // Additional tablet-specific setup if needed
        followButton.text = getString(R.string.follow)
    }

    private fun setupLargePhoneUI() {
        // Additional large phone setup if needed
    }

    private fun setupSmallPhoneUI() {
        // Additional small phone setup if needed
    }

    private fun showEditProfileDialog() {
        // Implementation for edit profile dialog
        // This would typically show a dialog or navigate to an edit screen
        // For now, we'll just show a simple toast message
        android.widget.Toast.makeText(this, "Edit Profile clicked!", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun toggleFollowState() {
        val currentText = followButton.text.toString()
        if (currentText == getString(R.string.follow)) {
            followButton.text = "Following"
        } else {
            followButton.text = getString(R.string.follow)
        }
    }
}