package com.example.fragmenttransactions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), HomeFragment.OnMessageSendListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Load HomeFragment as initial fragment if this is the first creation
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
    
    // Interface implementation for fragment communication
    override fun onMessageSend(message: String) {
        val detailFragment = DetailFragment.newInstance(message)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack("home_to_detail")
            .commit()
    }
}