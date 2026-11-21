package com.example.worklink.ui.employer

import androidx.annotation.IdRes

/**
 * Cho các Fragment con xin chuyển tab ở thanh điều hướng dưới (Bài đăng, Ứng viên, Lịch, Hộp thư).
 * [EmployerMainActivity] cài đặt; Fragment gọi qua `activity as? EmployerTabNavigator`.
 */
interface EmployerTabNavigator {
    fun openEmployerTab(@IdRes tabId: Int)
}
