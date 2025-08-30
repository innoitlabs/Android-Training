package com.example.animations

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GestureActivity : AppCompatActivity() {
    
    private lateinit var gestureDetector: GestureDetector
    private lateinit var gestureText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture)
        
        gestureText = findViewById(R.id.gesture_text)
        
        setupGestureDetector()
    }
    
    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                showGesture("Single Tap")
                return true
            }
            
            override fun onDoubleTap(e: MotionEvent): Boolean {
                showGesture("Double Tap")
                return true
            }
            
            override fun onLongPress(e: MotionEvent) {
                showGesture("Long Press")
            }
            
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                showGesture("Scroll")
                return true
            }
            
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                showGesture("Fling")
                return true
            }
        })
    }
    
    private fun showGesture(gesture: String) {
        gestureText.text = "Detected: $gesture"
        Toast.makeText(this, gesture, Toast.LENGTH_SHORT).show()
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}
