# Android AlarmManager Scheduling Guide

## Overview
AlarmManager is used for scheduling exact timing requirements in Android applications. This guide covers how to implement scheduled tasks using AlarmManager.

## Key Concepts

### 1. Alarm Types
- **RTC_WAKEUP**: Real-time clock, wakes up device
- **RTC**: Real-time clock, doesn't wake up device
- **ELAPSED_REALTIME_WAKEUP**: Time since boot, wakes up device
- **ELAPSED_REALTIME**: Time since boot, doesn't wake up device

### 2. Exact vs Inexact Alarms
- **Exact alarms**: Trigger at precise time
- **Inexact alarms**: System may batch for battery optimization

## Implementation

### 1. Basic Alarm Scheduling
```kotlin
class AlarmScheduler {
    
    fun scheduleAlarm(context: Context, triggerTime: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }
}
```

### 2. Repeating Alarms
```kotlin
fun scheduleRepeatingAlarm(context: Context, interval: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis(),
        interval,
        pendingIntent
    )
}
```

### 3. Alarm Receiver
```kotlin
class AlarmReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Alarm triggered")
        
        // Show notification
        showNotification(context)
        
        // Schedule next alarm if needed
        scheduleNextAlarm(context)
    }
    
    private fun showNotification(context: Context) {
        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setContentTitle("Scheduled Task")
            .setContentText("Your scheduled task is ready!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}
```

## Best Practices

1. **Use exact alarms sparingly** to preserve battery
2. **Handle alarm cancellation** properly
3. **Consider WorkManager** for deferrable tasks
4. **Test on different Android versions**
5. **Handle device reboots** if needed

## Summary
AlarmManager is powerful for exact timing requirements but should be used judiciously. Consider WorkManager for most background tasks and reserve AlarmManager for critical timing needs.
