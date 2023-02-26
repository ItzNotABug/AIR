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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lazygeniouz.air.utils.misc.Constants.gmsPackageName

const val TAG = "AdIdResetApp"

fun logDebug(vararg msg: Any?) {
    if (msg.isEmpty()) return

    val message = if (msg.size > 1) msg.joinToString(", ") else {
        if (msg.first() is Throwable) (msg.first() as Throwable).message ?: msg.first().toString()
        else msg.first().toString()
    }

    Log.d(TAG, message)
}

fun Context.getSettings(name: String): SharedPreferences {
    return getSharedPreferences(name, Context.MODE_PRIVATE)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

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

fun Context.notificationPermissionsGranted(): Boolean {
    return if (isAboveOrOnAndroidT) {
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}

fun Context.requestNotificationsPermission() {
    if (isAboveOrOnAndroidT) {
        ActivityCompat.requestPermissions(
            this as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101
        )
    }
}

val isAboveOrOnAndroidT get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

val Context.defaultPreferences get() = getSettings("${packageName}_preferences")

val Context.emptyPendingIntent: PendingIntent
    get() = PendingIntent.getActivity(
        this, System.currentTimeMillis().toInt(), Intent(), pendingIntentFlag
    )

private val pendingIntentFlag: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else PendingIntent.FLAG_UPDATE_CURRENT
    }