package com.example.roomdatabase

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val ageInput = findViewById<EditText>(R.id.ageInput)
        val addButton = findViewById<Button>(R.id.addButton)
        val outputText = findViewById<TextView>(R.id.outputText)
        val userCountText = findViewById<TextView>(R.id.userCountText)

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString().toIntOrNull() ?: 0
            
            if (name.isNotEmpty() && age > 0) {
                val user = User(name = name, age = age)
                userViewModel.insert(user)
                nameInput.text.clear()
                ageInput.text.clear()
            }
        }

        userViewModel.allUsers.observe(this) { users ->
            userCountText.text = "User Count: ${users.size}"
            if (users.isEmpty()) {
                outputText.text = "No users added yet"
            } else {
                outputText.text = users.joinToString("\n\n") { 
                    "ID: ${it.id}\nName: ${it.name}\nAge: ${it.age}" 
                }
            }
        }
    }
}