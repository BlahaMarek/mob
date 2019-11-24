package com.example.guysdestiny.services.wifiService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface WifiService {
    @Headers("Accept: application/json", "Cache-Control: no-cache", "Content-Type: application/javascript")
    @POST("/room/list.php")
    fun getWifiList(@Body wifiListRequestData: WifiListRequest):Call<WifiListResponse>
}