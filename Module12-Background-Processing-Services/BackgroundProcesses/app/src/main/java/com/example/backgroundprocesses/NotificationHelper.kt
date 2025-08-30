package com.example.backgroundprocesses

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID_GENERAL,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General app notifications"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
        } else {
            throw UnsupportedOperationException("NotificationChannel requires API 26+")
        }
    }
    
    private fun createForegroundChannel(): NotificationChannel {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID_FOREGROUND,
                "Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Foreground service notifications"
                setShowBadge(false)
            }
        } else {
            throw UnsupportedOperationException("NotificationChannel requires API 26+")
        }
    }
    
    private fun createAlertsChannel(): NotificationChannel {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID_ALERTS,
                "Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important alerts and warnings"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            }
        } else {
            throw UnsupportedOperationException("NotificationChannel requires API 26+")
        }
    }
    
    private fun createUpdatesChannel(): NotificationChannel {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID_UPDATES,
                "Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "App updates and sync notifications"
                enableLights(true)
                lightColor = Color.GREEN
            }
        } else {
            throw UnsupportedOperationException("NotificationChannel requires API 26+")
        }
    }
}
