package com.example.worklink.core.base

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.core.nav.RoleTabs
import com.example.worklink.core.ui.ChatFab
import com.example.worklink.core.ui.WorklinkBottomNav

/**
 * Khung chính của mỗi vai trò: vùng nội dung + nút chat + thanh điều hướng dưới.
 * Mỗi tab là một Fragment, được giữ lại khi chuyển tab (ẩn/hiện, không tạo lại).
 *
 * Cách dùng: kế thừa, khai báo [role] và trả về Fragment cho từng tab trong [createFragment].
 */
abstract class BaseHostActivity : BaseActivity() {

    protected abstract val role: Role

    protected abstract fun createFragment(tab: NavTab): Fragment

    /** Bấm nút chat nổi. Mặc định chưa làm gì. */
    protected open fun onChatClick() = Unit

    protected lateinit var bottomNav: WorklinkBottomNav
        private set
    protected lateinit var chatFab: ChatFab
        private set

    private var currentTabId = View.NO_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_host)

        bottomNav = findViewById(R.id.bottom_nav)
        chatFab = findViewById(R.id.chat_fab)

        val tabs = RoleTabs.of(role)
        currentTabId = savedInstanceState?.getInt(KEY_TAB_ID, tabs.first().id) ?: tabs.first().id

        bottomNav.activeColor = ContextCompat.getColor(this, role.navActiveColor)
        bottomNav.inactiveColor = ContextCompat.getColor(this, role.navInactiveColor)
        bottomNav.setTabs(tabs, currentTabId)
        bottomNav.onTabSelected = { showTab(it) }

        chatFab.isVisible = role.hasChatFab
        chatFab.setOnClickListener { onChatClick() }

        if (savedInstanceState == null) showTab(tabs.first())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TAB_ID, currentTabId)
    }

    /** Chuyển tab bằng code (ví dụ sau khi đăng tin xong thì về tab Bài đăng). */
    protected fun selectTab(tabId: Int) = bottomNav.select(tabId)

    private fun showTab(tab: NavTab) {
        val fm = supportFragmentManager
        val target = fm.findFragmentByTag(tagOf(tab.id)) ?: createFragment(tab)
        val transaction = fm.beginTransaction().setReorderingAllowed(true)
        fm.fragments
            .filter { it.isAdded && it !== target && it.tag?.startsWith(TAG_PREFIX) == true }
            .forEach { transaction.hide(it) }
        if (target.isAdded) {
            transaction.show(target)
        } else {
            transaction.add(R.id.host_container, target, tagOf(tab.id))
        }
        transaction.commit()
        currentTabId = tab.id
    }

    private fun tagOf(tabId: Int) = "$TAG_PREFIX$tabId"

    private companion object {
        const val KEY_TAB_ID = "worklink.host.tab_id"
        const val TAG_PREFIX = "worklink_tab_"
    }
}
