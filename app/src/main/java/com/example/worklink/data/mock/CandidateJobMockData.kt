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
    var isSaved: Boolean = false
)

object CandidateJobMockData {

    fun getRecommendedJobs(): List<CandidateJobItem> = listOf(
        CandidateJobItem(
            id = "REC_01",
            title = "Nhà thiết kế UX",
            company = "FPT Software",
            location = "Hà Nội (Làm việc từ xa)",
            postedTime = "1 ngày trước",
            source = "LinkedIn",
            category = "Lập trình",
            isRemote = true
        ),
        CandidateJobItem(
            id = "REC_02",
            title = "Kế toán tổng hợp",
            company = "Viettel",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "5 ngày trước",
            source = "Monster",
            category = "Kế toán",
            isRemote = true
        ),
        CandidateJobItem(
            id = "REC_03",
            title = "Lập trình viên Android",
            company = "FPT Software",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "2 ngày trước",
            source = "LinkedIn",
            category = "Lập trình",
            isRemote = true
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
            isRemote = true
        ),
        CandidateJobItem(
            id = "LAT_02",
            title = "Trưởng nhóm lập trình",
            company = "VNG",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "5 giờ trước",
            source = "Glassdoor",
            category = "Lập trình",
            isRemote = true
        ),
        CandidateJobItem(
            id = "LAT_03",
            title = "Lập trình viên Frontend",
            company = "Viettel",
            location = "Đà Nẵng (Làm việc từ xa)",
            postedTime = "9 giờ trước",
            source = "Glassdoor",
            category = "Lập trình",
            isRemote = true
        )
    )
}