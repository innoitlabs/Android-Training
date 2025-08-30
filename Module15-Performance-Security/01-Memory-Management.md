# Memory Management & Leak Prevention

## Overview
Memory leaks are one of the most common performance issues in Android apps. This guide covers how to identify, prevent, and fix memory leaks using proper lifecycle management and best practices.

## What is a Memory Leak?
A memory leak occurs when objects that are no longer needed are still being referenced, preventing the garbage collector from reclaiming their memory. In Android, this often happens with Activities, Fragments, and other UI components.

## Common Memory Leak Scenarios

### 1. Static References to Context/Activity
**❌ Problematic Code:**
```kotlin
object LeakHolder {
    var leakedContext: Context? = null
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ❌ This creates a memory leak!
        LeakHolder.leakedContext = this
    }
}
```

**✅ Fixed Code:**
```kotlin
object LeakHolder {
    var leakedContext: WeakReference<Context>? = null
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ✅ Use WeakReference to avoid memory leaks
        LeakHolder.leakedContext = WeakReference(applicationContext)
    }
}
```

### 2. Anonymous Inner Classes
**❌ Problematic Code:**
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ❌ Anonymous inner class holds implicit reference to Activity
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                // This holds a reference to MainActivity
                findViewById<TextView>(R.id.textView).text = "Updated"
            }
        }, 5000)
    }
}
```

**✅ Fixed Code:**
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private val updateRunnable = Runnable {
        // Use WeakReference or check if activity is still valid
        if (!isFinishing && !isDestroyed) {
            findViewById<TextView>(R.id.textView)?.text = "Updated"
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed(updateRunnable, 5000)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // ✅ Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateRunnable)
    }
}
```

### 3. Singleton with Context Reference
**❌ Problematic Code:**
```kotlin
object DataManager {
    private var context: Context? = null
    
    fun initialize(context: Context) {
        this.context = context // ❌ Holds strong reference
    }
    
    fun getData(): String {
        return context?.getString(R.string.app_name) ?: ""
    }
}
```

**✅ Fixed Code:**
```kotlin
object DataManager {
    private var contextRef: WeakReference<Context>? = null
    
    fun initialize(context: Context) {
        // ✅ Use WeakReference to avoid memory leaks
        this.contextRef = WeakReference(context.applicationContext)
    }
    
    fun getData(): String {
        return contextRef?.get()?.getString(R.string.app_name) ?: ""
    }
}
```

## Best Practices for Memory Management

### 1. Use ApplicationContext When Possible
```kotlin
// ✅ Use applicationContext for long-lived operations
val sharedPrefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
val database = Room.databaseBuilder(
    applicationContext, // ✅ Use applicationContext
    AppDatabase::class.java,
    "app_database"
).build()
```

### 2. Proper Lifecycle Management
```kotlin
class MainActivity : AppCompatActivity() {
    private var dataObserver: DataObserver? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        dataObserver = DataObserver()
        // ✅ Register observer
        dataManager.addObserver(dataObserver)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // ✅ Unregister observer to prevent memory leaks
        dataObserver?.let { dataManager.removeObserver(it) }
        dataObserver = null
    }
}
```

### 3. Use WeakReference for Callbacks
```kotlin
class NetworkManager {
    private val callbacks = mutableListOf<WeakReference<NetworkCallback>>()
    
    fun addCallback(callback: NetworkCallback) {
        callbacks.add(WeakReference(callback))
    }
    
    fun notifyCallbacks(data: String) {
        callbacks.removeAll { it.get() == null } // Remove null references
        callbacks.forEach { ref ->
            ref.get()?.onDataReceived(data)
        }
    }
}
```

### 4. Proper AsyncTask/Coroutine Management
```kotlin
class MainActivity : AppCompatActivity() {
    private var job: Job? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        job = lifecycleScope.launch {
            // ✅ Use lifecycleScope for automatic cancellation
            val result = withContext(Dispatchers.IO) {
                performHeavyOperation()
            }
            updateUI(result)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // ✅ Cancel coroutine when activity is destroyed
        job?.cancel()
    }
}
```

## Memory Leak Detection Tools

### 1. Android Profiler
- Open Android Studio
- Go to View → Tool Windows → Profiler
- Monitor memory usage during app lifecycle
- Look for increasing memory usage without garbage collection

### 2. LeakCanary (Integrated in Project)
```kotlin
// Already added to build.gradle.kts
debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
```

LeakCanary automatically detects memory leaks and provides detailed reports.

## Memory Optimization Tips

### 1. Use ViewBinding Instead of findViewById
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // ✅ Use binding instead of findViewById
        binding.textView.text = "Hello World"
    }
}
```

### 2. Optimize Bitmap Usage
```kotlin
// ✅ Use BitmapFactory.Options for memory-efficient image loading
val options = BitmapFactory.Options().apply {
    inSampleSize = 2 // Reduce memory usage by half
    inPreferredConfig = Bitmap.Config.RGB_565 // Use less memory per pixel
}

val bitmap = BitmapFactory.decodeResource(resources, R.drawable.large_image, options)
```

### 3. Use Lazy Initialization
```kotlin
class MainActivity : AppCompatActivity() {
    // ✅ Lazy initialization - only created when first accessed
    private val expensiveObject by lazy {
        ExpensiveObject()
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // ExpensiveObject is only created when this line executes
        expensiveObject.doSomething()
    }
}
```

## Summary
- **Avoid static references** to Activities and Fragments
- **Use WeakReference** when you need to hold references
- **Properly manage lifecycles** and unregister callbacks
- **Use ApplicationContext** for long-lived operations
- **Monitor memory usage** with Android Profiler
- **Use LeakCanary** for automatic leak detection
- **Optimize bitmap usage** and use lazy initialization

## Next Steps
- Practice identifying memory leaks in your code
- Use Android Profiler to monitor memory usage
- Implement the examples in the project
- Move to the next section: Performance Profiling
