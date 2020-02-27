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
import ua.turskyi.workmanagerexample.service.NotifyWork
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
        val notificationTime = customCalendar.timeInMillis + (1000 * 60 * 1)
        val currentTime = currentTimeMillis()
        val data = Data.Builder().putInt(NOTIFICATION_ID, 2).build()
        val delay = notificationTime - currentTime
        scheduleNotification(delay * 1, data)
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()
        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()
    }
}
