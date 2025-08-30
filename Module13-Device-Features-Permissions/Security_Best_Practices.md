# Android Security Best Practices Guide

## Overview
This guide covers essential security best practices for Android app development. You'll learn how to protect user data, implement secure authentication, handle sensitive information, and follow security guidelines throughout the development lifecycle.

## Core Security Principles

### 1. Defense in Depth
- Implement multiple layers of security
- Don't rely on a single security measure
- Use complementary security controls

### 2. Principle of Least Privilege
- Request minimum necessary permissions
- Grant minimal access to resources
- Use scoped permissions when possible

### 3. Secure by Default
- Implement secure configurations by default
- Require explicit opt-in for less secure options
- Validate all inputs and outputs

## Permission Security

### 1. Runtime Permissions
```kotlin
class SecurePermissionActivity : AppCompatActivity() {
    
    private fun requestPermissionsSecurely() {
        // Only request permissions when needed
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Show explanation before requesting
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showPermissionExplanation()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        }
    }
    
    private fun showPermissionExplanation() {
        AlertDialog.Builder(this)
            .setTitle("Camera Permission Required")
            .setMessage("This app needs camera access to take photos for your profile. " +
                       "We only access the camera when you explicitly choose to take a photo.")
            .setPositiveButton("Grant") { _, _ ->
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
```

### 2. Permission Validation
```kotlin
private fun validatePermissions() {
    val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    
    val missingPermissions = requiredPermissions.filter {
        checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
    }
    
    if (missingPermissions.isNotEmpty()) {
        // Handle missing permissions gracefully
        showPermissionRequiredDialog(missingPermissions)
    }
}
```

## Data Security

### 1. Secure Data Storage
```kotlin
class SecureStorageActivity : AppCompatActivity() {
    
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var keyStore: KeyStore
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSecureStorage()
    }
    
    private fun setupSecureStorage() {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        
        // Use EncryptedSharedPreferences for sensitive data
        encryptedSharedPrefs = EncryptedSharedPreferences.create(
            "secure_prefs",
            MasterKey.Builder(this).build(),
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    private fun storeSecureData(key: String, value: String) {
        encryptedSharedPrefs.edit().putString(key, value).apply()
    }
    
    private fun getSecureData(key: String): String? {
        return encryptedSharedPrefs.getString(key, null)
    }
}
```

### 2. File Encryption
```kotlin
class SecureFileManager {
    
    private lateinit var cipher: Cipher
    private lateinit var keyStore: KeyStore
    
    fun setupEncryption() {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, 
            "AndroidKeyStore"
        )
        
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            "file_encryption_key",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
        
        cipher = Cipher.getInstance("AES/GCM/NoPadding")
    }
    
    fun encryptFile(inputFile: File, outputFile: File) {
        val key = keyStore.getKey("file_encryption_key", null)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        
        val inputStream = FileInputStream(inputFile)
        val outputStream = FileOutputStream(outputFile)
        
        // Write IV
        outputStream.write(cipher.iv)
        
        // Encrypt data
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            val encryptedChunk = cipher.update(buffer, 0, bytesRead)
            outputStream.write(encryptedChunk)
        }
        
        val finalChunk = cipher.doFinal()
        outputStream.write(finalChunk)
        
        inputStream.close()
        outputStream.close()
    }
}
```

### 3. Network Security
```kotlin
class SecureNetworkManager {
    
    fun setupNetworkSecurity() {
        // Use HTTPS for all network communications
        val client = OkHttpClient.Builder()
            .certificatePinner(
                CertificatePinner.Builder()
                    .add("api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                    .build()
            )
            .build()
    }
    
    fun validateServerCertificate(hostname: String, certificate: X509Certificate): Boolean {
        try {
            certificate.checkValidity()
            certificate.verify(certificate.publicKey)
            return true
        } catch (e: Exception) {
            Log.e("Security", "Certificate validation failed", e)
            return false
        }
    }
}
```

## Authentication Security

### 1. Biometric Authentication
```kotlin
class SecureBiometricActivity : AppCompatActivity() {
    
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var cryptoObject: BiometricPrompt.CryptoObject
    
    private fun setupSecureBiometric() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Secure Authentication")
            .setSubtitle("Use your biometric credential")
            .setConfirmationRequired(true)
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
        
        biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // Handle successful authentication securely
                    handleSecureAuthentication()
                }
                
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Log security events
                    Log.w("Security", "Biometric authentication error: $errorCode - $errString")
                }
            })
    }
    
    private fun handleSecureAuthentication() {
        // Perform secure operations after authentication
        // Access encrypted data, perform sensitive operations
    }
}
```

### 2. Token Management
```kotlin
class SecureTokenManager {
    
    private lateinit var encryptedSharedPrefs: SharedPreferences
    
    fun storeAuthToken(token: String) {
        // Store tokens securely using EncryptedSharedPreferences
        encryptedSharedPrefs.edit()
            .putString("auth_token", token)
            .putLong("token_expiry", System.currentTimeMillis() + TOKEN_VALIDITY_DURATION)
            .apply()
    }
    
    fun getValidAuthToken(): String? {
        val token = encryptedSharedPrefs.getString("auth_token", null)
        val expiry = encryptedSharedPrefs.getLong("token_expiry", 0)
        
        return if (token != null && System.currentTimeMillis() < expiry) {
            token
        } else {
            // Token expired or invalid
            clearAuthToken()
            null
        }
    }
    
    fun clearAuthToken() {
        encryptedSharedPrefs.edit().remove("auth_token").remove("token_expiry").apply()
    }
    
    companion object {
        private const val TOKEN_VALIDITY_DURATION = 24 * 60 * 60 * 1000L // 24 hours
    }
}
```

## Input Validation and Sanitization

### 1. Input Validation
```kotlin
class InputValidator {
    
    fun validateEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
        return emailPattern.matcher(email).matches()
    }
    
    fun validatePassword(password: String): PasswordValidationResult {
        val result = PasswordValidationResult()
        
        if (password.length < 8) {
            result.addError("Password must be at least 8 characters long")
        }
        
        if (!password.matches(Regex(".*[A-Z].*"))) {
            result.addError("Password must contain at least one uppercase letter")
        }
        
        if (!password.matches(Regex(".*[a-z].*"))) {
            result.addError("Password must contain at least one lowercase letter")
        }
        
        if (!password.matches(Regex(".*\\d.*"))) {
            result.addError("Password must contain at least one digit")
        }
        
        if (!password.matches(Regex(".*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"))) {
            result.addError("Password must contain at least one special character")
        }
        
        return result
    }
    
    fun sanitizeInput(input: String): String {
        // Remove potentially dangerous characters
        return input.replace(Regex("[<>\"']"), "")
    }
}

data class PasswordValidationResult(
    val isValid: Boolean = true,
    val errors: MutableList<String> = mutableListOf()
) {
    fun addError(error: String) {
        errors.add(error)
    }
}
```

### 2. SQL Injection Prevention
```kotlin
class SecureDatabaseManager {
    
    private lateinit var database: SQLiteDatabase
    
    fun insertUserSecurely(name: String, email: String) {
        // Use parameterized queries to prevent SQL injection
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
        }
        
        database.insert("users", null, values)
    }
    
    fun getUserSecurely(userId: String): Cursor? {
        // Use parameterized queries
        return database.query(
            "users",
            null,
            "id = ?",
            arrayOf(userId),
            null,
            null,
            null
        )
    }
}
```

## Code Security

### 1. ProGuard/R8 Configuration
```proguard
# Keep sensitive classes
-keep class com.example.app.security.** { *; }

# Obfuscate but keep functionality
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}
```

### 2. Certificate Pinning
```kotlin
class CertificatePinningManager {
    
    fun setupCertificatePinning() {
        val certificatePinner = CertificatePinner.Builder()
            .add("api.example.com", 
                "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .add("api.example.com", 
                "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
            .build()
        
        val client = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()
    }
}
```

## Security Testing

### 1. Security Unit Tests
```kotlin
class SecurityTest {
    
    @Test
    fun testPasswordValidation() {
        val validator = InputValidator()
        
        // Test weak password
        val weakPassword = "123"
        val weakResult = validator.validatePassword(weakPassword)
        assertFalse(weakResult.isValid)
        assertTrue(weakResult.errors.isNotEmpty())
        
        // Test strong password
        val strongPassword = "SecurePass123!"
        val strongResult = validator.validatePassword(strongPassword)
        assertTrue(strongResult.isValid)
        assertTrue(strongResult.errors.isEmpty())
    }
    
    @Test
    fun testInputSanitization() {
        val validator = InputValidator()
        
        val maliciousInput = "<script>alert('xss')</script>"
        val sanitized = validator.sanitizeInput(maliciousInput)
        
        assertFalse(sanitized.contains("<"))
        assertFalse(sanitized.contains(">"))
        assertFalse(sanitized.contains("\""))
    }
}
```

### 2. Security Audit Checklist
```kotlin
class SecurityAuditor {
    
    fun performSecurityAudit(): SecurityAuditResult {
        val result = SecurityAuditResult()
        
        // Check for hardcoded secrets
        checkForHardcodedSecrets(result)
        
        // Verify permission usage
        checkPermissionUsage(result)
        
        // Validate network security
        checkNetworkSecurity(result)
        
        // Review data storage
        checkDataStorage(result)
        
        return result
    }
    
    private fun checkForHardcodedSecrets(result: SecurityAuditResult) {
        // Scan for hardcoded API keys, passwords, etc.
        val sourceFiles = getSourceFiles()
        for (file in sourceFiles) {
            if (file.contains("password") || file.contains("api_key")) {
                result.addWarning("Potential hardcoded secret found in ${file.name}")
            }
        }
    }
}

data class SecurityAuditResult(
    val warnings: MutableList<String> = mutableListOf(),
    val errors: MutableList<String> = mutableListOf()
) {
    fun addWarning(warning: String) {
        warnings.add(warning)
    }
    
    fun addError(error: String) {
        errors.add(error)
    }
}
```

## Best Practices Summary

### 1. Development Phase
- Use secure coding practices
- Implement input validation
- Follow the principle of least privilege
- Use secure libraries and frameworks

### 2. Testing Phase
- Perform security testing
- Use automated security tools
- Conduct penetration testing
- Review code for security vulnerabilities

### 3. Deployment Phase
- Use code obfuscation
- Implement certificate pinning
- Enable network security configuration
- Use secure app signing

### 4. Maintenance Phase
- Keep dependencies updated
- Monitor for security vulnerabilities
- Implement security patches
- Conduct regular security audits

## Common Security Mistakes to Avoid

### 1. Hardcoding Secrets
```kotlin
// ❌ Wrong - Hardcoded API key
private const val API_KEY = "sk-1234567890abcdef"

// ✅ Correct - Use BuildConfig or encrypted storage
private val apiKey = BuildConfig.API_KEY
```

### 2. Insecure Data Storage
```kotlin
// ❌ Wrong - Store sensitive data in plain text
sharedPrefs.edit().putString("password", password).apply()

// ✅ Correct - Use EncryptedSharedPreferences
encryptedPrefs.edit().putString("password", password).apply()
```

### 3. Weak Authentication
```kotlin
// ❌ Wrong - Simple password validation
fun isValidPassword(password: String): Boolean {
    return password.length >= 6
}

// ✅ Correct - Strong password validation
fun validatePassword(password: String): PasswordValidationResult {
    // Implement comprehensive validation
}
```

### 4. Insecure Network Communication
```kotlin
// ❌ Wrong - HTTP communication
val url = "http://api.example.com/data"

// ✅ Correct - HTTPS communication
val url = "https://api.example.com/data"
```

## Security Tools and Resources

### 1. Static Analysis Tools
- Android Studio's built-in security analysis
- SonarQube for code quality and security
- MobSF for mobile app security testing

### 2. Dynamic Analysis Tools
- OWASP ZAP for penetration testing
- Burp Suite for web security testing
- Frida for runtime manipulation

### 3. Security Libraries
- EncryptedSharedPreferences for secure storage
- Biometric API for secure authentication
- Network Security Config for network security

## Summary
Security in Android development requires a comprehensive approach covering all aspects of the application lifecycle. Follow these best practices to protect user data, implement secure authentication, and maintain the integrity of your applications. Regular security audits and staying updated with the latest security guidelines are essential for maintaining secure Android applications.
