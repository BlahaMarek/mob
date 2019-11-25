package com.example.guysdestiny.services.apiModels.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//vrati sa Login response - bude treba pouzivat nasledne ten
class RefreshRequest {
    var uid: String = ""
    var refresh: String = ""
    var api_key: String = ""
}