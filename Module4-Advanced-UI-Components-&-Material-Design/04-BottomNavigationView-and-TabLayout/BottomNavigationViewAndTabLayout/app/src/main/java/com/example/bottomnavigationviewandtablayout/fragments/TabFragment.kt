package com.example.bottomnavigationviewandtablayout.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TabFragment : Fragment() {

    private var content: String? = null

    companion object {
        fun newInstance(content: String) = TabFragment().apply {
            arguments = Bundle().apply { putString("content", content) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = arguments?.getString("content")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textView = TextView(context).apply {
            text = content
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        return textView
    }
}
