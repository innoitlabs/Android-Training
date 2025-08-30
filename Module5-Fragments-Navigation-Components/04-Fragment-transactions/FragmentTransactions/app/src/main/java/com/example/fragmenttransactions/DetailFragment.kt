package com.example.fragmenttransactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailFragment : Fragment() {
    
    companion object {
        private const val ARG_MESSAGE = "message"
        
        fun newInstance(message: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }
    
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
        val responseEditText = view.findViewById<EditText>(R.id.responseEditText)
        val sendResponseButton = view.findViewById<Button>(R.id.sendResponseButton)
        val goBackButton = view.findViewById<Button>(R.id.goBackButton)
        
        // Display message from interface-based communication
        val messageFromInterface = arguments?.getString(ARG_MESSAGE)
        if (!messageFromInterface.isNullOrEmpty()) {
            messageTextView.text = "Message from Interface: $messageFromInterface"
        }
        
        // Observe message from SharedViewModel
        sharedViewModel.messageFromHome.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                messageTextView.text = "Message from ViewModel: $message"
            }
        }
        
        // Send response back to HomeFragment via SharedViewModel
        sendResponseButton.setOnClickListener {
            val response = responseEditText.text.toString()
            if (response.isNotEmpty()) {
                sharedViewModel.sendFromDetail(response)
                goBack()
            }
        }
        
        // Go back button
        goBackButton.setOnClickListener {
            goBack()
        }
    }
    
    private fun goBack() {
        if (parentFragmentManager.backStackEntryCount > 0) {
            parentFragmentManager.popBackStack()
        }
    }
}
