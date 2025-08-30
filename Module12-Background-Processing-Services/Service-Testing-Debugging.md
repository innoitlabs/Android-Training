# Android Service Testing & Debugging Guide

## Overview
Testing and debugging Android services requires specific approaches due to their background nature and lifecycle management. This guide covers comprehensive testing strategies and debugging techniques.

## Testing Strategies

### 1. Unit Testing Services
```kotlin
@RunWith(AndroidJUnit4::class)
class ServiceTest {
    
    @get:Rule
    val serviceTestRule = ServiceTestRule()
    
    @Test
    fun testServiceLifecycle() {
        val serviceIntent = Intent(ApplicationProvider.getApplicationContext(), MyService::class.java)
        
        serviceTestRule.startService(serviceIntent)
        
        // Verify service is running
        assertThat(serviceTestRule.getService()).isNotNull()
        
        // Test service functionality
        val service = serviceTestRule.getService() as MyService
        assertThat(service.isRunning()).isTrue()
    }
}
```

### 2. WorkManager Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class WorkManagerTest {
    
    @get:Rule
    val workManagerRule = WorkManagerTestInitHelper.WorkManagerTestRule()
    
    @Test
    fun testWorkerExecution() {
        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        
        WorkManager.getInstance(ApplicationProvider.getApplicationContext())
            .enqueue(workRequest)
        
        val workInfo = workManagerRule.workInfosByTag("").get()
        assertThat(workInfo.size).isEqualTo(1)
        assertThat(workInfo[0].state).isEqualTo(WorkInfo.State.SUCCEEDED)
    }
}
```

### 3. Integration Testing
```kotlin
@RunWith(AndroidJUnit4::class)
class ServiceIntegrationTest {
    
    @Test
    fun testServiceWithWorkManager() {
        // Start service
        val serviceIntent = Intent(ApplicationProvider.getApplicationContext(), ForegroundService::class.java)
        val serviceTestRule = ServiceTestRule()
        serviceTestRule.startService(serviceIntent)
        
        // Schedule work
        val workRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(ApplicationProvider.getApplicationContext()).enqueue(workRequest)
        
        // Verify both service and work are running
        assertThat(serviceTestRule.getService()).isNotNull()
        // Additional assertions...
    }
}
```

## Debugging Techniques

### 1. Logging Service Lifecycle
```kotlin
class DebuggableService : Service() {
    
    companion object {
        private const val TAG = "DebuggableService"
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service started with startId: $startId")
        Log.d(TAG, "Intent action: ${intent?.action}")
        Log.d(TAG, "Intent extras: ${intent?.extras}")
        
        return START_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
    }
}
```

### 2. ADB Commands for Debugging
```bash
# Check running services
adb shell dumpsys activity services

# Check WorkManager status
adb shell dumpsys jobscheduler

# Check notification channels
adb shell dumpsys notification

# Monitor logcat for specific tags
adb logcat | grep "MyService\|WorkManager\|BackgroundProcesses"

# Check battery optimization status
adb shell dumpsys deviceidle
```

### 3. Debugging Background Restrictions
```kotlin
class BackgroundDebugHelper {
    
    fun debugBackgroundState(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        
        val packageName = context.packageName
        for (processInfo in appProcesses) {
            if (processInfo.processName == packageName) {
                Log.d("BackgroundDebug", "App importance: ${processInfo.importance}")
                Log.d("BackgroundDebug", "App PID: ${processInfo.pid}")
                break
            }
        }
        
        // Check if app is in Doze mode
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("BackgroundDebug", "Device idle mode: ${powerManager.isDeviceIdleMode}")
        }
    }
}
```

## Common Issues and Solutions

### 1. Service Not Starting in Background
**Issue**: Service fails to start when app is in background (Android 8.0+)
**Solution**: Use `startForegroundService()` and show notification immediately

```kotlin
// Correct approach
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    startForegroundService(intent)
} else {
    startService(intent)
}

// In service
override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val notification = createNotification()
    startForeground(NOTIFICATION_ID, notification)
    return START_STICKY
}
```

### 2. WorkManager Not Executing
**Issue**: WorkManager jobs don't run as expected
**Solution**: Check constraints and scheduling

```kotlin
// Debug WorkManager
WorkManager.getInstance(context)
    .getWorkInfosForUniqueWorkLiveData("work_name")
    .observe(this) { workInfos ->
        workInfos.forEach { workInfo ->
            Log.d("WorkManagerDebug", "Work state: ${workInfo.state}")
            Log.d("WorkManagerDebug", "Work tags: ${workInfo.tags}")
            Log.d("WorkManagerDebug", "Work constraints: ${workInfo.constraints}")
        }
    }
```

### 3. Location Service Issues
**Issue**: Location updates stop working in background
**Solution**: Ensure foreground service and proper permissions

```kotlin
// Check location permissions
private fun hasLocationPermissions(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

// Handle permission denials
try {
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    )
} catch (e: SecurityException) {
    Log.e("LocationService", "Permission denied", e)
    stopSelf()
}
```

## Performance Testing

### 1. Memory Leak Detection
```kotlin
class MemoryLeakDetector {
    
    fun checkForMemoryLeaks(service: Service) {
        // Use weak references to avoid memory leaks
        val weakReference = WeakReference(service)
        
        // Force garbage collection
        System.gc()
        
        // Check if service is still referenced
        if (weakReference.get() == null) {
            Log.d("MemoryLeakDetector", "Service properly garbage collected")
        } else {
            Log.w("MemoryLeakDetector", "Potential memory leak detected")
        }
    }
}
```

### 2. Battery Impact Testing
```kotlin
class BatteryImpactTest {
    
    fun measureBatteryImpact(context: Context) {
        val batteryIntent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        
        val initialLevel = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        
        // Run service for some time
        Thread.sleep(60000) // 1 minute
        
        val finalBatteryIntent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val finalLevel = finalBatteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        
        val batteryDrain = initialLevel - finalLevel
        Log.d("BatteryTest", "Battery drain: $batteryDrain%")
    }
}
```

## Best Practices

1. **Use comprehensive logging** for service lifecycle events
2. **Test on real devices** with different Android versions
3. **Monitor system resources** during service execution
4. **Handle edge cases** like low memory and battery optimization
5. **Use ADB commands** for system-level debugging
6. **Implement proper error handling** and recovery mechanisms

## Summary
Effective service testing and debugging requires a combination of unit tests, integration tests, and system-level debugging tools. Focus on understanding the service lifecycle and system restrictions to build robust background processing applications.
