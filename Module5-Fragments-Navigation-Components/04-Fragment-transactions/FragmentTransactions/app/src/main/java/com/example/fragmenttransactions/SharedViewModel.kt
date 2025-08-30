package com.example.fragmenttransactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    
    // LiveData for messages from HomeFragment to DetailFragment
    private val _messageFromHome = MutableLiveData<String>()
    val messageFromHome: LiveData<String> = _messageFromHome
    
    // LiveData for messages from DetailFragment to HomeFragment
    private val _messageFromDetail = MutableLiveData<String>()
    val messageFromDetail: LiveData<String> = _messageFromDetail
    
    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // LiveData for back stack count
    private val _backStackCount = MutableLiveData<Int>()
    val backStackCount: LiveData<Int> = _backStackCount
    
    /**
     * Send message from HomeFragment to DetailFragment
     * Simulates a network call with loading state
     */
    fun sendFromHome(message: String) {
        _isLoading.value = true
        
        viewModelScope.launch {
            // Simulate network delay
            delay(500)
            _messageFromHome.value = message
            _isLoading.value = false
        }
    }
    
    /**
     * Send message from DetailFragment to HomeFragment
     */
    fun sendFromDetail(message: String) {
        _messageFromDetail.value = message
    }
    
    /**
     * Update back stack count
     */
    fun updateBackStackCount(count: Int) {
        _backStackCount.value = count
    }
    
    /**
     * Clear all messages
     */
    fun clearMessages() {
        _messageFromHome.value = ""
        _messageFromDetail.value = ""
    }
    
    /**
     * Reset loading state
     */
    fun resetLoading() {
        _isLoading.value = false
    }
}
