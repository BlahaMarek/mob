package com.example.guysdestiny

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.apiModels.user.UserFidRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {
    lateinit var preferences: SharedPreferences
    private lateinit var viewModel: UserViewModel
    val TAG = "ServiceFirebase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result?.token

                val msg = token.toString()

                var req = UserFidRequest()
                req.fid = msg
                req.uid = viewModel.user.value!!.uid

                val notify: Call<ResponseBody> = APIService.create(baseContext).userFid(req)

                notify.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("badRequest", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Log.d("notify send", response.code().toString())
                    }
                })
            })

        FirebaseMessaging.getInstance().subscribeToTopic("Public")
            .addOnCompleteListener { task ->
                var msg = "Ide to"
                if (!task.isSuccessful) {
                    msg = "Nejde to"
                }
                Log.d("Message", msg)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            preferences = this.getSharedPreferences("guysdestiny", Context.MODE_PRIVATE)
            preferences.edit().clear().apply()

            FirebaseMessaging.getInstance().unsubscribeFromTopic("Public")
            FirebaseMessaging.getInstance().unsubscribeFromTopic(viewModel.currentWifi.value!!)

            Thread(Runnable {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()


            val intent = Intent(this, LoginActivity::class.java)
            baseContext.deleteDatabase("GuysDestinyDatabase")
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
