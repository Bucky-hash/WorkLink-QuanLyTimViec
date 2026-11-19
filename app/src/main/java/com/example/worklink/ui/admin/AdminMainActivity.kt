package com.example.worklink.ui.admin

import androidx.fragment.app.Fragment
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.ui.placeholder.PlaceholderFragment

/** Khung chính của Quản trị: Tổng quan · Người dùng · Duyệt tin · Thông báo · Tài khoản (không có nút chat). */
class AdminMainActivity : BaseHostActivity() {

    override val role = Role.ADMIN

    override fun createFragment(tab: NavTab): Fragment =
        PlaceholderFragment.newInstance(getString(tab.label))
}
