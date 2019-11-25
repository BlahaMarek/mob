package com.example.guysdestiny

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.guysdestiny.models.User

class UserViewModel : ViewModel() {
    private var user: User

    init {
        Log.i("ViewModel", "UserViewModel created")
        this.user = User("", "", "", "")
    }

    fun getUser(): User {
        return this.user
    }

    fun setUser(user: User) {
        this.user = user
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}