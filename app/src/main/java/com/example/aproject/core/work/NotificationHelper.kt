package com.example.aproject.core.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import com.example.aproject.MainActivity
import com.example.aproject.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates the notification channel and shows advice notifications.
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val CHANNEL_ID = "advice_channel"
        const val CHANNEL_NAME = "Advice"
    }

    /**
     * Creates the notification channel (required on Android 8.0+).
     * Android 8.0 meets the minimum SDK API Level 26 of the application.
     */
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily advice notifications"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    /**
     * Shows a notification with the given advice. Uses ID 1, replacing any previous advice notification.
     */
    fun showAdviceNotification(adviceText: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_add_advice_button)
            .setContentTitle("Daily Advice")
            .setContentText("$adviceText\n\nMore advices in the app!")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$adviceText\n\nMore advices in the app!")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(1, notification)
    }

    /**
     * Shows a notification inviting the user to open the app. Uses ID 2.
     */
    fun showNoInternetAdviceNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_add_advice_button)
            .setContentTitle("Don't know what awaits you?")
            .setContentText("Open the app and find out your advice for today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(createPendingIntent())
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(2, notification)
    }

    /**
     * Creates a [PendingIntent] that opens [MainActivity] when the notification is tapped.
     */
    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntentCompat.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT,
            false
        )!! //null is not possible (no FLAG_NO_CREATE)
    }
}