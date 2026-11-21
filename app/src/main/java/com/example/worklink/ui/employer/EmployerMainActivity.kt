package com.example.worklink.ui.employer

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.ui.employer.home.EmployerHomeFragment
import com.example.worklink.ui.employer.home.EmployerHomeSampleData
import com.example.worklink.ui.employer.inbox.EmployerInboxFragment
import com.example.worklink.ui.placeholder.PlaceholderFragment

/** Khung chính của Nhà tuyển dụng: Trang chủ · Bài đăng · Ứng viên · Lịch · Hộp thư. */
class EmployerMainActivity : BaseHostActivity(), EmployerTabNavigator {

    override val role = Role.EMPLOYER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Huy hiệu số tin chưa đọc trên nút chat nổi. Tạm lấy từ dữ liệu mẫu, thay bằng nguồn thật sau.
        chatFab.setUnreadCount(EmployerHomeSampleData.state.unreadMessages)
    }

    override fun createFragment(tab: NavTab): Fragment = when (tab.id) {
        R.id.tab_employer_home -> EmployerHomeFragment()
        R.id.tab_employer_inbox -> EmployerInboxFragment()
        else -> PlaceholderFragment.newInstance(getString(tab.label))
    }

    override fun openEmployerTab(tabId: Int) = selectTab(tabId)
}
