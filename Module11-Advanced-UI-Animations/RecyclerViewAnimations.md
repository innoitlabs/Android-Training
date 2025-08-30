# RecyclerView Animations & Item Decorations

## Overview
RecyclerView provides powerful animation capabilities for list items, including built-in animations, custom item decorations, and staggered animations. This guide covers how to create smooth, engaging list animations.

## Key Concepts

### 1. Item Animations
- **DefaultItemAnimator**: Built-in animations for add, remove, move, and change operations
- **Custom ItemAnimator**: Create custom animation behaviors
- **LayoutAnimationController**: Animate items when they first appear

### 2. Item Decorations
- **ItemDecoration**: Add visual elements between items
- **Custom decorations**: Create unique visual effects
- **Performance considerations**: Efficient decoration drawing

## Basic RecyclerView Setup

### 1. Simple RecyclerView with Animations
```kotlin
class RecyclerActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        
        recyclerView = findViewById(R.id.recycler_view)
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Set custom item animator
        val itemAnimator = DefaultItemAnimator()
        itemAnimator.addDuration = 500
        itemAnimator.removeDuration = 500
        itemAnimator.moveDuration = 500
        itemAnimator.changeDuration = 500
        recyclerView.itemAnimator = itemAnimator
        
        // Add item decoration
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        
        // Create and set adapter
        adapter = MyAdapter(createSampleData())
        recyclerView.adapter = adapter
    }
}
```

### 2. Custom Adapter with Animations
```kotlin
class AnimatedAdapter(private val items: List<String>) : 
    RecyclerView.Adapter<AnimatedAdapter.ViewHolder>() {
    
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
        
        // Add staggered animation
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 50f
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .setStartDelay(position * 100L)
            .start()
    }
    
    override fun getItemCount() = items.size
}
```

## Custom Item Animations

### 1. Custom ItemAnimator
```kotlin
class CustomItemAnimator : DefaultItemAnimator() {
    
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.scaleX = 0.5f
        holder.itemView.scaleY = 0.5f
        
        holder.itemView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setInterpolator(OvershootInterpolator())
            .start()
        
        return true
    }
    
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.animate()
            .alpha(0f)
            .scaleX(0.5f)
            .scaleY(0.5f)
            .setDuration(300)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                holder.itemView.alpha = 1f
                holder.itemView.scaleX = 1f
                holder.itemView.scaleY = 1f
            }
            .start()
        
        return true
    }
    
    override fun animateMove(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        holder.itemView.translationX = (fromX - toX).toFloat()
        holder.itemView.translationY = (fromY - toY).toFloat()
        
        holder.itemView.animate()
            .translationX(0f)
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
        
        return true
    }
}
```

### 2. Layout Animation Controller
```kotlin
private fun setupLayoutAnimation() {
    val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
    val controller = LayoutAnimationController(animation)
    controller.delay = 0.1f
    controller.order = LayoutAnimationController.ORDER_NORMAL
    
    recyclerView.layoutAnimation = controller
}
```

## Item Decorations

### 1. Basic Divider Decoration
```kotlin
class DividerDecoration : RecyclerView.ItemDecoration() {
    
    private val paint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 1f
    }
    
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            
            c.drawLine(
                child.left.toFloat(),
                top.toFloat(),
                child.right.toFloat(),
                top.toFloat(),
                paint
            )
        }
    }
    
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = 1
    }
}
```

### 2. Advanced Decoration with Gradients
```kotlin
class GradientDecoration : RecyclerView.ItemDecoration() {
    
    private val paint = Paint()
    private lateinit var gradient: LinearGradient
    
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (!::gradient.isInitialized) {
            gradient = LinearGradient(
                0f, 0f, parent.width.toFloat(), 0f,
                Color.TRANSPARENT, Color.GRAY, Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
            paint.shader = gradient
        }
        
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            
            c.drawLine(
                child.left.toFloat(),
                top.toFloat(),
                child.right.toFloat(),
                top.toFloat(),
                paint
            )
        }
    }
}
```

### 3. Custom Background Decoration
```kotlin
class BackgroundDecoration : RecyclerView.ItemDecoration() {
    
    private val paint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.FILL
    }
    
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            
            // Draw background for every other item
            if (i % 2 == 0) {
                c.drawRect(
                    child.left.toFloat(),
                    child.top.toFloat(),
                    child.right.toFloat(),
                    child.bottom.toFloat(),
                    paint
                )
            }
        }
    }
}
```

## Swipe-to-Dismiss Functionality

### 1. ItemTouchHelper Setup
```kotlin
class SwipeToDismissCallback(private val adapter: MyAdapter) : 
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }
    
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.removeItem(position)
    }
    
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        
        val itemView = viewHolder.itemView
        val background = ColorDrawable(Color.RED)
        val icon = ContextCompat.getDrawable(recyclerView.context, android.R.drawable.ic_menu_delete)
        
        // Draw background
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(c)
        
        // Draw icon
        icon?.let {
            val iconMargin = (itemView.height - it.intrinsicHeight) / 2
            val iconTop = itemView.top + iconMargin
            val iconBottom = iconTop + it.intrinsicHeight
            val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            
            it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            it.draw(c)
        }
    }
}
```

### 2. Implementing Swipe-to-Dismiss
```kotlin
private fun setupSwipeToDismiss() {
    val swipeCallback = SwipeToDismissCallback(adapter)
    val itemTouchHelper = ItemTouchHelper(swipeCallback)
    itemTouchHelper.attachToRecyclerView(recyclerView)
}

// In adapter
fun removeItem(position: Int) {
    val item = items[position]
    items.removeAt(position)
    notifyItemRemoved(position)
    
    // Optional: Show undo functionality
    showUndoSnackbar(item, position)
}
```

## Staggered Animations

### 1. Staggered Grid Layout
```kotlin
private fun setupStaggeredLayout() {
    val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    recyclerView.layoutManager = layoutManager
    
    // Add staggered animation
    val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom)
    val controller = LayoutAnimationController(animation)
    controller.delay = 0.1f
    controller.order = LayoutAnimationController.ORDER_NORMAL
    
    recyclerView.layoutAnimation = controller
}
```

### 2. Custom Staggered Animation
```kotlin
class StaggeredAdapter(private val items: List<String>) : 
    RecyclerView.Adapter<StaggeredAdapter.ViewHolder>() {
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]
        
        // Staggered animation based on position
        val delay = position * 100L
        holder.itemView.alpha = 0f
        holder.itemView.translationY = 100f
        
        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(delay)
            .setInterpolator(OvershootInterpolator())
            .start()
    }
}
```

## Performance Optimization

### 1. Efficient Item Decorations
```kotlin
class OptimizedDecoration : RecyclerView.ItemDecoration() {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()
    
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // Reuse objects to avoid allocation
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            
            rect.set(
                child.left.toFloat(),
                child.top.toFloat(),
                child.right.toFloat(),
                child.bottom.toFloat()
            )
            
            c.drawRect(rect, paint)
        }
    }
}
```

### 2. ViewHolder Pattern Optimization
```kotlin
class OptimizedAdapter : RecyclerView.Adapter<OptimizedAdapter.ViewHolder>() {
    
    private val items = mutableListOf<String>()
    
    fun updateItems(newItems: List<String>) {
        val diffCallback = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newItems.size
            
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }
            
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition] == newItems[newItemPosition]
            }
        })
        
        items.clear()
        items.addAll(newItems)
        diffCallback.dispatchUpdatesTo(this)
    }
}
```

## Common Use Cases

### 1. Chat Interface
```kotlin
class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        
        // Animate new messages
        if (message.isNew) {
            holder.itemView.alpha = 0f
            holder.itemView.translationX = if (message.isFromMe) 100f else -100f
            
            holder.itemView.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }
}
```

### 2. Shopping Cart
```kotlin
class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    
    fun addItem(item: CartItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
        
        // Animate the new item
        val position = items.size - 1
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
        viewHolder?.itemView?.let { view ->
            view.scaleX = 0f
            view.scaleY = 0f
            view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }
}
```

## Debugging Tips

### 1. Animation Debugging
```kotlin
// Enable animation debugging
recyclerView.itemAnimator?.let { animator ->
    animator.addListener(object : RecyclerView.ItemAnimator.ItemAnimatorFinishedListener {
        override fun onAnimationsFinished() {
            Log.d("RecyclerView", "Animations finished")
        }
    })
}
```

### 2. Performance Monitoring
```kotlin
recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                // Optimize when scrolling stops
            }
            RecyclerView.SCROLL_STATE_DRAGGING -> {
                // Pause animations during scrolling
            }
        }
    }
})
```

## Summary
RecyclerView animations provide powerful ways to create engaging list experiences. Key points:
- Use DefaultItemAnimator for basic animations
- Create custom ItemAnimator for specialized behaviors
- Implement ItemDecoration for visual enhancements
- Use LayoutAnimationController for staggered animations
- Optimize performance with efficient drawing
- Consider user experience when designing animations

## Next Steps
- Explore advanced animation patterns
- Learn about RecyclerView item types
- Study Material Design list patterns
- Practice with complex list interactions
