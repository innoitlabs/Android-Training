package com.example.sharedpreferences

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var nameInput: TextInputEditText
    private lateinit var ageInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var loadButton: Button
    private lateinit var darkModeSwitch: SwitchMaterial
    private lateinit var notificationsSwitch: SwitchMaterial
    private lateinit var loginButton: Button
    private lateinit var loginStatusText: TextView
    private lateinit var lastLoginText: TextView
    private lateinit var savedDataText: TextView
    private lateinit var clearDataButton: Button
    private lateinit var counterText: TextView
    private lateinit var incrementButton: Button
    private lateinit var decrementButton: Button

    // SharedPreferences instances
    private lateinit var userPrefs: android.content.SharedPreferences
    private lateinit var appPrefs: android.content.SharedPreferences
    private lateinit var sessionPrefs: android.content.SharedPreferences

    // Date formatter for timestamps
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        initializeSharedPreferences()

        // Initialize UI components
        initializeViews()

        // Load saved data on app start
        loadAllSavedData()

        // Setup click listeners
        setupClickListeners()

        // Setup switch listeners
        setupSwitchListeners()
    }

    private fun initializeSharedPreferences() {
        // Create different preference files for different types of data
        userPrefs = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        appPrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sessionPrefs = getSharedPreferences("session_data", Context.MODE_PRIVATE)
    }

    private fun initializeViews() {
        // User information views
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        emailInput = findViewById(R.id.emailInput)
        saveButton = findViewById(R.id.saveButton)
        loadButton = findViewById(R.id.loadButton)

        // App settings views
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        notificationsSwitch = findViewById(R.id.notificationsSwitch)

        // Session views
        loginButton = findViewById(R.id.loginButton)
        loginStatusText = findViewById(R.id.loginStatusText)
        lastLoginText = findViewById(R.id.lastLoginText)

        // Data display views
        savedDataText = findViewById(R.id.savedDataText)
        clearDataButton = findViewById(R.id.clearDataButton)

        // Counter views
        counterText = findViewById(R.id.counterText)
        incrementButton = findViewById(R.id.incrementButton)
        decrementButton = findViewById(R.id.decrementButton)
    }

    private fun setupClickListeners() {
        // Save user data
        saveButton.setOnClickListener {
            saveUserData()
        }

        // Load user data
        loadButton.setOnClickListener {
            loadUserData()
        }

        // Login functionality
        loginButton.setOnClickListener {
            toggleLoginStatus()
        }

        // Clear all data
        clearDataButton.setOnClickListener {
            showClearDataDialog()
        }

        // Counter functionality
        incrementButton.setOnClickListener {
            incrementCounter()
        }

        decrementButton.setOnClickListener {
            decrementCounter()
        }
    }

    private fun setupSwitchListeners() {
        // Dark mode switch
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveAppSettings()
            showToast("Dark mode: ${if (isChecked) "enabled" else "disabled"}")
        }

        // Notifications switch
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveAppSettings()
            showToast("Notifications: ${if (isChecked) "enabled" else "disabled"}")
        }
    }

    private fun saveUserData() {
        val name = nameInput.text.toString().trim()
        val ageStr = ageInput.text.toString().trim()
        val email = emailInput.text.toString().trim()

        // Validate input
        if (name.isEmpty()) {
            showToast("Please enter your name")
            return
        }

        if (ageStr.isEmpty()) {
            showToast("Please enter your age")
            return
        }

        val age = ageStr.toIntOrNull()
        if (age == null || age <= 0) {
            showToast("Please enter a valid age")
            return
        }

        // Save to SharedPreferences
        userPrefs.edit()
            .putString("user_name", name)
            .putInt("user_age", age)
            .putString("user_email", email)
            .putLong("last_saved", System.currentTimeMillis())
            .apply()

        showToast("User data saved successfully!")
        updateSavedDataDisplay()
    }

    private fun loadUserData() {
        val name = userPrefs.getString("user_name", "")
        val age = userPrefs.getInt("user_age", 0)
        val email = userPrefs.getString("user_email", "")

        nameInput.setText(name)
        ageInput.setText(if (age > 0) age.toString() else "")
        emailInput.setText(email)

        showToast("User data loaded!")
        updateSavedDataDisplay()
    }

    private fun saveAppSettings() {
        appPrefs.edit()
            .putBoolean("dark_mode", darkModeSwitch.isChecked)
            .putBoolean("notifications", notificationsSwitch.isChecked)
            .putLong("settings_updated", System.currentTimeMillis())
            .apply()
    }

    private fun loadAppSettings() {
        val darkMode = appPrefs.getBoolean("dark_mode", false)
        val notifications = appPrefs.getBoolean("notifications", true)

        darkModeSwitch.isChecked = darkMode
        notificationsSwitch.isChecked = notifications
    }

    private fun toggleLoginStatus() {
        val isCurrentlyLoggedIn = sessionPrefs.getBoolean("is_logged_in", false)

        if (isCurrentlyLoggedIn) {
            // Logout
            sessionPrefs.edit()
                .putBoolean("is_logged_in", false)
                .remove("user_id")
                .putLong("logout_time", System.currentTimeMillis())
                .apply()

            loginButton.text = "Login"
            showToast("Logged out successfully")
        } else {
            // Login
            val userId = "user_${System.currentTimeMillis()}"
            sessionPrefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("user_id", userId)
                .putLong("login_time", System.currentTimeMillis())
                .apply()

            loginButton.text = "Logout"
            showToast("Logged in successfully")
        }

        updateSessionDisplay()
    }

    private fun updateSessionDisplay() {
        val isLoggedIn = sessionPrefs.getBoolean("is_logged_in", false)
        val loginTime = sessionPrefs.getLong("login_time", 0)
        val logoutTime = sessionPrefs.getLong("logout_time", 0)

        loginStatusText.text = "Login Status: ${if (isLoggedIn) "Logged in" else "Not logged in"}"

        val lastLoginText = when {
            isLoggedIn && loginTime > 0 -> "Last Login: ${dateFormat.format(Date(loginTime))}"
            !isLoggedIn && logoutTime > 0 -> "Last Logout: ${dateFormat.format(Date(logoutTime))}"
            else -> "Last Login: Never"
        }

        this.lastLoginText.text = lastLoginText
    }

    private fun incrementCounter() {
        val currentCount = userPrefs.getInt("counter", 0)
        val newCount = currentCount + 1

        userPrefs.edit()
            .putInt("counter", newCount)
            .apply()

        updateCounterDisplay()
        showToast("Counter incremented to $newCount")
    }

    private fun decrementCounter() {
        val currentCount = userPrefs.getInt("counter", 0)
        val newCount = maxOf(0, currentCount - 1)

        userPrefs.edit()
            .putInt("counter", newCount)
            .apply()

        updateCounterDisplay()
        showToast("Counter decremented to $newCount")
    }

    private fun updateCounterDisplay() {
        val count = userPrefs.getInt("counter", 0)
        counterText.text = "Counter: $count"
    }

    private fun updateSavedDataDisplay() {
        val name = userPrefs.getString("user_name", "")
        val age = userPrefs.getInt("user_age", 0)
        val email = userPrefs.getString("user_email", "")
        val lastSaved = userPrefs.getLong("last_saved", 0)

        val displayText = buildString {
            if (name.isNotEmpty()) {
                append("Name: $name\n")
            }
            if (age > 0) {
                append("Age: $age\n")
            }
            if (email.isNotEmpty()) {
                append("Email: $email\n")
            }
            if (lastSaved > 0) {
                append("Last Saved: ${dateFormat.format(Date(lastSaved))}")
            }
            if (isEmpty()) {
                append("No data saved yet")
            }
        }

        savedDataText.text = displayText
    }

    private fun loadAllSavedData() {
        loadUserData()
        loadAppSettings()
        updateSessionDisplay()
        updateSavedDataDisplay()
        updateCounterDisplay()
    }

    private fun showClearDataDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Clear All Data")
            .setMessage("Are you sure you want to clear all saved data? This action cannot be undone.")
            .setPositiveButton("Clear") { _, _ ->
                clearAllData()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun clearAllData() {
        // Clear all SharedPreferences
        userPrefs.edit().clear().apply()
        appPrefs.edit().clear().apply()
        sessionPrefs.edit().clear().apply()

        // Reset UI
        nameInput.text?.clear()
        ageInput.text?.clear()
        emailInput.text?.clear()
        darkModeSwitch.isChecked = false
        notificationsSwitch.isChecked = true
        loginButton.text = "Login"

        // Update displays
        updateSessionDisplay()
        updateSavedDataDisplay()
        updateCounterDisplay()

        showToast("All data cleared successfully!")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Reload data when returning to the app
        loadAllSavedData()
    }
}
