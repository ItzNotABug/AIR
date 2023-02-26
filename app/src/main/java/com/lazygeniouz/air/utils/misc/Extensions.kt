package com.lazygeniouz.air.utils.misc

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alorma.compose.settings.storage.preferences.BooleanPreferenceSettingValueState
import com.alorma.compose.settings.storage.preferences.IntPreferenceSettingValueState
import com.alorma.compose.settings.storage.preferences.rememberPreferenceBooleanSettingState
import com.alorma.compose.settings.storage.preferences.rememberPreferenceIntSettingState
import com.lazygeniouz.air.utils.misc.Constants.gmsPackageName
import kotlinx.coroutines.CoroutineScope

const val TAG = "AdIdResetApp"

/**
 * Log relevant stuff to logcat.
 */
fun logDebug(vararg msg: Any?) {
    if (msg.isEmpty()) return

    val message = if (msg.size > 1) msg.joinToString(", ") else {
        if (msg.first() is Throwable) (msg.first() as Throwable).message ?: msg.first().toString()
        else msg.first().toString()
    }

    Log.d(TAG, message)
}

/**
 * Get the shared preference with provided name.
 */
fun Context.getSettings(name: String): SharedPreferences {
    return getSharedPreferences(name, Context.MODE_PRIVATE)
}

/**
 * Show a message via a Toast.
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

/**
 * Check if the `Google Mobile Services` is installed on the device.
 */
fun Context.isGmsInstalled(): Boolean {
    return try {
        val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                gmsPackageName, PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {
            @Suppress("deprecation")
            packageManager.getApplicationInfo(gmsPackageName, 0)
        }
        applicationInfo.enabled
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

/**
 * Check if the notification permission is granted on or above Android Tiramisu.
 */
fun Context.notificationPermissionsGranted(): Boolean {
    return if (isAboveOrOnAndroidT) {
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}

/**
 * Request the notification permission on or above Android Tiramisu.
 */
fun Context.requestNotificationsPermission() {
    if (isAboveOrOnAndroidT) {
        ActivityCompat.requestPermissions(
            this as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101
        )
    }
}

/**
 * Check if the device is on or above Android Tiramisu.
 */
val isAboveOrOnAndroidT get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

/**
 * Get the default shared preferences.
 */
val Context.defaultPreferences get() = getSettings("${packageName}_preferences")

val Context.userNotificationsEnabled: Boolean
    get() {
        return defaultPreferences.getBoolean(
            Constants.periodicResetNotificationsKey, true
        )
    }


/**
 * An empty PendingIntent to cancel the notification on click.
 */
val Context.emptyPendingIntent: PendingIntent
    get() = PendingIntent.getActivity(
        this, System.currentTimeMillis().toInt(), Intent(), pendingIntentFlag
    )

/**
 * Get a relevant PendingIntent flag depending on SDK version.
 */
private val pendingIntentFlag: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else PendingIntent.FLAG_UPDATE_CURRENT
    }

@Composable
fun rememberIntPreference(key: String, default: Int): IntPreferenceSettingValueState {
    return rememberPreferenceIntSettingState(key = key, defaultValue = default)
}

@Composable
fun rememberBooleanPreference(key: String, default: Boolean): BooleanPreferenceSettingValueState {
    return rememberPreferenceBooleanSettingState(key = key, defaultValue = default)
}

@Composable
fun OneShotEffect(block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(Unit, block = block)
}