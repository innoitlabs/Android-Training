package com.example.devicefeatures

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class BiometricActivity : AppCompatActivity() {
    
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var executor: Executor
    
    private lateinit var btnAuthenticate: Button
    private lateinit var btnCheckBiometrics: Button
    private lateinit var tvStatus: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric)
        
        btnAuthenticate = findViewById(R.id.btnAuthenticate)
        btnCheckBiometrics = findViewById(R.id.btnCheckBiometrics)
        tvStatus = findViewById(R.id.tvStatus)
        
        setupBiometricAuthentication()
        setupButtons()
    }
    
    private fun setupBiometricAuthentication() {
        executor = ContextCompat.getMainExecutor(this)
        
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    updateStatus("Authentication error: $errString")
                    
                    when (errorCode) {
                        BiometricPrompt.ERROR_HW_NOT_PRESENT -> {
                            updateStatus("Biometric hardware not available")
                        }
                        BiometricPrompt.ERROR_HW_UNAVAILABLE -> {
                            updateStatus("Biometric hardware unavailable")
                        }
                        BiometricPrompt.ERROR_LOCKOUT -> {
                            updateStatus("Too many failed attempts. Try again later.")
                        }
                        BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                            updateStatus("No biometrics enrolled")
                        }
                        BiometricPrompt.ERROR_USER_CANCELED -> {
                            updateStatus("Authentication canceled by user")
                        }
                    }
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    updateStatus("Authentication successful!")
                    
                    // Handle successful authentication
                    handleSuccessfulAuthentication()
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    updateStatus("Authentication failed. Please try again.")
                }
            })
        
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
    }
    
    private fun setupButtons() {
        btnCheckBiometrics.setOnClickListener {
            checkBiometricAvailability()
        }
        
        btnAuthenticate.setOnClickListener {
            if (isBiometricAvailable()) {
                biometricPrompt.authenticate(promptInfo)
            } else {
                updateStatus("Biometric authentication not available")
            }
        }
    }
    
    private fun checkBiometricAvailability() {
        val biometricManager = BiometricManager.from(this)
        
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                updateStatus("Biometric authentication is available")
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                updateStatus("No biometric hardware available")
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                updateStatus("Biometric hardware is unavailable")
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                updateStatus("No biometrics enrolled")
            }
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                updateStatus("Security update required")
            }
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                updateStatus("Biometric authentication not supported")
            }
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                updateStatus("Biometric status unknown")
            }
        }
    }
    
    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
    }
    
    private fun handleSuccessfulAuthentication() {
        // Perform actions after successful authentication
        // For example, unlock the app, show sensitive data, etc.
        
        runOnUiThread {
            // Update UI to show authenticated state
            btnAuthenticate.text = "Authenticated"
            btnAuthenticate.isEnabled = false
            
            // Show success animation or navigate to main content
            showSuccessAnimation()
        }
    }
    
    private fun showSuccessAnimation() {
        // Example: Show a success animation or message
        val successView = findViewById<View>(R.id.successView)
        successView.visibility = View.VISIBLE
        
        // Hide after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            successView.visibility = View.GONE
        }, 2000)
    }
    
    private fun updateStatus(message: String) {
        runOnUiThread {
            tvStatus.text = message
            Log.d("Biometric", message)
        }
    }
}
