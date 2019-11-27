package com.example.guysdestiny.services

import android.util.Log
import com.example.guysdestiny.services.apiModels.contact.*
import com.example.guysdestiny.services.apiModels.room.*
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.user.RefreshRequest
import com.example.guysdestiny.services.apiModels.user.UserFidRequest
import com.example.guysdestiny.wifi.WifiList
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class APIClient {
    var BASE_URL = "http://zadanie.mpage.sk/"
    var TOKEN = "718e52cd1b15312d88d39125256b15264323b43f"

    fun loginUser(login: LoginRequest) {

        val call: Call<LoginResponse> = prepareRetrofit(false).userLogin(login)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("user logged", response.code().toString())
            }
        })
    }

    fun createUser(login: LoginRequest) {

        val call: Call<LoginResponse> = prepareRetrofit(false).userCreate(login)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("user created", response.code().toString())
            }
        })
    }

    fun refreshUser(refresh: RefreshRequest) {

        val call: Call<LoginResponse> = prepareRetrofit(false).userRefresh(refresh)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("user refreshed", response.code().toString())
            }
        })
    }

    // TODO spravit bez response
    fun fidUser(fid: UserFidRequest) {

        val call: okhttp3.Call = prepareRetrofit(false).userFid(fid)
    }

    fun getRoomList(request: WifiListRequest) {

        val call: Call<List<WifiListResponse>> = prepareRetrofit(true).getWifiList(request)

        call.enqueue(object : Callback<List<WifiListResponse>> {
            override fun onFailure(call: Call<List<WifiListResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<WifiListResponse>>,
                response: Response<List<WifiListResponse>>
            ) {
                Log.d("goodRequest", response.code().toString())
            }
        })
    }

    fun getRoomListMessages(request: ReadRequest) {

        val call: Call<List<ReadResponse>> = prepareRetrofit(true).readWifiListMessages(request)

        call.enqueue(object : Callback<List<ReadResponse>> {
            override fun onFailure(call: Call<List<ReadResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ReadResponse>>,
                response: Response<List<ReadResponse>>
            ) {
                Log.d("goodRequest", response.code().toString())
            }
        })
    }

    // TODO spravit bez response
    fun postRoomListMessages(request: MessageRequest) {

        val call: okhttp3.Call = prepareRetrofit(true).postMessageWifiList(request)
    }

    fun getContactList(request: ContactListRequest) {

        val call: Call<List<ContactListResponse>> = prepareRetrofit(true).getContactList(request)

        call.enqueue(object : Callback<List<ContactListResponse>> {
            override fun onFailure(call: Call<List<ContactListResponse>>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ContactListResponse>>,
                response: Response<List<ContactListResponse>>
            ) {
                Log.d("goodRequest", response.code().toString())
            }
        })
    }

    fun getContactListMessages(request: ContactReadRequest) {

        val call: Call<List<ContactReadResponse>> = prepareRetrofit(true).readContactListMessages(request)

        call.enqueue(object : Callback<List<ContactReadResponse>> {
            override fun onFailure(call: Call<List<ContactReadResponse>>, t: Throwable) {

                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ContactReadResponse>>,
                response: Response<List<ContactReadResponse>>
            ) {
                    Log.d("goodRequest", response.code().toString())
            }
        })
    }

    // TODO spravit bez response
    fun postContactListMessages(request: ContactMessageRequest) {
        val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $TOKEN").build()
            chain.proceed(newRequest)
        }.build()

        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(APIService::class.java)
        val call: okhttp3.Call = apiService.postMessageContactList(request)

    }

    fun prepareRetrofit(needAuth: Boolean): APIService {
        var client: OkHttpClient
        if (needAuth) {
            client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer $TOKEN").build()
                chain.proceed(newRequest)
            }.build()
        }
        else {
            client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest = chain.request().newBuilder().build()
                chain.proceed(newRequest)
            }.build()
        }

        val retrofit = Retrofit.Builder().client(client).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(APIService::class.java)
    }
}