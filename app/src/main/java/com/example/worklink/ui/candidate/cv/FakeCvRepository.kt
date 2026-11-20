package com.example.worklink.ui.candidate.cv

/**
 * DỮ LIỆU GIẢ (chưa có backend): nội dung mẫu lấy từ Figma "Ứng viên - Tạo CV".
 * Muốn mở màn với ô trống thì trả về CvDraft() trong [initialDraft].
 * Lưu nháp / Lưu CV chỉ trả về thành công, không ghi đâu cả.
 */
class FakeCvRepository : CvRepository {

    override fun initialDraft(): CvDraft = CvDraft(
        title = "CV Lập trình viên Frontend",
        fullName = "Trương Văn Chiến",
        birthDate = "15/08/2000",
        gender = "Nam",
        email = "chien@example.com",
        phone = "0912 345 678",
        address = "Cầu Giấy, Hà Nội",
        website = "github.com/chien-dev",
        targetPosition = "Lập trình viên Frontend",
        careerObjective = "Phát triển giao diện web thân thiện, tối ưu trải nghiệm người dùng. " +
            "Mong muốn nâng cao kỹ năng React và đóng góp vào các sản phẩm có giá trị.",
        educations = listOf(
            CvEducation(
                school = "Đại học Công nghệ – ĐHQGHN",
                major = "Công nghệ thông tin",
                degree = "Cử nhân",
                start = "09/2018",
                end = "06/2022",
                description = "GPA: 3.5/4.0. Đồ án tốt nghiệp: xây dựng website tìm kiếm việc làm."
            )
        ),
        experiences = listOf(
            CvExperience(
                company = "Công ty TNHH Công nghệ ABC",
                position = "Lập trình viên Frontend",
                start = "07/2022",
                isCurrent = true,
                description = "• Phát triển giao diện web với ReactJS.\n" +
                    "• Phối hợp với nhóm thiết kế và backend.\n" +
                    "• Tối ưu tốc độ tải trang và hiển thị trên thiết bị di động."
            )
        ),
        skills = listOf("HTML / CSS", "JavaScript", "ReactJS", "Git", "Làm việc nhóm")
    )

    override fun saveDraft(draft: CvDraft): CvResult = CvResult.Success

    override fun saveCv(draft: CvDraft): CvResult = CvResult.Success
}
