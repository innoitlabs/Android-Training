# Android File Storage Guide

## Overview
This guide covers how to work with file storage in Android apps, including internal storage, external storage, and scoped storage. You'll learn how to read, write, and manage files securely and efficiently.

## Android Storage Types

### 1. Internal Storage
- Private to your app
- Always available
- Automatically deleted when app is uninstalled
- No permissions required

### 2. External Storage
- Shared storage space
- May not be available on all devices
- Requires permissions (for older Android versions)
- Scoped storage applies to Android 10+

### 3. Scoped Storage (Android 10+)
- Apps can only access their own files
- Media files can be shared via MediaStore
- Documents can be accessed via Storage Access Framework

## Implementation

### 1. File Activity Implementation
```kotlin
class FileActivity : AppCompatActivity() {
    
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 100
        private const val PICK_FILE_REQUEST_CODE = 101
    }
    
    private lateinit var btnWriteFile: Button
    private lateinit var btnReadFile: Button
    private lateinit var btnListFiles: Button
    private lateinit var btnPickFile: Button
    private lateinit var tvFileContent: TextView
    private lateinit var etFileContent: EditText
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)
        
        btnWriteFile = findViewById(R.id.btnWriteFile)
        btnReadFile = findViewById(R.id.btnReadFile)
        btnListFiles = findViewById(R.id.btnListFiles)
        btnPickFile = findViewById(R.id.btnPickFile)
        tvFileContent = findViewById(R.id.tvFileContent)
        etFileContent = findViewById(R.id.etFileContent)
        
        setupButtons()
    }
    
    private fun setupButtons() {
        btnWriteFile.setOnClickListener {
            writeToInternalStorage()
        }
        
        btnReadFile.setOnClickListener {
            readFromInternalStorage()
        }
        
        btnListFiles.setOnClickListener {
            listInternalFiles()
        }
        
        btnPickFile.setOnClickListener {
            pickExternalFile()
        }
    }
    
    // Internal Storage Operations
    
    private fun writeToInternalStorage() {
        try {
            val fileName = "example.txt"
            val fileContent = etFileContent.text.toString()
            
            if (fileContent.isNotEmpty()) {
                val file = File(filesDir, fileName)
                file.writeText(fileContent)
                
                Toast.makeText(this, "File written successfully", Toast.LENGTH_SHORT).show()
                etFileContent.text.clear()
                
                // Update file list
                listInternalFiles()
            } else {
                Toast.makeText(this, "Please enter some content", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error writing file: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("FileActivity", "Error writing file", e)
        }
    }
    
    private fun readFromInternalStorage() {
        try {
            val fileName = "example.txt"
            val file = File(filesDir, fileName)
            
            if (file.exists()) {
                val content = file.readText()
                tvFileContent.text = content
                Toast.makeText(this, "File read successfully", Toast.LENGTH_SHORT).show()
            } else {
                tvFileContent.text = "File does not exist"
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("FileActivity", "Error reading file", e)
        }
    }
    
    private fun listInternalFiles() {
        try {
            val files = filesDir.listFiles()
            val fileList = files?.joinToString("\n") { file ->
                "${file.name} (${file.length()} bytes)"
            } ?: "No files found"
            
            tvFileContent.text = "Internal Files:\n$fileList"
        } catch (e: Exception) {
            Toast.makeText(this, "Error listing files: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("FileActivity", "Error listing files", e)
        }
    }
    
    // External Storage Operations
    
    private fun writeToExternalStorage() {
        if (checkStoragePermission()) {
            try {
                val fileName = "external_example.txt"
                val fileContent = etFileContent.text.toString()
                
                if (fileContent.isNotEmpty()) {
                    val file = File(getExternalFilesDir(null), fileName)
                    file.writeText(fileContent)
                    
                    Toast.makeText(this, "External file written successfully", Toast.LENGTH_SHORT).show()
                    etFileContent.text.clear()
                } else {
                    Toast.makeText(this, "Please enter some content", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error writing external file: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("FileActivity", "Error writing external file", e)
            }
        } else {
            requestStoragePermission()
        }
    }
    
    private fun readFromExternalStorage() {
        if (checkStoragePermission()) {
            try {
                val fileName = "external_example.txt"
                val file = File(getExternalFilesDir(null), fileName)
                
                if (file.exists()) {
                    val content = file.readText()
                    tvFileContent.text = content
                    Toast.makeText(this, "External file read successfully", Toast.LENGTH_SHORT).show()
                } else {
                    tvFileContent.text = "External file does not exist"
                    Toast.makeText(this, "External file not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error reading external file: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("FileActivity", "Error reading external file", e)
            }
        } else {
            requestStoragePermission()
        }
    }
    
    private fun pickExternalFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }
    
    // Permission Handling
    
    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ uses scoped storage, no permission needed for app-specific files
            true
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
    
    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && 
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Storage permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                readExternalFile(uri)
            }
        }
    }
    
    private fun readExternalFile(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val content = inputStream.bufferedReader().readText()
                tvFileContent.text = content
                Toast.makeText(this, "External file read successfully", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading external file: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("FileActivity", "Error reading external file", e)
        }
    }
}
```

## Advanced File Operations

### 1. File Encryption
```kotlin
class SecureFileActivity : AppCompatActivity() {
    
    private lateinit var keyStore: KeyStore
    private lateinit var cipher: Cipher
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secure_file)
        
        setupEncryption()
    }
    
    private fun setupEncryption() {
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
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(false)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
        
        cipher = Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/" +
            "${KeyProperties.BLOCK_MODE_CBC}/" +
            "${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        )
    }
    
    private fun writeEncryptedFile(content: String, fileName: String) {
        try {
            val key = keyStore.getKey("file_encryption_key", null)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            
            val encryptedData = cipher.doFinal(content.toByteArray())
            val iv = cipher.iv
            
            val file = File(filesDir, fileName)
            val outputStream = FileOutputStream(file)
            
            // Write IV first
            outputStream.write(iv)
            // Write encrypted data
            outputStream.write(encryptedData)
            outputStream.close()
            
            Toast.makeText(this, "Encrypted file written", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error writing encrypted file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun readEncryptedFile(fileName: String): String? {
        try {
            val file = File(filesDir, fileName)
            val inputStream = FileInputStream(file)
            
            // Read IV
            val iv = ByteArray(16)
            inputStream.read(iv)
            
            // Read encrypted data
            val encryptedData = inputStream.readBytes()
            inputStream.close()
            
            val key = keyStore.getKey("file_encryption_key", null)
            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
            
            val decryptedData = cipher.doFinal(encryptedData)
            return String(decryptedData)
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading encrypted file: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}
```

### 2. File Compression
```kotlin
class CompressedFileActivity : AppCompatActivity() {
    
    private fun writeCompressedFile(content: String, fileName: String) {
        try {
            val file = File(filesDir, "$fileName.gz")
            val gzipOutputStream = GZIPOutputStream(FileOutputStream(file))
            
            gzipOutputStream.write(content.toByteArray())
            gzipOutputStream.close()
            
            Toast.makeText(this, "Compressed file written", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error writing compressed file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun readCompressedFile(fileName: String): String? {
        try {
            val file = File(filesDir, "$fileName.gz")
            val gzipInputStream = GZIPInputStream(FileInputStream(file))
            
            val content = gzipInputStream.bufferedReader().readText()
            gzipInputStream.close()
            
            return content
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading compressed file: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }
    }
}
```

### 3. File Sharing
```kotlin
class FileSharingActivity : AppCompatActivity() {
    
    private fun shareFile(fileName: String) {
        try {
            val file = File(filesDir, fileName)
            
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(
                    this,
                    "${packageName}.fileprovider",
                    file
                )
                
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    putExtra(Intent.EXTRA_SUBJECT, "Shared file: $fileName")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                
                startActivity(Intent.createChooser(shareIntent, "Share file"))
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error sharing file: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
```

## Layout File
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="File Storage Demo"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp" />

    <EditText
        android:id="@+id/etFileContent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter file content"
        android:inputType="textMultiLine"
        android:minLines="3"
        android:layout_marginBottom="16dp" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginBottom="16dp">

        <Button
            android:id="@+id/btnWriteFile"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginEnd="8dp"
            android:text="Write File" />

        <Button
            android:id="@+id/btnReadFile"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="8dp"
            android:text="Read File" />

    </LinearLayout>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginBottom="16dp">

        <Button
            android:id="@+id/btnListFiles"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginEnd="8dp"
            android:text="List Files" />

        <Button
            android:id="@+id/btnPickFile"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="8dp"
            android:text="Pick File" />

    </LinearLayout>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="File Content:"
        android:textStyle="bold"
        android:layout_marginBottom="8dp" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <TextView
            android:id="@+id/tvFileContent"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="No content"
            android:background="@android:color/white"
            android:padding="8dp"
            android:textSize="14sp" />

    </ScrollView>

</LinearLayout>
```

## File Provider Configuration

### 1. File Paths XML
```xml
<!-- res/xml/file_paths.xml -->
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <files-path name="my_files" path="." />
    <external-files-path name="my_external_files" path="." />
    <cache-path name="my_cache" path="." />
</paths>
```

### 2. AndroidManifest.xml
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

## Best Practices

### 1. Storage Location
- Use internal storage for private app data
- Use external storage for shared media files
- Follow scoped storage guidelines for Android 10+

### 2. Security
- Encrypt sensitive files
- Use FileProvider for sharing files
- Validate file content before processing

### 3. Performance
- Use appropriate I/O operations
- Handle large files efficiently
- Implement proper error handling

### 4. User Experience
- Provide clear feedback on file operations
- Handle storage space issues
- Support file sharing when appropriate

## Testing

### Unit Testing
```kotlin
@Test
fun testFileWriteRead() {
    val activity = ActivityScenario.launch(FileActivity::class.java)
    
    activity.onActivity { activity ->
        // Test file write and read operations
        val testContent = "Test content"
        val testFile = File(activity.filesDir, "test.txt")
        
        testFile.writeText(testContent)
        assertTrue(testFile.exists())
        
        val readContent = testFile.readText()
        assertEquals(testContent, readContent)
        
        testFile.delete()
    }
}
```

### UI Testing
```kotlin
@Test
fun testFileOperations() {
    val scenario = ActivityScenario.launch(FileActivity::class.java)
    
    scenario.onActivity { activity ->
        // Enter file content
        onView(withId(R.id.etFileContent))
            .perform(typeText("Test file content"), closeSoftKeyboard())
        
        // Click write button
        onView(withId(R.id.btnWriteFile))
            .perform(click())
        
        // Click read button
        onView(withId(R.id.btnReadFile))
            .perform(click())
        
        // Verify content is displayed
        onView(withId(R.id.tvFileContent))
            .check(matches(withText(containsString("Test file content"))))
    }
}
```

## Troubleshooting

### Common Issues:
1. **Permission denied**: Check storage permissions for older Android versions
2. **File not found**: Verify file paths and existence
3. **Scoped storage**: Use appropriate APIs for Android 10+
4. **File sharing**: Configure FileProvider properly

### Debug Tips:
- Log file operations for debugging
- Check file permissions and paths
- Test on different Android versions
- Verify storage space availability

## Summary
Android file storage provides flexible options for managing app data. Use internal storage for private data, external storage for shared content, and follow scoped storage guidelines for modern Android versions. Implement proper security measures and error handling for robust file operations.
