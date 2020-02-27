package ua.turskyi.workmanagerexample.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Prefs (context: Context) {

    companion object{
        const val PREFS_FILENAME = "prefs"
        const val SHARED_PREFS_ANDROID_VERSION = "sharedPrefsAndroidVersion"
        const val SHARED_PREFS_PHONE_MODEL = "sharedPrefsPhoneModel"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

    var mModel: String?
        get() = prefs.getString(SHARED_PREFS_ANDROID_VERSION, "")
        set(value) = prefs.edit().putString(SHARED_PREFS_ANDROID_VERSION, value).apply()

    var mVersion: String?
        get() = prefs.getString(SHARED_PREFS_PHONE_MODEL, "")
        set(value) = prefs.edit().putString(SHARED_PREFS_PHONE_MODEL, value).apply()
}