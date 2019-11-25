package com.example.guysdestiny.services.apiModels.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WifiListResponse  {
    @SerializedName("roomid")
    var roomid: String = ""
    @SerializedName("time")
    var time: String = ""
}