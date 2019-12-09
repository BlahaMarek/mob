package com.example.guysdestiny.services

import android.content.Context
import android.net.ConnectivityManager

class ConnectionService {
    fun isConnectedToNetwork(context : Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(connectivityManager.activeNetwork != null)
        {
            return true
        }
        return false
    }
}