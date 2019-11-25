package com.example.guysdestiny.services.apiModels.contact

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactReadResponse  {
    @SerializedName("uid")
    var uid: String = ""
    @SerializedName("contact")
    var contact: String = ""
    @SerializedName("message")
    var message: String = ""
    @SerializedName("time")
    var time: String = ""
    @SerializedName("uid_name")
    var uid_name: String = ""
    @SerializedName("contact_name")
    var contact_name: String = ""
    @SerializedName("uid_fid")
    var uid_fid: String = ""
    @SerializedName("contact_fid")
    var contact_fid: String = ""
}