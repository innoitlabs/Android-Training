package com.example.devicefeatures

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*

class LocationActivity : AppCompatActivity() {
    
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
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
    
    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }
}
