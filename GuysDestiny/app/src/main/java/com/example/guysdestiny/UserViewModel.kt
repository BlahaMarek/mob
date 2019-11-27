package com.example.guysdestiny

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guysdestiny.services.apiModels.user.LoginResponse

class UserViewModel : ViewModel() {
    val user = MutableLiveData<LoginResponse>()

    fun setUser(user: LoginResponse) {
        this.user.value = user
    }
}