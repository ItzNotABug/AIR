package com.lazygeniouz.air.ui.activity

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.layouts.GmsInstalledLayout
import com.lazygeniouz.air.layouts.NoGmsLayout
import com.lazygeniouz.air.layouts.OnResumeEvent
import com.lazygeniouz.air.ui.components.RootNotAvailable
import com.lazygeniouz.air.ui.theme.ThemedSurface

class MainActivity : ComponentActivity() {

    private val adIdViewModel: AdIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ThemedSurface { AdIdResetApp(adIdViewModel) } }
    }
}

@Composable
fun AdIdResetApp(adIdViewModel: AdIdViewModel) {
    // Check if root is available
    var isRootAvailable by remember { mutableStateOf(adIdViewModel.isRootAvailable()) }

    if (!isRootAvailable) {
        RootNotAvailable()
        OnResumeEvent { isRootAvailable = adIdViewModel.isRootAvailable() }
    } else {
        if (adIdViewModel.isGmsInstalled) GmsInstalledLayout(adIdViewModel)
        else NoGmsLayout(adIdViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // this VM generation is not good but this is just a preview anyway.
    val application = LocalContext.current.applicationContext as Application
    val irrelevantViewModel = AdIdViewModel(application)
    ThemedSurface { AdIdResetApp(irrelevantViewModel) }
}