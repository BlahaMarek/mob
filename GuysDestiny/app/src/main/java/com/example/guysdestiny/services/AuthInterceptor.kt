package com.example.guysdestiny.services

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept","application/json")
            .addHeader("Content-Type","application/json")


        if (chain.request().header("ZadanieApiAuth")?.compareTo("accept")==0) {
            var preferences: SharedPreferences = context.getSharedPreferences("guysdestiny", Context.MODE_PRIVATE)
            val accessToken: String = preferences.getString("access", "")!!
            request.addHeader("Authorization","Bearer $accessToken")
        }
        return chain.proceed(request.build())
    }
}