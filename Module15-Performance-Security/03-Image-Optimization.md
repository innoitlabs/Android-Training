# Image Loading & Caching Optimization

## Overview
Efficient image loading and caching is crucial for Android app performance. This guide covers how to optimize image loading using modern libraries like Coil, implement proper caching strategies, and avoid common performance pitfalls.

## Why Image Optimization Matters
- **Memory Usage**: Images consume significant memory
- **Network Bandwidth**: Large images slow down app performance
- **User Experience**: Slow image loading creates poor UX
- **Battery Life**: Inefficient image loading drains battery

## Using Coil for Image Loading

### 1. Adding Coil Dependency
```kotlin
// In app/build.gradle.kts
dependencies {
    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-compose:2.2.2") // For Jetpack Compose
}
```

### 2. Basic Image Loading
```kotlin
// Simple image loading
class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        
        val imageView = findViewById<ImageView>(R.id.imageView)
        
        // Basic image loading
        imageView.load("https://example.com/image.jpg")
    }
}
```

### 3. Advanced Image Loading with Options
```kotlin
// Advanced image loading with caching and transformations
class OptimizedImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        
        val imageView = findViewById<ImageView>(R.id.imageView)
        
        imageView.load("https://example.com/large-image.jpg") {
            // Enable crossfade animation
            crossfade(true)
            crossfade(300) // 300ms duration
            
            // Memory cache policy
            memoryCachePolicy(CachePolicy.ENABLED)
            
            // Disk cache policy
            diskCachePolicy(CachePolicy.ENABLED)
            
            // Resize image to fit view
            size(500, 500)
            
            // Apply transformations
            transformations(
                RoundedCornersTransformation(16f),
                GrayscaleTransformation()
            )
            
            // Placeholder and error images
            placeholder(R.drawable.placeholder)
            error(R.drawable.error_image)
            
            // Listener for loading states
            listener(
                onStart = { placeholder ->
                    // Loading started
                },
                onSuccess = { _, _ ->
                    // Loading succeeded
                },
                onError = { _, _ ->
                    // Loading failed
                }
            )
        }
    }
}
```

## Image Caching Strategies

### 1. Memory Caching
```kotlin
// Configure memory cache
class CacheConfig {
    fun configureMemoryCache(context: Context) {
        val imageLoader = ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25) // Use 25% of available memory
                    .build()
            }
            .build()
    }
}
```

### 2. Disk Caching
```kotlin
// Configure disk cache
class DiskCacheConfig {
    fun configureDiskCache(context: Context) {
        val imageLoader = ImageLoader.Builder(context)
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02) // Use 2% of available disk space
                    .build()
            }
            .build()
    }
}
```

### 3. Custom Cache Implementation
```kotlin
// Custom cache implementation
class CustomImageCache {
    private val memoryCache = LruCache<String, Bitmap>(100) // Cache 100 images
    
    fun getImage(url: String): Bitmap? {
        return memoryCache.get(url)
    }
    
    fun putImage(url: String, bitmap: Bitmap) {
        memoryCache.put(url, bitmap)
    }
    
    fun clearCache() {
        memoryCache.evictAll()
    }
}
```

## Image Optimization Techniques

### 1. Image Resizing
```kotlin
// Resize images to fit view dimensions
class ImageResizer {
    fun loadResizedImage(imageView: ImageView, url: String) {
        imageView.load(url) {
            // Get view dimensions
            val width = imageView.width
            val height = imageView.height
            
            // Resize image to fit view
            size(width, height)
            
            // Use center crop to maintain aspect ratio
            scale(Scale.FILL)
        }
    }
}
```

### 2. Image Compression
```kotlin
// Compress images for better performance
class ImageCompressor {
    fun loadCompressedImage(imageView: ImageView, url: String) {
        imageView.load(url) {
            // Reduce image quality for better performance
            quality(0.8f)
            
            // Use RGB_565 format for less memory usage
            colorSpace(ColorSpace.Srgb)
        }
    }
}
```

### 3. Progressive Loading
```kotlin
// Progressive image loading
class ProgressiveImageLoader {
    fun loadProgressiveImage(imageView: ImageView, url: String) {
        imageView.load(url) {
            // Enable progressive loading
            progressiveRenderingEnabled(true)
            
            // Show placeholder while loading
            placeholder(R.drawable.placeholder)
            
            // Crossfade when image loads
            crossfade(true)
        }
    }
}
```

## Bitmap Optimization

### 1. BitmapFactory Options
```kotlin
// Optimize bitmap loading
class BitmapOptimizer {
    fun loadOptimizedBitmap(context: Context, resourceId: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            // Decode image size only
            inJustDecodeBounds = true
        }
        
        BitmapFactory.decodeResource(context.resources, resourceId, options)
        
        // Calculate sample size
        options.inSampleSize = calculateInSampleSize(options, 500, 500)
        
        // Decode actual bitmap
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.RGB_565 // Use less memory
        
        return BitmapFactory.decodeResource(context.resources, resourceId, options)
    }
    
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            
            while (halfHeight / inSampleSize >= reqHeight && 
                   halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
}
```

### 2. Bitmap Recycling
```kotlin
// Proper bitmap recycling
class BitmapManager {
    private var currentBitmap: Bitmap? = null
    
    fun loadNewBitmap(imageView: ImageView, url: String) {
        // Recycle previous bitmap
        currentBitmap?.let { bitmap ->
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
        
        // Load new bitmap
        imageView.load(url) {
            listener(
                onSuccess = { _, result ->
                    currentBitmap = result.bitmap
                }
            )
        }
    }
    
    fun cleanup() {
        currentBitmap?.let { bitmap ->
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
        currentBitmap = null
    }
}
```

## RecyclerView Image Loading

### 1. Efficient RecyclerView Image Loading
```kotlin
// RecyclerView adapter with optimized image loading
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    private val images = mutableListOf<String>()
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = images[position]
        
        // Cancel previous request for this ImageView
        holder.imageView.clear()
        
        // Load new image
        holder.imageView.load(imageUrl) {
            // Optimize for RecyclerView
            size(300, 300)
            crossfade(false) // Disable crossfade for better performance
            memoryCachePolicy(CachePolicy.ENABLED)
        }
    }
    
    override fun getItemCount() = images.size
    
    fun updateImages(newImages: List<String>) {
        images.clear()
        images.addAll(newImages)
        notifyDataSetChanged()
    }
}
```

### 2. Image Preloading
```kotlin
// Preload images for better performance
class ImagePreloader {
    fun preloadImages(context: Context, urls: List<String>) {
        val imageLoader = ImageLoader(context)
        
        urls.forEach { url ->
            imageLoader.enqueue(
                ImageRequest.Builder(context)
                    .data(url)
                    .size(300, 300)
                    .build()
            )
        }
    }
}
```

## Performance Monitoring

### 1. Image Loading Performance
```kotlin
// Monitor image loading performance
class ImagePerformanceMonitor {
    fun loadImageWithMonitoring(imageView: ImageView, url: String) {
        val startTime = System.currentTimeMillis()
        
        imageView.load(url) {
            listener(
                onSuccess = { _, _ ->
                    val loadTime = System.currentTimeMillis() - startTime
                    Log.d("ImagePerformance", "Image loaded in ${loadTime}ms")
                },
                onError = { _, _ ->
                    Log.e("ImagePerformance", "Failed to load image: $url")
                }
            )
        }
    }
}
```

### 2. Memory Usage Monitoring
```kotlin
// Monitor memory usage for images
class ImageMemoryMonitor {
    fun monitorMemoryUsage() {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()
        val maxMemory = runtime.maxMemory()
        
        Log.d("ImageMemory", "Used memory: ${usedMemory / 1024 / 1024}MB")
        Log.d("ImageMemory", "Max memory: ${maxMemory / 1024 / 1024}MB")
    }
}
```

## Best Practices

### 1. Image Loading Best Practices
```kotlin
// Best practices for image loading
class ImageLoadingBestPractices {
    fun loadImageOptimally(imageView: ImageView, url: String) {
        imageView.load(url) {
            // Always specify size
            size(imageView.width, imageView.height)
            
            // Enable caching
            memoryCachePolicy(CachePolicy.ENABLED)
            diskCachePolicy(CachePolicy.ENABLED)
            
            // Use appropriate scale
            scale(Scale.FILL)
            
            // Handle errors gracefully
            error(R.drawable.error_image)
            
            // Use crossfade for better UX
            crossfade(true)
        }
    }
}
```

### 2. Memory Management
```kotlin
// Proper memory management for images
class ImageMemoryManager {
    fun manageImageMemory(context: Context) {
        // Clear memory cache when needed
        val imageLoader = ImageLoader(context)
        imageLoader.memoryCache.clear()
        
        // Clear disk cache when needed
        imageLoader.diskCache.clear()
    }
}
```

## Summary
- **Use Coil** for efficient image loading and caching
- **Implement proper caching strategies** for memory and disk
- **Optimize image size and quality** for better performance
- **Use appropriate transformations** and loading options
- **Monitor performance** and memory usage
- **Follow best practices** for RecyclerView and large image sets
- **Handle errors gracefully** and provide fallbacks

## Next Steps
- Implement Coil in your Android project
- Practice different caching strategies
- Monitor image loading performance
- Move to the next section: Network Optimization
