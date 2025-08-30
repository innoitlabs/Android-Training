package com.example.performancesecurity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.performancesecurity.databinding.ActivitySecureStorageBinding

class SecureStorageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecureStorageBinding
    private lateinit var securePrefs: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecureStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initializeSecureStorage()
        setupUI()
    }
    
    private fun initializeSecureStorage() {
        val masterKey = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        securePrefs = EncryptedSharedPreferences.create(
            this,
            "secure_demo_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    private fun setupUI() {
        binding.btnSave.setOnClickListener {
            saveSecureData()
        }
        
        binding.btnLoad.setOnClickListener {
            loadSecureData()
        }
        
        binding.btnClear.setOnClickListener {
            clearSecureData()
        }
        
        binding.btnShowAll.setOnClickListener {
            showAllData()
        }
    }
    
    private fun saveSecureData() {
        val key = binding.editTextKey.text.toString()
        val value = binding.editTextValue.text.toString()
        
        if (key.isNotEmpty() && value.isNotEmpty()) {
            securePrefs.edit().putString(key, value).apply()
            binding.textStatus.text = "Data saved securely!"
            Toast.makeText(this, "Data encrypted and saved", Toast.LENGTH_SHORT).show()
            
            // Clear input fields
            binding.editTextKey.text?.clear()
            binding.editTextValue.text?.clear()
        } else {
            Toast.makeText(this, "Please enter both key and value", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadSecureData() {
        val key = binding.editTextKey.text.toString()
        
        if (key.isNotEmpty()) {
            val value = securePrefs.getString(key, null)
            if (value != null) {
                binding.textResult.text = "Retrieved: $value"
                binding.textStatus.text = "Data loaded successfully!"
            } else {
                binding.textResult.text = "No data found for key: $key"
                binding.textStatus.text = "Key not found"
            }
        } else {
            Toast.makeText(this, "Please enter a key to retrieve", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun clearSecureData() {
        val key = binding.editTextKey.text.toString()
        
        if (key.isNotEmpty()) {
            securePrefs.edit().remove(key).apply()
            binding.textResult.text = "Data cleared for key: $key"
            binding.textStatus.text = "Data cleared successfully!"
        } else {
            // Clear all data
            securePrefs.edit().clear().apply()
            binding.textResult.text = "All data cleared"
            binding.textStatus.text = "All data cleared successfully!"
        }
        
        binding.editTextKey.text?.clear()
        binding.editTextValue.text?.clear()
    }
    
    private fun showAllData() {
        val allData = securePrefs.all
        if (allData.isEmpty()) {
            binding.textResult.text = "No data stored"
        } else {
            val dataString = allData.entries.joinToString("\n") { "${it.key}: ${it.value}" }
            binding.textResult.text = "All stored data:\n$dataString"
        }
        binding.textStatus.text = "Displayed all data"
    }
}
