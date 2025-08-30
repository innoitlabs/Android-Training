package com.example.accessibilityfeatures

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.appcompat.widget.AppCompatButton

class AccessibleActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    
    private var onLongClickListener: OnLongClickListener? = null
    private var customActionListener: OnCustomActionListener? = null
    
    init {
        setupAccessibility()
    }
    
    private fun setupAccessibility() {
        accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfo
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                
                // Add custom long press action
                info.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        AccessibilityNodeInfo.ACTION_LONG_CLICK,
                        "Long press to show options"
                    )
                )
                
                // Add custom action
                info.addAction(
                    AccessibilityNodeInfo.AccessibilityAction(
                        R.id.accessibilityActionCustom,
                        "Custom action"
                    )
                )
                
                // Customize the announcement
                val actionText = if (text.isNotEmpty()) {
                    "Button, $text. Double tap to activate, long press for options, swipe up for custom action"
                } else {
                    "Button. Double tap to activate, long press for options, swipe up for custom action"
                }
                info.text = actionText
            }
        }
    }
    
    override fun setOnLongClickListener(listener: OnLongClickListener?) {
        super.setOnLongClickListener(listener)
        onLongClickListener = listener
    }
    
    fun setOnCustomActionListener(listener: OnCustomActionListener?) {
        customActionListener = listener
    }
    
    override fun performAccessibilityAction(action: Int, arguments: Bundle?): Boolean {
        return when (action) {
            AccessibilityNodeInfo.ACTION_LONG_CLICK -> {
                onLongClickListener?.onLongClick(this) ?: false
            }
            R.id.accessibilityActionCustom -> {
                customActionListener?.onCustomAction(this)
                true
            }
            else -> super.performAccessibilityAction(action, arguments)
        }
    }
    
    interface OnCustomActionListener {
        fun onCustomAction(view: View)
    }
}
