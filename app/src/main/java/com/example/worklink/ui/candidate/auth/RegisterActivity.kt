package com.example.worklink.ui.candidate.auth

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.worklink.R
import com.example.worklink.core.base.BaseActivity
import com.example.worklink.core.ext.hideKeyboard
import com.example.worklink.core.ext.toast
import com.example.worklink.ui.candidate.cv.CreateCvActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Ứng viên - Đăng ký (Figma "Ứng viên - Đăng ký"): Email, Mật khẩu, điều khoản. */
class RegisterActivity : BaseActivity() {

    // DỮ LIỆU GIẢ: đổi sang repository thật khi có backend.
    private val repository: AuthRepository = FakeAuthRepository()

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Figma Đăng ký không có dòng phụ dưới "worklink" nên ẩn đi.
        bindAuthHeader(AuthTab.REGISTER, subtitleRes = null) { tab ->
            if (tab == AuthTab.LOGIN) switchTo(LoginActivity::class.java)
        }

        emailLayout = findViewById(R.id.til_email)
        passwordLayout = findViewById(R.id.til_password)
        emailInput = findViewById(R.id.et_email)
        passwordInput = findViewById(R.id.et_password)

        passwordLayout.bindPasswordToggle()
        emailLayout.clearErrorOnTyping()
        passwordLayout.clearErrorOnTyping()

        passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submit()
                true
            } else {
                false
            }
        }

        findViewById<View>(R.id.btn_register).setOnClickListener { submit() }
        findViewById<View>(R.id.tv_terms).setOnClickListener { toast(R.string.auth_feature_wip) }
        findViewById<View>(R.id.tv_go_login).setOnClickListener { switchTo(LoginActivity::class.java) }
    }

    private fun submit() {
        val email = emailInput.text?.toString().orEmpty().trim()
        val password = passwordInput.text?.toString().orEmpty()

        val emailError = AuthFormValidator.emailError(email)
        val passwordError = AuthFormValidator.passwordError(password)
        emailLayout.error = emailError?.let { getString(it) }
        passwordLayout.error = passwordError?.let { getString(it) }
        if (emailError != null || passwordError != null) return

        passwordInput.hideKeyboard()

        when (val result = repository.register(email, password)) {
            // Đăng ký xong thì sang Tạo CV; Lưu CV mới vào màn chính của Ứng viên.
            is AuthResult.Success -> switchTo(CreateCvActivity::class.java)
            is AuthResult.Failure -> emailLayout.error = getString(result.messageRes)
        }
    }
}
