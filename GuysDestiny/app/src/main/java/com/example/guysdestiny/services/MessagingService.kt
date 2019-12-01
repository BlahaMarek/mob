package com.example.guysdestiny.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.guysdestiny.MainActivity
import com.example.guysdestiny.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class MessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    val TAG = "ServiceFirebase"
    private val ADMIN_CHANNEL_ID = "GuysDestiny"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.let { message ->
            Log.i(TAG, "From: " + remoteMessage!!.from)
            Log.i(TAG, "Notification Message Body: " + remoteMessage.notification!!.body)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = ADMIN_CHANNEL_ID
                val descriptionText = "GuysDestiny"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(ADMIN_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            var builder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage!!.from)
                .setContentText(remoteMessage.notification!!.body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(0, builder.build())
            }

        }
    }



    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, token)
    }

}