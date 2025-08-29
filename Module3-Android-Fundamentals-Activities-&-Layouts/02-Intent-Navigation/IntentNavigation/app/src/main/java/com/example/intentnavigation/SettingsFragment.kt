package com.example.intentnavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * SettingsFragment - Demonstrates Navigation Component navigation
 * 
 * This fragment shows:
 * 1. Simple navigation without arguments
 * 2. Navigation back to previous fragment
 * 3. Settings-like interface
 */
class SettingsFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize UI components
        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle)
        val textViewInfo = view.findViewById<TextView>(R.id.textViewInfo)
        val buttonBack = view.findViewById<Button>(R.id.buttonBack)
        val buttonAbout = view.findViewById<Button>(R.id.buttonAbout)
        
        // Display title
        textViewTitle.text = "Settings"
        
        // Display information about Navigation Component
        val info = buildString {
            appendLine("Navigation Component Features:")
            appendLine()
            appendLine("1. Type-safe Navigation:")
            appendLine("   • SafeArgs for argument passing")
            appendLine("   • Compile-time safety")
            appendLine("   • No runtime crashes")
            appendLine()
            appendLine("2. Back Stack Management:")
            appendLine("   • Automatic back stack handling")
            appendLine("   • Proper up navigation")
            appendLine("   • Deep linking support")
            appendLine()
            appendLine("3. Visual Navigation Graph:")
            appendLine("   • See all destinations")
            appendLine("   • Understand app flow")
            appendLine("   • Easy to maintain")
            appendLine()
            appendLine("4. Benefits over Traditional Intents:")
            appendLine("   • Better performance")
            appendLine("   • Cleaner architecture")
            appendLine("   • Easier testing")
            appendLine("   • Better user experience")
        }
        textViewInfo.text = info
        
        // Setup button click listeners
        buttonBack.setOnClickListener {
            // Navigate back to previous fragment
            findNavController().navigateUp()
        }
        
        buttonAbout.setOnClickListener {
            showAboutDialog()
        }
    }
    
    /**
     * Show about dialog
     */
    private fun showAboutDialog() {
        val aboutText = buildString {
            appendLine("Intent Navigation Demo App")
            appendLine()
            appendLine("This app demonstrates:")
            appendLine("• Explicit Intents")
            appendLine("• Implicit Intents")
            appendLine("• Navigation Component")
            appendLine("• SafeArgs")
            appendLine("• Best Practices")
            appendLine()
            appendLine("Version: 1.0")
            appendLine("Built with Kotlin & Navigation Component")
        }
        
        // Create and show dialog
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("About")
            .setMessage(aboutText)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
