package com.example.navigationgraphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val button = view.findViewById<Button>(R.id.navigateButton)
        button.setOnClickListener {
            // Navigate to detail fragment with data using Safe Args
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment("John Doe")
            findNavController().navigate(action)
        }

        return view
    }
}
