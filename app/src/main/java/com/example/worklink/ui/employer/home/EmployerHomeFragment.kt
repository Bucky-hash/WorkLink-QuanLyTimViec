package com.example.worklink.ui.employer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.worklink.R
import com.example.worklink.core.base.BaseFragment
import com.example.worklink.core.ext.dp
import com.example.worklink.ui.employer.EmployerTabNavigator

/**
 * Trang chủ Nhà tuyển dụng (Figma: "Nhà tuyển dụng - Trang chủ"):
 * lời chào + chuông, 4 số liệu tuyển dụng, 8 lối tắt, 3 hồ sơ mới nhất.
 *
 * Màn này chỉ hiển thị. Hiện lấy dữ liệu từ [EmployerHomeSampleData]; khi có API/CSDL thì chỉ cần cấp
 * một [EmployerHomeState] thật cho [render], phần giao diện giữ nguyên.
 */
class EmployerHomeFragment : BaseFragment(R.layout.fragment_employer_home) {

    private val tabNavigator get() = activity as? EmployerTabNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render(view, EmployerHomeSampleData.state)
    }

    private fun render(root: View, state: EmployerHomeState) {
        bindHeader(root, state)
        bindSummary(root.findViewById(R.id.summary_container), state.summary)
        bindShortcuts(root.findViewById(R.id.shortcut_container))
        bindApplicants(root, state.recentApplicants)
    }

    // ------------------------------------------------------------------ Lời chào + chuông

    private fun bindHeader(root: View, state: EmployerHomeState) {
        root.findViewById<TextView>(R.id.company_name).text = state.companyName

        val badge = root.findViewById<TextView>(R.id.notification_badge)
        badge.isVisible = state.unreadNotifications > 0
        badge.text = if (state.unreadNotifications > 99) {
            getString(R.string.employer_unread_overflow)
        } else {
            state.unreadNotifications.toString()
        }

        root.findViewById<View>(R.id.company_avatar).setOnClickListener {
            onShortcutClick(EmployerShortcut.COMPANY_PROFILE)
        }
        root.findViewById<View>(R.id.notification_button).setOnClickListener {
            toastNotReady(getString(R.string.cd_notifications))
        }
    }

    // ------------------------------------------------------------------ 4 số liệu (lưới 2 x 2)

    private fun bindSummary(container: LinearLayout, summary: RecruitmentSummary) {
        val inflater = LayoutInflater.from(container.context)
        val metrics = listOf(
            SummaryMetric(summary.activePosts, R.string.employer_home_metric_active_posts, R.drawable.ic_emp_stat_posts),
            SummaryMetric(summary.newApplications, R.string.employer_home_metric_new_applications, R.drawable.ic_emp_stat_new_cv),
            SummaryMetric(summary.interviewsToday, R.string.employer_home_metric_interviews_today, R.drawable.ic_emp_stat_interview),
            SummaryMetric(summary.expiringPosts, R.string.employer_home_metric_expiring_posts, R.drawable.ic_emp_stat_expiring)
        )
        val cards = metrics.map { metric ->
            inflater.inflate(R.layout.item_employer_summary, container, false).apply {
                findViewById<TextView>(R.id.metric_value).text =
                    getString(R.string.employer_home_metric_value, metric.value)
                findViewById<TextView>(R.id.metric_label).setText(metric.label)
                findViewById<ImageView>(R.id.metric_icon).setImageResource(metric.icon)
            }
        }
        fillGrid(container, cards, columns = 2, columnGapDp = 8, rowGapDp = 8)
    }

    private class SummaryMetric(
        val value: Int,
        @StringRes val label: Int,
        @DrawableRes val icon: Int
    )

    // ------------------------------------------------------------------ 8 lối tắt (lưới 4 x 2)

    private fun bindShortcuts(container: LinearLayout) {
        val inflater = LayoutInflater.from(container.context)
        val cells = EmployerShortcut.entries.map { shortcut ->
            inflater.inflate(R.layout.item_employer_shortcut, container, false).apply {
                findViewById<ImageView>(R.id.shortcut_icon).setImageResource(shortcut.icon)
                findViewById<TextView>(R.id.shortcut_label).setText(shortcut.label)
                // Nhãn có xuống dòng "\n"; TalkBack đọc liền một cụm cho tự nhiên.
                contentDescription = getString(shortcut.label).replace('\n', ' ')
                setOnClickListener { onShortcutClick(shortcut) }
            }
        }
        fillGrid(container, cells, columns = 4, columnGapDp = 4, rowGapDp = 8)
    }

    private fun onShortcutClick(shortcut: EmployerShortcut) {
        val tabId = shortcut.targetTabId
        if (tabId != null) {
            tabNavigator?.openEmployerTab(tabId)
        } else {
            toastNotReady(getString(shortcut.label).replace('\n', ' '))
        }
    }

    // ------------------------------------------------------------------ Hồ sơ mới nhất

    private fun bindApplicants(root: View, applicants: List<RecentApplicant>) {
        root.findViewById<View>(R.id.view_all_applicants).setOnClickListener {
            tabNavigator?.openEmployerTab(R.id.tab_employer_candidates)
        }

        val container = root.findViewById<LinearLayout>(R.id.applicant_container)
        val inflater = LayoutInflater.from(container.context)
        container.removeAllViews()

        applicants.forEachIndexed { index, applicant ->
            val row = inflater.inflate(R.layout.item_employer_applicant, container, false)
            bindApplicant(row, applicant)
            container.addView(
                row,
                LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    if (index > 0) topMargin = container.context.dp(7)
                }
            )
        }
    }

    private fun bindApplicant(row: View, applicant: RecentApplicant) {
        val context = row.context

        row.findViewById<TextView>(R.id.applicant_name).text = applicant.name
        row.findViewById<TextView>(R.id.applicant_meta).text = getString(
            R.string.employer_applicant_meta,
            applicant.position,
            applicant.experienceYears
        )

        row.findViewById<TextView>(R.id.applicant_status).apply {
            setText(applicant.status.label)
            setBackgroundResource(applicant.status.background)
            setTextColor(ContextCompat.getColor(context, applicant.status.textColor))
        }

        // Có ảnh thật thì phủ kín khung, bỏ đệm và màu tint của biểu tượng mặc định.
        applicant.avatarRes?.let { res ->
            row.findViewById<ImageView>(R.id.applicant_avatar).apply {
                setPadding(0, 0, 0, 0)
                imageTintList = null
                setImageResource(res)
            }
        }

        row.findViewById<View>(R.id.action_view_profile).setOnClickListener {
            // Màn "Chi tiết ứng viên" chưa làm. Khi có, mở theo applicant.id.
            toastNotReady(getString(R.string.employer_applicant_action_view_profile))
        }
        row.findViewById<View>(R.id.action_message).setOnClickListener {
            tabNavigator?.openEmployerTab(R.id.tab_employer_inbox)
        }
    }

    // ------------------------------------------------------------------ Tiện ích

    /**
     * Xếp [cells] thành lưới [columns] cột bằng các hàng LinearLayout, mỗi ô chia đều chiều rộng.
     * Hàng cuối thiếu ô thì chèn ô trống để các ô còn lại không bị kéo giãn.
     */
    private fun fillGrid(
        container: LinearLayout,
        cells: List<View>,
        columns: Int,
        columnGapDp: Int,
        rowGapDp: Int
    ) {
        val context = container.context
        container.removeAllViews()

        cells.chunked(columns).forEachIndexed { rowIndex, rowCells ->
            val row = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }

            rowCells.forEachIndexed { column, cell ->
                row.addView(cell, LinearLayout.LayoutParams(0, MATCH_PARENT, 1f).apply {
                    if (column > 0) marginStart = context.dp(columnGapDp)
                })
            }
            repeat(columns - rowCells.size) {
                row.addView(Space(context), LinearLayout.LayoutParams(0, 0, 1f).apply {
                    marginStart = context.dp(columnGapDp)
                })
            }

            container.addView(row, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                if (rowIndex > 0) topMargin = context.dp(rowGapDp)
            })
        }
    }

    private fun toastNotReady(screenName: String) {
        toast(getString(R.string.placeholder_screen, screenName))
    }
}
