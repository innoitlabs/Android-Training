package com.example.intentnavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

/**
 * NavigationActivity - Demonstrates Jetpack Navigation Component
 * 
 * This activity shows:
 * 1. How to set up Navigation Component
 * 2. How to host fragments with navigation
 * 3. Modern navigation patterns in Android
 */
class NavigationActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        
        // Get the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        
        // Get the NavController
        val navController = navHostFragment.navController
        
        // Setup action bar with navigation controller
        setupActionBarWithNavController(navController)
        
        // Pass username to the first fragment if provided
        val username = intent.getStringExtra("USERNAME")
        if (username != null) {
            // Set the username as a global argument that can be accessed by all fragments
            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            navController.setGraph(R.navigation.nav_graph, bundle)
        }
    }
    
    /**
     * Handle up navigation in action bar
     */
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
