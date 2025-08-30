# Hands-on Lab: Background Sync & Location Tracker App

## Table of Contents
1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Project Setup](#project-setup)
4. [Step-by-Step Implementation](#step-by-step-implementation)
5. [Testing the Application](#testing-the-application)
6. [Troubleshooting](#troubleshooting)
7. [Extensions and Enhancements](#extensions-and-enhancements)

## Project Overview

This hands-on lab guides you through building a comprehensive Android application that demonstrates various background processing concepts:

- **Foreground Service** with persistent notification
- **WorkManager** for periodic data synchronization
- **AlarmManager** for scheduled reminders
- **Background Location Tracking** with Foreground Service
- **Notification Channels** and user notifications
- **Permission Handling** for location and notifications

### Learning Objectives
By completing this lab, you will:
- Implement a complete foreground service with notifications
- Use WorkManager for reliable background tasks
- Handle location permissions and background location updates
- Schedule tasks using AlarmManager
- Create and manage notification channels
- Test background processing on real devices

## Prerequisites

### Required Tools
- Android Studio (latest version)
- Android SDK (API 24+)
- Physical device or emulator with Google Play Services
- Basic knowledge of Kotlin and Android development

### Required Permissions
The app will request the following permissions:
- `ACCESS_FINE_LOCATION` - For location tracking
- `ACCESS_BACKGROUND_LOCATION` - For background location (Android 10+)
- `POST_NOTIFICATIONS` - For notifications (Android 13+)
- `FOREGROUND_SERVICE` - For foreground services (Android 14+)

## Project Setup

### 1. Open the Project
1. Open Android Studio
2. Open the `BackgroundProcesses` project
3. Sync the project with Gradle files
4. Ensure all dependencies are downloaded

### 2. Verify Dependencies
Check that the following dependencies are included in `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
```

### 3. Update AndroidManifest.xml
Ensure the manifest includes all necessary permissions and service declarations.

## Step-by-Step Implementation

### Step 1: Create Notification Channels

**File**: `NotificationHelper.kt`

```kotlin
class NotificationHelper(private val context: Context) {
    
    companion object {
        const val CHANNEL_ID_GENERAL = "general_channel"
        const val CHANNEL_ID_FOREGROUND = "foreground_channel"
        const val CHANNEL_ID_ALERTS = "alerts_channel"
        const val CHANNEL_ID_UPDATES = "updates_channel"
    }
    
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val channels = listOf(
                createGeneralChannel(),
                createForegroundChannel(),
                createAlertsChannel(),
                createUpdatesChannel()
            )
            
            notificationManager.createNotificationChannels(channels)
        }
    }
    
    private fun createGeneralChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID_GENERAL,
            "General Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "General app notifications"
            enableLights(true)
            lightColor = Color.BLUE
        }
    }
    
    private fun createForegroundChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID_FOREGROUND,
            "Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Foreground service notifications"
            setShowBadge(false)
        }
    }
    
    private fun createAlertsChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID_ALERTS,
            "Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Important alerts and warnings"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
    }
    
    private fun createUpdatesChannel(): NotificationChannel {
        return NotificationChannel(
            CHANNEL_ID_UPDATES,
            "Updates",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "App updates and sync notifications"
            enableLights(true)
            lightColor = Color.GREEN
        }
    }
}
```

### Step 2: Create the Foreground Service

**File**: `ForegroundService.kt`

```kotlin
class ForegroundService : Service() {
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val TAG = "ForegroundService"
    }
    
    private lateinit var notificationHelper: NotificationHelper
    private var isRunning = false
    
    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannels()
        Log.d(TAG, "ForegroundService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "ForegroundService started")
        
        when (intent?.action) {
            "START_SERVICE" -> startForegroundService()
            "STOP_SERVICE" -> stopForegroundService()
            else -> startForegroundService()
        }
        
        return START_STICKY
    }
    
    private fun startForegroundService() {
        if (!isRunning) {
            val notification = createNotification("Background Service Active", "Service is running in background")
            startForeground(NOTIFICATION_ID, notification)
            isRunning = true
            
            // Start background work
            startBackgroundWork()
        }
    }
    
    private fun stopForegroundService() {
        isRunning = false
        stopForeground(true)
        stopSelf()
    }
    
    private fun createNotification(title: String, content: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Stop service action
        val stopIntent = Intent(this, ForegroundService::class.java).apply {
            action = "STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID_FOREGROUND)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    private fun startBackgroundWork() {
        Thread {
            var counter = 0
            while (isRunning) {
                counter++
                Log.d(TAG, "Background work iteration: $counter")
                
                // Update notification every 10 iterations
                if (counter % 10 == 0) {
                    val notification = createNotification(
                        "Background Service Active",
                        "Running for ${counter} iterations"
                    )
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }
                
                Thread.sleep(2000) // Work every 2 seconds
            }
        }.start()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d(TAG, "ForegroundService destroyed")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
```

### Step 3: Create WorkManager Worker

**File**: `SyncWorker.kt`

```kotlin
class SyncWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    
    companion object {
        private const val TAG = "SyncWorker"
        const val WORK_NAME = "periodic_sync_work"
    }
    
    override suspend fun doWork(): Result {
        Log.d(TAG, "SyncWorker started")
        
        return try {
            // Simulate sync work
            performSync()
            Log.d(TAG, "SyncWorker completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "SyncWorker failed", e)
            Result.failure()
        }
    }
    
    private suspend fun performSync() {
        // Simulate network sync
        delay(3000)
        
        // Show notification about sync completion
        showSyncNotification()
        
        Log.d(TAG, "Sync completed")
    }
    
    private fun showSyncNotification() {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotificationChannels()
        
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID_UPDATES)
            .setContentTitle("Data Sync Complete")
            .setContentText("Background sync completed successfully")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }
}
```

### Step 4: Create AlarmManager Receiver

**File**: `AlarmReceiver.kt`

```kotlin
class AlarmReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "AlarmReceiver"
        const val ALARM_REQUEST_CODE = 100
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Alarm received")
        
        // Show reminder notification
        showReminderNotification(context)
        
        // Schedule next alarm
        scheduleNextAlarm(context)
    }
    
    private fun showReminderNotification(context: Context) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotificationChannels()
        
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID_ALERTS)
            .setContentTitle("Daily Reminder")
            .setContentText("Time to check your app!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(3, notification)
    }
    
    private fun scheduleNextAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Schedule next alarm in 24 hours
        val nextAlarmTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime,
                pendingIntent
            )
        }
        
        Log.d(TAG, "Next alarm scheduled for: ${Date(nextAlarmTime)}")
    }
}
```

### Step 5: Create Location Service

**File**: `LocationService.kt`

```kotlin
class LocationService : Service() {
    
    companion object {
        private const val NOTIFICATION_ID = 4
        private const val TAG = "LocationService"
    }
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var isTracking = false
    
    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationCallback()
        Log.d(TAG, "LocationService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "LocationService started")
        
        when (intent?.action) {
            "START_TRACKING" -> startLocationTracking()
            "STOP_TRACKING" -> stopLocationTracking()
            else -> startLocationTracking()
        }
        
        return START_STICKY
    }
    
    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    Log.d(TAG, "Location: ${location.latitude}, ${location.longitude}")
                    updateLocationNotification(location)
                }
            }
        }
    }
    
    private fun startLocationTracking() {
        if (!isTracking && hasLocationPermissions()) {
            val notification = createLocationNotification("Location tracking started", "0.0, 0.0")
            startForeground(NOTIFICATION_ID, notification)
            isTracking = true
            
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateDistanceMeters(10f)
                .build()
            
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            
            Log.d(TAG, "Location tracking started")
        }
    }
    
    private fun stopLocationTracking() {
        if (isTracking) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            isTracking = false
            stopForeground(true)
            stopSelf()
            Log.d(TAG, "Location tracking stopped")
        }
    }
    
    private fun updateLocationNotification(location: Location) {
        val notification = createLocationNotification(
            "Location Tracking Active",
            "${location.latitude}, ${location.longitude}"
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    private fun createLocationNotification(title: String, location: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val stopIntent = Intent(this, LocationService::class.java).apply {
            action = "STOP_TRACKING"
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID_FOREGROUND)
            .setContentTitle(title)
            .setContentText("Current location: $location")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isTracking) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
        Log.d(TAG, "LocationService destroyed")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
```

### Step 6: Update MainActivity

**File**: `MainActivity.kt`

```kotlin
class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 1002
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1003
    }
    
    private lateinit var workManager: WorkManager
    private lateinit var notificationHelper: NotificationHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        workManager = WorkManager.getInstance(this)
        notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannels()
        
        setupUI()
        requestPermissions()
    }
    
    private fun setupUI() {
        // Foreground Service buttons
        findViewById<Button>(R.id.btnStartForegroundService).setOnClickListener {
            startForegroundService()
        }
        
        findViewById<Button>(R.id.btnStopForegroundService).setOnClickListener {
            stopForegroundService()
        }
        
        // WorkManager buttons
        findViewById<Button>(R.id.btnScheduleWork).setOnClickListener {
            schedulePeriodicWork()
        }
        
        findViewById<Button>(R.id.btnCancelWork).setOnClickListener {
            cancelPeriodicWork()
        }
        
        // AlarmManager buttons
        findViewById<Button>(R.id.btnScheduleAlarm).setOnClickListener {
            scheduleAlarm()
        }
        
        findViewById<Button>(R.id.btnCancelAlarm).setOnClickListener {
            cancelAlarm()
        }
        
        // Location Service buttons
        findViewById<Button>(R.id.btnStartLocationTracking).setOnClickListener {
            startLocationTracking()
        }
        
        findViewById<Button>(R.id.btnStopLocationTracking).setOnClickListener {
            stopLocationTracking()
        }
        
        // Test buttons
        findViewById<Button>(R.id.btnTestNotification).setOnClickListener {
            testNotification()
        }
        
        findViewById<Button>(R.id.btnShowWorkStatus).setOnClickListener {
            showWorkStatus()
        }
    }
    
    private fun startForegroundService() {
        val intent = Intent(this, ForegroundService::class.java).apply {
            action = "START_SERVICE"
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Log.d(TAG, "Foreground service started")
    }
    
    private fun stopForegroundService() {
        val intent = Intent(this, ForegroundService::class.java).apply {
            action = "STOP_SERVICE"
        }
        startService(intent)
        Log.d(TAG, "Foreground service stopped")
    }
    
    private fun schedulePeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        
        val periodicWork = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWork
        )
        
        Log.d(TAG, "Periodic work scheduled")
        showToast("Periodic work scheduled")
    }
    
    private fun cancelPeriodicWork() {
        workManager.cancelUniqueWork(SyncWorker.WORK_NAME)
        Log.d(TAG, "Periodic work cancelled")
        showToast("Periodic work cancelled")
    }
    
    private fun scheduleAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            AlarmReceiver.ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Schedule alarm for 1 minute from now
        val alarmTime = System.currentTimeMillis() + (60 * 1000)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        }
        
        Log.d(TAG, "Alarm scheduled for: ${Date(alarmTime)}")
        showToast("Alarm scheduled for 1 minute from now")
    }
    
    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            AlarmReceiver.ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Alarm cancelled")
        showToast("Alarm cancelled")
    }
    
    private fun startLocationTracking() {
        if (hasLocationPermissions()) {
            val intent = Intent(this, LocationService::class.java).apply {
                action = "START_TRACKING"
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            Log.d(TAG, "Location tracking started")
            showToast("Location tracking started")
        } else {
            requestLocationPermissions()
        }
    }
    
    private fun stopLocationTracking() {
        val intent = Intent(this, LocationService::class.java).apply {
            action = "STOP_TRACKING"
        }
        startService(intent)
        Log.d(TAG, "Location tracking stopped")
        showToast("Location tracking stopped")
    }
    
    private fun testNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID_GENERAL)
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(5, notification)
        
        Log.d(TAG, "Test notification sent")
        showToast("Test notification sent")
    }
    
    private fun showWorkStatus() {
        workManager.getWorkInfosForUniqueWorkLiveData(SyncWorker.WORK_NAME)
            .observe(this) { workInfos ->
                val status = workInfos.firstOrNull()?.state?.name ?: "No work found"
                Log.d(TAG, "Work status: $status")
                showToast("Work status: $status")
            }
    }
    
    private fun requestPermissions() {
        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    
    private fun requestLocationPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }
    
    private fun hasLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Location permission granted")
                    showToast("Location permission granted")
                } else {
                    Log.d(TAG, "Location permission denied")
                    showToast("Location permission denied")
                }
            }
            NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Notification permission granted")
                    showToast("Notification permission granted")
                } else {
                    Log.d(TAG, "Notification permission denied")
                    showToast("Notification permission denied")
                }
            }
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
```

### Step 7: Update Layout File

**File**: `activity_main.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Background Processing Demo"
            android:textSize="24sp"
            android:textStyle="bold"
            android:gravity="center"
            android:layout_marginBottom="24dp" />

        <!-- Foreground Service Section -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Foreground Service"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnStartForegroundService"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Start Foreground Service"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnStopForegroundService"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Stop Foreground Service"
            android:layout_marginBottom="16dp" />

        <!-- WorkManager Section -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="WorkManager"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnScheduleWork"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Schedule Periodic Work"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnCancelWork"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Cancel Periodic Work"
            android:layout_marginBottom="16dp" />

        <!-- AlarmManager Section -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="AlarmManager"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnScheduleAlarm"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Schedule Alarm (1 min)"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnCancelAlarm"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Cancel Alarm"
            android:layout_marginBottom="16dp" />

        <!-- Location Service Section -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Location Tracking"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnStartLocationTracking"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Start Location Tracking"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnStopLocationTracking"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Stop Location Tracking"
            android:layout_marginBottom="16dp" />

        <!-- Test Section -->
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Testing"
            android:textSize="18sp"
            android:textStyle="bold"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnTestNotification"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Test Notification"
            android:layout_marginBottom="8dp" />

        <Button
            android:id="@+id/btnShowWorkStatus"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Show Work Status"
            android:layout_marginBottom="16dp" />

    </LinearLayout>
</ScrollView>
```

### Step 8: Update AndroidManifest.xml

**File**: `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.BackgroundProcesses"
        tools:targetApi="31">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.BackgroundProcesses">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Services -->
        <service
            android:name=".ForegroundService"
            android:enabled="true"
            android:exported="false" />

        <service
            android:name=".LocationService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="location" />

        <!-- Receivers -->
        <receiver
            android:name=".AlarmReceiver"
            android:enabled="true"
            android:exported="false" />

        <receiver
            android:name=".NotificationActionReceiver"
            android:enabled="true"
            android:exported="false" />

    </application>
</manifest>
```

## Testing the Application

### 1. Build and Run
1. Connect a physical device or start an emulator
2. Build and run the application
3. Grant all requested permissions when prompted

### 2. Test Foreground Service
1. Tap "Start Foreground Service"
2. Verify that a persistent notification appears
3. Check logcat for service lifecycle messages
4. Tap "Stop Foreground Service" to stop the service

### 3. Test WorkManager
1. Tap "Schedule Periodic Work"
2. Wait for the periodic work to execute (minimum 15 minutes)
3. Check logcat for work execution messages
4. Verify that sync notifications appear

### 4. Test AlarmManager
1. Tap "Schedule Alarm (1 min)"
2. Wait for 1 minute or put the app in background
3. Verify that the alarm notification appears
4. Check that the next alarm is automatically scheduled

### 5. Test Location Tracking
1. Grant location permissions when prompted
2. Tap "Start Location Tracking"
3. Verify that location service notification appears
4. Check logcat for location updates
5. Tap "Stop Location Tracking" to stop the service

### 6. Test Notifications
1. Tap "Test Notification" to send a test notification
2. Verify that notifications appear in the notification drawer
3. Test notification actions and interactions

### 7. Background Testing
1. Start the foreground service
2. Put the app in background (press home button)
3. Verify that the service continues running
4. Check that notifications still appear

## Troubleshooting

### Common Issues

1. **Service not starting in background (Android 8.0+)**
   - Ensure you're using `startForegroundService()` for Android 8.0+
   - Verify that the service starts a foreground notification immediately

2. **Location permissions not working**
   - Check that location permissions are granted
   - For Android 10+, ensure background location permission is granted
   - Verify that location services are enabled on the device

3. **WorkManager not executing**
   - Check that constraints are met (network, battery, etc.)
   - Verify that the minimum interval for periodic work is 15 minutes
   - Check logcat for work execution messages

4. **Notifications not appearing**
   - Verify that notification permissions are granted (Android 13+)
   - Check that notification channels are created
   - Ensure the app is not in battery optimization mode

### Debug Commands

```bash
# Check running services
adb shell dumpsys activity services

# Check WorkManager status
adb shell dumpsys jobscheduler

# Check notification channels
adb shell dumpsys notification

# Check location services
adb shell dumpsys location

# Monitor logcat for app logs
adb logcat | grep "BackgroundProcesses"
```

## Extensions and Enhancements

### 1. Add Data Persistence
- Implement Room database to store location data
- Add data synchronization with a remote server
- Create a data visualization screen

### 2. Enhanced Notifications
- Add notification actions for quick responses
- Implement notification groups for better organization
- Add rich notification content (images, progress bars)

### 3. Advanced WorkManager Features
- Implement chained work for complex workflows
- Add custom constraints for specific requirements
- Use WorkManager for file uploads/downloads

### 4. Location Enhancements
- Add geofencing capabilities
- Implement location-based notifications
- Add location history and tracking analytics

### 5. Battery Optimization
- Implement adaptive battery optimization handling
- Add battery usage monitoring
- Create power-efficient background processing

### 6. Testing Improvements
- Add unit tests for all components
- Implement integration tests for background processing
- Add UI tests for the main activity

## Summary

This hands-on lab provides a comprehensive introduction to Android background processing concepts. By completing this lab, you will have:

1. **Implemented a complete foreground service** with persistent notifications
2. **Used WorkManager** for reliable background task scheduling
3. **Scheduled alarms** using AlarmManager for exact timing
4. **Tracked location** in the background with proper permissions
5. **Created notification channels** for different notification types
6. **Handled runtime permissions** for location and notifications
7. **Tested background processing** on real devices

The application demonstrates best practices for background processing while respecting Android's battery optimization and background restrictions. This foundation can be extended to build more complex background processing applications.
