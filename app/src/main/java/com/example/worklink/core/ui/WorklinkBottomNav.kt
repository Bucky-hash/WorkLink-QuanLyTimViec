package com.example.worklink.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList
import com.example.worklink.R
import com.example.worklink.core.nav.NavTab

/**
 * Thanh điều hướng dưới: thẻ trắng bo 24, cao 72, cách mép 16 / 8 (Figma "Bottom Nav").
 * Tab đang chọn: icon + nhãn màu chủ đạo, nhãn SemiBold. Tab còn lại: xám, Medium.
 */
class WorklinkBottomNav @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val tabsContainer: LinearLayout
    private var tabs: List<NavTab> = emptyList()
    private val tabViews = LinkedHashMap<Int, View>()

    var selectedTabId: Int = View.NO_ID
        private set

    var onTabSelected: ((NavTab) -> Unit)? = null
    var onTabReselected: ((NavTab) -> Unit)? = null

    var activeColor: Int = ContextCompat.getColor(context, R.color.wl_primary)
        set(value) { field = value; render() }

    var inactiveColor: Int = ContextCompat.getColor(context, R.color.wl_text_secondary)
        set(value) { field = value; render() }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_nav, this, true)
        tabsContainer = findViewById(R.id.nav_tabs)

        val h = resources.getDimensionPixelSize(R.dimen.wl_nav_outer_padding_h)
        val v = resources.getDimensionPixelSize(R.dimen.wl_nav_outer_padding_v)
        setPadding(h, v, h, v)
        clipChildren = false
        clipToPadding = false

        context.obtainStyledAttributes(attrs, R.styleable.WorklinkBottomNav).use { a ->
            activeColor = a.getColor(R.styleable.WorklinkBottomNav_wlActiveColor, activeColor)
            inactiveColor = a.getColor(R.styleable.WorklinkBottomNav_wlInactiveColor, inactiveColor)
        }
    }

    /** Gán danh sách tab. Không gọi [onTabSelected]; Activity tự hiển thị màn đầu tiên. */
    fun setTabs(newTabs: List<NavTab>, selectedId: Int = newTabs.firstOrNull()?.id ?: View.NO_ID) {
        tabs = newTabs
        selectedTabId = selectedId
        tabViews.clear()
        tabsContainer.removeAllViews()
        val inflater = LayoutInflater.from(context)
        newTabs.forEach { tab ->
            val item = inflater.inflate(R.layout.item_bottom_nav_tab, tabsContainer, false)
            item.findViewById<ImageView>(R.id.nav_icon).setImageResource(tab.icon)
            item.findViewById<TextView>(R.id.nav_label).setText(tab.label)
            item.contentDescription = context.getString(tab.label)
            item.setOnClickListener { select(tab.id) }
            tabsContainer.addView(item)
            tabViews[tab.id] = item
        }
        render()
    }

    /** Chọn tab theo id. Đang ở tab đó rồi thì gọi [onTabReselected] (ví dụ để cuộn lên đầu). */
    fun select(tabId: Int, notify: Boolean = true) {
        val tab = tabs.firstOrNull { it.id == tabId } ?: return
        val changed = tabId != selectedTabId
        selectedTabId = tabId
        render()
        if (!notify) return
        if (changed) onTabSelected?.invoke(tab) else onTabReselected?.invoke(tab)
    }

    private fun render() {
        tabViews.forEach { (id, item) ->
            val active = id == selectedTabId
            val color = if (active) activeColor else inactiveColor
            val icon = item.findViewById<ImageView>(R.id.nav_icon)
            val label = item.findViewById<TextView>(R.id.nav_label)
            ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(color))
            label.setTextAppearance(
                if (active) R.style.TextAppearance_Worklink_TabLabel_Active
                else R.style.TextAppearance_Worklink_TabLabel
            )
            label.setTextColor(color)
            item.isSelected = active
        }
    }
}
