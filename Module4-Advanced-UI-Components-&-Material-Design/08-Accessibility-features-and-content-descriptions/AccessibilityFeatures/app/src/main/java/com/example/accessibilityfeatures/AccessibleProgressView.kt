package com.example.accessibilityfeatures

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AccessibleProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var progress = 0
    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    
    init {
        isFocusable = true
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
    }
    
    fun setProgress(newProgress: Int) {
        progress = newProgress.coerceIn(0, 100)
        updateAccessibilityInfo()
        invalidate()
    }
    
    fun getProgress(): Int = progress
    
    private fun updateAccessibilityInfo() {
        contentDescription = "Progress: $progress%"
        // Using the newer API for accessibility announcements
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val progressWidth = (width * progress / 100f)
        canvas.drawRect(0f, 0f, progressWidth, height.toFloat(), paint)
    }
    
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = "ProgressIndicator"
        info.contentDescription = "Progress: $progress%"
        info.addAction(
            AccessibilityNodeInfo.AccessibilityAction(
                AccessibilityNodeInfo.ACTION_CLICK,
                "Update progress"
            )
        )
    }
}
