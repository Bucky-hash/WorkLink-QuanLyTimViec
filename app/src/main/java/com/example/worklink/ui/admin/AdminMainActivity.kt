package com.example.worklink.ui.admin

import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.ui.placeholder.PlaceholderFragment

/**
 * Khung chính của Quản trị.
 */
class AdminMainActivity : BaseHostActivity() {

    override val role = Role.ADMIN

    override fun createFragment(tab: NavTab): Fragment {
        return when (tab.id) {

            R.id.tab_admin_overview ->
                AdminDashboardFragment()

            R.id.tab_admin_users ->
                UserManagementFragment()

            R.id.tab_admin_review ->
                JobApprovalFragment()

            else ->
                PlaceholderFragment.newInstance(
                    getString(tab.label)
                )
        }
    }
}
