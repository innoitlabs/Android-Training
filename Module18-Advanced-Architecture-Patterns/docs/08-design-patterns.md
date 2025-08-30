# Design Patterns in Android

## Overview

Design patterns are proven solutions to common software design problems. In Android development, understanding and applying these patterns leads to more maintainable, scalable, and testable code.

## Singleton Pattern

### 1. Retrofit Instance

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://api.example.com/"
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}

// Usage
class UserRepository @Inject constructor() {
    suspend fun getUsers(): List<User> {
        return RetrofitClient.apiService.getUsers()
    }
}
```

### 2. Database Instance

```kotlin
@Singleton
class DatabaseManager @Inject constructor(
    private val context: Context
) {
    private val database by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }
    
    fun getUserDao(): UserDao = database.userDao()
    fun getPostDao(): PostDao = database.postDao()
}
```

## Factory Pattern

### 1. ViewModel Factory

```kotlin
class UserViewModelFactory @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(getUserUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

// Usage with Hilt
@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    
    @Provides
    fun provideUserViewModelFactory(
        getUserUseCase: GetUserUseCase
    ): UserViewModelFactory {
        return UserViewModelFactory(getUserUseCase)
    }
}
```

### 2. Repository Factory

```kotlin
class RepositoryFactory @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val postDao: PostDao
) {
    
    fun createUserRepository(): UserRepository {
        return UserRepositoryImpl(apiService, userDao)
    }
    
    fun createPostRepository(): PostRepository {
        return PostRepositoryImpl(apiService, postDao)
    }
    
    fun createRepository(type: RepositoryType): Repository {
        return when (type) {
            RepositoryType.USER -> createUserRepository()
            RepositoryType.POST -> createPostRepository()
        }
    }
}

enum class RepositoryType {
    USER, POST
}
```

## Observer Pattern

### 1. LiveData Implementation

```kotlin
class UserViewModel : ViewModel() {
    
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    fun loadUser(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = userRepository.getUser(id)
                _user.value = user
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Usage in Activity/Fragment
class UserActivity : AppCompatActivity() {
    
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        viewModel.user.observe(this) { user ->
            // Update UI with user data
            updateUserUI(user)
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            // Show/hide loading indicator
            showLoading(isLoading)
        }
        
        viewModel.error.observe(this) { error ->
            // Show error message
            showError(error)
        }
    }
}
```

### 2. Flow Implementation

```kotlin
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()
    
    fun loadUser(id: Int) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            getUserUseCase(id)
                .onSuccess { user ->
                    _uiState.value = UserUiState.Success(user)
                }
                .onFailure { error ->
                    _uiState.value = UserUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

// Usage in Compose
@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (uiState) {
        is UserUiState.Loading -> LoadingIndicator()
        is UserUiState.Success -> UserContent(user = uiState.user)
        is UserUiState.Error -> ErrorContent(message = uiState.message)
    }
}
```

## Adapter Pattern

### 1. RecyclerView Adapter

```kotlin
class UserAdapter(
    private val onUserClick: (User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding, onUserClick)
    }
    
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class UserViewHolder(
        private val binding: ItemUserBinding,
        private val onUserClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(user: User) {
            binding.apply {
                userName.text = user.name
                userEmail.text = user.email
                root.setOnClickListener { onUserClick(user) }
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}

// Usage
class UserListFragment : Fragment() {
    
    private val userAdapter = UserAdapter { user ->
        // Handle user click
        navigateToUserDetail(user.id)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView.adapter = userAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
    
    private fun updateUsers(users: List<User>) {
        userAdapter.submitList(users)
    }
}
```

### 2. Compose Adapter Pattern

```kotlin
@Composable
fun UserList(
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    LazyColumn {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserCard(
                user = user,
                onClick = { onUserClick(user) }
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

## Strategy Pattern

### 1. Payment Strategy

```kotlin
interface PaymentStrategy {
    fun processPayment(amount: Double): PaymentResult
}

class CreditCardPayment : PaymentStrategy {
    override fun processPayment(amount: Double): PaymentResult {
        // Process credit card payment
        return PaymentResult.Success("Credit card payment processed")
    }
}

class PayPalPayment : PaymentStrategy {
    override fun processPayment(amount: Double): PaymentResult {
        // Process PayPal payment
        return PaymentResult.Success("PayPal payment processed")
    }
}

class CryptoPayment : PaymentStrategy {
    override fun processPayment(amount: Double): PaymentResult {
        // Process cryptocurrency payment
        return PaymentResult.Success("Crypto payment processed")
    }
}

class PaymentProcessor @Inject constructor() {
    
    fun processPayment(amount: Double, strategy: PaymentStrategy): PaymentResult {
        return strategy.processPayment(amount)
    }
}

sealed class PaymentResult {
    data class Success(val message: String) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
}

// Usage
class CheckoutViewModel @Inject constructor(
    private val paymentProcessor: PaymentProcessor
) : ViewModel() {
    
    fun processPayment(amount: Double, paymentType: PaymentType) {
        val strategy = when (paymentType) {
            PaymentType.CREDIT_CARD -> CreditCardPayment()
            PaymentType.PAYPAL -> PayPalPayment()
            PaymentType.CRYPTO -> CryptoPayment()
        }
        
        val result = paymentProcessor.processPayment(amount, strategy)
        // Handle result
    }
}

enum class PaymentType {
    CREDIT_CARD, PAYPAL, CRYPTO
}
```

### 2. Caching Strategy

```kotlin
interface CachingStrategy {
    fun get(key: String): Any?
    fun put(key: String, value: Any)
    fun clear()
}

class MemoryCache : CachingStrategy {
    private val cache = mutableMapOf<String, Any>()
    
    override fun get(key: String): Any? = cache[key]
    
    override fun put(key: String, value: Any) {
        cache[key] = value
    }
    
    override fun clear() {
        cache.clear()
    }
}

class DiskCache @Inject constructor(
    private val context: Context
) : CachingStrategy {
    
    private val sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
    
    override fun get(key: String): Any? {
        return sharedPreferences.getString(key, null)
    }
    
    override fun put(key: String, value: Any) {
        sharedPreferences.edit().putString(key, value.toString()).apply()
    }
    
    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

class CacheManager @Inject constructor(
    private val memoryCache: MemoryCache,
    private val diskCache: DiskCache
) {
    
    fun get(key: String): Any? {
        // Try memory cache first
        memoryCache.get(key)?.let { return it }
        
        // Try disk cache
        diskCache.get(key)?.let { value ->
            memoryCache.put(key, value)
            return value
        }
        
        return null
    }
    
    fun put(key: String, value: Any) {
        memoryCache.put(key, value)
        diskCache.put(key, value)
    }
}
```

## Command Pattern

### 1. Undo/Redo System

```kotlin
interface Command {
    fun execute()
    fun undo()
}

class AddUserCommand(
    private val userRepository: UserRepository,
    private val user: User
) : Command {
    
    override fun execute() {
        userRepository.addUser(user)
    }
    
    override fun undo() {
        userRepository.deleteUser(user.id)
    }
}

class UpdateUserCommand(
    private val userRepository: UserRepository,
    private val oldUser: User,
    private val newUser: User
) : Command {
    
    override fun execute() {
        userRepository.updateUser(newUser)
    }
    
    override fun undo() {
        userRepository.updateUser(oldUser)
    }
}

class CommandManager {
    private val undoStack = mutableListOf<Command>()
    private val redoStack = mutableListOf<Command>()
    
    fun executeCommand(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear() // Clear redo stack when new command is executed
    }
    
    fun undo() {
        if (undoStack.isNotEmpty()) {
            val command = undoStack.removeLast()
            command.undo()
            redoStack.add(command)
        }
    }
    
    fun redo() {
        if (redoStack.isNotEmpty()) {
            val command = redoStack.removeLast()
            command.execute()
            undoStack.add(command)
        }
    }
    
    fun canUndo(): Boolean = undoStack.isNotEmpty()
    fun canRedo(): Boolean = redoStack.isNotEmpty()
}

// Usage
class UserEditorViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val commandManager = CommandManager()
    
    fun addUser(user: User) {
        val command = AddUserCommand(userRepository, user)
        commandManager.executeCommand(command)
    }
    
    fun updateUser(oldUser: User, newUser: User) {
        val command = UpdateUserCommand(userRepository, oldUser, newUser)
        commandManager.executeCommand(command)
    }
    
    fun undo() {
        commandManager.undo()
    }
    
    fun redo() {
        commandManager.redo()
    }
    
    fun canUndo(): Boolean = commandManager.canUndo()
    fun canRedo(): Boolean = commandManager.canRedo()
}
```

## Template Method Pattern

### 1. Base Repository

```kotlin
abstract class BaseRepository<T> {
    
    protected abstract suspend fun fetchFromRemote(): List<T>
    protected abstract suspend fun fetchFromLocal(): List<T>
    protected abstract suspend fun saveToLocal(data: List<T>)
    protected abstract fun shouldFetchFromRemote(): Boolean
    
    suspend fun getData(): List<T> {
        return if (shouldFetchFromRemote()) {
            try {
                val remoteData = fetchFromRemote()
                saveToLocal(remoteData)
                remoteData
            } catch (e: Exception) {
                fetchFromLocal()
            }
        } else {
            fetchFromLocal()
        }
    }
}

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : BaseRepository<User>() {
    
    override suspend fun fetchFromRemote(): List<User> {
        return apiService.getUsers()
    }
    
    override suspend fun fetchFromLocal(): List<User> {
        return userDao.getAllUsers()
    }
    
    override suspend fun saveToLocal(data: List<User>) {
        userDao.insertAllUsers(data)
    }
    
    override fun shouldFetchFromRemote(): Boolean {
        // Check if cache is expired or empty
        return true
    }
}
```

## Best Practices

1. **Choose Appropriate Patterns**: Use patterns that solve your specific problem
2. **Keep It Simple**: Don't over-engineer with unnecessary patterns
3. **Testability**: Ensure patterns don't make testing difficult
4. **Documentation**: Document why specific patterns are used
5. **Consistency**: Use patterns consistently throughout the codebase
6. **Performance**: Consider performance implications of patterns
7. **Maintainability**: Choose patterns that improve code maintainability

## Summary

Design patterns provide:

- ✅ **Proven solutions** to common problems
- ✅ **Better code organization**
- ✅ **Improved maintainability**
- ✅ **Enhanced testability**
- ✅ **Consistent architecture**
- ✅ **Scalable codebase**

In the next section, we'll explore Code Organization & Package Structure for better project organization.
