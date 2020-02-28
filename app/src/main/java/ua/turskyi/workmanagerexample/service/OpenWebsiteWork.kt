package ua.turskyi.workmanagerexample.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.work.ListenableWorker.Result.success
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import ua.turskyi.workmanagerexample.AdBlockerApplication
import ua.turskyi.workmanagerexample.R
import ua.turskyi.workmanagerexample.data.Constants
import ua.turskyi.workmanagerexample.util.getHour
import ua.turskyi.workmanagerexample.util.getMinute
import ua.turskyi.workmanagerexample.util.vibratePhone
import java.util.*
import java.util.concurrent.TimeUnit

class OpenWebsiteWork(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val id = inputData.getLong(Constants.WEBSITE_OPENING_ID, 1).toInt()
        openWebsite(id)
        val currentTime = System.currentTimeMillis()
        val dueTime = Calendar.getInstance()
        dueTime.set(Calendar.HOUR_OF_DAY,  getHour(context = applicationContext))
        dueTime.set(Calendar.MINUTE, getMinute(context = applicationContext) + 1)
        dueTime.set(Calendar.SECOND, 0)
        if (dueTime.before(currentTime)) {
            dueTime.add(Calendar.MINUTE, 1)
        }

        val timeDiff = dueTime.timeInMillis - currentTime
        val intervalWorkRequest = OneTimeWorkRequest.Builder(OpenWebsiteWork::class.java)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag("TAG_WEBSITE")
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueue(intervalWorkRequest)
        return success()
    }

    private fun openWebsite(id: Int) {
        vibratePhone(applicationContext)
        val intentBrowser = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                applicationContext.getString(
                    R.string.website_link,
                    AdBlockerApplication.prefs?.mModel, AdBlockerApplication.prefs?.mVersion
                )
            )
        )
        intentBrowser.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intentBrowser.putExtra(Constants.NOTIFICATION_ID, id)

        val pendingIntentBrowser: PendingIntent = PendingIntent.getActivity(
            applicationContext, 3,
            intentBrowser, 0
        )
        pendingIntentBrowser.send()
    }
}