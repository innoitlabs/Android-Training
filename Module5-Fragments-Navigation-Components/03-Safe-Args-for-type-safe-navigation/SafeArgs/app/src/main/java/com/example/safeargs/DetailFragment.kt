package com.example.safeargs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class DetailFragment : Fragment() {
    // Safe Args generates this property automatically
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val detailTextView = view.findViewById<TextView>(R.id.detailTextView)
        val backButton = view.findViewById<Button>(R.id.backButton)

        // Display data based on what was passed
        val displayText = StringBuilder()

        if (args.user != null) {
            // Display User object data
            val user = args.user!!
            displayText.append("User Details:\n")
            displayText.append("ID: ${user.id}\n")
            displayText.append("Name: ${user.name}\n")
            displayText.append("Email: ${user.email}\n")
            displayText.append("Age: ${user.age}\n")
        } else {
            // Display primitive type data
            displayText.append("Hello ${args.username}, you are ${args.age} years old!\n")
            
            if (args.isActive) {
                displayText.append("Status: Active")
            } else {
                displayText.append("Status: Inactive")
            }
        }

        detailTextView.text = displayText.toString()

        // Back button
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return view
    }
}
