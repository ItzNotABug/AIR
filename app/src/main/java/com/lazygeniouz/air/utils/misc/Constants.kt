package com.lazygeniouz.air.utils.misc

import java.util.concurrent.TimeUnit

object Constants {
    private const val dataDir = "data/data/"
    const val gmsPackageName = "com.google.android.gms"
    const val adIdSettingsPath = "$dataDir$gmsPackageName/shared_prefs/adid_settings.xml"

    @Suppress("RegExpSimplifiable")
    private val adIdRegex = Regex("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
    val xmlBasedAdIdRegex = Regex("<string.*?>($adIdRegex)</string>")

    /**
     * Creates a executable command to search file containing provided [adId].
     *
     * @param adId The identifier to search on the filesystem.
     * @return Executable string command to search files.
     */
    fun getFindCommand(adId: String): String {
        // Use "-rLE -e" for RegEx
        return "-type d -name 'shared_prefs' -exec grep -rl $adId {} +"
    }

    const val periodicResetEnabledKey = "periodic_reset_enabled"
    const val periodicResetIntervalKey = "periodic_reset_interval"
    const val periodicResetNotificationsKey = "periodic_reset_notifications"

    val periodicResetIntervals = listOf(
        "15 Min." to TimeUnit.MINUTES.toMillis(15),
        "30 Min." to TimeUnit.MINUTES.toMillis(30),
        "1 Hour" to TimeUnit.HOURS.toMillis(1),
        "3 Hours" to TimeUnit.HOURS.toMillis(3),
        "6 Hours" to TimeUnit.HOURS.toMillis(6),
        "12 Hours" to TimeUnit.HOURS.toMillis(12),
        "24 Hours" to TimeUnit.HOURS.toMillis(24)
    )

    const val DEFAULT_CHANNEL_ID = "AirDefaultNotificationChannel"
    const val CHANNEL_NAME = "AIR Notifications"
    const val CHANNEL_DESCRIPTION = "Shows notifications on Ad Id Resets"
}