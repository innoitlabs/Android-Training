package com.example.backgroundprocesses

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            this,
            AlarmReceiver.ALARM_REQUEST_CODE,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        
        // Schedule alarm for 1 minute from now
        val alarmTime = System.currentTimeMillis() + (60 * 1000)
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                android.app.AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                android.app.AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        }
        
        Log.d(TAG, "Alarm scheduled for: ${java.util.Date(alarmTime)}")
        showToast("Alarm scheduled for 1 minute from now")
    }
    
    private fun cancelAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            this,
            AlarmReceiver.ALARM_REQUEST_CODE,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        val pendingIntent = android.app.PendingIntent.getActivity(
            this,
            0,
            intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = androidx.core.app.NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID_GENERAL)
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(androidx.core.app.NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
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