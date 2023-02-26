package com.lazygeniouz.air.ui.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alorma.compose.settings.storage.preferences.rememberPreferenceBooleanSettingState
import com.alorma.compose.settings.storage.preferences.rememberPreferenceIntSettingState
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.alorma.compose.settings.ui.SettingsListDropdown
import com.lazygeniouz.air.ui.components.OnResumeEvent
import com.lazygeniouz.air.utils.misc.Constants.periodicResetEnabledKey
import com.lazygeniouz.air.utils.misc.Constants.periodicResetIntervalKey
import com.lazygeniouz.air.utils.misc.Constants.periodicResetIntervals
import com.lazygeniouz.air.utils.misc.notificationPermissionsGranted
import com.lazygeniouz.air.utils.misc.requestNotificationsPermission
import com.lazygeniouz.air.utils.misc.toast
import com.lazygeniouz.air.utils.work.AdIdResetWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * The settings tab that allows setting up periodic Ad Id resets.
 */
@Composable
fun SettingsTab() {

    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var permissionGranted by remember { mutableStateOf(localContext.notificationPermissionsGranted()) }
    val enabledState =
        rememberPreferenceBooleanSettingState(key = periodicResetEnabledKey, defaultValue = false)
    val dropdownState =
        rememberPreferenceIntSettingState(key = periodicResetIntervalKey, defaultValue = 0)

    OnResumeEvent { permissionGranted = localContext.notificationPermissionsGranted() }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Settings",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )

        SettingsCheckbox(
            state = enabledState,
            icon = { Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null) },
            title = { Text(text = "Periodic Reset") },
            subtitle = { Text(text = "Enable this option for periodic Ad Id Reset") },
            onCheckedChange = { isSelected ->
                if (permissionGranted) {
                    if (!isSelected) AdIdResetWorker.cancel(localContext)
                    else AdIdResetWorker.schedule(localContext, false)
                } else {
                    enabledState.value = false
                    localContext.requestNotificationsPermission()
                }
            }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

        SettingsListDropdown(
            state = dropdownState,
            enabled = enabledState.value,
            title = { Text(text = "Periodic Reset Interval") },
            subtitle = { Text(text = "Select reset interval") },
            icon = { Icon(imageVector = Icons.Rounded.DateRange, contentDescription = null) },
            items = periodicResetIntervals.map { it.first },
            onItemSelected = { _, text ->
                AdIdResetWorker.schedule(localContext, true)
                coroutineScope.launch {
                    delay(500)
                    localContext.toast("Interval updated to $text")
                }
            }
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))

        Text(
            text = "Periodic resets are done using `WorkManager` api so they are guaranteed to run BUT not always exact!",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsTabPreview() {
    SettingsTab()
}