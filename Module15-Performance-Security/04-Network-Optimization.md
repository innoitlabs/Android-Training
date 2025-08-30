# Network Optimization & Compression

## Overview
Network optimization is crucial for Android apps to provide fast, efficient, and reliable network communication. This guide covers techniques for optimizing network requests, implementing compression, and managing network resources effectively.

## Why Network Optimization Matters
- **User Experience**: Faster loading times improve user satisfaction
- **Battery Life**: Efficient network usage reduces battery consumption
- **Data Usage**: Compression reduces mobile data costs
- **Reliability**: Better error handling and retry mechanisms
- **Performance**: Optimized requests improve app responsiveness

## OkHttp Configuration

### 1. Basic OkHttp Setup
```kotlin
// Basic OkHttp client
val client = OkHttpClient.Builder()
    .build()
```

### 2. Optimized OkHttp Configuration
```kotlin
// Optimized OkHttp client with compression and caching
val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Accept-Encoding", "gzip, deflate")
            .header("User-Agent", "MyApp/1.0")
            .build()
        chain.proceed(request)
    }
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()
```

## Compression Techniques

### 1. GZIP Compression
```kotlin
// Enable GZIP compression
class CompressionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept-Encoding", "gzip, deflate")
            .build()
        
        val response = chain.proceed(request)
        
        // Handle compressed responses
        return if (response.header("Content-Encoding", "")?.contains("gzip") == true) {
            response.newBuilder()
                .body(response.body?.let { GzipResponseBody(it) })
                .build()
        } else {
            response
        }
    }
}

class GzipResponseBody(private val responseBody: ResponseBody) : ResponseBody() {
    override fun contentType(): MediaType? = responseBody.contentType()
    override fun contentLength(): Long = responseBody.contentLength()
    override fun source(): BufferedSource = Okio.buffer(GzipSource(responseBody.source()))
}
```

### 2. Request Compression
```kotlin
// Compress request bodies
class RequestCompressionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        return if (request.body != null && shouldCompress(request)) {
            val compressedBody = compressRequestBody(request.body!!)
            val compressedRequest = request.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(request.method, compressedBody)
                .build()
            
            chain.proceed(compressedRequest)
        } else {
            chain.proceed(request)
        }
    }
    
    private fun shouldCompress(request: Request): Boolean {
        return request.header("Content-Type")?.contains("application/json") == true
    }
    
    private fun compressRequestBody(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? = body.contentType()
            override fun contentLength(): Long = -1
            
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = GzipSink(sink).buffer()
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}
```

## Caching Strategies

### 1. HTTP Caching
```kotlin
// Implement HTTP caching
val cacheSize = 10 * 1024 * 1024 // 10 MB
val cache = Cache(File(context.cacheDir, "http_cache"), cacheSize.toLong())

val client = OkHttpClient.Builder()
    .cache(cache)
    .addInterceptor { chain ->
        val request = chain.request()
        
        // Add cache headers
        val cacheRequest = request.newBuilder()
            .header("Cache-Control", "max-age=300") // 5 minutes
            .build()
        
        chain.proceed(cacheRequest)
    }
    .build()
```

### 2. Custom Caching
```kotlin
// Custom caching implementation
class CacheInterceptor : Interceptor {
    private val cache = LruCache<String, Response>(100)
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cacheKey = request.url.toString()
        
        // Check cache first
        cache.get(cacheKey)?.let { cachedResponse ->
            return cachedResponse
        }
        
        // Make network request
        val response = chain.proceed(request)
        
        // Cache successful responses
        if (response.isSuccessful) {
            cache.put(cacheKey, response)
        }
        
        return response
    }
}
```

## Connection Pooling

### 1. Connection Pool Configuration
```kotlin
// Configure connection pooling
val connectionPool = ConnectionPool(
    maxIdleConnections = 5,
    keepAliveDuration = 5,
    timeUnit = TimeUnit.MINUTES
)

val client = OkHttpClient.Builder()
    .connectionPool(connectionPool)
    .build()
```

### 2. Connection Management
```kotlin
// Manage connections efficiently
class ConnectionManager {
    private val client = OkHttpClient.Builder()
        .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
        .build()
    
    fun makeRequest(url: String, callback: (String) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody ?: "")
            }
            
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }
        })
    }
}
```

## Request Batching

### 1. Batch Multiple Requests
```kotlin
// Batch multiple requests
class BatchRequestManager {
    private val client = OkHttpClient()
    private val requestQueue = mutableListOf<Request>()
    
    fun addRequest(request: Request) {
        requestQueue.add(request)
    }
    
    fun executeBatch(callback: (List<Response>) -> Unit) {
        val responses = mutableListOf<Response>()
        var completedCount = 0
        
        requestQueue.forEach { request ->
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    responses.add(response)
                    completedCount++
                    
                    if (completedCount == requestQueue.size) {
                        callback(responses)
                    }
                }
                
                override fun onFailure(call: Call, e: IOException) {
                    completedCount++
                    if (completedCount == requestQueue.size) {
                        callback(responses)
                    }
                }
            })
        }
        
        requestQueue.clear()
    }
}
```

## Retry Mechanisms

### 1. Exponential Backoff
```kotlin
// Implement exponential backoff
class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null
        
        repeat(maxRetries) { attempt ->
            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                exception = e
                if (attempt == maxRetries - 1) {
                    throw e
                }
            }
            
            // Exponential backoff
            val delay = (2.0.pow(attempt.toDouble()) * 1000).toLong()
            Thread.sleep(delay)
        }
        
        return response ?: throw exception!!
    }
}
```

### 2. Retry with Different Strategies
```kotlin
// Retry with different strategies
class SmartRetryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        repeat(3) { attempt ->
            try {
                val response = chain.proceed(request)
                
                // Retry on specific status codes
                if (response.code in listOf(500, 502, 503, 504)) {
                    if (attempt < 2) {
                        Thread.sleep(1000 * (attempt + 1))
                        continue
                    }
                }
                
                return response
            } catch (e: IOException) {
                if (attempt == 2) throw e
                Thread.sleep(1000 * (attempt + 1))
            }
        }
        
        throw IOException("Max retries exceeded")
    }
}
```

## Network Monitoring

### 1. Request/Response Logging
```kotlin
// Log network requests and responses
class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        
        Log.d("Network", "Request: ${request.method} ${request.url}")
        
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()
        
        Log.d("Network", "Response: ${response.code} in ${endTime - startTime}ms")
        
        return response
    }
}
```

### 2. Performance Monitoring
```kotlin
// Monitor network performance
class NetworkPerformanceMonitor {
    private val metrics = mutableMapOf<String, Long>()
    
    fun startRequest(url: String) {
        metrics[url] = System.currentTimeMillis()
    }
    
    fun endRequest(url: String) {
        val startTime = metrics[url] ?: return
        val duration = System.currentTimeMillis() - startTime
        
        Log.d("NetworkPerformance", "$url took ${duration}ms")
        metrics.remove(url)
    }
    
    fun getAverageResponseTime(): Long {
        return if (metrics.isNotEmpty()) {
            metrics.values.average().toLong()
        } else {
            0L
        }
    }
}
```

## Best Practices

### 1. Network Configuration
```kotlin
// Best practices for network configuration
class OptimizedNetworkClient(private val context: Context) {
    private val client: OkHttpClient
    
    init {
        client = OkHttpClient.Builder()
            // Enable compression
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept-Encoding", "gzip, deflate")
                    .build()
                chain.proceed(request)
            }
            // Configure timeouts
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            // Enable caching
            .cache(Cache(File(context.cacheDir, "http_cache"), 10 * 1024 * 1024))
            // Configure connection pool
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            // Add retry mechanism
            .addInterceptor(RetryInterceptor(3))
            // Add logging
            .addInterceptor(LoggingInterceptor())
            .build()
    }
    
    fun makeRequest(url: String, callback: (String) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()
        
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                callback(responseBody ?: "")
            }
            
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }
        })
    }
}
```

### 2. Error Handling
```kotlin
// Comprehensive error handling
class NetworkErrorHandler {
    fun handleError(exception: Exception): String {
        return when (exception) {
            is UnknownHostException -> "No internet connection"
            is SocketTimeoutException -> "Request timed out"
            is IOException -> "Network error occurred"
            else -> "Unknown error: ${exception.message}"
        }
    }
    
    fun shouldRetry(exception: Exception): Boolean {
        return when (exception) {
            is SocketTimeoutException -> true
            is IOException -> true
            else -> false
        }
    }
}
```

## Summary
- **Enable compression** (GZIP) to reduce data transfer
- **Implement caching** to avoid redundant requests
- **Use connection pooling** for efficient resource management
- **Batch requests** when possible to reduce overhead
- **Implement retry mechanisms** with exponential backoff
- **Monitor network performance** to identify bottlenecks
- **Handle errors gracefully** with proper user feedback
- **Configure appropriate timeouts** for different scenarios

## Next Steps
- Implement compression in your network layer
- Add caching to frequently accessed data
- Set up connection pooling for better performance
- Add retry mechanisms for reliability
- Monitor network performance in production
- Move to the next section: Security Best Practices
