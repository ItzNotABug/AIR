package com.lazygeniouz.air.ui.tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.ui.components.CenteredColumn
import com.lazygeniouz.air.ui.layouts.GmsLayout
import com.lazygeniouz.air.ui.layouts.NoGmsLayout

@Composable
fun MainTab(adIdViewModel: AdIdViewModel) {
    CenteredColumn(modifier = Modifier.fillMaxSize()) {
        if (adIdViewModel.isGmsInstalled) GmsLayout(adIdViewModel)
        else NoGmsLayout(adIdViewModel)
    }
}