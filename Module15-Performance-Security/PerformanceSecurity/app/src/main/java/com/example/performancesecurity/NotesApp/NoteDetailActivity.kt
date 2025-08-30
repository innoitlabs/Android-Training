package com.example.performancesecurity.NotesApp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.performancesecurity.databinding.ActivityNoteDetailBinding
import java.util.*

class NoteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteDetailBinding
    private lateinit var storageManager: SecureStorageManager
    private var currentNoteId: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        storageManager = SecureStorageManager(this)
        
        setupUI()
        loadNote()
    }
    
    private fun setupUI() {
        binding.btnSave.setOnClickListener {
            saveNote()
        }
        
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }
    
    private fun loadNote() {
        currentNoteId = intent.getStringExtra("note_id")
        
        if (currentNoteId != null) {
            // Load existing note
            val notes = storageManager.getNotes()
            val note = notes.find { it.id == currentNoteId }
            
            note?.let {
                binding.editTextTitle.setText(it.title)
                binding.editTextContent.setText(it.content)
            }
        }
    }
    
    private fun saveNote() {
        val title = binding.editTextTitle.text.toString()
        val content = binding.editTextContent.text.toString()
        
        if (title.isNotEmpty()) {
            val note = Note(
                id = currentNoteId ?: UUID.randomUUID().toString(),
                title = title,
                content = content,
                updatedAt = Date()
            )
            
            storageManager.saveNote(note)
            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
        }
    }
}
