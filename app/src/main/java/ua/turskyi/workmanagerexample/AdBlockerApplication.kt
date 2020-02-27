package ua.turskyi.workmanagerexample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import ua.turskyi.workmanagerexample.data.Prefs
import ua.turskyi.workmanagerexample.util.getAndroidVersion
import ua.turskyi.workmanagerexample.util.getDeviceName

class AdBlockerApplication : Application() {
    companion object {
        lateinit var instance: AdBlockerApplication
        var prefs: Prefs? = null
    }

    private var model: String? = ""
    private var version: String? = ""

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
        createNotificationChannel()
        getUserData()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_title)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                packageName, name,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getUserData() {
        if (prefs?.mModel == ""){
            prefs?.mModel = getDeviceName()
        } else if (prefs?.mVersion == ""){
            prefs?.mVersion = getAndroidVersion()
        }
    }
}