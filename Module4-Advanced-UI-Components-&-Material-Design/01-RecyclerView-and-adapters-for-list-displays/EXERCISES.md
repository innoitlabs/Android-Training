# RecyclerView Exercises

## Overview

This document contains a series of exercises designed to help you master RecyclerView concepts in Android. Start with the easy exercises and work your way up to advanced challenges.

---

## Exercise 1: Basic RecyclerView Setup (Easy)

### Objective
Create a simple RecyclerView that displays a list of fruits.

### Requirements
- Create a `Fruit` data class with `name` and `color` properties
- Create a `FruitAdapter` with ViewHolder
- Display fruits in a vertical list
- Show fruit name and color in each item

### Starter Code
```kotlin
// Fruit.kt
data class Fruit(
    val name: String,
    val color: String
)

// Sample data
val fruits = listOf(
    Fruit("Apple", "Red"),
    Fruit("Banana", "Yellow"),
    Fruit("Orange", "Orange"),
    Fruit("Grape", "Purple"),
    Fruit("Strawberry", "Red")
)
```

### Expected Output
- A scrollable list showing fruit names and colors
- Each item should be clickable (show a Toast with fruit details)

### Hints
- Use `LinearLayoutManager` for vertical layout
- Create a simple item layout with two TextViews
- Implement basic click handling in the adapter

---

## Exercise 2: Enhanced Item Layout (Easy)

### Objective
Improve the fruit list by adding images and better styling.

### Requirements
- Add an ImageView for each fruit
- Use Material Design styling
- Add card-like appearance to items
- Implement proper spacing and padding

### Starter Code
```xml
<!-- item_fruit.xml -->
<com.google.android.material.card.MaterialCardView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardElevation="4dp"
    app:cardCornerRadius="8dp">
    
    <!-- Add your layout here -->
    
</com.google.android.material.card.MaterialCardView>
```

### Expected Output
- Cards with fruit images, names, and colors
- Material Design styling with shadows and rounded corners
- Smooth scrolling experience

### Hints
- Use `MaterialCardView` for card-like appearance
- Add proper constraints in your layout
- Use drawable resources for fruit images

---

## Exercise 3: Multiple View Types (Intermediate)

### Objective
Create a RecyclerView that displays different types of items (headers and content).

### Requirements
- Display section headers (e.g., "Fruits", "Vegetables")
- Show different layouts for headers vs content items
- Implement proper view type handling

### Starter Code
```kotlin
sealed class ListItem {
    data class Header(val title: String) : ListItem()
    data class Fruit(val name: String, val color: String) : ListItem()
}

val items = listOf(
    ListItem.Header("Fruits"),
    ListItem.Fruit("Apple", "Red"),
    ListItem.Fruit("Banana", "Yellow"),
    ListItem.Header("Vegetables"),
    ListItem.Fruit("Carrot", "Orange"),
    ListItem.Fruit("Broccoli", "Green")
)
```

### Expected Output
- Headers with different styling than content items
- Proper spacing between sections
- Different click behaviors for headers vs content

### Hints
- Override `getItemViewType()` in your adapter
- Create separate ViewHolder classes for each type
- Use different layouts for headers and content

---

## Exercise 4: Search and Filter (Intermediate)

### Objective
Add search functionality to filter the fruit list.

### Requirements
- Add a SearchView above the RecyclerView
- Implement real-time filtering
- Show "No results" message when no items match
- Preserve original list for reset functionality

### Starter Code
```kotlin
class FruitAdapter : RecyclerView.Adapter<FruitViewHolder>() {
    private var allFruits = listOf<Fruit>()
    private var filteredFruits = listOf<Fruit>()
    
    fun updateFruits(fruits: List<Fruit>) {
        allFruits = fruits
        filteredFruits = fruits
        notifyDataSetChanged()
    }
    
    fun filter(query: String) {
        // Implement filtering logic here
    }
}
```

### Expected Output
- Search bar that filters fruits in real-time
- Smooth animations when filtering
- Clear indication when no results are found

### Hints
- Use `SearchView.OnQueryTextListener`
- Implement case-insensitive search
- Consider using `DiffUtil` for smooth updates

---

## Exercise 5: Swipe to Delete (Intermediate)

### Objective
Implement swipe-to-delete functionality for list items.

### Requirements
- Swipe left to delete items
- Show confirmation dialog before deletion
- Animate item removal
- Update the data source properly

### Starter Code
```kotlin
class SwipeToDeleteCallback(
    private val adapter: FruitAdapter
) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false
    
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Implement deletion logic here
    }
}
```

### Expected Output
- Smooth swipe gestures
- Confirmation dialog before deletion
- Animated removal of items
- Proper state management

### Hints
- Use `ItemTouchHelper` for swipe detection
- Show `AlertDialog` for confirmation
- Use `notifyItemRemoved()` for smooth animations

---

## Exercise 6: Drag and Drop Reordering (Advanced)

### Objective
Allow users to reorder items by dragging them.

### Requirements
- Long press to start drag
- Visual feedback during drag
- Drop to reorder items
- Persist new order

### Starter Code
```kotlin
class DragDropCallback(
    private val adapter: FruitAdapter
) : ItemTouchHelper.Callback() {
    
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }
    
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Implement move logic here
        return true
    }
    
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Not used for drag and drop
    }
}
```

### Expected Output
- Long press to initiate drag
- Visual feedback showing drag state
- Smooth reordering animation
- Updated list order

### Hints
- Use `ItemTouchHelper` with drag flags
- Implement `onMove()` to handle reordering
- Update data source when items are moved

---

## Exercise 7: Grid Layout with Staggered Items (Advanced)

### Objective
Create a Pinterest-style grid layout with varying item heights.

### Requirements
- Use `StaggeredGridLayoutManager`
- Items should have different heights
- Implement proper image loading
- Handle orientation changes

### Starter Code
```kotlin
class PhotoAdapter : RecyclerView.Adapter<PhotoViewHolder>() {
    private val photos = mutableListOf<Photo>()
    
    data class Photo(
        val id: String,
        val url: String,
        val title: String,
        val height: Int // Random height for staggered effect
    )
}

// In MainActivity
recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
```

### Expected Output
- Pinterest-style grid layout
- Items with varying heights
- Smooth scrolling
- Proper image loading

### Hints
- Use `StaggeredGridLayoutManager`
- Implement image loading with Glide or Coil
- Handle configuration changes properly

---

## Exercise 8: Pagination with Loading (Advanced)

### Objective
Implement pagination to load data in chunks.

### Requirements
- Load initial data
- Show loading indicator at bottom
- Load more data when user scrolls to bottom
- Handle loading states properly

### Starter Code
```kotlin
class PaginatedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
    
    private var isLoading = false
    private val items = mutableListOf<Any>()
    
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
    
    fun addItems(newItems: List<Any>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }
}
```

### Expected Output
- Initial data loads quickly
- Loading indicator appears when scrolling to bottom
- Smooth loading of additional data
- Proper error handling

### Hints
- Use `RecyclerView.OnScrollListener` to detect bottom
- Implement loading states with different view types
- Handle network errors gracefully

---

## Exercise 9: Custom Item Animations (Advanced)

### Objective
Add custom animations for item appearance and removal.

### Requirements
- Fade-in animation for new items
- Slide-out animation for removed items
- Scale animation for item updates
- Smooth transitions between states

### Starter Code
```kotlin
class CustomItemAnimator : DefaultItemAnimator() {
    
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 100f
        
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(FastOutSlowInInterpolator())
            .start()
        
        return true
    }
    
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        // Implement remove animation
        return true
    }
}
```

### Expected Output
- Smooth fade-in for new items
- Elegant slide-out for removed items
- Professional-looking animations
- Consistent animation timing

### Hints
- Extend `DefaultItemAnimator`
- Use Android's animation framework
- Consider using Material Design motion library

---

## Exercise 10: Complete Contacts App (Project)

### Objective
Build a complete contacts application using all RecyclerView concepts.

### Requirements
- Display contacts in a list with photos
- Search and filter functionality
- Add new contacts
- Edit existing contacts
- Delete contacts with swipe
- Sort contacts alphabetically
- Implement proper data persistence

### Features to Implement
1. **Contact List**: Display contacts with photos, names, and phone numbers
2. **Search**: Real-time search through contacts
3. **Add Contact**: Form to add new contacts
4. **Edit Contact**: Tap to edit contact details
5. **Delete Contact**: Swipe to delete with confirmation
6. **Sort Options**: Sort by name, phone, or date added
7. **Data Persistence**: Save contacts to local storage

### Expected Output
- Full-featured contacts application
- Professional UI/UX
- Proper error handling
- Smooth animations and transitions
- Data persistence across app restarts

### Implementation Tips
- Use Room database for data persistence
- Implement MVVM architecture
- Use ViewBinding for type-safe view access
- Add proper error handling and loading states
- Test on different screen sizes

---

## Bonus Challenges

### Challenge 1: Multi-Selection Mode
Implement multi-selection mode where users can select multiple items and perform bulk operations.

### Challenge 2: Section Headers with Sticky Positioning
Create sticky section headers that remain visible while scrolling through their section.

### Challenge 3: Infinite Scrolling with Virtualization
Implement true infinite scrolling with virtualized data loading.

### Challenge 4: Custom Layout Manager
Create a custom LayoutManager for a unique layout arrangement.

---

## Testing Your Solutions

### Unit Tests
Write unit tests for your adapters and data models:
```kotlin
@Test
fun `adapter displays correct number of items`() {
    val adapter = FruitAdapter(listOf(Fruit("Apple", "Red")))
    assertEquals(1, adapter.itemCount)
}
```

### UI Tests
Test your RecyclerView interactions:
```kotlin
@Test
fun testItemClick() {
    onView(withId(R.id.recyclerView))
        .perform(RecyclerViewActions.actionOnItemAtPosition<FruitViewHolder>(0, click()))
    
    onView(withText("Apple"))
        .check(matches(isDisplayed()))
}
```

---

## Submission Guidelines

For each exercise:
1. **Code**: Clean, well-commented Kotlin code
2. **Layout**: Properly structured XML layouts
3. **Documentation**: Brief explanation of your approach
4. **Testing**: Basic tests to verify functionality

### Evaluation Criteria
- **Functionality**: Does it work as expected?
- **Code Quality**: Is the code clean and maintainable?
- **Performance**: Is it efficient and smooth?
- **User Experience**: Is it intuitive and responsive?

---

## Resources

- [RecyclerView Documentation](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [Material Design Components](https://material.io/components)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

Happy coding! 🚀
