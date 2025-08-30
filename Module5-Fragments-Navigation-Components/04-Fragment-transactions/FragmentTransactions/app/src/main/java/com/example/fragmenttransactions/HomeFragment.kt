package com.example.fragmenttransactions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class HomeFragment : Fragment() {
    
    // Interface for communication
    interface OnMessageSendListener {
        fun onMessageSend(message: String)
    }
    
    private var listener: OnMessageSendListener? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnMessageSendListener
    }
    
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)
        val sendButton = view.findViewById<Button>(R.id.sendButton)
        val responseTextView = view.findViewById<TextView>(R.id.responseTextView)
        val goToDetailButton = view.findViewById<Button>(R.id.goToDetailButton)
        
        // Interface-based communication
        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotEmpty()) {
                listener?.onMessageSend(message)
            }
        }
        
        // SharedViewModel communication
        goToDetailButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotEmpty()) {
                sharedViewModel.sendFromHome(message)
                navigateToDetail()
            }
        }
        
        // Observe response from DetailFragment via SharedViewModel
        sharedViewModel.messageFromDetail.observe(viewLifecycleOwner) { message ->
            responseTextView.text = "Response from Detail: $message"
        }
        
        // Observe loading state
        sharedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            sendButton.isEnabled = !isLoading
            goToDetailButton.isEnabled = !isLoading
            if (isLoading) {
                sendButton.text = "Sending..."
                goToDetailButton.text = "Sending..."
            } else {
                sendButton.text = "Send via Interface"
                goToDetailButton.text = "Send via ViewModel"
            }
        }
    }
    
    private fun navigateToDetail() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailFragment())
            .addToBackStack("home_to_detail")
            .commit()
    }
}
