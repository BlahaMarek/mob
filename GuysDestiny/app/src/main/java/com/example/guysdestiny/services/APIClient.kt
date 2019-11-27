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
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class APIClient {
    var BASE_URL = "http://zadanie.mpage.sk/"


    fun loginUser(login: LoginRequest) {

        val call: Call<LoginResponse> = prepareRetrofit(false, "").userLogin(login)

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

        val call: Call<LoginResponse> = prepareRetrofit(false, "").userCreate(login)

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

        val call: Call<LoginResponse> = prepareRetrofit(false, "").userRefresh(refresh)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("user refreshed", response.code().toString())
            }
        })
    }


    fun fidUser(fid: UserFidRequest, TOKEN: String) {

        val call: Call<ResponseBody> = prepareRetrofit(true, TOKEN).userFid(fid)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("user refreshed", response.code().toString())
            }
        })

    }

    fun getRoomList(request: WifiListRequest, TOKEN: String) {

        val call: Call<List<WifiListResponse>> = prepareRetrofit(true, TOKEN).getWifiList(request)

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

    fun getRoomListMessages(request: ReadRequest, TOKEN: String) {

        val call: Call<List<ReadResponse>> = prepareRetrofit(true, TOKEN).readWifiListMessages(request)

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

    fun postRoomListMessages(request: MessageRequest, TOKEN: String) {

        val call: Call<ResponseBody> = prepareRetrofit(true, TOKEN).postMessageWifiList(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("user refreshed", response.code().toString())
            }
        })
    }

    fun getContactList(request: ContactListRequest, TOKEN: String) {

        val call: Call<List<ContactListResponse>> = prepareRetrofit(true, TOKEN).getContactList(request)

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

    fun getContactListMessages(request: ContactReadRequest, TOKEN: String) {

        val call: Call<List<ContactReadResponse>> = prepareRetrofit(true, TOKEN).readContactListMessages(request)

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


    fun postContactListMessages(request: ContactMessageRequest, TOKEN: String) {
        val call: Call<ResponseBody> = prepareRetrofit(true, TOKEN).postMessageContactList(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("badRequest", t.message.toString())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("user refreshed", response.code().toString())
            }
        })

    }

    fun prepareRetrofit(needAuth: Boolean, TOKEN: String): APIService {
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