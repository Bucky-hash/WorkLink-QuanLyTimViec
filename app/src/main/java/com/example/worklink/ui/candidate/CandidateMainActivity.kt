package com.example.worklink.ui.candidate

import androidx.fragment.app.Fragment
import com.example.worklink.R
import com.example.worklink.core.base.BaseHostActivity
import com.example.worklink.core.nav.NavTab
import com.example.worklink.core.nav.Role
import com.example.worklink.data.mock.CandidateJobMockData
import com.example.worklink.ui.placeholder.PlaceholderFragment

class CandidateMainActivity : BaseHostActivity() {

    override val role = Role.CANDIDATE

    override fun createFragment(tab: NavTab): Fragment {
        val label = getString(tab.label).trim()
        return when {
            label.equals(getString(R.string.candidate_nav_explore), ignoreCase = true) ||
                    label.contains("Khám phá", ignoreCase = true) -> {
                CandidateExploreFragment()
            }
            label.equals(getString(R.string.candidate_nav_applied), ignoreCase = true) ||
                    label.contains("Ứng tuyển", ignoreCase = true) -> {
                val defaultJob = CandidateJobMockData.getRecommendedJobs().first()
                CandidateJobDetailFragment.newInstance(defaultJob)
            }
            else -> PlaceholderFragment.newInstance(title = label)
        }
    }
}