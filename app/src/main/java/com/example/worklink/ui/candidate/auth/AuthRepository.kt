package com.example.worklink.ui.candidate.auth

import androidx.annotation.StringRes

/** Kết quả đăng nhập / đăng ký. Khi có backend thì giữ nguyên kiểu này, chỉ thay lớp cài đặt. */
sealed interface AuthResult {
    object Success : AuthResult
    data class Failure(@StringRes val messageRes: Int) : AuthResult
}

/** Cổng dữ liệu cho màn Đăng nhập / Đăng ký. Hiện dùng [FakeAuthRepository]. */
interface AuthRepository {
    fun login(email: String, password: String): AuthResult
    fun register(email: String, password: String): AuthResult
}
