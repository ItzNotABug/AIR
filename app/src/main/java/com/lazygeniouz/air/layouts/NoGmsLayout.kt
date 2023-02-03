package com.lazygeniouz.air.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.*
import com.lazygeniouz.air.data.viewmodel.AdIdViewModel
import com.lazygeniouz.air.ui.components.*

/**
 * Layout that is shown on devices that do not have GMS installed.
 */
@Composable
fun NoGmsLayout(adIdViewModel: AdIdViewModel) {
    var advertisingId by remember { mutableStateOf(adIdViewModel.getAdIdFromFile()) }
    var isAdIdFileExists by remember { mutableStateOf(adIdViewModel.isAdIdFileExists()) }

    OnResumeEvent {
        advertisingId = adIdViewModel.getAdIdFromFile()
        isAdIdFileExists = adIdViewModel.isAdIdFileExists()
    }

    CenteredColumn {

        // GMS Install State
        GmsInstallationText()

        // Only on first open where Ad Id file is not found
        AnimatedVisibility(!isAdIdFileExists) {
            PasteAdIdentifierForm(adIdViewModel) { path ->
                isAdIdFileExists = true
                adIdViewModel.saveAdIdFilePath(path)
                advertisingId = adIdViewModel.getAdIdFromFile()
            }
        }

        AnimatedVisibility(isAdIdFileExists) {
            CenteredColumn {
                // File exists but Ad Identifier does not.
                // Ad Identifier is not available or was recently deleted.
                AnimatedVisibility(advertisingId.isEmpty()) {
                    AdIdentifierDoesNotExistsText()
                }

                // Show & Reset Ad Identifier
                AnimatedVisibility(advertisingId.isNotEmpty()) {
                    CenteredColumn {
                        AdIdentifierText(advertisingId)
                        ResetAdIdButton(adIdViewModel) {
                            advertisingId = adIdViewModel.getAdIdFromFile()
                        }
                    }
                }

                // Paste Ad Identifier again find the file storing it.
                ResetAdIdFilePath(adIdViewModel) {
                    isAdIdFileExists = adIdViewModel.isAdIdFileExists()
                }
            }
        }
    }
}