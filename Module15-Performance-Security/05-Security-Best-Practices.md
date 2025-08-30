# Security Best Practices & Data Encryption

## Overview
Security is crucial for Android applications, especially when handling sensitive user data. This guide covers essential security practices including data encryption, secure storage, and protection against common vulnerabilities.

## Why Security Matters
- **User Privacy**: Protect sensitive user information
- **Data Integrity**: Ensure data hasn't been tampered with
- **Compliance**: Meet regulatory requirements (GDPR, HIPAA, etc.)
- **Trust**: Build user confidence in your app
- **Prevention**: Avoid data breaches and security incidents

## Data Encryption Fundamentals

### 1. Symmetric vs Asymmetric Encryption
```kotlin
// Symmetric encryption (same key for encrypt/decrypt)
class SymmetricEncryption {
    private val algorithm = "AES"
    private val keySize = 256
    
    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(algorithm)
        keyGenerator.init(keySize)
        return keyGenerator.generateKey()
    }
    
    fun encrypt(data: String, key: SecretKey): String {
        val cipher = Cipher.getInstance("$algorithm/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }
    
    fun decrypt(encryptedData: String, key: SecretKey): String {
        val cipher = Cipher.getInstance("$algorithm/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}

// Asymmetric encryption (public/private key pair)
class AsymmetricEncryption {
    fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }
    
    fun encrypt(data: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }
    
    fun decrypt(encryptedData: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}
```

## Secure Storage Solutions

### 1. EncryptedSharedPreferences
```kotlin
// Using EncryptedSharedPreferences for secure storage
class SecureStorageManager(private val context: Context) {
    private lateinit var encryptedPrefs: SharedPreferences
    
    init {
        initializeSecureStorage()
    }
    
    private fun initializeSecureStorage() {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    fun storeSecureData(key: String, value: String) {
        encryptedPrefs.edit().putString(key, value).apply()
    }
    
    fun getSecureData(key: String): String? {
        return encryptedPrefs.getString(key, null)
    }
    
    fun removeSecureData(key: String) {
        encryptedPrefs.edit().remove(key).apply()
    }
    
    fun clearAllData() {
        encryptedPrefs.edit().clear().apply()
    }
}
```

### 2. Encrypted Database with Room
```kotlin
// Encrypted Room database
@Database(entities = [User::class], version = 1)
abstract class EncryptedDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: EncryptedDatabase? = null
        
        fun getDatabase(context: Context): EncryptedDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EncryptedDatabase::class.java,
                    "encrypted_database"
                )
                .openHelperFactory(SupportFactory(SQLiteDatabase.openOrCreateDatabase(
                    "encrypted_database",
                    "password".toByteArray(),
                    null
                )))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val encryptedData: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
    
    @Insert
    suspend fun insertUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
}
```

## Network Security

### 1. Certificate Pinning
```kotlin
// Certificate pinning to prevent man-in-the-middle attacks
class CertificatePinning {
    fun createSecureOkHttpClient(): OkHttpClient {
        val certificatePinner = CertificatePinner.Builder()
            .add("api.example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
            .build()
        
        return OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .build()
    }
}
```

### 2. TLS Configuration
```kotlin
// Secure TLS configuration
class SecureNetworkConfig {
    fun createSecureClient(): OkHttpClient {
        val trustManagerFactory = TrustManagerFactory.getInstance(
            TrustManagerFactory.getDefaultAlgorithm()
        )
        trustManagerFactory.init(null as KeyStore?)
        
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, null)
        
        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
            .build()
    }
}
```

## Input Validation and Sanitization

### 1. Input Validation
```kotlin
// Input validation to prevent injection attacks
class InputValidator {
    fun validateEmail(email: String): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS
        return emailPattern.matcher(email).matches()
    }
    
    fun validatePassword(password: String): Boolean {
        // At least 8 characters, 1 uppercase, 1 lowercase, 1 number
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }
    
    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
    }
}
```

### 2. SQL Injection Prevention
```kotlin
// Using parameterized queries to prevent SQL injection
class SecureDatabaseHelper {
    fun getUserById(userId: String): User? {
        val db = database.readableDatabase
        val cursor = db.query(
            "users",
            arrayOf("id", "name", "email"),
            "id = ?", // Use parameterized query
            arrayOf(userId), // Pass parameters separately
            null,
            null,
            null
        )
        
        return if (cursor.moveToFirst()) {
            User(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2)
            )
        } else null
    }
}
```

## Biometric Authentication

### 1. Biometric Authentication Implementation
```kotlin
// Biometric authentication using BiometricPrompt
class BiometricAuthManager(private val context: Context) {
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    
    fun setupBiometricAuth(activity: FragmentActivity, onSuccess: () -> Unit) {
        biometricPrompt = BiometricPrompt(activity, ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Handle authentication failure
                }
            })
        
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
    }
    
    fun authenticate() {
        biometricPrompt.authenticate(promptInfo)
    }
}
```

## Secure Communication

### 1. HTTPS Implementation
```kotlin
// Force HTTPS connections
class SecureNetworkManager {
    fun createSecureClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("User-Agent", "MyApp/1.0")
                    .method(original.method, original.body)
                
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }
    
    fun makeSecureRequest(url: String, callback: (String) -> Unit) {
        // Ensure URL uses HTTPS
        val secureUrl = if (url.startsWith("http://")) {
            url.replace("http://", "https://")
        } else {
            url
        }
        
        val client = createSecureClient()
        val request = Request.Builder().url(secureUrl).build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody ?: "")
            }
            
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }
        })
    }
}
```

## Security Best Practices

### 1. Code Obfuscation
```kotlin
// ProGuard rules for code obfuscation
// In proguard-rules.pro:
/*
-keep class com.example.app.model.** { *; }
-keepclassmembers class com.example.app.model.** {
    *;
}
-keepattributes Signature
-keepattributes *Annotation*
*/
```

### 2. Secure Random Number Generation
```kotlin
// Use SecureRandom for cryptographic operations
class SecureRandomGenerator {
    private val secureRandom = SecureRandom()
    
    fun generateSecureToken(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
    
    fun generateSecureKey(): String {
        val keyBytes = ByteArray(256 / 8) // 256-bit key
        secureRandom.nextBytes(keyBytes)
        return Base64.encodeToString(keyBytes, Base64.NO_WRAP)
    }
}
```

### 3. Secure Logging
```kotlin
// Avoid logging sensitive information
class SecureLogger {
    fun logUserAction(action: String, userId: String) {
        // ✅ Good: Log action without sensitive data
        Log.d("UserAction", "User performed: $action")
        
        // ❌ Bad: Log sensitive information
        // Log.d("UserAction", "User $userId performed: $action with data: $sensitiveData")
    }
    
    fun logError(error: Throwable, context: String) {
        // Log error without sensitive context
        Log.e("AppError", "Error in $context: ${error.message}")
    }
}
```

## Security Testing

### 1. Vulnerability Scanning
```kotlin
// Basic security checks
class SecurityChecker {
    fun checkAppSecurity(context: Context): SecurityReport {
        val report = SecurityReport()
        
        // Check if app is debuggable
        report.isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        
        // Check if app is installed from Play Store
        report.isFromPlayStore = isInstalledFromPlayStore(context)
        
        // Check for root access
        report.isRooted = isDeviceRooted()
        
        return report
    }
    
    private fun isInstalledFromPlayStore(context: Context): Boolean {
        val installer = context.packageManager.getInstallerPackageName(context.packageName)
        return installer == "com.android.vending"
    }
    
    private fun isDeviceRooted(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }
}

data class SecurityReport(
    var isDebuggable: Boolean = false,
    var isFromPlayStore: Boolean = false,
    var isRooted: Boolean = false
)
```

## Summary
- **Use EncryptedSharedPreferences** for secure data storage
- **Implement certificate pinning** to prevent MITM attacks
- **Validate and sanitize all inputs** to prevent injection attacks
- **Use biometric authentication** for enhanced security
- **Force HTTPS connections** for all network communication
- **Implement proper code obfuscation** with ProGuard
- **Use SecureRandom** for cryptographic operations
- **Avoid logging sensitive information**
- **Regularly test for security vulnerabilities**

## Next Steps
- Implement EncryptedSharedPreferences in your app
- Set up certificate pinning for network requests
- Add input validation to your forms
- Move to the next section: Secure Storage
