package com.example.guysdestiny.services.apiModels.room

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReadResponse  {
    @SerializedName("uid")
    var uid: String = ""
    @SerializedName("roomid")
    var roomid: String = ""
    @SerializedName("message")
    var message: String = ""
    @SerializedName("time")
    var time: String = ""
    @SerializedName("name")
    var name: String = ""
}