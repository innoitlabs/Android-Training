# Fragment Communication Patterns Complete Guide

## Overview

Fragment communication is essential when fragments need to exchange data or coordinate actions. This guide covers all major communication patterns with practical examples.

## Communication Patterns

### 1. Interface-Based Communication

#### Basic Interface Pattern
```kotlin
// Define interface
interface OnMessageSendListener {
    fun onMessageSend(message: String)
}

// In Fragment
class HomeFragment : Fragment() {
    private var listener: OnMessageSendListener? = null
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnMessageSendListener
    }
    
    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    
    private fun sendMessage() {
        listener?.onMessageSend("Hello from HomeFragment")
    }
}

// In Activity
class MainActivity : AppCompatActivity(), HomeFragment.OnMessageSendListener {
    override fun onMessageSend(message: String) {
        // Handle message
        val detailFragment = DetailFragment.newInstance(message)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
```

#### Multiple Interface Pattern
```kotlin
// Multiple interfaces for different actions
interface OnDataSendListener {
    fun onDataSend(data: String)
}

interface OnActionListener {
    fun onActionPerformed(action: String)
}

// Fragment implementing multiple interfaces
class HomeFragment : Fragment() {
    private var dataListener: OnDataSendListener? = null
    private var actionListener: OnActionListener? = null
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataListener = context as? OnDataSendListener
        actionListener = context as? OnActionListener
    }
}
```

### 2. Shared ViewModel Communication (Recommended)

#### Basic Shared ViewModel
```kotlin
class SharedViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    
    fun sendMessage(msg: String) {
        _message.value = msg
    }
}

// In Fragments
class HomeFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            sharedViewModel.sendMessage(message)
        }
    }
}

class DetailFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedViewModel.message.observe(viewLifecycleOwner) { message ->
            messageTextView.text = message
        }
    }
}
```

#### Advanced Shared ViewModel with State
```kotlin
data class UserData(
    val name: String,
    val email: String,
    val age: Int
)

class SharedViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun updateUserData(userData: UserData) {
        _isLoading.value = true
        // Simulate network call
        viewModelScope.launch {
            delay(1000)
            _userData.value = userData
            _isLoading.value = false
        }
    }
}
```

### 3. Fragment Result API (Modern Approach)

#### Basic Result API
```kotlin
// In sending fragment
class HomeFragment : Fragment() {
    private val getMessage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // Handle result
        uri?.let { sendMessageToDetail(it.toString()) }
    }
    
    private fun sendMessageToDetail(message: String) {
        val result = bundleOf("message" to message)
        setFragmentResult("message_key", result)
    }
}

// In receiving fragment
class DetailFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setFragmentResultListener("message_key") { _, bundle ->
            val message = bundle.getString("message")
            messageTextView.text = message
        }
    }
}
```

#### Advanced Result API with Custom Contract
```kotlin
class MessageContract : ActivityResultContract<String, String>() {
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, MessageActivity::class.java).apply {
            putExtra("message", input)
        }
    }
    
    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra("result")
        } else null
    }
}

// Usage
private val getMessageResult = registerForActivityResult(MessageContract()) { result ->
    result?.let { handleMessageResult(it) }
}
```

### 4. Event Bus Pattern (Legacy but Still Useful)

#### Using LiveEventBus
```kotlin
// Define events
data class MessageEvent(val message: String, val timestamp: Long = System.currentTimeMillis())

// Send event
LiveEventBus.get<MessageEvent>("message_channel").post(MessageEvent("Hello"))

// Receive event
LiveEventBus.get<MessageEvent>("message_channel")
    .observe(this) { event ->
        // Handle event
        updateUI(event.message)
    }
```

### 5. Shared Preferences Communication

#### Basic Shared Preferences
```kotlin
class SharedPrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("fragment_prefs", Context.MODE_PRIVATE)
    
    fun saveMessage(message: String) {
        prefs.edit().putString("message", message).apply()
    }
    
    fun getMessage(): String? {
        return prefs.getString("message", null)
    }
}

// Usage in fragments
class HomeFragment : Fragment() {
    private lateinit var prefsManager: SharedPrefsManager
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefsManager = SharedPrefsManager(context)
    }
    
    private fun saveMessage() {
        prefsManager.saveMessage("Hello from HomeFragment")
    }
}
```

## Bi-directional Communication

### Using Shared ViewModel
```kotlin
class BiDirectionalViewModel : ViewModel() {
    private val _messageFromHome = MutableLiveData<String>()
    val messageFromHome: LiveData<String> = _messageFromHome
    
    private val _messageFromDetail = MutableLiveData<String>()
    val messageFromDetail: LiveData<String> = _messageFromDetail
    
    fun sendFromHome(message: String) {
        _messageFromHome.value = message
    }
    
    fun sendFromDetail(message: String) {
        _messageFromDetail.value = message
    }
}
```

### Using Interface with Callback
```kotlin
interface FragmentCommunicationListener {
    fun onMessageFromHome(message: String)
    fun onMessageFromDetail(message: String)
}

class MainActivity : AppCompatActivity(), FragmentCommunicationListener {
    override fun onMessageFromHome(message: String) {
        // Handle message from home
        val detailFragment = supportFragmentManager.findFragmentById(R.id.detail_container) as? DetailFragment
        detailFragment?.updateMessage(message)
    }
    
    override fun onMessageFromDetail(message: String) {
        // Handle message from detail
        val homeFragment = supportFragmentManager.findFragmentById(R.id.home_container) as? HomeFragment
        homeFragment?.updateMessage(message)
    }
}
```

## Best Practices

### 1. Choose the Right Pattern
- **Shared ViewModel**: For complex state management and multiple observers
- **Interface**: For simple, direct communication between two fragments
- **Fragment Result API**: For one-time data passing
- **Event Bus**: For global events (use sparingly)

### 2. Lifecycle Management
```kotlin
// Always use viewLifecycleOwner for LiveData observations
sharedViewModel.message.observe(viewLifecycleOwner) { message ->
    // Update UI
}

// Clean up listeners
override fun onDestroyView() {
    super.onDestroyView()
    // Clear references to prevent memory leaks
}
```

### 3. Error Handling
```kotlin
// Safe interface casting
override fun onAttach(context: Context) {
    super.onAttach(context)
    listener = try {
        context as OnMessageSendListener
    } catch (e: ClassCastException) {
        throw ClassCastException("${context.javaClass.simpleName} must implement OnMessageSendListener")
    }
}
```

### 4. State Preservation
```kotlin
// Save state during configuration changes
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("current_message", currentMessage)
}

override fun onViewStateRestored(savedInstanceState: Bundle?) {
    super.onViewStateRestored(savedInstanceState)
    savedInstanceState?.getString("current_message")?.let { message ->
        // Restore state
    }
}
```

## Testing Communication Patterns

### Testing Shared ViewModel
```kotlin
@Test
fun testSharedViewModelCommunication() {
    val viewModel = SharedViewModel()
    val testObserver = Observer<String> { }
    
    viewModel.message.observeForever(testObserver)
    
    viewModel.sendMessage("Test message")
    
    assertEquals("Test message", viewModel.message.value)
    
    viewModel.message.removeObserver(testObserver)
}
```

### Testing Interface Communication
```kotlin
@Test
fun testInterfaceCommunication() {
    val mockListener = mock<OnMessageSendListener>()
    val fragment = HomeFragment()
    
    // Simulate onAttach
    fragment.onAttach(mockContext)
    
    // Test communication
    fragment.sendMessage("Test")
    
    verify(mockListener).onMessageSend("Test")
}
```

## Performance Considerations

### 1. Memory Management
- Always use `viewLifecycleOwner` for LiveData observations
- Clean up listeners in `onDestroyView()`
- Avoid strong references to prevent memory leaks

### 2. Efficient Updates
```kotlin
// Use distinctUntilChanged for LiveData
val message = _message.distinctUntilChanged()

// Use SingleLiveEvent for one-time events
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)
    
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }
    
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
}
```

### 3. Batch Updates
```kotlin
// Batch multiple updates in ViewModel
fun updateMultipleFields(name: String, email: String, age: Int) {
    viewModelScope.launch {
        _userData.value = _userData.value?.copy(
            name = name,
            email = email,
            age = age
        )
    }
}
```

This comprehensive guide covers all major fragment communication patterns with practical examples, best practices, and performance considerations.
