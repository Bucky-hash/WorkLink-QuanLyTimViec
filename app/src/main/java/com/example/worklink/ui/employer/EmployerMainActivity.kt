package com.example.worklink.ui.employer

import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.ui.placeholder.PlaceholderFragment

/** Khung chính của Nhà tuyển dụng: Trang chủ · Bài đăng · Ứng viên · Lịch · Hộp thư. */
class EmployerMainActivity : BaseHostActivity() {

    override val role = Role.EMPLOYER

    override fun createFragment(tab: NavTab): Fragment {
        val label = getString(tab.label).trim()
        return when {
            label.equals(getString(R.string.employer_interview_title), ignoreCase = true) ||
                    label.equals("Lịch", ignoreCase = true) ||
                    label.contains("Lịch phỏng vấn", ignoreCase = true) -> {
                EmployerInterviewScheduleFragment()
            }
            else -> PlaceholderFragment.newInstance(label)
        }
    }
}