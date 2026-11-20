package com.example.worklink.ui.candidate.cv

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.use
import androidx.core.widget.doAfterTextChanged
import com.example.worklink.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * Ô nhập của màn Tạo CV: nhãn + ô (cao 48, bo 10, viền #D1D5DB) + dòng gợi ý tùy chọn.
 *
 * - cvKind = text | multiline | picker (picker: không gõ, bấm để chọn qua [onActionClick])
 * - cvEndIcon: icon cuối ô (18dp). Với picker và ô "Thêm kỹ năng", bấm icon gọi [onActionClick].
 */
class CvField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val labelView: TextView
    private val inputLayout: TextInputLayout
    private val input: TextInputEditText
    private val helperView: TextView
    private val isPicker: Boolean

    /** Gọi khi người dùng bấm vào ô (picker) hoặc icon cuối ô. */
    var onActionClick: (() -> Unit)? = null

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.view_cv_field, this, true)
        labelView = findViewById(R.id.cv_label)
        inputLayout = findViewById(R.id.cv_input_layout)
        input = findViewById(R.id.cv_input)
        helperView = findViewById(R.id.cv_helper)

        var kind = KIND_TEXT
        var endIcon = 0
        context.obtainStyledAttributes(attrs, R.styleable.CvField).use { a ->
            labelView.text = a.getString(R.styleable.CvField_cvLabel)
            input.hint = a.getString(R.styleable.CvField_cvHint)
            a.getString(R.styleable.CvField_cvHelper)?.let {
                helperView.text = it
                helperView.visibility = VISIBLE
            }
            kind = a.getInt(R.styleable.CvField_cvKind, KIND_TEXT)
            endIcon = a.getResourceId(R.styleable.CvField_cvEndIcon, 0)
            val minLines = a.getInt(R.styleable.CvField_cvMinLines, 4)
            input.inputType = when (kind) {
                KIND_MULTILINE -> InputType.TYPE_CLASS_TEXT or
                    InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                else -> when (a.getInt(R.styleable.CvField_cvInputType, 0)) {
                    1 -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    2 -> InputType.TYPE_CLASS_PHONE
                    3 -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI
                    else -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                }
            }
            if (kind == KIND_MULTILINE) {
                input.gravity = Gravity.TOP or Gravity.START
                input.minLines = minLines
                input.maxLines = Int.MAX_VALUE
            }
        }
        isPicker = kind == KIND_PICKER

        if (endIcon != 0) {
            inputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            inputLayout.setEndIconDrawable(endIcon)
            inputLayout.setEndIconOnClickListener { onActionClick?.invoke() }
        }
        if (isPicker) {
            input.isFocusable = false
            input.isFocusableInTouchMode = false
            input.isCursorVisible = false
            input.keyListener = null
            input.setOnClickListener { onActionClick?.invoke() }
        } else {
            input.doAfterTextChanged { inputLayout.error = null }
        }
    }

    /** Nội dung đã cắt khoảng trắng hai đầu. Ghi vào thì giữ nguyên chữ. */
    var text: String
        get() = input.text?.toString().orEmpty().trim()
        set(value) {
            input.setText(value)
            inputLayout.error = null
        }

    val labelText: String get() = labelView.text.toString()

    val editText: TextInputEditText get() = input

    fun setError(message: String?) {
        inputLayout.error = message
    }

    /** Khóa / mở ô picker (ví dụ ô Kết thúc khi đang làm việc tại đây). */
    fun setFieldEnabled(enabled: Boolean) {
        inputLayout.isEnabled = enabled
        input.isEnabled = enabled
    }

    // Các CvField dùng chung id con (cv_input...) nên không để hệ thống lưu/khôi phục từng view con,
    // tránh các ô bị ghi đè lẫn nhau. Chỉ lưu nội dung chữ của chính CvField có android:id.
    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable(STATE_SUPER, super.onSaveInstanceState())
        putString(STATE_TEXT, input.text?.toString())
    }

    @Suppress("DEPRECATION")
    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(STATE_SUPER))
            input.setText(state.getString(STATE_TEXT).orEmpty())
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private companion object {
        const val KIND_TEXT = 0
        const val KIND_MULTILINE = 1
        const val KIND_PICKER = 2
        const val STATE_SUPER = "super"
        const val STATE_TEXT = "text"
    }
}
