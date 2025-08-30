package com.example.recyclerviewandadapters

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Examples of different RecyclerView LayoutManagers
 * 
 * This class demonstrates how to use various layout managers:
 * - LinearLayoutManager (vertical and horizontal)
 * - GridLayoutManager
 * - StaggeredGridLayoutManager
 */
object LayoutManagerExamples {

    /**
     * Creates a vertical LinearLayoutManager (default list behavior)
     */
    fun createVerticalLinearLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(null).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
    }

    /**
     * Creates a horizontal LinearLayoutManager (for horizontal scrolling)
     */
    fun createHorizontalLinearLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(null).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    /**
     * Creates a GridLayoutManager with specified number of columns
     * 
     * @param spanCount Number of columns in the grid
     */
    fun createGridLayoutManager(spanCount: Int = 2): GridLayoutManager {
        return GridLayoutManager(null, spanCount)
    }

    /**
     * Creates a StaggeredGridLayoutManager for Pinterest-style layouts
     * 
     * @param spanCount Number of columns
     * @param orientation Orientation (VERTICAL or HORIZONTAL)
     */
    fun createStaggeredGridLayoutManager(
        spanCount: Int = 2,
        orientation: Int = StaggeredGridLayoutManager.VERTICAL
    ): StaggeredGridLayoutManager {
        return StaggeredGridLayoutManager(spanCount, orientation)
    }

    /**
     * Applies a layout manager to a RecyclerView
     * 
     * @param recyclerView The RecyclerView to configure
     * @param layoutManagerType Type of layout manager to apply
     * @param spanCount Number of columns (for grid layouts)
     */
    fun applyLayoutManager(
        recyclerView: RecyclerView,
        layoutManagerType: LayoutManagerType,
        spanCount: Int = 2
    ) {
        val layoutManager = when (layoutManagerType) {
            LayoutManagerType.VERTICAL_LINEAR -> createVerticalLinearLayoutManager()
            LayoutManagerType.HORIZONTAL_LINEAR -> createHorizontalLinearLayoutManager()
            LayoutManagerType.GRID -> createGridLayoutManager(spanCount)
            LayoutManagerType.STAGGERED_GRID -> createStaggeredGridLayoutManager(spanCount)
        }
        
        recyclerView.layoutManager = layoutManager
    }
}

/**
 * Enum representing different types of layout managers
 */
enum class LayoutManagerType {
    VERTICAL_LINEAR,
    HORIZONTAL_LINEAR,
    GRID,
    STAGGERED_GRID
}

/**
 * Extension function to easily apply layout managers to RecyclerView
 */
fun RecyclerView.applyLayoutManager(
    type: LayoutManagerType,
    spanCount: Int = 2
) {
    LayoutManagerExamples.applyLayoutManager(this, type, spanCount)
}
