package com.example.guysdestiny.services

import android.nfc.Tag
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        if(p0?.data != null){
            Log.d("Nika", p0.data.toString())
        }

        if(p0?.notification != null){
            Log.d("Nika", p0.notification.toString())
        }
    }

    override fun onNewToken(p0: String) {
        Log.d("Nika:", p0)
    }
}