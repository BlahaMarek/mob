package com.example.guysdestiny

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.guysdestiny.services.apiModels.contact.ContactListResponse
import com.example.guysdestiny.services.apiModels.contact.ContactReadResponse
import com.example.guysdestiny.services.apiModels.room.ReadResponse
import com.example.guysdestiny.services.apiModels.room.WifiListResponse
import com.example.guysdestiny.services.apiModels.user.LoginResponse

class UserViewModel : ViewModel() {
    val user = MutableLiveData<LoginResponse>()
    val userToWriteFID = MutableLiveData<String>()
    val currentWifi = MutableLiveData<String>()
    val contactList = MutableLiveData<List<ContactListResponse>>()
    val contactRead = MutableLiveData<List<ContactReadResponse>>()
    val roomList    = MutableLiveData<List<WifiListResponse>>()
    val roomRead    = MutableLiveData<ArrayList<ReadResponse>>()

    fun setUser(user: LoginResponse) {
        this.user.value = user
    }

    fun setUserToWriteFID(fid: String) {
        this.userToWriteFID.value = fid
    }
    fun setCurrentWifi(wifi: String){
        this.currentWifi.value = wifi
    }

    fun setContactList(list: List<ContactListResponse>) {
        this.contactList.value = list
    }

    fun setContactRead(list: List<ContactReadResponse>) {
        this.contactRead.value = list
    }

    fun setRoomtList(list: List<WifiListResponse>) {
        this.roomList.value = list
    }

    fun setRoomtRead(list: ArrayList<ReadResponse>) {
        this.roomRead.value = list
    }


}