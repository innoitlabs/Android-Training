package com.example.performancesecurity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.performancesecurity.databinding.ActivityMemoryLeakDemoBinding
import java.lang.ref.WeakReference

class MemoryLeakDemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemoryLeakDemoBinding
    private lateinit var handler: Handler
    private val updateRunnable = Runnable {
        // Use WeakReference or check if activity is still valid
        if (!isFinishing && !isDestroyed) {
            binding.textStatus.text = "Updated at ${System.currentTimeMillis()}"
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoryLeakDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        handler = Handler(Looper.getMainLooper())
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.btnStartLeak.setOnClickListener {
            startMemoryLeakDemo()
        }
        
        binding.btnStartFixed.setOnClickListener {
            startFixedDemo()
        }
        
        binding.btnClear.setOnClickListener {
            clearStatus()
        }
    }
    
    private fun startMemoryLeakDemo() {
        binding.textStatus.text = "Starting memory leak demo..."
        
        // ❌ This creates a memory leak - storing Activity reference
        LeakHolder.leakedActivity = this
        
        // Post delayed task that will hold reference to Activity
        handler.postDelayed(updateRunnable, 5000)
        
        Toast.makeText(this, "Memory leak demo started. Check LeakCanary!", Toast.LENGTH_LONG).show()
    }
    
    private fun startFixedDemo() {
        binding.textStatus.text = "Starting fixed demo..."
        
        // ✅ Fixed - use WeakReference
        LeakHolder.weakActivityRef = WeakReference(this)
        
        // Post delayed task with proper cleanup
        handler.postDelayed(updateRunnable, 5000)
        
        Toast.makeText(this, "Fixed demo started. No memory leaks!", Toast.LENGTH_LONG).show()
    }
    
    private fun clearStatus() {
        binding.textStatus.text = "Status cleared"
        handler.removeCallbacks(updateRunnable)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // ✅ Remove callbacks to prevent memory leaks
        handler.removeCallbacks(updateRunnable)
    }
}

// Demo class to demonstrate memory leaks
object LeakHolder {
    // ❌ This will cause memory leaks
    var leakedActivity: MemoryLeakDemoActivity? = null
    
    // ✅ This is safe - uses WeakReference
    var weakActivityRef: WeakReference<MemoryLeakDemoActivity>? = null
}
