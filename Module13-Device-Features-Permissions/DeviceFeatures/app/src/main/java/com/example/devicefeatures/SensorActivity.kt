package com.example.devicefeatures

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class SensorActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var magnetometer: Sensor? = null
    private var lightSensor: Sensor? = null
    
    private lateinit var tvAccelerometer: TextView
    private lateinit var tvGyroscope: TextView
    private lateinit var tvMagnetometer: TextView
    private lateinit var tvLight: TextView
    private lateinit var btnStartSensors: Button
    private lateinit var btnStopSensors: Button
    
    private var isSensorsActive = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
        
        tvAccelerometer = findViewById(R.id.tvAccelerometer)
        tvGyroscope = findViewById(R.id.tvGyroscope)
        tvMagnetometer = findViewById(R.id.tvMagnetometer)
        tvLight = findViewById(R.id.tvLight)
        btnStartSensors = findViewById(R.id.btnStartSensors)
        btnStopSensors = findViewById(R.id.btnStopSensors)
        
        setupSensorManager()
        setupButtons()
    }
    
    private fun setupSensorManager() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        
        // Get default sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        
        // Check sensor availability
        checkSensorAvailability()
    }
    
    private fun checkSensorAvailability() {
        val availableSensors = mutableListOf<String>()
        
        if (accelerometer != null) availableSensors.add("Accelerometer")
        if (gyroscope != null) availableSensors.add("Gyroscope")
        if (magnetometer != null) availableSensors.add("Magnetometer")
        if (lightSensor != null) availableSensors.add("Light Sensor")
        
        Toast.makeText(
            this, 
            "Available sensors: ${availableSensors.joinToString(", ")}", 
            Toast.LENGTH_LONG
        ).show()
    }
    
    private fun setupButtons() {
        btnStartSensors.setOnClickListener {
            startSensors()
        }
        
        btnStopSensors.setOnClickListener {
            stopSensors()
        }
    }
    
    private fun startSensors() {
        if (!isSensorsActive) {
            // Register sensor listeners
            accelerometer?.let {
                sensorManager.registerListener(
                    this, 
                    it, 
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
            
            gyroscope?.let {
                sensorManager.registerListener(
                    this, 
                    it, 
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
            
            magnetometer?.let {
                sensorManager.registerListener(
                    this, 
                    it, 
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
            
            lightSensor?.let {
                sensorManager.registerListener(
                    this, 
                    it, 
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
            
            isSensorsActive = true
            btnStartSensors.isEnabled = false
            btnStopSensors.isEnabled = true
            Toast.makeText(this, "Sensors started", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun stopSensors() {
        if (isSensorsActive) {
            sensorManager.unregisterListener(this)
            isSensorsActive = false
            btnStartSensors.isEnabled = true
            btnStopSensors.isEnabled = false
            
            // Clear displays
            tvAccelerometer.text = "Accelerometer: Stopped"
            tvGyroscope.text = "Gyroscope: Stopped"
            tvMagnetometer.text = "Magnetometer: Stopped"
            tvLight.text = "Light Sensor: Stopped"
            
            Toast.makeText(this, "Sensors stopped", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    
                    val magnitude = sqrt(x * x + y * y + z * z)
                    
                    tvAccelerometer.text = """
                        Accelerometer:
                        X: ${String.format("%.2f", x)} m/s²
                        Y: ${String.format("%.2f", y)} m/s²
                        Z: ${String.format("%.2f", z)} m/s²
                        Magnitude: ${String.format("%.2f", magnitude)} m/s²
                    """.trimIndent()
                    
                    // Detect device movement
                    if (magnitude > 15) {
                        Log.d("Sensor", "Device movement detected!")
                    }
                }
                
                Sensor.TYPE_GYROSCOPE -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    
                    tvGyroscope.text = """
                        Gyroscope:
                        X: ${String.format("%.2f", x)} rad/s
                        Y: ${String.format("%.2f", y)} rad/s
                        Z: ${String.format("%.2f", z)} rad/s
                    """.trimIndent()
                    
                    // Detect rotation
                    val rotationMagnitude = sqrt(x * x + y * y + z * z)
                    if (rotationMagnitude > 1.0) {
                        Log.d("Sensor", "Device rotation detected!")
                    }
                }
                
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    
                    val magnitude = sqrt(x * x + y * y + z * z)
                    
                    tvMagnetometer.text = """
                        Magnetometer:
                        X: ${String.format("%.2f", x)} μT
                        Y: ${String.format("%.2f", y)} μT
                        Z: ${String.format("%.2f", z)} μT
                        Magnitude: ${String.format("%.2f", magnitude)} μT
                    """.trimIndent()
                }
                
                Sensor.TYPE_LIGHT -> {
                    val lightLevel = it.values[0]
                    
                    tvLight.text = """
                        Light Sensor:
                        Level: ${String.format("%.2f", lightLevel)} lux
                        Status: ${getLightStatus(lightLevel)}
                    """.trimIndent()
                }
            }
        }
    }
    
    private fun getLightStatus(lightLevel: Float): String {
        return when {
            lightLevel < 10 -> "Very Dark"
            lightLevel < 50 -> "Dark"
            lightLevel < 200 -> "Dim"
            lightLevel < 1000 -> "Normal"
            lightLevel < 10000 -> "Bright"
            else -> "Very Bright"
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
        Log.d("Sensor", "Accuracy changed for ${sensor?.name}: $accuracy")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopSensors()
    }
}
