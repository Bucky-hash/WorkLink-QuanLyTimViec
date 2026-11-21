package com.example.worklink.ui.candidate.auth

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import com.example.worklink.R
import com.example.worklink.core.base.BaseActivity
import com.example.worklink.core.ext.hideKeyboard
import com.example.worklink.core.ext.toast
import com.example.worklink.ui.admin.AdminMainActivity
import com.example.worklink.ui.candidate.CandidateMainActivity
import com.example.worklink.ui.employer.EmployerMainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/** Ứng viên - Đăng nhập (Figma "Ứng viên - Đăng nhập"). */
class LoginActivity : BaseActivity() {

    // DỮ LIỆU GIẢ: đổi sang repository thật khi có backend.
    private val repository: AuthRepository = FakeAuthRepository()

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bindAuthHeader(AuthTab.LOGIN, R.string.auth_login_subtitle) { tab ->
            if (tab == AuthTab.REGISTER) switchTo(RegisterActivity::class.java)
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

        findViewById<View>(R.id.btn_login).setOnClickListener { submit() }
        findViewById<View>(R.id.tv_forgot_password).setOnClickListener { toast(R.string.auth_feature_wip) }
        findViewById<View>(R.id.tv_go_register).setOnClickListener { switchTo(RegisterActivity::class.java) }
        findViewById<View>(R.id.tv_role_employer).setOnClickListener { openMain(EmployerMainActivity::class.java) }
        findViewById<View>(R.id.tv_role_admin).setOnClickListener { openMain(AdminMainActivity::class.java) }
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
        // "Ghi nhớ" chưa lưu gì vì chưa có backend / phiên đăng nhập; chỉ đọc giá trị để sau này dùng.
        @Suppress("UNUSED_VARIABLE")
        val remember = findViewById<CheckBox>(R.id.cb_remember).isChecked

        when (val result = repository.login(email, password)) {
            is AuthResult.Success -> openMain(CandidateMainActivity::class.java)
            is AuthResult.Failure -> toast(result.messageRes)
        }
    }
}
