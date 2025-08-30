package com.example.animations

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        
        // Set the transition name for the shared element
        findViewById<android.view.View>(R.id.detail_shared_element).transitionName = "sharedElement"
    }
    
    fun finish(view: View) {
        finish()
    }
}
