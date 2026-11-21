package com.example.worklink.ui.candidate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.data.mock.CandidateJobMockData
import com.example.worklink.ui.placeholder.PlaceholderFragment

class CandidateMainActivity : BaseHostActivity() {

    override val role = Role.CANDIDATE

    private var hasCompletedPreferences = false

    override fun onCreate(savedInstanceState: Bundle?) {
        hasCompletedPreferences = savedInstanceState?.getBoolean(KEY_PREF_COMPLETED, false) ?: false
        super.onCreate(savedInstanceState)
        updateHostChromeVisibility(showChrome = hasCompletedPreferences)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        updateHostChromeVisibility(showChrome = hasCompletedPreferences)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_PREF_COMPLETED, hasCompletedPreferences)
    }

    override fun createFragment(tab: NavTab): Fragment {
        val label = getString(tab.label).trim()
        val isExploreTab = label.equals(getString(R.string.candidate_nav_explore), ignoreCase = true) ||
                label.contains("Khám phá", ignoreCase = true)

        if (!hasCompletedPreferences && isExploreTab) {
            return CandidateJobPreferencesFragment()
        }

        return when {
            isExploreTab -> CandidateExploreFragment()
            label.equals(getString(R.string.candidate_nav_saved), ignoreCase = true) ||
                    label.contains("Đã lưu", ignoreCase = true) -> {
                CandidateSavedJobsFragment()
            }
            label.equals(getString(R.string.candidate_nav_applied), ignoreCase = true) ||
                    label.contains("Ứng tuyển", ignoreCase = true) -> {
                val defaultJob = CandidateJobMockData.getRecommendedJobs().first()
                CandidateJobDetailFragment.newInstance(defaultJob)
            }
            label.equals(getString(R.string.candidate_nav_notifications), ignoreCase = true) ||
                    label.contains("Thông báo", ignoreCase = true) -> {
                CandidateNotificationsFragment()
            }
            label.equals(getString(R.string.candidate_nav_account), ignoreCase = true) ||
                    label.contains("Tài khoản", ignoreCase = true) -> {
                CandidateProfileFragment()
            }
            else -> PlaceholderFragment.newInstance(title = label)
        }
    }

    fun completePreferencesAndOpenExplore() {
        hasCompletedPreferences = true
        updateHostChromeVisibility(showChrome = true)

        val prefFragment = supportFragmentManager.fragments.firstOrNull {
            it is CandidateJobPreferencesFragment
        }
        if (prefFragment != null) {
            val containerId = prefFragment.id
            val tabTag = prefFragment.tag
            supportFragmentManager.beginTransaction()
                .replace(containerId, CandidateExploreFragment(), tabTag)
                .commitNow()
        }
    }

    private fun updateHostChromeVisibility(showChrome: Boolean) {
        val rootView = findViewById<ViewGroup>(android.R.id.content) ?: return
        val visibility = if (showChrome) View.VISIBLE else View.GONE
        toggleChromeViews(rootView, visibility)
    }

    private fun toggleChromeViews(view: View, visibility: Int) {
        val className = view.javaClass.simpleName
        if (className == "WorklinkBottomNav" || className == "ChatFab") {
            view.visibility = visibility
            return
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                toggleChromeViews(view.getChildAt(i), visibility)
            }
        }
    }

    companion object {
        private const val KEY_PREF_COMPLETED = "key_candidate_pref_completed"
    }
}