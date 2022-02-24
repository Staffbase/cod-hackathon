package dev.staffbase.cod

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.RemoteMessage
import com.microsoft.windowsazure.messaging.notificationhubs.NotificationListener


class CustomNotificationListener : NotificationListener {

    override fun onPushNotificationReceived(context: Context?, message: RemoteMessage?) {
        val notification: RemoteMessage.Notification? = message!!.notification
        val title: String = notification?.title ?: "emptyTitle"
        val body: String = notification?.body ?: "emptyBody"
        val data = message.data

        Log.d("TAG", "Title: ${notification?.title ?: ""}")

        if (context == null) {
            Log.d("TAG", "Context is null")
            return
        }



        val builder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(androidx.appcompat.R.drawable.abc_btn_check_material)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "description",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // send notification
        val notification1: Notification = builder.build()
        try {
            notificationManager.notify(1, notification1)
        } catch (var6: SecurityException) {
            notification1.defaults = 5
            notificationManager.notify(1, notification1)
        }
    }
}