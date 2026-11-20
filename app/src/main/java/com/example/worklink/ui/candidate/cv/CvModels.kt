package com.example.worklink.ui.candidate.cv

import androidx.annotation.StringRes

/** Toàn bộ nội dung màn Tạo CV. Ngày lưu dạng chữ đúng như hiển thị (dd/MM/yyyy hoặc MM/yyyy). */
data class CvDraft(
    val title: String = "",
    val fullName: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val website: String = "",
    val targetPosition: String = "",
    val careerObjective: String = "",
    val avatarUri: String? = null,
    val educations: List<CvEducation> = listOf(CvEducation()),
    val experiences: List<CvExperience> = listOf(CvExperience()),
    val skills: List<String> = emptyList()
)

data class CvEducation(
    val school: String = "",
    val major: String = "",
    val degree: String = "",
    val start: String = "",
    val end: String = "",
    val description: String = ""
)

data class CvExperience(
    val company: String = "",
    val position: String = "",
    val start: String = "",
    /** Rỗng khi [isCurrent] = true (hiển thị "Hiện tại"). */
    val end: String = "",
    val isCurrent: Boolean = false,
    val description: String = ""
)

sealed interface CvResult {
    object Success : CvResult
    data class Failure(@StringRes val messageRes: Int) : CvResult
}

/** Cổng dữ liệu cho màn Tạo CV. Hiện dùng [FakeCvRepository]; khi có backend chỉ cần thay lớp cài đặt. */
interface CvRepository {
    fun initialDraft(): CvDraft
    fun saveDraft(draft: CvDraft): CvResult
    fun saveCv(draft: CvDraft): CvResult
}
