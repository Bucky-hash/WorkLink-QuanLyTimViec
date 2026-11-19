package com.example.worklink.core.nav

import com.example.worklink.R

/** Danh sách tab của từng vai trò, đúng thứ tự và icon trong Figma. */
object RoleTabs {

    fun of(role: Role): List<NavTab> = when (role) {
        Role.CANDIDATE -> listOf(
            NavTab(R.id.tab_candidate_explore, R.drawable.ic_compass, R.string.nav_candidate_explore),
            NavTab(R.id.tab_candidate_saved, R.drawable.ic_bookmark, R.string.nav_candidate_saved),
            NavTab(R.id.tab_candidate_applied, R.drawable.ic_briefcase, R.string.nav_candidate_applied),
            NavTab(R.id.tab_candidate_notifications, R.drawable.ic_bell, R.string.nav_candidate_notifications),
            NavTab(R.id.tab_candidate_account, R.drawable.ic_user, R.string.nav_candidate_account)
        )

        Role.EMPLOYER -> listOf(
            NavTab(R.id.tab_employer_home, R.drawable.ic_compass, R.string.nav_employer_home),
            NavTab(R.id.tab_employer_posts, R.drawable.ic_briefcase, R.string.nav_employer_posts),
            NavTab(R.id.tab_employer_candidates, R.drawable.ic_user, R.string.nav_employer_candidates),
            NavTab(R.id.tab_employer_calendar, R.drawable.ic_calendar, R.string.nav_employer_calendar),
            NavTab(R.id.tab_employer_inbox, R.drawable.ic_mail, R.string.nav_employer_inbox)
        )

        // Figma dùng icon bookmark cho "Người dùng" (nhiều khả năng sao chép từ bản Ứng viên).
        Role.ADMIN -> listOf(
            NavTab(R.id.tab_admin_overview, R.drawable.ic_compass, R.string.nav_admin_overview),
            NavTab(R.id.tab_admin_users, R.drawable.ic_bookmark, R.string.nav_admin_users),
            NavTab(R.id.tab_admin_review, R.drawable.ic_briefcase, R.string.nav_admin_review),
            NavTab(R.id.tab_admin_notifications, R.drawable.ic_bell, R.string.nav_admin_notifications),
            NavTab(R.id.tab_admin_account, R.drawable.ic_user, R.string.nav_admin_account)
        )
    }
}
