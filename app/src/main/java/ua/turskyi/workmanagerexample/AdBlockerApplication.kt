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

class AdBlockerApplication : Application() {
    companion object {
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
        createNotificationChannel()
        getUserData()
    }

    private fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            val descriptionText = getString(R.string.channel_description)
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL, Constants.NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = descriptionText
                lightColor = Color.RED
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(100)
                setSound(defaultSoundUri, audioAttributes)
            }
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