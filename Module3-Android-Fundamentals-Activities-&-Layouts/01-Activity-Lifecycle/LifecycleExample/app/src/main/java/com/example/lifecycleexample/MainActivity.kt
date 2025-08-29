package com.example.lifecycleexample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityLifecycle"
    
    // UI Components
    private lateinit var titleTextView: TextView
    private lateinit var counterTextView: TextView
    private lateinit var timerTextView: TextView
    private lateinit var editText: EditText
    private lateinit var incrementButton: Button
    private lateinit var resetButton: Button
    private lateinit var statusTextView: TextView
    
    // State variables
    private var counter = 0
    private var savedText = ""
    private var timer: Timer? = null
    private var timerCounter = 0
    private lateinit var sharedPreferences: SharedPreferences
    
    // Lifecycle tracking
    private var onCreateCount = 0
    private var onStartCount = 0
    private var onResumeCount = 0
    private var onPauseCount = 0
    private var onStopCount = 0
    private var onDestroyCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        onCreateCount++
        Log.d(TAG, "🎬 onCreate called (Count: $onCreateCount)")
        showToast("🎬 onCreate called")
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LifecycleDemo", Context.MODE_PRIVATE)
        
        // Initialize UI components
        initializeViews()
        
        // Restore state if available
        savedInstanceState?.let { bundle ->
            counter = bundle.getInt("counter", 0)
            savedText = bundle.getString("saved_text", "")
            timerCounter = bundle.getInt("timer_counter", 0)
            updateUI()
            Log.d(TAG, "📦 State restored from savedInstanceState")
        }
        
        // Load persistent state
        counter = sharedPreferences.getInt("persistent_counter", counter)
        updateUI()
        Log.d(TAG, "💾 Persistent state loaded")
    }

    override fun onStart() {
        super.onStart()
        onStartCount++
        Log.d(TAG, "👁️ onStart called (Count: $onStartCount)")
        showToast("👁️ onStart called")
        
        // Update status
        updateStatus("Activity is becoming visible")
    }

    override fun onResume() {
        super.onResume()
        onResumeCount++
        Log.d(TAG, "▶️ onResume called (Count: $onResumeCount)")
        showToast("▶️ onResume called")
        
        // Restore saved text
        if (savedText.isNotEmpty()) {
            editText.setText(savedText)
            Log.d(TAG, "📝 Text restored: $savedText")
        }
        
        // Start timer
        startTimer()
        
        // Update status
        updateStatus("Activity is interactive")
    }

    override fun onPause() {
        super.onPause()
        onPauseCount++
        Log.d(TAG, "⏸️ onPause called (Count: $onPauseCount)")
        showToast("⏸️ onPause called")
        
        // Save current text
        savedText = editText.text.toString()
        Log.d(TAG, "💾 Text saved: $savedText")
        
        // Save persistent state
        sharedPreferences.edit().putInt("persistent_counter", counter).apply()
        Log.d(TAG, "💾 Persistent state saved")
        
        // Stop timer
        stopTimer()
        
        // Update status
        updateStatus("Activity is partially hidden")
    }

    override fun onStop() {
        super.onStop()
        onStopCount++
        Log.d(TAG, "⏹️ onStop called (Count: $onStopCount)")
        showToast("⏹️ onStop called")
        
        // Update status
        updateStatus("Activity is completely hidden")
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyCount++
        Log.d(TAG, "💀 onDestroy called (Count: $onDestroyCount)")
        showToast("💀 onDestroy called")
        
        // Ensure timer is stopped
        stopTimer()
        
        // Update status
        updateStatus("Activity is being destroyed")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        
        // Save state for configuration changes
        outState.putInt("counter", counter)
        outState.putString("saved_text", editText.text.toString())
        outState.putInt("timer_counter", timerCounter)
        
        Log.d(TAG, "📦 onSaveInstanceState called")
    }

    private fun initializeViews() {
        titleTextView = findViewById(R.id.titleTextView)
        counterTextView = findViewById(R.id.counterTextView)
        timerTextView = findViewById(R.id.timerTextView)
        editText = findViewById(R.id.editText)
        incrementButton = findViewById(R.id.incrementButton)
        resetButton = findViewById(R.id.resetButton)
        statusTextView = findViewById(R.id.statusTextView)
        
        // Set up click listeners
        incrementButton.setOnClickListener {
            counter++
            updateUI()
            Log.d(TAG, "🔢 Counter incremented to: $counter")
        }
        
        resetButton.setOnClickListener {
            counter = 0
            timerCounter = 0
            savedText = ""
            editText.setText("")
            sharedPreferences.edit().clear().apply()
            updateUI()
            showToast("🔄 Counter reset!")
            Log.d(TAG, "🔄 All data reset")
        }
    }

    private fun updateUI() {
        counterTextView.text = "Counter: $counter"
        timerTextView.text = "Timer: $timerCounter seconds"
        
        // Update lifecycle counts
        titleTextView.text = """
            Activity Lifecycle Demo
            
            Lifecycle Counts:
            onCreate: $onCreateCount
            onStart: $onStartCount
            onResume: $onResumeCount
            onPause: $onPauseCount
            onStop: $onStopCount
            onDestroy: $onDestroyCount
        """.trimIndent()
    }

    private fun updateStatus(status: String) {
        statusTextView.text = "Status: $status"
    }

    private fun startTimer() {
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    timerCounter++
                    updateUI()
                }
            }
        }, 0, 1000) // Update every 1 second
        
        Log.d(TAG, "⏰ Timer started")
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
        Log.d(TAG, "⏰ Timer stopped")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}