package com.example.animations

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MotionLayoutActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout)
        
        Toast.makeText(this, "MotionLayout Demo - Coming Soon!", Toast.LENGTH_SHORT).show()
    }
}
