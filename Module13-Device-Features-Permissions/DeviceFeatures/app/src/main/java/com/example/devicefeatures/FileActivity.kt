package com.example.devicefeatures

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileActivity : AppCompatActivity() {
    
    companion object {
        private const val STORAGE_PERMISSION_REQUEST_CODE = 100
        private const val PICK_FILE_REQUEST_CODE = 101
        private const val CAMERA_REQUEST_CODE = 102
    }
    
    private lateinit var btnWriteFile: Button
    private lateinit var btnReadFile: Button
    private lateinit var btnListFiles: Button
    private lateinit var btnPickFile: Button
    private lateinit var btnTakePhoto: Button
    private lateinit var tvFileContent: TextView
    private lateinit var etFileContent: EditText
    
    private var photoUri: Uri? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)
        
        btnWriteFile = findViewById(R.id.btnWriteFile)
        btnReadFile = findViewById(R.id.btnReadFile)
        btnListFiles = findViewById(R.id.btnListFiles)
        btnPickFile = findViewById(R.id.btnPickFile)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
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
        
        btnTakePhoto.setOnClickListener {
            if (checkCameraPermission()) {
                takePhoto()
            } else {
                requestCameraPermission()
            }
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
    
    private fun takePhoto() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }
        
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
    
    // Permission Handling
    
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            STORAGE_PERMISSION_REQUEST_CODE
        )
    }
    
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
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
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && 
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
                    takePhoto()
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        when (requestCode) {
            PICK_FILE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    data?.data?.let { uri ->
                        readExternalFile(uri)
                    }
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Photo captured successfully", Toast.LENGTH_SHORT).show()
                    // Handle the captured photo
                    photoUri?.let { uri ->
                        // You can load the image here if needed
                        Log.d("FileActivity", "Photo saved to: $uri")
                    }
                }
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
