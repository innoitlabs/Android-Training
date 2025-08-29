package com.example.layouts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RelativeLayoutActivity : AppCompatActivity() {
    
    private lateinit var nameInput: EditText
    private lateinit var submitButton: Button
    private lateinit var clearButton: Button
    private lateinit var outputText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relative_example)

        // Initialize views
        initializeViews()
        
        // Set up event listeners
        setupEventListeners()
    }

    private fun initializeViews() {
        nameInput = findViewById(R.id.nameInput)
        submitButton = findViewById(R.id.submitButton)
        clearButton = findViewById(R.id.clearButton)
        outputText = findViewById(R.id.outputText)
    }

    private fun setupEventListeners() {
        // Submit button click listener
        submitButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            when {
                name.isEmpty() -> {
                    outputText.text = getString(R.string.please_enter_name)
                    nameInput.error = getString(R.string.name_required)
                }
                !validateName(name) -> {
                    outputText.text = getString(R.string.invalid_name)
                    nameInput.error = getString(R.string.name_validation_error)
                }
                else -> {
                    val greeting = getString(R.string.hello_format, name)
                    outputText.text = greeting
                    nameInput.error = null
                }
            }
        }

        // Clear button click listener
        clearButton.setOnClickListener {
            nameInput.text.clear()
            outputText.text = getString(R.string.welcome_message)
            nameInput.error = null
        }

        // Text change listener for real-time validation
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                submitButton.isEnabled = s?.toString()?.trim()?.isNotEmpty() == true
                // Clear error when user starts typing
                if (s?.isNotEmpty() == true) {
                    nameInput.error = null
                }
            }
        })
    }

    private fun validateName(name: String): Boolean {
        return name.length >= 2 && name.all { it.isLetter() || it.isWhitespace() }
    }
}
