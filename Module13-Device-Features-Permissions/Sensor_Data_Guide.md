# Android Sensor Data Guide

## Overview
This guide covers how to access and process sensor data from Android device sensors. You'll learn how to work with accelerometers, gyroscopes, magnetometers, and other sensors to create interactive and responsive applications.

## Android Sensor Types

### 1. Motion Sensors
- **Accelerometer**: Measures acceleration in 3 axes
- **Gyroscope**: Measures rotation around 3 axes
- **Gravity**: Measures gravity direction
- **Linear Acceleration**: Measures acceleration excluding gravity

### 2. Position Sensors
- **Magnetometer**: Measures magnetic field
- **Proximity**: Measures distance to nearby objects
- **Light**: Measures ambient light level

### 3. Environment Sensors
- **Temperature**: Measures device temperature
- **Pressure**: Measures atmospheric pressure
- **Humidity**: Measures relative humidity

## Implementation

### 1. Sensor Activity Implementation
```kotlin
class SensorActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor
    private lateinit var magnetometer: Sensor
    private lateinit var lightSensor: Sensor
    
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
    
    override fun onPause() {
        super.onPause()
        // Optionally stop sensors when activity pauses
        // stopSensors()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopSensors()
    }
}
```

## Advanced Sensor Features

### 1. Sensor Fusion (Accelerometer + Gyroscope + Magnetometer)
```kotlin
class SensorFusionActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var gyroscope: Sensor
    private lateinit var magnetometer: Sensor
    
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_fusion)
        
        setupSensorManager()
        startSensorFusion()
    }
    
    private fun setupSensorManager() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }
    
    private fun startSensorFusion() {
        accelerometer?.let {
            sensorManager.registerListener(
                this, 
                it, 
                SensorManager.SENSOR_DELAY_GAME
            )
        }
        
        magnetometer?.let {
            sensorManager.registerListener(
                this, 
                it, 
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(it.values, 0, accelerometerReading, 0, 3)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(it.values, 0, magnetometerReading, 0, 3)
                }
            }
            
            // Calculate device orientation
            if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)) {
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                
                val azimuthInRadians = orientationAngles[0]
                val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()
                
                // Update UI with device orientation
                updateOrientationDisplay(azimuthInDegrees)
            }
        }
    }
    
    private fun updateOrientationDisplay(azimuth: Float) {
        val direction = when {
            azimuth >= -22.5 && azimuth < 22.5 -> "North"
            azimuth >= 22.5 && azimuth < 67.5 -> "Northeast"
            azimuth >= 67.5 && azimuth < 112.5 -> "East"
            azimuth >= 112.5 && azimuth < 157.5 -> "Southeast"
            azimuth >= 157.5 && azimuth < 202.5 -> "South"
            azimuth >= 202.5 && azimuth < 247.5 -> "Southwest"
            azimuth >= 247.5 && azimuth < 292.5 -> "West"
            azimuth >= 292.5 && azimuth < 337.5 -> "Northwest"
            else -> "North"
        }
        
        findViewById<TextView>(R.id.tvOrientation).text = """
            Device Orientation:
            Azimuth: ${String.format("%.1f", azimuth)}°
            Direction: $direction
        """.trimIndent()
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes
    }
}
```

### 2. Step Counter
```kotlin
class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounter: Sensor
    private lateinit var stepDetector: Sensor
    
    private var stepCount = 0
    private var initialStepCount = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        
        setupStepCounter()
    }
    
    private fun setupStepCounter() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        
        if (stepCounter != null) {
            sensorManager.registerListener(
                this, 
                stepCounter, 
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        
        if (stepDetector != null) {
            sensorManager.registerListener(
                this, 
                stepDetector, 
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    if (initialStepCount < 0) {
                        initialStepCount = it.values[0].toInt()
                    }
                    stepCount = it.values[0].toInt() - initialStepCount
                    updateStepDisplay()
                }
                Sensor.TYPE_STEP_DETECTOR -> {
                    if (it.values[0] == 1.0f) {
                        stepCount++
                        updateStepDisplay()
                    }
                }
            }
        }
    }
    
    private fun updateStepDisplay() {
        findViewById<TextView>(R.id.tvStepCount).text = "Steps: $stepCount"
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes
    }
}
```

### 3. Proximity Sensor
```kotlin
class ProximitySensorActivity : AppCompatActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private lateinit var proximitySensor: Sensor
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proximity)
        
        setupProximitySensor()
    }
    
    private fun setupProximitySensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        
        proximitySensor?.let {
            sensorManager.registerListener(
                this, 
                it, 
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PROXIMITY) {
                val distance = it.values[0]
                val maxRange = it.sensor.maximumRange
                
                val status = if (distance < maxRange) {
                    "Object detected (${String.format("%.2f", distance)} cm)"
                } else {
                    "No object detected"
                }
                
                findViewById<TextView>(R.id.tvProximity).text = status
                
                // Example: Turn off screen when object is detected
                if (distance < maxRange) {
                    // Handle proximity event
                    Log.d("Proximity", "Object detected - turning off screen")
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes
    }
}
```

## Layout Files

### Main Sensor Activity Layout
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
        android:text="Sensor Data"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginBottom="16dp">

        <Button
            android:id="@+id/btnStartSensors"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginEnd="8dp"
            android:text="Start Sensors" />

        <Button
            android:id="@+id/btnStopSensors"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:layout_marginStart="8dp"
            android:text="Stop Sensors"
            android:enabled="false" />

    </LinearLayout>

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <TextView
                android:id="@+id/tvAccelerometer"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Accelerometer: Stopped"
                android:background="@android:color/white"
                android:padding="8dp"
                android:layout_marginBottom="8dp"
                android:textSize="12sp" />

            <TextView
                android:id="@+id/tvGyroscope"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Gyroscope: Stopped"
                android:background="@android:color/white"
                android:padding="8dp"
                android:layout_marginBottom="8dp"
                android:textSize="12sp" />

            <TextView
                android:id="@+id/tvMagnetometer"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Magnetometer: Stopped"
                android:background="@android:color/white"
                android:padding="8dp"
                android:layout_marginBottom="8dp"
                android:textSize="12sp" />

            <TextView
                android:id="@+id/tvLight"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Light Sensor: Stopped"
                android:background="@android:color/white"
                android:padding="8dp"
                android:textSize="12sp" />

        </LinearLayout>

    </ScrollView>

</LinearLayout>
```

## Best Practices

### 1. Performance Optimization
- Use appropriate sensor delay rates
- Unregister listeners when not needed
- Process sensor data efficiently

### 2. Battery Management
- Stop sensors when app is in background
- Use sensor fusion to reduce power consumption
- Choose appropriate sensor types

### 3. Data Processing
- Filter sensor data to reduce noise
- Use moving averages for stable readings
- Handle sensor accuracy changes

### 4. Error Handling
- Check sensor availability before use
- Handle sensor accuracy changes
- Provide fallbacks for missing sensors

## Testing

### Unit Testing
```kotlin
@Test
fun testSensorAvailability() {
    val activity = ActivityScenario.launch(SensorActivity::class.java)
    
    activity.onActivity { activity ->
        val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        
        // Test sensor availability
        assertNotNull(accelerometer)
    }
}
```

### UI Testing
```kotlin
@Test
fun testSensorStartStop() {
    val scenario = ActivityScenario.launch(SensorActivity::class.java)
    
    scenario.onActivity { activity ->
        // Click start sensors button
        onView(withId(R.id.btnStartSensors))
            .perform(click())
        
        // Verify sensors are active
        onView(withId(R.id.btnStopSensors))
            .check(matches(isEnabled()))
        
        // Click stop sensors button
        onView(withId(R.id.btnStopSensors))
            .perform(click())
        
        // Verify sensors are stopped
        onView(withId(R.id.btnStartSensors))
            .check(matches(isEnabled()))
    }
}
```

## Troubleshooting

### Common Issues:
1. **Sensors not available**: Check device sensor availability
2. **Inaccurate readings**: Calibrate sensors or use sensor fusion
3. **Battery drain**: Optimize sensor usage and update rates
4. **Performance issues**: Use appropriate sensor delay rates

### Debug Tips:
- Log sensor readings for debugging
- Test on different devices
- Check sensor accuracy levels
- Monitor battery usage

## Summary
Android sensors provide rich data for creating interactive applications. Use appropriate sensor types, handle data efficiently, and follow best practices for performance and battery optimization. Sensor fusion can provide more accurate and stable readings for complex applications.
