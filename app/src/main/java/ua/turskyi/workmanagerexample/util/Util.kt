package ua.turskyi.workmanagerexample.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.TimePicker
import java.util.*

fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
    val drawable = getDrawable(drawableId) ?: return null
    val bitmap = createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    ) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun getHour(context: Context): Int {
    @Suppress("DEPRECATION")
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> TimePicker(context).hour
        else -> TimePicker(context).currentHour
    }
}

fun getMinute(context: Context): Int {
    @Suppress("DEPRECATION")
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> TimePicker(context).minute
        else -> TimePicker(context).currentMinute
    }
}

fun vibratePhone(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(100)
    }
}

/**
 * @Description
 * this method was taken from here
 * https://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically
 */
fun getDeviceName(): String? {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.toLowerCase(Locale.getDefault()).startsWith(manufacturer.toLowerCase(Locale.getDefault()))) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + "(" + model + ")"
    }
}


private fun capitalize(s: String?): String {
    if (s == null || s.isEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        Character.toUpperCase(first).toString() + s.substring(1)
    }
}

/**
 * @Description
 * this method was taken from here
 * https://stackoverflow.com/a/25828574/10636137
 */
fun getAndroidVersion(): String? {
    val release = Build.VERSION.RELEASE
    val sdkVersion = Build.VERSION.SDK_INT
    return "$sdkVersion($release)"
}