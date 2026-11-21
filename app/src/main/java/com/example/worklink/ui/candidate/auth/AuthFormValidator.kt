package com.example.worklink.ui.candidate.auth

import android.util.Patterns
import androidx.annotation.StringRes
import com.example.worklink.R

/** Kiểm tra ô nhập dùng chung cho Đăng nhập và Đăng ký. Trả về id chuỗi lỗi, hoặc null nếu hợp lệ. */
object AuthFormValidator {

    const val MIN_PASSWORD_LENGTH = 6

    @StringRes
    fun emailError(email: String): Int? = when {
        email.isBlank() -> R.string.auth_error_email_empty
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> R.string.auth_error_email_invalid
        else -> null
    }

    @StringRes
    fun passwordError(password: String): Int? = when {
        password.isEmpty() -> R.string.auth_error_password_empty
        password.length < MIN_PASSWORD_LENGTH -> R.string.auth_error_password_short
        else -> null
    }
}
