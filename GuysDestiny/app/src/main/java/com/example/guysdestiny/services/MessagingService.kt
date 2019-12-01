package com.example.guysdestiny.services

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    val TAG = "ServiceFirebase"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.let { message ->
            Log.i(TAG, "From: " + remoteMessage!!.from)
            Log.i(TAG, "Notification Message Body: " + remoteMessage.notification!!.body)
            Log.i(TAG, message.getData().get("message"))

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, token)
    }

}