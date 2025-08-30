package com.example.deeplinking

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val goToDetailButton = view.findViewById<Button>(R.id.goToDetailButton)
        goToDetailButton.setOnClickListener {
            val bundle = Bundle().apply {
                putString("itemId", "42")
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }
    }
}
