package com.example.worklink.core.nav

import androidx.annotation.ColorRes
import com.example.worklink.R

/**
 * Ba vai trò trong app. Mỗi vai trò có thanh điều hướng dưới riêng.
 * Màu tab theo Figma: Nhà tuyển dụng dùng #438E13 / #6B7570, hai vai trò còn lại dùng #58CC02 / #6B7280.
 */
enum class Role(
    @ColorRes val navActiveColor: Int,
    @ColorRes val navInactiveColor: Int,
    val hasChatFab: Boolean
) {
    CANDIDATE(R.color.wl_primary, R.color.wl_text_secondary, hasChatFab = true),
    EMPLOYER(R.color.wl_primary_dark, R.color.wl_nav_inactive_employer, hasChatFab = true),
    ADMIN(R.color.wl_primary, R.color.wl_text_secondary, hasChatFab = false)
}
