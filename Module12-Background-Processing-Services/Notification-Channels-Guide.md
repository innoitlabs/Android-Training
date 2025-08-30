# Android Notification Channels & User Notifications Guide

## Table of Contents
1. [Introduction to Notification Channels](#introduction-to-notification-channels)
2. [Creating Notification Channels](#creating-notification-channels)
3. [Notification Channel Properties](#notification-channel-properties)
4. [Building Notifications](#building-notifications)
5. [Foreground Service Notifications](#foreground-service-notifications)
6. [Notification Actions](#notification-actions)
7. [Notification Styles](#notification-styles)
8. [Notification Groups](#notification-groups)
9. [Notification Badges](#notification-badges)
10. [Best Practices](#best-practices)
11. [Testing Notifications](#testing-notifications)

## Introduction to Notification Channels

Notification channels were introduced in Android 8.0 (API 26) to give users more control over the types of notifications they receive from apps. Each channel represents a category of notifications that can be configured independently.

### Key Concepts
- **Notification Channel**: A category of notifications with specific settings
- **Importance Level**: Controls how the notification is displayed and behaves
- **User Control**: Users can customize channel settings in system settings
- **Foreground Service Requirement**: Foreground services must show a notification

## Creating Notification Channels

### Basic Channel Creation

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
            
            // General channel
            val generalChannel = NotificationChannel(
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
            
            // Foreground service channel
            val foregroundChannel = NotificationChannel(
                CHANNEL_ID_FOREGROUND,
                "Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for foreground services"
                setShowBadge(false)
            }
            
            // Alerts channel
            val alertsChannel = NotificationChannel(
                CHANNEL_ID_ALERTS,
                "Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important alerts and warnings"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                setSound(Settings.System.DEFAULT_NOTIFICATION_URI, AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build())
            }
            
            // Updates channel
            val updatesChannel = NotificationChannel(
                CHANNEL_ID_UPDATES,
                "Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "App updates and sync notifications"
                enableLights(true)
                lightColor = Color.GREEN
            }
            
            notificationManager.createNotificationChannels(
                listOf(generalChannel, foregroundChannel, alertsChannel, updatesChannel)
            )
        }
    }
}
```

### Channel Creation in Application Class

```kotlin
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
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
            "general_channel",
            "General",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "General notifications"
            enableLights(true)
            lightColor = Color.BLUE
        }
    }
    
    private fun createForegroundChannel(): NotificationChannel {
        return NotificationChannel(
            "foreground_channel",
            "Foreground Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Foreground service notifications"
            setShowBadge(false)
        }
    }
    
    private fun createAlertsChannel(): NotificationChannel {
        return NotificationChannel(
            "alerts_channel",
            "Alerts",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Important alerts"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
    }
    
    private fun createUpdatesChannel(): NotificationChannel {
        return NotificationChannel(
            "updates_channel",
            "Updates",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "App updates"
            enableLights(true)
            lightColor = Color.GREEN
        }
    }
}
```

## Notification Channel Properties

### Importance Levels

```kotlin
enum class NotificationImportance(val importance: Int, val description: String) {
    NONE(NotificationManager.IMPORTANCE_NONE, "No sound or visual interruption"),
    MIN(NotificationManager.IMPORTANCE_MIN, "Minimal sound and visual interruption"),
    LOW(NotificationManager.IMPORTANCE_LOW, "Low priority notifications"),
    DEFAULT(NotificationManager.IMPORTANCE_DEFAULT, "Default importance"),
    HIGH(NotificationManager.IMPORTANCE_HIGH, "High priority notifications"),
    MAX(NotificationManager.IMPORTANCE_MAX, "Maximum importance")
}

class ChannelConfigurator {
    
    fun createChannelWithImportance(channelId: String, name: String, importance: NotificationImportance): NotificationChannel {
        return NotificationChannel(channelId, name, importance.importance).apply {
            description = importance.description
            configureForImportance(importance)
        }
    }
    
    private fun NotificationChannel.configureForImportance(importance: NotificationImportance) {
        when (importance) {
            NotificationImportance.NONE -> {
                enableLights(false)
                enableVibration(false)
                setSound(null, null)
            }
            NotificationImportance.MIN -> {
                enableLights(false)
                enableVibration(false)
                setSound(null, null)
            }
            NotificationImportance.LOW -> {
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(false)
                setSound(null, null)
            }
            NotificationImportance.DEFAULT -> {
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 250, 250)
                setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null)
            }
            NotificationImportance.HIGH -> {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null)
            }
            NotificationImportance.MAX -> {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null)
            }
        }
    }
}
```

### Advanced Channel Configuration

```kotlin
class AdvancedChannelConfigurator {
    
    fun createAdvancedChannel(channelId: String, name: String): NotificationChannel {
        return NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH).apply {
            // Basic settings
            description = "Advanced notification channel"
            
            // Visual settings
            enableLights(true)
            lightColor = Color.CYAN
            setShowBadge(true)
            
            // Sound settings
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()
            setSound(Settings.System.DEFAULT_NOTIFICATION_URI, audioAttributes)
            
            // Vibration settings
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 200, 100, 200, 100, 200)
            
            // Lock screen settings
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            
            // Allow bypass DND
            setBypassDnd(true)
        }
    }
}
```

## Building Notifications

### Basic Notification

```kotlin
class NotificationBuilder(private val context: Context) {
    
    fun createBasicNotification(
        title: String,
        content: String,
        channelId: String = NotificationHelper.CHANNEL_ID_GENERAL
    ): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    
    fun createNotificationWithActions(
        title: String,
        content: String,
        channelId: String = NotificationHelper.CHANNEL_ID_GENERAL
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Action 1: Open app
        val openIntent = Intent(context, MainActivity::class.java)
        val openPendingIntent = PendingIntent.getActivity(
            context,
            1,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Action 2: Dismiss
        val dismissIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "DISMISS"
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Open", openPendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Dismiss", dismissPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
```

### Progress Notification

```kotlin
class ProgressNotificationBuilder(private val context: Context) {
    
    fun createProgressNotification(
        title: String,
        content: String,
        progress: Int,
        max: Int,
        indeterminate: Boolean = false,
        channelId: String = NotificationHelper.CHANNEL_ID_UPDATES
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setProgress(max, progress, indeterminate)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    fun updateProgressNotification(
        notificationId: Int,
        title: String,
        content: String,
        progress: Int,
        max: Int,
        indeterminate: Boolean = false,
        channelId: String = NotificationHelper.CHANNEL_ID_UPDATES
    ) {
        val notification = createProgressNotification(title, content, progress, max, indeterminate, channelId)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}
```

## Foreground Service Notifications

### Foreground Service Notification

```kotlin
class ForegroundServiceNotificationBuilder(private val context: Context) {
    
    fun createForegroundNotification(
        title: String,
        content: String,
        channelId: String = NotificationHelper.CHANNEL_ID_FOREGROUND
    ): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Stop service action
        val stopIntent = Intent(context, ForegroundService::class.java).apply {
            action = "STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getService(
            context,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
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
    
    fun createLocationTrackingNotification(
        latitude: Double,
        longitude: Double,
        channelId: String = NotificationHelper.CHANNEL_ID_FOREGROUND
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Location Tracking Active")
            .setContentText("Current location: $latitude, $longitude")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
}
```

## Notification Actions

### Action Receiver

```kotlin
class NotificationActionReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "DISMISS" -> {
                // Handle dismiss action
                Log.d("NotificationAction", "Dismiss action received")
            }
            "ACCEPT" -> {
                // Handle accept action
                Log.d("NotificationAction", "Accept action received")
            }
            "DECLINE" -> {
                // Handle decline action
                Log.d("NotificationAction", "Decline action received")
            }
        }
    }
}
```

### Action Builder

```kotlin
class NotificationActionBuilder(private val context: Context) {
    
    fun createActionNotification(
        title: String,
        content: String,
        actions: List<NotificationAction>,
        channelId: String = NotificationHelper.CHANNEL_ID_ALERTS
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        
        // Add actions
        actions.forEach { action ->
            val actionIntent = Intent(context, NotificationActionReceiver::class.java).apply {
                this.action = action.action
                putExtra("notification_id", action.notificationId)
            }
            
            val actionPendingIntent = PendingIntent.getBroadcast(
                context,
                action.requestCode,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            builder.addAction(action.icon, action.title, actionPendingIntent)
        }
        
        return builder.build()
    }
    
    data class NotificationAction(
        val title: String,
        val action: String,
        val icon: Int,
        val requestCode: Int,
        val notificationId: Int
    )
}
```

## Notification Styles

### Big Text Style

```kotlin
class BigTextNotificationBuilder(private val context: Context) {
    
    fun createBigTextNotification(
        title: String,
        content: String,
        bigText: String,
        channelId: String = NotificationHelper.CHANNEL_ID_GENERAL
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(bigText)
            .setBigContentTitle(title)
            .setSummaryText("Summary")
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setStyle(bigTextStyle)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
```

### Inbox Style

```kotlin
class InboxNotificationBuilder(private val context: Context) {
    
    fun createInboxNotification(
        title: String,
        content: String,
        lines: List<String>,
        channelId: String = NotificationHelper.CHANNEL_ID_UPDATES
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle(title)
            .setSummaryText("${lines.size} new items")
        
        lines.forEach { line ->
            inboxStyle.addLine(line)
        }
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setStyle(inboxStyle)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
}
```

## Notification Groups

### Group Notifications

```kotlin
class GroupNotificationBuilder(private val context: Context) {
    
    companion object {
        const val GROUP_KEY_MESSAGES = "messages_group"
        const val GROUP_KEY_UPDATES = "updates_group"
    }
    
    fun createGroupedNotifications(
        messages: List<Message>,
        channelId: String = NotificationHelper.CHANNEL_ID_GENERAL
    ) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Create individual notifications
        messages.forEachIndexed { index, message ->
            val notification = createMessageNotification(message, index, channelId)
            notificationManager.notify(index, notification)
        }
        
        // Create summary notification
        val summaryNotification = createSummaryNotification(messages.size, channelId)
        notificationManager.notify(999, summaryNotification)
    }
    
    private fun createMessageNotification(
        message: Message,
        notificationId: Int,
        channelId: String
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(message.sender)
            .setContentText(message.content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_MESSAGES)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    
    private fun createSummaryNotification(
        messageCount: Int,
        channelId: String
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle("New Messages")
            .setSummaryText("$messageCount new messages")
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("New Messages")
            .setContentText("$messageCount new messages")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setStyle(inboxStyle)
            .setGroup(GROUP_KEY_MESSAGES)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    
    data class Message(
        val sender: String,
        val content: String,
        val timestamp: Long
    )
}
```

## Notification Badges

### Badge Configuration

```kotlin
class BadgeNotificationBuilder(private val context: Context) {
    
    fun createBadgeNotification(
        title: String,
        content: String,
        badgeCount: Int,
        channelId: String = NotificationHelper.CHANNEL_ID_ALERTS
    ): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setNumber(badgeCount)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    
    fun updateBadgeCount(notificationId: Int, badgeCount: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = notificationManager.activeNotifications.find { it.id == notificationId }
        
        notification?.let {
            val updatedNotification = NotificationCompat.Builder(context, it.notification)
                .setNumber(badgeCount)
                .build()
            
            notificationManager.notify(notificationId, updatedNotification)
        }
    }
}
```

## Best Practices

### 1. Channel Management

```kotlin
class NotificationChannelManager(private val context: Context) {
    
    fun createChannelsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            // Check if channels already exist
            val existingChannels = notificationManager.notificationChannels
            val channelIds = existingChannels.map { it.id }
            
            // Create missing channels
            val requiredChannels = getRequiredChannels()
            val missingChannels = requiredChannels.filter { !channelIds.contains(it.id) }
            
            if (missingChannels.isNotEmpty()) {
                notificationManager.createNotificationChannels(missingChannels)
            }
        }
    }
    
    private fun getRequiredChannels(): List<NotificationChannel> {
        return listOf(
            createGeneralChannel(),
            createForegroundChannel(),
            createAlertsChannel(),
            createUpdatesChannel()
        )
    }
    
    fun deleteChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.deleteNotificationChannel(channelId)
        }
    }
}
```

### 2. Notification Permission Handling

```kotlin
class NotificationPermissionHelper {
    
    fun areNotificationsEnabled(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.areNotificationsEnabled()
    }
    
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }
    
    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1003
    }
}
```

### 3. Notification Cleanup

```kotlin
class NotificationCleanupHelper(private val context: Context) {
    
    fun cancelNotification(notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
    
    fun cancelAllNotifications() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
    
    fun cancelNotificationsByTag(tag: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(tag, 0)
    }
}
```

## Testing Notifications

### Notification Testing

```kotlin
class NotificationTester(private val context: Context) {
    
    fun testAllNotificationTypes() {
        val notificationHelper = NotificationHelper(context)
        val notificationBuilder = NotificationBuilder(context)
        
        // Test basic notification
        val basicNotification = notificationBuilder.createBasicNotification(
            "Test Notification",
            "This is a test notification"
        )
        showNotification(1, basicNotification)
        
        // Test progress notification
        val progressBuilder = ProgressNotificationBuilder(context)
        val progressNotification = progressBuilder.createProgressNotification(
            "Download Progress",
            "Downloading file...",
            50,
            100
        )
        showNotification(2, progressNotification)
        
        // Test action notification
        val actions = listOf(
            NotificationActionBuilder.NotificationAction(
                "Accept",
                "ACCEPT",
                R.drawable.ic_launcher_foreground,
                1,
                3
            ),
            NotificationActionBuilder.NotificationAction(
                "Decline",
                "DECLINE",
                R.drawable.ic_launcher_foreground,
                2,
                3
            )
        )
        val actionBuilder = NotificationActionBuilder(context)
        val actionNotification = actionBuilder.createActionNotification(
            "Action Required",
            "Please respond to this notification",
            actions
        )
        showNotification(3, actionNotification)
    }
    
    private fun showNotification(notificationId: Int, notification: Notification) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }
}
```

## Summary

Notification channels and user notifications are essential for modern Android development. Key points to remember:

1. **Create notification channels** for Android 8.0+
2. **Use appropriate importance levels** for different notification types
3. **Implement foreground service notifications** for background services
4. **Handle notification permissions** properly
5. **Use notification styles** to enhance user experience
6. **Group related notifications** for better organization
7. **Test notifications** thoroughly on different Android versions
8. **Follow platform guidelines** for notification design

Proper notification implementation improves user experience and ensures your app works correctly across all Android versions.
