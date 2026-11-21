package com.example.worklink.data.mock

data class CandidateJobItem(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val postedTime: String,
    val source: String,
    val category: String,
    val isRemote: Boolean,
    val aboutCompany: String,
    val requirements: String,
    var isSaved: Boolean = false
)

object CandidateJobMockData {

    fun getRecommendedJobs(): List<CandidateJobItem> = listOf(
        CandidateJobItem(
            id = "REC_01",
            title = "Nhà thiết kế UX",
            company = "FPT Software",
            location = "Hà Nội (Làm việc từ xa)",
            postedTime = "Đăng 9 giờ trước",
            source = "LinkedIn",
            category = "Lập trình",
            isRemote = true,
            aboutCompany = "Điều gì đoàn kết tất cả các thành viên FPT Software trên các đội và khu vực là chúng tôi đều nỗ lực mang lại niềm vui cho khách hàng và làm cho cuộc sống của họ dễ dàng hơn. Sứ mệnh của chúng tôi thúc đẩy chúng tôi tìm kiếm những quan điểm đa dạng.",
            requirements = "• Thiết kế wireframe, prototype trên Figma cho ứng dụng di động.\n• Phối hợp cùng đội ngũ lập trình Android/iOS để tối ưu trải nghiệm người dùng.\n• Có kinh nghiệm nghiên cứu UX và xây dựng Design System."
        ),
        CandidateJobItem(
            id = "REC_02",
            title = "Kế toán tổng hợp",
            company = "Viettel",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "5 ngày trước",
            source = "Monster",
            category = "Kế toán",
            isRemote = true,
            aboutCompany = "Tập đoàn Công nghiệp - Viễn thông Quân đội Viettel tiên phong kiến tạo xã hội số tại Việt Nam với môi trường làm việc chuyên nghiệp, minh bạch và sáng tạo.",
            requirements = "• Tốt nghiệp chuyên ngành Kế toán, Kiểm toán hoặc Tài chính.\n• Thành thạo phần mềm kế toán MISA và Excel nâng cao.\n• Cẩn thận, trung thực và có tinh thần trách nhiệm cao."
        ),
        CandidateJobItem(
            id = "REC_03",
            title = "Lập trình viên Android",
            company = "FPT Software",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "2 ngày trước",
            source = "LinkedIn",
            category = "Lập trình",
            isRemote = true,
            aboutCompany = "FPT Software Đà Nẵng quy tụ hàng nghìn kỹ sư phần mềm phát triển các sản phẩm công nghệ cho khách hàng toàn cầu.",
            requirements = "• Thành thạo Kotlin, Android SDK, MVVM và REST API.\n• Có kinh nghiệm làm việc với Git và quy trình Agile/Scrum.\n• Ưu tiên ứng viên có sản phẩm thực tế trên Google Play."
        )
    )

    fun getLatestJobs(): List<CandidateJobItem> = listOf(
        CandidateJobItem(
            id = "LAT_01",
            title = "Chuyên viên Marketing",
            company = "VNG",
            location = "TP.HCM (Làm việc từ xa)",
            postedTime = "9 giờ trước",
            source = "LinkedIn",
            category = "Marketing",
            isRemote = true,
            aboutCompany = "VNG là công ty công nghệ hàng đầu Việt Nam trong lĩnh vực trò chơi trực tuyến, nền tảng kết nối, thanh toán điện tử và dịch vụ đám mây.",
            requirements = "• Lập kế hoạch và triển khai chiến dịch truyền thông sản phẩm số.\n• Phân tích dữ liệu tăng trưởng người dùng và tối ưu ngân sách quảng cáo.\n• Sáng tạo nội dung bắt kịp xu hướng công nghệ."
        ),
        CandidateJobItem(
            id = "LAT_02",
            title = "Trưởng nhóm lập trình",
            company = "VNG",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "5 giờ trước",
            source = "Glassdoor",
            category = "Lập trình",
            isRemote = true,
            aboutCompany = "VNG xây dựng môi trường kỹ thuật cởi mở, khuyến khích thử nghiệm và làm chủ công nghệ mới.",
            requirements = "• Tối thiểu 3 năm kinh nghiệm phát triển phần mềm và dẫn dắt nhóm.\n• Nắm vững kiến trúc hệ thống, review code và đào tạo thành viên.\n• Kỹ năng giao tiếp và giải quyết vấn đề tốt."
        ),
        CandidateJobItem(
            id = "LAT_03",
            title = "Lập trình viên Frontend",
            company = "Viettel",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "9 giờ trước",
            source = "Glassdoor",
            category = "Lập trình",
            isRemote = true,
            aboutCompany = "Trung tâm Phần mềm Viettel phát triển các nền tảng số phục vụ hàng triệu người dùng doanh nghiệp và cá nhân.",
            requirements = "• Thành thạo HTML, CSS, JavaScript, ReactJS hoặc TypeScript.\n• Biết sử dụng Git và tích hợp RESTful API.\n• Chủ động, ham học hỏi và phối hợp nhóm hiệu quả."
        )
    )
}