package com.example.guysdestiny.services

import android.util.Log
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.room.WifiListRequest
import com.example.guysdestiny.services.apiModels.room.WifiListResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class APIClient {
    var BASE_URL = "http://zadanie.mpage.sk/"
    var TOKEN = "e37e46dfb438ace0a5e2737aaf27a19fbf4dfdd6"

    fun loginUser(login: LoginRequest) {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder().build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(APIService::class.java)
        val call: Call<LoginResponse> = apiService.userLogin(login)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse> ) {
                Log.d("user logged", response.code().toString())
            }
        })
    }
    fun createUser(login: LoginRequest) {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder().build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(APIService::class.java)
        val call: Call<LoginResponse> = apiService.userCreate(login)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse> ) {
                Log.d("user created", response.code().toString())
            }
        })
    }


    fun getRoomList(request: WifiListRequest) {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer $TOKEN").build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(APIService::class.java)
        val call: Call<List<WifiListResponse>> = apiService.getWifiList(request)

        call.enqueue(object : Callback<List<WifiListResponse>> {
            override fun onFailure(call: Call<List<WifiListResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }
            override fun onResponse(call: Call<List<WifiListResponse>>, response: Response<List<WifiListResponse>> ) {
                Log.d("goodRequest", response.code().toString())
            }
        })
    }
}