package com.lazygeniouz.air.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.*
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.ui.components.*

/**
 * Layout that is shown on devices that do have GMS installed.
 */
@Composable
fun GmsLayout(adIdViewModel: AdIdViewModel) {
    val advertisingId by remember { mutableStateOf(adIdViewModel.getAdIdFromFile()) }
    var isAdIdFileExists by remember { mutableStateOf(adIdViewModel.isAdIdFileExists()) }

    OnResumeEvent { isAdIdFileExists = adIdViewModel.isAdIdFileExists() }

    CenteredColumn {
        // GMS Install State
        GmsInstallationText()

        // Show & Reset Ad Identifier
        AnimatedVisibility(isAdIdFileExists) {
            CenteredColumn {
                AdIdentifierText(advertisingId)
                ResetAdIdButton(adIdViewModel) { isSuccess -> isAdIdFileExists = !isSuccess }
            }
        }

        // Ad Identifier is not available or was recently deleted
        AnimatedVisibility(!isAdIdFileExists) { AdIdentifierDoesNotExistsText() }
    }
}