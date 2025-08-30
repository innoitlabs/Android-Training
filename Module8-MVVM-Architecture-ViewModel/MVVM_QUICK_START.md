# MVVM Quick Start Guide

## Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.9+
- Basic understanding of Android development

## Step 1: Add Dependencies

Add these dependencies to your `app/build.gradle.kts`:

```kotlin
dependencies {
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // Data Binding
    implementation("androidx.databinding:databinding-runtime:8.2.2")
    
    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    
    // Retrofit for Network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Room for Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
```

## Step 2: Enable Data Binding

In your `app/build.gradle.kts`:

```kotlin
android {
    buildFeatures {
        dataBinding = true
    }
}
```

## Step 3: Create Data Model

```kotlin
// User.kt
data class User(
    val id: Int,
    val name: String,
    val email: String
)
```

## Step 4: Create Repository

```kotlin
// UserRepository.kt
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getUsers(): Result<List<User>> {
        return try {
            val users = apiService.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## Step 5: Create ViewModel

```kotlin
// UserViewModel.kt
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<UiState<List<User>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<User>>> = _uiState
    
    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getUsers()
                .onSuccess { users ->
                    _uiState.value = UiState.Success(users)
                }
                .onFailure { exception ->
                    _uiState.value = UiState.Error(exception.message ?: "Unknown error")
                }
        }
    }
}
```

## Step 6: Create UI State

```kotlin
// UiState.kt
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

## Step 7: Update Layout with Data Binding

```xml
<!-- activity_main.xml -->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    
    <data>
        <variable
            name="viewModel"
            type="com.example.mvvmapp.UserViewModel" />
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="16dp">

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{() -> viewModel.loadUsers()}"
            android:text="Load Users" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            app:visibleIf="@{viewModel.uiState instanceof com.example.mvvmapp.UiState.Success}" />

        <ProgressBar
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            app:visibleIf="@{viewModel.uiState instanceof com.example.mvvmapp.UiState.Loading}" />

        <TextView
            android:id="@+id/errorText"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:textColor="@android:color/holo_red_dark"
            app:visibleIf="@{viewModel.uiState instanceof com.example.mvvmapp.UiState.Error}" />

    </LinearLayout>
</layout>
```

## Step 8: Update Activity

```kotlin
// MainActivity.kt
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        
        setupObservers()
    }
    
    private fun setupObservers() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    // Loading state handled by data binding
                }
                is UiState.Success -> {
                    // Update RecyclerView adapter
                    // adapter.submitList(state.data)
                }
                is UiState.Error -> {
                    binding.errorText.text = state.message
                }
            }
        }
    }
}
```

## Step 9: Add Hilt Setup

```kotlin
// Application.kt
@HiltAndroidApp
class MyApplication : Application()

// AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepository(apiService)
    }
}
```

## Step 10: Create API Service

```kotlin
// ApiService.kt
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}
```

## Testing Your Implementation

1. Build the project
2. Run on emulator/device
3. Tap "Load Users" button
4. Verify loading state appears
5. Verify users are displayed or error is shown

## Common Issues & Solutions

### Issue: Data Binding not working
**Solution**: Make sure data binding is enabled in build.gradle.kts

### Issue: Hilt not working
**Solution**: 
- Add @HiltAndroidApp to Application class
- Add @AndroidEntryPoint to Activity
- Sync project after adding dependencies

### Issue: ViewModel not surviving rotation
**Solution**: Use by viewModels() delegate instead of ViewModelProvider

### Issue: LiveData not updating UI
**Solution**: Make sure you're observing on the main thread and lifecycle owner is set

## Next Steps

1. Add Room database for offline caching
2. Implement search functionality
3. Add pagination
4. Write unit tests
5. Add error handling and retry mechanisms

---

**You're now ready to build MVVM apps! 🎉**
