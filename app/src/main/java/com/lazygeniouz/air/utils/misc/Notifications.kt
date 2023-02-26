package com.lazygeniouz.air.utils.misc

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_DEFAULT
import com.lazygeniouz.air.utils.misc.Constants.CHANNEL_DESCRIPTION
import com.lazygeniouz.air.utils.misc.Constants.CHANNEL_NAME
import com.lazygeniouz.air.utils.misc.Constants.DEFAULT_CHANNEL_ID

class Notifications(private val context: Context) {

    private val notificationManager by lazy { NotificationManagerCompat.from(context) }

    fun notify(@StringRes titleResourceId: Int, @StringRes messageResourceId: Int) {
        val notification = buildNotification(titleResourceId, messageResourceId)
        notify(notification)
    }

    private fun buildNotification(
        @StringRes titleResourceId: Int, @StringRes messageResourceId: Int,
    ): Notification {
        val title = context.getString(titleResourceId)
        val message = context.getString(messageResourceId)
        return NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message).setAutoCancel(true)
            .setContentIntent(context.emptyPendingIntent)
            // I really don't care about this icon right now.
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
    }

    private fun makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(DEFAULT_CHANNEL_ID) != null) return

            NotificationChannelCompat.Builder(
                DEFAULT_CHANNEL_ID, IMPORTANCE_DEFAULT
            ).apply {
                setName(CHANNEL_NAME)
                setDescription(CHANNEL_DESCRIPTION)
                notificationManager.createNotificationChannel(build())
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun notify(notification: Notification) {
        makeNotificationChannel()
        if (context.notificationPermissionsGranted()) {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}