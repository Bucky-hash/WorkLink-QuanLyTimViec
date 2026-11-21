package com.example.worklink.data.mock

enum class EmployerInterviewStatus {
    UPCOMING,
    CONFIRMED
}

data class EmployerInterviewDayItem(
    val dayOfMonth: Int,
    val dayLabelResId: Int,
    val fullDayNameResId: Int,
    val hasEvents: Boolean
)

data class EmployerInterviewSession(
    val id: String,
    val dayOfMonth: Int,
    val startTime: String,
    val endTime: String,
    val durationText: String,
    val candidateName: String,
    val jobTitle: String,
    val locationOrLink: String,
    val status: EmployerInterviewStatus
)

object EmployerInterviewMockData {

    fun getWeekDays(): List<EmployerInterviewDayItem> = listOf(
        EmployerInterviewDayItem(19, com.example.worklink.R.string.employer_interview_day_t2, com.example.worklink.R.string.employer_interview_full_t2, hasEvents = false),
        EmployerInterviewDayItem(20, com.example.worklink.R.string.employer_interview_day_t3, com.example.worklink.R.string.employer_interview_full_t3, hasEvents = true),
        EmployerInterviewDayItem(21, com.example.worklink.R.string.employer_interview_day_t4, com.example.worklink.R.string.employer_interview_full_t4, hasEvents = true),
        EmployerInterviewDayItem(22, com.example.worklink.R.string.employer_interview_day_t5, com.example.worklink.R.string.employer_interview_full_t5, hasEvents = false),
        EmployerInterviewDayItem(23, com.example.worklink.R.string.employer_interview_day_t6, com.example.worklink.R.string.employer_interview_full_t6, hasEvents = true),
        EmployerInterviewDayItem(24, com.example.worklink.R.string.employer_interview_day_t7, com.example.worklink.R.string.employer_interview_full_t7, hasEvents = false),
        EmployerInterviewDayItem(25, com.example.worklink.R.string.employer_interview_day_cn, com.example.worklink.R.string.employer_interview_full_cn, hasEvents = false)
    )

    fun getSessions(): List<EmployerInterviewSession> = listOf(
        EmployerInterviewSession(
            id = "iv_1",
            dayOfMonth = 21,
            startTime = "10:00",
            endTime = "10:45",
            durationText = "45 phút",
            candidateName = "Nguyễn Thảo Linh",
            jobTitle = "Lập trình Flutter",
            locationOrLink = "Google Meet Trực tuyến",
            status = EmployerInterviewStatus.UPCOMING
        ),
        EmployerInterviewSession(
            id = "iv_2",
            dayOfMonth = 21,
            startTime = "11:00",
            endTime = "11:45",
            durationText = "45 phút",
            candidateName = "Trần Minh Tuấn",
            jobTitle = "Lập trình di động",
            locationOrLink = "Phòng họp A Văn phòng ABC",
            status = EmployerInterviewStatus.CONFIRMED
        ),
        EmployerInterviewSession(
            id = "iv_3",
            dayOfMonth = 21,
            startTime = "14:00",
            endTime = "14:45",
            durationText = "45 phút",
            candidateName = "Lê Hoàng Nam",
            jobTitle = "Lập trình Flutter",
            locationOrLink = "Google Meet Trực tuyến",
            status = EmployerInterviewStatus.CONFIRMED
        ),
        EmployerInterviewSession(
            id = "iv_4",
            dayOfMonth = 20,
            startTime = "09:30",
            endTime = "10:15",
            durationText = "45 phút",
            candidateName = "Phạm Quốc Khánh",
            jobTitle = "Lập trình viên Frontend",
            locationOrLink = "Google Meet Trực tuyến",
            status = EmployerInterviewStatus.CONFIRMED
        ),
        EmployerInterviewSession(
            id = "iv_5",
            dayOfMonth = 23,
            startTime = "15:00",
            endTime = "15:45",
            durationText = "45 phút",
            candidateName = "Đỗ Minh Anh",
            jobTitle = "Chuyên viên UI/UX Designer",
            locationOrLink = "Phòng họp B Văn phòng ABC",
            status = EmployerInterviewStatus.UPCOMING
        )
    )
}