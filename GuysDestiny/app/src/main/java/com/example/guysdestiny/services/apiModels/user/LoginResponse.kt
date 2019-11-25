package com.example.guysdestiny.services.apiModels.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse  {
    @SerializedName("uid")
    var uid: String = ""
    @SerializedName("access")
    var access: String = ""
    @SerializedName("refresh")
    var refresh: String = ""
}