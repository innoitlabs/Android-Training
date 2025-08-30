package com.example.devicefeatures

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

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
    
    fun requestStoragePermission() {
        requestPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_REQUEST_CODE
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
