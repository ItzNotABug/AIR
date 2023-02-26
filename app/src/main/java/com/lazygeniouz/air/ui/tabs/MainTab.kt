package com.lazygeniouz.air.ui.tabs

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.ui.components.CenteredColumn
import com.lazygeniouz.air.ui.layouts.GmsLayout
import com.lazygeniouz.air.ui.layouts.NoGmsLayout

/**
 * The main tab that shows the current Ad Id & allows deleting.
 */
@Composable
fun MainTab(adIdViewModel: AdIdViewModel) {
    CenteredColumn(modifier = Modifier.fillMaxSize()) {
        if (adIdViewModel.isGmsInstalled) GmsLayout(adIdViewModel)
        else NoGmsLayout(adIdViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun MainTabPreview() {
    // this VM generation is not good but this is just a preview anyway.
    val application = LocalContext.current.applicationContext as Application
    val irrelevantViewModel = AdIdViewModel(application)
    MainTab(irrelevantViewModel)
}