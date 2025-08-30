package com.example.performancesecurity.NotesApp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.performancesecurity.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onNoteDelete: (Note) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    
    private val notes = mutableListOf<Note>()
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NoteViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }
    
    override fun getItemCount() = notes.size
    
    inner class NoteViewHolder(private val binding: ItemNoteBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(note: Note) {
            binding.apply {
                textTitle.text = note.title
                textContent.text = note.content
                textDate.text = dateFormat.format(note.updatedAt)
                
                // Load image with Coil
                note.imageUrl?.let { url ->
                    imageNote.load(url) {
                        crossfade(true)
                        placeholder(android.R.drawable.ic_menu_gallery)
                        error(android.R.drawable.ic_menu_gallery)
                    }
                    imageNote.visibility = android.view.View.VISIBLE
                } ?: run {
                    imageNote.visibility = android.view.View.GONE
                }
                
                root.setOnClickListener { onNoteClick(note) }
                buttonDelete.setOnClickListener { onNoteDelete(note) }
            }
        }
    }
}
