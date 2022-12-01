package com.amalwin.favdishapplication.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.amalwin.favdishapplication.R
import com.amalwin.favdishapplication.views.activities.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val inputData = inputData.getString("Input Key")
        Log.i("Notify Worker", "Input Data : $inputData")

        sendNotification()
        return Result.success(generateOutputData())
    }

    private fun generateOutputData(): Data =
        Data.Builder().putString("Output_Key", "Output Value").build()

    private fun generateDateFormat(timeInMillis: Long): String {
        val simpleDateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return simpleDateFormatter.format(calendar.time)
    }

    private fun sendNotification() {
        val notification_id = 0
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("NOTIFICATION_ID", notification_id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val titleNotification = applicationContext.getString(R.string.notification_title)
        val subTitleNotification = applicationContext.getString(R.string.notification_subtitle)
        val bitmap = applicationContext.vectorToBitmap(R.mipmap.ic_launcher_round)
        val bigPictureStyle =
            NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext, "CHANNEL1")
            .setContentTitle(titleNotification)
            .setContentText(subTitleNotification)
            .setSmallIcon(R.drawable.ic_all_dish)
            .setLargeIcon(bitmap)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setStyle(bigPictureStyle)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId("CHANNEL1")
            // Setup the ringtone for notification
            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            val channel =
                NotificationChannel("CHANNEL1", "Notification", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 600, 500, 400, 300, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notification_id, notification.build())
    }

    private fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
        // Get the drawable from vector image
        val drawable = AppCompatResources.getDrawable(this, drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    companion object {
        fun logger(message: String) = Log.i("Notify Worker", message)
    }


}