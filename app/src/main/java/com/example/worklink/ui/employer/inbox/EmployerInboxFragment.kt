package com.example.worklink.ui.employer.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.example.worklink.R
import com.example.worklink.core.base.BaseFragment
import com.example.worklink.core.ext.dp

/**
 * Hộp thư Nhà tuyển dụng (Figma: "Nhà tuyển dụng - Hộp thư"):
 * tiêu đề + máy ảnh / cuộc trò chuyện mới, ô tìm kiếm, dải đang hoạt động,
 * bộ lọc Tất cả · Chưa đọc, danh sách cuộc trò chuyện (Woli AI nằm trong danh sách như một dòng).
 *
 * Màn này chỉ hiển thị. Hiện lấy dữ liệu từ [EmployerInboxSampleData]; khi có API/CSDL thì chỉ cần
 * cấp một [EmployerInboxState] thật cho [state], phần giao diện giữ nguyên.
 * Màn chat chi tiết chưa làm nên bấm vào dòng hiện thông báo tạm.
 */
class EmployerInboxFragment : BaseFragment(R.layout.fragment_employer_inbox) {

    private var state: EmployerInboxState = EmployerInboxSampleData.state
    private var filter = InboxFilter.ALL
    private var query = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filter = savedInstanceState?.getInt(KEY_FILTER)
            ?.let { InboxFilter.entries.getOrNull(it) } ?: InboxFilter.ALL

        bindHeader(view)
        bindSearch(view)
        bindActiveContacts(view.findViewById(R.id.active_contacts_container))
        bindFilters(view)
        renderConversations(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_FILTER, filter.ordinal)
    }

    // ------------------------------------------------------------------ Tiêu đề + 2 nút

    private fun bindHeader(root: View) {
        root.findViewById<View>(R.id.action_camera).setOnClickListener {
            toastNotReady(getString(R.string.cd_inbox_camera))
        }
        root.findViewById<View>(R.id.action_new_conversation).setOnClickListener {
            toastNotReady(getString(R.string.cd_inbox_new_conversation))
        }
    }

    // ------------------------------------------------------------------ Tìm kiếm

    private fun bindSearch(root: View) {
        root.findViewById<EditText>(R.id.search_input).apply {
            // Gõ tới đâu lọc tới đó. Văn bản được hệ thống khôi phục sau xoay màn hình cũng đi qua đây.
            doAfterTextChanged {
                query = it?.toString().orEmpty()
                renderConversations(root)
            }
            setOnEditorActionListener { field, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    field.clearFocus()
                    ViewCompat.getWindowInsetsController(field)?.hide(WindowInsetsCompat.Type.ime())
                    true
                } else {
                    false
                }
            }
        }
    }

    // ------------------------------------------------------------------ Dải đang hoạt động

    private fun bindActiveContacts(container: LinearLayout) {
        val inflater = LayoutInflater.from(container.context)
        container.removeAllViews()

        state.conversations.filter { it.isOnline && !it.isAi }.forEachIndexed { index, contact ->
            val item = inflater.inflate(R.layout.item_employer_active_contact, container, false)
            item.findViewById<TextView>(R.id.contact_name).text = contact.name
            item.contentDescription = getString(R.string.employer_inbox_active_description, contact.name)
            bindAvatar(item, contact)
            item.setOnClickListener { openConversation(contact) }
            container.addView(
                item,
                LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    if (index > 0) marginStart = container.context.dp(12)
                }
            )
        }
        // Không ai đang hoạt động thì ẩn cả dải, khỏi chừa khoảng trống.
        (container.parent as View).isVisible = container.childCount > 0
    }

    // ------------------------------------------------------------------ Bộ lọc

    private fun bindFilters(root: View) {
        root.findViewById<TextView>(R.id.filter_unread_count).apply {
            isVisible = state.unreadTotal > 0
            text = if (state.unreadTotal > 99) {
                getString(R.string.employer_unread_overflow)
            } else {
                state.unreadTotal.toString()
            }
        }
        root.findViewById<View>(R.id.filter_all).setOnClickListener { selectFilter(root, InboxFilter.ALL) }
        root.findViewById<View>(R.id.filter_unread).setOnClickListener { selectFilter(root, InboxFilter.UNREAD) }
        updateFilterChips(root)
    }

    private fun selectFilter(root: View, selected: InboxFilter) {
        if (filter == selected) return
        filter = selected
        updateFilterChips(root)
        renderConversations(root)
    }

    private fun updateFilterChips(root: View) {
        root.findViewById<View>(R.id.filter_all).isSelected = filter == InboxFilter.ALL
        root.findViewById<View>(R.id.filter_unread).isSelected = filter == InboxFilter.UNREAD
    }

    // ------------------------------------------------------------------ Danh sách cuộc trò chuyện

    private fun renderConversations(root: View) {
        val container = root.findViewById<LinearLayout>(R.id.conversation_container)
        val empty = root.findViewById<TextView>(R.id.inbox_empty)
        val items = state.conversations.filterBy(filter, query)

        container.removeAllViews()
        empty.isVisible = items.isEmpty()
        empty.setText(emptyMessage())

        val inflater = LayoutInflater.from(container.context)
        items.forEachIndexed { index, conversation ->
            val row = inflater.inflate(R.layout.item_employer_conversation, container, false)
            bindConversation(row, conversation)
            container.addView(
                row,
                LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    if (index > 0) topMargin = container.context.dp(7)
                }
            )
        }
    }

    @StringRes
    private fun emptyMessage(): Int = when {
        query.isNotBlank() -> R.string.employer_inbox_empty_search
        filter == InboxFilter.UNREAD -> R.string.employer_inbox_empty_unread
        else -> R.string.employer_inbox_empty
    }

    private fun bindConversation(row: View, conversation: Conversation) {
        row.findViewById<TextView>(R.id.conversation_name).text = conversation.name

        row.findViewById<TextView>(R.id.conversation_time).apply {
            text = conversation.timeLabel
            isVisible = conversation.timeLabel.isNotEmpty()
            setTextAppearance(
                if (conversation.isUnread) R.style.TextAppearance_Worklink_Employer_InboxTime_Unread
                else R.style.TextAppearance_Worklink_Employer_InboxTime
            )
        }

        row.findViewById<TextView>(R.id.conversation_preview).apply {
            text = conversation.lastMessage
            setTextAppearance(
                if (conversation.isUnread) R.style.TextAppearance_Worklink_Employer_InboxPreview_Unread
                else R.style.TextAppearance_Worklink_Employer_InboxPreview
            )
        }

        row.findViewById<TextView>(R.id.conversation_unread_badge).apply {
            isVisible = conversation.isUnread
            text = if (conversation.unreadCount > 99) {
                getString(R.string.employer_unread_overflow)
            } else {
                conversation.unreadCount.toString()
            }
        }

        // TalkBack đọc một cụm: tên, số tin chưa đọc, rồi tin nhắn cuối.
        row.contentDescription = if (conversation.isUnread) {
            getString(
                R.string.employer_inbox_row_unread_description,
                conversation.name, conversation.unreadCount, conversation.lastMessage
            )
        } else {
            getString(R.string.employer_inbox_row_description, conversation.name, conversation.lastMessage)
        }

        bindAvatar(row, conversation)
        row.setOnClickListener { openConversation(conversation) }
    }

    /** Dùng chung cho dòng cuộc trò chuyện và dải đang hoạt động (cùng id avatar). */
    private fun bindAvatar(item: View, conversation: Conversation) {
        val container = item.findViewById<View>(R.id.avatar_container)
        val icon = item.findViewById<ImageView>(R.id.avatar_icon)

        when {
            conversation.isAi -> {
                container.setBackgroundResource(R.drawable.bg_inbox_ai_avatar)
                icon.imageTintList = null
                icon.setImageResource(R.drawable.ic_emp_ai)
            }
            conversation.avatarRes != null -> {
                // Có ảnh thật thì phủ kín khung, bỏ màu tint của biểu tượng mặc định.
                icon.imageTintList = null
                icon.scaleType = ImageView.ScaleType.CENTER_CROP
                icon.layoutParams = icon.layoutParams.apply { width = MATCH_PARENT; height = MATCH_PARENT }
                icon.setImageResource(conversation.avatarRes)
            }
        }

        item.findViewById<View>(R.id.online_dot)?.isVisible = conversation.isOnline && !conversation.isAi
    }

    private fun openConversation(conversation: Conversation) {
        // Màn chat chi tiết chưa làm. Khi có, mở theo conversation.id.
        toastNotReady(conversation.name)
    }

    private fun toastNotReady(screenName: String) {
        toast(getString(R.string.placeholder_screen, screenName))
    }

    private companion object {
        const val KEY_FILTER = "employer_inbox_filter"
    }
}
