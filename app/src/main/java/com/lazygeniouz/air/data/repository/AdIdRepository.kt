package com.lazygeniouz.air.data.repository

import android.content.Context
import androidx.core.content.edit
import com.lazygeniouz.air.utils.misc.Constants
import com.lazygeniouz.air.utils.misc.getSettings
import java.io.File

/**
 * A base repository class to handle GMS & NoGMS use-cases.
 */
sealed class AdIdRepository {

    abstract val isAdIdFileExists: Boolean

    /**
     * For devices that have GMS installed.
     */
    object GMS : AdIdRepository() {

        /**
         * Check if the GMS created file i.e. **adid_settings.xml** exists on device.
         */
        override val isAdIdFileExists: Boolean get() = File(Constants.adIdSettingsPath).exists()
    }

    /**
     * For devices that do not have GMS installed.
     */
    class NoGMS(context: Context) : AdIdRepository() {
        private val settingsKey = "nonGms"
        private val adIdSettingsKey = "adid_settings_file_path"
        private val sharedPreferences by lazy { context.getSettings(settingsKey) }

        /**
         * Returns path of the File that stores the `Advertising Identifier`.
         *
         * This will be empty on the first launch on devices without No GMS, obviously.
         */
        val adIdFilePathOnNonGms: String
            get() = sharedPreferences.getString(adIdSettingsKey, "") ?: ""

        /**
         * Check if the path of the file that stores the Advertising Identifier is saved in local
         * storage.
         */
        override val isAdIdFileExists: Boolean get() = adIdFilePathOnNonGms.isNotEmpty()

        /**
         * Save the path of the file that has the provided Advertising Identifier in its contents.
         */
        fun saveAdIdPathOnNonGms(path: String) {
            if (path.isEmpty()) return
            sharedPreferences.edit { putString(adIdSettingsKey, path) }
        }

        /**
         * Delete the file that has the provided Advertising Identifier in its contents.
         */
        fun deleteAdIdPathOnNonGms() {
            sharedPreferences.edit { remove(adIdSettingsKey) }
        }
    }
}