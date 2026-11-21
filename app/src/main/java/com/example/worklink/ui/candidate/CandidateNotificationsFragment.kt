package com.example.worklink.ui.candidate

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.worklink.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

enum class CandidateNotificationGroup {
    TODAY,
    YESTERDAY
}

data class CandidateNotificationItem(
    val id: String,
    val title: String,
    val body: String,
    val timeText: String,
    val group: CandidateNotificationGroup,
    var isUnread: Boolean
)

class CandidateNotificationsFragment : Fragment() {

    private var tvUnreadCount: TextView? = null
    private var btnMarkAllRead: TextView? = null
    private var chipAll: Chip? = null
    private var chipUnread: Chip? = null
    private var tvEmpty: TextView? = null
    private var rvNotifications: RecyclerView? = null

    private val notifications = mutableListOf<CandidateNotificationItem>()
    private var onlyUnread = false
    private lateinit var adapter: CandidateNotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_candidate_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        initMockNotifications()
        setupRecyclerView()
        bindFiltersAndActions()
        refreshUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvUnreadCount = null
        btnMarkAllRead = null
        chipAll = null
        chipUnread = null
        tvEmpty = null
        rvNotifications = null
    }

    private fun bindViews(root: View) {
        tvUnreadCount = root.findViewById(R.id.tv_candidate_notif_unread_count)
        btnMarkAllRead = root.findViewById(R.id.btn_candidate_notif_mark_all_read)
        chipAll = root.findViewById(R.id.chip_candidate_notif_all)
        chipUnread = root.findViewById(R.id.chip_candidate_notif_unread)
        tvEmpty = root.findViewById(R.id.tv_candidate_notif_empty)
        rvNotifications = root.findViewById(R.id.rv_candidate_notif_list)
    }

    private fun initMockNotifications() {
        if (notifications.isNotEmpty()) return
        notifications.addAll(
            listOf(
                CandidateNotificationItem(
                    id = "n1",
                    title = getString(R.string.candidate_notif_item1_title),
                    body = getString(R.string.candidate_notif_item1_body),
                    timeText = getString(R.string.candidate_notif_item1_time),
                    group = CandidateNotificationGroup.TODAY,
                    isUnread = true
                ),
                CandidateNotificationItem(
                    id = "n2",
                    title = getString(R.string.candidate_notif_item2_title),
                    body = getString(R.string.candidate_notif_item2_body),
                    timeText = getString(R.string.candidate_notif_item2_time),
                    group = CandidateNotificationGroup.TODAY,
                    isUnread = true
                ),
                CandidateNotificationItem(
                    id = "n3",
                    title = getString(R.string.candidate_notif_item3_title),
                    body = getString(R.string.candidate_notif_item3_body),
                    timeText = getString(R.string.candidate_notif_item3_time),
                    group = CandidateNotificationGroup.TODAY,
                    isUnread = true
                ),
                CandidateNotificationItem(
                    id = "n4",
                    title = getString(R.string.candidate_notif_item4_title),
                    body = getString(R.string.candidate_notif_item4_body),
                    timeText = getString(R.string.candidate_notif_item4_time),
                    group = CandidateNotificationGroup.YESTERDAY,
                    isUnread = false
                )
            )
        )
    }

    private fun setupRecyclerView() {
        adapter = CandidateNotificationAdapter { clicked ->
            if (clicked.isUnread) {
                clicked.isUnread = false
                refreshUi()
            }
        }
        rvNotifications?.layoutManager = LinearLayoutManager(requireContext())
        rvNotifications?.adapter = adapter
    }

    private fun bindFiltersAndActions() {
        chipAll?.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                onlyUnread = false
                refreshUi()
            }
        }

        chipUnread?.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                onlyUnread = true
                refreshUi()
            }
        }

        btnMarkAllRead?.setOnClickListener {
            notifications.forEach { it.isUnread = false }
            refreshUi()
            Toast.makeText(
                requireContext(),
                getString(R.string.candidate_notif_marked_all_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshUi() {
        val unreadCount = notifications.count { it.isUnread }
        tvUnreadCount?.text = getString(R.string.candidate_notif_unread_summary, unreadCount)
        chipUnread?.text = getString(R.string.candidate_notif_tab_unread, unreadCount)

        val filtered = if (onlyUnread) {
            notifications.filter { it.isUnread }
        } else {
            notifications.toList()
        }

        tvEmpty?.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        rvNotifications?.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE

        val rows = buildDisplayRows(filtered)
        adapter.submitRows(rows)
    }

    private fun buildDisplayRows(items: List<CandidateNotificationItem>): List<NotificationDisplayRow> {
        val rows = mutableListOf<NotificationDisplayRow>()
        var lastGroup: CandidateNotificationGroup? = null

        for (item in items) {
            val showHeader = item.group != lastGroup
            lastGroup = item.group
            val headerLabel = when (item.group) {
                CandidateNotificationGroup.TODAY -> getString(R.string.candidate_notif_group_today)
                CandidateNotificationGroup.YESTERDAY -> getString(R.string.candidate_notif_group_yesterday)
            }
            rows.add(
                NotificationDisplayRow(
                    item = item,
                    headerTitle = if (showHeader) headerLabel else null
                )
            )
        }
        return rows
    }
}

private data class NotificationDisplayRow(
    val item: CandidateNotificationItem,
    val headerTitle: String?
)

private class CandidateNotificationAdapter(
    private val onItemClick: (CandidateNotificationItem) -> Unit
) : RecyclerView.Adapter<CandidateNotificationAdapter.NotificationViewHolder>() {

    private val rows = mutableListOf<NotificationDisplayRow>()

    fun submitRows(newRows: List<NotificationDisplayRow>) {
        rows.clear()
        rows.addAll(newRows)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_candidate_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(rows[position], onItemClick)
    }

    override fun getItemCount(): Int = rows.size

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvGroupHeader: TextView = itemView.findViewById(R.id.tv_candidate_notif_group_header)
        private val cardItem: MaterialCardView = itemView.findViewById(R.id.card_candidate_notif_item)
        private val tvDot: TextView = itemView.findViewById(R.id.tv_candidate_notif_dot)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_candidate_notif_item_title)
        private val tvBody: TextView = itemView.findViewById(R.id.tv_candidate_notif_item_body)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_candidate_notif_item_time)
        private val tvStatus: TextView = itemView.findViewById(R.id.tv_candidate_notif_item_status)

        fun bind(row: NotificationDisplayRow, onItemClick: (CandidateNotificationItem) -> Unit) {
            val context = itemView.context
            val item = row.item

            if (row.headerTitle != null) {
                tvGroupHeader.visibility = View.VISIBLE
                tvGroupHeader.text = row.headerTitle
            } else {
                tvGroupHeader.visibility = View.GONE
            }

            tvTitle.text = item.title
            tvBody.text = item.body
            tvTime.text = item.timeText

            if (item.isUnread) {
                tvDot.visibility = View.VISIBLE
                tvStatus.text = context.getString(R.string.candidate_notif_status_unread)
                tvStatus.setTextColor(Color.parseColor("#16A34A"))
                cardItem.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                tvDot.visibility = View.INVISIBLE
                tvStatus.text = context.getString(R.string.candidate_notif_status_read)
                tvStatus.setTextColor(Color.parseColor("#6B7280"))
                cardItem.setCardBackgroundColor(Color.parseColor("#F9FAFB"))
            }

            cardItem.setOnClickListener { onItemClick(item) }
        }
    }
}