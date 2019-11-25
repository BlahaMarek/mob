package com.example.guysdestiny.services

import com.example.guysdestiny.services.apiModels.contact.*
import com.example.guysdestiny.services.apiModels.room.*
import com.example.guysdestiny.services.apiModels.user.LoginRequest
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import com.example.guysdestiny.services.apiModels.user.RefreshRequest
import retrofit2.Call
import retrofit2.http.Body
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
    fun userFid(@Body body: RefreshRequest): okhttp3.Call

    // WIFI
    @POST("room/list.php")
    fun getWifiList(@Body body: WifiListRequest):Call<List<WifiListResponse>>

    @POST("room/message.php")
    fun messageWifiList(@Body body: MessageRequest): okhttp3.Call

    @POST("room/read.php")
    fun readWifiListMessages(@Body body: ReadRequest):Call<List<ReadResponse>>

    // CONTACT
    @POST("contact/list.php")
    fun getContactList(@Body body: ContactListRequest):Call<List<ContactListResponse>>

    @POST("contact/message.php")
    fun messageContactList(@Body body: ContactMessageRequest): okhttp3.Call

    @POST("contact/read.php")
    fun readContactListMessages(@Body body: ContactReadRequest):Call<List<ContactReadResponse>>
}