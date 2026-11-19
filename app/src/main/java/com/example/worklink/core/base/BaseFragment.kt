package com.example.worklink.core.base

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.worklink.core.ext.hideKeyboard
import com.example.worklink.core.ext.toast

/** Fragment gốc: các màn hình trong tab (Khám phá, Trang chủ, Hộp thư...) kế thừa lớp này. */
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected fun toast(@StringRes resId: Int) = requireContext().toast(resId)

    protected fun toast(message: String) = requireContext().toast(message)

    protected fun hideKeyboard() {
        view?.hideKeyboard()
    }
}
