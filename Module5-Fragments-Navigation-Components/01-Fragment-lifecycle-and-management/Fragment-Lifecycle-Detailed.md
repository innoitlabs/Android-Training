# Fragment Lifecycle - Detailed Guide

## Overview

The Fragment lifecycle is more complex than the Activity lifecycle because Fragments can be created, destroyed, and recreated independently of their host Activity. Understanding this lifecycle is crucial for proper resource management and avoiding memory leaks.

## Complete Lifecycle Flow

```
INITIALIZED
    ↓
ATTACHED (onAttach)
    ↓
CREATED (onCreate)
    ↓
VIEW_CREATED (onCreateView, onViewCreated)
    ↓
STARTED (onStart)
    ↓
RESUMED (onResume)
    ↓
PAUSED (onPause)
    ↓
STOPPED (onStop)
    ↓
VIEW_DESTROYED (onDestroyView)
    ↓
DESTROYED (onDestroy)
    ↓
DETACHED (onDetach)
```

## Lifecycle Methods in Detail

### 1. onAttach(Context context)
- **Called when**: Fragment is attached to its Activity
- **Purpose**: Initialize Fragment-Activity communication
- **Common use cases**: 
  - Store Activity reference
  - Initialize callbacks
  - Set up communication channels

```kotlin
override fun onAttach(context: Context) {
    super.onAttach(context)
    Log.d(TAG, "onAttach called")
    
    // Example: Set up callback interface
    if (context is OnFragmentInteractionListener) {
        listener = context
    } else {
        throw RuntimeException("$context must implement OnFragmentInteractionListener")
    }
}
```

### 2. onCreate(Bundle savedInstanceState)
- **Called when**: Fragment is being created
- **Purpose**: Initialize non-UI components
- **Common use cases**:
  - Initialize variables
  - Set up data sources
  - Configure Fragment behavior

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate called")
    
    // Example: Initialize data
    arguments?.let {
        userId = it.getString(ARG_USER_ID)
    }
}
```

### 3. onCreateView(LayoutInflater, ViewGroup?, Bundle?)
- **Called when**: Fragment needs to create its UI
- **Purpose**: Inflate and return the Fragment's view hierarchy
- **Important**: Always return the inflated view or null

```kotlin
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    Log.d(TAG, "onCreateView called")
    
    // Inflate the layout
    val view = inflater.inflate(R.layout.fragment_home, container, false)
    
    // Initialize views (basic setup only)
    val textView = view.findViewById<TextView>(R.id.textView)
    textView.text = "Hello from Fragment!"
    
    return view
}
```

### 4. onViewCreated(View, Bundle?)
- **Called when**: View hierarchy is created and attached
- **Purpose**: Setup UI components and event listeners
- **Best practice**: Do all UI-related work here

```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Log.d(TAG, "onViewCreated called")
    
    // Setup UI components
    val button = view.findViewById<Button>(R.id.button)
    button.setOnClickListener {
        // Handle click
    }
    
    // Load data into views
    loadUserData()
}
```

### 5. onStart()
- **Called when**: Fragment becomes visible to user
- **Purpose**: Prepare for user interaction
- **Common use cases**: Start animations, refresh data

```kotlin
override fun onStart() {
    super.onStart()
    Log.d(TAG, "onStart called")
    
    // Example: Start animations or refresh data
    startAnimation()
    refreshData()
}
```

### 6. onResume()
- **Called when**: Fragment becomes interactive
- **Purpose**: Resume Fragment functionality
- **Common use cases**: Resume animations, start sensors

```kotlin
override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume called")
    
    // Example: Resume animations or start sensors
    resumeAnimation()
    startLocationUpdates()
}
```

### 7. onPause()
- **Called when**: Fragment is no longer interactive
- **Purpose**: Pause Fragment functionality
- **Common use cases**: Pause animations, stop sensors

```kotlin
override fun onPause() {
    super.onPause()
    Log.d(TAG, "onPause called")
    
    // Example: Pause animations or stop sensors
    pauseAnimation()
    stopLocationUpdates()
}
```

### 8. onStop()
- **Called when**: Fragment is no longer visible
- **Purpose**: Stop Fragment operations
- **Common use cases**: Stop background tasks

```kotlin
override fun onStop() {
    super.onStop()
    Log.d(TAG, "onStop called")
    
    // Example: Stop background tasks
    stopBackgroundTask()
}
```

### 9. onDestroyView()
- **Called when**: Fragment's view is being destroyed
- **Purpose**: Clean up view-related resources
- **Critical**: Clear view references to prevent memory leaks

```kotlin
override fun onDestroyView() {
    super.onDestroyView()
    Log.d(TAG, "onDestroyView called")
    
    // CRITICAL: Clear view references to prevent memory leaks
    _binding = null
    
    // Clean up other view-related resources
    cancelAnimations()
    clearViewListeners()
}
```

### 10. onDestroy()
- **Called when**: Fragment is being destroyed
- **Purpose**: Clean up Fragment resources
- **Common use cases**: Cancel operations, clear data

```kotlin
override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "onDestroy called")
    
    // Clean up Fragment resources
    cancelAllOperations()
    clearData()
}
```

### 11. onDetach()
- **Called when**: Fragment is detached from Activity
- **Purpose**: Final cleanup
- **Common use cases**: Clear Activity references

```kotlin
override fun onDetach() {
    super.onDetach()
    Log.d(TAG, "onDetach called")
    
    // Clear Activity references
    listener = null
}
```

## Configuration Changes

### Handling Screen Rotation

When the device configuration changes (rotation, language, etc.), the Fragment's view is destroyed and recreated, but the Fragment instance may be retained.

```kotlin
class MyFragment : Fragment() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Retain Fragment instance across configuration changes
        retainInstance = true
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // savedInstanceState contains data saved in onSaveInstanceState
        return inflater.inflate(R.layout.fragment_my, container, false)
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save important data
        outState.putString("key", "value")
    }
}
```

## Memory Management Best Practices

### 1. Clear View References

```kotlin
class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }
}
```

### 2. Cancel Operations

```kotlin
class MyFragment : Fragment() {
    private var job: Job? = null
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        job = lifecycleScope.launch {
            // Perform background operation
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()  // Cancel ongoing operations
    }
}
```

### 3. Remove Listeners

```kotlin
class MyFragment : Fragment() {
    private var locationListener: LocationListener? = null
    
    override fun onResume() {
        super.onResume()
        locationListener = LocationListener { /* handle location */ }
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener)
    }
    
    override fun onPause() {
        super.onPause()
        locationListener?.let { listener ->
            locationManager.removeUpdates(listener)
        }
    }
}
```

## Common Lifecycle Scenarios

### Scenario 1: Fragment Added to Back Stack

```
Activity: onCreate → onStart → onResume
Fragment: onAttach → onCreate → onCreateView → onViewCreated → onStart → onResume
```

### Scenario 2: Fragment Replaced (Not Added to Back Stack)

```
Old Fragment: onPause → onStop → onDestroyView → onDestroy → onDetach
New Fragment: onAttach → onCreate → onCreateView → onViewCreated → onStart → onResume
```

### Scenario 3: Configuration Change

```
Fragment: onPause → onStop → onDestroyView
Fragment: onCreateView → onViewCreated → onStart → onResume
```

### Scenario 4: Activity Destroyed

```
Fragment: onPause → onStop → onDestroyView → onDestroy → onDetach
Activity: onPause → onStop → onDestroy
```

## Testing Lifecycle

### Logging All Lifecycle Methods

```kotlin
class LifecycleLoggingFragment : Fragment() {
    private val TAG = "LifecycleLogging"
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_lifecycle_logging, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
    
    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }
}
```

## Summary

Understanding the Fragment lifecycle is essential for:

1. **Proper Resource Management**: Clean up resources at the right time
2. **Memory Leak Prevention**: Clear references in onDestroyView
3. **State Management**: Save and restore state appropriately
4. **Performance Optimization**: Start/stop operations efficiently
5. **User Experience**: Handle configuration changes gracefully

The key is to always think about the lifecycle when implementing Fragment functionality and ensure proper cleanup to prevent memory leaks and crashes.
