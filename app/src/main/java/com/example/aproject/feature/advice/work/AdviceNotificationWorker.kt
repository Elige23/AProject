package com.example.aproject.feature.advice.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aproject.core.work.NotificationHelper
import com.example.aproject.feature.advice.data.api.AdviceApi
import com.example.aproject.feature.advice.domain.model.Advice
import com.example.aproject.feature.advice.domain.repository.AdviceRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker that fetches a random advice, saves it locally, and shows a notification.
 * If the request fails (e.g. no internet), it shows an invitation notification and retries later.
 *
 * In the catch block in `override suspend fun doWork()` for error handling, we display a standard
 * notification and mark `Result.success()`. Using `Result.retry()` will initiate retries with an
 * exponential delay (starting at 30 seconds by default).
 */
@HiltWorker
class AdviceNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val adviceApi: AdviceApi,
    private val repository: AdviceRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val response = adviceApi.getRandomAdvice()
            val advice = Advice(advice = response.slip.advice)

            repository.insertAdvice(advice)
            notificationHelper.showAdviceNotification(advice.advice)
            Result.success()
        } catch (e: Exception) {
            notificationHelper.showNoInternetAdviceNotification()
            Result.success()
        }
    }
}