package com.example.worklink.ui.employer.home

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.example.worklink.R

/** Toàn bộ dữ liệu màn Trang chủ Nhà tuyển dụng. Sau này chỉ cần thay nguồn dữ liệu, giao diện không đổi. */
data class EmployerHomeState(
    val companyName: String,
    val unreadNotifications: Int,
    val unreadMessages: Int,
    val summary: RecruitmentSummary,
    val recentApplicants: List<RecentApplicant>
)

/** Bốn số liệu ở đầu trang. */
data class RecruitmentSummary(
    val activePosts: Int,
    val newApplications: Int,
    val interviewsToday: Int,
    val expiringPosts: Int
)

data class RecentApplicant(
    val id: Long,
    val name: String,
    val position: String,
    val experienceYears: Int,
    val status: ApplicantStatus,
    /** Ảnh có sẵn trong app. null thì hiện biểu tượng người mặc định. */
    @DrawableRes val avatarRes: Int? = null
)

/** Trạng thái xử lý hồ sơ và kiểu nhãn tương ứng (màu theo Figma). */
enum class ApplicantStatus(
    @StringRes val label: Int,
    @DrawableRes val background: Int,
    @ColorRes val textColor: Int
) {
    NEW(R.string.employer_applicant_status_new, R.drawable.bg_status_badge_new, R.color.wl_status_new_text),
    CV_APPROVED(R.string.employer_applicant_status_cv_approved, R.drawable.bg_status_badge_positive, R.color.wl_primary_dark),
    INTERVIEW(R.string.employer_applicant_status_interview, R.drawable.bg_status_badge_positive, R.color.wl_primary_dark)
}

/**
 * Tám lối tắt trong "Tiện ích tuyển dụng", đúng thứ tự Figma.
 * [targetTabId] là tab sẽ chuyển tới; null nghĩa là màn đó chưa làm (hiện thông báo tạm).
 */
enum class EmployerShortcut(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    @IdRes val targetTabId: Int? = null
) {
    POST_JOB(R.drawable.ic_emp_sc_post_job, R.string.employer_shortcut_post_job),
    MANAGE_POSTS(R.drawable.ic_emp_sc_manage_posts, R.string.employer_shortcut_manage_posts, R.id.tab_employer_posts),
    MANAGE_CANDIDATES(R.drawable.ic_emp_sc_candidates, R.string.employer_shortcut_manage_candidates, R.id.tab_employer_candidates),
    INTERVIEWS(R.drawable.ic_emp_sc_calendar, R.string.employer_shortcut_interviews, R.id.tab_employer_calendar),
    COMPANY_PROFILE(R.drawable.ic_emp_sc_company, R.string.employer_shortcut_company_profile),
    MESSAGES(R.drawable.ic_emp_sc_messages, R.string.employer_shortcut_messages, R.id.tab_employer_inbox),
    STATISTICS(R.drawable.ic_emp_sc_stats, R.string.employer_shortcut_statistics),
    ACCOUNT_SETTINGS(R.drawable.ic_emp_sc_settings, R.string.employer_shortcut_account_settings)
}

/** DỮ LIỆU MẪU lấy từ Figma. Xóa khi nối API/CSDL thật. */
object EmployerHomeSampleData {
    val state = EmployerHomeState(
        companyName = "Công ty ABC",
        unreadNotifications = 3,
        unreadMessages = 3,
        summary = RecruitmentSummary(
            activePosts = 2,
            newApplications = 8,
            interviewsToday = 3,
            expiringPosts = 1
        ),
        recentApplicants = listOf(
            RecentApplicant(1, "Nguyễn Thảo Linh", "Lập trình Flutter", 3, ApplicantStatus.NEW, R.drawable.img_sample_applicant_1),
            RecentApplicant(2, "Trần Minh Tuấn", "Lập trình di động", 4, ApplicantStatus.CV_APPROVED, R.drawable.img_sample_applicant_2),
            RecentApplicant(3, "Lê Hoàng Nam", "Lập trình Flutter", 2, ApplicantStatus.INTERVIEW, R.drawable.img_sample_applicant_3)
        )
    )
}
