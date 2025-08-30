package com.example.navigationgraphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        
        val detailText = view.findViewById<TextView>(R.id.detailText)
        val usernameText = view.findViewById<TextView>(R.id.usernameText)
        
        detailText.text = "Welcome to Detail Screen"
        usernameText.text = "Hello, ${args.username}!"
        
        return view
    }
}
