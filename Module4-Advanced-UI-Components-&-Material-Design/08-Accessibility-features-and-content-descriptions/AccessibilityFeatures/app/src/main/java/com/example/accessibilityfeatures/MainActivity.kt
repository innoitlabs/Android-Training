package com.example.accessibilityfeatures

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var errorText: TextView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var progressView: AccessibleProgressView
    private lateinit var startProgressButton: Button
    private lateinit var resetProgressButton: Button
    private lateinit var actionButton: AccessibleActionButton
    private lateinit var statusText: TextView
    private lateinit var updateStatusButton: Button
    
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initializeViews()
        setupAccessibility()
        setupValidation()
        setupEventListeners()
    }
    
    private fun initializeViews() {
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        errorText = findViewById(R.id.errorText)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)
        progressView = findViewById(R.id.progressView)
        startProgressButton = findViewById(R.id.startProgressButton)
        resetProgressButton = findViewById(R.id.resetProgressButton)
        actionButton = findViewById(R.id.actionButton)
        statusText = findViewById(R.id.statusText)
        updateStatusButton = findViewById(R.id.updateStatusButton)
    }
    
    private fun setupAccessibility() {
        // Set up live region for error messages
        errorText.accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_ASSERTIVE
        
        // Set up live region for status updates
        statusText.accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
        
        // Programmatically set content descriptions for dynamic content
        updateStatusText("Ready to start")
    }
    
    private fun setupValidation() {
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        emailInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateEmail(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        
        phoneInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validatePhone(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    
    private fun setupEventListeners() {
        saveButton.setOnClickListener {
            if (validateForm()) {
                saveProfile()
            }
        }
        
        cancelButton.setOnClickListener {
            clearForm()
        }
        
        startProgressButton.setOnClickListener {
            startProgressSimulation()
        }
        
        resetProgressButton.setOnClickListener {
            resetProgress()
        }
        
        actionButton.setOnClickListener {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()
        }
        
        actionButton.setOnLongClickListener {
            Toast.makeText(this, "Long press detected!", Toast.LENGTH_SHORT).show()
            true
        }
        
        actionButton.setOnCustomActionListener(object : AccessibleActionButton.OnCustomActionListener {
            override fun onCustomAction(view: View) {
                Toast.makeText(this@MainActivity, "Custom action triggered!", Toast.LENGTH_SHORT).show()
            }
        })
        
        updateStatusButton.setOnClickListener {
            updateStatusWithRandomMessage()
        }
    }
    
    private fun validateName(name: String) {
        if (name.isNotEmpty() && name.length < 2) {
            showError("Name must be at least 2 characters long")
        } else if (name.isEmpty()) {
            clearError()
        } else {
            clearError()
        }
    }
    
    private fun validateEmail(email: String) {
        if (email.isNotEmpty()) {
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if (!email.matches(emailPattern.toRegex())) {
                showError("Please enter a valid email address")
            } else {
                clearError()
            }
        } else {
            clearError()
        }
    }
    
    private fun validatePhone(phone: String) {
        if (phone.isNotEmpty() && phone.length < 10) {
            showError("Phone number must be at least 10 digits")
        } else if (phone.isEmpty()) {
            clearError()
        } else {
            clearError()
        }
    }
    
    private fun validateForm(): Boolean {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()
        
        if (name.isEmpty()) {
            showError("Name is required")
            return false
        }
        
        if (email.isEmpty()) {
            showError("Email is required")
            return false
        }
        
        if (phone.isEmpty()) {
            showError("Phone number is required")
            return false
        }
        
        clearError()
        return true
    }
    
    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE
        // Using the newer API for accessibility announcements
        errorText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
    }
    
    private fun clearError() {
        errorText.text = ""
        errorText.visibility = View.GONE
    }
    
    private fun saveProfile() {
        val name = nameInput.text.toString()
        val email = emailInput.text.toString()
        val phone = phoneInput.text.toString()
        
        // Simulate saving
        updateStatusText("Saving profile...")
        
        handler.postDelayed({
            updateStatusText("Profile saved successfully!")
            Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
        }, 1000)
    }
    
    private fun clearForm() {
        nameInput.text.clear()
        emailInput.text.clear()
        phoneInput.text.clear()
        clearError()
        updateStatusText("Form cleared")
    }
    
    private fun startProgressSimulation() {
        updateStatusText("Starting progress simulation...")
        
        var currentProgress = 0
        progressRunnable = object : Runnable {
            override fun run() {
                if (currentProgress <= 100) {
                    progressView.setProgress(currentProgress)
                    currentProgress += 10
                    handler.postDelayed(this, 500)
                } else {
                    updateStatusText("Progress simulation completed!")
                }
            }
        }
        
        handler.post(progressRunnable!!)
    }
    
    private fun resetProgress() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressView.setProgress(0)
        updateStatusText("Progress reset to zero")
    }
    
    private fun updateStatusText(message: String) {
        statusText.text = message
        // Using the newer API for accessibility announcements
        statusText.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
    }
    
    private fun updateStatusWithRandomMessage() {
        val messages = listOf(
            "Status updated: All systems operational",
            "Status updated: Processing complete",
            "Status updated: Ready for next task",
            "Status updated: Configuration saved",
            "Status updated: Data synchronized"
        )
        
        val randomMessage = messages.random()
        updateStatusText(randomMessage)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        progressRunnable?.let { handler.removeCallbacks(it) }
    }
}