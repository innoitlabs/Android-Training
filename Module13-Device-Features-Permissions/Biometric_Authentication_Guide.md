# Android Biometric Authentication Guide

## Overview
This guide covers how to implement biometric authentication in Android apps using the Biometric API. You'll learn how to authenticate users using fingerprint, face recognition, and other biometric methods.

## Biometric Authentication Components

### 1. BiometricManager
- Checks biometric hardware availability
- Verifies biometric enrollment status

### 2. BiometricPrompt
- Handles biometric authentication UI
- Manages authentication callbacks

### 3. BiometricPrompt.PromptInfo
- Configures authentication prompt
- Sets title, subtitle, and button text

## Implementation

### 1. Add Dependencies
```kotlin
// app/build.gradle.kts
dependencies {
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
}
```

### 2. Add Permissions
```xml
<uses-permission android:name="android.permission.USE_BIOMETRIC" />
<uses-permission android:name="android.permission.USE_FINGERPRINT" />
```

### 3. Biometric Activity Implementation
```kotlin
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
```

## Advanced Biometric Features

### 1. Crypto-based Authentication
```kotlin
class CryptoBiometricActivity : AppCompatActivity() {
    
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var cryptoObject: BiometricPrompt.CryptoObject
    private lateinit var keyStore: KeyStore
    private lateinit var cipher: Cipher
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_biometric)
        
        setupCryptoBiometric()
    }
    
    private fun setupCryptoBiometric() {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, 
            "AndroidKeyStore"
        )
        
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            "biometric_key",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
        
        cipher = Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/" +
            "${KeyProperties.BLOCK_MODE_CBC}/" +
            "${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        )
        
        val key = keyStore.getKey("biometric_key", null)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        
        cryptoObject = BiometricPrompt.CryptoObject(cipher)
        
        biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    
                    // Use the crypto object for encryption/decryption
                    val cryptoObject = result.cryptoObject
                    cryptoObject?.cipher?.let { cipher ->
                        // Perform encryption or decryption
                        performCryptoOperation(cipher)
                    }
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle error
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle failure
                }
            })
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Crypto Authentication")
            .setSubtitle("Authenticate to encrypt/decrypt data")
            .setNegativeButtonText("Cancel")
            .build()
        
        biometricPrompt.authenticate(promptInfo, cryptoObject)
    }
    
    private fun performCryptoOperation(cipher: Cipher) {
        // Example: Encrypt sensitive data
        val sensitiveData = "This is sensitive information"
        val encryptedData = cipher.doFinal(sensitiveData.toByteArray())
        
        // Store encrypted data securely
        saveEncryptedData(encryptedData)
    }
    
    private fun saveEncryptedData(encryptedData: ByteArray) {
        // Save encrypted data to secure storage
        val sharedPrefs = getSharedPreferences("secure_data", Context.MODE_PRIVATE)
        val encodedData = Base64.encodeToString(encryptedData, Base64.DEFAULT)
        sharedPrefs.edit().putString("encrypted_data", encodedData).apply()
    }
}
```

### 2. Biometric with Device Credentials
```kotlin
private fun setupDeviceCredentialAuthentication() {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Device Authentication")
        .setSubtitle("Log in using your device credentials")
        .setAllowedAuthenticators(
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
        .build()
    
    biometricPrompt.authenticate(promptInfo)
}
```

### 3. Custom Biometric UI
```kotlin
private fun setupCustomBiometricUI() {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Custom Authentication")
        .setSubtitle("Use your fingerprint or face")
        .setDescription("This app requires biometric authentication")
        .setConfirmationRequired(true)
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        .build()
    
    biometricPrompt.authenticate(promptInfo)
}
```

## Layout File
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:gravity="center">

    <ImageView
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:src="@drawable/ic_fingerprint"
        android:layout_marginBottom="24dp"
        android:contentDescription="Fingerprint Icon" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Biometric Authentication"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Use your fingerprint or face to authenticate"
        android:textSize="16sp"
        android:gravity="center"
        android:layout_marginBottom="32dp" />

    <Button
        android:id="@+id/btnCheckBiometrics"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Check Biometric Availability"
        android:layout_marginBottom="16dp" />

    <Button
        android:id="@+id/btnAuthenticate"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Authenticate"
        android:layout_marginBottom="24dp" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Status: Ready"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp" />

    <TextView
        android:id="@+id/tvStatus"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="No authentication attempted"
        android:background="@android:color/white"
        android:padding="12dp"
        android:gravity="center"
        android:textSize="14sp" />

    <View
        android:id="@+id/successView"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="@drawable/success_background"
        android:visibility="gone"
        android:layout_marginTop="16dp" />

</LinearLayout>
```

## Best Practices

### 1. Security Considerations
- Use crypto-based authentication for sensitive data
- Implement proper error handling
- Don't store biometric data
- Use appropriate authentication levels

### 2. User Experience
- Provide clear feedback on authentication status
- Handle all authentication scenarios gracefully
- Offer alternative authentication methods
- Respect user privacy preferences

### 3. Fallback Mechanisms
- Provide PIN/password as backup
- Handle hardware unavailability
- Support multiple authentication methods

### 4. Error Handling
- Handle all biometric error codes
- Provide meaningful error messages
- Implement retry mechanisms
- Handle device-specific issues

## Testing

### Unit Testing
```kotlin
@Test
fun testBiometricAvailability() {
    val activity = ActivityScenario.launch(BiometricActivity::class.java)
    
    activity.onActivity { activity ->
        // Test biometric availability check
        assertTrue(activity.isBiometricAvailable())
    }
}
```

### UI Testing
```kotlin
@Test
fun testBiometricAuthentication() {
    val scenario = ActivityScenario.launch(BiometricActivity::class.java)
    
    scenario.onActivity { activity ->
        // Click authenticate button
        onView(withId(R.id.btnAuthenticate))
            .perform(click())
        
        // Verify authentication prompt appears
        // Note: Actual biometric testing requires device/emulator with biometrics
    }
}
```

## Troubleshooting

### Common Issues:
1. **Biometric not available**: Check hardware availability and enrollment
2. **Authentication errors**: Handle specific error codes properly
3. **Crypto operations**: Ensure proper key management
4. **Device compatibility**: Test on different devices

### Debug Tips:
- Check biometric manager status
- Log authentication callbacks
- Test on devices with/without biometrics
- Verify key store operations

## Summary
Biometric authentication provides secure and convenient user authentication. Use the Biometric API for consistent behavior across devices, implement proper error handling, and consider crypto-based authentication for sensitive data. Always provide fallback mechanisms and respect user privacy.
