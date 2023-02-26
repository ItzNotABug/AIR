package com.lazygeniouz.air.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.lazygeniouz.air.data.repository.AdIdRepository
import com.lazygeniouz.air.utils.misc.isGmsInstalled
import com.lazygeniouz.air.utils.root.RootHelper

/**
 * A [AndroidViewModel] to handle Ad Identifier based operations.
 */
class AdIdViewModel(application: Application) : AndroidViewModel(application) {

    private val noGmsAdIdRepository by lazy { AdIdRepository.NoGMS(application) }
    val isGmsInstalled get() = getApplication<Application>().isGmsInstalled()

    private fun getAdIdFilePath(): String {
        return if (isGmsInstalled) AdIdRepository.GMS.adIdFilePath
        else noGmsAdIdRepository.adIdFilePath
    }

    /**
     * @see AdIdRepository.isAdIdFileExists
     */
    fun isAdIdFileExists(): Boolean {
        return if (isGmsInstalled) AdIdRepository.GMS.isAdIdFileExists
        else noGmsAdIdRepository.isAdIdFileExists
    }

    /**
     * @see RootHelper.getAdIdFromFile
     */
    fun getAdIdFromFile(): String {
        val correctAdIdFilePath = getAdIdFilePath()
        return RootHelper.getAdIdFromFile(correctAdIdFilePath)
    }

    /**
     * @see AdIdRepository.NoGMS.saveAdIdPathOnNonGms
     */
    fun saveAdIdFilePath(adIdFilePath: String) {
        if (isGmsInstalled) return
        if (adIdFilePath.isEmpty()) return
        noGmsAdIdRepository.saveAdIdPathOnNonGms(adIdFilePath)
    }

    /**
     * @see AdIdRepository.NoGMS.deleteAdIdPathOnNonGms
     */
    fun deleteAdIdFilePath() {
        if (isGmsInstalled) return
        noGmsAdIdRepository.deleteAdIdPathOnNonGms()
    }

    /**
     * @see RootHelper.isRootAvailable
     */
    fun isRootAvailable(): Boolean {
        return RootHelper.isRootAvailable()
    }

    /**
     * @see RootHelper.deleteAdIdFile
     */
    fun deleteAdIdFile(): Boolean {
        return RootHelper.deleteAdIdFile(getAdIdFilePath())
    }

    /**
     * @see RootHelper.searchForAdIdFile
     */
    fun searchForAdIdFile(adId: String, filePath: (String) -> Unit) {
        return RootHelper.searchForAdIdFile(adId, filePath)
    }
}