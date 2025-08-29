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
 * DetailFragment - Demonstrates receiving arguments via SafeArgs
 * 
 * This fragment shows:
 * 1. How to receive arguments using SafeArgs
 * 2. How to display detailed information
 * 3. Navigation back to previous fragment
 */
class DetailFragment : Fragment() {
    
    // SafeArgs - automatically generated class for type-safe argument passing
    private val args: DetailFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize UI components
        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewDetails = view.findViewById<TextView>(R.id.textViewDetails)
        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        val buttonShare = view.findViewById<Button>(R.id.buttonShare)
        
        // Display title with username
        val username = args.username ?: "Guest"
        textViewTitle.text = "Detail View for $username"
        
        // Display detailed information
        val details = buildString {
            appendLine("User Details:")
            appendLine("Username: ${args.username ?: "Not provided"}")
            appendLine("User ID: ${args.userId}")
            appendLine("Login Status: ${if (args.isLoggedIn) "Logged In" else "Not Logged In"}")
            appendLine()
            appendLine("Navigation Information:")
            appendLine("Fragment: DetailFragment")
            appendLine("Arguments passed via SafeArgs")
            appendLine("Type-safe argument passing")
            appendLine()
            appendLine("Benefits of SafeArgs:")
            appendLine("• Compile-time safety")
            appendLine("• No more getStringExtra() calls")
            appendLine("• Automatic null safety")
            appendLine("• Better IDE support")
        }
        textViewDetails.text = details
        
        // Setup button click listeners
        buttonBack.setOnClickListener {
            // Navigate back to previous fragment
            findNavController().navigateUp()
        }
        
        buttonShare.setOnClickListener {
            shareUserDetails()
        }
    }
    
    /**
     * Share user details using implicit intent
     */
    private fun shareUserDetails() {
        val shareText = buildString {
            appendLine("User Details from Navigation Component")
            appendLine("Username: ${args.username ?: "Guest"}")
            appendLine("User ID: ${args.userId}")
            appendLine("Status: ${if (args.isLoggedIn) "Active" else "Inactive"}")
            appendLine()
            appendLine("Shared from Navigation Component Demo")
        }
        
        // Create share intent
        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
            putExtra(android.content.Intent.EXTRA_SUBJECT, "User Details: ${args.username}")
        }
        
        // Create chooser and start activity
        val chooser = android.content.Intent.createChooser(shareIntent, "Share User Details")
        startActivity(chooser)
    }
}
