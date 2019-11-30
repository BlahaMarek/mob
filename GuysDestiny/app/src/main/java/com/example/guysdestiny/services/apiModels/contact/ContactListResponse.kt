package com.example.guysdestiny.services.apiModels.contact

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactListResponse  (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = ""
)