# Android Services and Background Tasks - Practice Exercises

## Easy Exercises

### Exercise 1: Basic Started Service
Create a simple started service that logs messages in lifecycle methods.

### Exercise 2: Notification on Service Start
Show a notification when a service starts with a "Stop Service" action.

### Exercise 3: Simple WorkManager Worker
Create a basic WorkManager worker that simulates work for 5 seconds.

## Intermediate Exercises

### Exercise 4: WorkManager with Wi-Fi Constraint
Create a WorkManager job that only runs when connected to Wi-Fi.

### Exercise 5: Bound Service with Data Sharing
Create a bound service that shares data with the activity.

### Exercise 6: Progress Notification
Create a service that shows progress in a notification from 0% to 100%.

### Exercise 7: AlarmManager with Custom Time
Schedule an alarm for a specific time using TimePicker.

## Advanced Exercises

### Exercise 8: Foreground Service with Location Updates
Create a foreground service that tracks location in background.

### Exercise 9: WorkManager Chain with Data Passing
Create a chain of three workers that pass data between them.

### Exercise 10: Background Sync with Conflict Resolution
Implement a background sync system with conflict resolution.

### Exercise 11: Battery-Optimized Background Processing
Create a background processing system that respects battery optimization.

### Exercise 12: Comprehensive Background App
Build a complete background processing application combining all concepts.

## Sample Solutions

### Basic Started Service
```kotlin
class BasicService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d("BasicService", "Service created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BasicService", "Service started")
        return START_NOT_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d("BasicService", "Service destroyed")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
```

### WorkManager with Constraint
```kotlin
class NetworkWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        // Simulate network work
        Thread.sleep(3000)
        return Result.success()
    }
}

// Schedule with Wi-Fi constraint
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.UNMETERED)
    .build()

val workRequest = OneTimeWorkRequestBuilder<NetworkWorker>()
    .setConstraints(constraints)
    .build()

WorkManager.getInstance(context).enqueue(workRequest)
```

### Progress Notification
```kotlin
class ProgressService : Service() {
    private fun updateProgress(progress: Int) {
        val notification = NotificationCompat.Builder(this, "progress_channel")
            .setContentTitle("Progress Service")
            .setContentText("Progress: $progress%")
            .setProgress(100, progress, false)
            .setOngoing(true)
            .build()
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}
```

Complete all exercises to master Android background processing concepts!
