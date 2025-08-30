package com.example.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CustomProgressView @JvmOverloads constructor(
    context: Context, 
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val backgroundPaint = Paint().apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private val progressPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    private var progress = 0f
    private var maxProgress = 100f
    private var showText = true

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomProgressView,
            0, 0
        ).apply {
            try {
                progressPaint.color = getColor(R.styleable.CustomProgressView_progressColor, Color.BLUE)
                backgroundPaint.color = getColor(R.styleable.CustomProgressView_backgroundColor, Color.LTGRAY)
                progress = getFloat(R.styleable.CustomProgressView_progress, 0f)
                maxProgress = getFloat(R.styleable.CustomProgressView_maxProgress, 100f)
                showText = getBoolean(R.styleable.CustomProgressView_showText, true)
                
                val strokeWidth = getDimension(R.styleable.CustomProgressView_progressStrokeWidth, 20f)
                progressPaint.strokeWidth = strokeWidth
                backgroundPaint.strokeWidth = strokeWidth
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) / 2 - 30).toFloat()

        // Draw background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Draw progress arc
        val sweepAngle = (progress / maxProgress) * 360f
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f, // Start from top
            sweepAngle,
            false,
            progressPaint
        )

        // Draw progress text
        if (showText) {
            val progressText = "${progress.toInt()}%"
            canvas.drawText(progressText, centerX, centerY + textPaint.textSize / 3, textPaint)
        }
    }

    fun setProgress(newProgress: Float) {
        progress = newProgress.coerceIn(0f, maxProgress)
        invalidate()
    }

    fun animateProgress(targetProgress: Float, duration: Long = 1000) {
        val animator = ValueAnimator.ofFloat(progress, targetProgress)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            setProgress(animation.animatedValue as Float)
        }
        animator.start()
    }

    fun setProgressColor(color: Int) {
        progressPaint.color = color
        invalidate()
    }

    fun setProgressBackgroundColor(color: Int) {
        backgroundPaint.color = color
        invalidate()
    }

    fun setShowText(show: Boolean) {
        showText = show
        invalidate()
    }
}
