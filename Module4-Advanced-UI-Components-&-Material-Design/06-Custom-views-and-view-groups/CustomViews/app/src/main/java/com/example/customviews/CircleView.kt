package com.example.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircleView,
            0, 0
        ).apply {
            try {
                paint.color = getColor(R.styleable.CircleView_circleColor, Color.RED)
                paint.strokeWidth = getDimension(R.styleable.CircleView_circleStrokeWidth, 5f)
                
                val fillStyle = getInteger(R.styleable.CircleView_fillStyle, 0)
                paint.style = if (fillStyle == 0) Paint.Style.FILL else Paint.Style.STROKE
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2).toFloat()
        
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    fun setColor(color: Int) {
        paint.color = color
        invalidate()
    }

    fun setFillStyle(isFill: Boolean) {
        paint.style = if (isFill) Paint.Style.FILL else Paint.Style.STROKE
        invalidate()
    }
}
