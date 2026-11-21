package com.example.worklink.ui.candidate

import androidx.fragment.app.Fragment
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.ui.placeholder.PlaceholderFragment

/** Khung chính của Ứng viên: Khám phá · Đã lưu · Ứng tuyển · Thông báo · Tài khoản. */
class CandidateMainActivity : BaseHostActivity() {

    override val role = Role.CANDIDATE

    override fun createFragment(tab: NavTab): Fragment {
        val label = getString(tab.label)
        return if (label.equals("Khám phá", ignoreCase = true)) {
            CandidateExploreFragment()
        } else {
            PlaceholderFragment.newInstance(label)
        }
    }
}