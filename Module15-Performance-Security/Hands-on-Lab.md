# Hands-on Lab: Secure & Optimized Notes App

## Overview
In this hands-on lab, you'll build a complete Notes app that demonstrates all the performance optimization and security practices covered in this module. The app will include secure storage, image loading, network optimization, memory leak detection, and performance benchmarking.

## Project Requirements

### Features to Implement
1. **Secure Notes Storage** - EncryptedSharedPreferences for note data
2. **Image Loading** - Coil for efficient image loading and caching
3. **Network Sync** - OkHttp with compression for note synchronization
4. **Memory Leak Detection** - LeakCanary integration
5. **Performance Benchmarking** - Startup time measurement
6. **Battery Optimization** - WorkManager for background tasks

### Technical Requirements
- Kotlin 1.9+
- AndroidX Security
- Coil for image loading
- OkHttp for networking
- LeakCanary for memory leak detection
- Jetpack Benchmark for performance testing
- WorkManager for background tasks

## Step-by-Step Implementation

### Step 1: Project Setup

#### 1.1 Update Dependencies
First, update the `app/build.gradle.kts` file with all necessary dependencies:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.performancesecurity"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.performancesecurity"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
    
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.biometric:biometric:1.1.0")
    
    // Image loading
    implementation("io.coil-kt:coil:2.2.2")
    
    // Networking
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Background tasks
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    
    // Memory leak detection
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
    
    // Performance testing
    androidTestImplementation("androidx.benchmark:benchmark-macro-junit4:1.2.0")
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

#### 1.2 Update Version Catalog
Update `gradle/libs.versions.toml`:

```toml
[versions]
agp = "8.12.2"
kotlin = "2.0.21"
coreKtx = "1.10.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
appcompat = "1.6.1"
material = "1.10.0"
activity = "1.8.0"
constraintlayout = "2.1.4"
security = "1.1.0-alpha06"
biometric = "1.1.0"
coil = "2.2.2"
okhttp = "4.11.0"
retrofit = "2.9.0"
work = "2.8.1"
leakcanary = "2.10"
benchmark = "1.2.0"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-activity = { group = "androidx.activity", name = "activity", version.ref = "activity" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
androidx-security-crypto = { group = "androidx.security", name = "security-crypto", version.ref = "security" }
androidx-biometric = { group = "androidx.biometric", name = "biometric", version.ref = "biometric" }
coil = { group = "io.coil-kt", name = "coil", version.ref = "coil" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "work" }
leakcanary-android = { group = "com.squareup.leakcanary", name = "leakcanary-android", version.ref = "leakcanary" }
benchmark-macro-junit4 = { group = "androidx.benchmark", name = "benchmark-macro-junit4", version.ref = "benchmark" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
```

### Step 2: Data Models

#### 2.1 Create Note Data Class
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/Note.kt
package com.example.performancesecurity.NotesApp

import java.util.Date

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
    val isEncrypted: Boolean = false
)
```

#### 2.2 Create API Response Models
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/ApiModels.kt
package com.example.performancesecurity.NotesApp

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)

data class SyncRequest(
    val notes: List<Note>,
    val lastSyncTime: Long
)

data class SyncResponse(
    val notes: List<Note>,
    val serverTime: Long
)
```

### Step 3: Secure Storage Implementation

#### 3.1 Secure Storage Manager
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/SecureStorageManager.kt
package com.example.performancesecurity.NotesApp

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
}
```

### Step 4: Network Layer

#### 4.1 API Service
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/ApiService.kt
package com.example.performancesecurity.NotesApp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("sync")
    suspend fun syncNotes(@Body request: SyncRequest): Response<ApiResponse<SyncResponse>>
}
```

#### 4.2 Network Client
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/NetworkClient.kt
package com.example.performancesecurity.NotesApp

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkClient(private val context: Context) {
    private val okHttpClient: OkHttpClient
    private val apiService: ApiService
    
    init {
        okHttpClient = createOkHttpClient()
        apiService = createApiService()
    }
    
    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("User-Agent", "NotesApp/1.0")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    private fun createApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Replace with your API URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    suspend fun syncNotes(notes: List<Note>, lastSyncTime: Long): Result<SyncResponse> {
        return try {
            val request = SyncRequest(notes, lastSyncTime)
            val response = apiService.syncNotes(request)
            
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Sync failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Step 5: Background Sync with WorkManager

#### 5.1 Sync Worker
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/SyncWorker.kt
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
```

### Step 6: UI Implementation

#### 6.1 Main Activity
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/MainActivity.kt
package com.example.performancesecurity.NotesApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.performancesecurity.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storageManager: SecureStorageManager
    private lateinit var notesAdapter: NotesAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
```

#### 6.2 Notes Adapter
```kotlin
// app/src/main/java/com/example/performancesecurity/NotesApp/NotesAdapter.kt
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
```

### Step 7: Performance Testing

#### 7.1 Startup Benchmark
```kotlin
// app/src/androidTest/java/com/example/performancesecurity/NotesApp/StartupBenchmarkTest.kt
package com.example.performancesecurity.NotesApp

import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarkTest {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()
    
    @Test fun startup() = benchmarkRule.measureRepeated {
        startActivityAndWait()
    }
    
    @Test fun startupWithNotes() = benchmarkRule.measureRepeated {
        startActivityAndWait()
        // Add some notes to test performance with data
        // This would require additional setup
    }
}
```

### Step 8: Layout Files

#### 8.1 Main Activity Layout
```xml
<!-- app/src/main/res/layout/activity_main.xml -->
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="16dp"
        android:clipToPadding="false" />

    <LinearLayout
        android:id="@+id/emptyState"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:orientation="vertical"
        android:visibility="gone">

        <ImageView
            android:layout_width="120dp"
            android:layout_height="120dp"
            android:src="@android:drawable/ic_menu_edit"
            android:alpha="0.5" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="No notes yet"
            android:textSize="18sp"
            android:textColor="@android:color/darker_gray" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Tap the + button to create your first note"
            android:textSize="14sp"
            android:textColor="@android:color/darker_gray" />

    </LinearLayout>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabAddNote"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_margin="16dp"
        android:src="@android:drawable/ic_input_add"
        app:fabSize="normal" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

#### 8.2 Note Item Layout
```xml
<!-- app/src/main/res/layout/item_note.xml -->
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginBottom="8dp"
    app:cardElevation="2dp"
    app:cardCornerRadius="8dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp">

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/textTitle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="16sp"
                android:textStyle="bold"
                android:maxLines="1"
                android:ellipsize="end" />

            <TextView
                android:id="@+id/textContent"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="4dp"
                android:textSize="14sp"
                android:maxLines="2"
                android:ellipsize="end" />

            <TextView
                android:id="@+id/textDate"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="8dp"
                android:textSize="12sp"
                android:textColor="@android:color/darker_gray" />

        </LinearLayout>

        <ImageView
            android:id="@+id/imageNote"
            android:layout_width="60dp"
            android:layout_height="60dp"
            android:layout_marginStart="12dp"
            android:scaleType="centerCrop"
            android:visibility="gone" />

        <ImageButton
            android:id="@+id/buttonDelete"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:background="?attr/selectableItemBackgroundBorderless"
            android:src="@android:drawable/ic_menu_delete"
            android:contentDescription="Delete note" />

    </LinearLayout>

</com.google.android.material.card.MaterialCardView>
```

## Testing the Application

### 1. Build and Run
1. Open the project in Android Studio
2. Sync Gradle files
3. Build the project (Build → Make Project)
4. Run on an emulator or device

### 2. Test Features
1. **Create Notes**: Tap the + button to create new notes
2. **Secure Storage**: Notes are stored encrypted
3. **Image Loading**: Add images to notes (if implemented)
4. **Memory Leak Detection**: LeakCanary will detect any memory leaks
5. **Performance Testing**: Run the benchmark tests

### 3. Performance Monitoring
1. Use Android Profiler to monitor:
   - Memory usage
   - CPU usage
   - Network activity
2. Check for memory leaks with LeakCanary
3. Run benchmark tests to measure startup time

## Expected Outcomes

### Performance Metrics
- **Startup Time**: < 2 seconds on mid-range devices
- **Memory Usage**: < 100MB for typical usage
- **Network Efficiency**: Compressed requests and responses
- **Battery Impact**: Minimal background sync impact

### Security Features
- **Encrypted Storage**: All notes stored securely
- **Network Security**: HTTPS with compression
- **Input Validation**: Sanitized user inputs
- **Memory Safety**: No memory leaks detected

## Troubleshooting

### Common Issues
1. **Build Errors**: Ensure all dependencies are properly synced
2. **Runtime Errors**: Check that all permissions are granted
3. **Memory Leaks**: Use LeakCanary to identify and fix leaks
4. **Performance Issues**: Use Android Profiler to identify bottlenecks

### Debug Tips
1. Enable debug logging for network requests
2. Monitor memory usage in Android Profiler
3. Check LeakCanary reports for memory leaks
4. Run benchmark tests regularly

## Summary
This hands-on lab demonstrates a complete implementation of:
- **Secure data storage** with EncryptedSharedPreferences
- **Efficient image loading** with Coil
- **Optimized networking** with OkHttp
- **Memory leak detection** with LeakCanary
- **Performance benchmarking** with Jetpack Benchmark
- **Background task management** with WorkManager

The app serves as a practical example of applying all the performance optimization and security practices covered in this module.

## Next Steps
- Extend the app with additional features
- Implement more advanced security measures
- Add comprehensive unit and integration tests
- Optimize for different device configurations
