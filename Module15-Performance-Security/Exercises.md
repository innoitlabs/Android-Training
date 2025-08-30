# Exercises: Android Performance & Security

## Overview
This document contains practical exercises to reinforce your understanding of Android performance optimization and security practices. Complete these exercises to gain hands-on experience with the concepts covered in this module.

## Exercise Categories

### Beginner Level (Easy)
These exercises focus on basic concepts and simple implementations.

### Intermediate Level (Medium)
These exercises require more complex implementations and deeper understanding.

### Advanced Level (Hard)
These exercises involve advanced techniques and real-world scenarios.

---

## Beginner Exercises

### Exercise 1: Memory Leak Detection
**Objective**: Identify and fix a memory leak in a simple Android app.

**Task**:
1. Create a simple Activity that loads images
2. Intentionally create a memory leak by storing Activity reference in a static variable
3. Use LeakCanary to detect the leak
4. Fix the leak using WeakReference

**Code Template**:
```kotlin
// Create this class with a memory leak
class ImageLoader {
    companion object {
        var currentActivity: MainActivity? = null // ❌ Memory leak
    }
    
    fun loadImage(activity: MainActivity, imageUrl: String) {
        currentActivity = activity // ❌ This creates a memory leak
        // Load image logic
    }
}

// Fix the memory leak
class ImageLoader {
    companion object {
        var currentActivity: WeakReference<MainActivity>? = null // ✅ Fixed
    }
    
    fun loadImage(activity: MainActivity, imageUrl: String) {
        currentActivity = WeakReference(activity) // ✅ No memory leak
        // Load image logic
    }
}
```

**Expected Outcome**: LeakCanary should detect the leak in the first version and not detect any leaks in the fixed version.

---

### Exercise 2: Basic Image Loading with Coil
**Objective**: Implement basic image loading using Coil.

**Task**:
1. Create an Activity with an ImageView
2. Load an image from a URL using Coil
3. Add placeholder and error images
4. Implement crossfade animation

**Code Template**:
```kotlin
class ImageLoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_loading)
        
        val imageView = findViewById<ImageView>(R.id.imageView)
        
        // TODO: Implement image loading with Coil
        // - Load image from URL
        // - Add placeholder
        // - Add error image
        // - Enable crossfade
    }
}
```

**Expected Outcome**: Image loads smoothly with placeholder, error handling, and crossfade animation.

---

### Exercise 3: Secure SharedPreferences
**Objective**: Implement secure data storage using EncryptedSharedPreferences.

**Task**:
1. Create a simple login form
2. Store user credentials securely using EncryptedSharedPreferences
3. Retrieve and display stored credentials
4. Implement secure data clearing

**Code Template**:
```kotlin
class SecureStorageActivity : AppCompatActivity() {
    private lateinit var securePrefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secure_storage)
        
        // TODO: Initialize EncryptedSharedPreferences
        // TODO: Implement save/load/clear methods
    }
    
    private fun saveSecureData(key: String, value: String) {
        // TODO: Implement secure save
    }
    
    private fun loadSecureData(key: String): String? {
        // TODO: Implement secure load
        return null
    }
    
    private fun clearSecureData() {
        // TODO: Implement secure clear
    }
}
```

**Expected Outcome**: Data is stored and retrieved securely, and cannot be accessed by other apps.

---

## Intermediate Exercises

### Exercise 4: Network Performance Optimization
**Objective**: Implement network optimization with OkHttp and compression.

**Task**:
1. Create a network client with OkHttp
2. Enable GZIP compression
3. Implement request/response caching
4. Add request timeout and retry logic
5. Monitor network performance

**Code Template**:
```kotlin
class OptimizedNetworkClient {
    private val client: OkHttpClient
    
    init {
        // TODO: Configure OkHttp client with:
        // - GZIP compression
        // - Caching
        // - Timeouts
        // - Retry logic
        client = OkHttpClient.Builder()
            .build()
    }
    
    fun makeRequest(url: String, callback: (String) -> Unit) {
        // TODO: Implement optimized network request
    }
    
    fun clearCache() {
        // TODO: Implement cache clearing
    }
}
```

**Expected Outcome**: Network requests are faster, use less bandwidth, and handle errors gracefully.

---

### Exercise 5: RecyclerView Performance Optimization
**Objective**: Optimize RecyclerView for large datasets with images.

**Task**:
1. Create a RecyclerView with 100+ items
2. Each item contains an image loaded with Coil
3. Implement efficient image loading and caching
4. Optimize view recycling
5. Monitor memory usage

**Code Template**:
```kotlin
class OptimizedRecyclerAdapter : RecyclerView.Adapter<OptimizedRecyclerAdapter.ViewHolder>() {
    private val items = mutableListOf<Item>()
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        
        // TODO: Implement optimized image loading
        // - Cancel previous requests
        // - Use appropriate image size
        // - Enable caching
        // - Handle view recycling
    }
    
    fun updateItems(newItems: List<Item>) {
        // TODO: Implement efficient item updates
    }
}
```

**Expected Outcome**: RecyclerView scrolls smoothly without memory issues, and images load efficiently.

---

### Exercise 6: Performance Benchmarking
**Objective**: Implement performance benchmarking for app operations.

**Task**:
1. Create benchmark tests for common operations
2. Measure startup time
3. Benchmark database operations
4. Test image loading performance
5. Generate performance reports

**Code Template**:
```kotlin
@RunWith(AndroidJUnit4::class)
@LargeTest
class PerformanceBenchmark {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()
    
    @Test fun startupBenchmark() = benchmarkRule.measureRepeated {
        // TODO: Implement startup benchmark
    }
    
    @Test fun databaseBenchmark() = benchmarkRule.measureRepeated {
        // TODO: Implement database operations benchmark
    }
    
    @Test fun imageLoadingBenchmark() = benchmarkRule.measureRepeated {
        // TODO: Implement image loading benchmark
    }
}
```

**Expected Outcome**: Accurate performance measurements and identification of bottlenecks.

---

## Advanced Exercises

### Exercise 7: Complete Secure Notes App
**Objective**: Build a complete secure notes app with all optimization features.

**Task**:
1. Implement secure note storage with encryption
2. Add image support with efficient loading
3. Implement network sync with compression
4. Add memory leak detection
5. Include performance benchmarking
6. Implement background sync with WorkManager

**Requirements**:
- Use EncryptedSharedPreferences for note storage
- Implement Coil for image loading
- Use OkHttp with compression for network requests
- Integrate LeakCanary for memory leak detection
- Add Jetpack Benchmark for performance testing
- Use WorkManager for background synchronization

**Expected Outcome**: A fully functional, secure, and optimized notes app.

---

### Exercise 8: Advanced Security Implementation
**Objective**: Implement comprehensive security measures.

**Task**:
1. Implement certificate pinning
2. Add biometric authentication
3. Create secure key management
4. Implement input validation and sanitization
5. Add security logging and monitoring
6. Create security testing framework

**Code Template**:
```kotlin
class SecurityManager {
    fun setupCertificatePinning(): OkHttpClient {
        // TODO: Implement certificate pinning
    }
    
    fun setupBiometricAuth(): BiometricPrompt {
        // TODO: Implement biometric authentication
    }
    
    fun generateSecureKey(): SecretKey {
        // TODO: Implement secure key generation
    }
    
    fun validateInput(input: String): Boolean {
        // TODO: Implement input validation
    }
    
    fun logSecurityEvent(event: String) {
        // TODO: Implement secure logging
    }
}
```

**Expected Outcome**: Comprehensive security implementation with multiple layers of protection.

---

### Exercise 9: Performance Profiling and Optimization
**Objective**: Profile and optimize a complex Android app.

**Task**:
1. Profile an existing app using Android Profiler
2. Identify performance bottlenecks
3. Implement optimizations
4. Measure performance improvements
5. Create optimization report

**Steps**:
1. Choose a complex app (or create one)
2. Use CPU Profiler to identify slow methods
3. Use Memory Profiler to find memory leaks
4. Use Network Profiler to optimize network usage
5. Implement optimizations
6. Re-profile and measure improvements

**Expected Outcome**: Significant performance improvements and detailed optimization report.

---

### Exercise 10: Battery Optimization
**Objective**: Implement comprehensive battery optimization.

**Task**:
1. Analyze battery usage patterns
2. Optimize background tasks
3. Implement efficient location updates
4. Optimize network requests
5. Add battery monitoring
6. Test on different devices

**Code Template**:
```kotlin
class BatteryOptimizer {
    fun optimizeBackgroundTasks() {
        // TODO: Implement WorkManager with battery constraints
    }
    
    fun optimizeLocationUpdates() {
        // TODO: Implement efficient location updates
    }
    
    fun optimizeNetworkRequests() {
        // TODO: Implement batched network requests
    }
    
    fun monitorBatteryUsage() {
        // TODO: Implement battery monitoring
    }
}
```

**Expected Outcome**: Reduced battery consumption and improved user experience.

---

## Exercise Submission Guidelines

### What to Submit
1. **Source Code**: Complete, working code for each exercise
2. **Documentation**: Comments explaining your implementation
3. **Screenshots**: App screenshots showing functionality
4. **Performance Data**: Benchmark results and profiling data
5. **Report**: Summary of what you learned and challenges faced

### Evaluation Criteria
- **Functionality**: Does the code work as expected?
- **Performance**: Are optimizations implemented correctly?
- **Security**: Are security measures properly implemented?
- **Code Quality**: Is the code clean, well-documented, and maintainable?
- **Understanding**: Does the implementation show understanding of concepts?

### Tips for Success
1. **Start Simple**: Begin with basic implementations and add complexity
2. **Test Thoroughly**: Test on different devices and scenarios
3. **Profile Regularly**: Use Android Profiler to monitor performance
4. **Document Everything**: Comment your code and document your decisions
5. **Iterate**: Don't be afraid to refactor and improve your code

---

## Additional Challenges

### Challenge 1: Multi-Module Architecture
Implement the secure notes app using a multi-module architecture with separate modules for:
- UI layer
- Domain layer
- Data layer
- Security layer

### Challenge 2: Offline-First Architecture
Implement an offline-first architecture with:
- Local database with Room
- Offline data synchronization
- Conflict resolution
- Data consistency

### Challenge 3: Accessibility and Performance
Implement accessibility features while maintaining performance:
- Screen reader support
- High contrast mode
- Large text support
- Performance optimization for accessibility features

### Challenge 4: Internationalization and Performance
Implement internationalization while optimizing for:
- Memory usage with different languages
- Performance with RTL languages
- Efficient string resource management
- Dynamic language switching

---

## Resources for Help

### Documentation
- [Android Developer Documentation](https://developer.android.com/)
- [Coil Documentation](https://coil-kt.github.io/coil/)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [LeakCanary Documentation](https://square.github.io/leakcanary/)

### Tools
- Android Studio Profiler
- LeakCanary
- Android Studio Layout Inspector
- Android Studio Database Inspector

### Community
- Stack Overflow
- Android Developers Community
- GitHub repositories with similar implementations

---

## Conclusion

These exercises provide hands-on experience with Android performance optimization and security practices. Complete them systematically to build a strong foundation in these areas. Remember to:

- **Practice regularly** with real-world scenarios
- **Measure performance** before and after optimizations
- **Test security** implementations thoroughly
- **Document your learning** for future reference
- **Share your solutions** with the community

Good luck with your exercises! 🚀
