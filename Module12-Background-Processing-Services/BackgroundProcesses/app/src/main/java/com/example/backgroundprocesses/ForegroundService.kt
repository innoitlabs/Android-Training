package com.example.backgroundprocesses

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
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
