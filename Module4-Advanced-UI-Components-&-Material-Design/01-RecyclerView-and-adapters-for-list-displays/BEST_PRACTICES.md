# RecyclerView Best Practices

## Overview

This guide covers essential best practices for developing efficient and maintainable RecyclerView implementations in Android.

---

## 1. Performance Optimization

### 1.1 Use ViewHolder Pattern Correctly

**✅ Good Practice:**
```kotlin
class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameText: TextView = itemView.findViewById(R.id.nameText)
    val phoneText: TextView = itemView.findViewById(R.id.phoneText)
    
    fun bind(contact: Contact) {
        nameText.text = contact.name
        phoneText.text = contact.phone
    }
}
```

**❌ Avoid:**
```kotlin
override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
    val nameText = holder.itemView.findViewById<TextView>(R.id.nameText)
    val phoneText = holder.itemView.findViewById<TextView>(R.id.phoneText)
    // This defeats the purpose of ViewHolder
}
```

### 1.2 Minimize Object Creation

**✅ Good Practice:**
```kotlin
class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {
    private val contacts = mutableListOf<Contact>()
    
    fun updateContacts(newContacts: List<Contact>) {
        contacts.clear()
        contacts.addAll(newContacts)
        notifyDataSetChanged()
    }
}
```

**✅ Better Practice (with DiffUtil):**
```kotlin
class ContactAdapter : ListAdapter<Contact, ContactViewHolder>(ContactDiffCallback()) {
    // DiffUtil handles efficient updates automatically
}

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}
```

### 1.3 Use ConstraintLayout for Item Layouts

**✅ Good Practice:**
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    
    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="56dp"
        android:layout_height="56dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent" />
    
    <TextView
        android:id="@+id/nameText"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toEndOf="@id/profileImage"
        app:layout_constraintEnd_toStartOf="@id/phoneButton"
        app:layout_constraintTop_toTopOf="parent" />
        
</androidx.constraintlayout.widget.ConstraintLayout>
```

---

## 2. Memory Management

### 2.1 Avoid Memory Leaks

**✅ Good Practice:**
```kotlin
class ContactAdapter(
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactViewHolder>() {
    
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        
        // Use weak references for long-running operations
        holder.itemView.setOnClickListener {
            onItemClick(contact)
        }
    }
}
```

### 2.2 Handle Image Loading Efficiently

**✅ Good Practice:**
```kotlin
// Using Glide or Coil for efficient image loading
Glide.with(holder.profileImage.context)
    .load(contact.profileImageUrl)
    .placeholder(R.drawable.default_avatar)
    .error(R.drawable.error_avatar)
    .circleCrop()
    .into(holder.profileImage)
```

---

## 3. User Experience

### 3.1 Implement Proper Click Handling

**✅ Good Practice:**
```kotlin
class ContactAdapter(
    private val onItemClick: (Contact) -> Unit,
    private val onCallClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactViewHolder>() {
    
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        
        // Item click
        holder.itemView.setOnClickListener {
            onItemClick(contact)
        }
        
        // Specific button click
        holder.callButton.setOnClickListener {
            onCallClick(contact)
        }
        
        // Prevent event bubbling
        holder.callButton.setOnClickListener { event ->
            event.stopPropagation()
            onCallClick(contact)
        }
    }
}
```

### 3.2 Add Visual Feedback

**✅ Good Practice:**
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:background="?android:attr/selectableItemBackground"
    android:clickable="true"
    android:focusable="true">
    <!-- Item content -->
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 3.3 Implement Loading States

**✅ Good Practice:**
```kotlin
sealed class ContactListState {
    object Loading : ContactListState()
    data class Success(val contacts: List<Contact>) : ContactListState()
    data class Error(val message: String) : ContactListState()
}

class ContactAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    private var state: ContactListState = ContactListState.Loading
    
    fun updateState(newState: ContactListState) {
        val oldState = state
        state = newState
        
        when (newState) {
            is ContactListState.Loading -> {
                // Show loading view
            }
            is ContactListState.Success -> {
                // Update contacts list
                updateContacts(newState.contacts)
            }
            is ContactListState.Error -> {
                // Show error view
            }
        }
    }
}
```

---

## 4. Code Organization

### 4.1 Separate Concerns

**✅ Good Practice:**
```kotlin
// Data model
data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val email: String
)

// Adapter
class ContactAdapter(
    private val onItemClick: (Contact) -> Unit
) : ListAdapter<Contact, ContactViewHolder>(ContactDiffCallback())

// ViewHolder
class ContactViewHolder(
    private val binding: ItemContactBinding
) : RecyclerView.ViewHolder(binding.root) {
    
    fun bind(contact: Contact, onItemClick: (Contact) -> Unit) {
        binding.apply {
            nameText.text = contact.name
            phoneText.text = contact.phone
            emailText.text = contact.email
            
            root.setOnClickListener { onItemClick(contact) }
        }
    }
}
```

### 4.2 Use View Binding

**✅ Good Practice:**
```kotlin
class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactViewHolder(binding)
    }
}

class ContactViewHolder(
    private val binding: ItemContactBinding
) : RecyclerView.ViewHolder(binding.root) {
    
    fun bind(contact: Contact) {
        binding.nameText.text = contact.name
        binding.phoneText.text = contact.phone
    }
}
```

---

## 5. Advanced Patterns

### 5.1 Multiple View Types

**✅ Good Practice:**
```kotlin
class ContactAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_CONTACT = 0
        private const val VIEW_TYPE_HEADER = 1
    }
    
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Contact -> VIEW_TYPE_CONTACT
            is Header -> VIEW_TYPE_HEADER
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CONTACT -> ContactViewHolder.create(parent)
            VIEW_TYPE_HEADER -> HeaderViewHolder.create(parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }
}
```

### 5.2 Pagination

**✅ Good Practice:**
```kotlin
class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {
    
    private var isLoading = false
    
    fun setLoading(loading: Boolean) {
        if (isLoading != loading) {
            isLoading = loading
            if (loading) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
        }
    }
    
    override fun getItemCount(): Int {
        return contacts.size + if (isLoading) 1 else 0
    }
}
```

---

## 6. Testing

### 6.1 Unit Testing

**✅ Good Practice:**
```kotlin
@Test
fun `adapter displays correct number of items`() {
    val contacts = listOf(
        Contact("Alice", "123"),
        Contact("Bob", "456")
    )
    val adapter = ContactAdapter(contacts, {})
    
    assertEquals(2, adapter.itemCount)
}

@Test
fun `adapter calls onItemClick when item is clicked`() {
    val contact = Contact("Alice", "123")
    var clickedContact: Contact? = null
    
    val adapter = ContactAdapter(listOf(contact)) { clickedContact = it }
    val holder = adapter.onCreateViewHolder(mock(), 0)
    
    adapter.onBindViewHolder(holder, 0)
    holder.itemView.performClick()
    
    assertEquals(contact, clickedContact)
}
```

### 6.2 UI Testing

**✅ Good Practice:**
```kotlin
@Test
fun testRecyclerViewDisplaysContacts() {
    launchActivity<MainActivity>().use {
        onView(withId(R.id.recyclerView))
            .check(matches(hasMinimumChildCount(1)))
        
        onView(withText("Alice Johnson"))
            .check(matches(isDisplayed()))
    }
}
```

---

## 7. Common Anti-Patterns to Avoid

### 7.1 Don't Call notifyDataSetChanged() Frequently

**❌ Avoid:**
```kotlin
fun updateData(newData: List<Contact>) {
    contacts = newData
    notifyDataSetChanged() // Expensive operation
}
```

**✅ Use DiffUtil Instead:**
```kotlin
fun updateData(newData: List<Contact>) {
    val diffResult = DiffUtil.calculateDiff(ContactDiffCallback(contacts, newData))
    contacts = newData
    diffResult.dispatchUpdatesTo(this)
}
```

### 7.2 Don't Create New Objects in onBindViewHolder

**❌ Avoid:**
```kotlin
override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
    val contact = contacts[position]
    holder.itemView.setOnClickListener { 
        // Creating new object every time
        Intent(holder.itemView.context, DetailActivity::class.java).apply {
            putExtra("contact", contact)
            holder.itemView.context.startActivity(this)
        }
    }
}
```

**✅ Pre-create Objects:**
```kotlin
class ContactAdapter : RecyclerView.Adapter<ContactViewHolder>() {
    private val clickListener = View.OnClickListener { view ->
        val position = view.tag as Int
        val contact = getItem(position)
        onItemClick(contact)
    }
    
    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.itemView.tag = position
        holder.itemView.setOnClickListener(clickListener)
    }
}
```

---

## 8. Performance Monitoring

### 8.1 Use Android Profiler

- Monitor memory usage during scrolling
- Check for memory leaks
- Analyze CPU usage patterns

### 8.2 Implement Performance Metrics

```kotlin
class PerformanceMonitor {
    fun measureBindTime(block: () -> Unit) {
        val startTime = System.nanoTime()
        block()
        val endTime = System.nanoTime()
        Log.d("Performance", "Bind time: ${endTime - startTime} ns")
    }
}
```

---

## Summary

Following these best practices will help you create:

- **Efficient** RecyclerView implementations
- **Maintainable** code that's easy to understand and modify
- **User-friendly** interfaces with smooth interactions
- **Testable** components that can be easily verified
- **Scalable** solutions that perform well with large datasets

Remember: The key to good RecyclerView implementation is understanding the ViewHolder pattern, minimizing object creation, and using the right tools for the job (DiffUtil, View Binding, etc.).
