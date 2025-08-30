# Android Background Location Tracking Guide

## Overview
Background location tracking requires special considerations in Android, especially with modern background restrictions. This guide covers implementing location tracking in foreground services.

## Key Requirements

### 1. Permissions
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
```

### 2. Foreground Service Requirement
- Background location requires foreground service (Android 10+)
- Must show persistent notification
- User must be aware of location tracking

## Implementation

### 1. Location Service
```kotlin
class LocationService : Service() {
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationCallback()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (hasLocationPermissions()) {
            startLocationTracking()
        } else {
            stopSelf()
        }
        return START_STICKY
    }
    
    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d("LocationService", "Location: ${location.latitude}, ${location.longitude}")
                    updateLocationNotification(location)
                }
            }
        }
    }
    
    private fun startLocationTracking() {
        val notification = createLocationNotification("Location tracking started", "0.0, 0.0")
        startForeground(NOTIFICATION_ID, notification)
        
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateDistanceMeters(10f)
            .build()
        
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationService", "Permission denied", e)
            stopSelf()
        }
    }
}
```

### 2. Permission Handling
```kotlin
class LocationPermissionHelper {
    
    fun requestLocationPermissions(activity: Activity) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activity.requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    
    fun requestBackgroundLocationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            activity.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
}
```

## Best Practices

1. **Always use foreground service** for background location
2. **Handle permission denials** gracefully
3. **Show user-friendly notifications**
4. **Respect battery optimization**
5. **Test on real devices**

## Summary
Background location tracking requires careful implementation with foreground services and proper permission handling. Focus on user experience and battery efficiency.
