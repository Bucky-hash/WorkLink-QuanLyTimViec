package com.example.worklink.ui.candidate.auth

import android.app.Activity
import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.example.worklink.R
import com.google.android.material.textfield.TextInputLayout

enum class AuthTab { REGISTER, LOGIN }

/**
 * Gắn header dùng chung (include_auth_header): đánh dấu tab đang chọn và bắt sự kiện bấm tab kia.
 * [subtitleRes] = null thì ẩn dòng phụ dưới "worklink" (Figma Đăng ký không có dòng này).
 */
internal fun Activity.bindAuthHeader(
    selected: AuthTab,
    subtitleRes: Int?,
    onOtherTabClick: (AuthTab) -> Unit
) {
    val subtitle = findViewById<TextView>(R.id.auth_subtitle)
    if (subtitleRes == null) subtitle.visibility = View.GONE else subtitle.setText(subtitleRes)

    fun render(label: TextView, underline: View, active: Boolean) {
        label.setTextAppearance(
            if (active) R.style.TextAppearance_Worklink_AuthTab_Active
            else R.style.TextAppearance_Worklink_AuthTab
        )
        underline.visibility = if (active) View.VISIBLE else View.INVISIBLE
    }

    render(
        findViewById(R.id.tab_register_label),
        findViewById(R.id.tab_register_underline),
        selected == AuthTab.REGISTER
    )
    render(
        findViewById(R.id.tab_login_label),
        findViewById(R.id.tab_login_underline),
        selected == AuthTab.LOGIN
    )

    findViewById<View>(R.id.tab_register).setOnClickListener {
        if (selected != AuthTab.REGISTER) onOtherTabClick(AuthTab.REGISTER)
    }
    findViewById<View>(R.id.tab_login).setOnClickListener {
        if (selected != AuthTab.LOGIN) onOtherTabClick(AuthTab.LOGIN)
    }
}

/** Chuyển sang màn khác của luồng đăng nhập và đóng màn hiện tại (không chồng thêm vào back stack). */
internal fun Activity.switchTo(target: Class<out Activity>) {
    startActivity(Intent(this, target))
    finish()
}

/** Vào màn chính của một vai trò và xóa luồng đăng nhập khỏi back stack. */
internal fun Activity.openMain(target: Class<out Activity>) {
    startActivity(
        Intent(this, target).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    )
}

/** Icon mắt ở cuối ô mật khẩu: bấm để hiện / ẩn. Mặc định ẩn (icon mắt gạch như Figma). */
internal fun TextInputLayout.bindPasswordToggle() {
    val edit = editText ?: return
    val font = ResourcesCompat.getFont(context, R.font.inter_regular)
    var visible = false

    fun render() {
        val selection = edit.selectionEnd
        edit.transformationMethod = if (visible) null else PasswordTransformationMethod.getInstance()
        edit.typeface = font // inputType password tự đổi sang font monospace, đặt lại Inter
        if (selection >= 0) edit.setSelection(selection)
        setEndIconDrawable(if (visible) R.drawable.ic_auth_eye else R.drawable.ic_auth_eye_off)
        endIconContentDescription = context.getString(
            if (visible) R.string.cd_hide_password else R.string.cd_show_password
        )
    }

    render()
    setEndIconOnClickListener {
        visible = !visible
        render()
    }
}

/** Xóa thông báo lỗi của ô khi người dùng gõ lại. */
internal fun TextInputLayout.clearErrorOnTyping() {
    editText?.doAfterTextChanged { error = null }
}
