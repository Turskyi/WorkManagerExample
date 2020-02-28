package ua.turskyi.workmanagerexample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import ua.turskyi.workmanagerexample.data.Constants
import ua.turskyi.workmanagerexample.data.Prefs
import ua.turskyi.workmanagerexample.util.getAndroidVersion
import ua.turskyi.workmanagerexample.util.getDeviceName

/**
 * @Description
 * Every 10 minutes the app opens a browser with an arbitrary website, and every 20 minutes sends a
 * push notification with arbitrary text by clicking on which opens an arbitrary website.
 * After restarting the phone app should work as before.
 * When open the application should be displayed a blank screen.
 */
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
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val name = getString(R.string.channel_title)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL, name,
                importance
            ).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100)
                setSound(defaultSoundUri, audioAttributes)
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