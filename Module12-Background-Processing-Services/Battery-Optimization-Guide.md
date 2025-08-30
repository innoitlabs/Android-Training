# Android Battery Optimization Guide

## Overview
Battery optimization is crucial for Android apps to provide a good user experience and comply with platform guidelines. This guide covers techniques for optimizing battery usage in background processing.

## Key Concepts

### 1. Doze Mode
- Device enters Doze mode when stationary and unplugged
- Network access is suspended
- Wake locks are ignored
- Sync operations are delayed

### 2. App Standby
- Apps enter standby after being unused for several days
- Network access is restricted
- Background sync is limited
- JobScheduler jobs are deferred

### 3. Battery Optimization
- System learns user patterns
- Restricts background activity
- Provides adaptive battery management

## Implementation Strategies

### 1. Check Battery Optimization Status
```kotlin
class BatteryOptimizationHelper {
    
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val packageName = context.packageName
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager.isIgnoringBatteryOptimizations(packageName)
        } else {
            true
        }
    }
    
    fun requestIgnoreBatteryOptimizations(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:${activity.packageName}")
            }
            activity.startActivity(intent)
        }
    }
}
```

### 2. Adaptive Background Processing
```kotlin
class BatteryOptimizedWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork(): Result {
        val batteryLevel = getBatteryLevel()
        val isOptimized = isBatteryOptimized()
        
        return when {
            batteryLevel < 20 -> {
                Log.d("BatteryWorker", "Low battery, deferring work")
                Result.retry()
            }
            isOptimized && batteryLevel < 50 -> {
                Log.d("BatteryWorker", "Battery optimized and low, deferring work")
                Result.retry()
            }
            else -> {
                performWork()
                Result.success()
            }
        }
    }
    
    private fun getBatteryLevel(): Int {
        val batteryIntent = applicationContext.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        
        return if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            100
        }
    }
}
```

### 3. WorkManager with Battery Constraints
```kotlin
val constraints = Constraints.Builder()
    .setRequiresBatteryNotLow(true)
    .setRequiresCharging(false)
    .build()

val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
    .setConstraints(constraints)
    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.MINUTES)
    .build()

WorkManager.getInstance(context).enqueue(workRequest)
```

## Best Practices

1. **Use WorkManager** for deferrable background work
2. **Respect battery constraints** and system restrictions
3. **Implement exponential backoff** for failed operations
4. **Request battery optimization exceptions** only when necessary
5. **Test on real devices** with different battery levels

## Testing Battery Behavior

```bash
# Enable Doze mode
adb shell dumpsys battery unplug
adb shell dumpsys deviceidle step

# Check Doze mode state
adb shell dumpsys deviceidle

# Exit Doze mode
adb shell dumpsys deviceidle disable
```

## Summary
Effective battery optimization requires understanding system restrictions and implementing adaptive processing strategies. Focus on user experience while respecting platform guidelines.
