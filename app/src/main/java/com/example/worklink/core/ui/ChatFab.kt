package com.example.worklink.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.example.worklink.R

/** Nút tròn "Mở tin nhắn" 52x52 màu #6CC51D kèm huy hiệu số tin chưa đọc (Figma). */
class ChatFab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val badge: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_chat_fab, this, true)
        badge = findViewById(R.id.fab_badge)
        clipChildren = false
        clipToPadding = false
    }

    fun setUnreadCount(count: Int) {
        badge.visibility = if (count > 0) View.VISIBLE else View.GONE
        badge.text = if (count > 99) "99+" else count.toString()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        findViewById<View>(R.id.fab_circle).setOnClickListener(l)
    }
}
