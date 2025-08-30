package com.example.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    
    private lateinit var animatedView: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        animatedView = findViewById(R.id.animated_view)
        
        setupButtons()
        startInitialAnimation()
    }
    
    private fun setupButtons() {
        // Property Animation Button
        findViewById<Button>(R.id.btn_property_animation).setOnClickListener {
            demonstratePropertyAnimations()
        }
        
        // View Animation Button
        findViewById<Button>(R.id.btn_view_animation).setOnClickListener {
            demonstrateViewAnimations()
        }
        
        // MotionLayout Button
        findViewById<Button>(R.id.btn_motion_layout).setOnClickListener {
            startActivity(Intent(this, MotionLayoutActivity::class.java))
        }
        
        // Custom View Button
        findViewById<Button>(R.id.btn_custom_view).setOnClickListener {
            startActivity(Intent(this, CustomViewActivity::class.java))
        }
        
        // Gesture Detection Button
        findViewById<Button>(R.id.btn_gesture_detection).setOnClickListener {
            startActivity(Intent(this, GestureActivity::class.java))
        }
        
        // RecyclerView Animation Button
        findViewById<Button>(R.id.btn_recycler_animation).setOnClickListener {
            startActivity(Intent(this, RecyclerActivity::class.java))
        }
        
        // Shared Element Transition Button
        findViewById<Button>(R.id.btn_shared_transition).setOnClickListener {
            demonstrateSharedElementTransition()
        }
    }
    
    private fun startInitialAnimation() {
        // Fade in the animated view when activity starts
        animatedView.alpha = 0f
        animatedView.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(500)
            .start()
    }
    
    private fun demonstratePropertyAnimations() {
        // Create a complex animation sequence
        val scaleX = ObjectAnimator.ofFloat(animatedView, "scaleX", 1f, 1.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(animatedView, "scaleY", 1f, 1.5f, 1f)
        val rotation = ObjectAnimator.ofFloat(animatedView, "rotation", 0f, 360f)
        val translationY = ObjectAnimator.ofFloat(animatedView, "translationY", 0f, -100f, 0f)
        
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, rotation, translationY)
        animatorSet.duration = 2000
        
        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                Toast.makeText(this@MainActivity, "Property Animation Complete!", Toast.LENGTH_SHORT).show()
            }
        })
        
        animatorSet.start()
    }
    
    private fun demonstrateViewAnimations() {
        // Load and start view animations
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in)
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        
        // Start animations in sequence
        animatedView.startAnimation(fadeIn)
        
        fadeIn.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                animatedView.startAnimation(scaleIn)
            }
            
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
        
        scaleIn.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                animatedView.startAnimation(slideIn)
                Toast.makeText(this@MainActivity, "View Animation Complete!", Toast.LENGTH_SHORT).show()
            }
            
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
    }
    
    private fun demonstrateSharedElementTransition() {
        val intent = Intent(this, DetailActivity::class.java)
        
        // Set up shared element transition
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            animatedView,
            "sharedElement"
        )
        
        startActivity(intent, options.toBundle())
    }
}