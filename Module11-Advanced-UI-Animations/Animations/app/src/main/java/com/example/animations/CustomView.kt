package com.example.animations

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0f
    private var rotationAngle = 0f
    private var animator: ValueAnimator? = null
    
    init {
        paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        
        startAnimation()
    }
    
    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            
            addUpdateListener { animation ->
                progress = animation.animatedValue as Float
                rotationAngle = progress * 360f
                invalidate()
            }
        }
        animator?.start()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 3f
        
        // Draw background circle
        paint.color = Color.LTGRAY
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerX, centerY, radius, paint)
        
        // Draw progress arc
        paint.color = Color.BLUE
        canvas.save()
        canvas.rotate(rotationAngle, centerX, centerY)
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            -90f,
            progress * 360f,
            false,
            paint
        )
        canvas.restore()
        
        // Draw center text
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.textSize = 40f
        paint.textAlign = Paint.Align.CENTER
        val text = "${(progress * 100).toInt()}%"
        canvas.drawText(text, centerX, centerY + 15f, paint)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}
