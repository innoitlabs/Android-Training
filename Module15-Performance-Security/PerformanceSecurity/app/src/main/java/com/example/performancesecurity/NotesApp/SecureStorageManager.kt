package com.example.performancesecurity.NotesApp

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class SecureStorageManager(private val context: Context) {
    private lateinit var encryptedPrefs: SharedPreferences
    private val gson = Gson()
    
    init {
        initializeSecureStorage()
    }
    
    private fun initializeSecureStorage() {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        encryptedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_notes_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    fun saveNotes(notes: List<Note>) {
        val notesJson = gson.toJson(notes)
        encryptedPrefs.edit().putString("notes", notesJson).apply()
    }
    
    fun getNotes(): List<Note> {
        val notesJson = encryptedPrefs.getString("notes", "[]")
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(notesJson, type) ?: emptyList()
    }
    
    fun saveNote(note: Note) {
        val notes = getNotes().toMutableList()
        val existingIndex = notes.indexOfFirst { it.id == note.id }
        
        if (existingIndex != -1) {
            notes[existingIndex] = note
        } else {
            notes.add(note)
        }
        
        saveNotes(notes)
    }
    
    fun deleteNote(noteId: String) {
        val notes = getNotes().toMutableList()
        notes.removeAll { it.id == noteId }
        saveNotes(notes)
    }
    
    fun getLastSyncTime(): Long {
        return encryptedPrefs.getLong("last_sync_time", 0L)
    }
    
    fun setLastSyncTime(time: Long) {
        encryptedPrefs.edit().putLong("last_sync_time", time).apply()
    }
    
    fun clearAllData() {
        encryptedPrefs.edit().clear().apply()
    }
}
