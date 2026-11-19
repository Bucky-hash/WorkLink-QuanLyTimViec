package com.example.worklink.core.nav

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

data class NavTab(
    @IdRes val id: Int,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
)
