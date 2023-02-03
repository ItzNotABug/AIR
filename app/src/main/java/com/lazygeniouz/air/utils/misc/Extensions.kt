package com.lazygeniouz.air.utils.misc

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
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