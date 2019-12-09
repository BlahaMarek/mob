package com.example.guysdestiny.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.guysdestiny.services.apiModels.NotificationBody
import com.example.guysdestiny.services.apiModels.NotificationRequest
import com.example.guysdestiny.services.apiModels.contact.ContactMessageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.fragment_chat.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    lateinit var preferences: SharedPreferences
    var PREF_NAME = "guysdestiny"


    val TAG = "ServiceFirebase"
    private val ADMIN_CHANNEL_ID = "GuysDestiny"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage?.let { message ->
            Log.i(TAG, "From: " + remoteMessage!!.from)
            Log.i(TAG, "Notification Message Body: " + remoteMessage.notification!!.body)
            preferences =
                getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            if (remoteMessage.notification!!.title.equals(
                    preferences.getString( "login","")!!) || preferences.getString("uid", "").equals("")
            ) {
                return
            }

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
                .setContentTitle(remoteMessage!!.notification!!.title)
                .setContentText("Prijali ste novu spravu")
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
    }

    fun sendNotification(token: String, topics: String, message: String, from: String) {

        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=AIzaSyAZNSawrn4sAKhm2waolIJLrIFbJtaALMA")
            chain.proceed(newRequest.build())
        }.build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var body = NotificationRequest()
        var notif = NotificationBody()

        notif.message = message
        notif.title = from

        body.notification = notif

        if (topics.equals("id")) {
            body.to = token
        } else {
            body.to = "/topics/$topics"
        }

        val call: Call<ResponseBody> =
            retrofit.create(APIService::class.java).sendNotification(body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("reqNotFail", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("reqNotRes", response.toString())
            }
        })
    }
}