package com.example.fragementlifecycle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

class DetailFragment : Fragment() {
    private val TAG = "DetailFragment"

    override fun onAttach(context: android.content.Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")
        
        val args: DetailFragmentArgs by navArgs()
        val username = args.username
        val userId = args.userId
        
        val welcomeText = view.findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome, $username! (ID: $userId)"
        
        view.findViewById<Button>(R.id.backButton).setOnClickListener {
            findNavController().navigateUp()
        }
        
        view.findViewById<Button>(R.id.settingsButton).setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_settingsFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach called")
    }
}
