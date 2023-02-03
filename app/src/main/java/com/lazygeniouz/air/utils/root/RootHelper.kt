package com.lazygeniouz.air.utils.root

import com.lazygeniouz.air.utils.misc.Constants
import com.lazygeniouz.air.utils.misc.Constants.xmlBasedAdIdRegex
import com.lazygeniouz.air.utils.misc.logDebug
import com.lazygeniouz.air.utils.runtime.Runtime
import com.lazygeniouz.air.utils.runtime.Runtime.readLines

/**
 * A utility class for executing some miscellaneous root commands.
 */
object RootHelper {

    /**
     * Check if the device is rooted or not.
     *
     * On a rooted device, the first launch will usually show the Root Manager app to
     * handle (Deny / Grant) Su permission.
     * After that, the method should properly return the correct value.
     *
     * On devices without Root, running the `su` command will return **Permission denied**.
     *
     * @return **true** if the device is rooted & the permission is Granted by the
     * Root-Manager App, **false** otherwise.
     */
    fun isRootAvailable(): Boolean {
        return try {
            Runtime.hasSu()
        } catch (ignore: Exception) {
            false
        }
    }

    /**
     * Returns the **Advertising Identifier** of the device.
     *
     * Usually you'd need to use **AdvertisingIdClient** on a device with GMS installed,
     * to read the identifier but using the `cat` command we can easily read it from the file.
     *
     * @param adIdFilePath The path of the file that stores the Advertising Identifier.
     * @return Advertising Identifier of the device.
     */
    fun getAdIdFromFile(adIdFilePath: String): String {
        return try {
            // possible on first run for non gms
            if (adIdFilePath.isEmpty()) return ""
            Runtime.exec("cat $adIdFilePath").readLines().first { line ->
                val matchResult = xmlBasedAdIdRegex.find(line)
                if (matchResult != null) return matchResult.groupValues[1]
                else false
            }
        } catch (ignore: Exception) {
            ""
        }
    }

    /**
     * A utility method that traverses all the installed app's `shared_prefs` & checks for all the
     * xml files in the directory to search for the provided Advertising Identifier.
     *
     * This is a one time operation & that too only for devices that do not have GMS installed,
     * e.g. Huawei (HMS) or other core Chinese phones.
     *
     * @param adId Identifier to search for.
     * @param onPathFound Callback that is invoked when a file is found containing the [adId].
     * In-case there are multiple files containing the same [adId] then the first one is returned.
     */
    fun searchForAdIdFile(adId: String, onPathFound: (String) -> Unit) {
        try {
            Runtime.find(Constants.getFindCommand(adId)).readLines().let { lines ->
                if (lines.isNotEmpty()) {
                    onPathFound(lines.first())
                    if (lines.size > 1) {
                        // multiple files have the same ad-id stored.
                        // TODO: we'll have to think of something else.
                        // lines.forEach { line -> onPathFound(line) }
                        logDebug("Provided AdId found in multiple files.")
                    }
                } else onPathFound("")
            }
        } catch (ignore: Exception) {
            onPathFound("")
        }
    }

    /**
     * Delete the file containing Advertising Identifier.
     *
     * @param adIdFilePath The path of the file containing the identifier.
     * @return **true** if the file was successfully deleted, **false** otherwise or if an
     * [Exception] was caught.
     */
    fun deleteAdIdFile(adIdFilePath: String): Boolean {
        return try {
            val process = Runtime.exec("rm -f $adIdFilePath")
            val exitValue = process.waitFor()
            (exitValue == 0).also { process.destroy() }
        } catch (ignore: Exception) {
            false
        }
    }
}