package com.example.aproject.core.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.aproject.feature.advice.work.AdviceNotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Schedules background work for the application.
 */
@Singleton
class WorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val ADVICE_WORK_NAME = "daily_advice_notification"
    }

    /**
     * Schedules a daily advice notification at 10:00 AM.
     */
    fun scheduleDailyAdvice() {
        val now = ZonedDateTime.now()
        var next = now
            .withHour(10)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        if (!next.isAfter(now)) {
            next = next.plusDays(1)
        }

        val delay = Duration.between(now, next).toMillis()

        val workRequest = PeriodicWorkRequestBuilder<AdviceNotificationWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ADVICE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}