package com.example.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class TwoColumnLayout @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : ViewGroup(context, attrs) {

    private var columnCount = 2
    private var columnSpacing = 16f
    private var rowSpacing = 16f

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TwoColumnLayout,
            0, 0
        ).apply {
            try {
                columnCount = getInteger(R.styleable.TwoColumnLayout_columnCount, 2)
                columnSpacing = getDimension(R.styleable.TwoColumnLayout_columnSpacing, 16f)
                rowSpacing = getDimension(R.styleable.TwoColumnLayout_rowSpacing, 16f)
            } finally {
                recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val availableWidth = width - (columnCount - 1) * columnSpacing.toInt()
        val columnWidth = availableWidth / columnCount

        var totalHeight = 0
        var currentRowHeight = 0
        var itemsInCurrentRow = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childSpec = MeasureSpec.makeMeasureSpec(columnWidth, MeasureSpec.EXACTLY)
            measureChild(child, childSpec, heightMeasureSpec)

            currentRowHeight = maxOf(currentRowHeight, child.measuredHeight)
            itemsInCurrentRow++

            if (itemsInCurrentRow == columnCount) {
                totalHeight += currentRowHeight
                if (i < childCount - 1) totalHeight += rowSpacing.toInt()
                currentRowHeight = 0
                itemsInCurrentRow = 0
            }
        }

        // Handle remaining items in the last row
        if (itemsInCurrentRow > 0) {
            totalHeight += currentRowHeight
        }

        setMeasuredDimension(width, totalHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        val availableWidth = width - (columnCount - 1) * columnSpacing.toInt()
        val columnWidth = availableWidth / columnCount

        var currentX = left
        var currentY = top
        var currentRowHeight = 0
        var itemsInCurrentRow = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childHeight = child.measuredHeight

            child.layout(currentX, currentY, currentX + columnWidth, currentY + childHeight)

            currentRowHeight = maxOf(currentRowHeight, childHeight)
            itemsInCurrentRow++

            if (itemsInCurrentRow == columnCount) {
                currentY += currentRowHeight + rowSpacing.toInt()
                currentX = left
                currentRowHeight = 0
                itemsInCurrentRow = 0
            } else {
                currentX += columnWidth + columnSpacing.toInt()
            }
        }
    }
}
