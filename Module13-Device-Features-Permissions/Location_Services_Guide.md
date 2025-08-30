# Android Location Services Guide

## Overview
This guide covers how to integrate location services into Android apps using Google Play Services Location API. You'll learn how to get current location, track location updates, and handle location permissions properly.

## Location Services Components

### 1. FusedLocationProviderClient
- Most efficient way to get location
- Automatically handles different location providers
- Battery-optimized location requests

### 2. LocationRequest
- Configures location update parameters
- Controls accuracy, frequency, and priority

### 3. LocationCallback
- Handles location updates
- Receives location data asynchronously

## Implementation

### 1. Add Dependencies
```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.google.android.gms:play-services-location:21.0.1")
}
```

### 2. Add Permissions
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

### 3. Location Activity Implementation
```kotlin
class LocationActivity : AppCompatActivity() {
    
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 101
    }
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    
    private lateinit var tvLocation: TextView
    private lateinit var btnGetLocation: Button
    private lateinit var btnStartTracking: Button
    private lateinit var btnStopTracking: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        
        tvLocation = findViewById(R.id.tvLocation)
        btnGetLocation = findViewById(R.id.btnGetLocation)
        btnStartTracking = findViewById(R.id.btnStartTracking)
        btnStopTracking = findViewById(R.id.btnStopTracking)
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        setupLocationCallback()
        setupLocationRequest()
        setupButtons()
    }
    
    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    updateLocationDisplay(location)
                }
            }
        }
    }
    
    private fun setupLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }
    
    private fun setupButtons() {
        btnGetLocation.setOnClickListener {
            if (checkLocationPermission()) {
                getLastLocation()
            } else {
                requestLocationPermission()
            }
        }
        
        btnStartTracking.setOnClickListener {
            if (checkLocationPermission()) {
                startLocationUpdates()
            } else {
                requestLocationPermission()
            }
        }
        
        btnStopTracking.setOnClickListener {
            stopLocationUpdates()
        }
    }
    
    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, 
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    
    private fun getLastLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        updateLocationDisplay(it)
                    } ?: run {
                        Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error getting location: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    
    private fun startLocationUpdates() {
        if (checkLocationPermission()) {
            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
                Toast.makeText(this, "Location tracking started", Toast.LENGTH_SHORT).show()
            } catch (e: SecurityException) {
                Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Toast.makeText(this, "Location tracking stopped", Toast.LENGTH_SHORT).show()
    }
    
    private fun updateLocationDisplay(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        val accuracy = location.accuracy
        
        val locationText = """
            Latitude: $latitude
            Longitude: $longitude
            Accuracy: ${accuracy}m
            Provider: ${location.provider}
            Time: ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(location.time))}
        """.trimIndent()
        
        tvLocation.text = locationText
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && 
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Optionally restart location updates when activity resumes
    }
    
    override fun onPause() {
        super.onPause()
        // Optionally stop location updates when activity pauses
        // stopLocationUpdates()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }
}
```

## Advanced Location Features

### Background Location (Android 10+)
```kotlin
private fun requestBackgroundLocationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (checkLocationPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            Toast.makeText(this, "Fine location permission required first", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun startBackgroundLocationUpdates() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        if (ContextCompat.checkSelfPermission(
                this, 
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            
            val backgroundLocationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 30000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(15000)
                .build()
            
            fusedLocationClient.requestLocationUpdates(
                backgroundLocationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }
}
```

### Geofencing
```kotlin
class GeofenceActivity : AppCompatActivity() {
    
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceList: MutableList<Geofence>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geofence)
        
        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceList = mutableListOf()
        
        createGeofence()
    }
    
    private fun createGeofence() {
        val geofence = Geofence.Builder()
            .setRequestId("office_geofence")
            .setCircularRegion(
                37.4220, // latitude
                -122.0841, // longitude
                100f // radius in meters
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
        
        geofenceList.add(geofence)
    }
    
    private fun startGeofencing() {
        if (checkLocationPermission()) {
            val geofencingRequest = GeofencingRequest.Builder().apply {
                setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                addGeofences(geofenceList)
            }.build()
            
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Intent(this, GeofenceBroadcastReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener {
                    Toast.makeText(this, "Geofencing started", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Geofencing failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        
        geofencingEvent?.let { event ->
            if (event.hasError()) {
                Log.e("Geofence", "Geofencing error: ${event.errorCode}")
                return
            }
            
            val geofenceTransition = event.geofenceTransition
            
            when (geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    Log.d("Geofence", "Entered geofence")
                    // Handle enter event
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    Log.d("Geofence", "Exited geofence")
                    // Handle exit event
                }
            }
        }
    }
}
```

### Location Settings
```kotlin
private fun checkLocationSettings() {
    val locationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()
    
    val client = LocationServices.getSettingsClient(this)
    val task = client.checkLocationSettings(locationSettingsRequest)
    
    task.addOnSuccessListener { locationSettingsResponse ->
        // Location settings are satisfied
        startLocationUpdates()
    }
    
    task.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                exception.startResolutionForResult(this, LOCATION_SETTINGS_REQUEST_CODE)
            } catch (sendEx: IntentSender.SendIntentException) {
                Toast.makeText(this, "Error opening location settings", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    
    if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
        if (resultCode == RESULT_OK) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Location settings required", Toast.LENGTH_SHORT).show()
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
        android:text="Location Services"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="24dp" />

    <Button
        android:id="@+id/btnGetLocation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Get Current Location"
        android:layout_marginBottom="8dp" />

    <Button
        android:id="@+id/btnStartTracking"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Start Location Tracking"
        android:layout_marginBottom="8dp" />

    <Button
        android:id="@+id/btnStopTracking"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Stop Location Tracking"
        android:layout_marginBottom="16dp" />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Location Information:"
        android:textStyle="bold"
        android:layout_marginBottom="8dp" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <TextView
            android:id="@+id/tvLocation"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="No location data available"
            android:background="@android:color/white"
            android:padding="8dp"
            android:textSize="14sp" />

    </ScrollView>

</LinearLayout>
```

## Best Practices

### 1. Permission Handling
- Request location permissions just-in-time
- Handle both fine and coarse location permissions
- Request background location only when necessary

### 2. Battery Optimization
- Use appropriate location request priorities
- Set reasonable update intervals
- Stop location updates when not needed

### 3. Accuracy vs Battery Life
- `PRIORITY_HIGH_ACCURACY`: Most accurate, highest battery usage
- `PRIORITY_BALANCED_POWER_ACCURACY`: Good balance
- `PRIORITY_LOW_POWER`: Least accurate, lowest battery usage

### 4. Error Handling
- Handle location provider availability
- Check location settings
- Handle permission denial gracefully

## Testing

### Unit Testing
```kotlin
@Test
fun testLocationPermissionFlow() {
    val activity = ActivityScenario.launch(LocationActivity::class.java)
    
    activity.onActivity { activity ->
        // Test permission check
        assertFalse(activity.checkLocationPermission())
        
        // Test location request
        // (Would require mocking location services)
    }
}
```

### UI Testing
```kotlin
@Test
fun testLocationDisplay() {
    val scenario = ActivityScenario.launch(LocationActivity::class.java)
    
    scenario.onActivity { activity ->
        // Click get location button
        onView(withId(R.id.btnGetLocation))
            .perform(click())
        
        // Verify location is displayed
        onView(withId(R.id.tvLocation))
            .check(matches(not(withText("No location data available"))))
    }
}
```

## Troubleshooting

### Common Issues:
1. **Location not available**: Check if location services are enabled
2. **Permission denied**: Handle permission requests properly
3. **Battery drain**: Use appropriate location request settings
4. **Background location**: Requires separate permission on Android 10+

### Debug Tips:
- Use `adb shell dumpsys location` to check location services
- Log location updates for debugging
- Test on different devices and Android versions
- Verify Google Play Services availability

## Summary
Location services in Android provide powerful capabilities for getting user location. Use FusedLocationProviderClient for the best performance and battery efficiency. Always handle permissions properly and consider the impact on battery life when implementing location features.
