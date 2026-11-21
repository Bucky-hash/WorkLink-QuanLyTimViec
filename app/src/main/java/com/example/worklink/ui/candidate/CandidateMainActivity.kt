package com.example.worklink.ui.candidate

import android.os.Bundle
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
            label.equals(getString(R.string.candidate_nav_account), ignoreCase = true) ||
                    label.contains("Tài khoản", ignoreCase = true) -> {
                CandidateProfileFragment()
            }
            else -> PlaceholderFragment.newInstance(title = label)
        }
    }

    fun completePreferencesAndOpenExplore() {
        hasCompletedPreferences = true
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

    companion object {
        private const val KEY_PREF_COMPLETED = "key_candidate_pref_completed"
    }
}