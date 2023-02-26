package com.lazygeniouz.air.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.lazygeniouz.air.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Main : Screen("main", R.string.main_tab, Icons.Default.Home)
    object Setting : Screen("settings", R.string.settings_tab, Icons.Default.Settings)
}