package com.example.intentnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

/**
 * HomeFragment - Demonstrates Navigation Component with SafeArgs
 * 
 * This fragment shows:
 * 1. How to receive arguments using SafeArgs
 * 2. How to navigate to other fragments
 * 3. Type-safe argument passing
 */
class HomeFragment : Fragment() {
    
    // SafeArgs - automatically generated class for type-safe argument passing
    private val args: HomeFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize UI components
        val textViewWelcome = view.findViewById<TextView>(R.id.textViewWelcome)
        val textViewArgs = view.findViewById<TextView>(R.id.textViewArgs)
        val buttonNavigateToDetail = view.findViewById<Button>(R.id.buttonNavigateToDetail)
        val buttonNavigateToSettings = view.findViewById<Button>(R.id.buttonNavigateToSettings)
        
        // Display welcome message with username from SafeArgs
        val username = args.username ?: "Guest"
        textViewWelcome.text = "Welcome to Navigation Component, $username!"
        
        // Display received arguments
        val argsInfo = buildString {
            appendLine("Arguments received via SafeArgs:")
            appendLine("Username: ${args.username ?: "Not provided"}")
            appendLine("User ID: ${args.userId}")
            appendLine("Is Logged In: ${args.isLoggedIn}")
        }
        textViewArgs.text = argsInfo
        
        // Setup navigation buttons
        buttonNavigateToDetail.setOnClickListener {
            navigateToDetailFragment()
        }
        
        buttonNavigateToSettings.setOnClickListener {
            navigateToSettingsFragment()
        }
    }
    
    /**
     * Navigate to DetailFragment using Navigation Component
     * This demonstrates how to pass arguments safely using SafeArgs
     */
    private fun navigateToDetailFragment() {
        // Create action with arguments using SafeArgs
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
            username = args.username ?: "Guest",
            userId = args.userId,
            isLoggedIn = args.isLoggedIn
        )
        
        // Navigate using the action
        findNavController().navigate(action)
    }
    
    /**
     * Navigate to SettingsFragment
     */
    private fun navigateToSettingsFragment() {
        // Navigate using resource ID (alternative approach)
        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
    }
}
