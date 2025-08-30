package com.example.devicefeatures

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupNavigationButtons()
    }
    
    private fun setupNavigationButtons() {
        findViewById<Button>(R.id.btnLocation).setOnClickListener {
            startActivity(Intent(this, LocationActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnSensor).setOnClickListener {
            startActivity(Intent(this, SensorActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnBiometric).setOnClickListener {
            startActivity(Intent(this, BiometricActivity::class.java))
        }
        
        findViewById<Button>(R.id.btnFile).setOnClickListener {
            startActivity(Intent(this, FileActivity::class.java))
        }
    }
}