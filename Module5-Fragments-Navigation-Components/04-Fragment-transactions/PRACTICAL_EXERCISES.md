# Practical Exercises: Fragment Transactions & Communication

## Exercise Overview

This guide provides hands-on exercises to master fragment transactions and communication patterns. Each exercise builds upon the previous one, gradually increasing in complexity.

## Prerequisites

- Android Studio installed
- Basic knowledge of Kotlin
- Understanding of Android Activities and basic UI components

## Exercise 1: Basic Fragment Transactions

### Objective
Create a simple app with two fragments and basic navigation.

### Requirements
- MainActivity with FrameLayout container
- HomeFragment with a button
- DetailFragment with a "Go Back" button
- Basic navigation between fragments

### Step-by-Step Implementation

#### 1. Update MainActivity
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Load HomeFragment as initial fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}
```

#### 2. Create HomeFragment
```kotlin
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.findViewById<Button>(R.id.goToDetailButton).setOnClickListener {
            navigateToDetail()
        }
    }
    
    private fun navigateToDetail() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailFragment())
            .addToBackStack("home_to_detail")
            .commit()
    }
}
```

#### 3. Create DetailFragment
```kotlin
class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.findViewById<Button>(R.id.goBackButton).setOnClickListener {
            goBack()
        }
    }
    
    private fun goBack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
    }
}
```

### Testing
1. Run the app
2. Verify HomeFragment loads first
3. Click "Go to Detail" button
4. Verify DetailFragment appears
5. Click "Go Back" button
6. Verify return to HomeFragment
7. Test hardware back button

## Exercise 2: Interface-Based Communication

### Objective
Implement communication between fragments using interfaces.

### Requirements
- HomeFragment with EditText and Button
- DetailFragment displays the message
- Interface-based communication
- Data passing from Home to Detail

### Implementation

#### 1. Define Interface
```kotlin
interface OnMessageSendListener {
    fun onMessageSend(message: String)
}
```

#### 2. Update MainActivity
```kotlin
class MainActivity : AppCompatActivity(), OnMessageSendListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
    
    override fun onMessageSend(message: String) {
        val detailFragment = DetailFragment.newInstance(message)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack("home_to_detail")
            .commit()
    }
}
```

#### 3. Update HomeFragment
```kotlin
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
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)
        val sendButton = view.findViewById<Button>(R.id.sendButton)
        
        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotEmpty()) {
                listener?.onMessageSend(message)
            }
        }
    }
}
```

#### 4. Update DetailFragment
```kotlin
class DetailFragment : Fragment() {
    companion object {
        private const val ARG_MESSAGE = "message"
        
        fun newInstance(message: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val message = arguments?.getString(ARG_MESSAGE) ?: ""
        view.findViewById<TextView>(R.id.messageTextView).text = message
        
        view.findViewById<Button>(R.id.goBackButton).setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            }
        }
    }
}
```

### Testing
1. Enter text in EditText
2. Click "Send" button
3. Verify message appears in DetailFragment
4. Test back navigation
5. Verify data persistence during rotation

## Exercise 3: Shared ViewModel Communication

### Objective
Implement bi-directional communication using SharedViewModel.

### Requirements
- SharedViewModel for state management
- Bi-directional communication
- Real-time updates
- State preservation during configuration changes

### Implementation

#### 1. Create SharedViewModel
```kotlin
class SharedViewModel : ViewModel() {
    private val _messageFromHome = MutableLiveData<String>()
    val messageFromHome: LiveData<String> = _messageFromHome
    
    private val _messageFromDetail = MutableLiveData<String>()
    val messageFromDetail: LiveData<String> = _messageFromDetail
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun sendFromHome(message: String) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(500) // Simulate processing
            _messageFromHome.value = message
            _isLoading.value = false
        }
    }
    
    fun sendFromDetail(message: String) {
        _messageFromDetail.value = message
    }
}
```

#### 2. Update HomeFragment
```kotlin
class HomeFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val messageEditText = view.findViewById<EditText>(R.id.messageEditText)
        val sendButton = view.findViewById<Button>(R.id.sendButton)
        val responseTextView = view.findViewById<TextView>(R.id.responseTextView)
        
        // Send message
        sendButton.setOnClickListener {
            val message = messageEditText.text.toString()
            if (message.isNotEmpty()) {
                sharedViewModel.sendFromHome(message)
                navigateToDetail()
            }
        }
        
        // Observe response from DetailFragment
        sharedViewModel.messageFromDetail.observe(viewLifecycleOwner) { message ->
            responseTextView.text = "Response: $message"
        }
        
        // Observe loading state
        sharedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            sendButton.isEnabled = !isLoading
            if (isLoading) {
                sendButton.text = "Sending..."
            } else {
                sendButton.text = "Send to Detail"
            }
        }
    }
    
    private fun navigateToDetail() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailFragment())
            .addToBackStack("home_to_detail")
            .commit()
    }
}
```

#### 3. Update DetailFragment
```kotlin
class DetailFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
        val responseEditText = view.findViewById<EditText>(R.id.responseEditText)
        val sendResponseButton = view.findViewById<Button>(R.id.sendResponseButton)
        val goBackButton = view.findViewById<Button>(R.id.goBackButton)
        
        // Observe message from HomeFragment
        sharedViewModel.messageFromHome.observe(viewLifecycleOwner) { message ->
            messageTextView.text = "Message from Home: $message"
        }
        
        // Send response back to HomeFragment
        sendResponseButton.setOnClickListener {
            val response = responseEditText.text.toString()
            if (response.isNotEmpty()) {
                sharedViewModel.sendFromDetail(response)
                goBack()
            }
        }
        
        goBackButton.setOnClickListener {
            goBack()
        }
    }
    
    private fun goBack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
    }
}
```

### Testing
1. Send message from HomeFragment
2. Verify loading state
3. Check message appears in DetailFragment
4. Send response back
5. Verify response appears in HomeFragment
6. Test configuration changes (rotation)
7. Verify state preservation

## Exercise 4: Advanced Fragment Management

### Objective
Implement advanced fragment patterns with custom transitions and state management.

### Requirements
- Custom fragment transitions
- Fragment state preservation
- Multiple fragment containers
- Advanced back stack management

### Implementation

#### 1. Create Custom Transitions
```xml
<!-- res/anim/slide_in_right.xml -->
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="100%"
        android:toXDelta="0%" />
</set>
```

```xml
<!-- res/anim/slide_out_left.xml -->
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="300"
        android:fromXDelta="0%"
        android:toXDelta="-100%" />
</set>
```

#### 2. Advanced Fragment Transaction
```kotlin
class FragmentNavigator(private val fragmentManager: FragmentManager) {
    
    fun navigateWithAnimation(
        containerId: Int,
        fragment: Fragment,
        tag: String? = null,
        addToBackStack: Boolean = true
    ) {
        val transaction = fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(containerId, fragment, tag)
        
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }
        
        transaction.commit()
    }
    
    fun popBackStackToFragment(tag: String, inclusive: Boolean = false) {
        fragmentManager.popBackStack(tag, if (inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0)
    }
    
    fun clearBackStack() {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
```

#### 3. State Management
```kotlin
class FragmentStateManager {
    private val fragmentStates = mutableMapOf<String, Bundle>()
    
    fun saveState(tag: String, state: Bundle) {
        fragmentStates[tag] = state
    }
    
    fun getState(tag: String): Bundle? {
        return fragmentStates[tag]
    }
    
    fun clearState(tag: String) {
        fragmentStates.remove(tag)
    }
    
    fun clearAllStates() {
        fragmentStates.clear()
    }
}
```

### Testing
1. Test custom animations
2. Verify state preservation
3. Test back stack management
4. Check memory usage
5. Test configuration changes

## Exercise 5: Real-World Application

### Objective
Build a complete messaging app using all learned concepts.

### Requirements
- Multiple fragments (ChatList, ChatDetail, Profile)
- Shared ViewModel for chat data
- Interface communication for user actions
- Fragment Result API for file selection
- Custom transitions and animations
- State preservation and error handling

### Implementation Steps

1. **Create ChatListFragment**
   - Display list of conversations
   - Interface for chat selection
   - Shared ViewModel for chat data

2. **Create ChatDetailFragment**
   - Display messages
   - Send new messages
   - File attachment using Fragment Result API

3. **Create ProfileFragment**
   - User profile information
   - Settings management
   - Bi-directional communication

4. **Implement SharedViewModel**
   - Chat data management
   - User state management
   - Message handling

5. **Add Custom Transitions**
   - Smooth navigation animations
   - State preservation
   - Error handling

### Testing Checklist
- [ ] Navigation between all fragments
- [ ] Data persistence during configuration changes
- [ ] Back stack management
- [ ] Communication between fragments
- [ ] Error handling
- [ ] Performance optimization
- [ ] Memory leak prevention

## Bonus Exercises

### Exercise 6: Testing
- Write unit tests for ViewModels
- Write integration tests for fragment communication
- Test configuration changes
- Test memory leaks

### Exercise 7: Performance Optimization
- Implement lazy loading
- Optimize fragment transactions
- Add caching mechanisms
- Monitor memory usage

### Exercise 8: Advanced Patterns
- Implement MVVM architecture
- Add dependency injection
- Use Navigation Component
- Implement deep linking

## Assessment Criteria

### Beginner Level (Exercise 1-2)
- Basic fragment transactions work
- Interface communication functional
- No crashes during navigation

### Intermediate Level (Exercise 3-4)
- Shared ViewModel implementation
- State preservation working
- Custom animations functional
- Error handling implemented

### Advanced Level (Exercise 5+)
- Complete app functionality
- Performance optimized
- Comprehensive testing
- Best practices followed

## Troubleshooting Guide

### Common Issues
1. **Fragment not showing**: Check container ID and layout
2. **Back navigation not working**: Verify addToBackStack() calls
3. **Communication not working**: Check interface implementation
4. **Memory leaks**: Use viewLifecycleOwner for LiveData
5. **State loss**: Handle configuration changes properly

### Debug Tips
- Use FragmentManager logging
- Monitor back stack changes
- Check ViewModel state
- Verify lifecycle management

This comprehensive exercise guide provides hands-on experience with all major fragment concepts and patterns.
