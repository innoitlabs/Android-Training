package com.example.floatingbuttonsnackbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    
    private lateinit var taskListText: TextView
    private val tasks = mutableListOf<String>()
    private var taskCounter = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        taskListText = findViewById(R.id.taskListText)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        
        // Set up FAB click listener
        fab.setOnClickListener { view ->
            addNewTask(view)
        }
    }
    
    private fun addNewTask(view: android.view.View) {
        val newTask = "Task $taskCounter"
        tasks.add(newTask)
        taskCounter++
        
        updateTaskDisplay()
        
        // Show Snackbar with undo option
        Snackbar.make(view, "Task added: $newTask", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                undoLastTask(view)
            }
            .show()
    }
    
    private fun undoLastTask(view: android.view.View) {
        if (tasks.isNotEmpty()) {
            val removedTask = tasks.removeAt(tasks.lastIndex)
            taskCounter--
            updateTaskDisplay()
            
            Snackbar.make(view, "Undone: $removedTask", Snackbar.LENGTH_SHORT).show()
        }
    }
    
    private fun updateTaskDisplay() {
        if (tasks.isEmpty()) {
            taskListText.text = "No tasks yet. Tap the + button to add a task!"
        } else {
            val taskText = tasks.joinToString("\n") { "• $it" }
            taskListText.text = taskText
        }
    }
}