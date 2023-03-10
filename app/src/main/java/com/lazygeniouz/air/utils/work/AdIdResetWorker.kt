package com.lazygeniouz.air.utils.work

import android.content.Context
import androidx.work.*
import com.lazygeniouz.air.R
import com.lazygeniouz.air.data.repository.AdIdRepository
import com.lazygeniouz.air.utils.misc.Constants
import com.lazygeniouz.air.utils.misc.Constants.periodicResetIntervalKey
import com.lazygeniouz.air.utils.misc.Notifications
import com.lazygeniouz.air.utils.misc.defaultPreferences
import com.lazygeniouz.air.utils.misc.isGmsInstalled
import com.lazygeniouz.air.utils.root.RootHelper
import java.util.concurrent.TimeUnit

/**
 * A worker to periodically delete the Advertising Identifier.
 */
class AdIdResetWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    private val appContext get() = applicationContext
    private val notifications by lazy { Notifications(appContext) }

    override suspend fun doWork(): Result {
        val adIdFilePath = getAdIdFile()
        if (!isAdIdFileExists()) {
            notifyStatus(false)
            return Result.failure()
        }

        RootHelper.deleteAdIdFile(adIdFilePath).also { notifyStatus(true) }
        return Result.success()
    }

    private fun isAdIdFileExists(): Boolean {
        return if (appContext.isGmsInstalled()) AdIdRepository.GMS.isAdIdFileExists
        else AdIdRepository.NoGMS(appContext).isAdIdFileExists
    }

    private fun notifyStatus(isSuccess: Boolean) {
        val title = if (isSuccess) R.string.notification_success_title
        else R.string.notification_failure_title

        val message = if (isSuccess) R.string.notification_success_message
        else R.string.notification_failure_message

        notifications.notify(title, message)
    }

    private fun getAdIdFile(): String {
        return if (appContext.isGmsInstalled()) AdIdRepository.GMS.adIdFilePath
        else AdIdRepository.NoGMS(appContext).adIdFilePath
    }

    companion object {
        private const val workKey = "adIdResetWorker"

        /**
         * Schedule a [AdIdResetWorker] or update it as required.
         */
        fun schedule(context: Context, isUpdate: Boolean) {
            val adIdResetWorker = getPeriodicWorkRequest(context)
            val manager = WorkManager.getInstance(context)
            val workPolicy = if (isUpdate) {
                cancel(context)
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE
            } else ExistingPeriodicWorkPolicy.KEEP
            manager.enqueueUniquePeriodicWork(workKey, workPolicy, adIdResetWorker)
        }

        /**
         * Cancel all the workers.
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWork()
        }

        private fun getPeriodicWorkRequest(context: Context): PeriodicWorkRequest {
            val interval = getIntervalPeriod(context)
            return PeriodicWorkRequestBuilder<AdIdResetWorker>(
                interval, TimeUnit.MILLISECONDS
            ).build()
        }

        private fun getIntervalPeriod(context: Context): Long {
            val preferences = context.defaultPreferences
            val savedInterval = preferences.getInt(periodicResetIntervalKey, 0)
            return Constants.periodicResetIntervals[savedInterval].second
        }
    }
}