package com.example.worklink.core.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.example.worklink.R
import com.google.android.material.imageview.ShapeableImageView

/** Header trang chủ: avatar tròn + "Xin chào, <tên>." + chuông thông báo có chấm xanh (Figma). */
class WorklinkHomeHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val avatar: ShapeableImageView
    private val greeting: TextView
    private val bellDot: View

    var onAvatarClick: (() -> Unit)? = null
    var onNotificationClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_home_header, this, true)
        avatar = findViewById(R.id.header_avatar)
        greeting = findViewById(R.id.header_greeting)
        bellDot = findViewById(R.id.header_bell_dot)
        avatar.setOnClickListener { onAvatarClick?.invoke() }
        findViewById<View>(R.id.header_bell).setOnClickListener { onNotificationClick?.invoke() }
    }

    fun setUserName(name: String) {
        greeting.text = context.getString(R.string.home_greeting, name)
    }

    fun setNotificationDotVisible(visible: Boolean) {
        bellDot.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setAvatar(@DrawableRes resId: Int) = applyAvatar { avatar.setImageResource(resId) }

    fun setAvatar(drawable: Drawable?) = applyAvatar { avatar.setImageDrawable(drawable) }

    fun setAvatar(bitmap: Bitmap?) = applyAvatar { avatar.setImageBitmap(bitmap) }

    /** Ảnh thật phủ kín khung, bỏ đệm và màu tint của ảnh mặc định. */
    private fun applyAvatar(set: () -> Unit) {
        avatar.setPadding(0, 0, 0, 0)
        avatar.imageTintList = null
        set()
    }
}
