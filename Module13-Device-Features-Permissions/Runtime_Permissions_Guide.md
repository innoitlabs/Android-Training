# Android Runtime Permissions Guide

## Overview
Runtime permissions are a fundamental security feature in Android that requires apps to request sensitive permissions at runtime rather than at install time. This guide covers everything you need to know about implementing runtime permissions in your Android apps.

## Permission Types

### Normal Permissions
- Granted automatically at install time
- Don't pose a risk to user privacy or device operation
- Examples: `INTERNET`, `ACCESS_NETWORK_STATE`, `VIBRATE`

### Dangerous Permissions
- Require explicit user approval at runtime
- Access sensitive user data or device features
- Examples: `CAMERA`, `ACCESS_FINE_LOCATION`, `READ_CONTACTS`

## Permission Groups

Android organizes dangerous permissions into groups. When a user grants one permission in a group, all permissions in that group are automatically granted.

### Common Permission Groups:
- **CAMERA**: `CAMERA`
- **LOCATION**: `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`
- **STORAGE**: `READ_EXTERNAL_STORAGE`, `WRITE_EXTERNAL_STORAGE`
- **CONTACTS**: `READ_CONTACTS`, `WRITE_CONTACTS`
- **MICROPHONE**: `RECORD_AUDIO`
- **SMS**: `SEND_SMS`, `READ_SMS`, `RECEIVE_SMS`

## Implementation Steps

### 1. Declare Permissions in AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### 2. Check Permission Status
```kotlin
fun checkPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this, 
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
```

### 3. Request Permission
```kotlin
fun requestPermission(permission: String, requestCode: Int) {
    if (ContextCompat.checkSelfPermission(
            this, 
            permission
        ) != PackageManager.PERMISSION_GRANTED) {
        
        // Check if we should show explanation
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // Show explanation to user
            showPermissionExplanation(permission, requestCode)
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                this, 
                arrayOf(permission), 
                requestCode
            )
        }
    }
}
```

### 4. Handle Permission Results
```kotlin
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
                // Permission granted, proceed with camera functionality
                openCamera()
            } else {
                // Permission denied
                handlePermissionDenied("Camera")
            }
        }
        LOCATION_PERMISSION_REQUEST_CODE -> {
            if (grantResults.isNotEmpty() && 
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location functionality
                startLocationUpdates()
            } else {
                // Permission denied
                handlePermissionDenied("Location")
            }
        }
    }
}
```

## Best Practices

### 1. Request Permissions Just-in-Time
- Only request permissions when the user actually needs the feature
- Don't request all permissions at app startup

### 2. Provide Clear Explanations
```kotlin
private fun showPermissionExplanation(permission: String, requestCode: Int) {
    val message = when (permission) {
        Manifest.permission.CAMERA -> 
            "Camera access is needed to take photos for your profile"
        Manifest.permission.ACCESS_FINE_LOCATION -> 
            "Location access is needed to show nearby places"
        else -> "This permission is required for app functionality"
    }
    
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage(message)
        .setPositiveButton("Grant") { _, _ ->
            ActivityCompat.requestPermissions(
                this, 
                arrayOf(permission), 
                requestCode
            )
        }
        .setNegativeButton("Cancel", null)
        .show()
}
```

### 3. Handle "Don't Ask Again" Gracefully
```kotlin
private fun handlePermissionDenied(permissionName: String) {
    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
        // User selected "Don't ask again"
        showSettingsDialog()
    } else {
        // User denied permission but can be asked again
        Toast.makeText(this, "$permissionName permission is required", Toast.LENGTH_LONG).show()
    }
}

private fun showSettingsDialog() {
    AlertDialog.Builder(this)
        .setTitle("Permission Required")
        .setMessage("This feature requires permission. Please enable it in Settings.")
        .setPositiveButton("Settings") { _, _ ->
            openAppSettings()
        }
        .setNegativeButton("Cancel", null)
        .show()
}

private fun openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}
```

### 4. Use Permission Helper Classes
```kotlin
class PermissionHelper(private val activity: Activity) {
    
    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val LOCATION_PERMISSION_REQUEST_CODE = 101
        const val STORAGE_PERMISSION_REQUEST_CODE = 102
    }
    
    fun requestCameraPermission() {
        requestPermission(
            Manifest.permission.CAMERA,
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }
    
    fun requestLocationPermission() {
        requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    
    private fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                activity, 
                permission
            ) != PackageManager.PERMISSION_GRANTED) {
            
            ActivityCompat.requestPermissions(
                activity, 
                arrayOf(permission), 
                requestCode
            )
        }
    }
    
    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, 
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}
```

## Common Permission Patterns

### Multiple Permissions
```kotlin
private fun requestMultiplePermissions() {
    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    
    val permissionsToRequest = permissions.filter {
        !isPermissionGranted(it)
    }.toTypedArray()
    
    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            this, 
            permissionsToRequest, 
            MULTIPLE_PERMISSIONS_REQUEST_CODE
        )
    }
}
```

### Conditional Permission Requests
```kotlin
private fun requestLocationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10+ - request background location separately
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestBackgroundLocationPermission()
        } else {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    } else {
        // Android 9 and below
        requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}
```

## Testing Permissions

### Using ADB Commands
```bash
# Grant permission
adb shell pm grant com.example.app android.permission.CAMERA

# Revoke permission
adb shell pm revoke com.example.app android.permission.CAMERA

# Check permission status
adb shell dumpsys package com.example.app | grep permission
```

### UI Testing
```kotlin
@Test
fun testCameraPermissionFlow() {
    // Click button that requires camera permission
    onView(withId(R.id.btnCamera))
        .perform(click())
    
    // Check if permission dialog appears
    onView(withText("Allow"))
        .check(matches(isDisplayed()))
    
    // Grant permission
    onView(withText("Allow"))
        .perform(click())
    
    // Verify camera functionality works
    onView(withId(R.id.imageView))
        .check(matches(isDisplayed()))
}
```

## Troubleshooting

### Common Issues:
1. **Permission not requested**: Ensure permission is declared in AndroidManifest.xml
2. **Permission denied permanently**: Handle "Don't ask again" case
3. **Permission group behavior**: Remember that granting one permission grants all in the group
4. **Target SDK issues**: Ensure proper target SDK version for permission behavior

### Debug Tips:
- Use `adb shell dumpsys package` to check permission status
- Log permission request results for debugging
- Test on different Android versions
- Test with different permission states (granted, denied, never asked)

## Summary
Runtime permissions are essential for modern Android development. Follow these best practices:
- Request permissions just-in-time
- Provide clear explanations
- Handle all permission states gracefully
- Test thoroughly on different devices and Android versions
- Use helper classes for cleaner code organization
