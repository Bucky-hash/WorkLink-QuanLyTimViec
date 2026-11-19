package com.example.worklink.ui.employer

import androidx.fragment.app.Fragment
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.ui.placeholder.PlaceholderFragment

/** Khung chính của Nhà tuyển dụng: Trang chủ · Bài đăng · Ứng viên · Lịch · Hộp thư. */
class EmployerMainActivity : BaseHostActivity() {

    override val role = Role.EMPLOYER

    override fun createFragment(tab: NavTab): Fragment = when (tab.id) {
        // R.id.tab_employer_home -> EmployerHomeFragment()
        // R.id.tab_employer_inbox -> EmployerInboxFragment()
        else -> PlaceholderFragment.newInstance(getString(tab.label))
    }
}
