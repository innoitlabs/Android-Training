package com.example.performancesecurity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.performancesecurity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
    }
    
    private fun setupUI() {
        binding.btnMemoryLeakDemo.setOnClickListener {
            startActivity(Intent(this, MemoryLeakDemoActivity::class.java))
        }
        
        binding.btnImageOptimization.setOnClickListener {
            startActivity(Intent(this, ImageOptimizationActivity::class.java))
        }
        
        binding.btnSecureStorage.setOnClickListener {
            startActivity(Intent(this, SecureStorageActivity::class.java))
        }
        
        binding.btnNetworkOptimization.setOnClickListener {
            startActivity(Intent(this, NetworkOptimizationActivity::class.java))
        }
        
                            binding.btnNotesApp.setOnClickListener {
                        startActivity(Intent(this, com.example.performancesecurity.NotesApp.MainActivity::class.java))
                    }
        
        binding.btnPerformanceTest.setOnClickListener {
            startActivity(Intent(this, PerformanceTestActivity::class.java))
        }
    }
}