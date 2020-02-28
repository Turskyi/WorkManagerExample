package ua.turskyi.workmanagerexample.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import ua.turskyi.workmanagerexample.R
import ua.turskyi.workmanagerexample.data.Constants.NOTIFICATION_ID
import ua.turskyi.workmanagerexample.data.Constants.NOTIFICATION_WORK
import ua.turskyi.workmanagerexample.data.Constants.WEBSITE_OPENING_ID
import ua.turskyi.workmanagerexample.data.Constants.WEBSITE_OPENING_WORK
import ua.turskyi.workmanagerexample.service.NotifyWork
import ua.turskyi.workmanagerexample.service.OpenWebsiteWork
import ua.turskyi.workmanagerexample.util.getHour
import ua.turskyi.workmanagerexample.util.getMinute
import java.lang.System.currentTimeMillis
import java.util.Calendar.*
import java.util.concurrent.TimeUnit.MILLISECONDS

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customCalendar = getInstance()
        customCalendar.set(
            getInstance().get(YEAR),
            getInstance().get(MONTH),
            getInstance().get(DAY_OF_MONTH),
            getHour(context = this),
            getMinute(context = this),
            0
        )
        val notificationTime = customCalendar.timeInMillis + (1000 * 60 * 2)
        val websiteOpeningTime = customCalendar.timeInMillis + (1000 * 60 * 1)
        val currentTime = currentTimeMillis()
        val notificationData = Data.Builder().putInt(NOTIFICATION_ID, 2).build()
        val websiteOpeningData = Data.Builder().putInt(WEBSITE_OPENING_ID, 1).build()
        val notificationDelay = notificationTime - currentTime
        val websiteOpeningDelay = websiteOpeningTime - currentTime
        scheduleWebsiteOpening(websiteOpeningDelay * 1, websiteOpeningData)
        scheduleNotification(notificationDelay * 2, notificationData)
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()
        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()
    }

    private fun scheduleWebsiteOpening(delay: Long, data: Data) {
        val webSiteOpeningWork = OneTimeWorkRequest.Builder(OpenWebsiteWork::class.java)
            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()
        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(WEBSITE_OPENING_WORK, REPLACE, webSiteOpeningWork).enqueue()
    }
}
