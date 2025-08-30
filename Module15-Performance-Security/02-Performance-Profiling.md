# Performance Profiling with Android Profiler

## Overview
Android Profiler is a powerful tool in Android Studio that helps you analyze your app's performance in real-time. It provides detailed insights into CPU usage, memory allocation, network activity, and energy consumption.

## Getting Started with Android Profiler

### 1. Opening Android Profiler
1. Open your project in Android Studio
2. Go to **View → Tool Windows → Profiler**
3. Or use the shortcut: **Alt+6** (Windows/Linux) or **Cmd+6** (macOS)

### 2. Profiler Components
The Android Profiler consists of four main profilers:
- **CPU Profiler** - Analyze CPU usage and method execution
- **Memory Profiler** - Monitor memory allocation and garbage collection
- **Network Profiler** - Track network requests and responses
- **Energy Profiler** - Monitor battery usage and energy consumption

## CPU Profiler

### Understanding CPU Profiler
The CPU Profiler helps you identify performance bottlenecks in your code by showing:
- Method execution time
- CPU usage patterns
- Thread activity
- Call stack analysis

### Using CPU Profiler
```kotlin
// Example: Profiling a heavy operation
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        findViewById<Button>(R.id.profileButton).setOnClickListener {
            // This operation will be visible in CPU Profiler
            performHeavyOperation()
        }
    }
    
    private fun performHeavyOperation() {
        // CPU Profiler will show this method's execution time
        repeat(1000000) {
            val result = it * it
        }
    }
}
```

### CPU Profiler Views
1. **Call Chart** - Shows method calls as a flame chart
2. **Flame Chart** - Shows method calls in a hierarchical view
3. **Top Down** - Shows method calls from top to bottom
4. **Bottom Up** - Shows method calls from bottom to top

### Profiling Best Practices
```kotlin
// ✅ Good: Profile specific operations
class PerformanceTest {
    fun profileOperation() {
        // Start profiling
        val startTime = System.currentTimeMillis()
        
        // Your operation here
        performOperation()
        
        // End profiling
        val endTime = System.currentTimeMillis()
        Log.d("Performance", "Operation took: ${endTime - startTime}ms")
    }
}

// ❌ Avoid: Profiling everything at once
// This makes it hard to identify specific bottlenecks
```

## Memory Profiler

### Understanding Memory Profiler
The Memory Profiler shows:
- Memory allocation over time
- Garbage collection events
- Memory leaks
- Object allocation patterns

### Memory Profiler Features
```kotlin
// Example: Monitoring memory allocation
class MemoryTest {
    private val dataList = mutableListOf<String>()
    
    fun addData() {
        // This will be visible in Memory Profiler
        repeat(1000) {
            dataList.add("Data item $it")
        }
    }
    
    fun clearData() {
        // This will trigger garbage collection
        dataList.clear()
        System.gc() // Force garbage collection for testing
    }
}
```

### Memory Profiler Views
1. **Memory Timeline** - Shows memory usage over time
2. **Memory Allocation** - Shows object allocation details
3. **Memory Dump** - Shows current memory state

### Memory Analysis Tips
```kotlin
// ✅ Monitor specific memory operations
class MemoryMonitor {
    private var memoryBefore: Long = 0
    private var memoryAfter: Long = 0
    
    fun startMonitoring() {
        memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    }
    
    fun endMonitoring() {
        memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        val memoryUsed = memoryAfter - memoryBefore
        Log.d("Memory", "Memory used: ${memoryUsed / 1024}KB")
    }
}
```

## Network Profiler

### Understanding Network Profiler
The Network Profiler shows:
- Network requests and responses
- Request timing
- Data transfer sizes
- Network errors

### Network Profiler Usage
```kotlin
// Example: Profiling network requests
class NetworkTest {
    private val client = OkHttpClient()
    
    fun makeNetworkRequest() {
        val request = Request.Builder()
            .url("https://api.example.com/data")
            .build()
            
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // Network Profiler will show this request
                val data = response.body?.string()
                Log.d("Network", "Response received: ${data?.length} bytes")
            }
            
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Network", "Request failed", e)
            }
        })
    }
}
```

### Network Profiler Features
1. **Request List** - Shows all network requests
2. **Request Details** - Shows headers, body, timing
3. **Response Details** - Shows response data and timing

## Energy Profiler

### Understanding Energy Profiler
The Energy Profiler shows:
- Battery usage over time
- Energy consumption by component
- Wake locks and alarms
- Location and sensor usage

### Energy Profiler Usage
```kotlin
// Example: Monitoring energy consumption
class EnergyTest {
    private lateinit var locationManager: LocationManager
    private lateinit var wakeLock: PowerManager.WakeLock
    
    fun startEnergyIntensiveOperation() {
        // These operations will be visible in Energy Profiler
        
        // Location updates
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L, // 1 second
            1f, // 1 meter
            locationListener
        )
        
        // Wake lock
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "MyApp::MyWakelockTag"
        )
        wakeLock.acquire()
    }
    
    fun stopEnergyIntensiveOperation() {
        locationManager.removeUpdates(locationListener)
        wakeLock.release()
    }
}
```

## Advanced Profiling Techniques

### 1. Custom Profiling Points
```kotlin
// Add custom profiling points in your code
class CustomProfiler {
    fun profileMethod() {
        // Start profiling
        Debug.startMethodTracing("my_trace")
        
        try {
            // Your code here
            performOperation()
        } finally {
            // Stop profiling
            Debug.stopMethodTracing()
        }
    }
}
```

### 2. Performance Monitoring
```kotlin
// Monitor performance in production
class PerformanceMonitor {
    private val metrics = mutableMapOf<String, Long>()
    
    fun startTimer(operation: String) {
        metrics[operation] = System.currentTimeMillis()
    }
    
    fun endTimer(operation: String) {
        val startTime = metrics[operation] ?: return
        val duration = System.currentTimeMillis() - startTime
        Log.d("Performance", "$operation took ${duration}ms")
        metrics.remove(operation)
    }
}
```

### 3. Benchmark Testing
```kotlin
// Use Jetpack Benchmark for performance testing
@RunWith(AndroidJUnit4::class)
@LargeTest
class PerformanceBenchmark {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()
    
    @Test fun measureOperation() = benchmarkRule.measureRepeated {
        // Your operation here
        performOperation()
    }
}
```

## Profiling Best Practices

### 1. Profile on Real Devices
- Always profile on real devices, not just emulators
- Different devices have different performance characteristics
- Test on low-end devices to identify bottlenecks

### 2. Profile Release Builds
```kotlin
// Enable profiling in release builds
android {
    buildTypes {
        release {
            isDebuggable = true // Enable for profiling
            isMinifyEnabled = false // Disable for easier analysis
        }
    }
}
```

### 3. Focus on Critical Paths
```kotlin
// Profile the most important operations
class CriticalPathProfiler {
    fun profileCriticalOperation() {
        // Profile app startup
        profileAppStartup()
        
        // Profile main user flows
        profileMainUserFlow()
        
        // Profile heavy operations
        profileHeavyOperations()
    }
}
```

### 4. Use Profiling Data
```kotlin
// Use profiling data to optimize code
class OptimizedCode {
    // Before optimization (slow)
    fun slowOperation() {
        repeat(1000000) {
            val result = it * it
        }
    }
    
    // After optimization (fast)
    fun fastOperation() {
        // Use more efficient algorithms
        // Cache results
        // Use background threads
    }
}
```

## Summary
- **CPU Profiler** - Analyze method execution and CPU usage
- **Memory Profiler** - Monitor memory allocation and leaks
- **Network Profiler** - Track network requests and timing
- **Energy Profiler** - Monitor battery usage and energy consumption
- **Profile on real devices** for accurate results
- **Focus on critical paths** and user-facing operations
- **Use profiling data** to optimize your code

## Next Steps
- Practice using Android Profiler with the provided examples
- Profile your own code to identify bottlenecks
- Use the profiling data to optimize performance
- Move to the next section: Image Optimization
