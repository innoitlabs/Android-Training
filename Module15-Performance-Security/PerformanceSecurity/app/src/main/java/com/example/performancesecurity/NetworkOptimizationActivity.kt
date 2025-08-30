package com.example.performancesecurity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.performancesecurity.databinding.ActivityNetworkOptimizationBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class NetworkOptimizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNetworkOptimizationBinding
    private lateinit var optimizedClient: OkHttpClient
    private lateinit var basicClient: OkHttpClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkOptimizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initializeClients()
        setupUI()
    }
    
    private fun initializeClients() {
        // Basic client without optimization
        basicClient = OkHttpClient.Builder()
            .build()
        
        // Optimized client with compression and caching
        optimizedClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("User-Agent", "PerformanceSecurityApp/1.0")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    private fun setupUI() {
        binding.btnBasicRequest.setOnClickListener {
            makeBasicRequest()
        }
        
        binding.btnOptimizedRequest.setOnClickListener {
            makeOptimizedRequest()
        }
        
        binding.btnMultipleRequests.setOnClickListener {
            makeMultipleRequests()
        }
        
        binding.btnClearLog.setOnClickListener {
            clearLog()
        }
    }
    
    private fun makeBasicRequest() {
        binding.textLog.append("Making basic request...\n")
        
        val request = Request.Builder()
            .url("https://httpbin.org/json")
            .build()
        
        basicClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    val responseSize = response.body?.contentLength() ?: 0
                    binding.textLog.append("Basic request completed. Response size: ${responseSize} bytes\n")
                    binding.textStatus.text = "Basic request completed"
                }
            }
            
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    binding.textLog.append("Basic request failed: ${e.message}\n")
                    binding.textStatus.text = "Basic request failed"
                }
            }
        })
    }
    
    private fun makeOptimizedRequest() {
        binding.textLog.append("Making optimized request...\n")
        
        val request = Request.Builder()
            .url("https://httpbin.org/json")
            .build()
        
        optimizedClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                runOnUiThread {
                    val responseSize = response.body?.contentLength() ?: 0
                    val encoding = response.header("Content-Encoding", "none")
                    binding.textLog.append("Optimized request completed. Response size: ${responseSize} bytes, Encoding: $encoding\n")
                    binding.textStatus.text = "Optimized request completed"
                }
            }
            
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    binding.textLog.append("Optimized request failed: ${e.message}\n")
                    binding.textStatus.text = "Optimized request failed"
                }
            }
        })
    }
    
    private fun makeMultipleRequests() {
        binding.textLog.append("Making multiple requests...\n")
        
        val urls = listOf(
            "https://httpbin.org/json",
            "https://httpbin.org/xml",
            "https://httpbin.org/html"
        )
        
        urls.forEachIndexed { index, url ->
            val request = Request.Builder()
                .url(url)
                .build()
            
            optimizedClient.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    runOnUiThread {
                        val responseSize = response.body?.contentLength() ?: 0
                        binding.textLog.append("Request ${index + 1} completed. Size: ${responseSize} bytes\n")
                        
                        if (index == urls.size - 1) {
                            binding.textStatus.text = "All requests completed"
                        }
                    }
                }
                
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    runOnUiThread {
                        binding.textLog.append("Request ${index + 1} failed: ${e.message}\n")
                    }
                }
            })
        }
    }
    
    private fun clearLog() {
        binding.textLog.text = ""
        binding.textStatus.text = "Log cleared"
    }
}
