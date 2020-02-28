package ua.turskyi.workmanagerexample.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.ListenableWorker.Result.success
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import ua.turskyi.workmanagerexample.AdBlockerApplication
import ua.turskyi.workmanagerexample.R
import ua.turskyi.workmanagerexample.data.Constants.NOTIFICATION_CHANNEL
import ua.turskyi.workmanagerexample.data.Constants.NOTIFICATION_ID
import ua.turskyi.workmanagerexample.data.Constants.NOTIFICATION_NAME
import ua.turskyi.workmanagerexample.util.getHour
import ua.turskyi.workmanagerexample.util.getMinute
import ua.turskyi.workmanagerexample.util.vectorToBitmap
import ua.turskyi.workmanagerexample.view.MainActivity
import java.util.*
import java.util.concurrent.TimeUnit

class NotifyWork(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID, 2).toInt()
        sendNotification(id)
        val currentTime = System.currentTimeMillis()
        val dueTime = Calendar.getInstance()
        dueTime.set(Calendar.HOUR_OF_DAY,  getHour(context = applicationContext))
        dueTime.set(Calendar.MINUTE, getMinute(context = applicationContext) + 2)
        dueTime.set(Calendar.SECOND, 0)
        if (dueTime.before(currentTime)) {
            dueTime.add(Calendar.MINUTE, 2)
        }

        val timeDiff = dueTime.timeInMillis - currentTime
        val intervalWorkRequest =
            OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag("TAG_NOTIFICATION")
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(intervalWorkRequest)
        return success()
    }

    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_black)
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subtitleNotification = applicationContext.getString(R.string.notification_content_text)
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(titleNotification)
            .setContentText(subtitleNotification)
            .setDefaults(DEFAULT_ALL)
            .setContentIntent(openWebsite())
            .setAutoCancel(true)
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(applicationContext.getString(R.string.notification_title))
        notificationBuilder.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL)
        }
        notificationManager.notify(id, notificationBuilder.build())
    }

    private fun openWebsite(): PendingIntent {
        val intentBrowser = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                applicationContext.getString(
                    R.string.website_link,
                    AdBlockerApplication.prefs?.mModel,
                    AdBlockerApplication.prefs?.mVersion
                )
            )
        )
        return getActivity(
            applicationContext, 3,
            intentBrowser, 0
        )
    }
}