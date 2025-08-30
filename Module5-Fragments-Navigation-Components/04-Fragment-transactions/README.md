# Android Fragment Transactions & Communication Patterns

## Learning Objectives

By the end of this lesson, learners will be able to:
- Understand fragment transactions using FragmentManager and FragmentTransaction
- Replace, add, and remove fragments dynamically
- Manage the back stack for navigation
- Implement fragment-to-fragment communication patterns
- Use shared ViewModel for communication between fragments
- Follow best practices for fragment management

## Table of Contents

1. [Introduction](#introduction)
2. [Fragment Transactions](#fragment-transactions)
3. [Fragment Communication Patterns](#fragment-communication-patterns)
4. [Best Practices](#best-practices)
5. [Hands-on Lab](#hands-on-lab)
6. [Exercises](#exercises)
7. [Summary](#summary)

## Introduction

### What are Fragments?
Fragments are modular pieces of UI and logic that live inside an Activity. They represent a behavior or a portion of user interface in an Activity.

### Key Concepts
- **Fragment Transactions**: Operations (add, replace, remove) managed by FragmentManager
- **Back Stack**: Allows navigation backwards through fragment transactions
- **Fragment Communication**: Necessary when two fragments need to exchange data

## Fragment Transactions

### Basic Transaction Operations

```kotlin
supportFragmentManager.beginTransaction()
    .replace(R.id.fragment_container, HomeFragment())
    .addToBackStack(null) // allows going back
    .commit()
```

### Available Operations:
- `add()` → Adds a fragment
- `replace()` → Replaces current fragment
- `remove()` → Removes a fragment
- `addToBackStack()` → Enables back navigation

### Transaction Lifecycle
1. **beginTransaction()** - Start a new transaction
2. **Configure operations** - Add, replace, remove fragments
3. **addToBackStack()** - Optional: Add to back stack
4. **commit()** - Execute the transaction

## Fragment Communication Patterns

### 1. Interface-Based Communication (Tight Coupling)

**Pros:**
- Direct communication
- Type-safe
- Clear contract

**Cons:**
- Tight coupling between fragments
- Activity must implement interface
- Harder to test

**Implementation:**
```kotlin
// Interface definition
interface OnMessageSendListener {
    fun onMessageSend(message: String)
}

// In Fragment
private var listener: OnMessageSendListener? = null

override fun onAttach(context: Context) {
    super.onAttach(context)
    listener = context as? OnMessageSendListener
}

// Send data
button.setOnClickListener {
    listener?.onMessageSend("Hello from Fragment")
}
```

### 2. Shared ViewModel Communication (Recommended)

**Pros:**
- Loose coupling
- Survives configuration changes
- Easy to test
- Supports multiple observers

**Cons:**
- Slightly more complex setup
- Need to handle lifecycle

**Implementation:**
```kotlin
// Shared ViewModel
class SharedViewModel : ViewModel() {
    val message = MutableLiveData<String>()
}

// In Fragments
private val sharedViewModel: SharedViewModel by activityViewModels()

// Send data
sharedViewModel.message.value = "Hello from Fragment"

// Receive data
sharedViewModel.message.observe(viewLifecycleOwner) { msg ->
    textView.text = msg
}
```

## Best Practices

### Fragment Management
- Use `replace()` instead of `add()` for full-screen fragments
- Always call `addToBackStack()` if you want "Back" navigation
- Use meaningful back stack names for debugging

### Communication
- Prefer Shared ViewModel over interfaces for loose coupling
- Keep fragments UI-focused (business logic in ViewModel)
- Handle configuration changes properly

### Lifecycle Management
- Use `viewLifecycleOwner` for LiveData observations
- Clean up resources in `onDestroyView()`
- Avoid memory leaks by nullifying references

## Hands-on Lab

### Mini Project: Message Passing App

Build a 2-fragment app with the following features:
- **HomeFragment**: EditText + Button for input
- **DetailFragment**: Displays the typed text
- **Communication**: Both interface and SharedViewModel methods
- **Navigation**: Forward & back with proper back stack

### Project Structure
```
FragmentTransactions/
├── app/src/main/
│   ├── java/com/example/fragmenttransactions/
│   │   ├── MainActivity.kt
│   │   ├── HomeFragment.kt
│   │   ├── DetailFragment.kt
│   │   └── SharedViewModel.kt
│   └── res/layout/
│       ├── activity_main.xml
│       ├── fragment_home.xml
│       └── fragment_detail.xml
```

## Exercises

### Easy Level
- Log lifecycle events when fragments are replaced
- Add transition animations to fragment transactions

### Intermediate Level
- Pass data using an interface from Home → Detail
- Implement bi-directional communication

### Advanced Level
- Use SharedViewModel for multi-fragment communication
- Add fragment state preservation during configuration changes
- Implement custom fragment transitions

## Summary

### Key Takeaways
- Fragment transactions enable dynamic UI changes
- Back stack provides proper navigation history
- Communication patterns:
  - Interfaces (tight coupling, direct communication)
  - Shared ViewModel (loose coupling, recommended)
- Follow lifecycle best practices for robust apps

### Next Steps
- Explore Navigation Component for more complex navigation
- Learn about Fragment Result API for modern communication
- Practice with real-world scenarios and edge cases

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Run on emulator or device
4. Test all navigation and communication features

## Troubleshooting

### Common Issues
- **Fragment not showing**: Check container ID and layout
- **Back navigation not working**: Ensure `addToBackStack()` is called
- **Communication not working**: Verify interface implementation or ViewModel setup
- **Memory leaks**: Use `viewLifecycleOwner` for LiveData observations
