package com.example.customviews

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

        setupCustomViews()
    }

    private fun setupCustomViews() {
        // Find custom views
        val circleView = findViewById<CircleView>(R.id.circleView)
        val progressView = findViewById<CustomProgressView>(R.id.progressView)
        val twoColumnLayout = findViewById<TwoColumnLayout>(R.id.twoColumnLayout)

        // Setup CircleView interactions
        circleView.setOnClickListener {
            val colors = arrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA)
            circleView.setColor(colors.random())
            Toast.makeText(this, "Circle color changed!", Toast.LENGTH_SHORT).show()
        }

        // Setup ProgressView animations
        progressView.setOnClickListener {
            val randomProgress = (0..100).random().toFloat()
            progressView.animateProgress(randomProgress, 1500)
            Toast.makeText(this, "Progress animated to ${randomProgress.toInt()}%", Toast.LENGTH_SHORT).show()
        }

        // Setup buttons in TwoColumnLayout
        val buttons = arrayOf(
            findViewById<Button>(R.id.button1),
            findViewById<Button>(R.id.button2),
            findViewById<Button>(R.id.button3),
            findViewById<Button>(R.id.button4),
            findViewById<Button>(R.id.button5),
            findViewById<Button>(R.id.button6)
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Toast.makeText(this, "Button ${index + 1} clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        // Start with some initial animations
        progressView.animateProgress(75f, 2000)
    }
}