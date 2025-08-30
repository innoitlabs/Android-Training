package com.example.performancesecurity.NotesApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.performancesecurity.databinding.ActivityNotesMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesMainBinding
    private lateinit var storageManager: SecureStorageManager
    private lateinit var notesAdapter: NotesAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        storageManager = SecureStorageManager(this)
        setupRecyclerView()
        setupFab()
        loadNotes()
        
        // Schedule background sync
        SyncWorker.scheduleSync(this)
    }
    
    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(
            onNoteClick = { note ->
                // Navigate to note detail
                val intent = Intent(this, NoteDetailActivity::class.java)
                intent.putExtra("note_id", note.id)
                startActivity(intent)
            },
            onNoteDelete = { note ->
                storageManager.deleteNote(note.id)
                loadNotes()
            }
        )
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }
    }
    
    private fun setupFab() {
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun loadNotes() {
        val notes = storageManager.getNotes()
        notesAdapter.updateNotes(notes)
        
        if (notes.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }
    
    override fun onResume() {
        super.onResume()
        loadNotes()
    }
}
