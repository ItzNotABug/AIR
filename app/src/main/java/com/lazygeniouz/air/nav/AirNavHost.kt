package com.lazygeniouz.air.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.ui.tabs.MainTab
import com.lazygeniouz.air.ui.tabs.SettingsTab

@Composable
fun AirNavHost(
    innerPadding: PaddingValues,
    adIdViewModel: AdIdViewModel,
    navController: NavHostController,
) {
    NavHost(navController, Screen.Main.route, Modifier.padding(innerPadding)) {
        mainTabComposable(adIdViewModel)
        settingsTabComposable()
    }
}

// Home Tab
fun NavGraphBuilder.mainTabComposable(adIdViewModel: AdIdViewModel) {
    return composable(Screen.Main.route) { MainTab(adIdViewModel = adIdViewModel) }
}

// Settings Tab
fun NavGraphBuilder.settingsTabComposable() {
    return composable(Screen.Setting.route) { SettingsTab() }
}