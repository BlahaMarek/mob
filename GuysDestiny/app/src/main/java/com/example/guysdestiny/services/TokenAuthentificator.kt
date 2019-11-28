package com.example.guysdestiny.services

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.guysdestiny.R
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.user.RefreshRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Call
import retrofit2.Callback

class TokenAuthentificator(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        if ( response.code()==401)
        {
            var preferences: SharedPreferences = context.getSharedPreferences("guysdestiny", Context.MODE_PRIVATE)
            var userAccessToken: String = preferences.getString("access", "")!!

            if (!response.request().header("Authorization").equals("Bearer $userAccessToken")){
                return null
            }
            var refreshReq: RefreshRequest = RefreshRequest()
            refreshReq.uid = preferences.getString("uid", "")!!
            refreshReq.refresh = preferences.getString("refresh", "")!!

            //TODO skontrolovat ci to funguje (moze fojst k chybe ze sa to vykonava synchronne na jedne threade)
            val call = APIService.create(context).userRefresh(refreshReq).execute()

            if (call.isSuccessful){
                userAccessToken=call.body()!!.access
                preferences.edit().putString("refresh", call.body()!!.refresh).apply()
                preferences.edit().putString("access",  call.body()!!.access).apply()

                return response.request().newBuilder()
                    .header("Authorization","Bearer $userAccessToken")
                    .build()
            }


        }
        return null
    }
}