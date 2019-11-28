package com.example.guysdestiny.services

import android.content.Context
import com.example.guysdestiny.services.apiModels.contact.*
import com.example.guysdestiny.services.apiModels.room.*
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.user.RefreshRequest
import com.example.guysdestiny.services.apiModels.user.UserFidRequest
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



interface APIService {
    // USERS

    @POST("user/login.php")
    fun userLogin(@Body body: LoginRequest): Call<LoginResponse>

    @POST("user/create.php")
    fun userCreate(@Body body: LoginRequest): Call<LoginResponse>

    @POST("user/refresh.php")
    fun userRefresh(@Body body: RefreshRequest): Call<LoginResponse>

    @POST("user/fid.php")
    fun userFid(@Body body: UserFidRequest): Call<ResponseBody>

    // WIFI
    @POST("room/list.php")
    @Headers("ZadanieApiAuth: accept")
    fun getWifiList(@Body body: WifiListRequest):Call<List<WifiListResponse>>

    @POST("room/read.php")
    @Headers("ZadanieApiAuth: accept")
    fun readWifiListMessages(@Body body: ReadRequest):Call<List<ReadResponse>>

    @POST("room/message.php")
    @Headers("ZadanieApiAuth: accept")
    fun postMessageWifiList(@Body body: MessageRequest): Call<ResponseBody>

    // CONTACT
    @POST("contact/list.php")
    @Headers("ZadanieApiAuth: accept")
    fun getContactList(@Body body: ContactListRequest):Call<List<ContactListResponse>>

    @POST("contact/read.php")
    @Headers("ZadanieApiAuth: accept")
    fun readContactListMessages(@Body body: ContactReadRequest):Call<List<ContactReadResponse>>

    @POST("contact/message.php")
    @Headers("ZadanieApiAuth: accept")
    fun postMessageContactList(@Body body: ContactMessageRequest): Call<ResponseBody>

    companion object {

        fun create(context: Context): APIService {

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .authenticator(TokenAuthentificator(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://zadanie.mpage.sk/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}