package com.example.performancesecurity.NotesApp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    private val storageManager = SecureStorageManager(context)
    private val networkClient = NetworkClient(context)
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val notes = storageManager.getNotes()
            val lastSyncTime = storageManager.getLastSyncTime()
            
            val result = networkClient.syncNotes(notes, lastSyncTime)
            
            if (result.isSuccess) {
                val syncResponse = result.getOrNull()!!
                storageManager.saveNotes(syncResponse.notes)
                storageManager.setLastSyncTime(syncResponse.serverTime)
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
    
    companion object {
        fun scheduleSync(context: Context) {
            val constraints = androidx.work.Constraints.Builder()
                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
            
            val syncRequest = androidx.work.PeriodicWorkRequestBuilder<SyncWorker>(
                15, java.util.concurrent.TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()
            
            androidx.work.WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "notes_sync",
                    androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                    syncRequest
                )
        }
    }
}
