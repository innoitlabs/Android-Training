package com.example.performancesecurity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.annotation.ExperimentalCoilApi
import coil.load
import com.example.performancesecurity.databinding.ActivityImageOptimizationBinding

@OptIn(ExperimentalCoilApi::class)
class ImageOptimizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageOptimizationBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageOptimizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.btnLoadBasic.setOnClickListener {
            loadBasicImage()
        }
        
        binding.btnLoadOptimized.setOnClickListener {
            loadOptimizedImage()
        }
        
        binding.btnLoadWithCache.setOnClickListener {
            loadImageWithCache()
        }
        
        binding.btnClearCache.setOnClickListener {
            clearImageCache()
        }
    }
    
    private fun loadBasicImage() {
        binding.textStatus.text = "Loading basic image..."
        
        // Basic image loading
        binding.imageView.load("https://picsum.photos/500/300") {
            listener(
                onStart = { placeholder ->
                    binding.textStatus.text = "Loading started..."
                },
                onSuccess = { _, _ ->
                    binding.textStatus.text = "Basic image loaded successfully!"
                },
                onError = { _, _ ->
                    binding.textStatus.text = "Failed to load basic image"
                }
            )
        }
    }
    
    private fun loadOptimizedImage() {
        binding.textStatus.text = "Loading optimized image..."
        
        // Optimized image loading with Coil
        binding.imageView.load("https://picsum.photos/500/300") {
            // Enable crossfade animation
            crossfade(true)
            crossfade(300) // 300ms duration
            
            // Resize image to fit view
            size(500, 300)
            
            // Placeholder and error images
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_gallery)
            
            // Listener for loading states
            listener(
                onStart = { placeholder ->
                    binding.textStatus.text = "Loading optimized image..."
                },
                onSuccess = { _, _ ->
                    binding.textStatus.text = "Optimized image loaded successfully!"
                },
                onError = { _, _ ->
                    binding.textStatus.text = "Failed to load optimized image"
                }
            )
        }
    }
    
    private fun loadImageWithCache() {
        binding.textStatus.text = "Loading image with cache..."
        
        // Load image with aggressive caching
        binding.imageView.load("https://picsum.photos/600/400") {
            crossfade(true)
            
            // Use center crop to maintain aspect ratio
            scale(coil.size.Scale.FILL)
            
            listener(
                onSuccess = { _, _ ->
                    binding.textStatus.text = "Cached image loaded successfully!"
                    Toast.makeText(this@ImageOptimizationActivity, "Image cached for future use", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
    
    private fun clearImageCache() {
        binding.textStatus.text = "Clearing image cache..."
        
        // Clear Coil's memory cache
        val imageLoader = coil.ImageLoader(this)
        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
        
        binding.textStatus.text = "Image cache cleared!"
        Toast.makeText(this, "Cache cleared", Toast.LENGTH_SHORT).show()
    }
}
