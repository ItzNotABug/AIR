package com.lazygeniouz.air.utils.misc

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
}