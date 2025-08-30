package com.example.animations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimatedAdapter
    
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
        recyclerView.addItemDecoration(DividerItemDecoration())
        
        // Create and set adapter
        adapter = AnimatedAdapter(createSampleData())
        recyclerView.adapter = adapter
    }
    
    private fun createSampleData(): List<String> {
        return listOf(
            "Item 1 - Animated Entry",
            "Item 2 - Smooth Transitions",
            "Item 3 - Custom Animations",
            "Item 4 - RecyclerView Magic",
            "Item 5 - Material Design",
            "Item 6 - Performance Optimized",
            "Item 7 - Accessibility Ready",
            "Item 8 - Modern Android"
        )
    }
    
    private inner class AnimatedAdapter(private val items: List<String>) : 
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
    
    private inner class DividerItemDecoration : RecyclerView.ItemDecoration() {
        override fun onDraw(c: android.graphics.Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.GRAY
                strokeWidth = 1f
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
}
