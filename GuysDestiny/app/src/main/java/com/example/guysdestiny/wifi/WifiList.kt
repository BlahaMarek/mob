package com.example.guysdestiny.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guysdestiny.R
import com.example.guysdestiny.services.apiModels.room.WifiListRequest
import com.example.guysdestiny.services.apiModels.room.WifiListResponse
import com.example.guysdestiny.UserViewModel
import com.example.guysdestiny.localDatabase.WifiDatabaseService
import com.example.guysdestiny.services.APIService
import com.example.guysdestiny.services.ConnectionService
import com.example.guysdestiny.services.apiModels.user.LoginResponse
import kotlinx.android.synthetic.main.fragment_wifi_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WifiList : Fragment() {
    private val MY_PERMISSIONS_REQUEST_LOCATION = 1
    private lateinit var viewModel: UserViewModel
    private lateinit var viewModelData: LoginResponse
    val wifisNames = mutableSetOf<String>()
    val regex = Regex("[^a-zA-Z0-9-_.~%]")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = activity?.let { ViewModelProviders.of(it).get(UserViewModel::class.java) }!!

        val userUid = activity!!.intent.extras!!.getString("userUid")!!
        val userAccess = activity!!.intent.extras!!.getString("userAccess")!!
        val userRefresh = activity!!.intent.extras!!.getString("userRefresh")!!

        val logRes: LoginResponse = LoginResponse()
        logRes.uid = userUid
        logRes.access = userAccess
        logRes.refresh = userRefresh
        viewModel.setUser(logRes)
        viewModelData = viewModel.user.value!!

        return inflater.inflate(R.layout.fragment_wifi_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission( activity!!.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
        } else {
            var wifis = ArrayList<WifiListResponse>()

            if (viewModel.roomList.value != null) {
                wifis = ArrayList(viewModel.roomList.value!!)

                recyclerView_wifiList?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = CustomAdapter(wifis, activity!!.applicationContext)
                }
            } else {
                wifiMan(wifis)
            }
        }

        btnContactList.setOnClickListener {
            view.findNavController().navigate(R.id.contactList)
        }
        refreshButton.setOnClickListener{
            val wifis = ArrayList<WifiListResponse>()
            wifiMan(wifis)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun wifiMan(wifis: ArrayList<WifiListResponse>) {
        wifis.add(WifiListResponse("Public", "14:00:00"))
        wifisNames.add("Public")

        val wifiManager = activity!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (wifiManager.connectionInfo.ssid != null) {
            var wifiSSID = wifiManager.connectionInfo.ssid.toString().replace("\"", "")
            wifiSSID = wifiSSID.replace(regex,"_")

            if (!wifisNames.contains(wifiSSID)) {
                wifis.add(WifiListResponse(wifiSSID, "14:00:00"))
                wifisNames.add(wifiSSID)
                viewModel.setCurrentWifi(wifiSSID)
            }

            Log.d("ssid", wifiSSID)
        } else {
            if (wifiManager.connectionInfo.bssid != null) {
                var wifiBSSID = wifiManager.connectionInfo.bssid.toString()
                wifiBSSID = wifiBSSID.replace(regex,"_")

                if (!wifisNames.contains(wifiBSSID)) {
                    wifis.add(WifiListResponse(wifiBSSID, "14:00:00"))
                    wifisNames.add(wifiBSSID)
                    viewModel.setCurrentWifi(wifiBSSID)
                }
                Log.d("bssid", wifiBSSID)
            }
        }

        getRoomList(wifis)
    }

    fun getRoomList(wifis: ArrayList<WifiListResponse>) {
        // ak pouzivatel nema pripojenie na net, nemusime volat API na server, ale iba vytiahneme veci z lokalnej databazy
        if(!ConnectionService().isConnectedToNetwork(activity!!.applicationContext))
        {
            Toast.makeText(
                context,
                "Nie ste pripojený k internetu, preto údaje nemusia byť aktuálne",
                Toast.LENGTH_SHORT
            ).show()
            getDataFromDatabase(wifis)
            wifisNames.clear()
        } else {
            getDataFromDatabase(wifis)
            val request = WifiListRequest()
            request.uid = viewModelData.uid
            val call: Call<List<WifiListResponse>> = APIService.create(activity!!.applicationContext).getWifiList(request)
            call.enqueue(object : Callback<List<WifiListResponse>> {
                override fun onFailure(call: Call<List<WifiListResponse>>, t: Throwable) {
                    Log.d("badRequest", t.message.toString())
                }

                override fun onResponse(call: Call<List<WifiListResponse>>, response: Response<List<WifiListResponse>>) {
                    if ( response.body() != null) {
                        val dbHandler = WifiDatabaseService(activity!!.applicationContext)
                        val res: List<WifiListResponse> = response.body()!!
                        if(res.count() > 0)
                        {
                            dbHandler.addWifis(res)
                        }
                        for(item in res){
                            var ssid = item.roomid
                            ssid = ssid.replace(regex,"_")

                            if (!wifisNames.contains(ssid)) {
                                wifisNames.add(ssid)
                                wifis.add(WifiListResponse(ssid, "14:00:00"))
                            }
                        }
                    }
                    viewModel.setRoomtList(wifis)
                    wifisNames.clear()
                    recyclerView_wifiList?.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = CustomAdapter(wifis, activity!!.applicationContext)
                    }
                }
            })
        }
    }

    fun getDataFromDatabase(wifis: ArrayList<WifiListResponse>) {
        val dbHandler = WifiDatabaseService(activity!!.applicationContext)
        val wifisFromLocalDb = dbHandler.getWifis()
        for(item in wifisFromLocalDb){
            var ssid = item.roomid
            ssid = ssid.replace(regex,"_")

            if (!wifisNames.contains(ssid)) {
                wifisNames.add(ssid)
                wifis.add(WifiListResponse(ssid, "14:00:00"))
            }
        }
        viewModel.setRoomtList(wifis)
        recyclerView_wifiList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CustomAdapter(wifis, activity!!.applicationContext)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("perDone", "permission for Location")
                    var wifis = ArrayList<WifiListResponse>()

                    if (viewModel.roomList.value != null) {
                        wifis = ArrayList(viewModel.roomList.value!!)
                        recyclerView_wifiList?.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = CustomAdapter(wifis, activity!!.applicationContext)
                        }
                    } else {
                        wifiMan(wifis)
                    }
                } else {
                    Log.d("location", "no permission for Location")
                }
                return
            }
            else -> {
            }
        }
    }
}
