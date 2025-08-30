package com.example.cardview

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    
    private lateinit var basicCardView: CardView
    private lateinit var materialCardView: MaterialCardView
    private lateinit var checkableCardView: MaterialCardView
    private lateinit var profileCardView: MaterialCardView
    private lateinit var interactiveCardView: MaterialCardView
    
    private lateinit var cardButton: Button
    private lateinit var profileButton: Button
    
    private var isInteractiveCardBlue = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        initializeViews()
        
        // Set up click listeners
        setupClickListeners()
        
        // Set up checkable card
        setupCheckableCard()
    }
    
    private fun initializeViews() {
        basicCardView = findViewById(R.id.basicCardView)
        materialCardView = findViewById(R.id.materialCardView)
        checkableCardView = findViewById(R.id.checkableCardView)
        profileCardView = findViewById(R.id.profileCardView)
        interactiveCardView = findViewById(R.id.interactiveCardView)
        
        cardButton = findViewById(R.id.cardButton)
        profileButton = findViewById(R.id.profileButton)
    }
    
    private fun setupClickListeners() {
        // Basic CardView click
        basicCardView.setOnClickListener {
            Toast.makeText(this, "Basic CardView clicked!", Toast.LENGTH_SHORT).show()
        }
        
        // MaterialCardView click
        materialCardView.setOnClickListener {
            Toast.makeText(this, "MaterialCardView clicked!", Toast.LENGTH_SHORT).show()
        }
        
        // Card button click
        cardButton.setOnClickListener {
            Toast.makeText(this, "Card button clicked! This demonstrates button interaction within a card.", Toast.LENGTH_LONG).show()
        }
        
        // Profile card click
        profileCardView.setOnClickListener {
            Toast.makeText(this, "Profile card clicked! This could open a detailed profile view.", Toast.LENGTH_SHORT).show()
        }
        
        // Profile button click
        profileButton.setOnClickListener {
            Toast.makeText(this, "View Profile button clicked! This would navigate to the full profile.", Toast.LENGTH_SHORT).show()
        }
        
        // Interactive card (color toggle)
        interactiveCardView.setOnClickListener {
            toggleInteractiveCardColor()
        }
    }
    
    private fun setupCheckableCard() {
        // Make the card checkable
        checkableCardView.isCheckable = true
        checkableCardView.isChecked = false
        
        // Set up checked change listener
        checkableCardView.setOnCheckedChangeListener { card, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Card selected! Checkable state: true", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Card deselected! Checkable state: false", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun toggleInteractiveCardColor() {
        if (isInteractiveCardBlue) {
            // Change to light purple
            interactiveCardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple_100))
            Toast.makeText(this, "Background changed to purple! Tap again to toggle.", Toast.LENGTH_SHORT).show()
        } else {
            // Change back to light blue
            interactiveCardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_blue))
            Toast.makeText(this, "Background changed to blue! Tap again to toggle.", Toast.LENGTH_SHORT).show()
        }
        isInteractiveCardBlue = !isInteractiveCardBlue
    }
}