package com.example.safeargs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val ageEditText = view.findViewById<EditText>(R.id.ageEditText)
        val navigateButton = view.findViewById<Button>(R.id.navigateButton)
        val navigateWithUserButton = view.findViewById<Button>(R.id.navigateWithUserButton)

        // Navigate with primitive types
        navigateButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val ageText = ageEditText.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ageText.isEmpty()) {
                Toast.makeText(context, "Please enter an age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null || age <= 0) {
                Toast.makeText(context, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Safe Args generates this method automatically
            val action = HomeFragmentDirections
                .actionHomeFragmentToDetailFragment(name, age, null, true)
            findNavController().navigate(action)
        }

        // Navigate with complex object
        navigateWithUserButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val ageText = ageEditText.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (ageText.isEmpty()) {
                Toast.makeText(context, "Please enter an age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null || age <= 0) {
                Toast.makeText(context, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create User object and navigate
            val user = User(1, name, "$name@example.com", age)
            val action = HomeFragmentDirections
                .actionHomeFragmentToDetailFragment("", 0, user, false)
            findNavController().navigate(action)
        }

        return view
    }
}
