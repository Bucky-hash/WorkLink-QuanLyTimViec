package com.example.worklink.ui.candidate.auth

import com.example.worklink.R

/**
 * DỮ LIỆU GIẢ (chưa có backend). Thay bằng lớp gọi API thật rồi đổi chỗ khởi tạo
 * trong LoginActivity / RegisterActivity.
 *
 * - Đăng nhập: thành công với mọi email, mật khẩu hợp lệ (đã kiểm tra ở form).
 * - Đăng ký: báo lỗi nếu email là [TAKEN_EMAIL] để thử giao diện lỗi, còn lại thành công.
 */
class FakeAuthRepository : AuthRepository {

    override fun login(email: String, password: String): AuthResult = AuthResult.Success

    override fun register(email: String, password: String): AuthResult =
        if (email.equals(TAKEN_EMAIL, ignoreCase = true)) {
            AuthResult.Failure(R.string.auth_error_email_taken)
        } else {
            AuthResult.Success
        }

    companion object {
        const val TAKEN_EMAIL = "candidate@worklink.vn"
    }
}
