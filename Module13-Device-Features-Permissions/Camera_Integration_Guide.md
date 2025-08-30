# Android Camera Integration Guide

## Overview
This guide covers how to integrate camera functionality into Android apps, including both traditional Intent-based approaches and modern CameraX library. You'll learn how to capture images, handle permissions, and process camera data.

## Camera Integration Approaches

### 1. Intent-Based Camera (Simple)
- Uses system camera app
- Quick to implement
- Limited customization
- Good for basic photo capture

### 2. CameraX (Modern)
- Jetpack library for camera functionality
- More control and customization
- Better lifecycle management
- Preview, image capture, and image analysis

## Implementation

### Intent-Based Camera Integration

#### 1. Add Permissions
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

#### 2. Create File Provider (for Android 7+)
```xml
<!-- res/xml/file_paths.xml -->
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path name="my_images" path="Pictures" />
</paths>
```

```xml
<!-- AndroidManifest.xml -->
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

#### 3. Camera Activity Implementation
```kotlin
class CameraActivity : AppCompatActivity() {
    
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 101
    }
    
    private lateinit var imageView: ImageView
    private lateinit var btnCapture: Button
    private var photoUri: Uri? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        
        imageView = findViewById(R.id.imageView)
        btnCapture = findViewById(R.id.btnCapture)
        
        btnCapture.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
    }
    
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, 
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
    
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        
        // Create file for the photo
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            photoFile
        )
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        
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
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && 
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Load the captured image
            photoUri?.let { uri ->
                try {
                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    }
                    imageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
```

### CameraX Integration

#### 1. Add Dependencies
```kotlin
// app/build.gradle.kts
dependencies {
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
}
```

#### 2. CameraX Activity Implementation
```kotlin
class CameraXActivity : AppCompatActivity() {
    
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture
    private lateinit var preview: Preview
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    
    private lateinit var previewView: PreviewView
    private lateinit var btnCapture: Button
    private lateinit var btnSwitch: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_x)
        
        previewView = findViewById(R.id.previewView)
        btnCapture = findViewById(R.id.btnCapture)
        btnSwitch = findViewById(R.id.btnSwitch)
        
        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }
        
        btnCapture.setOnClickListener {
            takePhoto()
        }
        
        btnSwitch.setOnClickListener {
            switchCamera()
        }
    }
    
    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            
            preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            
            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                
                preview.setSurfaceProvider(previewView.surfaceProvider)
                
            } catch (e: Exception) {
                Log.e("CameraX", "Camera binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    
    private fun takePhoto() {
        val photoFile = createImageFile()
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val message = "Photo saved: $savedUri"
                    Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
                    
                    // Load the captured image
                    try {
                        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, savedUri))
                        } else {
                            MediaStore.Images.Media.getBitmap(contentResolver, savedUri)
                        }
                        // Display the image
                        findViewById<ImageView>(R.id.imageView).setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(baseContext, "Error loading image", Toast.LENGTH_SHORT).show()
                    }
                }
                
                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(baseContext, "Photo capture failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    
    private fun switchCamera() {
        val cameraSelector = if (camera.cameraInfo.lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageCapture
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)
        } catch (e: Exception) {
            Log.e("CameraX", "Camera switching failed", e)
        }
    }
    
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }
    
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, 
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && 
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
```

## Layout Files

### Intent-Based Camera Layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:scaleType="centerCrop"
        android:background="@android:color/darker_gray" />

    <Button
        android:id="@+id/btnCapture"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Take Photo" />

</LinearLayout>
```

### CameraX Layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <androidx.camera.view.PreviewView
        android:id="@+id/previewView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp">

        <Button
            android:id="@+id/btnSwitch"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginEnd="8dp"
            android:text="Switch Camera" />

        <Button
            android:id="@+id/btnCapture"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="8dp"
            android:text="Capture" />

    </LinearLayout>

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:scaleType="centerCrop"
        android:background="@android:color/darker_gray" />

</LinearLayout>
```

## Advanced Features

### Image Analysis with CameraX
```kotlin
class ImageAnalysisActivity : AppCompatActivity() {
    
    private lateinit var imageAnalysis: ImageAnalysis
    
    private fun setupImageAnalysis() {
        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
            // Process the image here
            val buffer = imageProxy.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            
            // Example: Convert to bitmap for processing
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            
            // Do something with the bitmap
            processImage(bitmap)
            
            imageProxy.close()
        }
    }
    
    private fun processImage(bitmap: Bitmap) {
        // Example: Detect faces, objects, or perform OCR
        runOnUiThread {
            // Update UI with analysis results
        }
    }
}
```

### Camera Controls
```kotlin
private fun setupCameraControls() {
    // Flash control
    camera.cameraControl.enableTorch(true) // Turn on flash
    
    // Zoom control
    camera.cameraControl.setZoomRatio(2.0f) // 2x zoom
    
    // Focus control
    val action = FocusMeteringAction.Builder(previewView.surfaceProvider).build()
    camera.cameraControl.startFocusAndMetering(action)
}
```

## Best Practices

### 1. Permission Handling
- Always check camera permission before accessing camera
- Provide clear explanations for camera usage
- Handle permission denial gracefully

### 2. File Management
- Use FileProvider for sharing files (Android 7+)
- Store images in app-specific directories
- Clean up temporary files

### 3. Performance
- Use appropriate image quality settings
- Implement proper lifecycle management
- Handle camera resource cleanup

### 4. User Experience
- Show camera preview when possible
- Provide clear feedback for capture actions
- Handle camera errors gracefully

## Testing

### Unit Testing
```kotlin
@Test
fun testCameraPermissionFlow() {
    // Test permission request flow
    val activity = ActivityScenario.launch(CameraActivity::class.java)
    
    activity.onActivity { activity ->
        // Verify permission check
        assertFalse(activity.checkCameraPermission())
        
        // Verify camera opens after permission grant
        // (This would require mocking the camera intent)
    }
}
```

### UI Testing
```kotlin
@Test
fun testCameraCapture() {
    // Launch camera activity
    val scenario = ActivityScenario.launch(CameraActivity::class.java)
    
    scenario.onActivity { activity ->
        // Click capture button
        onView(withId(R.id.btnCapture))
            .perform(click())
        
        // Verify image is captured and displayed
        onView(withId(R.id.imageView))
            .check(matches(isDisplayed()))
    }
}
```

## Troubleshooting

### Common Issues:
1. **Camera not opening**: Check permissions and device camera availability
2. **File provider errors**: Ensure proper FileProvider configuration
3. **Image loading issues**: Handle different Android versions for bitmap loading
4. **CameraX binding errors**: Check lifecycle and camera availability

### Debug Tips:
- Use `adb shell dumpsys media.camera` to check camera status
- Log camera events for debugging
- Test on different devices and Android versions
- Verify FileProvider paths and authorities

## Summary
Camera integration in Android can be implemented using either Intent-based approach or CameraX. Choose based on your requirements:
- **Intent-based**: Simple, quick implementation
- **CameraX**: More control, better lifecycle management, advanced features

Always handle permissions properly and follow security best practices when working with camera functionality.
