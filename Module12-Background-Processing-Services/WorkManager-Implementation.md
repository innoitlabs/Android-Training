# WorkManager Implementation Guide

## Table of Contents
1. [Introduction to WorkManager](#introduction-to-workmanager)
2. [WorkManager vs Other Solutions](#workmanager-vs-other-solutions)
3. [Basic WorkManager Implementation](#basic-workmanager-implementation)
4. [Work Types](#work-types)
5. [Constraints](#constraints)
6. [Chained Work](#chained-work)
7. [Periodic Work](#periodic-work)
8. [Input/Output Data](#inputoutput-data)
9. [Work Status and Observing](#work-status-and-observing)
10. [Best Practices](#best-practices)
11. [Testing WorkManager](#testing-workmanager)

## Introduction to WorkManager

WorkManager is the recommended solution for deferrable background work that needs to be executed reliably. It's part of Android Jetpack and provides a consistent API for scheduling background work across all Android versions.

### Key Features
- **Guaranteed Execution**: WorkManager ensures your work runs even if the app is killed
- **Battery Optimization**: Respects system battery optimization policies
- **Flexible Scheduling**: One-time, periodic, and chained work
- **Constraints**: Network, battery, storage, and custom constraints
- **Observable**: Track work status and progress

## WorkManager vs Other Solutions

| Solution | Use Case | Guaranteed Execution | Battery Optimized |
|----------|----------|---------------------|-------------------|
| **WorkManager** | Deferrable background work | ✅ Yes | ✅ Yes |
| **Service** | Immediate background work | ❌ No | ❌ No |
| **AlarmManager** | Exact timing requirements | ✅ Yes | ❌ No |
| **JobScheduler** | System-managed jobs | ✅ Yes | ✅ Yes |

## Basic WorkManager Implementation

### 1. Add Dependencies

```kotlin
// In app/build.gradle.kts
dependencies {
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.work:work-multiprocess:2.9.0") // For multi-process apps
}
```

### 2. Create a Worker

```kotlin
class SimpleWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {
    
    companion object {
        private const val TAG = "SimpleWorker"
    }
    
    override fun doWork(): Result {
        Log.d(TAG, "SimpleWorker started")
        
        return try {
            // Simulate some work
            Thread.sleep(5000)
            Log.d(TAG, "SimpleWorker completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "SimpleWorker failed", e)
            Result.failure()
        }
    }
}
```

### 3. Schedule Work

```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        findViewById<Button>(R.id.btnScheduleWork).setOnClickListener {
            scheduleSimpleWork()
        }
    }
    
    private fun scheduleSimpleWork() {
        val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>()
            .build()
        
        WorkManager.getInstance(this)
            .enqueue(workRequest)
        
        Log.d("MainActivity", "Work scheduled")
    }
}
```

## Work Types

### 1. OneTimeWorkRequest
For one-time background work.

```kotlin
val oneTimeWork = OneTimeWorkRequestBuilder<SimpleWorker>()
    .build()

WorkManager.getInstance(context).enqueue(oneTimeWork)
```

### 2. PeriodicWorkRequest
For recurring background work (minimum interval: 15 minutes).

```kotlin
val periodicWork = PeriodicWorkRequestBuilder<SyncWorker>(
    repeatInterval = 1,
    repeatIntervalTimeUnit = TimeUnit.HOURS
).build()

WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "sync_work",
    ExistingPeriodicWorkPolicy.KEEP,
    periodicWork
)
```

### 3. CoroutineWorker
For Kotlin coroutines-based work.

```kotlin
class CoroutineWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    
    override suspend fun doWork(): Result {
        return try {
            // Suspend function calls
            val result = performAsyncWork()
            Log.d("CoroutineWorker", "Work completed: $result")
            Result.success()
        } catch (e: Exception) {
            Log.e("CoroutineWorker", "Work failed", e)
            Result.failure()
        }
    }
    
    private suspend fun performAsyncWork(): String {
        delay(3000) // Simulate async work
        return "Async work completed"
    }
}
```

## Constraints

### 1. Network Constraints

```kotlin
val networkConstraint = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()

val workRequest = OneTimeWorkRequestBuilder<NetworkWorker>()
    .setConstraints(networkConstraint)
    .build()
```

### 2. Battery Constraints

```kotlin
val batteryConstraint = Constraints.Builder()
    .setRequiresBatteryNotLow(true)
    .setRequiresCharging(true)
    .build()

val workRequest = OneTimeWorkRequestBuilder<BatteryWorker>()
    .setConstraints(batteryConstraint)
    .build()
```

### 3. Storage Constraints

```kotlin
val storageConstraint = Constraints.Builder()
    .setRequiresStorageNotLow(true)
    .build()

val workRequest = OneTimeWorkRequestBuilder<StorageWorker>()
    .setConstraints(storageConstraint)
    .build()
```

### 4. Combined Constraints

```kotlin
val constraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi only
    .setRequiresBatteryNotLow(true)
    .setRequiresCharging(false)
    .setRequiresStorageNotLow(true)
    .build()

val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
    .setConstraints(constraints)
    .build()
```

## Chained Work

### Sequential Work Chain

```kotlin
class DownloadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("DownloadWorker", "Downloading file...")
        // Simulate download
        Thread.sleep(2000)
        return Result.success()
    }
}

class ProcessWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("ProcessWorker", "Processing downloaded file...")
        // Simulate processing
        Thread.sleep(1000)
        return Result.success()
    }
}

class UploadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        Log.d("UploadWorker", "Uploading processed file...")
        // Simulate upload
        Thread.sleep(1500)
        return Result.success()
    }
}

// Chain the work
val downloadWork = OneTimeWorkRequestBuilder<DownloadWorker>().build()
val processWork = OneTimeWorkRequestBuilder<ProcessWorker>().build()
val uploadWork = OneTimeWorkRequestBuilder<UploadWorker>().build()

WorkManager.getInstance(context)
    .beginWith(downloadWork)
    .then(processWork)
    .then(uploadWork)
    .enqueue()
```

### Parallel Work

```kotlin
val work1 = OneTimeWorkRequestBuilder<Worker1>().build()
val work2 = OneTimeWorkRequestBuilder<Worker2>().build()
val work3 = OneTimeWorkRequestBuilder<Worker3>().build()

WorkManager.getInstance(context)
    .beginWith(listOf(work1, work2, work3))
    .then(OneTimeWorkRequestBuilder<FinalWorker>().build())
    .enqueue()
```

## Periodic Work

### Basic Periodic Work

```kotlin
class PeriodicSyncWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork(): Result {
        Log.d("PeriodicSyncWorker", "Performing periodic sync...")
        
        return try {
            // Perform sync operation
            performSync()
            Result.success()
        } catch (e: Exception) {
            Log.e("PeriodicSyncWorker", "Sync failed", e)
            Result.retry()
        }
    }
    
    private fun performSync() {
        // Simulate sync work
        Thread.sleep(3000)
        Log.d("PeriodicSyncWorker", "Sync completed")
    }
}

// Schedule periodic work
val periodicWork = PeriodicWorkRequestBuilder<PeriodicSyncWorker>(
    repeatInterval = 1,
    repeatIntervalTimeUnit = TimeUnit.HOURS
).setConstraints(
    Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()
).build()

WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "periodic_sync",
    ExistingPeriodicWorkPolicy.KEEP,
    periodicWork
)
```

## Input/Output Data

### Passing Data to Workers

```kotlin
class DataWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork(): Result {
        // Get input data
        val inputString = inputData.getString("input_key")
        val inputInt = inputData.getInt("number_key", 0)
        
        Log.d("DataWorker", "Input string: $inputString, Input int: $inputInt")
        
        // Process the data
        val result = processData(inputString, inputInt)
        
        // Return output data
        val outputData = Data.Builder()
            .putString("result_key", result)
            .putBoolean("success_key", true)
            .build()
        
        return Result.success(outputData)
    }
    
    private fun processData(input: String?, number: Int): String {
        return "Processed: $input with number: $number"
    }
}

// Schedule work with input data
val inputData = Data.Builder()
    .putString("input_key", "Hello from MainActivity")
    .putInt("number_key", 42)
    .build()

val workRequest = OneTimeWorkRequestBuilder<DataWorker>()
    .setInputData(inputData)
    .build()

WorkManager.getInstance(context).enqueue(workRequest)
```

### Observing Work Output

```kotlin
WorkManager.getInstance(context)
    .getWorkInfoByIdLiveData(workRequest.id)
    .observe(this) { workInfo ->
        workInfo?.let {
            when (it.state) {
                WorkInfo.State.SUCCEEDED -> {
                    val outputData = it.outputData
                    val result = outputData.getString("result_key")
                    val success = outputData.getBoolean("success_key", false)
                    Log.d("MainActivity", "Work succeeded: $result, Success: $success")
                }
                WorkInfo.State.FAILED -> {
                    Log.e("MainActivity", "Work failed")
                }
                WorkInfo.State.RUNNING -> {
                    Log.d("MainActivity", "Work is running")
                }
                else -> {
                    Log.d("MainActivity", "Work state: ${it.state}")
                }
            }
        }
    }
```

## Work Status and Observing

### Observing Work Status

```kotlin
class MainActivity : AppCompatActivity() {
    
    private lateinit var workManager: WorkManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        workManager = WorkManager.getInstance(this)
        
        findViewById<Button>(R.id.btnScheduleWork).setOnClickListener {
            scheduleWorkAndObserve()
        }
    }
    
    private fun scheduleWorkAndObserve() {
        val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>()
            .build()
        
        workManager.enqueue(workRequest)
        
        // Observe work status
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                workInfo?.let {
                    updateUI(it.state)
                    Log.d("MainActivity", "Work state: ${it.state}")
                }
            }
    }
    
    private fun updateUI(state: WorkInfo.State) {
        val statusText = when (state) {
            WorkInfo.State.ENQUEUED -> "Work enqueued"
            WorkInfo.State.RUNNING -> "Work running"
            WorkInfo.State.SUCCEEDED -> "Work succeeded"
            WorkInfo.State.FAILED -> "Work failed"
            WorkInfo.State.BLOCKED -> "Work blocked"
            WorkInfo.State.CANCELLED -> "Work cancelled"
        }
        
        findViewById<TextView>(R.id.tvWorkStatus).text = statusText
    }
}
```

### Observing All Work

```kotlin
// Observe all work by tag
workManager.getWorkInfosByTagLiveData("sync_tag")
    .observe(this) { workInfoList ->
        workInfoList.forEach { workInfo ->
            Log.d("MainActivity", "Work ${workInfo.id}: ${workInfo.state}")
        }
    }

// Observe all work by unique name
workManager.getWorkInfosForUniqueWorkLiveData("unique_sync")
    .observe(this) { workInfoList ->
        workInfoList.forEach { workInfo ->
            Log.d("MainActivity", "Unique work ${workInfo.id}: ${workInfo.state}")
        }
    }
```

## Best Practices

### 1. Use Appropriate Work Types
```kotlin
// ✅ Use OneTimeWorkRequest for one-time tasks
val oneTimeWork = OneTimeWorkRequestBuilder<SyncWorker>().build()

// ✅ Use PeriodicWorkRequest for recurring tasks
val periodicWork = PeriodicWorkRequestBuilder<SyncWorker>(
    repeatInterval = 1,
    repeatIntervalTimeUnit = TimeUnit.HOURS
).build()

// ✅ Use CoroutineWorker for async operations
class AsyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Use suspend functions
        return Result.success()
    }
}
```

### 2. Handle Work Failures
```kotlin
class RobustWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork(): Result {
        return try {
            performWork()
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Log.w("RobustWorker", "Attempt ${runAttemptCount + 1} failed, retrying...")
                Result.retry()
            } else {
                Log.e("RobustWorker", "All attempts failed", e)
                Result.failure()
            }
        }
    }
    
    private fun performWork() {
        // Simulate work that might fail
        if (Random.nextBoolean()) {
            throw RuntimeException("Random failure")
        }
        Thread.sleep(1000)
    }
}
```

### 3. Use Unique Work for Critical Operations
```kotlin
// ✅ Use unique work to prevent duplicates
val uniqueWork = OneTimeWorkRequestBuilder<CriticalWorker>()
    .build()

workManager.enqueueUniqueWork(
    "critical_sync",
    ExistingWorkPolicy.REPLACE,
    uniqueWork
)
```

### 4. Proper Error Handling
```kotlin
class ErrorHandlingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    
    override fun doWork(): Result {
        return try {
            val result = performRiskyOperation()
            if (result.isSuccess) {
                Result.success()
            } else {
                Log.w("ErrorHandlingWorker", "Operation failed: ${result.errorMessage}")
                Result.failure()
            }
        } catch (e: NetworkException) {
            Log.w("ErrorHandlingWorker", "Network error, will retry", e)
            Result.retry()
        } catch (e: Exception) {
            Log.e("ErrorHandlingWorker", "Unexpected error", e)
            Result.failure()
        }
    }
}
```

## Testing WorkManager

### Unit Testing

```kotlin
@RunWith(AndroidJUnit4::class)
class WorkManagerTest {
    
    @get:Rule
    val workManagerRule = WorkManagerTestInitHelper.WorkManagerTestRule()
    
    @Test
    fun testSimpleWorker() {
        val workRequest = OneTimeWorkRequestBuilder<SimpleWorker>().build()
        
        WorkManager.getInstance(ApplicationProvider.getApplicationContext())
            .enqueue(workRequest)
        
        val workInfo = workManagerRule.workInfosByTag("").get()
        assertThat(workInfo.size, `is`(1))
        assertThat(workInfo[0].state, `is`(WorkInfo.State.SUCCEEDED))
    }
    
    @Test
    fun testWorkerWithInputData() {
        val inputData = Data.Builder()
            .putString("test_key", "test_value")
            .build()
        
        val workRequest = OneTimeWorkRequestBuilder<DataWorker>()
            .setInputData(inputData)
            .build()
        
        WorkManager.getInstance(ApplicationProvider.getApplicationContext())
            .enqueue(workRequest)
        
        val workInfo = workManagerRule.workInfosByTag("").get()
        assertThat(workInfo[0].state, `is`(WorkInfo.State.SUCCEEDED))
        
        val outputData = workInfo[0].outputData
        assertThat(outputData.getString("result_key"), `is`("Processed: test_value with number: 0"))
    }
}
```

### Integration Testing

```kotlin
@RunWith(AndroidJUnit4::class)
class WorkManagerIntegrationTest {
    
    @get:Rule
    val workManagerRule = WorkManagerTestInitHelper.WorkManagerTestRule()
    
    @Test
    fun testWorkChain() {
        val downloadWork = OneTimeWorkRequestBuilder<DownloadWorker>().build()
        val processWork = OneTimeWorkRequestBuilder<ProcessWorker>().build()
        val uploadWork = OneTimeWorkRequestBuilder<UploadWorker>().build()
        
        WorkManager.getInstance(ApplicationProvider.getApplicationContext())
            .beginWith(downloadWork)
            .then(processWork)
            .then(uploadWork)
            .enqueue()
        
        val workInfos = workManagerRule.workInfosByTag("").get()
        assertThat(workInfos.size, `is`(3))
        
        workInfos.forEach { workInfo ->
            assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        }
    }
}
```

## Summary

WorkManager is the recommended solution for deferrable background work in Android. Key points to remember:

1. **Use WorkManager** for deferrable background work
2. **Choose appropriate work types** (OneTime, Periodic, Coroutine)
3. **Set constraints** to optimize battery usage and ensure proper execution
4. **Handle failures gracefully** with retry logic
5. **Use unique work** for critical operations to prevent duplicates
6. **Observe work status** to provide user feedback
7. **Test thoroughly** with WorkManagerTestInitHelper

WorkManager provides a robust, battery-optimized solution for background work that works consistently across all Android versions.
