package com.example.worklink.ui.employer.inbox

import androidx.annotation.DrawableRes
import java.text.Normalizer
import java.util.Locale

/** Toàn bộ dữ liệu màn Hộp thư Nhà tuyển dụng. Sau này chỉ cần thay nguồn dữ liệu, giao diện không đổi. */
data class EmployerInboxState(
    val conversations: List<Conversation>
) {
    /** Tổng số tin chưa đọc: dùng cho nhãn "Chưa đọc" và huy hiệu trên nút chat nổi. */
    val unreadTotal: Int get() = conversations.sumOf { it.unreadCount }
}

data class Conversation(
    val id: Long,
    val name: String,
    /** Nhãn giờ hiển thị sẵn ("9:38", "Hôm qua"). Khi có API thì đổi sang mốc thời gian và định dạng ở đây. */
    val timeLabel: String,
    val lastMessage: String,
    val unreadCount: Int = 0,
    /** Hiện ở dải "đang hoạt động" phía trên danh sách. */
    val isOnline: Boolean = false,
    /** Trợ lý Woli AI: đổi avatar sang biểu tượng AI, không hiện ở dải đang hoạt động. */
    val isAi: Boolean = false,
    /** Ảnh có sẵn trong app. null thì hiện biểu tượng tòa nhà mặc định. */
    @DrawableRes val avatarRes: Int? = null
) {
    val isUnread: Boolean get() = unreadCount > 0
}

/** Hai bộ lọc ở đầu danh sách (Figma: "Tất cả" · "Chưa đọc"). */
enum class InboxFilter { ALL, UNREAD }

/**
 * Lọc theo bộ lọc và từ khóa. Tìm không phân biệt hoa thường và dấu tiếng Việt
 * ("viet" khớp "Viettel", "phong van" khớp "phỏng vấn"), tìm trong tên và tin nhắn cuối.
 */
fun List<Conversation>.filterBy(tab: InboxFilter, query: String): List<Conversation> {
    val keyword = query.trim().foldForSearch()
    return filter { conversation ->
        (tab == InboxFilter.ALL || conversation.isUnread) &&
            (keyword.isEmpty() ||
                conversation.name.foldForSearch().contains(keyword) ||
                conversation.lastMessage.foldForSearch().contains(keyword))
    }
}

private val COMBINING_MARKS = Regex("\\p{InCombiningDiacriticalMarks}+")

private fun String.foldForSearch(): String =
    Normalizer.normalize(lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(COMBINING_MARKS, "")
        .replace('đ', 'd')

/** DỮ LIỆU MẪU lấy từ Figma. Xóa khi nối API/CSDL thật. */
object EmployerInboxSampleData {
    val state = EmployerInboxState(
        conversations = listOf(
            Conversation(1, "Công ty ABC", "9:38", "Cần thêm 2 hồ sơ Backend cho dự án mới", unreadCount = 1, isOnline = true),
            Conversation(2, "FPT Software", "9:25", "Gửi lịch phỏng vấn vòng 2 cho 3 ứng viên", unreadCount = 1, isOnline = true),
            Conversation(3, "Viettel", "8:50", "Chốt lịch gọi trao đổi về bài đăng mới", unreadCount = 1, isOnline = true),
            Conversation(4, "FPT Software", "8:10", "Gửi lại CV của 2 ứng viên đã duyệt", isOnline = true),
            Conversation(5, "Sendo", "Hôm qua", "Phản hồi hồ sơ ứng viên vị trí QA"),
            Conversation(6, "Tiki", "Hôm qua", "Cảm ơn đã gửi thông tin ứng viên"),
            Conversation(7, "Woli AI", "", "Đã sẵn sàng giúp bạn soạn tin nhắn cho nhà tuyển dụng", isAi = true)
        )
    )
}
