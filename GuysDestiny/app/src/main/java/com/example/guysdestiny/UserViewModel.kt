package com.example.guysdestiny

import android.util.Log
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    init {
        Log.i("ViewModel", "UserViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }
}