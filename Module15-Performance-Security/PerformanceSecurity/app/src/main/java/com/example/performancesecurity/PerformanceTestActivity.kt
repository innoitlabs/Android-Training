package com.example.performancesecurity

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.performancesecurity.databinding.ActivityPerformanceTestBinding
import kotlinx.coroutines.*

class PerformanceTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerformanceTestBinding
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformanceTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.btnStartupTest.setOnClickListener {
            runStartupTest()
        }
        
        binding.btnMemoryTest.setOnClickListener {
            runMemoryTest()
        }
        
        binding.btnCpuTest.setOnClickListener {
            runCpuTest()
        }
        
        binding.btnNetworkTest.setOnClickListener {
            runNetworkTest()
        }
        
        binding.btnClearResults.setOnClickListener {
            clearResults()
        }
    }
    
    private fun runStartupTest() {
        binding.textResults.append("Running startup test...\n")
        
        val startTime = SystemClock.elapsedRealtime()
        
        // Simulate startup operations
        scope.launch(Dispatchers.IO) {
            // Simulate heavy initialization
            delay(100)
            
            withContext(Dispatchers.Main) {
                val endTime = SystemClock.elapsedRealtime()
                val duration = endTime - startTime
                binding.textResults.append("Startup test completed in ${duration}ms\n")
                binding.textStatus.text = "Startup test completed"
            }
        }
    }
    
    private fun runMemoryTest() {
        binding.textResults.append("Running memory test...\n")
        
        val runtime = Runtime.getRuntime()
        val initialMemory = runtime.totalMemory() - runtime.freeMemory()
        
        scope.launch(Dispatchers.IO) {
            // Simulate memory-intensive operations
            val list = mutableListOf<String>()
            repeat(10000) {
                list.add("Test data $it")
            }
            
            val finalMemory = runtime.totalMemory() - runtime.freeMemory()
            val memoryUsed = finalMemory - initialMemory
            
            withContext(Dispatchers.Main) {
                binding.textResults.append("Memory test completed. Used: ${memoryUsed / 1024}KB\n")
                binding.textStatus.text = "Memory test completed"
            }
        }
    }
    
    private fun runCpuTest() {
        binding.textResults.append("Running CPU test...\n")
        
        val startTime = SystemClock.elapsedRealtime()
        
        scope.launch(Dispatchers.Default) {
            // Simulate CPU-intensive operations
            var result = 0.0
            repeat(1000000) {
                result += Math.sqrt(it.toDouble())
            }
            
            val endTime = SystemClock.elapsedRealtime()
            val duration = endTime - startTime
            
            withContext(Dispatchers.Main) {
                binding.textResults.append("CPU test completed in ${duration}ms. Result: $result\n")
                binding.textStatus.text = "CPU test completed"
            }
        }
    }
    
    private fun runNetworkTest() {
        binding.textResults.append("Running network test...\n")
        
        val startTime = SystemClock.elapsedRealtime()
        
        scope.launch(Dispatchers.IO) {
            try {
                val url = java.net.URL("https://httpbin.org/json")
                val connection = url.openConnection() as java.net.HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                
                val responseCode = connection.responseCode
                val responseSize = connection.contentLength
                
                val endTime = SystemClock.elapsedRealtime()
                val duration = endTime - startTime
                
                withContext(Dispatchers.Main) {
                    binding.textResults.append("Network test completed in ${duration}ms. Response: $responseCode, Size: $responseSize bytes\n")
                    binding.textStatus.text = "Network test completed"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.textResults.append("Network test failed: ${e.message}\n")
                    binding.textStatus.text = "Network test failed"
                }
            }
        }
    }
    
    private fun clearResults() {
        binding.textResults.text = ""
        binding.textStatus.text = "Results cleared"
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
